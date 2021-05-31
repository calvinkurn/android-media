package com.tokopedia.troubleshooter.notification.data.service.fcm

interface FirebaseInstanceManager {
    fun getNewToken(token: (String) -> Unit, error: (Throwable) -> Unit)
}