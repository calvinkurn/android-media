package com.tokopedia.shop.common.domain.entity

data class ShopPrefetchData(
    val shopAvatar: String,
    val shopName: String,
    val shopBadge: String,
    val shopLastOnline: String,
    val shopRating: Float,
    val isFollowed: Boolean
)
