package com.tokopedia.troubleshooter.notification.analytics

import com.tokopedia.troubleshooter.notification.ui.uiview.DeviceSettingState
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
            token: String?,
            notification: Result<UserSettingUIView>?,
            device: Result<DeviceSettingState>?
    ) {
        if (token.isNotNull() && notification.isNotNull() && device.isNotNull()) {
            Timber.w("P2#LOG_PUSH_NOTIF#'Troubleshooter';token='%s';setting='%s';device='%s';",
                    token,
                    notification,
                    device
            )
        }
    }

}