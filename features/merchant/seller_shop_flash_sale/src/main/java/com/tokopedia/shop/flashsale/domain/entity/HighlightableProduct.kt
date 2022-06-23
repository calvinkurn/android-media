package com.tokopedia.shop.flashsale.domain.entity

data class HighlightableProduct(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val originalPrice: Long,
    val discountedPrice: Long,
    val discountPercentage: Int,
    val customStock: Long,
    val maxOrder: Int,
    val disabled: Boolean,
    val isSelected: Boolean,
    val position: Int,
)