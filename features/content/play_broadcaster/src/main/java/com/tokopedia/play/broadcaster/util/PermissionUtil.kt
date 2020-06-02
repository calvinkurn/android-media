package com.tokopedia.play.broadcaster.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat


/**
 * Created by mzennis on 02/06/20.
 */
class PermissionUtil(private val mContext: Context) {

    private data class Permission(
            var name: String,
            var isGranted: Boolean,
            var isFirstTime: Boolean
    )

    interface Listener {

        /**
         * When all permission is granted
         */
        fun onAllPermissionGranted()

        /**
         * When one permission is granted
         */
        fun onPermissionGranted(permissions: List<String>)

        /**
         * When the permission is previously asked but not granted &
         * When the permission is disabled by device policy or the user checked "Never ask again"
         *     check box on previous request permission
         */
        fun onPermissionDisabled()

        fun onError(throwable: Throwable)
    }

    private var sharedPreference: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(mContext)
    private var mPermission: MutableList<Permission> = mutableListOf()

    fun checkPermission(permissions: Array<String>, listener: Listener) {
        if (shouldAskPermission()) {
            permissions.forEach {
                val permissionResult = ActivityCompat.checkSelfPermission(mContext, it)
                this.mPermission.add(Permission(
                        name = it,
                        isGranted = permissionResult == PackageManager.PERMISSION_GRANTED,
                        isFirstTime = isFirstTime(it)))
            }

            if (mPermission.none { !it.isGranted }) {
                listener.onAllPermissionGranted()
                return
            }

            if (mContext is Activity) {
                mPermission.forEach {
                    if (!mContext.shouldShowRequestPermissionRationale(it.name)) {
                        it.isFirstTime = isFirstTime(it.name)
                    }
                }
            } else {
                listener.onError(Throwable("context is not activity"))
            }

            if (mPermission.any { !it.isFirstTime }) {
                listener.onPermissionDisabled()
            } else {
                (mContext as Activity).requestPermissions(arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO),
                        1234)
                mPermission.filter { !it.isGranted }.forEach {
                    firstTime(it.name)
                }
            }

            val permissionGranted: MutableList<String> = mutableListOf()
            mPermission.filter { it.isGranted }.forEach {
                permissionGranted.add(it.name)
            }

            if (permissionGranted.isNotEmpty()) {
                listener.onPermissionGranted(permissionGranted)
            }

        } else {
            listener.onAllPermissionGranted()
        }
    }

    private fun firstTime(permission: String) {
        sharedPreference?.edit()?.putBoolean(permission, false)?.apply()
    }

    private fun isFirstTime(permission: String): Boolean {
        return sharedPreference?.getBoolean(permission, true) == true
    }
//
//    companion object {
//        const val PLAY_PERMISSION_PREFERENCE_KEY = "play_permission"
//    }
//
//    fun shouldAskPermission(permissions: Array<String>) {
//
//    }
//
//    /*
//    * Check if version is marshmallow and above.
//    * Used in deciding to ask runtime permission
//    * */
    private fun shouldAskPermission(): Boolean { // done
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
//
//    private fun shouldAskPermission(context: Context, permission: String): Boolean {
//        if (shouldAskPermission()) {
//            val permissionResult: Int = ActivityCompat.checkSelfPermission(context, permission)
//            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
//                return true
//            }
//        }
//        return false
//    }
//
////    fun checkPermission(context: Context, permission: String, listener: PermissionAskListener) {
////        /*
////        * If permission is not granted
////        * */
////        if (shouldAskPermission(context, permission)) {
////            /*
////            * If permission denied previously
////            * */
////            if ((context as Activity).shouldShowRequestPermissionRationale(permission)) {
////                listener.onPermissionPreviouslyDenied()
////            } else { /*
////                * Permission denied or first time requested
////                * */
////                if (isFirstTimeAskingPermission(context, permission)) {
////                    firstTimeAskingPermission(context, permission, false)
////                    listener.onPermissionAsk()
////                } else { /*
////                    * Handle the feature without permission or ask user to manually allow permission
////                    * */
////                    listener.onPermissionDisabled()
////                }
////            }
////        } else {
////            listener.onPermissionGranted()
////        }
////    }
//
//    /*
//    * Callback on various cases on checking permission
//    *
//    * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
//    *     If permission is already granted, onPermissionGranted() would be called.
//    *
//    * 2.  Above M, if the permission is being asked first time onPermissionAsk() would be called.
//    *
//    * 3.  Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
//    *     would be called.
//    *
//    * 4.  Above M, if the permission is disabled by device policy or the user checked "Never ask again"
//    *     check box on previous request permission, onPermissionDisabled() would be called.
//    * */
//    interface PermissionAskListener {
//        /*
//        * Callback to ask permission
//        * */
//        fun onPermissionAsk()
//
//        /*
//        * Callback on permission denied
//        * */
//        fun onPermissionPreviouslyDenied()
//
//        /*
//        * Callback on permission "Never show again" checked and denied
//        * */
//        fun onPermissionDisabled()
//
//        /*
//        * Callback on permission granted
//        * */
//        fun onPermissionGranted()
//    }
}