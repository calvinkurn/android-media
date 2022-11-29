package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.common.constant.IS_FULFILLMENT_KEY
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter

class ShopProductFilterParameterSharedViewModel : ViewModel() {

    val sharedShopProductFilterParameter: LiveData<ShopProductFilterParameter>
        get() = _sharedSortData
    private val _sharedSortData = MutableLiveData<ShopProductFilterParameter>()

    var isFulfillmentFilterActive: Boolean = false

    fun changeSharedSortData(shopProductFilterParameter: ShopProductFilterParameter) {
        _sharedSortData.postValue(shopProductFilterParameter)
    }

    fun setFulfillmentFilterActiveStatus(mapParameter: Map<String, String>) {
        isFulfillmentFilterActive = if (mapParameter.containsKey(IS_FULFILLMENT_KEY)) {
            mapParameter[IS_FULFILLMENT_KEY] == true.toString()
        } else {
            false
        }
    }
}
