package com.tokopedia.age_restriction.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

open class BaseARViewModel() : BaseViewModel() {
    private var progBarVisibility = MutableLiveData<Boolean>()
    private var warningMessage = MutableLiveData<String>()
    private var errorMessage = MutableLiveData<String>()


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