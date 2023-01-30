package com.tokopedia.shop.search.view.adapter.model

import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory

class ShopSearchProductFixedResultDataModel(
    var searchQuery: String = "",
    var searchTypeLabel: String = "",
    override var type: Type
) : ShopSearchProductDataModel() {

    override fun type(
        typeFactory: ShopSearchProductAdapterTypeFactory
    ) = typeFactory.type(this)
}
