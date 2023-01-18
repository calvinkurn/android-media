package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class RatingResponse(
    @SerializedName("data")
        var data: Data? = null,
    @SerializedName("is_success")
        var isSuccess: Int = 0
)
