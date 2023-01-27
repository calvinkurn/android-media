package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel

data class ShipmentScheduleDeliveryHolderData(
    val scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
    val position: Int,
)
