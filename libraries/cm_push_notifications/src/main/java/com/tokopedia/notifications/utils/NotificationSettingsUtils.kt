package com.tokopedia.notifications.utils

import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.common.NotificationSettingsGtmEvents
import com.tokopedia.notifications.domain.NotificationSettingTrackerUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


class NotificationSettingsUtils(private val context: Context) {

    private var notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val postNotificationPermission = "android.permission.POST_NOTIFICATIONS"
    private val userSession: UserSessionInterface = UserSession(context)
    private val sdkLevel33 = 33
    private val graphRepository: GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }

    private fun getSettingTrackerUseCase() : NotificationSettingTrackerUseCase {
        return NotificationSettingTrackerUseCase(graphRepository)
    }

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
        if(Build.VERSION.SDK_INT >= sdkLevel33) {
            try {
                NotificationSettingsGtmEvents(userSession, context).updateFrequency()
                NotificationSettingsGtmEvents(userSession, context).sendPromptImpressionEvent(context)
            } catch (_: Exception) {
            }
        }
    }

    fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= sdkLevel33) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    postNotificationPermission
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    getSettingTrackerUseCase().sendTrackerUserSettings({}, {})
                    NotificationSettingsGtmEvents(userSession, context).sendActionAllowEvent(context)
                } catch (_: Exception) {
                }

            } else if (ContextCompat.checkSelfPermission(
                    context,
                    postNotificationPermission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                try {
                    NotificationSettingsGtmEvents(userSession, context).sendActionNotAllowEvent(
                        context
                    )
                } catch (_: Exception) {
                }
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