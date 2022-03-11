package com.yangyao.putf;

import java.util.Arrays;

public class Putf {

    public static byte[] encode(String value) {
        byte[] buffer = new byte[value.length() * 3];
        char last = 0;
        int pos = 0;
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if ((c & ~0x7F) == 0) {
                buffer[pos++] = (byte) c;
            } else {
                c ^= last;
                if ((c & ~0x7F) == 0) {
                    buffer[pos++] = (byte) (c & 0x7F | 0x80);
                    buffer[pos++] = (byte) 0;
                } else if ((c & ~0x3FFF) == 0) {
                    buffer[pos++] = (byte) (c & 0x7F | 0x80);
                    buffer[pos++] = (byte) (c >>> 7);
                } else {
                    buffer[pos++] = (byte) (c & 0x7F | 0x80);
                    buffer[pos++] = (byte) ((c >>> 7) & 0x7F | 0x80);
                    buffer[pos++] = (byte) (c >>> 14);
                }
            }
            last = value.charAt(i);
        }
        return Arrays.copyOf(buffer, pos);
    }

    public static String decode(byte[] bytes) {
        char[] chars = new char[bytes.length];
        int pos = 0;
        char last = 0;
        for (int i = 0; i < bytes.length; i++) {
            char c = 0;
            if ((bytes[i] & ~0x7F) == 0) {
                c = (char) bytes[i];
            } else {
                int disp = 0;
                while (true) {
                    byte b = bytes[i];
                    c |= (char) ((b & 0x7F) << (disp * 7));
                    if ((b & ~0x7F) == 0) {
                        c ^= last;
                        break;
                    }
                    disp++;
                    i++;
                }
            }
            chars[pos++] = c;
            last = c;
        }
        return new String(chars, 0, pos);
    }

}
