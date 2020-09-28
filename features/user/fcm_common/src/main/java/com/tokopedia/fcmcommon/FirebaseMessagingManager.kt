package com.tokopedia.fcmcommon

interface FirebaseMessagingManager {
    fun onNewToken(newToken: String?)
    fun isNewToken(token: String): Boolean
    fun clear()
    fun syncFcmToken(listener: SyncListener)
    fun currentToken(): String

    interface SyncListener {
        fun onSuccess()
        fun onError(exception: Exception?)
    }

    companion object {
        const val QUERY_UPDATE_FCM_TOKEN = "query_update_fcm_token"

        const val PARAM_OLD_TOKEN = "oldToken"
        const val PARAM_NEW_TOKEN = "newToken"
    }
}