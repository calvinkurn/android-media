package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel

import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.uimodel.AddressUiState

sealed interface PinpointAction {
    object GetCurrentLocation : PinpointAction

    data class InvalidDistrictPinpoint(val errorText: String) : PinpointAction

    data class NetworkError(val errorText: String) : PinpointAction
}

data class MoveMap(val lat: Double, val long: Double)

sealed interface PinpointBottomSheetState {
    data class LocationInvalid(
        val type: LocationInvalidType,
        val showMapIllustration: Boolean,
        val buttonState: ButtonInvalidModel,
        val uiState: AddressUiState?,
        val uiModel: PinpointUiModel
    ) : PinpointBottomSheetState {

        enum class LocationInvalidType {
            OUT_OF_COVERAGE, LOCATION_NOT_FOUND
        }

        data class ButtonInvalidModel(
            val show: Boolean,
            val enable: Boolean = show
        )
    }

    data class LocationDetail(
        val title: String,
        val description: String,
        val buttonPrimary: PrimaryButtonUiModel,
        val buttonSecondary: SecondaryButtonUiModel
    ) : PinpointBottomSheetState {
        data class SecondaryButtonUiModel(
            val show: Boolean,
            val state: AddressUiState?,
            val enable: Boolean
        )

        data class PrimaryButtonUiModel(
            val show: Boolean,
            val enable: Boolean,
            val addressUiState: AddressUiState?,
            val success: Boolean
        )
    }
}

sealed interface ChoosePinpoint {
    data class SetPinpointResult(
        val saveAddressDataModel: SaveAddressDataModel,
        val pinpointUiModel: PinpointUiModel
    ) : ChoosePinpoint

    data class GoToAddressForm(
        val saveChanges: Boolean,
        val pinpointUiModel: PinpointUiModel,
        val addressState: AddressUiState?,
        val source: String,
        val isPositiveFlow: Boolean
    ) : ChoosePinpoint
}
