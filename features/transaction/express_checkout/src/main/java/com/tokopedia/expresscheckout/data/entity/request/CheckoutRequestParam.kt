package com.tokopedia.expresscheckout.data.entity.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class CheckoutRequestParam(
        @SerializedName("carts")
        var carts: Cart? = null,

        @SerializedName("profile")
        var profile: Profile? = null
)