package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class TicketDetailResponse(
        @SerializedName("tickets")
        val tickets: Tickets? = null,
        @SerializedName("is_success")
        var isSuccess: Int = 0
)

