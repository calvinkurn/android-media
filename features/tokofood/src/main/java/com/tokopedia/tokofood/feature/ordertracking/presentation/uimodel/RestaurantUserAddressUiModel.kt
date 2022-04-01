package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory

data class RestaurantUserAddressUiModel(
    val merchantName: String,
    val distanceInKm: String,
    val destinationLabel: String,
    val destinationPhone: String,
    val destinationAddress: String
): BaseOrderTrackingTypeFactory {

    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}