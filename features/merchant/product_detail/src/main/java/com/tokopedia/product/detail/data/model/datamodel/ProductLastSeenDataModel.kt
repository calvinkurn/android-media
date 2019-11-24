package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductLastSeenDataModel(
        val type: String = "product_last_seen",
        val name: String = "product_last_seen",
        var lastSeen: String = ""
) : DynamicPDPDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}