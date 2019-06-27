package com.tokopedia.navigation.domain.pojo

/**
 * @author : Steven 11/04/19
 */
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotificationUpdateUnread(
        @Expose
        @SerializedName("notifcenter_unread")
        var pojo: NotificationUpdateUnreadPojo = NotificationUpdateUnreadPojo()
)

data class NotificationUpdateUnreadPojo(
        @Expose
        @SerializedName("notif_unread")
        var notifUnreadString: String = "",
        @Expose
        @SerializedName("notif_unread_int")
        var notifUnreadInt: Long = 0
)