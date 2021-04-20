package com.tokopedia.troubleshooter.notification.ui.state

sealed class ConfigState {
    object PushNotification: ConfigState()
    object Notification: ConfigState()
    object Device: ConfigState()
    object Ringtone: ConfigState()
    object Undefined: ConfigState()
}