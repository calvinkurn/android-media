package com.tokopedia.logisticseller.ui.confirmshipping.data.model

import com.tokopedia.targetedticker.domain.TickerModel

data class ConfirmShippingState(
    val referenceNumber: String = "",
    val mode: ConfirmShippingMode = ConfirmShippingMode.CONFIRM_SHIPPING,
    val loading: Boolean = true,
    val courierList: List<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>? = null,
    val chosenCourier: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment? = null,
    val chosenService: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage? = null,
    val tickerData: TickerModel? = null,
)

data class ConfirmShippingErrorState(
    val throwable: Throwable,
    val source: ConfirmShippingErrorStateSource
)

enum class ConfirmShippingErrorStateSource {
    COURIER_LIST, TARGETED_TICKER
}

enum class ConfirmShippingMode {
    CHANGE_COURIER, CONFIRM_SHIPPING
}

sealed interface ConfirmShippingEvent {
    data class OnCreate(
        val orderId: String,
        val mode: ConfirmShippingMode
    ) : ConfirmShippingEvent

    object Loading : ConfirmShippingEvent

    data class ChooseCourier(
        val courier: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment
    ) : ConfirmShippingEvent

    data class ChooseService(
        val service: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment.ShipmentPackage
    ) : ConfirmShippingEvent

    data class ChangeRefNum(
        val refNum: String
    ) : ConfirmShippingEvent

    data class Submit(val orderId: String) : ConfirmShippingEvent

    data class SwitchMode(val isChangeCourier: Boolean) : ConfirmShippingEvent
}

sealed interface ConfirmShippingResult {
    data class ShippingConfirmed(val message: String?) : ConfirmShippingResult
    data class CourierChanged(val message: String?) : ConfirmShippingResult
    data class FailedConfirmShipping(val throwable: Throwable) : ConfirmShippingResult
    data class FailedChangeCourier(val throwable: Throwable) : ConfirmShippingResult
}
