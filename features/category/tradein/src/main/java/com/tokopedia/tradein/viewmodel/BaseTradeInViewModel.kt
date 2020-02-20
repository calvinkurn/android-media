@file:JvmName("BaseTradeInViewModel")
package com.tokopedia.tradein.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.tradein.TradeInRepository
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.tradein.view.viewcontrollers.ContextInterface

open class BaseTradeInViewModel() : BaseViewModel() {
    protected var progBarVisibility = MutableLiveData<Boolean>()
    protected var warningMessage = MutableLiveData<String>()
    protected var errorMessage = MutableLiveData<String>()
    private var contextInterface: ContextInterface? = null

    fun getProgBarVisibility(): LiveData<Boolean> {
        return progBarVisibility
    }

    fun getWarningMessage(): LiveData<String> {
        return warningMessage
    }

    fun getErrorMessage(): LiveData<String> {
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