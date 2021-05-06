package com.tokopedia.fcmcommon

interface FirebaseMessagingManager {
    fun onNewToken(newToken: String?)
    fun isNewToken(token: String): Boolean
    fun syncFcmToken(listener: SyncListener)
    fun currentToken(): String

    interface SyncListener {
        fun onSuccess()
        fun onError(exception: Exception?)
    }

    companion object {
        const val PARAM_OLD_TOKEN = "oldToken"
        const val PARAM_NEW_TOKEN = "newToken"
        const val ENABLE_OLD_GCM_UPDATE = "android_enable_old_gcm_update"
        const val ENABLE_OLD_GCM_UPDATE_SERVICE = "android_enable_old_gcm_update_service"
    }
}