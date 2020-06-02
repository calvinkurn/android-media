package com.tokopedia.play.broadcaster.util

import android.app.Activity
import android.content.Context
import android.content.Intent
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

    private var mPreference: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(mContext)
    private var mPermission: MutableList<Permission> = mutableListOf()
    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        this.mListener = listener
    }

    fun checkPermission(permissions: Array<out String>) {
        if (shouldAskPermission()) {
            defineGrantedPermission(permissions)

            if (isAllPermissionGranted()) {
                mListener?.onAllPermissionGranted()
                return
            }

            if (mContext is Activity) {
                defineFirstTimePermission()
            } else {
                mListener?.onError(Throwable("Context is not activity"))
            }

            if (isAnyPermissionDenied()) {
                mListener?.onPermissionDisabled()
            } else {
                saveState()
                askPermission(permissions)
            }

            val permissionsGranted = listPermissionsGranted()
            if (permissionsGranted.isNotEmpty()) {
                mListener?.onPermissionGranted(permissionsGranted)
            }

        } else {
            mListener?.onAllPermissionGranted()
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PLAY_REQUEST_PERMISSION_CODE) {
            permissions.forEachIndexed { index, name ->
                mPermission.find { name == it.name }?.isGranted = grantResults[index] == PackageManager.PERMISSION_GRANTED
            }

            if (isAllPermissionGranted()) {
                mListener?.onAllPermissionGranted()
                return
            }

            if (isAnyPermissionDenied()) {
                mListener?.onPermissionDisabled()
            }

            val permissionsGranted = listPermissionsGranted()
            if (permissionsGranted.isNotEmpty()) {
                mListener?.onPermissionGranted(permissionsGranted)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLAY_REQUEST_PERMISSION_CODE) {
            mPermission.forEach { permission ->
                val permissionResult = ActivityCompat.checkSelfPermission(mContext, permission.name)
                mPermission.find { permission.name == it.name }?.isGranted = permissionResult == PackageManager.PERMISSION_GRANTED
            }

            if (isAllPermissionGranted()) {
                mListener?.onAllPermissionGranted()
                return
            }

            val permissionsGranted = listPermissionsGranted()
            if (permissionsGranted.isNotEmpty()) {
                mListener?.onPermissionGranted(permissionsGranted)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun defineFirstTimePermission() {
        mPermission.forEach {
            if (!(mContext as Activity).shouldShowRequestPermissionRationale(it.name)) {
                it.isFirstTime = isFirstTimeAskingPermission(it.name)
            }
        }
    }

    private fun defineGrantedPermission(permissionList: Array<out String>) {
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
        mPermission.forEach {
            it.isFirstTime = isFirstTimeAskingPermission(it.name)
        }
    }

    private fun isAllPermissionGranted() : Boolean= mPermission.none { !it.isGranted }

    private fun isAnyPermissionDenied(): Boolean = mPermission.any { !it.isFirstTime }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission(permissions: Array<out String>) {
        (mContext as Activity).requestPermissions(permissions, PLAY_REQUEST_PERMISSION_CODE)
    }

    /**
     * Check if version is marshmallow and above.
     */
    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun setFirstTimeAskingPermission(permission: String) {
        mPreference?.edit()?.putBoolean(permission, false)?.apply()
    }

    private fun isFirstTimeAskingPermission(permission: String): Boolean {
        return mPreference?.getBoolean(permission, true) == true
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
}