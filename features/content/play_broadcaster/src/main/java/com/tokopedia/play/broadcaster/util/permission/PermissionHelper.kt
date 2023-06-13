package com.tokopedia.play.broadcaster.util.permission

import android.Manifest
import android.os.Build

/**
 * Created by jegul on 07/07/20
 */
interface PermissionHelper {

    companion object {

        val READ_EXTERNAL_STORAGE: String
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
    }

    /**
     * This method is used to request single permission to users following Android documentation best practice
     * [Documentation](https://developer.android.com/training/permissions/requesting)
     *
     * @param permission -> the requested permission
     * @param requestCode -> the unique request code for this request
     * @param permissionResultListener -> the listener to listen the result of the request
     */
    fun requestPermissionFullFlow(permission: String, requestCode: Int, permissionResultListener: PermissionResultListener)

    /**
     * This method is used to request multiple permissions to users following Android documentation best practice
     * [Documentation](https://developer.android.com/training/permissions/requesting)
     *
     * @param permissions -> the requested permissions
     * @param requestCode -> the unique request code for this request
     * @param permissionResultListener -> the listener to listen the result of the request
     */
    fun requestMultiPermissionsFullFlow(permissions: Array<String>, requestCode: Int, permissionResultListener: PermissionResultListener)

    /**
     * This method is used to request single permission from Manifest.permission.*
     * if the permission requested is already granted, then nothing will happened
     * the request code passed will be used to identify the request from other different requests
     *
     * @param permission -> the requested permission
     * @param requestCode -> the unique request code for this request
     */
    fun requestPermission(permission: String, requestCode: Int, statusHandler: PermissionStatusHandler)

    /**
     * This method is used to request multiple permissions from Manifest.permission.*
     * if the permission requested is already granted, then nothing will happened
     * the request code passed will be used to identify the request from other different requests
     *
     * @param permissions -> the requested permissions
     * @param requestCode -> the unique request code for this request
     */
    fun requestMultiPermissions(permissions: Array<String>, requestCode: Int, statusHandler: PermissionStatusHandler)


    /**
     * This method is used to check if the permission is already granted by the user
     *
     * @param permission -> the permission whose status is requested
     * @return true means that permission is granted, false otherwise
     */
    fun isPermissionGranted(permission: String): Boolean

    /**
     * This method is used to check if the permissions are already granted by the user
     *
     * @param permissions -> the permissions whose statuses are requested
     * @return true means that all permissions are granted, false otherwise
     */
    fun isAllPermissionsGranted(permissions: Array<String>): Boolean


    /**
     * This method is used to check if user should be notified of the reasons behind the requested permission
     *
     * @param permission -> the permission requested
     * @return true means that the permission is never asked before or users choose to never be asked again
     */
    fun shouldShowRequestPermissionRationale(permission: String): Boolean


    /**
     * This method is used to delegate result from [Activity#onRequestPermissionResult] or [Fragment#onRequestPermissionResult]
     * the result will then be passed to the listener param added on [requestPermission] or [requestMultiPermissions]
     *
     * @return true means it has been handled by the listener, false otherwise
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean
}
