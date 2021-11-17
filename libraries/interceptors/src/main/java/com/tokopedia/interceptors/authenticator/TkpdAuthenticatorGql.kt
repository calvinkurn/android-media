package com.tokopedia.interceptors.authenticator

import android.app.Application
import android.util.Log
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.logger.ServerLogger
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.refreshtoken.AccessTokenRefresh
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.user.session.UserSession
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okio.Buffer
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Yoris on 13/10/21.
 */

class TkpdAuthenticatorGql(
    val application: Application,
    val networkRouter: NetworkRouter,
    val userSession: UserSession,
    val refreshTokenUseCaseGql: RefreshTokenGql
): Authenticator {

    private fun isNeedRefresh() = userSession.isLoggedIn
    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun isRolledOut(): Boolean {
        return getAbTestPlatform().getString(ROLLOUT_REFRESH_TOKEN).isNotEmpty()
    }

    private fun isEnableConfig(): Boolean {
        val config = FirebaseRemoteConfigImpl(application)
        return config.getBoolean(CONFIG_KEY_REFRESH_TOKE_GQL, false)
    }

    private fun getRefreshQueryPath(finalRequest: Request, response: Response): String {
        var result = ""
        try {
            result = response.request().url().toString() + " "
            val path: String
            val copy = finalRequest.newBuilder().build()
            val buffer = Buffer()
            if (copy.body() != null) {
                copy.body()!!.writeTo(buffer)
            }
            path = buffer.readUtf8()
            val pattern = Pattern.compile(TkpdAuthInterceptor.PATH_REGEX)
            val matcher = pattern.matcher(path)
            result += if (matcher.find()) {
                matcher.group()
            } else {
                path.substring(0, Math.min(path.length, 150))
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            return result
        }
        return result
    }

    private fun getTokenOld(response: Response): String {
        val path: String = getRefreshQueryPath(response.request(), response)
        val accessTokenRefresh = AccessTokenRefresh()
        val newAccessToken = accessTokenRefresh.refreshToken(application.applicationContext, userSession, networkRouter, path)
        return newAccessToken ?: ""
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if(isNeedRefresh()) {
            val path: String = getRefreshQueryPath(response.request(), response)
            return if(responseCount(response) == 0)
                try {
                    val originalRequest = response.request()
                    if(isEnableConfig() && isRolledOut()) {
                        val tokenResponse = refreshTokenUseCaseGql.refreshToken(application.applicationContext, userSession, networkRouter)
                        if(tokenResponse != null) {
                            return if(tokenResponse.accessToken.isEmpty()) {
                                logRefreshTokenEvent(ERROR_GQL_ACCESS_TOKEN_EMPTY, TYPE_REFRESH_WITH_GQL, path, trimToken(userSession.accessToken), isSuccess = false)
                                return refreshWithOldMethod(response, ERROR_GQL_ACCESS_TOKEN_EMPTY)
                            } else {
                                logRefreshTokenEvent("", TYPE_REFRESH_WITH_GQL, path, "", isSuccess = true)
                                onRefreshTokenSuccess(accessToken = tokenResponse.accessToken, refreshToken = tokenResponse.refreshToken, tokenType = tokenResponse.tokenType)
                                updateRequestWithNewToken(originalRequest)
                            }
                        } else {
                            logRefreshTokenEvent(ERROR_GQL_ACCESS_TOKEN_NULL, TYPE_REFRESH_WITH_GQL, path, trimToken(userSession.accessToken), isSuccess = false)
                            return refreshWithOldMethod(response, ERROR_GQL_ACCESS_TOKEN_NULL)
                        }
                    } else {
                        return refreshWithOldMethod(response)
                    }
                } catch (ex: Exception) {
                    logRefreshTokenEvent(formatThrowable(ex), TYPE_FAILED_AUTHENTICATE, path, trimToken(userSession.accessToken), isSuccess = false)
                    null
                }
            else {
                networkRouter.showForceLogoutTokenDialog("/")
                logRefreshTokenEvent("", TYPE_RESPONSE_COUNT, "", "", isSuccess = false)
                return null
            }
        } else {
            if(responseCount(response)!=0) {
                logRefreshTokenEvent("", TYPE_RESPONSE_COUNT_NOT_LOGIN, "", "", isSuccess = false)
                return null
            }
        }
        return response.request()
    }

    private fun refreshWithOldMethod(response: Response, reason: String = ""): Request? {
        return try {
            val newToken = getTokenOld(response)
            if(newToken.isNotEmpty()) {
                logRefreshTokenEvent(reason, TYPE_RETRY_REFRESH_TOKEN_REST, "", "", true)
                networkRouter.doRelogin(newToken)
                updateRequestWithNewToken(response.request())
            } else {
                logRefreshTokenEvent(reason, TYPE_RETRY_REFRESH_TOKEN_REST, "", "", false)
                null
            }
        }catch (e: Exception) {
            logRefreshTokenEvent(formatThrowable(e), TYPE_RETRY_REFRESH_TOKEN_REST, "", "", false)
            null
        }
    }

    private fun onRefreshTokenSuccess(accessToken: String, refreshToken: String, tokenType: String) {
        saveNewToken(accessToken = accessToken, refreshToken = refreshToken, tokenType = tokenType)
        networkRouter.doRelogin(accessToken)
    }

    private fun saveNewToken(accessToken: String, refreshToken: String, tokenType: String) {
        if (accessToken.isNotEmpty()) {
            userSession.setToken(accessToken, tokenType)
        }
        if (refreshToken.isNotEmpty()) {
            userSession.setRefreshToken(
                EncoderDecoder.Encrypt(
                    refreshToken,
                    userSession.refreshTokenIV
                )
            )
        }
    }

    private fun trimToken(accessToken: String): String {
        if(accessToken.isNotEmpty() && accessToken.length >= 10) {
            return accessToken.takeLast(10)
        }
        return accessToken
    }

    private fun responseCount(response: Response): Int {
        var result = 0
        var curResponse: Response? = response
        while (curResponse?.priorResponse() != null) {
            result++
            curResponse = curResponse.priorResponse()
        }
        return result
    }

    private fun updateRequestWithNewToken(request: Request): Request {
        val newRequest = request.newBuilder()
        val freshAccessToken = userSession.accessToken
        val isContainsBearer = request.header(AUTHORIZATION)?.contains(BEARER)

        newRequest.header(HEADER_ACCOUNTS_AUTHORIZATION, "$HEADER_PARAM_BEARER $freshAccessToken")
        if(isContainsBearer == true){
            newRequest.header(HEADER_PARAM_AUTHORIZATION, "$HEADER_PARAM_BEARER $freshAccessToken")
        }
        return newRequest.build()
    }

    private fun logRefreshTokenEvent(error: String, type: String, path: String, accessToken: String, isSuccess: Boolean) {
        val messageMap: MutableMap<String, String> = HashMap()
        messageMap["type"] = type
        messageMap["is_gql"] = isEnableConfig().toString()
        messageMap["path"] = path
        messageMap["success"] = isSuccess.toString()

        if(error.isNotEmpty()) {
            messageMap["error"] = error
        }

        if (accessToken.isNotEmpty()) {
            messageMap["oldToken"] = accessToken
        }
        ServerLogger.log(
            com.tokopedia.logger.utils.Priority.P2,
            "USER_AUTHENTICATOR",
            messageMap
        )
    }

    companion object {
        private const val HEADER_ACCOUNTS_AUTHORIZATION = "accounts-authorization"
        private const val HEADER_PARAM_BEARER = "Bearer"
        private const val AUTHORIZATION = "authorization"
        private const val BEARER = "Bearer"
        private const val HEADER_PARAM_AUTHORIZATION = "authorization"
        private const val CONFIG_KEY_REFRESH_TOKE_GQL = "android_user_new_gql"


        private const val TYPE_RETRY_REFRESH_TOKEN_REST = "retry_refresh_with_rest_api"
        private const val TYPE_REFRESH_WITH_GQL = "refresh_token_with_gql"

        const val ERROR_GQL_ACCESS_TOKEN_EMPTY = "gql access token is empty"
        const val ERROR_GQL_ACCESS_TOKEN_NULL = "gql access token is null"

        const val TYPE_FAILED_AUTHENTICATE = "failed_authenticate"
        const val TYPE_RESPONSE_COUNT = "response_count"
        const val TYPE_RESPONSE_COUNT_NOT_LOGIN = "response_count_not_logged_in"

        const val ROLLOUT_REFRESH_TOKEN = "refresh_token_gql"

        fun formatThrowable(throwable: Throwable): String {
            return try{
                Log.getStackTraceString(throwable).take(1000)
            } catch (e: Exception){
                e.toString()
            }
        }

        fun createAuthenticator(context: Application, networkRouter: NetworkRouter, userSession: UserSession, refreshTokenUseCaseGql: RefreshTokenGql): TkpdAuthenticatorGql {
            return TkpdAuthenticatorGql(context, networkRouter, userSession, refreshTokenUseCaseGql)
        }
    }
}