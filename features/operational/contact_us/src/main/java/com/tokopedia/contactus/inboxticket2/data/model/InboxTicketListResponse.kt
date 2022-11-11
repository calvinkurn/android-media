package com.tokopedia.contactus.inboxticket2.data.model

import com.google.gson.annotations.SerializedName

data class InboxTicketListResponse(
        @SerializedName("ticket")
        val ticket: Ticket?
) {
    data class Ticket(
            @SerializedName("data")
            val TicketData: Data?,
            @SerializedName("status")
            val status: String?
    ) {
        data class Data(
                @SerializedName("is_success")
                val isSuccess: Int?,
                @SerializedName("next_page")
                val nextPage: String?,
                @SerializedName("previous_page")
                val previousPage: String?,
                @SerializedName("tickets")
                val ticketItems: List<TicketItem>?
        ) {
            data class TicketItem(
                    @SerializedName("case_number")
                    val caseNumber: String? = "",
                    @SerializedName("id")
                    val id: String? = "",
                    @SerializedName("is_official_store")
                    val isOfficialStore: Boolean = false,
                    @SerializedName("last_message")
                    val lastMessage: String? = "",
                    @SerializedName("last_update")
                    val lastUpdate: String? = "",
                    @SerializedName("message")
                    val message: String? = "",
                    @SerializedName("need_rating")
                    val needRating: Int = 0,
                    @SerializedName("read_status")
                    val readStatus: String? = "",
                    @SerializedName("read_status_id")
                    val readStatusId: Long = 0,
                    @SerializedName("status")
                    val status: String? = "",
                    @SerializedName("status_id")
                    val statusId: Long = 0,
                    @SerializedName("subject")
                    val subject: String? = "",
                    @SerializedName("url_detail")
                    val urlDetail: String? = ""
            )
        }
    }
}
