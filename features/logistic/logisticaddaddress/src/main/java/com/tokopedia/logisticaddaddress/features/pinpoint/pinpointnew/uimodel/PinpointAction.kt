package com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.uimodel

import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.uimodel.AddressUiState

sealed interface PinpointAction {
    object GetCurrentLocation : PinpointAction

    data class InvalidDistrictPinpoint(val errorText: String) : PinpointAction

    data class NetworkError(val errorText: String) : PinpointAction
}

data class MoveMap(val lat: Double, val long: Double)

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
