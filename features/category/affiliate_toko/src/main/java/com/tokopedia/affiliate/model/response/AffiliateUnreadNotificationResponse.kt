package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateUnreadNotificationResponse(
    @SerializedName("notifcenter_unread")
    val totalUnread: TotalUnread?
) {
    data class TotalUnread(
        @SerializedName("notif_unread_int")
        val totalUnreadCount: Int
    )
}
