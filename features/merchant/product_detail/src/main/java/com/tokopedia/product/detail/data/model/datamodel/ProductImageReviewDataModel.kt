package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory

data class ProductImageReviewDataModel(
        val type: String = "",
        val name: String = "",
        var productInfoP2General: ProductInfoP2General? = null
) : DynamicPDPDataModel {
    override fun name(): String = name
    override fun type(): String = type
    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }
}