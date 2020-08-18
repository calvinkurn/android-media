package com.tokopedia.troubleshooter.notification.data.service.fcm

import com.google.firebase.iid.FirebaseInstanceId

class FirebaseInstanceManagerImpl: FirebaseInstanceManager {

    override fun getNewToken(token: (String) -> Unit) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener
            if (task.result?.token == null) return@addOnCompleteListener
            task.result?.token?.let { newToken ->
                token(newToken)
            }
        }
    }

}