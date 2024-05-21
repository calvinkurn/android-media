package com.tokopedia.settingnotif.usersetting.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class DeviceNotificationSettingActivity: BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showNotificationSettings(this)
        finish()
    }

    private fun showNotificationSettings(context: Context) {
        val notificationSettingsIntent = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O /*26*/ -> Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P /*28*/) {
                    flags += Intent.FLAG_ACTIVITY_NEW_TASK
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP /*21*/ -> Intent().apply {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra("app_package", context.packageName)
                putExtra("app_uid", context.applicationInfo.uid)
            }
            Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT /*19*/ -> Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:${context.packageName}")
            }
            else -> null
        }
        notificationSettingsIntent?.let(context::startActivity)
    }
}
