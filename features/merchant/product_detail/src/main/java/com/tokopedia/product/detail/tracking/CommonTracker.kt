package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1

data class CommonTracker(
    private val productInfo: DynamicProductInfoP1,
    val userId: String
) {
    private val productBasic = productInfo.basic
    private val basicCategory = productBasic.category

    val shopId = productBasic.shopID
    val layoutName = productInfo.layoutName
    val categoryName = basicCategory.name
    val categoryId = basicCategory.id
    val productId = productBasic.productID
    val shopType = productInfo.shopTypeString
}