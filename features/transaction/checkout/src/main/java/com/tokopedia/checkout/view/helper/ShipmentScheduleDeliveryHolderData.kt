package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import rx.subjects.PublishSubject

data class ShipmentScheduleDeliveryHolderData(
    val scheduleDeliveryUiModel: ScheduleDeliveryUiModel,
    val position: Int,
)

data class ShipmentScheduleDeliveryMapData(
    val donePublisher: PublishSubject<Boolean>,
    val shouldStopInClearCache: Boolean = false,
    val shouldStopInValidateUsePromo: Boolean = false
)
