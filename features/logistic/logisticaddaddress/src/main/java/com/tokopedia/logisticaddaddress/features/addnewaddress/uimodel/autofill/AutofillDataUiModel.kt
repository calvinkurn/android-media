package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill

/**
 * Created by fwidjaja on 2019-05-17.
 */
data class AutofillDataUiModel (
        var title: String = "",
        var formattedAddress: String = "",
        var latitude: String = "",
        var longitude: String = "",
        var districtId: Int = 0,
        var provinceId: Int = 0,
        var cityId: Int = 0,
        var postalCode: String = "")