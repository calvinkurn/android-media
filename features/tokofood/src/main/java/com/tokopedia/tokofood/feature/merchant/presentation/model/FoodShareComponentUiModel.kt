package com.tokopedia.tokofood.feature.merchant.presentation.model

data class FoodShareComponentUiModel(
    val merchantName: String,
    val menuName: String,
    val price: String,
    val merchantLocation: String,
    val merchantUrl: String,
    val appLink: String
)