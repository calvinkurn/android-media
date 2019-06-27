package com.tokopedia.expresscheckout.data.entity.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class PaymentDetail(
        @SerializedName("name")
        val name: String,

        @SerializedName("amount")
        val amount: Int
)