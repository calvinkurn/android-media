package com.tokopedia.troubleshooter.notification.data.service.fcm

import com.google.firebase.iid.FirebaseInstanceId

class FirebaseInstanceManagerImpl: FirebaseInstanceManager {

    override fun getNewToken(token: (String) -> Unit, error: (Throwable) -> Unit) {
        try {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                if (!task.isSuccessful) throw Throwable()
                if (task.result?.token == null) throw Throwable()
                task.result?.token?.let { newToken ->
                    token(newToken)
                }
            }
        } catch (t: Throwable) {
            error(t)
        }
    }

}