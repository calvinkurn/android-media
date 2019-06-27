package com.tokopedia.kelontongapp.firebase

import android.os.Bundle
import android.util.Log

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tokopedia.kelontongapp.notification.NotificationFactory
import com.moengage.push.PushManager
import com.moengage.pushbase.push.MoEngageNotificationUtils



/**
 * Created by meta on 16/10/18.
 */
class KelontongFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage != null) {
            val pushPayload = remoteMessage.data
            if (MoEngageNotificationUtils.isFromMoEngagePlatform(pushPayload)) {
                PushManager.getInstance().pushHandler.handlePushPayload(applicationContext, pushPayload)
            } else {
                val data = convertMap(remoteMessage)
                Log.d("FCM ", data.toString())
                NotificationFactory.show(this, data)
            }
        }
    }

    private fun convertMap(message: RemoteMessage?): Bundle {
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
