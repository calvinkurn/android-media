package com.tokopedia.contactus.inboxticket2.data.model

import com.google.gson.annotations.SerializedName

class Data {
    @SerializedName("tickets")
    var tickets: Tickets? = null
    @SerializedName("isSuccess")
    var isSuccess = 0
}