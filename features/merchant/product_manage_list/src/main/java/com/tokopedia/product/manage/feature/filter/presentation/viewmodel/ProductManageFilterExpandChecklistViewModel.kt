package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ProductManageFilterExpandChecklistViewModel @Inject constructor(): ViewModel() {

    private val _dataSize = MutableLiveData<Int>()
    val dataSize: LiveData<Int>
    get() = _dataSize

    fun updateDataSize(dataLength: Int) {
        _dataSize.postValue(dataLength)
    }

}