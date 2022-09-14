package com.tokopedia.notifications.utils

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.NotificationSettingsGtmEvents
import com.tokopedia.user.session.UserSession


class NotificationSettingsUtils(private val context: Context) {

    private var notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val postNotificationPermission = "android.permission.POST_NOTIFICATIONS"
    private val userSession = UserSession(context)

    fun checkNotificationsModeForSpecificChannel(channel: String?): NotificationMode {
        return if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = if (channel.isNullOrBlank()) {
                    notificationManager.getNotificationChannel(CMConstant.NotificationChannel.CHANNEL_ID)
                } else {
                    notificationManager.getNotificationChannel(channel)
                }
                return if (notificationChannel != null && notificationChannel.importance == NotificationManager.IMPORTANCE_NONE) {
                    NotificationMode.CHANNEL_DISABLED
                } else {
                    NotificationMode.ENABLED
                }
            } else {
                NotificationMode.ENABLED
            }
        } else {
            NotificationMode.DISABLED
        }
    }

    fun sendNotificationPromptEvent() {
        try {
            NotificationSettingsGtmEvents(userSession).sendPromptImpressionEvent(context)
        } catch (e: Exception){
        }
    }

    fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                postNotificationPermission) == PackageManager.PERMISSION_GRANTED) {
            try {
                NotificationSettingsGtmEvents(userSession).sendActionAllowEvent(context)
            } catch (e: Exception){
            }

        } else if (ContextCompat.checkSelfPermission(
                context,
                postNotificationPermission) == PackageManager.PERMISSION_DENIED) {
            try {
                NotificationSettingsGtmEvents(userSession).sendActionNotAllowEvent(
                    context
                )
            } catch (e: Exception) {
            }
        }
    }

    enum class NotificationMode {
        ENABLED {
            override fun getEvent(): String {
                return "NOTIFICATION_ENABLED"
            }
        },
        DISABLED {
            override fun getEvent(): String {
                return "NOTIFICATION_DISABLED"
            }
        },
        CHANNEL_DISABLED {
            override fun getEvent(): String {
                return "NOTIFICATION_CHANNEL_DISABLED"
            }
        };

        abstract fun getEvent(): String
    }
}