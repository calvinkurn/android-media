package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.ElectronicMoneyEncryption
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.domain.request.BCAFlazzRequestMapper
import com.tokopedia.emoney.domain.request.BCAFlazzStatus
import com.tokopedia.emoney.domain.request.CommonBodyEnc
import com.tokopedia.emoney.domain.response.BCAFlazzData
import com.tokopedia.emoney.domain.response.BCAFlazzResponseMapper
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibrary
import com.tokopedia.emoney.integration.data.JNIResult
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject

class BCABalanceViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val bcaLibrary: BCALibrary,
    private val gson: Gson,
    private val electronicMoneyEncryption: ElectronicMoneyEncryption,
    private val bcaFlazzUseCase: GetBCAFlazzUseCase
) : BaseViewModel(dispatcher) {

    private var bcaInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val bcaInquiry: LiveData<EmoneyInquiry>
        get() = bcaInquiryMutable

    private var errorCardMessageMutable = SingleLiveEvent<Throwable>()
    val errorCardMessage: LiveData<Throwable>
        get() = errorCardMessageMutable

    fun processBCATagBalance(
        isoDep: IsoDep, merchantId: String, terminalId: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        strCurrDateTime: String,
        ATD: String,
    ) {
        val bcaMTId = BCAFlazzResponseMapper.bcaMTId(merchantId, terminalId)
        val setConfigResult = bcaLibrary.C_BCASetConfig(bcaMTId)
        if (isoDep != null) {
            run {
                try {
                    if (setConfigResult.isSuccess == SUCCESS_JNI) {
                        val dataBalance = checkLatestBalance()
                        if (dataBalance.isSuccess == SUCCESS_JNI) {
                            getPendingBalanceProcess(
                                isoDep, dataBalance.cardNo, dataBalance.balance,
                                rawPublicKeyString, rawPrivateKeyString, GEN_TWO, strCurrDateTime, ATD
                            )
                        } else {
                            isoDep.close()
                            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                        }
                    } else {
                        isoDep.close()
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    fun processBCACheckBalanceGen1(
        isoDep: IsoDep,
        rawPublicKeyString: String,
        rawPrivateKeyString: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val dataBalance = checkLatestBalance()
                    getPendingBalanceProcess(
                        isoDep, dataBalance.cardNo, dataBalance.balance,
                        rawPublicKeyString, rawPrivateKeyString, GEN_ONE, "", ""
                    )
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getPendingBalanceProcess(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String
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
                    rawPrivateKeyString, cardType, strCurrDateTime, ATD
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
                    result
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
            errorCardMessageMutable.postValue(it)
        }
    }

    private fun getGenerateTrxIdProcess(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
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
                    result.attributes.transactionID
                )
            } else if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.transactionID.isEmpty()) {
                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
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
            errorCardMessageMutable.postValue(it)
        }
    }

    private fun processSDKBCADataSession1(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String
    ) {
        if (isoDep != null) {
            run {
                try {
                    val bcaSession1 = bcaLibrary.C_BCAdataSession_1(
                        strTransactionId, ATD,
                        strCurrDateTime
                    )
                    if (bcaSession1.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(bcaSession1.strLogRsp)) {
                        getSessionKeyProcess(
                            isoDep, cardNumber, lastBalance, rawPublicKeyString,
                            rawPrivateKeyString, cardType, strCurrDateTime, ATD, strTransactionId,
                            separateCardDataFromResponseCode(bcaSession1.strLogRsp)
                        )
                    } else {
                        // set error if BCAdataSession_1 process error
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getSessionKeyProcess(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        cardDataSession1Key: String
    ) {
        launchCatchError(block = {
            val payloadGetSessionKeyQuery = BCAFlazzRequestMapper.createGetBCAGenerateSessionKey(
                gson, cardNumber, cardDataSession1Key, lastBalance, strTransactionId, cardType
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
                    rawPrivateKeyString, cardType, strCurrDateTime, ATD, strTransactionId, result
                )
            } else if (result.status == BCAFlazzStatus.WRITE.status && result.attributes.cardData.isEmpty()) {
                errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
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
            errorCardMessageMutable.postValue(it)
        }
    }

    private fun processSDKBCADataSession2(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData
    ) {
        if (isoDep != null) {
            run {
                try {
                    val bcaSession2 =
                        bcaLibrary.C_BCAdataSession_2(bcaFlazzData.attributes.cardData)
                    if (bcaSession2.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(bcaSession2.strLogRsp)) {
                        processSDKBCATopUp1(
                            isoDep, cardNumber, lastBalance, rawPublicKeyString,
                            rawPrivateKeyString, cardType, strCurrDateTime, ATD, strTransactionId,
                            bcaFlazzData
                        )
                    } else {
                        isoDep.close()
                        // set error if BCAdataSession_2 process error
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun processSDKBCATopUp1(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strCurrDateTime: String,
        ATD: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData
    ) {
        if (isoDep != null) {
            run {
                try {
                    val topUp1 = bcaLibrary.BCATopUp_1(
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
                            separateCardDataFromResponseCode(topUp1.strLogRsp),
                            ATD
                        )
                    } else {
                        isoDep.close()
                        // Bila respon dari BCATopUp_1 bukan SUCCESS
                        // (prefix SUCCESS : 0000), maka tidak perlu melakukan Reversal karena rekening sumber dana tidak terdebet
                        // src:Top Up Flazz di mch online-sosialisasi merchant 1222
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getBetweenTopUpProcess(
        isoDep: IsoDep,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData,
        cardDataTopUp1: String,
        ATD: String
    ) {
        launchCatchError(block = {
            val payloadGetBetweenTopUpQuery = BCAFlazzRequestMapper.createGetBCADataBetweenTopUp(
                gson, cardNumber, cardDataTopUp1, bcaFlazzData.attributes.amount, lastBalance,
                strTransactionId, cardType
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
                    cardType, strTransactionId, result
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
                    bcaFlazzData
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
                    bcaFlazzData
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
                bcaFlazzData
            )
        }
    }

    private fun processSDKBCATopUp2(
        isoDep: IsoDep,
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        bcaFlazzData: BCAFlazzData
    ) {
        if (isoDep != null) {
            run {
                try {
                    val topUp2 = bcaLibrary.BCATopUp_2(bcaFlazzData.attributes.cardData)
                    val updatedBalance = checkLatestBalance()
                    if (topUp2.isSuccess == SUCCESS_JNI
                        && getBCAPrefixSuccess(topUp2.strLogRsp)) {
                        getACKProcess(
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            separateCardDataFromResponseCode(topUp2.strLogRsp),
                            if (updatedBalance.isSuccess == SUCCESS_JNI) updatedBalance.balance else bcaFlazzData.attributes.lastBalance
                        )
                    } else if (topUp2.strLogRsp.isNotEmpty()) {
                        // Ada kemungkinan saldo Flazz sudah bertambah walaupun output BCATopUp2 bukan SUCCESS.
                        // Tetap kirimkan output BCATopUp2 ke BCA melalui API /flazz/ack
                        getACKProcess(
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            separateCardDataFromResponseCode(topUp2.strLogRsp),
                            if (updatedBalance.isSuccess == SUCCESS_JNI) updatedBalance.balance else bcaFlazzData.attributes.lastBalance
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
                            cardType
                        )
                    }

                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getACKProcess(
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        strTransactionId: String,
        cardDataTopUp2: String,
        updatedBalance: Int
    ) {
        launchCatchError(block = {
            val payloadGetACKQuery = BCAFlazzRequestMapper.createGetBCADataACKTopUp(
                gson, cardNumber, cardDataTopUp2, updatedBalance, strTransactionId, cardType
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
                    ackStatusOverride = true
                )
            )
        }) {
            errorCardMessageMutable.postValue(it)
        }
    }

    private fun processSDKReversal(
        isoDep: IsoDep,
        strTransactionId: String,
        ATD: String,
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
        bcaFlazzData: BCAFlazzData
    ) {
        if (isoDep != null) {
            run {
                try {
                    val reversal = bcaLibrary.BCAdataReversal(strTransactionId, ATD)
                    if (reversal.isSuccess == SUCCESS_JNI && getBCAPrefixSuccess(reversal.strLogRsp)) {
                        getReversalProcess(
                            cardNumber,
                            lastBalance,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            bcaFlazzData,
                            separateCardDataFromResponseCode(reversal.strLogRsp)
                        )
                    } else if(getBCARC8303(reversal.strLogRsp)) {
                        processSDKBCAlastBCATopUp(
                            isoDep,
                            strTransactionId,
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType
                        )
                    } else {
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
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
                gson, cardNumber, cardDataReversal, bcaFlazzData.attributes.amount, lastBalance,
                strTransactionId, cardType
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
            errorCardMessageMutable.postValue(it)
        }
    }

    private fun processSDKBCAlastBCATopUp(
        isoDep: IsoDep,
        strTransactionId: String,
        cardNumber: String,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
    ) {
        if (isoDep != null) {
            run {
                try {
                    val lastTopUp = bcaLibrary.BCAlastBCATopUp()
                    val updatedBalance = checkLatestBalance()
                    if (lastTopUp.isSuccess == SUCCESS_JNI) {
                        getACKProcess(
                            cardNumber,
                            rawPublicKeyString,
                            rawPrivateKeyString,
                            cardType,
                            strTransactionId,
                            separateCardDataFromResponseCode(lastTopUp.strLogRsp),
                            updatedBalance.balance
                        )
                    }
                } catch (e: Throwable) {
                    isoDep.close()
                    Timber.d(e)
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun checkLatestBalance(): JNIResult {
        return bcaLibrary.C_BCACheckBalance()
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

    companion object {
        private const val GEN_ONE = "1"
        private const val GEN_TWO = "2"
        private const val SUCCESS_JNI = 1
        private const val SUCCESS_PREFIX = "0000"
        private const val PREFIX_SIZE = 4
        private const val RC_8303 = "8303"
    }
}
