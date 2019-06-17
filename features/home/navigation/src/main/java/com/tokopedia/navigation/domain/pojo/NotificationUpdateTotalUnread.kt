package com.tokopedia.navigation.domain.pojo

/**
 * @author : Steven 11/04/19
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationUpdateTotalUnread(
        @Expose
        @SerializedName("notifcenter_total_unread")
        var pojo: NotificationUpdateTotalUnreadPojo = NotificationUpdateTotalUnreadPojo()
)

data class NotificationUpdateTotalUnreadPojo(
        @Expose
        @SerializedName("notif_total_unread")
        var notifUnreadString: String = "",
        @Expose
        @SerializedName("notif_total_unread_int")
        var notifUnreadInt: Long = 0
)