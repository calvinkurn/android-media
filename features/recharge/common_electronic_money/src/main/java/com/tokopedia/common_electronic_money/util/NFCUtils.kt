package com.tokopedia.common_electronic_money.util

import android.annotation.SuppressLint
import com.tokopedia.kotlin.extensions.view.isZero
import java.math.BigInteger

/**
 * Created by Rizky on 15/05/18.
 */
class NFCUtils {

    companion object {

        private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
        private const val RADIX_16 = 16
        private const val MULTIPLIER_2 = 2
        private const val CHUNK_2 = 2
        private const val HEX_BIT_COUNT_4 = 4
        private const val HEX_0xFF = 0xFF
        private const val HEX_0x0F = 0x0F
        private const val CARD_SLICE_FROM_0 = 0
        private const val CARD_SLICE_FROM_4 = 4
        private const val CARD_SLICE_FROM_8 = 8
        private const val CARD_SLICE_FROM_12 = 12
        private const val CARD_SLICE_TO_4 = 4
        private const val CARD_SLICE_TO_8 = 8
        private const val CARD_SLICE_TO_12 = 12
        private const val CARD_SLICE_TO_16 = 16

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
            return cardNumber.substring(CARD_SLICE_FROM_0, CARD_SLICE_TO_4) +
                    " - " +
                    cardNumber.substring(CARD_SLICE_FROM_4, CARD_SLICE_TO_8) +
                    " - " +
                    cardNumber.substring(CARD_SLICE_FROM_8, CARD_SLICE_TO_12) +
                    " - " +
                    cardNumber.substring(CARD_SLICE_FROM_12, CARD_SLICE_TO_16)
        }

        @SuppressLint("Method Call Prohibited")
        @JvmStatic
        fun stringToByteArrayRadix(str: String): ByteArray{
            return str.chunked(CHUNK_2)
                    .map { it.toInt(RADIX_16).toByte() }
                    .toByteArray()
        }

        @JvmStatic
        fun isCommandFailed(byteArray: ByteArray): Boolean {
            val len: Int = byteArray.size
            return if (len.isZero()) true
            else ((byteArray[len - 2].compareTo(0x90.toByte())) != 0) ||
                (byteArray[len - 1].compareTo(0x00.toByte()) != 0)
        }
    }
}
