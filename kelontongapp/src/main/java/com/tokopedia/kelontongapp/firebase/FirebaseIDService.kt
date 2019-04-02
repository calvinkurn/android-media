package com.tokopedia.kelontongapp.firebase

import android.util.Log

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by meta on 16/10/18.
 */
class FirebaseIDService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d("FCM", "RefreshedToken: " + refreshedToken!!)
        Preference.saveFcmToken(this, refreshedToken)
    }
}
