package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

class ProductPreorderResponse(
        @SerializedName("duration_day")
        val durationDay: String = ""
)