package com.tokopedia.mvc.domain.entity

data class Variant(
    val variantId: Long,
    val variantName: String,
    val combinations : List<Int>,
    val imageUrl: String,
    val price: Long,
    val isSelected: Boolean,
    val stockCount: Int,
    val soldCount: Int,
    val isEligible: Boolean,
    val reason: String,
    val isCheckable: Boolean,
    val isDeletable: Boolean
)
