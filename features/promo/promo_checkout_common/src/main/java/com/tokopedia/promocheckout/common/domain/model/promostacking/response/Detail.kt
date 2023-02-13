package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-01.
 */

data class Detail(
        @SerializedName("description")
        val description: String = "",

        @SerializedName("type")
        val type: String = "",

        @SerializedName("amount_str")
        val amountStr: String,

        @SerializedName("amount")
        val amount: Long
)