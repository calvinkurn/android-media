package com.tokopedia.notifications.utils

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.NotificationSettingsGtmEvents
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class NotificationSettingsUtils(private val context: Context) {

    private var notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val userSession: UserSessionInterface = UserSession(context)

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                NotificationSettingsGtmEvents(userSession, context).sendPromptImpressionEvent(context)
            } catch (_: Exception) {
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
