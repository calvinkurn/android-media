package com.tokopedia.inbox.universalinbox.data.entity

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class UniversalInboxCounterWrapperResponse(
    @SerializedName("notifications")
    var allCounter: UniversalInboxAllCounterResponse = UniversalInboxAllCounterResponse()
)
data class UniversalInboxAllCounterResponse(
    @SerializedName("chat")
    var chatUnread: UniversalInboxChatCounterResponse = UniversalInboxChatCounterResponse(),

    @SerializedName("inbox")
    var othersUnread: UniversalInboxOthersCounterResponse = UniversalInboxOthersCounterResponse(),

    @SerializedName("notifcenter_unread")
    var notifCenterUnread: UniversalInboxNotifCenterCounterResponse = UniversalInboxNotifCenterCounterResponse()
)

data class UniversalInboxChatCounterResponse(
    @SerializedName("unreadsUser")
    var unreadBuyer: Int = Int.ZERO,

    @SerializedName("unreadsSeller")
    var unreadSeller: Int = Int.ZERO
)

data class UniversalInboxOthersCounterResponse(
    @SerializedName("talk")
    var discussionUnread: Int = Int.ZERO,

    @SerializedName("ticket")
    var helpUnread: Int = Int.ZERO,

    @SerializedName("review")
    var reviewUnread: Int = Int.ZERO
)

data class UniversalInboxNotifCenterCounterResponse(
    @SerializedName("notif_unread")
    var notifUnread: String = ""
)
