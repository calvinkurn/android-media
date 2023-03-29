package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.AttributesEmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NFCUtils
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class JakCardBalanceViewModel @Inject constructor(val dispatcher: CoroutineDispatcher):
    BaseViewModel(dispatcher)  {

    private var errorCardMessageMutable = SingleLiveEvent<Throwable>()
    val errorCardMessage: LiveData<Throwable>
        get() = errorCardMessageMutable

    private var jakCardInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val jakCardInquiry: LiveData<EmoneyInquiry>
        get() = jakCardInquiryMutable

    lateinit var isoDep: IsoDep

    fun procesJakCardTagIntent(isoDep: IsoDep) {
        if (isoDep != null) {
            run {
                try {
                    this.isoDep = isoDep
                    isoDep.connect()
                    isoDep.timeout = TRANSCEIVE_TIMEOUT_IN_SEC // 7 sec time out

                    val selectRequest = NFCUtils.stringToByteArrayRadix(getSelectAIDCommand())
                    val selectResponse = isoDep.transceive(selectRequest)
                    if (NFCUtils.isCommandFailed(selectResponse)) {
                        errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                    } else {
                        val checkBalanceRequest = NFCUtils.stringToByteArrayRadix(COMMAND_CHECK_BALANCE)
                        val checkBalanceResponse = isoDep.transceive(checkBalanceRequest)

                        if (NFCUtils.isCommandFailed(checkBalanceResponse)) {
                            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                        } else {
                            val separatedHexResult = separateWithSuccessCode(NFCUtils.toHex(checkBalanceResponse))
                            val balance = convertHexBalanceToIntBalance(separatedHexResult)

                            jakCardInquiryMutable.postValue(
                                EmoneyInquiry(
                                    attributesEmoneyInquiry = AttributesEmoneyInquiry(
                                        lastBalance = balance,
                                        buttonText = "Halo"
                                    ),
                                    error = EmoneyInquiryError(
                                        status = 0,
                                    )
                                )
                            )
                        }
                    }

                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

    private fun getSelectAIDCommand(): String {
        return if(GlobalConfig.isAllowDebuggingTools()) {
            COMMAND_SELECT_JAK_CARD_STAG + COMMAND_AID_JAK_CARD_STAG
        } else {
            COMMAND_SELECT_JAK_CARD_PROD + COMMAND_AID_JAK_CARD_PROD
        }
    }

    private fun convertHexBalanceToIntBalance(hexBalance: String): Int {
        return hexBalance.toInt(RADIX_BALANCE)
    }

    private fun separateWithSuccessCode(response: String): String {
        val size = response.length
        val maxResponseLength = size - CODE_RESPONSE_SIZE
        return response.substring(Int.ZERO, maxResponseLength)
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
    }
}
