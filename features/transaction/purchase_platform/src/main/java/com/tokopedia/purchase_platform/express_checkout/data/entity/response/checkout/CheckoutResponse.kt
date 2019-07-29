package com.tokopedia.purchase_platform.express_checkout.data.entity.response.checkout

import com.google.gson.annotations.SerializedName
import com.tokopedia.transactiondata.entity.response.expresscheckout.Header

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class CheckoutResponse(
        @SerializedName("header")
        val header: Header,

        @SerializedName("data")
        val data: CheckoutData,

        @SerializedName("status")
        val status: String
)