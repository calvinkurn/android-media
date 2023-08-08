package com.tokopedia.logisticCommon.uimodel

/**
 * Created by irpan on 20/06/23.
 */
enum class AddressUiState {
    AddAddress, EditAddress, PinpointOnly
}

fun AddressUiState?.isEdit(): Boolean {
    return this == AddressUiState.EditAddress
}

fun AddressUiState?.isAdd(): Boolean {
    return this == AddressUiState.AddAddress
}

fun AddressUiState?.isPinpointOnly(): Boolean {
    return this == AddressUiState.PinpointOnly
}

fun AddressUiState?.isEditOrPinpointOnly(): Boolean {
    return this == AddressUiState.PinpointOnly || this == AddressUiState.EditAddress
}

fun String?.toAddressUiState(): AddressUiState {
    return this?.let { value ->
        try {
            AddressUiState.valueOf(value)
        } catch (@Suppress("SwallowedException") e: IllegalArgumentException) {
            AddressUiState.AddAddress
        }
    } ?: AddressUiState.AddAddress
}
