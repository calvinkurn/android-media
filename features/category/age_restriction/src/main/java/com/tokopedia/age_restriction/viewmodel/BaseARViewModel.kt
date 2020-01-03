package com.tokopedia.age_restriction.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

open class BaseARViewModel() : BaseViewModel() {
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
}