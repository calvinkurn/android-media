package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactory
import com.tokopedia.kotlin.model.ImpressHolder

data class ProductLastSeenDataModel(
        val type: String = ProductDetailConstant.PRODUCT_LAST_SEEN,
        val name: String = ProductDetailConstant.PRODUCT_LAST_SEEN,
        var lastSeen: String = ""
) : DynamicPdpDataModel, ImpressHolder() {
    override fun type(): String = type

    override fun type(typeFactory: DynamicProductDetailAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun name(): String = name
}