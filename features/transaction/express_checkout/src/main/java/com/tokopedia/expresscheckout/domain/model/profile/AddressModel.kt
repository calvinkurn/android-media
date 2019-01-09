package com.tokopedia.expresscheckout.domain.model.profile

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class AddressModel(
        var addressId: Int = 0,
        var receiverName: String? = null,
        var addressName: String? = null,
        var addressStreet: String? = null,
        var districtId: Int = 0,
        var districtName: String? = null,
        var cityId: Int = 0,
        var cityName: String? = null,
        var provinceId: Int = 0,
        var provinceName: String? = null,
        var phone: String? = null,
        var longitude: String? = null,
        var latitude: String? = null,
        var postalCode: String? = null
)