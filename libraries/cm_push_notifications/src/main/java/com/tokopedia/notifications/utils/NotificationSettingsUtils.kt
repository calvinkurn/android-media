package com.tokopedia.notifications.utils

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.notifications.common.CMConstant


class NotificationSettingsUtils(private val context: Context) {

    private var notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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