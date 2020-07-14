package com.tokopedia.topads.dashboard.view.adapter.product.viewmodel

import com.tokopedia.topads.dashboard.data.model.nongroupItem.WithoutGroupDataItem
import com.tokopedia.topads.dashboard.view.adapter.product.ProductAdapterTypeFactory

class ProductItemViewModel( val data: WithoutGroupDataItem): ProductViewModel() {
    var isChecked = false

    override fun type(typesFactory: ProductAdapterTypeFactory): Int {
        return typesFactory.type(this)
    }

}