package com.tokopedia.product.detail.tracking

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1

data class CommonTracker(
    val productInfo: DynamicProductInfoP1,
    val userId: String
) {
    private val productBasic by lazy { productInfo.basic }
    private val basicCategory by lazy { productBasic.category }

    val shopId by lazy { productBasic.shopID }
    val layoutName by lazy { productInfo.layoutName }
    val categoryName by lazy { basicCategory.name }
    val categoryId by lazy { basicCategory.id }
    val productId by lazy { productBasic.productID }
    val shopType by lazy { productInfo.shopTypeString }
    val categoryChildId by lazy { basicCategory.detail.lastOrNull()?.id ?: "" }
}
