package com.tokopedia.promotionstarget.data.notification

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus.Companion.AUTH_ERROR
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus.Companion.DATA_ERROR
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus.Companion.GENERAL_ERROR
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus.Companion.INVALID_REQ_PARAM_ERROR
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus.Companion.NOT_ELIGIBLE
import com.tokopedia.promotionstarget.data.notification.GratifResultStatus.Companion.SUCCESS
import com.tokopedia.promotionstarget.data.notification.HachikoButtonType.Companion.DISMISS
import com.tokopedia.promotionstarget.data.notification.HachikoButtonType.Companion.REDIRECT
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType.Companion.ORGANIC
import com.tokopedia.promotionstarget.data.notification.NotificationEntryType.Companion.PUSH
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.ACTIVE_NON_PUSH_NOTIF
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.ACTIVE_PUSH_NOTIF
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.INACTIVE
import com.tokopedia.promotionstarget.data.notification.NotificationStatusType.Companion.SEEN
import com.tokopedia.promotionstarget.data.notification.PopupType.Companion.ACTIVE
import com.tokopedia.promotionstarget.data.notification.PopupType.Companion.EXPIRED
import com.tokopedia.promotionstarget.data.notification.PopupType.Companion.UNKNOWN
import com.tokopedia.promotionstarget.data.notification.PopupType.Companion.USED

data class GratifNotificationResponse(
        @SerializedName("getNotification") val response: GratifNotification? = null
)

data class GratifNotification(
        @SerializedName("notificationID") val notificationID: String? = null,
        @SerializedName("eventID") val eventID: String? = null,
        @SerializedName("promoCode") val promoCode: String? = null,
        @SerializedName("resultStatus") val resultStatus: ResultStatus?,
        @SerializedName("wordingActive") val wordingActive: Wording?,
        @SerializedName("wordingExpired") val wordingExpired: Wording?,
        @SerializedName("wordingUsed") val wordingUsed: Wording?,
        @SerializedName("button") val secondButton: SecondButton?,
        @SerializedName("hachikoButton") val hachikoButton: HachikoButton?,
        @NotificationStatusType @SerializedName("notificationStatus") val notificationStatus: Int? = null
)

data class ResultStatus(
        @GratifResultStatus @SerializedName("code") val code: String?,
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
        @SerializedName("appLink") val applink: String?,
        @SerializedName("isShown") val isShown: Boolean?,
        @SerializedName("type") val type: String?
)

data class HachikoButton(
        @HachikoButtonType @SerializedName("type") val type: String?
)


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
@IntDef(ACTIVE, SEEN, USED, EXPIRED, UNKNOWN)
annotation class PopupType {
    companion object {
        const val ACTIVE = 1
        const val SEEN = 2
        const val USED = 3
        const val EXPIRED = 4
        const val UNKNOWN = 5
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

@Retention(AnnotationRetention.SOURCE)
@StringDef(SUCCESS, NOT_ELIGIBLE, DATA_ERROR, GENERAL_ERROR, AUTH_ERROR, INVALID_REQ_PARAM_ERROR)
annotation class GratifResultStatus {
    companion object {
        const val SUCCESS = "200000"
        const val NOT_ELIGIBLE = "300007"
        const val DATA_ERROR = "300002"
        const val GENERAL_ERROR = "100002"
        const val AUTH_ERROR = "401"
        const val INVALID_REQ_PARAM_ERROR = "400"
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(REDIRECT, DISMISS)
annotation class HachikoButtonType {
    companion object {
        const val REDIRECT = "redirect"
        const val DISMISS = "dismiss"
    }
}