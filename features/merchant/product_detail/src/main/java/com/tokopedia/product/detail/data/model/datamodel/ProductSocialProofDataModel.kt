package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductSocialProofDataModel(
        val type: String = "",
        val name: String = "",
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