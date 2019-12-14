package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductValuePropositionDataModel(
        val type: String = "",
        val name: String = "",
        var isOfficialStore: Boolean = false
) : DynamicPDPDataModel {
    override fun type(): String = type
    override fun name(): String = name

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

}