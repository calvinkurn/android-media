package com.tokopedia.expresscheckout.data.entity.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Data(
        @SerializedName("applink")
        val applink: String,

        @SerializedName("callback_url")
        val callbackUrl: String,

        @SerializedName("reflect")
        val reflect: Reflect,

        @SerializedName("redirect_param")
        val redirectParam: String
)