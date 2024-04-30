package com.tokopedia.applink.notifsetting

import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created by @ilhamsuaib on 4/29/24.
 */

object DeeplinkMapperNotifSetting {

    fun getNotifSettingInternalDeepLink(type: NotifSettingType): String {
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
