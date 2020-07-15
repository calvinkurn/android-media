package com.tokopedia.exploreCategory.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel

open class BaseECViewModel : BaseViewModel() {
    protected var shimmerVisibility = MutableLiveData<Boolean>()
    protected var errorMessage = MutableLiveData<String>()

    fun getShimmerVisibility(): LiveData<Boolean> {
        return shimmerVisibility
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }
}
