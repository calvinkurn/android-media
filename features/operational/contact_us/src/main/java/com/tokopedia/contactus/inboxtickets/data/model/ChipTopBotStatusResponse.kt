package com.tokopedia.contactus.inboxtickets.data.model

import com.google.gson.annotations.SerializedName

data class ChipTopBotStatusResponse(
        @SerializedName("chipTopBotStatusInbox")
        val chipTopBotStatusInbox: ChipTopBotStatusInbox? = null
) {
    data class ChipTopBotStatusInbox(
        @SerializedName("data")
            val chipTopBotStatusData: ChipTopBotStatusData?  = null,
        @SerializedName("message_error")
            val messageError: List<String>? = null,
        @SerializedName("status")
            val status: String?= null
    ) {
        data class ChipTopBotStatusData(
                @SerializedName("is_active")
                val isActive: Boolean = false,
                @SerializedName("is_success")
                val isSuccess: Int = 0,
                @SerializedName("message_id")
                val messageId: String? = null,
                @SerializedName("unread_notif")
                val unreadNotif: Boolean = false,
                @SerializedName("welcome_message")
                val welcomeMessage: String? = null
        ){
            fun getMessageID() = messageId.orEmpty()

            fun getMessageWelcome() = welcomeMessage.orEmpty()
        }

        fun getTopBotStatusData() = chipTopBotStatusData?: ChipTopBotStatusData()
    }

    fun getTopBotStatusInbox() = chipTopBotStatusInbox?: ChipTopBotStatusInbox()
}
