package com.tokopedia.fcmcommon

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.tokopedia.fcmcommon.domain.UpdateFcmTokenUseCase
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FirebaseMessagingManagerImpl @Inject constructor(
    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase,
    private val sharedPreferences: SharedPreferences,
    private val userSession: UserSessionInterface,
    private val sendTokenToCMHandler: SendTokenToCMHandler
) : FirebaseMessagingManager {

    override fun onNewToken(newToken: String?) {
        if (newToken == null) return

        if (isNewToken(newToken)) {
            if (userSession.isLoggedIn) {
                updateTokenOnServer(newToken)
            } else {
                setDeviceId(newToken)
                saveNewTokenToPref(newToken)
            }
        }
    }

    override fun isNewToken(token: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken != null && token != prefToken
    }

    override fun syncFcmToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    onNewToken(token)
                }
            }
    }

    override fun currentToken(): String {
        return getTokenFromPref() ?: ""
    }

    private fun updateTokenOnServer(
        newToken: String
    ) {
        try {
            val params = createUpdateTokenParams(newToken)
            updateFcmTokenUseCase(params, {
                if (it.updateTokenSuccess()) {
                    saveNewTokenToPref(newToken)
                    setDeviceId(newToken)
                }
            }, {
                val error = IllegalStateException(it.localizedMessage)
                logFailUpdateFcmToken(error, newToken, "Error")
            })
            updateTokenOnCMServer(newToken)
        } catch (exception: Exception) {
            exception.printStackTrace()
            logFailUpdateFcmToken(exception, newToken, "Exception")
        }
    }

    private fun setDeviceId(newToken: String) {
        userSession.deviceId = newToken
    }

    private fun updateTokenOnCMServer(token: String) {
        sendTokenToCMHandler.updateToken(token)
    }

    private fun logFailUpdateFcmToken(error: Throwable, token: String, type: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val errorMessage = """ $type update fcm token, 
                    userId: ${userSession.userId},
                    deviceId: ${userSession.deviceId},
                    prefToken: ${currentToken()},
                    fcmTokenShouldBe: $token,
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
