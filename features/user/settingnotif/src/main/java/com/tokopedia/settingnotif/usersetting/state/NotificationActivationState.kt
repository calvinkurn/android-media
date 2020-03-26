package com.tokopedia.settingnotif.usersetting.state

typealias PushNotif = NotificationActivationState.PushNotif
typealias Email = NotificationActivationState.Email
typealias Phone = NotificationActivationState.Phone

sealed class NotificationActivationState {
    object PushNotif: NotificationActivationState()
    object Email: NotificationActivationState()
    object Phone: NotificationActivationState()
}