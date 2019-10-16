package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 2019-10-01.
 */

data class TickerInfo(
        @SerializedName("message")
        val message: String = "",

        @SerializedName("status_code")
        val statusCode: Int = 0,

        @SerializedName("unique_id")
        val uniqueId: String = ""
)