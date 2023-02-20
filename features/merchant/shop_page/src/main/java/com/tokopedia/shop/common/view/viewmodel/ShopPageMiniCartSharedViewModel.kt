package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

class ShopPageMiniCartSharedViewModel : ViewModel() {

    val miniCartSimplifiedData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartSimplifiedData
    private val _miniCartSimplifiedData = MutableLiveData<MiniCartSimplifiedData>()

    fun updateSharedMiniCartData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartSimplifiedData.postValue(miniCartSimplifiedData)
    }
}
