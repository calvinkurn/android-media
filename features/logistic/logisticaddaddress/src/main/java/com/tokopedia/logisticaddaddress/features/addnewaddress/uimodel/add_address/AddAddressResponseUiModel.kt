package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address

/**
 * Created by fwidjaja on 2019-05-28.
 */
data class AddAddressResponseUiModel (
        var data: AddAddressDataUiModel = AddAddressDataUiModel(),
        var status: String = ""
)