package com.tokopedia.product_bundle.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class ProductBundleViewModel @Inject constructor(
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun getProductInfo() {

    }

    fun getBundleInfo() {

    }
}