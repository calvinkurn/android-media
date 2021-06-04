package com.tokopedia.common_electronic_money.util

import java.math.BigInteger

/**
 * Created by Rizky on 15/05/18.
 */
class NFCUtils {

    companion object {

        private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

        @JvmStatic
        fun hexStringToByteArray(str: String): ByteArray {
            return BigInteger(str, 16).toByteArray()
        }

        @JvmStatic
        fun stringToByteArray(str: String): ByteArray {
            return str.toByteArray()
        }

        @JvmStatic
        fun toHex(bytes: ByteArray): String {
            val hexChars = CharArray(bytes.size * 2)
            for (j in bytes.indices) {
                val v = bytes[j].toInt() and 0xFF
                hexChars[j * 2] = HEX_CHARS[v.ushr(4)]
                hexChars[j * 2 + 1] = HEX_CHARS[v and 0x0F]
            }
            return String(hexChars)
        }

        @JvmStatic
        fun formatCardUID(cardNumber: String): String {
            return cardNumber.substring(0, 4) + " - " + cardNumber.substring(4, 8) + " - " +
                    cardNumber.substring(8, 12) + " - " + cardNumber.substring(12, 16)
        }

        @JvmStatic
        fun get8Byte(bytes: ByteArray):ByteArray {
            return bytes.sliceArray(0..7)
        }
    }
}