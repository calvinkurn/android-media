package com.tokopedia.troubleshooter.notification.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.provider.Settings.ACTION_SOUND_SETTINGS

const val INTENT_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS"

fun Context?.gotoNotificationSetting() {
    val context = this ?: return

    startActivity(Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                action = INTENT_SETTINGS
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
            else -> {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + context.packageName)
            }
        }
    })
}

fun Context?.gotoDeviceSettings() {
    this?.startActivity(Intent(Settings.ACTION_SETTINGS))
}

fun Context?.gotoAudioSetting() {
    this?.startActivity(Intent(ACTION_SOUND_SETTINGS))
}