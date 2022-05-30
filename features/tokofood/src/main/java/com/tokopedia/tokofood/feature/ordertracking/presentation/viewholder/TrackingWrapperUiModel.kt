package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel

data class TrackingWrapperUiModel(
    val orderId: String,
    val shopId: String,
    val foodItems: List<FoodItemUiModel>
)