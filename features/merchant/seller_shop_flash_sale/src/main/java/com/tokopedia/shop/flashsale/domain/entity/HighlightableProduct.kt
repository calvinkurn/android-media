package com.tokopedia.shop.flashsale.domain.entity

data class HighlightableProduct(
    val id: String,
    val name: String,
    val imageUrl: String,
    val originalPrice: Long,
    val discountedPrice: Long,
    val discountPercentage: Int,
    val disabled: Boolean,
    val isSelected: Boolean
)