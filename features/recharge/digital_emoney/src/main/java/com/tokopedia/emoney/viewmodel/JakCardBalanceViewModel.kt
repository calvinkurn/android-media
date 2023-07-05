package com.tokopedia.emoney.viewmodel

import android.annotation.SuppressLint
import android.nfc.tech.IsoDep
import androidx.lifecycle.LiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.domain.request.JakCardRequestMapper
import com.tokopedia.emoney.domain.request.JakCardStatus
import com.tokopedia.emoney.domain.response.JakCardData
import com.tokopedia.emoney.domain.response.JakCardDataEnc
import com.tokopedia.emoney.domain.response.JakCardResponseMapper
import com.tokopedia.emoney.domain.usecase.GetJakCardUseCase
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class JakCardBalanceViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val jakCardUseCase: GetJakCardUseCase,
    private val electronicMoneyEncryption: ElectronicMoneyEncryption,
    private val gson: Gson
) : BaseViewModel(dispatcher) {

    private var errorCardMessageMutable = SingleLiveEvent<Throwable>()
    val errorCardMessage: LiveData<Throwable>
        get() = errorCardMessageMutable

    private var jakCardInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val jakCardInquiry: LiveData<EmoneyInquiry>
        get() = jakCardInquiryMutable

    @Suppress("LateinitUsage")
    lateinit var isoDep: IsoDep

    fun processJakCardTagIntent(isoDep: IsoDep, isDev: Boolean,
                                rawPublicKeyString: String,
                                rawPrivateKeyString: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    this.isoDep = isoDep
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC // 7 sec time out

                    val selectRequest = NFCUtils.stringToByteArrayRadix(getSelectAIDCommand(isDev))
                    val selectResponse = isoDep.transceive(selectRequest)
                    val selectResponseString = NFCUtils.toHex(selectResponse)
                    if (NFCUtils.isCommandFailed(selectResponse)) {
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    } else {
                        val checkBalanceRequest =
                            NFCUtils.stringToByteArrayRadix(COMMAND_CHECK_BALANCE)
                        val checkBalanceResponse = isoDep.transceive(checkBalanceRequest)
                        if (NFCUtils.isCommandFailed(checkBalanceResponse)) {
                            errorCardMessageMutable.postValue(
                                MessageErrorException(
                                    NfcCardErrorTypeDef.FAILED_READ_CARD
                                )
                            )
                        } else {
                            val cardNumber = getCardNumberFromSelectResponse(selectResponseString)
                            val separatedCheckBalanceString =
                                separateWithSuccessCode(NFCUtils.toHex(checkBalanceResponse))
                            val lastBalance =
                                convertHexBalanceToIntBalance(separatedCheckBalanceString)
                            getPendingBalanceProcess(selectResponseString, cardNumber, lastBalance, rawPublicKeyString, rawPrivateKeyString)
                        }
                    }

                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getPendingBalanceProcess(
        selectResponseString: String,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String
    ) {
        launchCatchError(block = {

            val payloadGetPendingBalanceQuery = JakCardRequestMapper.createGetPendingBalanceParam(
                selectResponseString,
                cardNumber,
                lastBalance,
                gson
            )

            val encParam = electronicMoneyEncryption.createEncryptedPayload(rawPublicKeyString, payloadGetPendingBalanceQuery)
            val paramGetPendingBalanceQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)

            val encResult = jakCardUseCase.execute(paramGetPendingBalanceQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)

            if (result.status == JakCardStatus.WRITE.status) {
                processInitLoad(
                    selectResponseString,
                    cardNumber,
                    lastBalance,
                    result.attributes.cryptogram,
                    rawPublicKeyString,
                    rawPrivateKeyString
                )
            } else {
                jakCardInquiryMutable.postValue(JakCardResponseMapper.jakCardResponseMapper(result))
            }
        }) {
            errorCardMessageMutable.postValue(it)
        }
    }

    fun processInitLoad(
        selectResponseString: String,
        cardNumber: String,
        lastBalance: Int,
        cryptogram: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String
    ) {
        if (isIsoDepInitialized() && isoDep.isConnected && cryptogram.isNotEmpty()) {
            try {
                val initLoadRequest = NFCUtils.stringToByteArrayRadix(cryptogram)
                val initLoadResponse = isoDep.transceive(initLoadRequest)
                val initLoadResponseString = NFCUtils.toHex(initLoadResponse)

                if (NFCUtils.isCommandFailed(initLoadResponse)) {
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                } else {
                    val topUpCardData = getCardDataTopUp(
                        separateWithSuccessCode(initLoadResponseString),
                        getDepositFromSelectResponse(selectResponseString),
                        getCardExpiryFromSelectResponse(selectResponseString)
                    )

                    val amount = convertHexBalanceToIntBalance(getAmountFromCryptogram(cryptogram))

                    getTopUpProcess(topUpCardData, cardNumber, lastBalance, amount, rawPublicKeyString, rawPrivateKeyString)
                }
            } catch (e: IOException) {
                errorCardMessageMutable.postValue(e)
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getTopUpProcess(
        topUpCardData: String,
        cardNumber: String,
        lastBalance: Int,
        amount: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String
    ) {
        launchCatchError(block = {
            val payloadGetTopUpQuery = JakCardRequestMapper.createGetTopUpParam(
                topUpCardData, cardNumber,
                lastBalance, amount, gson
            )

            val encParam = electronicMoneyEncryption.createEncryptedPayload(rawPublicKeyString, payloadGetTopUpQuery)
            val paramGetTopUpQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)

            val encResult = jakCardUseCase.execute(paramGetTopUpQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)

            if (result.status == JakCardStatus.WRITE.status) {
                processLoad(
                    result,
                    topUpCardData,
                    result.attributes.cryptogram,
                    result.attributes.stan,
                    result.attributes.refNo,
                    amount,
                    cardNumber,
                    rawPublicKeyString,
                    rawPrivateKeyString
                )
            } else {
                jakCardInquiryMutable.postValue(JakCardResponseMapper.jakCardResponseMapper(result))
            }
        }) {
            errorCardMessageMutable.postValue(it)
        }
    }

    fun processLoad(
        topUpJakCardResponse: JakCardData,
        topUpCardData: String,
        cryptogram: String,
        stan: String,
        refNo: String,
        amount: Int,
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String
    ) {
        if (isIsoDepInitialized() && isoDep.isConnected && cryptogram.isNotEmpty()) {
            try {
                val loadRequest = NFCUtils.stringToByteArrayRadix(cryptogram)
                val loadResponse = isoDep.transceive(loadRequest)
                val loadResponseString = NFCUtils.toHex(loadResponse)

                if (NFCUtils.isCommandFailed(loadResponse)) {
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                } else {
                    val checkBalanceAfterLoadRequest =
                        NFCUtils.stringToByteArrayRadix(COMMAND_CHECK_BALANCE)
                    val checkBalanceAfterLoadResponse =
                        isoDep.transceive(checkBalanceAfterLoadRequest)

                    val topUpConfirmationCardData = getCardDataTopUpRequest(
                        separateWithSuccessCode(loadResponseString),
                        topUpCardData
                    )
                    val separatedCheckBalanceString =
                        separateWithSuccessCode(NFCUtils.toHex(checkBalanceAfterLoadResponse))
                    if (NFCUtils.isCommandFailed(checkBalanceAfterLoadResponse)) {
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    } else {
                        val lastBalanceAfterUpdate =
                            convertHexBalanceToIntBalance(separatedCheckBalanceString)
                        getTopUpConfirmationProcess(
                            topUpJakCardResponse,
                            topUpConfirmationCardData,
                            cardNumber,
                            lastBalanceAfterUpdate,
                            amount,
                            stan,
                            refNo,
                            rawPublicKeyString,
                            rawPrivateKeyString
                        )
                    }
                }
            } catch (e: IOException) {
                errorCardMessageMutable.postValue(e)
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    fun isIsoDepInitialized(): Boolean = ::isoDep.isInitialized

    private fun getTopUpConfirmationProcess(
        topUpJakCardResponse: JakCardData,
        topUpConfirmationCardData: String,
        cardNumber: String,
        lastBalanceAfterUpdate: Int,
        amount: Int,
        stan: String,
        refNo: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String
    ) {
        launchCatchError(block = {
            val payloadGetTopUpQuery = JakCardRequestMapper.createGetTopUpConfirmationParam(
                topUpConfirmationCardData,
                cardNumber, lastBalanceAfterUpdate, amount, stan, refNo, gson
            )

            val encParam = electronicMoneyEncryption.createEncryptedPayload(rawPublicKeyString, payloadGetTopUpQuery)
            val paramGetTopUpQuery = JakCardRequestMapper.createEncryptedParam(encParam.first, encParam.second)

            val encResult = jakCardUseCase.execute(paramGetTopUpQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)

            if (result.status == JakCardStatus.ERROR.status ||
                result.status == JakCardStatus.WRITE.status
            ) {
                result.status = JakCardStatus.DONE.status
            }
            jakCardInquiryMutable.postValue(JakCardResponseMapper.jakCardResponseMapper(result))
        }) {
            jakCardInquiryMutable.postValue(
                JakCardResponseMapper.jakCardResponseMapper(
                    JakCardResponseMapper.jakCardResponseConfirmationFailed(
                        topUpJakCardResponse,
                        lastBalanceAfterUpdate
                    )
                )
            )
        }
    }

    private fun getSelectAIDCommand(isDev: Boolean): String {
        return if (isDev) {
            COMMAND_SELECT_JAK_CARD_STAG + COMMAND_AID_JAK_CARD_STAG
        } else {
            COMMAND_SELECT_JAK_CARD_PROD + COMMAND_AID_JAK_CARD_PROD
        }
    }

    @SuppressLint("Method Call Prohibited")
    private fun convertHexBalanceToIntBalance(hexBalance: String): Int {
        return hexBalance.toInt(RADIX_BALANCE)
    }

    private fun separateWithSuccessCode(response: String): String {
        val size = response.length
        val maxResponseLength = size - CODE_RESPONSE_SIZE
        val separatedResponse = response.substring(Int.ZERO, maxResponseLength)
        return separatedResponse
    }

    private fun getCardNumberFromSelectResponse(selectResponseString: String): String {
        return selectResponseString.substring(CARD_NUMBER_START, CARD_NUMBER_END)
    }

    private fun getDepositFromSelectResponse(selectResponseString: String): String {
        return selectResponseString.substring(CARD_DEPOSIT_START, CARD_DEPOSIT_END)
    }

    private fun getCardExpiryFromSelectResponse(selectResponseString: String): String {
        return selectResponseString.substring(CARD_EXPIRY_START, CARD_EXPIRY_END)
    }

    private fun getCardDataTopUp(
        initLoadResponse: String,
        deposit: String,
        cardExpiry: String
    ): String {
        return initLoadResponse + deposit + cardExpiry
    }

    private fun getCardDataTopUpRequest(
        separatedLoadResponse: String,
        topUpCardData: String
    ): String {
        return topUpCardData + separatedLoadResponse
    }

    private fun getAmountFromCryptogram(cryptogram: String): String {
        return cryptogram.substring(CARD_AMOUNT_START, CARD_AMOUNT_END)
    }

    private fun decryptPayload(jakCardDataEnc: JakCardDataEnc, rawPrivateKeyString: String): JakCardData {
        val decryptedPayload = electronicMoneyEncryption.createDecryptedPayload(
            rawPrivateKeyString,
            jakCardDataEnc.encKey,
            jakCardDataEnc.encPayload
        )

        return getJakcardData(decryptedPayload)
    }

    fun getJakcardData(payload: String): JakCardData {
        return gson.fromJson(payload, JakCardData::class.java)
    }

    companion object {
        private const val TRANSCEIVE_TIMEOUT_IN_SEC = 7000
        private const val COMMAND_SELECT_JAK_CARD_PROD = "00A4040008"
        private const val COMMAND_SELECT_JAK_CARD_STAG = "00A4040007"
        private const val COMMAND_AID_JAK_CARD_PROD = "A0000005714E4A43"
        private const val COMMAND_AID_JAK_CARD_STAG = "D3600000030003"

        private const val COMMAND_CHECK_BALANCE = "904C000004"
        private const val CODE_RESPONSE_SIZE = 4
        private const val RADIX_BALANCE = 16

        private const val CARD_NUMBER_START = 16
        private const val CARD_NUMBER_END = 32
        private const val CARD_DEPOSIT_START = 74
        private const val CARD_DEPOSIT_END = 82
        private const val CARD_EXPIRY_START = 50
        private const val CARD_EXPIRY_END = 58
        private const val CARD_AMOUNT_START = 10
        private const val CARD_AMOUNT_END = 18
    }
}
