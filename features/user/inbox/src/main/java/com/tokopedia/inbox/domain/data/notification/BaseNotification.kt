package com.tokopedia.inbox.domain.data.notification

import com.google.gson.annotations.SerializedName

abstract class BaseNotification(
        @SerializedName("chat_int")
        var chatInt: Int = 0,
        @SerializedName("notifcenter_int")
        var notifcenterInt: Int = 0,
        @SerializedName("talk_int")
        var talkInt: Int = 0,
        @SerializedName("total_int")
        var totalInt: Int = 0
) {

}