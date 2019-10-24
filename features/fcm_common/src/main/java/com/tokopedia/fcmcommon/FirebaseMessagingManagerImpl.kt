package com.tokopedia.fcmcommon

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.fcmcommon.data.UpdateFcmTokenResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FirebaseMessagingManagerImpl @Inject constructor(
        @ApplicationContext
        private val context: Context,
        private val sharedPreferences: SharedPreferences,
        private val repository: GraphqlRepository,
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

    override fun onNewToken(newToken: String) {
        launch {
            if (!isNewToken(newToken)) return@launch
            withContext(coroutineContextProvider.IO) {
                updateTokenOnServer(newToken)
            }
        }
    }

    override fun isNewToken(token: String): Boolean {
        val prefToken = getTokenFromPref()
        return prefToken != null && token != prefToken
    }

    override fun clear() {
        coroutineContext.cancelChildren()
    }

    private suspend fun updateTokenOnServer(newToken: String) {
        val request = createUpdateTokenRequest(newToken)
        val response = repository.getReseponse(request)
        val data = response.getSuccessData<UpdateFcmTokenResponse>()
        if (data.updateTokenSuccess()) {
            saveNewTokenToPref(newToken)
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
    }
}