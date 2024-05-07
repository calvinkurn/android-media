package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.usecase.GetBCAGenCheckerUseCase
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.RechargeEmoneyInquiryLogRequest
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.domain.EmoneyParamMapper.mapParamLogErrorNetworkFlazz
import com.tokopedia.emoney.domain.request.BCAFlazzRequestMapper
import com.tokopedia.emoney.domain.request.BCAFlazzStatus
import com.tokopedia.emoney.domain.request.CommonBodyEnc
import com.tokopedia.emoney.domain.response.BCAFlazzData
import com.tokopedia.emoney.domain.response.BCAFlazzResponseMapper
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibraryIntegration
import com.tokopedia.emoney.integration.data.JNIResult
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class BCABalanceViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val bcaLibrary: BCALibraryIntegration,
    private val gson: Gson,
    private val electronicMoneyEncryption: ElectronicMoneyEncryption,
    private val bcaFlazzUseCase: GetBCAFlazzUseCase,
    private val getBCAGenCheckerUseCase: GetBCAGenCheckerUseCase
) : BaseViewModel(dispatcher) {

    private var bcaInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val bcaInquiry: LiveData<EmoneyInquiry>
        get() = bcaInquiryMutable

    private var errorCardMessageMutable = SingleLiveEvent<Pair<Throwable, RechargeEmoneyInquiryLogRequest>>()
    val errorCardMessage: LiveData<Pair<Throwable, RechargeEmoneyInquiryLogRequest>>
        get() = errorCardMessageMutable

    private var errorTagLogMutable = SingleLiveEvent<RechargeEmoneyInquiryLogRequest>()
    val errorTagLog: LiveData<RechargeEmoneyInquiryLogRequest>
        get() = errorTagLogMutable

    fun processBCATagBalance(
        isoDep: IsoDep, merchantId: String, terminalId: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        strCurrDateTime: String,
        ATD: String,
        messageBCATopUp2: String
    ) {
        val bcaMTId = BCAFlazzResponseMapper.bcaMTId(merchantId, terminalId)
        val setConfigResult = bcaLibrary.bcaSetConfig(bcaMTId)
        if (isoDep != null) {
            run {
                try {
                    if (setConfigResult.isSuccess == SUCCESS_JNI) {
                        val dataBalance = checkLatestBalance()
                        if (dataBalance.isSuccess == SUCCESS_JNI) {
                            getPendingBalanceProcess(
                                isoDep, dataBalance.cardNo, dataBalance.balance,
                                rawPublicKeyString, rawPrivateKeyString, GEN_TWO, strCurrDateTime, ATD,
                                messageBCATopUp2 = messageBCATopUp2
                            )
                        } else {
                            isoDep.close()
                            errorCardMessageMutable.postValue(Pair(
                                MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                                mapParamLogErrorNetworkFlazz(
                                        dataBalance.cardNo,
                                        messageLogNR(
                                            TAG_PROCESS_PENDING_BALANCE_GEN_2,
                                            separateResponseCodeFromCardData(
                                                dataBalance.strLogRsp
                                            )
                                        ),
                                        dataBalance.balance
                                    )
                                )
                            )
                        }
                    } else {
                        isoDep.close()
                        errorCardMessageMutable.postValue(Pair(
                                MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                                mapParamLogErrorNetworkFlazz(
                                    setConfigResult.cardNo,
                                    messageLogNR(
                                        TAG_PROCESS_PENDING_BALANCE_GEN_2,
                                        separateResponseCodeFromCardData(
                                            setConfigResult.strLogRsp
                                        )
                                    ), EMPTY_LAST_BALANCE
                                )
                            )
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                "",
                                messageLogNR(
                                    TAG_PROCESS_PENDING_BALANCE_GEN_2,
                                    e.message
                                ), EMPTY_LAST_BALANCE
                            )
                        )
                    )
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        setConfigResult.cardNo,
                        messageLogNR(
                            TAG_PROCESS_PENDING_BALANCE_GEN_2,
                            ERROR_MESSAGE_ISODEP
                        ), EMPTY_LAST_BALANCE
                    )
                )
            )
        }
    }

    fun processBCACheckBalanceGen1(
        isoDep: IsoDep,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        messageGen1: String,
    ) {
        if (isoDep != null) {
            run {
                try {
                    val dataBalance = checkLatestBalance()
                    launchCatchError(block = {
                        val data = getBCAGenCheckerUseCase.execute(listOf(dataBalance.cardNo))
                        val message = if (data.persoFavoriteNumber.items.first().label2.isNotEmpty()) {
                            data.persoFavoriteNumber.items.first().label2
                        } else {
                            messageGen1
                        }
                        getPendingBalanceProcess(
                            isoDep, dataBalance.cardNo, dataBalance.balance,
                            rawPublicKeyString, rawPrivateKeyString, GEN_ONE, "", "",
                            message, ""
                        )
                    }) {
                        getPendingBalanceProcess(
                            isoDep, dataBalance.cardNo, dataBalance.balance,
                            rawPublicKeyString, rawPrivateKeyString, GEN_ONE, "", "",
                            messageGen1, ""
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                "",
                                messageLogNR(
                                    TAG_PROCESS_PENDING_BALANCE_GEN_1,
                                    e.message
                                ), EMPTY_LAST_BALANCE
                            )
                        )
                    )
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        "",
                        messageLogNR(
                            TAG_PROCESS_PENDING_BALANCE_GEN_1,
                            ERROR_MESSAGE_ISODEP
                        ), EMPTY_LAST_BALANCE
                    )
                )
            )
        }
    }

    fun getPendingBalanceProcess(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        messageBCAGen1: String = "",
        messageBCATopUp2: String
    ) {
        launchCatchError(block = {
            val payloadGetPendingBalanceQuery = BCAFlazzRequestMapper.createGetPendingBalanceParam(
                gson, cardNumber, lastBalance, cardType
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(
                rawPublicKeyString,
                payloadGetPendingBalanceQuery
            )
            val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(
                encParam.first,
                encParam.second
            )

            val encResult = bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)
            Timber.d("BCALOGWKPendingBalance $result")

            if (result.status == BCAFlazzStatus.WRITE.status) {
                getGenerateTrxIdProcess(
                    isoDep, cardNumber, lastBalance, rawPublicKeyString,
                    rawPrivateKeyString, cardType, strCurrDateTime, ATD,
                    messageBCATopUp2
                )
            } else if(result.status == BCAFlazzStatus.REVERSAL.status && result.attributes.transactionID.isNotEmpty()) {
                processSDKReversal(
                    isoDep,
                    result.attributes.transactionID,
                    ATD,
                    cardNumber,
                    lastBalance,
                    rawPublicKeyString,
                    rawPrivateKeyString,
                    cardType,
                    result,
                    messageBCATopUp2
                )
            } else {
                bcaInquiryMutable.postValue(
                    BCAFlazzResponseMapper.bcaMapper(
                        cardNumber,
                        lastBalance, result.attributes.imageIssuer, getIsBCAGenOne(cardType),
                        result.attributes.amount, result.status, result.attributes.message,
                        result.attributes.hasMorePendingBalance,
                        messageBCAGen1 = messageBCAGen1
                    )
                )
            }
        }) {
            errorCardMessageMutable.postValue(Pair(
                    it,
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_PENDING_BALANCE,
                            it.message
                        ), lastBalance
                    )
                )
            )
        }
    }

    fun getGenerateTrxIdProcess(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        messageBCATopUp2: String
    ) {
        launchCatchError(block = {
            val payloadGetGenerateTrxIdQuery = BCAFlazzRequestMapper.createGetBCAGenerateTrxId(
                gson, cardNumber, lastBalance, cardType
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(
                rawPublicKeyString,
                payloadGetGenerateTrxIdQuery
            )
            val paramGetGenerateTrxIdQuery = BCAFlazzRequestMapper.createEncryptedParam(
                encParam.first,
                encParam.second
            )

            val encResult = bcaFlazzUseCase.execute(paramGetGenerateTrxIdQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)
            Timber.d("BCALOGWKTrxId $result")

            if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.transactionID.isNotEmpty()) {
                processSDKBCADataSession1(
                    isoDep,
                    cardNumber,
                    lastBalance,
                    rawPublicKeyString,
                    rawPrivateKeyString,
                    cardType,
                    strCurrDateTime,
                    ATD,
                    result.attributes.transactionID,
                    messageBCATopUp2
                )
            } else if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.transactionID.isEmpty()) {
                errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_TRANSACTION_ID,
                            ERROR_TRANSACTION_ID_EMPTY
                        ), lastBalance
                    )
                ))
            } else {
                bcaInquiryMutable.postValue(
                    BCAFlazzResponseMapper.bcaMapper(
                        cardNumber,
                        lastBalance,
                        result.attributes.imageIssuer,
                        getIsBCAGenOne(cardType),
                        result.attributes.amount,
                        result.status,
                        result.attributes.message,
                        result.attributes.hasMorePendingBalance
                    )
                )
            }
        }) {
            errorCardMessageMutable.postValue(Pair(
                it,
                mapParamLogErrorNetworkFlazz(
                    cardNumber,
                    messageLogNR(
                        TAG_PROCESS_TRANSACTION_ID,
                        it.message
                    ), lastBalance
                )
            )
            )
        }
    }

    fun processSDKBCADataSession1(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        messageBCATopUp2: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val bcaSession1 = bcaLibrary.bcaDataSession1(
                        strTransactionId, ATD,
                        strCurrDateTime
                    )
                    if (bcaSession1.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(bcaSession1.strLogRsp)) {
                        getSessionKeyProcess(
                            isoDep, cardNumber, lastBalance, rawPublicKeyString,
                            rawPrivateKeyString, cardType, strCurrDateTime, ATD, strTransactionId,
                            bcaSession1.strLogRsp,
                            messageBCATopUp2
                        )
                    } else {
                        // set error if BCAdataSession_1 process error
                        errorCardMessageMutable.postValue(Pair(
                                MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                                mapParamLogErrorNetworkFlazz(
                                    cardNumber,
                                    messageLogNR(
                                        TAG_PROCESS_SDK_SESSION_1,
                                        separateResponseCodeFromCardData(
                                            bcaSession1.strLogRsp
                                        )
                                    ), lastBalance
                                )
                            )
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_SESSION_1,
                                    e.message
                                ),
                                lastBalance
                            )
                        )
                    )
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_SDK_SESSION_1,
                            ERROR_MESSAGE_ISODEP
                        ),
                        lastBalance
                    )
                )
            )
        }
    }

    fun getSessionKeyProcess(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        cardDataSession1Key: String,
        messageBCATopUp2: String
    ) {
        launchCatchError(block = {
            val payloadGetSessionKeyQuery = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
                gson, cardNumber, separateCardDataFromResponseCode(cardDataSession1Key), lastBalance,
                strTransactionId, cardType,
                separateResponseCodeFromCardData(cardDataSession1Key)
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(
                rawPublicKeyString,
                payloadGetSessionKeyQuery
            )
            val paramGetSessionKeyQuery = BCAFlazzRequestMapper.createEncryptedParam(
                encParam.first,
                encParam.second
            )

            val encResult = bcaFlazzUseCase.execute(paramGetSessionKeyQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)
            Timber.d("BCALOGWKSessionKey $result")

            if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.cardData.isNotEmpty()) {
                processSDKBCADataSession2(
                    isoDep, cardNumber, lastBalance, rawPublicKeyString,
                    rawPrivateKeyString, cardType, strCurrDateTime, ATD, strTransactionId, result,
                    messageBCATopUp2
                )
            } else if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.cardData.isEmpty()) {
                errorCardMessageMutable.postValue(Pair(
                        MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                        mapParamLogErrorNetworkFlazz(
                            cardNumber,
                            messageLogNR(
                                TAG_PROCESS_SESSION_KEY,
                                ERROR_SESSION_CARD_DATA_EMPTY
                            ),
                            lastBalance
                        )
                    )
                )
            } else {
                bcaInquiryMutable.postValue(
                    BCAFlazzResponseMapper.bcaMapper(
                        cardNumber,
                        lastBalance,
                        result.attributes.imageIssuer,
                        getIsBCAGenOne(cardType),
                        result.attributes.amount,
                        result.status,
                        result.attributes.message,
                        result.attributes.hasMorePendingBalance
                    )
                )
            }
        }) {
            errorCardMessageMutable.postValue(Pair(
                    it,
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_SESSION_KEY,
                            it.message
                        ),
                        lastBalance
                    )
                )
            )
        }
    }

    fun processSDKBCADataSession2(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData,
        messageBCATopUp2: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val bcaSession2 =
                        bcaLibrary.bcaDataSession2(bcaFlazzData.attributes.cardData)
                    if (bcaSession2.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(bcaSession2.strLogRsp)) {
                        processSDKBCATopUp1(
                            isoDep, cardNumber, lastBalance, rawPublicKeyString,
                            rawPrivateKeyString, cardType, strCurrDateTime, ATD, strTransactionId,
                            bcaFlazzData,
                            messageBCATopUp2
                        )
                    } else {
                        isoDep.close()
                        // set error if BCAdataSession_2 process error
                        errorCardMessageMutable.postValue(Pair(
                                MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                                mapParamLogErrorNetworkFlazz(
                                    cardNumber,
                                    messageLogNR(
                                        TAG_PROCESS_SDK_SESSION_2,
                                        separateResponseCodeFromCardData(
                                            bcaSession2.strLogRsp
                                        )
                                    ),
                                    lastBalance
                                )
                            )
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_SESSION_2,
                                    e.message
                                ),
                                lastBalance
                            )
                        )
                    )
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                     cardNumber,
                        messageLogNR(
                            TAG_PROCESS_SDK_SESSION_2,
                            ERROR_MESSAGE_ISODEP
                        ),
                        lastBalance
                    )
                )
            )
        }
    }

    fun processSDKBCATopUp1(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData,
        messageBCATopUp2: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val topUp1 = bcaLibrary.bcaTopUp1(
                        strTransactionId,
                        ATD,
                        bcaFlazzData.attributes.accessCardNumber,
                        bcaFlazzData.attributes.accessCode,
                        strCurrDateTime,
                        bcaFlazzData.attributes.amount.toLong()
                    )
                    if (topUp1.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(topUp1.strLogRsp)) {
                        getBetweenTopUpProcess(
                            isoDep,
                            cardNumber,
                            lastBalance,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            bcaFlazzData,
                            topUp1.strLogRsp,
                            ATD,
                            messageBCATopUp2
                        )
                    } else {
                        isoDep.close()
                        // Bila respon dari BCATopUp_1 bukan SUCCESS
                        // (prefix SUCCESS : 0000), maka tidak perlu melakukan Reversal karena rekening sumber dana tidak terdebet
                        // src:Top Up Flazz di mch online-sosialisasi merchant 1222
                        errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_TOP_UP_1,
                                    separateResponseCodeFromCardData(
                                        topUp1.strLogRsp
                                    )
                                ),
                                lastBalance
                            ))
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_TOP_UP_1,
                                    e.message
                                ),
                                lastBalance
                            )
                        )
                    )
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_SDK_TOP_UP_1,
                            ERROR_MESSAGE_ISODEP
                        ),
                        lastBalance
                    )
                )
            )
        }
    }

    fun getBetweenTopUpProcess(
        isoDep: IsoDep?,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData,
        cardDataTopUp1: String,
        ATD: String,
        messageBCATopUp2: String
    ) {
        launchCatchError(block = {
            val payloadGetBetweenTopUpQuery = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
                gson, cardNumber, separateCardDataFromResponseCode(cardDataTopUp1), bcaFlazzData.attributes.amount,
                lastBalance, strTransactionId, cardType,
                separateResponseCodeFromCardData(cardDataTopUp1)
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(
                rawPublicKeyString,
                payloadGetBetweenTopUpQuery
            )
            val paramGetBetweenTopUpQuery = BCAFlazzRequestMapper.createEncryptedParam(
                encParam.first,
                encParam.second
            )

            val encResult = bcaFlazzUseCase.execute(paramGetBetweenTopUpQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)
            Timber.d("BCALOGWKTopUp $result")

            if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.cardData.isNotEmpty()) {
                processSDKBCATopUp2(
                    isoDep, cardNumber, rawPublicKeyString, rawPrivateKeyString,
                    cardType, strTransactionId, result, lastBalance, messageBCATopUp2
                )
            } else if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.cardData.isEmpty()) {
                // Panggil function library BCAdataReversal dan kirimkan output library ke BCA
                // melalui service API reversal
                processSDKReversal(
                    isoDep,
                    strTransactionId,
                    ATD,
                    cardNumber,
                    lastBalance,
                    rawPublicKeyString,
                    rawPrivateKeyString,
                    cardType,
                    bcaFlazzData,
                    messageBCATopUp2
                )
            } else if (result.status == BCAFlazzStatus.ERROR.status) {
                // Panggil function library BCAdataReversal dan kirimkan output library ke BCA
                // melalui service API reversal
                processSDKReversal(
                    isoDep,
                    strTransactionId,
                    ATD,
                    cardNumber,
                    lastBalance,
                    rawPublicKeyString,
                    rawPrivateKeyString,
                    cardType,
                    bcaFlazzData,
                    messageBCATopUp2
                )
            } else if(result.status == BCAFlazzStatus.DONE.status){
                errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(result.attributes.message),
                    mapParamLogErrorNetworkFlazz(
                            cardNumber,
                            messageLogNR(
                                TAG_PROCESS_BETWEEN_TOP_UP,
                                result.attributes.message
                            ),
                            lastBalance
                        )
                    )
                )
            } else {
                bcaInquiryMutable.postValue(
                    BCAFlazzResponseMapper.bcaMapper(
                        cardNumber,
                        lastBalance, result.attributes.imageIssuer, getIsBCAGenOne(cardType),
                        result.attributes.amount, result.status, result.attributes.message,
                        result.attributes.hasMorePendingBalance
                    )
                )
            }
        }) {
            // Panggil function library BCAdataReversal dan kirimkan output library ke BCA
            // melalui service API reversal
            processSDKReversal(
                isoDep,
                strTransactionId,
                ATD,
                cardNumber,
                lastBalance,
                rawPublicKeyString,
                rawPrivateKeyString,
                cardType,
                bcaFlazzData,
                messageBCATopUp2
            )
        }
    }

    fun processSDKBCATopUp2(
        isoDep: IsoDep?,
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData,
        lastBalance: Int,
        messageBCATopUp2: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val topUp2 = bcaLibrary.bcaTopUp2(bcaFlazzData.attributes.cardData)
                    val updatedBalance = checkLatestBalance()
                    if (topUp2.isSuccess == SUCCESS_JNI
                        && getBCAPrefixSuccess(topUp2.strLogRsp)) {
                        getACKProcess(
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            topUp2.strLogRsp,
                            if (updatedBalance.isSuccess == SUCCESS_JNI) updatedBalance.balance else bcaFlazzData.attributes.lastBalance,
                            messageBCATopUp2,
                            isErrorTopUp2 = false
                        )
                    } else if (topUp2.strLogRsp.isNotEmpty()) {
                        // Ada kemungkinan saldo Flazz sudah bertambah walaupun output BCATopUp2 bukan SUCCESS.
                        // Tetap kirimkan output BCATopUp2 ke BCA melalui API /flazz/ack
                        errorTagLogMutable.postValue(
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_TOP_UP_2,
                                    separateResponseCodeFromCardData(
                                        topUp2.strLogRsp
                                    )
                                ),
                                lastBalance
                            )
                        )

                        getACKProcess(
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            topUp2.strLogRsp,
                            if (updatedBalance.isSuccess == SUCCESS_JNI) updatedBalance.balance else bcaFlazzData.attributes.lastBalance,
                            messageBCATopUp2,
                            isErrorTopUp2 = true
                        )
                    } else if (topUp2.strLogRsp.isEmpty()) {
                        // Output BCATopUp_2 tidak ada / hilang
                        // Panggil function library BCAlastBCATopUp dan kirimkan output-nya ke BCA
                        // melalui API ACK
                        processSDKBCAlastBCATopUp(
                            isoDep,
                            strTransactionId,
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            if (updatedBalance.isSuccess == SUCCESS_JNI) updatedBalance.balance else bcaFlazzData.attributes.lastBalance,
                            messageBCATopUp2,
                            isErrorTopUp2 = true
                        )
                    }

                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_TOP_UP_2,
                                    e.message
                                ),
                                lastBalance
                            )
                        )
                    )
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                mapParamLogErrorNetworkFlazz(
                    cardNumber,
                    messageLogNR(
                        TAG_PROCESS_SDK_TOP_UP_2,
                        ERROR_MESSAGE_ISODEP
                    ),
                    lastBalance
                )
            )
            )
        }
    }

    private fun getACKProcess(
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        cardDataTopUp2: String,
        updatedBalance: Int,
        messageBCATopUp2: String,
        isErrorTopUp2: Boolean
    ) {
        launchCatchError(block = {
            val payloadGetACKQuery = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
                gson, cardNumber, separateCardDataFromResponseCode(cardDataTopUp2),
                updatedBalance, strTransactionId, cardType,
                separateResponseCodeFromCardData(cardDataTopUp2)
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(
                rawPublicKeyString,
                payloadGetACKQuery
            )
            val paramGetACKQuery =
                BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)

            val encResult = bcaFlazzUseCase.execute(paramGetACKQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)
            Timber.d("BCALOGWKSACK $result")

            bcaInquiryMutable.postValue(
                BCAFlazzResponseMapper.bcaMapper(
                    cardNumber,
                    updatedBalance,
                    result.attributes.imageIssuer,
                    getIsBCAGenOne(cardType),
                    result.attributes.amount,
                    result.status,
                    result.attributes.message,
                    result.attributes.hasMorePendingBalance,
                    ackStatusOverride = true,
                    messageTopUp2 = messageBCATopUp2,
                    isErrorTopUp2 = isErrorTopUp2
                )
            )
        }) {
            errorCardMessageMutable.postValue(Pair(
                it,
                mapParamLogErrorNetworkFlazz(
                    cardNumber,
                    messageLogNR(
                        TAG_PROCESS_ACK,
                        it.message
                    ),
                    updatedBalance
                )
                )
            )
        }
    }

    fun processSDKReversal(
        isoDep: IsoDep?,
        strTransactionId: String,
        ATD: String,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        bcaFlazzData: BCAFlazzData,
        messageBCATopUp2: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val reversal = bcaLibrary.bcaDataReversal(strTransactionId, ATD)
                    if (reversal.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(reversal.strLogRsp)) {
                        getReversalProcess(
                            cardNumber,
                            lastBalance,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            bcaFlazzData,
                            reversal.strLogRsp
                        )
                    } else if(getBCARC8303(reversal.strLogRsp)) {
                        processSDKBCAlastBCATopUp(
                            isoDep,
                            strTransactionId,
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            lastBalance,
                            messageBCATopUp2,
                            isErrorTopUp2 = false
                        )
                    } else {
                        errorCardMessageMutable.postValue(Pair(
                            MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                            mapParamLogErrorNetworkFlazz(
                                cardNumber,
                                messageLogNR(
                                    TAG_PROCESS_SDK_REVERSAL,
                                    separateResponseCodeFromCardData(reversal.strLogRsp)
                                ),
                                lastBalance
                            )
                        ))
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                        MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                        mapParamLogErrorNetworkFlazz(
                            cardNumber,
                            messageLogNR(
                                TAG_PROCESS_SDK_REVERSAL,
                                e.message
                            ),
                            lastBalance
                        )
                    ))
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_SDK_REVERSAL,
                            ERROR_MESSAGE_ISODEP
                        ),
                        lastBalance
                    )
                )
            )
        }
    }

    private fun getReversalProcess(
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData,
        cardDataReversal: String,
    ) {
        launchCatchError(block = {
            val payloadGetReversalQuery = BCAFlazzRequestMapper.createGetBCADataReversal(
                gson, cardNumber, separateCardDataFromResponseCode(cardDataReversal),
                bcaFlazzData.attributes.amount, lastBalance,
                strTransactionId, cardType,
                separateResponseCodeFromCardData(cardDataReversal)
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(
                rawPublicKeyString,
                payloadGetReversalQuery
            )
            val paramGetReversalQuery = BCAFlazzRequestMapper.createEncryptedParam(
                encParam.first,
                encParam.second
            )

            val encResult = bcaFlazzUseCase.execute(paramGetReversalQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)
            Timber.d("BCALOGWKReversal $result")

            if (result.status != BCAFlazzStatus.DONE.status) {
                bcaInquiryMutable.postValue(
                    BCAFlazzResponseMapper.bcaMapper(
                        cardNumber,
                        lastBalance,
                        result.attributes.imageIssuer,
                        getIsBCAGenOne(cardType),
                        result.attributes.amount,
                        BCAFlazzStatus.ERROR.status,
                        result.attributes.message,
                        result.attributes.hasMorePendingBalance
                    )
                )
            } else {
                bcaInquiryMutable.postValue(
                    BCAFlazzResponseMapper.bcaMapper(
                        cardNumber,
                        lastBalance,
                        result.attributes.imageIssuer,
                        getIsBCAGenOne(cardType),
                        result.attributes.amount,
                        result.status,
                        result.attributes.message,
                        result.attributes.hasMorePendingBalance
                    )
                )
            }
        }) {
            errorCardMessageMutable.postValue(Pair(
                it,
                mapParamLogErrorNetworkFlazz(
                    cardNumber,
                    messageLogNR(
                        TAG_PROCESS_REVERSAL,
                        it.message
                    ), lastBalance
                )
            ))
        }
    }

    fun processSDKBCAlastBCATopUp(
        isoDep: IsoDep?,
        strTransactionId: String,
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        lastBalance: Int,
        messageBCATopUp2: String,
        isErrorTopUp2: Boolean
    ) {
        if (isoDep != null) {
            run {
                try {
                    val lastTopUp = bcaLibrary.bcaLastBCATopUp()
                    val updatedBalance = checkLatestBalance()
                    if (lastTopUp.isSuccess == SUCCESS_JNI) {
                        getACKProcess(
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            lastTopUp.strLogRsp,
                            updatedBalance.balance,
                            messageBCATopUp2,
                            isErrorTopUp2 = isErrorTopUp2
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(Pair(
                        MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                        mapParamLogErrorNetworkFlazz(
                            cardNumber,
                            messageLogNR(
                                TAG_PROCESS_SDK_BCALASTBCATOP_UP,
                                e.message
                            ), lastBalance
                        ),
                    ))
                }
            }
        } else {
            errorCardMessageMutable.postValue(Pair(
                    MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD),
                    mapParamLogErrorNetworkFlazz(
                        cardNumber,
                        messageLogNR(
                            TAG_PROCESS_SDK_BCALASTBCATOP_UP,
                            ERROR_MESSAGE_ISODEP
                        ), lastBalance
                    )
                )
            )
        }
    }

    private fun checkLatestBalance(): JNIResult {
        return bcaLibrary.bcaCheckBalance()
    }

    private fun decryptPayload(
        bcaFlazzDataEnc: CommonBodyEnc,
        rawPrivateKeyString: String
    ): BCAFlazzData {
        val decryptedPayload = electronicMoneyEncryption.createDecryptedPayload(
            rawPrivateKeyString,
            bcaFlazzDataEnc.encKey,
            bcaFlazzDataEnc.encPayload
        )

        return getBCAFlazzData(decryptedPayload)
    }

    private fun getBCAFlazzData(payload: String): BCAFlazzData {
        return gson.fromJson(payload, BCAFlazzData::class.java)
    }

    private fun getIsBCAGenOne(cardType: String): Boolean {
        return cardType == GEN_ONE
    }

    private fun getBCAPrefixSuccess(strLogResp: String): Boolean {
        return strLogResp.startsWith(SUCCESS_PREFIX)
    }

    private fun getBCARC8303(strLogResp: String): Boolean {
        return strLogResp.startsWith(RC_8303)
    }

    private fun separateCardDataFromResponseCode(strLogResp: String): String {
        return if (strLogResp.length > PREFIX_SIZE) {
            strLogResp.substring(PREFIX_SIZE, strLogResp.length)
        } else {
            strLogResp
        }
    }

    private fun messageLogNR(step: String, errorMessage: String?) : String {
        return "$step: $errorMessage"
    }
    private fun separateResponseCodeFromCardData(strLogResp: String): String {
        return if (strLogResp.length > PREFIX_SIZE) {
            strLogResp.substring(Int.ZERO, PREFIX_SIZE)
        } else {
            strLogResp
        }
    }

    companion object {
        private const val GEN_ONE = "1"
        private const val GEN_TWO = "2"
        private const val SUCCESS_JNI = 1
        private const val SUCCESS_PREFIX = "0000"
        private const val PREFIX_SIZE = 4
        private const val RC_8303 = "8303"
        private const val EMPTY_LAST_BALANCE = 0
        const val ERROR_MESSAGE_ISODEP = "ISODep Connection Issue"
        const val TAG_PROCESS_SDK_BCALASTBCATOP_UP = "PROCESS_SDK_BCALASTBCATOPUP"
        const val TAG_PROCESS_SDK_REVERSAL = "PROCESS_SDK_REVERSAL"
        const val TAG_PROCESS_REVERSAL = "PROCESS_REVERSAL"
        const val TAG_PROCESS_ACK = "PROCESS_ACK"
        const val TAG_PROCESS_SDK_TOP_UP_2 = "PROCESS_SDK_TOP_UP_2"
        const val TAG_PROCESS_BETWEEN_TOP_UP = "PROCESS_BETWEEN_TOP_UP"
        const val TAG_PROCESS_SDK_TOP_UP_1 = "PROCESS_SDK_TOP_UP_1"
        const val TAG_PROCESS_SDK_SESSION_2 = "PROCESS_SDK_SESSION_2"
        const val TAG_PROCESS_SESSION_KEY = "PROCESS_SESSION_KEY"
        const val ERROR_SESSION_CARD_DATA_EMPTY = "PROCESS_SESSION_CARD_DATA_EMPTY"
        const val TAG_PROCESS_SDK_SESSION_1 = "PROCESS_SDK_SESSION_1"
        const val TAG_PROCESS_TRANSACTION_ID = "PROCESS_TRANSACTION_ID"
        const val ERROR_TRANSACTION_ID_EMPTY = "TRANSACTION_ID_EMPTY"
        const val TAG_PROCESS_PENDING_BALANCE = "PROCESS_PENDING_BALANCE"
        const val TAG_PROCESS_PENDING_BALANCE_GEN_1 = "PROCESS_PENDING_BALANCE_GEN_1"
        const val TAG_PROCESS_PENDING_BALANCE_GEN_2 = "PROCESS_PENDING_BALANCE_GEN_2"
    }
}
