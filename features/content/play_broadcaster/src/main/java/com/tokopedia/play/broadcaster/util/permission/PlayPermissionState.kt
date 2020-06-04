package com.tokopedia.play.broadcaster.util.permission


/**
 * Created by mzennis on 03/06/20.
 */
sealed class PlayPermissionState {

    /**
     * When all permission granted
     */
    object Granted: PlayPermissionState()

    /**
     * When the permission is previously asked but not granted
     */
    data class Denied(val permissions: List<String>): PlayPermissionState()

    data class Error(val throwable: Throwable): PlayPermissionState()
}