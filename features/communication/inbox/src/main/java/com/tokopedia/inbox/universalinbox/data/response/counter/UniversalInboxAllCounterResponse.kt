package com.tokopedia.inbox.universalinbox.data.response.counter

import com.google.gson.annotations.SerializedName

data class UniversalInboxAllCounterResponse(
    @SerializedName("chat")
    var chatUnread: UniversalInboxChatCounterResponse = UniversalInboxChatCounterResponse(),

    @SerializedName("inbox")
    var othersUnread: UniversalInboxOthersCounterResponse = UniversalInboxOthersCounterResponse(),

    @SerializedName("notifcenter_unread")
    var notifCenterUnread: UniversalInboxNotifCenterCounterResponse = UniversalInboxNotifCenterCounterResponse()
)
