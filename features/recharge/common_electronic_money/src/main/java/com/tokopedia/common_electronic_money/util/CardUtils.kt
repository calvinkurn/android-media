package com.tokopedia.common_electronic_money.util

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.hexStringToByteArray
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.toHex

/**
 * Author errysuprayogi on 15,May,2020
 */
class CardUtils {

    companion object {

        private const val EMONEY_SELECT_COMMAND = "00A40400080000000000000001"
        private const val EMONEY_SUCCESSFULLY_EXECUTED = "9000"

        @JvmStatic
        fun cardIsEmoney(intent: Intent): Boolean {
            try {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val isoDep = IsoDep.get(tag)
                    isoDep.connect()
                    val bytes = isoDep.transceive(hexStringToByteArray(EMONEY_SELECT_COMMAND))
                    isoDep.close()
                    return toHex(bytes) == EMONEY_SUCCESSFULLY_EXECUTED
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
            return false
        }
    }

}