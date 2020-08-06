package com.tokopedia.fcmcommon

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.fcmcommon.data.UpdateFcmTokenResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FirebaseMessagingManagerImpl @Inject constructor(
        @ApplicationContext
        private val context: Context,
        private val sharedPreferences: SharedPreferences,
        private val repository: GraphqlRepository,
        private val userSession: UserSessionInterface,
        private val coroutineContextProvider: CoroutineContextProviders,
        private val queries: Map<String, String>
) : FirebaseMessagingManager, CoroutineScope {

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
            CoroutineExceptionHandler { _, throwable ->
                GlobalScope.launch {
                    Timber.e(throwable)
                }
            }

    override val coroutineContext: CoroutineContext
        get() = coroutineContextProvider.Main + SupervisorJob() + coroutineExceptionHandler

    override fun onNewToken(newToken: String?) {
        if (!userSession.isLoggedIn || newToken == null || !isNewToken(newToken)) return
        launch(coroutineContextProvider.IO) {
            updateTokenOnServer(newToken)
        }
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

            launch(coroutineContextProvider.IO) {
                updateTokenOnServer(currentFcmToken, listener)
            }
        }
    }

    private fun sameTokenWithPrefToken(currentFcmToken: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken == currentFcmToken
    }

    override fun currentToken(): String {
        return getTokenFromPref()?: ""
    }

    override fun clear() {
        coroutineContext.cancelChildren()
    }

    private suspend fun updateTokenOnServer(
            newToken: String,
            listener: FirebaseMessagingManager.SyncListener? = null
    ) {
        try {
            val request = createUpdateTokenRequest(newToken)
            val response = repository.getReseponse(request)
            val data = response.getSuccessData<UpdateFcmTokenResponse>()
            if (data.updateTokenSuccess()) {
                saveNewTokenToPref(newToken)
                listener?.onSuccess()
            } else {
                val errorMessage = data.getErrorMessage()
                val error = IllegalStateException(errorMessage)
                listener?.onError(error)
                logFailUpdateFcmToken(error, newToken)
            }
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
                    userEmail: ${userSession.email},
                    deviceId: ${userSession.deviceId},
                    fcmTokenShouldBe: $token
                    errorMessage: ${error.message},
                """.trimIndent()
                Crashlytics.logException(Exception(errorMessage, error))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createUpdateTokenRequest(newToken: String): List<GraphqlRequest> {
        val query = queries[FirebaseMessagingManager.QUERY_UPDATE_FCM_TOKEN]
        val params = createUpdateTokenParams(newToken)
        return listOf(
                GraphqlRequest(query, UpdateFcmTokenResponse::class.java, params)
        )
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