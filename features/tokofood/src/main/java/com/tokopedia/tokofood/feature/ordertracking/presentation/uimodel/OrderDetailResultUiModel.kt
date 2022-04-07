package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory

data class OrderDetailResultUiModel(
    val orderDetailList: List<BaseOrderTrackingTypeFactory>,
    val isOrderCompleted: Boolean
)