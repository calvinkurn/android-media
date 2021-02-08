package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class StepTwoResponse(
        @SerializedName("ticket_reply_attach")
        val ticketReplyAttach: TicketReplyAttach?
) {
    data class TicketReplyAttach(
            @SerializedName("data")
            val ticketReplyAttachData: TicketReplyAttachData?
    ) {
        data class TicketReplyAttachData(
                @SerializedName("is_success")
                val isSuccess: Int = 0,
                @SerializedName("status")
                val status: String? = ""
        )
    }
}