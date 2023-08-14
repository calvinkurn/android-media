package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopHeaderDynamicUspSharedViewModel : ViewModel() {

    val sharedDynamicUspValue: LiveData<String>
        get() = _sharedDynamicUspValue
    private val _sharedDynamicUspValue = MutableLiveData<String>()

    fun updateSharedDynamicUspValue(dynamicUspValue: String) {
        _sharedDynamicUspValue.postValue(dynamicUspValue)
    }
}
