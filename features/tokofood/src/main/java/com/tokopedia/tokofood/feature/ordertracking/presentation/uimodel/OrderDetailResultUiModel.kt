package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory

data class OrderDetailResultUiModel(
    val orderDetailList: List<BaseOrderTrackingTypeFactory>,
    val foodItemList: List<BaseOrderTrackingTypeFactory>,
    val merchantData: MerchantDataUiModel,
    val orderStatusKey: String,
    val actionButtonsUiModel: ActionButtonsUiModel,
    val toolbarLiveTrackingUiModel: ToolbarLiveTrackingUiModel
)