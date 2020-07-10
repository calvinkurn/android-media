package com.tokopedia.troubleshooter.notification.ui.uiview

sealed class ConfigState {
    object PushNotification: ConfigState()
    object Notification: ConfigState()
    object Categories: ConfigState()
    object Ringtone: ConfigState()
}