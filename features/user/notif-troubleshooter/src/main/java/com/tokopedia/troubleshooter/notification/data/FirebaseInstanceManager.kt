package com.tokopedia.troubleshooter.notification.data

interface FirebaseInstanceManager {
    fun getNewToken(token: (String) -> Unit)
}