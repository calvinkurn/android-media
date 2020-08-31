package com.tokopedia.contactus.inboxticket2.data.model


import com.google.gson.annotations.SerializedName

data class TicketReplyResponse(
    @SerializedName("ticket_reply")
    val ticketReply: TicketReply?
) {
    data class TicketReply(
        @SerializedName("data")
        val ticketReplyData: Data?
    ) {
        data class Data(
            @SerializedName("post_key")
            val postKey: String = "",
            @SerializedName("status")
            val status: String = ""
        )
    }
}