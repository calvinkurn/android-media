package com.tokopedia.contactus.inboxtickets.data.model


import com.google.gson.annotations.SerializedName

data class TicketReplyResponse(
    @SerializedName("ticket_reply")
    val ticketReply: TicketReply? = null
) {
    data class TicketReply(
        @SerializedName("data")
        val ticketReplyData: Data? = null
    ) {
        data class Data(
            @SerializedName("post_key")
            val postKey: String = "",
            @SerializedName("status")
            val status: String = ""
        )

        fun getTicketReplayData() = ticketReplyData?: Data()

    }

    fun getTicketReplay() = ticketReply?: TicketReply()
}
