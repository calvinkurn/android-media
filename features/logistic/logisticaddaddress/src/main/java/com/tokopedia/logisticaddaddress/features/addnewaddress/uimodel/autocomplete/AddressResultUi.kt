package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete

import com.tokopedia.logisticaddaddress.features.autocomplete.AutoCompleteAdapter

data class AddressResultUi(
        var addrId: Int = 0,
        var addrName: String = "",
        var address1: String = "",
        var latitude: String = "",
        var longitude: String = ""
): AutoCompleteAdapter.AutoCompleteVisitable