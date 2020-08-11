package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopSortSharedViewModel : ViewModel() {

    val sharedSortData: LiveData<Pair<String, String>>
        get() = _sharedSortData
    private val _sharedSortData = MutableLiveData<Pair<String, String>>()

    fun changeSharedSortData(sortId: String, sortName: String){
        _sharedSortData.postValue(Pair(sortId, sortName))
    }
}