package com.tokopedia.product.info.view

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import javax.inject.Inject

/**
 * Created by Yehezkiel on 13/10/20
 */
class BsProductDetailInfoViewModel @Inject constructor(dispatchers: DynamicProductDetailDispatcherProvider)
    : BaseViewModel(dispatchers.io()) {

    private val productShopId = MutableLiveData<Pair<String, String>>()

    fun setParams(productId: String, shopId: String) {
        productShopId.value = productId to shopId
    }
}