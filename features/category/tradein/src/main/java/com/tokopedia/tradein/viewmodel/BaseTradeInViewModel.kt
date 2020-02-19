@file:JvmName("BaseTradeInViewModel")
package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tradein.TradeInRepository
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

open class BaseTradeInViewModel() : BaseViewModel() {
    protected var progBarVisibility = MutableLiveData<Boolean>()
    protected var warningMessage = MutableLiveData<String>()
    protected var errorMessage = MutableLiveData<String>()
    private var contextInterface: ContextInterface? = null


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

    fun setContextInterface(contextInterface: ContextInterface) {
        this.contextInterface = contextInterface
    }

    fun getResource(): Resources? {
        return contextInterface?.contextFromActivity?.resources
    }
}