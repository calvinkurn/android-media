package com.tokopedia.play.broadcaster.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat


/**
 * Created by mzennis on 02/06/20.
 */
class PermissionUtil(private val mContext: Context) {

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
            defineGrantedPermission(permissions)

            if (isAllPermissionGranted()) {
                listener.onAllPermissionGranted()
                return
            }

            if (mContext is Activity) {
                defineFirstTimePermission()
            } else {
                listener.onError(Throwable("Context is not activity"))
            }

            if (isAnyPermissionDenied()) {
                listener.onPermissionDisabled()
            } else {
                askPermission(permissions)
                saveState()
            }

            val permissionsGranted = listPermissionsGranted()
            if (permissionsGranted.isNotEmpty()) {
                listener.onPermissionGranted(permissionsGranted)
            }

        } else {
            listener.onAllPermissionGranted()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // TODO("handle request permission result")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun defineFirstTimePermission() {
        mPermission.forEach {
            if (!(mContext as Activity).shouldShowRequestPermissionRationale(it.name)) {
                it.isFirstTime = isFirstTimeAskingPermission(it.name)
            }
        }
    }

    private fun defineGrantedPermission(permissionList: Array<String>) {
        permissionList.forEach {
            val permissionResult = ActivityCompat.checkSelfPermission(mContext, it)
            this.mPermission.add(Permission(
                    name = it,
                    isGranted = permissionResult == PackageManager.PERMISSION_GRANTED,
                    isFirstTime = isFirstTimeAskingPermission(it)))
        }
    }

    private fun saveState() {
        mPermission.filter { !it.isGranted }.forEach {
            setFirstTimeAskingPermission(it.name)
        }
    }

    private fun isAllPermissionGranted() : Boolean= mPermission.none { !it.isGranted }

    private fun isAnyPermissionDenied(): Boolean = mPermission.any { !it.isFirstTime }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission(permissions: Array<String>) {
        (mContext as Activity).requestPermissions(permissions, PLAY_REQUEST_PERMISSION_CODE)
    }

    /**
     * Check if version is marshmallow and above.
     */
    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun setFirstTimeAskingPermission(permission: String) {
        sharedPreference?.edit()?.putBoolean(permission, false)?.apply()
    }

    private fun isFirstTimeAskingPermission(permission: String): Boolean {
        return sharedPreference?.getBoolean(permission, true) == true
    }

    private fun listPermissionsGranted(): List<String> = mPermission.filter { it.isGranted }.map { it.name }

    private data class Permission(
            var name: String,
            var isGranted: Boolean,
            var isFirstTime: Boolean
    )

    companion object {
        const val PLAY_REQUEST_PERMISSION_CODE = 3432
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