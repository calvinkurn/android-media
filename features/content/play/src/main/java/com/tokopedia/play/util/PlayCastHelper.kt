package com.tokopedia.play.util

import android.content.Context
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.lang.Exception

/**
 * Created By : Jonathan Darwin on November 23, 2021
 */
object PlayCastHelper {

    fun getCastContext(context: Context): CastContext? {
        return try {
            val apiAvailability = GoogleApiAvailability.getInstance()
            val result = apiAvailability.isGooglePlayServicesAvailable(context)

            if(result == ConnectionResult.SUCCESS) CastContext.getSharedInstance(context)
            else null
        }
        catch (e: Exception) {
            null
        }
    }
}