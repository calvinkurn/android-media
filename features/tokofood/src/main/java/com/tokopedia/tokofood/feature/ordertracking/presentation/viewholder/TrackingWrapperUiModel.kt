package com.tokopedia.tokofood.feature.ordertracking.presentation.viewholder

import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.MerchantDataUiModel

data class TrackingWrapperUiModel(
    val orderId: String,
    val foodItems: List<FoodItemUiModel>,
    val merchantData: MerchantDataUiModel
)