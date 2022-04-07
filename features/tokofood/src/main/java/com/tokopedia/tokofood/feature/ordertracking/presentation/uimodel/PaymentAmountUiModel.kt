package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl

data class PaymentAmountUiModel(
    val grandTotalValue: String,
    val totalLabel: String
): BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}