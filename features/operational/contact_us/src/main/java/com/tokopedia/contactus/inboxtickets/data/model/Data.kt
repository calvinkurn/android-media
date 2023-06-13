package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("tickets")
    var tickets: Tickets? = null,
    @SerializedName("isSuccess")
    var isSuccess: Int = 0
)
