package com.tokopedia.settingnotif.usersetting.state

typealias PushNotif = NotificationItemState.PushNotif
typealias Email = NotificationItemState.Email
typealias Phone = NotificationItemState.Phone

sealed class NotificationItemState {
    object PushNotif: NotificationItemState()
    object Email: NotificationItemState()
    object Phone: NotificationItemState()
    object Troubleshooter: NotificationItemState()
}