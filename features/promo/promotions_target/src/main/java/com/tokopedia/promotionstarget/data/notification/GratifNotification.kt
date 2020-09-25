package com.tokopedia.promotionstarget.data.notification

import androidx.annotation.IntDef
import com.google.gson.annotations.SerializedName
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType.Companion.ORGANIC
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType.Companion.PUSH
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.ACTIVE_NON_PUSH_NOTIF
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.ACTIVE_PUSH_NOTIF
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.INACTIVE
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.SEEN

data class GratifNotificationResponse(
        @SerializedName("getNotification") val response: GratifNotification? = null
)

data class GratifNotification(
        @SerializedName("notificationID") val notificationID: String? = null,
        @SerializedName("promoCode") val promoCode: String? = null,
        @SerializedName("resultStatus") val resultStatus: ResultStatus?,
        @SerializedName("wordingActive") val wordingActive: Wording?,
        @SerializedName("wordingExpired") val wordingExpired: Wording?,
        @SerializedName("wordingUsed") val wordingUsed: Wording?,
        @SerializedName("button") val secondButton: SecondButton?,
        @NotificationStatusType @SerializedName("notificationStatus") val notificationStatus: Int? = null
)

data class ResultStatus(
        @SerializedName("code") val code: String?,
        @SerializedName("message") val message: List<String?>?,
        @SerializedName("reason") val reason: String?
)

data class Wording(
        @SerializedName("titleId") val title: String?,
        @SerializedName("subtitle1Id") val subtitle1: String?,
        @SerializedName("subtitle2Id") val subtitle2: String?
)

data class SecondButton(
        @SerializedName("text") val text: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("applink") val applink: String?,
        @SerializedName("isShown") val isShown: Boolean?
)


//todo Rahul check data type once api is finalized for https://tokopedia.atlassian.net/wiki/spaces/P/pages/930022376/GQL+Get+Notification+Detail
@Retention(AnnotationRetention.SOURCE)
@IntDef(ACTIVE_NON_PUSH_NOTIF, ACTIVE_PUSH_NOTIF, SEEN, INACTIVE)
annotation class NotificationStatusType {
    companion object {
        const val ACTIVE_NON_PUSH_NOTIF = 1
        const val ACTIVE_PUSH_NOTIF = 11
        const val SEEN = 20
        const val INACTIVE = 30
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(ORGANIC, PUSH)
annotation class NotificationEntryType {
    companion object {
        const val ORGANIC = 100
        const val PUSH = 200
    }
}

//@Retention(AnnotationRetention.SOURCE)
//@IntDef(ORGANIC, PUSH)
//annotation class NotificationEntryType {
//    companion object {
//        const val ORGANIC = 100
//        const val PUSH = 200
//    }
//}