package com.tokopedia.dynamicfeatures.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

object Utils {

    fun isPlayStoreAvailable(context: Context): Boolean {
        return try {
            val pm: PackageManager = context.packageManager
            pm.getInstalledPackages(PackageManager.GET_META_DATA).first {
                it.packageName == "com.android.vending"
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isPlayServiceConnected(context: Context): Boolean {
        // this code should be on main thread.
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(context)
        return result == ConnectionResult.SUCCESS
    }

    fun showPlayServiceErrorDialog(activity: Activity) {
        // this code should be on main thread.
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(activity)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result, 9000).show()
            }
        }
    }
}