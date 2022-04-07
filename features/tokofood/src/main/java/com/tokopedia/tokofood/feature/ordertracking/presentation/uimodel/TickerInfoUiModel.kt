package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory
import com.tokopedia.unifycomponents.ticker.TickerData


data class TickerInfoData(
    val tickerList: List<TickerData>
): BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}