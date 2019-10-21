package com.tokopedia.purchase_platform.features.express_checkout.data.entity.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class CheckoutRequestParam(
        @SerializedName("carts")
        var carts: Cart? = null,

        @SerializedName("profile")
        var profile: Profile? = null,

        @SerializedName("client_id")
        var clientId: String? = null,

        @SerializedName("fingerprint_support")
        var fingerprintSupport: String? = "false",

        @SerializedName("fingerprint_publickey")
        var fingerprintPublicKey: String? = null
)