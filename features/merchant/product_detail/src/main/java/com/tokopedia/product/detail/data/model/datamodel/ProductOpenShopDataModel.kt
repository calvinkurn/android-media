package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductOpenShopDataModel(
        val title: String = ""
) : DynamicPdpDataModel {
    override fun type(): String = ""

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = ""
}