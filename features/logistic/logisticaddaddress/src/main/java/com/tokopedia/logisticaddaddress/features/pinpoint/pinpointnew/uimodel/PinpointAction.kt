package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel

import com.tokopedia.logisticCommon.uimodel.AddressUiState

sealed interface PinpointAction {
    object GetCurrentLocation : PinpointAction

    data class LocationInvalid(
        val show: Boolean,
        val type: Int,
        val title: String,
        val detail: String,
        val imageUrl: String,
        val showMapIllustration: Boolean,
        @Deprecated("use pinpoint button ui model")
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

data class PinpointButtonUiModel(
    val show: Boolean = false,
    val text: String = "",
    val variant: Int = -1,
    val type: Int = -1,
    val state: PinpointButtonEnum,
    val enable: Boolean
)

interface BasePinpointButtonUiModel<T> {
    val show: Boolean
    val text: String
    val variant: Int
    val type: Int
    val state: T
    val enable: Boolean
}

sealed interface BottomSheetState {
    data class LocationInvalid(
        val type: LocationInvalidType,
        val title: String,
        val detail: String,
        val imageUrl: String,
        val showMapIllustration: Boolean,
        val buttonState: ButtonInvalidModel
    ) : BottomSheetState {

        enum class LocationInvalidType {
            OUT_OF_COVERAGE, LOCATION_NOT_FOUND
        }

        data class ButtonInvalidModel(
            override val show: Boolean,
            override val state: LocationInvalidType,
            override val enable: Boolean = show,
            override val variant: Int = -1,
            override val type: Int = -1,
            override val text: String = ""
        ) : BasePinpointButtonUiModel<LocationInvalidType>
    }

    data class LocationDetail(
        val title: String,
        val description: String,
        val buttonPrimary: PrimaryButtonUiModel,
        val buttonSecondary: LocationDetailButtonUiModel
    ) : BottomSheetState {
        data class LocationDetailButtonUiModel(
            override val show: Boolean,
            override val state: AddressUiState?,
            override val enable: Boolean,
            override val variant: Int = -1,
            override val type: Int = -1,
            override val text: String = ""
        ) : BasePinpointButtonUiModel<AddressUiState?>

        data class PrimaryButtonUiModel(
            override val show: Boolean,
            override val state: PrimaryButtonState,
            override val enable: Boolean,
            override val variant: Int = -1,
            override val type: Int = -1,
            override val text: String = ""
        ) : BasePinpointButtonUiModel<PrimaryButtonUiModel.PrimaryButtonState> {
            data class PrimaryButtonState(val addressUiState: AddressUiState?, val success: Boolean)
        }
    }
}

enum class PinpointButtonEnum
