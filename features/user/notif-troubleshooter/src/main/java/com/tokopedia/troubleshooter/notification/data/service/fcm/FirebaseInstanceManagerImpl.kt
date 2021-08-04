package com.tokopedia.troubleshooter.notification.data.service.fcm

import com.google.firebase.installations.FirebaseInstallations

class FirebaseInstanceManagerImpl: FirebaseInstanceManager {

    override fun getNewToken(token: (String) -> Unit, error: (Throwable) -> Unit) {
        try {
            FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener { task ->
                if (!task.isSuccessful || task.result?.token == null) {
                    error(Throwable("get a new token isn't successful"))
                    return@addOnCompleteListener
                }

                task.result?.token?.let { token(it) }
            }
        } catch (t: Throwable) {
            error(t)
        }
    }

}