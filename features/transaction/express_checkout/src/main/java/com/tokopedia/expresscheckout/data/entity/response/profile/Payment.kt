package com.tokopedia.expresscheckout.data.entity.response.profile

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Payment(
        @SerializedName("gateway_code")
        val gatewayCode: String,

        @SerializedName("checkout_param")
        val checkoutParam: String,

        @SerializedName("image")
        val image: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("url")
        val url: String
)