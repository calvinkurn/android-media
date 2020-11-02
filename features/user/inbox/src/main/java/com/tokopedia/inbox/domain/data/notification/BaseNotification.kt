package com.tokopedia.inbox.domain.data.notification

import com.google.gson.annotations.SerializedName

abstract class BaseNotification(
        @SerializedName("chat_int")
        val chatInt: Int = 0,
        @SerializedName("notifcenter_int")
        val notifcenterInt: Int = 0,
        @SerializedName("talk_int")
        val talkInt: Int = 0,
        @SerializedName("total_int")
        val totalInt: Int = 0
) {

}