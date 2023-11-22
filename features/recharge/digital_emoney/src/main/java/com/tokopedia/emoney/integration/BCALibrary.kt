package com.tokopedia.emoney.integration

import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.tokopedia.emoney.integration.data.JNIResult
import timber.log.Timber
import java.io.IOException
import java.util.*

class BCALibrary {

    var myTag: Tag? = null
    var isSuccessInit: Boolean = false
    init {
        isSuccessInit = try {
            System.loadLibrary("bcabridgelibrary")
            true
        } catch (e: Throwable) {
            Timber.d(e)
            false
        }

    }

    external fun C_BCAVersionDll(): JNIResult

    external fun C_BCAIsMyCard(): JNIResult

    external fun C_BCACheckBalance(): JNIResult

    external fun C_BCASetConfig(strConfig: String): JNIResult

    external fun C_BCAGetConfig(): JNIResult

    external fun C_BCAdataSession_1(strTransactionId: String, ATD: String, strCurrDateTime: String): JNIResult

    external fun C_BCAdataSession_2(responseData: String): JNIResult

    external fun BCATopUp_1(strTransactionId: String, ATD: String, strAccessCardNumber: String, strAccessCode: String, strCurrDateTime: String, lngAmount: Long): JNIResult

    external fun BCATopUp_2(responseData: String): JNIResult

    external fun BCAdataReversal(strTransactionId: String, ATD: String): JNIResult

    external fun BCAlastBCATopUp(): JNIResult

    external fun BCAdataCardInfo(strTransactionId: String, ATD: String): JNIResult


    val HEX_DIGITS = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )

    fun bytes2Hexchar(ba: ByteArray): String {
        val length = ba.size
        val buf = CharArray(length * 2)
        var i = 0
        var j = 0
        var k: Int
        while (i < length) {
            k = ba[i++].toInt()
            buf[j++] = HEX_DIGITS[k ushr 4 and 0x0F]
            buf[j++] = HEX_DIGITS[k and 0x0F]
        }
        return String(buf).uppercase(Locale.getDefault())
    }

    fun hexchar2Bytes(hex: String): ByteArray {
        val len = hex.length
        val buf = ByteArray((len + 1) / 2)
        var i = 0
        var j = 0
        if (len % 2 == 1) {
            buf[j++] = hexchar2Decimal(hex[i++]).toByte()
        }
        while (i < len) {
            buf[j++] = (hexchar2Decimal(hex[i++]) shl 4 or
                hexchar2Decimal(hex[i++])).toByte()
        }
        return buf
    }

    fun hexchar2Decimal(ch: Char): Int {
        if (ch in '0'..'9') return ch.code - '0'.code
        if (ch in 'A'..'F') return ch.code - 'A'.code + 10
        return if (ch in 'a'..'f') ch.code - 'a'.code + 10 else 0
    }

    fun sendContactlessAPDU(cmdByte: String, lenC: Int): String {
        val cmdAPDU: ByteArray = hexchar2Bytes(cmdByte)
        var result: ByteArray? = null
        var RAPDU: String? = ""
        if (myTag == null) {
            return ""
        }
        try {
            val isoDep = IsoDep.get(myTag)
            result = isoDep.transceive(cmdAPDU)
            RAPDU = bytes2Hexchar(result)
        } catch (e: IOException) {
            return ""
        } catch (e: Throwable) {
            return ""
        }
        return RAPDU
    }
}
