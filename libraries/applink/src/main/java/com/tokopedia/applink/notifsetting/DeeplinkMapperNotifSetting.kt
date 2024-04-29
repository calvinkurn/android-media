package com.tokopedia.applink.notifsetting

import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Created by @ilhamsuaib on 4/29/24.
 */

object DeeplinkMapperNotifSetting {

    private const val DEEPLINK_FORMAT = "%s?type=%s"

    fun getNotifSettingInternalDeepLink(type: NotifSettingType): String {
        return getDeeplinkByType(type)
    }

    private fun getDeeplinkByType(type: NotifSettingType): String {
        return String.format(DEEPLINK_FORMAT, ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING, type.value)
    }
}

sealed class NotifSettingType(val value: String = "") {
    object Default : NotifSettingType()
    object PushNotification : NotifSettingType("push_notification")
    object Email : NotifSettingType("email")
    object Sms : NotifSettingType("sms")
}
