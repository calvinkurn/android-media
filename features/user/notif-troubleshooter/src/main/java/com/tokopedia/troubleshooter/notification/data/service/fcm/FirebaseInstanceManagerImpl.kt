package com.tokopedia.troubleshooter.notification.data.service.fcm

import com.google.firebase.messaging.FirebaseMessaging

class FirebaseInstanceManagerImpl: FirebaseInstanceManager {

    override fun getNewToken(token: (String) -> Unit, error: (Throwable) -> Unit) {
        try {
            val firebaseMessaging = FirebaseMessaging.getInstance().token
            firebaseMessaging.addOnSuccessListener {
                if (!it.isNullOrEmpty()) {
                    token(it)
                }
            }

            firebaseMessaging.addOnFailureListener {
                error(it)
            }
        } catch (t: Throwable) {
            error(t)
        }
    }

}