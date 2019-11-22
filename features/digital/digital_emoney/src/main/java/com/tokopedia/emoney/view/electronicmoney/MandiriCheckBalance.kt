package com.tokopedia.emoney.view.electronicmoney

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import com.tokopedia.emoney.NFCUtils
import com.tokopedia.emoney.R
import com.tokopedia.emoney.view.activity.EmoneyCheckBalanceNFCActivity
import com.tokopedia.emoney.viewmodel.EmoneyInquiryBalanceViewModel
import java.io.IOException

class MandiriCheckBalance(val listener: MandiriActionListener) : ElectronicMoney {

    private lateinit var isoDep: IsoDep

    override fun processTagIntent(intent: Intent) {
        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
        if (tag != null) {

            //do something with tagFromIntent
            isoDep = IsoDep.get(tag)

            try {
                isoDep.close()
                isoDep.connect()
                isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC // 5 sec time out

                val commandSelectEMoney = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_SELECT_EMONEY))
                val commandCardAttribute = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_ATTRIBUTE))
                val commandCardInfo = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_INFO))
                val commandLastBalance = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_LAST_BALANCE))

                run {
                    val cardUID = isoDep.tag.id

                    val responseSelectEMoney = NFCUtils.toHex(commandSelectEMoney)
                    val responseCardAttribute = NFCUtils.toHex(commandCardAttribute)
                    val responseCardUID = NFCUtils.toHex(cardUID)
                    val responseCardInfo = NFCUtils.toHex(commandCardInfo)
                    val responseCardLastBalance = NFCUtils.toHex(commandLastBalance)

                    //success scan card e-money
                    if (responseSelectEMoney == COMMAND_SUCCESSFULLY_EXECUTED) {
                        listener.setIssuerId(ISSUER_ID_EMONEY)
                        val mapAttributes = HashMap<String, kotlin.Any>()
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_CARD_ATTRIBUTE] = responseCardAttribute
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_CARD_INFO] = responseCardInfo
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_ISSUER_ID] = EmoneyCheckBalanceNFCActivity.ISSUER_ID_EMONEY
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_CARD_UUID] = responseCardUID
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_LAST_BALANCE] = responseCardLastBalance

                        listener.getInquiryBalanceMandiri(mapAttributes)
                    } else {
                        isoDep.close()
                        listener.onErrorCardNotFound(ISSUER_ID_EMONEY, intent)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                listener.onErrorDefault(R.string.emoney_failed_read_card)
            }
        }
    }

    override fun writeBalanceToCard(intent: Intent, payload: String, id: Int, mapAttributes: HashMap<String, Any>) {
        if (isoDep != null && isoDep.isConnected) {
            try {
                val responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(payload))

                run {
                    if (responseInByte != null) {
                        // to get card payload
                        val response = NFCUtils.toHex(responseInByte)
                        mapAttributes[EmoneyInquiryBalanceViewModel.PARAM_PAYLOAD] = response
                        listener.sendCommandMandiri(id, mapAttributes)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                listener.onErrorDefault(R.string.emoney_update_balance_failed)
            }
        } else {
            listener.onErrorDefault(R.string.emoney_update_balance_failed)
        }
    }

    companion object {
        private const val ISSUER_ID_EMONEY = 1
        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 5000

        //identifier for emoney 0000000000000001
        private const val COMMAND_SELECT_EMONEY = "00A40400080000000000000001"

        private const val COMMAND_CARD_ATTRIBUTE = "00F210000B"
        private const val COMMAND_CARD_INFO = "00B300003F"
        private const val COMMAND_LAST_BALANCE = "00B500000A"
        private const val COMMAND_SUCCESSFULLY_EXECUTED = "9000"
    }
}