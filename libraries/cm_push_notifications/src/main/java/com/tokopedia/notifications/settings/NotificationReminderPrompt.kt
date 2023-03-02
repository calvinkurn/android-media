package com.tokopedia.notifications.settings

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity


class NotificationReminderPrompt(private val view: NotificationGeneralPromptView) {

    fun showReminderPrompt(activity: FragmentActivity) {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_DENIED
        ) {
            view.show(true)
        }
    }
}
