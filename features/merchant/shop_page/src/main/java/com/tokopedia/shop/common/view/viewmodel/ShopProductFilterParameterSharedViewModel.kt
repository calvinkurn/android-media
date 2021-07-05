package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter

class ShopProductFilterParameterSharedViewModel : ViewModel() {

    val sharedShopProductFilterParameter: LiveData<ShopProductFilterParameter>
        get() = _sharedSortData
    private val _sharedSortData = MutableLiveData<ShopProductFilterParameter>()

    fun changeSharedSortData(shopProductFilterParameter: ShopProductFilterParameter){
        _sharedSortData.postValue(shopProductFilterParameter)
    }
}