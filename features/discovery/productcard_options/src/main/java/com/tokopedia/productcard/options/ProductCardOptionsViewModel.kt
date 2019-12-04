package com.tokopedia.productcard.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.model.ProductCardOptionsModel

internal class ProductCardOptionsViewModel(
        dispatcherProvider: DispatcherProvider,
        productCardOptionsModel: ProductCardOptionsModel
): BaseViewModel(dispatcherProvider.ui()) {

    private val productCardOptionsItemList = MutableLiveData<List<Any>>()

    init {

    }

    fun getItemList(): LiveData<List<Any>> {
        return productCardOptionsItemList
    }
}