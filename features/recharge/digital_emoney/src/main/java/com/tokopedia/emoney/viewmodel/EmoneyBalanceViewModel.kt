package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryResponse
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class EmoneyBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val issuerId = SingleLiveEvent<Int>()
    val errorCardMessage = SingleLiveEvent<Throwable>()
    val emoneyInquiry = SingleLiveEvent<EmoneyInquiry>()
    val errorInquiryBalance = SingleLiveEvent<Throwable>()

    lateinit var isoDep: IsoDep

    fun processEmoneyTagIntent(isoDep: IsoDep, balanceRawQuery: String, idCard: Int) {
        //do something with tagFromIntent
        if (isoDep != null) {
            run {
                try {
                    this.isoDep = isoDep
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC // 5 sec time out

                    val commandSelectEMoney = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_SELECT_EMONEY))
                    val commandCardAttribute = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_ATTRIBUTE))
                    val commandCardInfo = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_CARD_INFO))
                    val commandLastBalance = isoDep.transceive(NFCUtils.hexStringToByteArray(COMMAND_LAST_BALANCE))
                    val cardUID = isoDep.tag.id

                    val responseSelectEMoney = NFCUtils.toHex(commandSelectEMoney)
                    val responseCardAttribute = NFCUtils.toHex(commandCardAttribute)
                    val responseCardUID = NFCUtils.toHex(cardUID)
                    val responseCardInfo = NFCUtils.toHex(commandCardInfo)
                    val responseCardLastBalance = NFCUtils.toHex(commandLastBalance)

                    //success scan card e-money
                    if (responseSelectEMoney == COMMAND_SUCCESSFULLY_EXECUTED) {
                        issuerId.postValue(ISSUER_ID_EMONEY)
                        val mapAttributes = HashMap<String, Any>()
                        mapAttributes[PARAM_CARD_ATTRIBUTE] = responseCardAttribute
                        mapAttributes[PARAM_CARD_INFO] = responseCardInfo
                        mapAttributes[PARAM_ISSUER_ID] = ISSUER_ID_EMONEY
                        mapAttributes[PARAM_CARD_UUID] = responseCardUID
                        mapAttributes[PARAM_LAST_BALANCE] = responseCardLastBalance
                        getEmoneyInquiryBalance(PARAM_INQUIRY, balanceRawQuery, idCard, mapAttributes)
                    } else {
                        isoDep.close()
                        errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getEmoneyInquiryBalance(paramCommand: String, balanceRawQuery: String, idCard: Int,
                                        mapAttributesParam: HashMap<String, Any>) {
        launchCatchError(block = {
            var mapParam = HashMap<String, Any>()
            mapParam.put(TYPE_CARD, paramCommand)
            mapParam.put(ID_CARD, idCard)
            mapParam.put(ATTRIBUTES_CARD, mapAttributesParam)

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(balanceRawQuery, EmoneyInquiryResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<EmoneyInquiryResponse>()

            data.emoneyInquiry.attributesEmoneyInquiry?.let {
                it.formattedCardNumber = NFCUtils.formatCardUID(it.cardNumber)
                it.operatorId = EMONEY_OPERATOR_ID
                it.issuer_id = ISSUER_ID_EMONEY

                if (it.status == 0) {
                    writeBalanceToCard(it.payload, balanceRawQuery, data.emoneyInquiry.id.toInt(), mapAttributesParam)
                } else {
                    emoneyInquiry.postValue(data.emoneyInquiry)
                }
            }
        }) {
            errorInquiryBalance.postValue(it)
        }
    }

    fun writeBalanceToCard(payload: String, balanceRawQuery: String, id: Int, mapAttributes: HashMap<String, Any>) {
        if (::isoDep.isInitialized && isoDep.isConnected) {
            try {
                val responseInByte = isoDep.transceive(NFCUtils.hexStringToByteArray(payload))

                run {
                    if (responseInByte != null) {
                        // to get card payload
                        val response = NFCUtils.toHex(responseInByte)
                        mapAttributes[PARAM_PAYLOAD] = response
                        getEmoneyInquiryBalance(PARAM_SEND_COMMAND, balanceRawQuery, id, mapAttributes)
                    } else {
                        isoDep.close()
                        errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                }
            } catch (e: IOException) {
                isoDep.close()
                errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
            }
        } else {
            errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    companion object {
        const val TYPE_CARD = "type"
        const val ID_CARD = "id"
        const val ATTRIBUTES_CARD = "attributes"

        const val PARAM_INQUIRY = "SMARTCARD_INQUIRY"
        const val PARAM_SEND_COMMAND = "SMARTCARD_COMMAND"

        const val PARAM_CARD_UUID = "card_uuid"
        const val PARAM_ISSUER_ID = "card_issuer_id"
        const val PARAM_CARD_ATTRIBUTE = "card_attribute"
        const val PARAM_CARD_INFO = "card_info"
        const val PARAM_LAST_BALANCE = "last_balance"
        const val PARAM_PAYLOAD = "payload"

        const val EMONEY_OPERATOR_ID = "578"
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