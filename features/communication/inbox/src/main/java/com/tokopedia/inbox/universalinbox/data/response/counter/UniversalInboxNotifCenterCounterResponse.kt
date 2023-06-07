package com.tokopedia.inbox.universalinbox.data.response.counter

import com.google.gson.annotations.SerializedName

data class UniversalInboxNotifCenterCounterResponse(
    @SerializedName("notif_unread")
    var notifUnread: String = ""
)
