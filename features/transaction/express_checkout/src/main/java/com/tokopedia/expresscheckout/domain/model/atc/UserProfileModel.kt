package com.tokopedia.expresscheckout.domain.model.atc;

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class UserProfileModel(
        var code: Int? = null,
        var message: Int? = null,
        var addressId: Int? = null,
        var receiverName: String? = null,
        var addressStreet: String? = null,
        var districtName: String? = null,
        var cityName: String? = null,
        var provinceName: String? = null,
        var phoneNo: String? = null,
        var gatewayCode: String? = null,
        var checkoutParam: String? = null,
        var image: String? = null,
        var description: String? = null,
        var url: String? = null,
        var serviceId: Int? = null
)