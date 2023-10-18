package com.tokopedia.notifications.settings

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.tokopedia.notifications.common.CMNotificationUtils

class NotificationReminderPrompt(private val view: NotificationGeneralPromptView) {

    fun showReminderPrompt(activity: FragmentActivity, pageName: String) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED &&
            CMNotificationUtils.validateReminderPromptDisplay(activity.applicationContext, pageName)
        ) {
            view.show(true)
        }
    }
}
