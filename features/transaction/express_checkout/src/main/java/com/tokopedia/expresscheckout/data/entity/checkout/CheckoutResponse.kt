package com.tokopedia.expresscheckout.data.entity.checkout

import com.google.gson.annotations.SerializedName
import com.tokopedia.expresscheckout.data.entity.Header

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