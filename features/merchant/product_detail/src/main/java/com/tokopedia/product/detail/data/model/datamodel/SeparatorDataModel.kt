package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class SeparatorDataModel(
        val type: String = "",
        val name: String = ""
) : DynamicPDPDataModel {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}