package com.tokopedia.play.broadcaster.util.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.play.broadcaster.util.preference.PermissionSharedPreferences
import kotlin.math.min

/**
 * Created by jegul on 08/07/20
 */
class PermissionHelperImpl : PermissionHelper {

    private val permissionMap: MutableMap<Int, (PermissionStatusManager) -> Unit> = mutableMapOf()

    private val permissionPref: PermissionSharedPreferences?

    private val mContext: Context
    private val permissionRequester: (permissions: Array<String>, requestCode: Int) -> Unit
    private val showRationaleHandler: (permission: String) -> Boolean

    /**
     * @param fragment -> the caller fragment
     * @param permissionSharedPreferences -> permission shared preferences that saves whether the permission has ever been asked or not
     *
     * permissionSharedPreferences will be used as an indicator
     * whether request permission rationale should be shown even if the user has never been asked or not.
     * Passing null will ignore it and follows system [Fragment.shouldShowRequestPermissionRationale]
     */
    constructor(fragment: Fragment, permissionSharedPreferences: PermissionSharedPreferences? = null) {
        mContext = fragment.requireContext()
        permissionRequester = fragment::requestPermissions
        showRationaleHandler = fragment::shouldShowRequestPermissionRationale

        permissionPref = permissionSharedPreferences
    }

    /**
     * @param activity -> the caller activity
     * @param permissionSharedPreferences -> permission shared preferences that saves whether the permission has ever been asked or not
     *
     * permissionSharedPreferences will be used as an indicator
     * whether request permission rationale should be shown even if the user has never been asked or not.
     * Passing null will ignore it and follows system [Activity.shouldShowRequestPermissionRationale]
     */
    constructor(activity: Activity, permissionSharedPreferences: PermissionSharedPreferences? = null) {
        mContext = activity
        permissionRequester = { permissions, requestCode ->
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
        showRationaleHandler = { permission ->
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
        }

        permissionPref = permissionSharedPreferences
    }

    override fun requestPermissionFullFlow(permission: String, requestCode: Int, permissionResultListener: PermissionResultListener) {
        requestMultiPermissionsFullFlow(arrayOf(permission), requestCode, permissionResultListener)
    }

    override fun requestMultiPermissionsFullFlow(permissions: Array<String>, requestCode: Int, permissionResultListener: PermissionResultListener) {
        when {
            isAllPermissionsGranted(permissions) -> {
                permissionResultListener.onRequestPermissionResult()
                        .invoke(
                                PermissionStatusManager(
                                        permissions.associateBy({ it }) { PackageManager.PERMISSION_GRANTED },
                                        requestCode
                                )
                        )
            }
            else -> {
                val rationaleList = permissions.filter { shouldShowRequestPermissionRationale(it) }

                if (rationaleList.isNotEmpty()) {
                    val isHandled = permissionResultListener.onShouldShowRequestPermissionRationale(permissions, requestCode)
                    if (!isHandled) requestMultiPermissions(permissions, requestCode, permissionResultListener.onRequestPermissionResult())
                } else {
                    requestMultiPermissions(permissions, requestCode, permissionResultListener.onRequestPermissionResult())
                }
            }
        }
    }

    override fun requestPermission(permission: String, requestCode: Int, statusHandler: PermissionStatusHandler) {
        requestMultiPermissions(arrayOf(permission), requestCode, statusHandler)
    }

    override fun requestMultiPermissions(permissions: Array<String>, requestCode: Int, statusHandler: PermissionStatusHandler) {
        if (!isAllPermissionsGranted(permissions)) {
            permissionMap[requestCode] = statusHandler
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
        return permissionPref?.hasBeenAsked(permission) == false || showRationaleHandler(permission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        val listener = permissionMap[requestCode] ?: return false
        val lowestLength = min(permissions.size, grantResults.size)
        val resultMap = mutableMapOf<String, Int>()
        for (i in 0 until lowestLength) {
            permissionPref?.setHasBeenAsked(permissions[i])
            resultMap[permissions[i]] = grantResults[i]
        }
        listener.invoke(PermissionStatusManager(resultMap, requestCode))
        permissionMap.remove(requestCode)
        return true
    }
}