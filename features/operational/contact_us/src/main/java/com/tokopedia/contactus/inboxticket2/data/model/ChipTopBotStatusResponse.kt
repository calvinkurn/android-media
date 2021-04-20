package com.tokopedia.contactus.inboxticket2.data.model


import com.google.gson.annotations.SerializedName

data class ChipTopBotStatusResponse(
        @SerializedName("chipTopBotStatusInbox")
        val chipTopBotStatusInbox: ChipTopBotStatusInbox?
) {
    data class ChipTopBotStatusInbox(
            @SerializedName("data")
            val chipTopBotStatusData: ChipTopBotStatusData?,
            @SerializedName("message_error")
            val messageError: List<String>?,
            @SerializedName("status")
            val status: String?
    ) {
        data class ChipTopBotStatusData(
                @SerializedName("is_active")
                val isActive: Boolean = false,
                @SerializedName("is_success")
                val isSuccess: Int = 0,
                @SerializedName("message_id")
                val messageId: String?,
                @SerializedName("unread_notif")
                val unreadNotif: Boolean = false,
                @SerializedName("welcome_message")
                val welcomeMessage: String?
        )
    }
}