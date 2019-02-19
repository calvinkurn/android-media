package com.tokopedia.expresscheckout.data.entity.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class CheckoutData(
        @SerializedName("success")
        val success: Int,

        @SerializedName("error")
        val error: String,

        @SerializedName("error_state")
        val errorState: Int,

        @SerializedName("message")
        val message: String,

        @SerializedName("data")
        val data: Data
)