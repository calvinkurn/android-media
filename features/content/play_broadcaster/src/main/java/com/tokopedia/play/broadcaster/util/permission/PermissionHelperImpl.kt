package com.tokopedia.play.broadcaster.util.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlin.math.min

/**
 * Created by jegul on 08/07/20
 */
class PermissionHelperImpl : PermissionHelper {

    private val permissionMap: MutableMap<Int, (PermissionStatusHandler) -> Unit> = mutableMapOf()

    private val mContext: Context
    private val permissionRequester: (permissions: Array<String>, requestCode: Int) -> Unit
    private val showRationaleHandler: (permission: String) -> Boolean

    constructor(fragment: Fragment) {
        mContext = fragment.requireContext()
        permissionRequester = fragment::requestPermissions
        showRationaleHandler = fragment::shouldShowRequestPermissionRationale
    }

    constructor(activity: Activity) {
        mContext = activity
        permissionRequester = { permissions, requestCode ->
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
        showRationaleHandler = { permission ->
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }
    }

    override fun requestPermissionFullFlow(permission: String, requestCode: Int, permissionResultListener: PermissionResultListener) {
        requestMultiPermissionsFullFlow(arrayOf(permission), requestCode, permissionResultListener)
    }

    override fun requestMultiPermissionsFullFlow(permissions: Array<String>, requestCode: Int, permissionResultListener: PermissionResultListener) {
        when {
            isAllPermissionsGranted(permissions) -> {
                permissionResultListener.onRequestPermissionResult()
                        .invoke(
                                PermissionStatusHandler(
                                        permissions.associateBy({ it }) { PackageManager.PERMISSION_GRANTED },
                                        requestCode
                                )
                        )
            }
            else -> {
                val rationaleList = permissions.filter { shouldShowRequestPermissionRationale(it) }

                if (rationaleList.isNotEmpty()) {
                    val isHandled = permissionResultListener.shouldShowRequestPermissionRationale(permissions, requestCode)
                    if (!isHandled) requestMultiPermissions(permissions, requestCode, permissionResultListener.onRequestPermissionResult())
                } else {
                    requestMultiPermissions(permissions, requestCode, permissionResultListener.onRequestPermissionResult())
                }
            }
        }
    }

    override fun requestPermission(permission: String, requestCode: Int, listener: PermissionStatusHandler.() -> Unit) {
        requestMultiPermissions(arrayOf(permission), requestCode, listener)
    }

    override fun requestMultiPermissions(permissions: Array<String>, requestCode: Int, listener: PermissionStatusHandler.() -> Unit) {
        if (!isAllPermissionsGranted(permissions)) {
            permissionMap[requestCode] = listener
            permissionRequester(permissions, requestCode)
        }
    }

    override fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
                mContext,
                permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun isAllPermissionsGranted(permissions: Array<String>): Boolean {
        return permissions.all(::isPermissionGranted)
    }

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return showRationaleHandler(permission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        val listener = permissionMap[requestCode] ?: return false
        val lowestLength = min(permissions.size, grantResults.size)
        val resultMap = mutableMapOf<String, Int>()
        for (i in 0 until lowestLength) {
            resultMap[permissions[i]] = grantResults[i]
        }
        listener.invoke(PermissionStatusHandler(resultMap, requestCode))
        permissionMap.remove(requestCode)
        return true
    }
}