package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSocialProofDataModel(
        val type: String = "",
        val name: String = "",
        val dataLayout: List<ComponentData> = listOf(),
        //P1
        var dynamicProductInfoP1: DynamicProductInfoP1? = null,
        //P2
        var rating: Rating? = null,
        var wishListCount: Int = 0
) : DynamicPDPDataModel {
    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}