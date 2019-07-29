package com.tokopedia.purchase_platform.express_checkout.data.entity.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class CheckoutExpressGqlResponse(
        @SerializedName("express_checkout")
        val checkoutResponse: CheckoutResponse
)