package com.tokopedia.media.picker.utils.permission

import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

class PermissionManager private constructor(
    private val callback: PermissionRequestCallback,
    private val delegate: PermissionDelegate
) {
    fun requestPermissions(permissions: List<String>, requestCode: Int) {
        delegate.requestPermission(permissions.toTypedArray(), requestCode)
    }

    fun requestPermission(permission: String, requestCode: Int) {
        delegate.requestPermission(arrayOf(permission), requestCode)
    }

    fun onRequestPermissionsResult(
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (permissions.isEmpty() && grantResults.isEmpty()) return

        val requestPermissionResult = combineResult(permissions, grantResults)

        handleGrantedPermissions(filterResult(requestPermissionResult, PackageManager.PERMISSION_GRANTED))
        handleDeniedPermissions(filterResult(requestPermissionResult, PackageManager.PERMISSION_DENIED))
    }

    private fun combineResult(permissions: Array<out String>, grantResults: IntArray) =
        permissions
            .mapIndexed { index, permission ->
                Pair(permission, grantResults[index])
            }
            .associate { it }

    private fun filterResult(requestPermissionResult: Map<String, Int>, grantStatus: Int) =
        requestPermissionResult
            .filterValues { it == grantStatus }
            .keys
            .toList()

    private fun handleGrantedPermissions(permissions: List<String>) {
        if (permissions.isNotEmpty()) {
            callback.onGranted(permissions)
        }
    }

    private fun handleDeniedPermissions(permissions: List<String>) {
        val denials = permissions.filter(delegate::shouldShowRequestPermissionRationale)
        val permanentDenials = permissions.filterNot(delegate::shouldShowRequestPermissionRationale)

        if (denials.isNotEmpty()) {
            callback.onDenied(denials)
        }

        if (permanentDenials.isNotEmpty()) {
            callback.onPermissionPermanentlyDenied(permanentDenials)
        }
    }

    companion object {
        fun init(activity: Activity, callback: PermissionRequestCallback): PermissionManager {
            return PermissionManager(activity, callback, ActivityPermissionDelegate(activity))
        }

        fun init(fragment: Fragment, callback: PermissionRequestCallback): PermissionManager {
            return PermissionManager(fragment, callback, FragmentPermissionDelegate(fragment))
        }
    }

    internal constructor(
        fragment: Fragment,
        callback: PermissionRequestCallback,
        permissionDelegate: PermissionDelegate
    ) : this(callback, permissionDelegate)

    internal constructor(
        activity: Activity,
        callback: PermissionRequestCallback,
        permissionDelegate: PermissionDelegate
    ) : this(callback, permissionDelegate)
}
