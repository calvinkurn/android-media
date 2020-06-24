package com.tokopedia.play.broadcaster.util.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * Created by mzennis on 02/06/20.
 */
class PlayPermissionUtil(private val mContext: Context) {

    private var mPreference: SharedPreferences? = PreferenceManager.getDefaultSharedPreferences(mContext)
    private var mPermission: MutableList<Permission> = mutableListOf()
    private val _observablePermissionState = MutableLiveData<PlayPermissionState>()

    fun getObservablePlayPermissionState(): LiveData<PlayPermissionState> {
        return _observablePermissionState
    }

    fun checkPermission(permissions: Array<out String>) {
        if (shouldAskPermission()) {
            defineGrantedPermission(permissions)

            if (isAllPermissionGranted()) {
                _observablePermissionState.value = PlayPermissionState.Granted
                return
            }

            if (mContext is Activity) {
                defineFirstTimePermission()
            } else {
                _observablePermissionState.value = PlayPermissionState.Error(Throwable("Context is not activity"))
                return
            }

            if (isAnyPermissionDenied()) {
                val permissionsGranted = listPermissionsGranted()
                _observablePermissionState.value = PlayPermissionState.Denied(permissionsGranted)
                return
            } else {
                saveState()
                askPermission(permissions)
            }

        } else {
            _observablePermissionState.value = PlayPermissionState.Granted
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        return if (requestCode == PLAY_REQUEST_PERMISSION_CODE) {
            permissions.forEachIndexed { index, name ->
                mPermission.find { name == it.name }?.isGranted = grantResults[index] == PackageManager.PERMISSION_GRANTED
            }

            if (isAllPermissionGranted()) {
                _observablePermissionState.value = PlayPermissionState.Granted
                return true
            }

            if (isAnyPermissionDenied()) {
                val permissionsGranted = listPermissionsGranted()
                _observablePermissionState.value = PlayPermissionState.Denied(permissionsGranted)
            }

            true
        } else false
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLAY_REQUEST_PERMISSION_CODE) {
            mPermission.forEach { permission ->
                val permissionResult = ActivityCompat.checkSelfPermission(mContext, permission.name)
                mPermission.find { permission.name == it.name }?.isGranted = permissionResult == PackageManager.PERMISSION_GRANTED
            }

            if (isAllPermissionGranted()) {
                _observablePermissionState.value = PlayPermissionState.Granted
                return
            }

            val permissionsGranted = listPermissionsGranted()
            _observablePermissionState.value = PlayPermissionState.Denied(permissionsGranted)
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

    private fun isAllPermissionGranted() : Boolean = mPermission.none { !it.isGranted }

    private fun isAnyPermissionDenied(): Boolean = mPermission.any { !it.isFirstTime }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission(permissions: Array<out String>) {
        (mContext as Activity).requestPermissions(permissions, PLAY_REQUEST_PERMISSION_CODE)
    }

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