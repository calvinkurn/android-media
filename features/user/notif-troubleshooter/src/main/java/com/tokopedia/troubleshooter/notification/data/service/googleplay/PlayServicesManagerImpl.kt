package com.tokopedia.troubleshooter.notification.data.service.googleplay

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

class PlayServicesManagerImpl(private val context: Context): PlayServicesManager {

    override fun isPlayServiceExist(): Boolean {
        try {
            val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
            val resultCode: Int =
                googleApiAvailability.isGooglePlayServicesAvailable(context)
            return resultCode == ConnectionResult.SUCCESS
        } catch (_: Exception) {
            return false
        }
    }
}

