package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory


class ProductItemViewModel(var data: TopAdsProductModel) : ProductViewModel() {

    var isChecked: Boolean = false

    override fun type(typesFactory: ProductListAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }
}