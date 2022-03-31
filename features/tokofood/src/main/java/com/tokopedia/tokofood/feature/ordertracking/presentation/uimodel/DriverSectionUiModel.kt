package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory


data class DriverSectionUiModel(
    val driverInformationList: List<DriverInformation> = emptyList(),
    val name: String,
    val photoUrl: String,
    val phone: String,
    val licensePlateNumber: String
): BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class DriverInformation(
    val iconInformation: String = "",
    val informationName: String = ""
)