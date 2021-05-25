package com.tokopedia.common_electronic_money.util

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.hexStringToByteArray
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.toHex
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

/**
 * Author errysuprayogi on 15,May,2020
 */
class CardUtils {

    companion object {

        private const val BRIZZI_TAG = "BRIZZI"
        private const val EMONEY_TAG = "EMONEY"
        private const val PREFIX_SELECT_COMMAND = "00A4040008"
        private const val EMONEY_AID = "0000000000000001"
        private const val SUCCESSFULLY_EXECUTED = "9000"
        private const val BRIZZI_SUCCESSFULLY_EXECUTED = "9100"
        private val BRIZZI_APDU_COMMAND = byteArrayOf(
                0x90.toByte(),  // CLA Class
                0x5A.toByte(),  // INS Instruction
                0x00.toByte(),  // P1  Parameter 1
                0x00.toByte(),  // P2  Parameter 2
                0x03.toByte(), // // LE Data Field
                0x01.toByte(),
                0x00.toByte(),
                0x00.toByte(),
                0x00.toByte()
        )


        @JvmStatic
        fun isEmoneyCard(intent: Intent): Boolean {
            try {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val isoDep = IsoDep.get(tag)
                    isoDep.connect()
                    val bytes = isoDep.transceive(hexStringToByteArray(EMONEY_SELECT_COMMAND))
                    isoDep.close()
                    return toHex(bytes) == EMONEY_SUCCESSFULLY_EXECUTED
                }
            } catch (e: Exception) {
                ServerLogger.log(Priority.P2, EMONEY_TAG, mapOf("err" to "ERROR_EMONEY_CHECKER: ${e.message}"))
                e.printStackTrace()
            }
            return false
        }

        @JvmStatic
        fun isBrizziCard(intent: Intent): Boolean {
            try {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val isoDep = IsoDep.get(tag)
                    isoDep.connect()
                    val bytes = isoDep.transceive(BRIZZI_APDU_COMMAND)
                    isoDep.close()
                    return toHex(bytes) == BRIZZI_SUCCESSFULLY_EXECUTED
                }
            } catch (e: Exception) {
                ServerLogger.log(Priority.P2, BRIZZI_TAG, mapOf("err" to "ERROR_BRIZZI_CHECKER: ${e.message}"))
                e.printStackTrace()
            }
            return false
        }
    }
}