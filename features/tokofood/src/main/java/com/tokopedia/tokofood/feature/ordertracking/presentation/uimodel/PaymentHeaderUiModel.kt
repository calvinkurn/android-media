package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactoryImpl

class PaymentHeaderUiModel: BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}