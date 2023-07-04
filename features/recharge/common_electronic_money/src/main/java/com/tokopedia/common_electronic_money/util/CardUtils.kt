package com.tokopedia.common_electronic_money.util

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.hexStringToByteArray
import com.tokopedia.common_electronic_money.util.NFCUtils.Companion.toHex
import com.tokopedia.config.GlobalConfig

/**
 * Author errysuprayogi on 15,May,2020
 */
class CardUtils {

    companion object {

        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 5000
        private const val PREFIX_SELECT_COMMAND = "00A4040008"
        private const val PREFIX_SELECT_COMMAND_JAKCARD = "00A4040007"
        private const val JAKCARD_AID_PROD = "A0000005714E4A43"
        private const val JAKCARD_AID_STAG = "D3600000030003"
        private const val TAPCASH_AID = "A000424E49100001"
        private const val EMONEY_AID = "0000000000000001"
        private const val SUCCESSFULLY_EXECUTED = "9000"
        private const val SUCCESSFULLY_EXECUTED_BRIZZI = "9100"
        private val BRIZZI_COMMAND = byteArrayOf(
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
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC
                    val bytes = isoDep.transceive(hexStringToByteArray(PREFIX_SELECT_COMMAND + EMONEY_AID))
                    isoDep.close()
                    return toHex(bytes) == SUCCESSFULLY_EXECUTED
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
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
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC
                    val bytes = isoDep.transceive(BRIZZI_COMMAND)
                    isoDep.close()
                    return toHex(bytes) == SUCCESSFULLY_EXECUTED_BRIZZI
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                e.printStackTrace()
            }
            return false
        }

        @JvmStatic
        fun isTapcashCard(intent: Intent): Boolean {
            try {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val isoDep = IsoDep.get(tag)
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC
                    val bytes = isoDep.transceive(hexStringToByteArray(PREFIX_SELECT_COMMAND + TAPCASH_AID))
                    isoDep.close()
                    return toHex(bytes) == SUCCESSFULLY_EXECUTED
                }
            } catch (e: Exception){
                FirebaseCrashlytics.getInstance().recordException(e)
                e.printStackTrace()
            }
            return false
        }

        @JvmStatic
        fun isJakCard(intent: Intent): Boolean {
            try {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val isoDep = IsoDep.get(tag)
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC
                    val selectCommand = PREFIX_SELECT_COMMAND + JAKCARD_AID_PROD
                    val bytes = isoDep.transceive(hexStringToByteArray(selectCommand))
                    isoDep.close()
                    return !NFCUtils.isCommandFailed(bytes)
                }
            } catch (e: Exception){
                FirebaseCrashlytics.getInstance().recordException(e)
            }
            return false
        }

        @JvmStatic
        fun isJakCardDev(intent: Intent): Boolean {
            try {
                val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val isoDep = IsoDep.get(tag)
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC
                    val selectCommand = PREFIX_SELECT_COMMAND_JAKCARD + JAKCARD_AID_STAG
                    val bytes = isoDep.transceive(hexStringToByteArray(selectCommand))
                    isoDep.close()
                    return !NFCUtils.isCommandFailed(bytes)
                }
            } catch (e: Exception){
                FirebaseCrashlytics.getInstance().recordException(e)
            }
            return false
        }


    }
}
