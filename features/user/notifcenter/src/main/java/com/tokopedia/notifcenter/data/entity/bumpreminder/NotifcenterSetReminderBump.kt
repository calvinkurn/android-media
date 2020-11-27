package com.tokopedia.notifcenter.data.entity.bumpreminder


import com.google.gson.annotations.SerializedName

data class NotifcenterSetReminderBump(
        @SerializedName("data")
        val `data`: Data = Data(),
        @SerializedName("links")
        val links: Links = Links(),
        @SerializedName("message_error")
        val messageError: List<String> = listOf(),
        @SerializedName("server")
        val server: String = "",
        @SerializedName("server_proccess_time")
        val serverProccessTime: Int = 0,
        @SerializedName("status")
        val status: String = ""
)