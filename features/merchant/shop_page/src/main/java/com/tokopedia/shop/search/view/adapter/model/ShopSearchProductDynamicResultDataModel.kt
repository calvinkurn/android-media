package com.tokopedia.shop.search.view.adapter.model

import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory

class ShopSearchProductDynamicResultDataModel(
    var imageUri: String = "",
    var name: String = "",
    var price: String = "",
    var appLink: String = "",
    var url: String = "",
    var searchQuery: String = "",
    override var type: Type = Type.TYPE_PDP
) : ShopSearchProductDataModel() {

    override fun type(
        typeFactory: ShopSearchProductAdapterTypeFactory
    ) = typeFactory.type(this)
}
