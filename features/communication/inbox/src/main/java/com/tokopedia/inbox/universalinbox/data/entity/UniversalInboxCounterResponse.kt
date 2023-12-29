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

    @SerializedName("inbox_counter")
    var inboxCounter: UniversalInboxNotifCenterCounterWrapperResponse = UniversalInboxNotifCenterCounterWrapperResponse()
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

data class UniversalInboxNotifCenterCounterWrapperResponse(
    @SerializedName("all")
    var notifCenterWrapperUnread: UniversalInboxNotifCenterCounterResponse =
        UniversalInboxNotifCenterCounterResponse()
)

data class UniversalInboxNotifCenterCounterResponse(
    @SerializedName("notifcenter_int")
    var notifUnread: String = ""
)
