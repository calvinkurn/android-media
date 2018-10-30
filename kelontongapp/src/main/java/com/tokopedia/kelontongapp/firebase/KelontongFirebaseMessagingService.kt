package com.tokopedia.kelontongapp.firebase

import android.os.Bundle
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.kelontongapp.notification.NotificationFactory

/**
 * Created by meta on 16/10/18.
 */
class KelontongFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        val data = convertMap(remoteMessage)
        Log.d("FCM ", data.toString())
        NotificationFactory.show(this, data)
    }

    protected fun convertMap(message: RemoteMessage?): Bundle {
        val map = message!!.data
        val bundle = Bundle(map?.size ?: 0)
        if (map != null) {
            for ((key, value) in map) {
                bundle.putString(key, value)
            }
        }
        return bundle
    }
}
