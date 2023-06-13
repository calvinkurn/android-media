package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.common.util.ShopProductViewGridType

class ShopChangeProductGridSharedViewModel : ViewModel() {

    val sharedProductGridType: LiveData<ShopProductViewGridType>
        get() = _sharedProductGridType
    private val _sharedProductGridType = MutableLiveData<ShopProductViewGridType>()

    fun changeSharedProductGridType(gridType: ShopProductViewGridType) {
        _sharedProductGridType.postValue(gridType)
    }
}
