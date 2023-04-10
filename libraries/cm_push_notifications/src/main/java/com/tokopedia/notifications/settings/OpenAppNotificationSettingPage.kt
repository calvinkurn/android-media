package com.tokopedia.notifications.settings

import android.content.Intent
import androidx.fragment.app.FragmentActivity


class OpenAppNotificationSettingPage {

    fun goToAppNotificationSettingsPage(activity: FragmentActivity) {
        val intent = Intent()
        intent.action = APP_SETTING
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(APP_PACKAGE, activity.packageName)
        intent.putExtra(APP_UID, activity.applicationInfo?.uid)
        intent.putExtra(APP_PACKAGE_NAME, activity.packageName)
        activity.startActivity(intent)
    }

    companion object {
        const val APP_SETTING = "android.settings.APP_NOTIFICATION_SETTINGS"
        const val APP_PACKAGE = "app_package"
        const val APP_UID = "app_uid"
        const val APP_PACKAGE_NAME = "android.provider.extra.APP_PACKAGE"
    }
}
