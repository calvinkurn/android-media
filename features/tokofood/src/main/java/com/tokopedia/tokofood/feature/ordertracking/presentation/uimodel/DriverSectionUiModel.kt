package com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel

import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.OrderTrackingAdapterTypeFactory


data class DriverSectionUiModel(
    val driverInformationList: List<DriverInformationUiModel> = emptyList(),
    val name: String,
    val photoUrl: String,
    val phone: String,
    val licensePlateNumber: String,
    val isCallable: Boolean
): BaseOrderTrackingTypeFactory {
    override fun type(typeFactory: OrderTrackingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class DriverInformationUiModel(
    val iconInformation: Int?,
    val informationName: String
)