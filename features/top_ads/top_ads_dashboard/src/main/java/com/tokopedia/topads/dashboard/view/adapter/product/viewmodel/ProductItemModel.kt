package com.tokopedia.topads.dashboard.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapterTypeFactory

class ProductItemModel(val data: WithoutGroupDataItem) : ProductModel() {
    var isChecked = false
    var isChanged = false
    var valueChanged = false

    override fun type(typesFactory: ProductAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}