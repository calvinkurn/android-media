package com.tokopedia.instantloan.ddcollector

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

import java.util.ArrayList
import java.util.HashMap

internal class PermissionUtils(context: Context, private val mCallback: PermissionResultCallback) {

    private val mContext: Activity
    private var mMissingPermissions: List<String> = ArrayList()

    init {
        this.mContext = context as Activity
    }

    /**
     * Check the API Level & Permission
     *
     * @param permissions
     * @param requestCode
     */
    fun checkPermission(permissions: List<String>, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionsGranted(permissions)) {
                mCallback.permissionGranted(requestCode)
            } else {
                this.mMissingPermissions = getMissingPermission(permissions)
                requestPermissions(mMissingPermissions, requestCode)
            }
        } else {
            mCallback.permissionGranted(requestCode)
        }
    }

    private fun requestPermissions(missingPermission: List<String>, requestCode: Int) {
        if (!missingPermission.isEmpty()) {
            ActivityCompat.requestPermissions(mContext, missingPermission.toTypedArray(), requestCode)
        }
    }

    fun isPermissionsGranted(permissions: List<String>): Boolean {
        if (permissions.isEmpty()) {
            return true
        }

        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(mContext, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        return true
    }

    private fun getMissingPermission(permissions: List<String>): List<String> {
        val missingPermissions = ArrayList<String>()
        if (permissions.isEmpty()) {
            return missingPermissions
        }

        for (i in permissions.indices) {
            if (ContextCompat.checkSelfPermission(mContext, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permissions[i])
            }

        }
        return missingPermissions
    }

    fun onRequestPermissionsResult(requestCode: Int,requiredPermission: List<String>, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val permissionExeuted = HashMap<String, Int>()

                for (i in permissions.indices) {
                    permissionExeuted[permissions[i]] = grantResults[i]
                }

                val pendingPermissions = ArrayList<String>()

                if(mMissingPermissions.size == 0) {
                    this.mMissingPermissions = getMissingPermission(requiredPermission)
                }

                for (i in this.mMissingPermissions.indices) {
                    if (permissionExeuted[this.mMissingPermissions[i]] != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, this.mMissingPermissions[i])) {
                            pendingPermissions.add(this.mMissingPermissions[i])
                        }
                    }
                }

                if (pendingPermissions.size > 0) {
                    mCallback.permissionDenied(requestCode)
                } else {
                    mCallback.permissionGranted(requestCode)
                }
            } else {
                mCallback.permissionDenied(requestCode)
            }
        }
    }

    companion object {
        val PERMISSION_REQUEST_CODE = 99
    }
}