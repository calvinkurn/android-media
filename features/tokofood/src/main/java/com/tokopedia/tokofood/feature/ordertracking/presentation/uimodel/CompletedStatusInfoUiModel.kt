package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory

class CompletedStatusInfoUiModel(
    val orderStatus: String
) : BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}