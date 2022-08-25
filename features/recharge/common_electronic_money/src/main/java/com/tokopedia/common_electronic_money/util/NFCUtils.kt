package com.tokopedia.common_electronic_money.util

import android.annotation.SuppressLint
import java.math.BigInteger

/**
 * Created by Rizky on 15/05/18.
 */
class NFCUtils {

    companion object {

        private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
        private const val RADIX_16 = 16
        private const val MULTIPLIER_2 = 2
        private const val CHUCK_2 = 2
        private const val HEX_BIT_COUNT_4 = 4
        private const val HEX_0xFF = 0xFF
        private const val HEX_0x0F = 0xFF

        @JvmStatic
        fun hexStringToByteArray(str: String): ByteArray {
            return BigInteger(str, RADIX_16).toByteArray()
        }

        @JvmStatic
        fun toHex(bytes: ByteArray): String {
            val hexChars = CharArray(bytes.size * MULTIPLIER_2)
            for (j in bytes.indices) {
                val v = bytes[j].toInt() and HEX_0xFF
                hexChars[j * MULTIPLIER_2] = HEX_CHARS[v.ushr(HEX_BIT_COUNT_4)]
                hexChars[j * MULTIPLIER_2 + 1] = HEX_CHARS[v and HEX_0x0F]
            }
            return String(hexChars)
        }

        @JvmStatic
        fun formatCardUID(cardNumber: String): String {
            return cardNumber.substring(0, 4) + " - " + cardNumber.substring(4, 8) + " - " +
                    cardNumber.substring(8, 12) + " - " + cardNumber.substring(12, 16)
        }

        @SuppressLint("Method Call Prohibited")
        @JvmStatic
        fun stringToByteArrayRadix(str: String): ByteArray{
            return str.chunked(CHUCK_2)
                    .map { it.toInt(RADIX_16).toByte() }
                    .toByteArray()
        }
    }
}