@file:JvmName("BaseTradeInViewModel")
package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

open class BaseTradeInViewModel() : BaseViewModel() {
    protected var progBarVisibility = MutableLiveData<Boolean>()
    protected var warningMessage = MutableLiveData<String>()
    protected var errorMessage = MutableLiveData<String>()

    fun getProgBarVisibility(): LiveData<Boolean> {
        return progBarVisibility
    }

    fun getWarningMessage(): LiveData<String> {
        return warningMessage
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

}