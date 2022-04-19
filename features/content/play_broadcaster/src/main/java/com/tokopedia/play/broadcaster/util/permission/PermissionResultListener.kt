package com.tokopedia.play.broadcaster.util.permission

/**
 * Created by jegul on 08/07/20
 */
interface PermissionResultListener {

    /**
     * This method will be called once the result of all the requested permissions are known
     *
     * @return the function to handle the result
     */
    fun onRequestPermissionResult(): PermissionStatusHandler

    /**
     * This method will be called once there are permissions that needs to show the user
     * the reason behind the request of the permission
     *
     * @param permissions -> the requested permissions
     * @param requestCode -> the unique request code for this request
     *
     * @return true means the caller has handled the action and the request of the permission will be handled by the caller,
     * false means that the caller has not handled the action and should be handled by the receiver of this param
     */
    fun onShouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean
}