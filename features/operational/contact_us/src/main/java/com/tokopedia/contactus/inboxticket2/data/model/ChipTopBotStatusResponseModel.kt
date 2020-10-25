package com.tokopedia.contactus.inboxticket2.data.model


import com.google.gson.annotations.SerializedName

data class ChipTopBotStatusResponseModel(
        @SerializedName("data")
        var chipTopBotStatusResponse: ChipTopBotStatusResponse?
) {
    data class ChipTopBotStatusResponse(
            @SerializedName("chipTopBotStatus")
            var chipTopBotStatus: ChipTopBotStatus?
    ) {
        data class ChipTopBotStatus(
                @SerializedName("data")
                var statusData: StatusData,
                @SerializedName("message_error")
                var messageError: List<Any>,
                @SerializedName("server_process_time")
                var serverProcessTime: String,
                @SerializedName("status")
                var status: String
        ) {
            data class StatusData(
                    @SerializedName("is_active")
                    var isActive: Boolean,
                    @SerializedName("is_success")
                    var isSuccess: Int,
                    @SerializedName("message_id")
                    var messageId: Int,
                    @SerializedName("unread_notif")
                    var unreadNotif: Boolean,
                    @SerializedName("welcome_message")
                    var welcomeMessage: String
            )
        }
    }
}