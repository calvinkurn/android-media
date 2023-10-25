package com.tokopedia.shop.info.domain.entity

data class Review(
    val rating: Int,
    val reviewTime: String,
    val reviewText: String,
    val reviewerName: String
)
