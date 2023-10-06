package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel

sealed interface PinpointAction {
    object GetCurrentLocation : PinpointAction

    data class LocationInvalid(
        val type: Int,
        val title: String,
        val detail: String,
        val imageUrl: String,
        val showMapIllustration: Boolean,
        val buttonState: ButtonState?
    ) : PinpointAction {
        data class ButtonState(
            val variant: Int = -1,
            val type: Int = -1,
            val text: String = ""
        )

        val showButton = buttonState != null
    }

    data class MoveMap(val lat: Double, val long: Double) : PinpointAction

    data class UpdatePinpointDetail(val title: String, val description: String) : PinpointAction

    object InvalidDistrictPinpoint : PinpointAction
}
