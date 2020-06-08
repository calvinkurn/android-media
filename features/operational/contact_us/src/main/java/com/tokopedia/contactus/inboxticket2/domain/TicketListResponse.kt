package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class TicketListResponse(
        @SerializedName("next_page")
        val nextPage: String? = null,
        @SerializedName("tickets")
        val tickets: List<TicketsItem>? = null,
        @SerializedName("previous_page")
        val previousPage: String? = null,
        @SerializedName("is_success")
        var isSuccess: Int = 0
)