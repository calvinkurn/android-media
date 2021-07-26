package com.tokopedia.troubleshooter.notification.analytics

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.troubleshooter.notification.ui.state.DeviceSettingState
import com.tokopedia.troubleshooter.notification.ui.uiview.UserSettingUIView
import com.tokopedia.troubleshooter.notification.util.isNotNull
import com.tokopedia.usecase.coroutines.Result
import timber.log.Timber

object TroubleshooterTimber {

    fun token(token: String) {
        Timber.d("Troubleshoot Token $token")
    }

    fun notificationSetting(notification: UserSettingUIView) {
        Timber.d("Notifikasi yang aktif ${notification.totalOn} dari ${notification.notifications}")
    }

    fun combine(
            token: Result<String>?,
            notification: Result<UserSettingUIView>?,
            device: Result<DeviceSettingState>?
    ) {
        if (token.isNotNull() && notification.isNotNull() && device.isNotNull()) {
            ServerLogger.log(Priority.P2, "LOG_PUSH_NOTIF", mapOf("type" to "Troubleshooter",
                    "token" to token?.toString().orEmpty(),
                    "setting" to notification?.toString().orEmpty(),
                    "device" to device?.toString().orEmpty()
            ))
        }
    }


}