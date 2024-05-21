package com.tokopedia.applink.notifsetting

import android.annotation.SuppressLint
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created by @ilhamsuaib on 4/29/24.
 */

object DeeplinkMapperNotifSetting {

    @SuppressLint("PII Data Exposure")
    fun getEmailNotifSettingInternalDeepLink(): String {
        return getNotifSettingInternalDeepLink(NotifSettingType.Email)
    }

    fun getNotifSettingInternalDeepLink(type: NotifSettingType = NotifSettingType.Default): String {
        return if (type.value.isBlank()) {
            ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
        } else {
            ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING + "?type=" + type.value
        }
    }
}

sealed class NotifSettingType(val value: String = "") {
    object Default : NotifSettingType()
    object PushNotification : NotifSettingType("push_notification")
    object Email : NotifSettingType("email")
    object Sms : NotifSettingType("sms")
}
