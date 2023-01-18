package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName

data class InboxTicketListResponse(
        @SerializedName("ticket")
        val ticket: Ticket? = null
) {
    data class Ticket(
        @SerializedName("data")
            val ticketData: Data? = null,
        @SerializedName("status")
            val status: String? = null
    ) {
        data class Data(
                @SerializedName("is_success")
                val isSuccess: Int? = null,
                @SerializedName("next_page")
                val nextPage: String? = null,
                @SerializedName("previous_page")
                val previousPage: String? = null,
                @SerializedName("tickets")
                val ticketItems: List<TicketItem>? = null
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

            fun getTicketList() = ticketItems?: arrayListOf()

            fun getNextUrl() = nextPage.orEmpty()

            fun getPrevUrl() = previousPage.orEmpty()
        }

        fun getTicket() = ticketData?: Data()
    }

    fun getTicketOnInbox() = ticket?: Ticket()
}
