package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant

data class ProductDetailDataModel(
    val layoutData: ProductInfoP1 = ProductInfoP1(),
    val listOfLayout: MutableList<DynamicPdpDataModel> = mutableListOf(),
    val variantData: ProductVariant? = null
)
