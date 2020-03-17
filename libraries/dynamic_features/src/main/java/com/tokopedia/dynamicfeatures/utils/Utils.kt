package com.tokopedia.dynamicfeatures.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.tokopedia.dynamicfeatures.constant.CommonConstant

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

    fun getPlayStoreVersionName(context: Context):String {
        return try {
            val pm: PackageManager = context.packageManager
            val playStoreInfo = pm.getInstalledPackages(PackageManager.GET_META_DATA).first {
                it.packageName == CommonConstant.PLAY_STORE_PACKAGE_NAME
            }
            playStoreInfo.versionName
        } catch (e: Exception) {
            "-1"
        }
    }

    fun getPlayStoreLongVersionCode(context: Context):Long {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return -1
        return try {
            val pm: PackageManager = context.packageManager
            val playStoreInfo = pm.getInstalledPackages(PackageManager.GET_META_DATA).first {
                it.packageName == CommonConstant.PLAY_STORE_PACKAGE_NAME
            }
            playStoreInfo.longVersionCode
        } catch (e: Exception) {
            -1
        }
    }

    fun getPlayServiceLongVersionCode(context: Context):Long {
        return try {
            val pm: PackageManager = context.packageManager
            PackageInfoCompat.getLongVersionCode(pm.getPackageInfo(GoogleApiAvailability.GOOGLE_PLAY_SERVICES_PACKAGE, 0))
        } catch (e: Exception) {
            -1
        }
    }

    fun getInstallerPackageName(context: Context):String {
        return try {
            val pm: PackageManager = context.packageManager
            pm.getInstallerPackageName(context.packageName)
        } catch (e: Exception) {
            "-"
        }
    }
}