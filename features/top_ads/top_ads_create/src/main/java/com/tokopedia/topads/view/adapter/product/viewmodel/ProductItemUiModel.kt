package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory

/**
 * Author errysuprayogi on 12,November,2019
 */
class ProductItemUiModel(var data: TopAdsProductModel) : ProductUiModel() {

    var isChecked: Boolean = false
    var isCompact: Boolean = false

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}
