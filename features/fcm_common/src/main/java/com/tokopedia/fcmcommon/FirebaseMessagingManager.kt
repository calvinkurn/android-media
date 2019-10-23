package com.tokopedia.fcmcommon

interface FirebaseMessagingManager {
    fun onNewToken(newToken: String)
    fun isNewToken(token: String): Boolean

    companion object {
        const val QUERY_UPDATE_FCM_TOKEN = "query_update_fcm_token"

        const val PARAM_OLD_TOKEN = "param_old_token"
        const val PARAM_NEW_TOKEN = "param_new_token"
    }
}