package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl

data class OrderTrackingStatusInfoUiModel(
    val stepperStatusList: List<StepperStatusUiModel>,
    val statusKey: String,
    val orderStatusTitle: String,
    val orderStatusSubTitle: String,
    val lottieUrl: String
) : BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}

data class StepperStatusUiModel(
    val isIconActive: Boolean,
    val isLineActive: Boolean,
    val iconName: Int
)