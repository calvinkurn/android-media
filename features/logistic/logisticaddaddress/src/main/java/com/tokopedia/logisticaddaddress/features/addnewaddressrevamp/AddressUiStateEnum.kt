package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

/**
 * Created by irpan on 20/06/23.
 */
enum class AddressUiStateEnum() {
    AddAddress, EditAddress, PinpointOnly
}

fun AddressUiStateEnum.isEdit(): Boolean {
    return this == AddressUiStateEnum.EditAddress
}

fun AddressUiStateEnum.isAdd(): Boolean {
    return this == AddressUiStateEnum.AddAddress
}

fun AddressUiStateEnum.isPinpointOnly(): Boolean {
    return this == AddressUiStateEnum.PinpointOnly
}
