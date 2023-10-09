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
import com.tokopedia.emoney.domain.response.JakCardData
import com.tokopedia.emoney.domain.response.JakCardDataEnc
import com.tokopedia.emoney.domain.usecase.GetBCAFlazzUseCase
import com.tokopedia.emoney.integration.BCALibrary
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class BCABalanceViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    private val bcaLibrary: BCALibrary,
    private val gson: Gson,
    private val electronicMoneyEncryption: ElectronicMoneyEncryption,
    private val bcaFlazzUseCase: GetBCAFlazzUseCase
): BaseViewModel(dispatcher) {

    private var bcaInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val bcaInquiry: LiveData<EmoneyInquiry>
        get() = bcaInquiryMutable

    private var errorCardMessageMutable = SingleLiveEvent<Throwable>()
    val errorCardMessage: LiveData<Throwable>
        get() = errorCardMessageMutable

    fun processBCATagBalance(isoDep: IsoDep, merchantId: String, terminalId: String,
                             rawPublicKeyString: String,
                             rawPrivateKeyString: String) {
        val bcaMTId = BCAFlazzResponseMapper.bcaMTId(merchantId, terminalId)
        bcaLibrary.C_BCASetConfig(bcaMTId)
        val bcaGetConfig = bcaLibrary.C_BCAGetConfig()
        if (isoDep != null) {
            run {
                try {
                    val dataBalance = bcaLibrary.C_BCACheckBalance()
                    getPendingBalanceProcess(dataBalance.cardNo, dataBalance.balance, rawPublicKeyString,
                        rawPrivateKeyString, GEN_TWO)
                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    fun processBCACheckBalanceGen1(isoDep: IsoDep,
                                   rawPublicKeyString: String,
                                   rawPrivateKeyString: String) {
        if (isoDep != null) {
            run {
                try {
                    val dataBalance = bcaLibrary.C_BCACheckBalance()
                    getPendingBalanceProcess(dataBalance.cardNo, dataBalance.balance, rawPublicKeyString,
                        rawPrivateKeyString, GEN_ONE)
                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getPendingBalanceProcess(
        cardNumber: String,
        lastBalance: Int,
        rawPublicKeyString: String,
        rawPrivateKeyString: String,
        cardType: String,
    ){
        launchCatchError(block = {
            val payloadGetPendingBalanceQuery = BCAFlazzRequestMapper.createGetPendingBalanceParam(
                gson, cardNumber, lastBalance, cardType
            )
            val encParam = electronicMoneyEncryption.createEncryptedPayload(rawPublicKeyString, payloadGetPendingBalanceQuery)
            val paramGetPendingBalanceQuery = BCAFlazzRequestMapper.createEncryptedParam(encParam.first, encParam.second)

            val encResult = bcaFlazzUseCase.execute(paramGetPendingBalanceQuery)
            val result = decryptPayload(encResult.data, rawPrivateKeyString)

            if (result.status == BCAFlazzStatus.WRITE.status) {

            } else {
                bcaInquiryMutable.postValue(BCAFlazzResponseMapper.bcaMapper(cardNumber, lastBalance, result.attributes.imageIssuer, getIsBCAGenOne(cardType), result.attributes.amount))
            }
        }) {
            errorCardMessageMutable.postValue(it)
        }
    }

    private fun decryptPayload(bcaFlazzDataEnc: CommonBodyEnc, rawPrivateKeyString: String): BCAFlazzData {
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

    companion object {
        private const val GEN_ONE = "1"
        private const val GEN_TWO = "2"
    }
}
