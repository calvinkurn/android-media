@file:JvmName("BaseTradeInViewModel")
package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tradein.TradeInRepository
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

open class BaseTradeInViewModel() : BaseViewModel() {
    protected var progBarVisibility = MutableLiveData<Boolean>()
    protected var warningMessage = MutableLiveData<String>()
    protected var errorMessage = MutableLiveData<String>()


    fun getProgressBarVisibility(): MutableLiveData<Boolean> {
        return progBarVisibility
    }

    fun getWarningmessage(): MutableLiveData<String> {
        return warningMessage
    }

    fun getErrormessage(): MutableLiveData<String> {
        return errorMessage
    }

    fun getMYRepository(): TradeInRepository {
        return TradeInRepository()
    }
}