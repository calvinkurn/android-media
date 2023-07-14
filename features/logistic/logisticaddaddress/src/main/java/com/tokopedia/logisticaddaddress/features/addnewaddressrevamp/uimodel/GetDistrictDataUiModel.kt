package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel

/**
 * Created by fwidjaja on 2019-05-16.
 */
data class GetDistrictDataUiModel(
    var title: String = "",
    var formattedAddress: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var provinceName: String = "",
    var districtName: String = "",
    var cityName: String = "",
    var districtId: Long = 0,
    var postalCode: String = "",
    var cityId: Long = 0,
    var provinceId: Long = 0,
    var errMessage: String? = null,
    var errorCode: Int = 0
)
