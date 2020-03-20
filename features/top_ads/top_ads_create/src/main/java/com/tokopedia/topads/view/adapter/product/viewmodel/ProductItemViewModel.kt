package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.data.response.ResponseProductList
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
class ProductItemViewModel(var data: ResponseProductList.Result.TopadsGetListProduct.Data) : ProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}