package com.tokopedia.expresscheckout.data.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 13/12/18.
 */

data class ExpressCheckoutResponse(
        @SerializedName("header")
        val header: ExpressCheckoutFormHeader,

        @SerializedName("data")
        val data: ExpressCheckoutFormData,

        @SerializedName("status")
        val status: String
)