package com.tokopedia.troubleshooter.notification.ui.uiview

sealed class ConfigState {
    object PushNotification: ConfigState()
    object Notification: ConfigState()
    object Device: ConfigState()
    object Ringtone: ConfigState()
    object Channel: ConfigState()
}