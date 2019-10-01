package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-01.
 */

data class TickerInfo(
        @SerializedName("unique_id")
        val uniqueId: String = "",

        @SerializedName("status_code")
        val statusCode: Int = 0,

        @SerializedName("message")
        val message: String = ""
)