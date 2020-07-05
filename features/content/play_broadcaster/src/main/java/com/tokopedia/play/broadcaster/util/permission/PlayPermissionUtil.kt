package com.tokopedia.play.broadcaster.util.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


/**
 * Created by mzennis on 02/06/20.
 */
class PlayPermissionUtil(private val mContext: Context) {

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

            val permissionsGranted = listPermissionsGranted()
            saveState()
            if (isAnyPermissionDenied()) {
                if (shouldShowRequestPermission()) {
                    askPermission(permissions)
                } else {
                    _observablePermissionState.value = PlayPermissionState.Denied(permissionsGranted)
                    return
                }
            }

        } else {
            _observablePermissionState.value = PlayPermissionState.Granted
        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        return if (requestCode == PLAY_REQUEST_PERMISSION_CODE) {
            permissions.forEachIndexed { index, name ->
                mPermission.find { name == it.name }?.granted = grantResults[index] == PackageManager.PERMISSION_GRANTED
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
                mPermission.find { permission.name == it.name }?.granted = permissionResult == PackageManager.PERMISSION_GRANTED
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
    private fun defineGrantedPermission(permissionList: Array<out String>) {
        this.mPermission.clear()
        permissionList.forEach {
            val permissionResult = ActivityCompat.checkSelfPermission(mContext, it)
            this.mPermission.add(Permission(
                    name = it,
                    granted = permissionResult == PackageManager.PERMISSION_GRANTED,
                    shouldShowRequestPermission = shouldShowRequestPermission(it)))
        }
    }

    private fun saveState() {
        mPermission.forEach {
            it.shouldShowRequestPermission = shouldShowRequestPermission(it.name)
        }
    }

    fun isAllPermissionGranted() : Boolean = mPermission.none { !it.granted }

    private fun isAnyPermissionDenied(): Boolean = mPermission.any { !it.granted }

    private fun shouldShowRequestPermission(): Boolean = mPermission.any { it.shouldShowRequestPermission }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askPermission(permissions: Array<out String>) {
        if (mContext is Activity) mContext.requestPermissions(permissions, PLAY_REQUEST_PERMISSION_CODE)
    }

    private fun shouldAskPermission(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun shouldShowRequestPermission(permission: String): Boolean {
        return if (mContext is Activity) ActivityCompat.shouldShowRequestPermissionRationale(mContext, permission) else false
    }

    private fun listPermissionsGranted(): List<String> = mPermission.filter { it.granted }.map { it.name }

    private data class Permission(
            var name: String,
            var granted: Boolean,
            var shouldShowRequestPermission: Boolean
    )

    companion object {
        const val PLAY_REQUEST_PERMISSION_CODE = 3432
    }
}