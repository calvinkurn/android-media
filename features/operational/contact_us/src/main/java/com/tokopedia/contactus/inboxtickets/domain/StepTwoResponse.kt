package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class StepTwoResponse(
    @SerializedName("ticket_reply_attach")
    val ticketReplyAttach: TicketReplyAttach? = null
) {
    data class TicketReplyAttach(
        @SerializedName("data")
        val ticketReplyAttachData: TicketReplyAttachData? = null
    ) {

        fun getAttachment() = ticketReplyAttachData ?: TicketReplyAttachData()

        data class TicketReplyAttachData(
            @SerializedName("is_success")
            val isSuccess: Int = 0,
            @SerializedName("status")
            val status: String? = ""
        )
    }

    fun getTicketAttach() = ticketReplyAttach ?: TicketReplyAttach()
}
