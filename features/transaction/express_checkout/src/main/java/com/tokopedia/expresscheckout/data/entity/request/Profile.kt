package com.tokopedia.expresscheckout.data.entity.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class Profile(
        @SerializedName("profile_id")
        var profileId: Int? = 0,

        @SerializedName("status")
        var status: Int? = 0,

        @SerializedName("address_id")
        var addressId: Int? = 0,

        @SerializedName("gateway_code")
        var gatewayCode: String? = null,

        @SerializedName("checkout_param")
        var checkoutParam: CheckoutParam? = null,

        @SerializedName("description")
        var description: String? = null
)