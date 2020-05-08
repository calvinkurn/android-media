package com.tokopedia.device.info.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.device.info.DeviceInfo.getImeiCache
import com.tokopedia.device.info.cache.DeviceInfoCache
import com.tokopedia.device.info.cache.FingerprintCache

/**
 * ImeiPermissionAsker is helper to ask Imei Permission
 * It will automatically setImei to cache whenever the user accept the Imei Permission
 * How to use:
 *
 * {
 *      ...
 *      ImeiPermissionAsker.checkImeiPermission(activity, onNeedAskPermission = {
 *              // show intro of why this permission needed
 *              // for example: show dialog to info that this permission is needed to enable promo
 *              // when user click ok, call ImeiPermissionAsker.askImeiPermission(activity)
 *          },onUserAlreadyDeniedAndDontAskAgain = {
 *              // user has previously denied and don't ask again.
 *              // show intro then show user to open settings
 *          }, onAlreadyGranted = {
 *              // user already grant imei permission, so go to next step
 *          })
 *      }
 *  }
 *
 *  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
 *      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
 *      ImeiPermissionAsker.onImeiRequestPermissionsResult(activity, requestCode, permissions, grantResults,
 *          onUserDenied = {
 *              // user click don't allow imei permission
 *          }, onUserDeniedAndDontAskAgain = {
 *              // user click don't allow imei permission and don't ask again
 *              // ask user to open settings
 *          }, onUserAcceptPermission = {
 *              // user click allow, so go to next step
 *          })
 *  }
 *
 */

object ImeiPermissionAsker {
    const val IMEI_PERMISION_REQ_CODE = 720
    const val KEY_FIRST_TIME_ASK = "keyFirstTimeAsk"

    fun checkImeiPermission(activity: FragmentActivity,
                            onNeedAskPermission: (() -> Unit),
                            onAlreadyGranted: (() -> Unit),
                            onUserAlreadyDeniedAndDontAskAgain: (() -> Unit)) {
        val hasPhoneStatePermission = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        if (hasPhoneStatePermission) {
            // user already allow permission, but may clear data/clear cache
            val (_, isCached) = getImeiCache(activity)
            if (!isCached) {
                updateImeiCache(activity)
            }
            onAlreadyGranted.invoke()
        } else {
            val imeiCache = DeviceInfo.getImei(activity)
            // user don't allow permission but imei is in cache.
            if (!imeiCache.isNullOrEmpty()) {
                onAlreadyGranted.invoke()
            }
            // user don't allow permission and imei is not in cache.
            else {
                if(isFirstTimeAsk(activity)) {
                    onNeedAskPermission.invoke()
                }
                else {
                    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)
                    if (!showRationale) {
                        onUserAlreadyDeniedAndDontAskAgain.invoke()
                    } else {
                        onNeedAskPermission.invoke()
                    }
                }
            }
        }
    }

    fun askImeiPermission(activity: FragmentActivity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.READ_PHONE_STATE),
            IMEI_PERMISION_REQ_CODE
        );
    }

    fun askImeiPermissionFragment(fragment: Fragment) {
        fragment.context?.run { setFirstTimeAsk(this, false) }
        fragment.requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE),
                IMEI_PERMISION_REQ_CODE
        )
    }

    fun onImeiRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray,
                                       onUserDenied: (() -> Unit),
                                       onUserDeniedAndDontAskAgain: (() -> Unit),
                                       onUserAcceptPermission: (() -> Unit)) {
        if (requestCode == IMEI_PERMISION_REQ_CODE) {
            // for each permission check if the user granted/denied them
            // you may want to group the rationale in a single dialog,
            // this is just an example
            for ((i, value) in permissions.withIndex()) {
                if (value == Manifest.permission.READ_PHONE_STATE) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        // user rejected the permission
                        val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)
                        if (!showRationale) {
                            onUserDeniedAndDontAskAgain.invoke()
                        } else {
                            onUserDenied.invoke()
                        }
                    } else if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        updateImeiCache(activity)
                        onUserAcceptPermission.invoke()
                    }
                    break
                }
            }
        }
    }

    private fun setFirstTimeAsk(context: Context, isFirstTime: Boolean){
        val prefs: SharedPreferences = context.getSharedPreferences(
                KEY_FIRST_TIME_ASK, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FIRST_TIME_ASK, isFirstTime).apply()
    }

    private fun isFirstTimeAsk(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(
                KEY_FIRST_TIME_ASK, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FIRST_TIME_ASK, true)
    }

    private fun updateImeiCache(context: Context) {
        DeviceInfoCache(context).clearImei()
        DeviceInfo.getImei(context)
        FingerprintCache.clearFingerprintCache(context)
    }
}