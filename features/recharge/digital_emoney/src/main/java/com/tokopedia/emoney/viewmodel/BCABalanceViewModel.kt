package com.tokopedia.emoney.viewmodel

import android.nfc.tech.IsoDep
import android.util.Log
import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.emoney.domain.response.BCAResponseMapper
import com.tokopedia.emoney.integration.BCALibrary
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import java.io.IOException
import javax.inject.Inject

class BCABalanceViewModel @Inject constructor(
    dispatcher: CoroutineDispatcher,
    val bcaLibrary: BCALibrary
): BaseViewModel(dispatcher) {

    private var bcaInquiryMutable = SingleLiveEvent<EmoneyInquiry>()
    val bcaInquiry: LiveData<EmoneyInquiry>
        get() = bcaInquiryMutable

    private var errorCardMessageMutable = SingleLiveEvent<Throwable>()
    val errorCardMessage: LiveData<Throwable>
        get() = errorCardMessageMutable

    fun processBCATagBalance(isoDep: IsoDep, merchantId: String, terminalId: String) {
        val bcaMTId = BCAResponseMapper.bcaMTId(merchantId, terminalId)
        val bcaSetConfig = bcaLibrary.C_BCASetConfig(bcaMTId)
        if (isoDep != null) {
            run {
                try {
                    val bcaCheckBalanceResult = bcaLibrary.C_BCACheckBalance()
                    val mappedResult = BCAResponseMapper.bcaMapper(bcaCheckBalanceResult, false, isExtraPendingBalance = false, bcaCheckBalanceResult.balance) //TODO Change to pending balance
                    val bcaGetConfig = bcaLibrary.C_BCAGetConfig()
                    val bcaLastTopUp = bcaLibrary.BCAlastBCATopUp()
                    Log.d("SETCONFIGG", bcaSetConfig.strLogRsp+" _hahaha_ "+bcaGetConfig.strConfig+ "_hahahahaha_"+bcaLastTopUp.strLogRsp)
                    //TODO Add Extra Balance from GQL
                    bcaInquiryMutable.postValue(mappedResult)
                } catch (e: IOException) {
                    isoDep.close()
                    errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
                }
            }
        } else {
            errorCardMessageMutable.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
        }
    }

}
