package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory

data class OrderTrackingStatusInfoUiModel(
    val stepperStatusList: List<StepperStatusUiModel>,
    val orderStatusTitle: String,
    val orderStatusSubTitle: String,
    val lottieUrl: String
) : Visitable<OrderTrackingAdapterTypeFactory> {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class StepperStatusUiModel(
    val isActive: Boolean
)