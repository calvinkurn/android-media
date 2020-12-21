package com.tokopedia.fcmcommon

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.tokopedia.fcmcommon.domain.UpdateFcmTokenUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FirebaseMessagingManagerImpl @Inject constructor(
        private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
        private val sharedPreferences: SharedPreferences,
        private val userSession: UserSessionInterface
) : FirebaseMessagingManager {

    override fun onNewToken(newToken: String?) {
        if (!userSession.isLoggedIn || newToken == null || !isNewToken(newToken)) return
        updateTokenOnServer(newToken)
    }

    override fun isNewToken(token: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken != null && token != prefToken
    }

    override fun syncFcmToken(listener: FirebaseMessagingManager.SyncListener) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                listener.onError(task.exception)
                return@addOnCompleteListener
            }

            val currentFcmToken = task.result?.token

            if (currentFcmToken == null) {
                val exception = IllegalStateException("Null FCM token")
                listener.onError(exception)
                return@addOnCompleteListener
            }

            if (sameTokenWithPrefToken(currentFcmToken)) {
                listener.onSuccess()
                return@addOnCompleteListener
            }

            if (!userSession.isLoggedIn) {
                return@addOnCompleteListener
            }

            updateTokenOnServer(currentFcmToken, listener)
        }
    }

    private fun sameTokenWithPrefToken(currentFcmToken: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken == currentFcmToken
    }

    override fun currentToken(): String {
        return getTokenFromPref()?: ""
    }

    private fun updateTokenOnServer(
            newToken: String,
            listener: FirebaseMessagingManager.SyncListener? = null
    ) {
        try {
            val params = createUpdateTokenParams(newToken)
            updateFcmTokenUseCase(params, {
                if (it.updateTokenSuccess()) {
                    saveNewTokenToPref(newToken)
                    listener?.onSuccess()
                }
            }, {
                val error = IllegalStateException(it.localizedMessage)
                listener?.onError(error)
                logFailUpdateFcmToken(error, newToken)
            })
        } catch (exception: Exception) {
            exception.printStackTrace()
            listener?.onError(exception)
            logFailUpdateFcmToken(exception, newToken)
        }
    }

    private fun logFailUpdateFcmToken(error: Throwable, token: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val errorMessage = """ Error update fcm token, 
                    userId: ${userSession.userId},
                    deviceId: ${userSession.deviceId},
                    fcmTokenShouldBe: $token
                    errorMessage: ${error.message},
                """.trimIndent()
                FirebaseCrashlytics.getInstance().recordException(Exception(errorMessage))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createUpdateTokenParams(newToken: String): Map<String, String> {
        val currentToken = getTokenFromPref() ?: newToken
        return mapOf(
                FirebaseMessagingManager.PARAM_OLD_TOKEN to currentToken,
                FirebaseMessagingManager.PARAM_NEW_TOKEN to newToken
        )
    }

    private fun getTokenFromPref(): String? {
        return sharedPreferences.getString(FCM_TOKEN, "")
    }

    private fun saveNewTokenToPref(newToken: String) {
        sharedPreferences.edit().apply {
            putString(FCM_TOKEN, newToken)
        }.apply()
    }

    companion object {
        private const val FCM_TOKEN = "pref_fcm_token"

        @JvmStatic
        fun getFcmTokenFromPref(context: Context): String {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(FCM_TOKEN, "") ?: ""
        }
    }
}