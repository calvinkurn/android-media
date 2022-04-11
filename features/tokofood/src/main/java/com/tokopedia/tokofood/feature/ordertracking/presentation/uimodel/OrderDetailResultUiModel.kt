package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory

data class OrderDetailResultUiModel(
    val orderDetailList: List<BaseOrderTrackingTypeFactory>,
    val foodItemList: List<BaseOrderTrackingTypeFactory>,
    val isOrderCompleted: Boolean,
    val actionButtonsUiModel: ActionButtonsUiModel,
    val toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel
)