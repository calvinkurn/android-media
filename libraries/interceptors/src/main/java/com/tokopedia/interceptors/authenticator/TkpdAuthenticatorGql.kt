package com.tokopedia.interceptors.authenticator

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.interceptors.forcelogout.ForceLogoutData
import com.tokopedia.interceptors.forcelogout.ForceLogoutUseCase
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.logger.ServerLogger
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.network.refreshtoken.AccessTokenRefresh
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okio.Buffer
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by Yoris on 13/10/21.
 */

class TkpdAuthenticatorGql(
    val application: Application,
    val networkRouter: NetworkRouter,
    val userSession: UserSessionInterface,
    val refreshTokenUseCaseGql: RefreshTokenGql
): Authenticator {

    private fun isNeedRefresh() = userSession.isLoggedIn

    private fun getRefreshQueryPath(finalRequest: Request, response: Response): String {
        var result = ""
        try {
            result = response.request.url.toString() + " "
            val path: String
            val copy = finalRequest.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            path = buffer.readUtf8()
            val pattern = Pattern.compile(TkpdAuthInterceptor.PATH_REGEX)
            val matcher = pattern.matcher(path)
            result += if (matcher.find()) {
                matcher.group()
            } else {
                path.substring(0, Math.min(path.length, PATH_LENGTH_SUBSTRING))
            }
        } catch (ex: Exception) {
            Timber.e(ex)
            return result
        }
        return result
    }

    private fun getTokenOld(response: Response): String {
        val path: String = getRefreshQueryPath(response.request, response)
        val accessTokenRefresh = AccessTokenRefresh()
        val newAccessToken = accessTokenRefresh.refreshToken(application.applicationContext, userSession, networkRouter, path)
        return newAccessToken ?: ""
    }

    private fun broadcastForceLogoutInfo(forceLogoutData: ForceLogoutData) {
        val intent = Intent()
        intent.action = "com.tokopedia.tkpd.FORCE_LOGOUT_v2"
        intent.putExtra("title", forceLogoutData.title)
        intent.putExtra("description", forceLogoutData.description)
        intent.putExtra("url", forceLogoutData.url)
        LocalBroadcastManager.getInstance(application.applicationContext).sendBroadcast(intent)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if(isNeedRefresh()) {
            val path: String = getRefreshQueryPath(response.request, response)
            return if(responseCount(response) == 0) {
                try {
                    val originalRequest = response.request
                    val tokenResponse = refreshTokenUseCaseGql.refreshToken(
                        application.applicationContext,
                        userSession,
                        networkRouter
                    )
                    if (tokenResponse != null) {
                        if(tokenResponse.errors?.isNotEmpty() == true) {
                            val forceLogoutInfo = checkForceLogoutInfo()
                            if(forceLogoutInfo?.isForceLogout == true) {
                                userSession.logoutSession()
                                broadcastForceLogoutInfo(forceLogoutInfo)
                            } else {
                                networkRouter.showForceLogoutTokenDialog("/")
                            }
                            return null
                        } else if (tokenResponse.accessToken?.isEmpty() == true) {
                            logRefreshTokenEvent(
                                ERROR_GQL_ACCESS_TOKEN_EMPTY,
                                TYPE_REFRESH_WITH_GQL,
                                path,
                                trimToken(userSession.accessToken)
                            )
                            return refreshWithOldMethod(response)
                        } else {
                            onRefreshTokenSuccess(
                                accessToken = tokenResponse.accessToken ?: "",
                                refreshToken = tokenResponse.refreshToken ?: "",
                                tokenType = tokenResponse.tokenType ?: ""
                            )
                            return updateRequestWithNewToken(originalRequest)
                        }
                    } else {
                        logRefreshTokenEvent(
                            ERROR_GQL_ACCESS_TOKEN_NULL,
                            TYPE_REFRESH_WITH_GQL,
                            path,
                            trimToken(userSession.accessToken)
                        )
                        return refreshWithOldMethod(response)
                    }
                } catch (ex: Exception) {
                    logRefreshTokenEvent(
                        formatThrowable(ex),
                        TYPE_FAILED_AUTHENTICATE,
                        path,
                        trimToken(userSession.accessToken)
                    )
                    null
                }
            } else {
                networkRouter.showForceLogoutTokenDialog("/")
                logRefreshTokenEvent("", TYPE_RESPONSE_COUNT, "", "")
                return null
            }
        } else {
            if(responseCount(response)!=0) {
                logRefreshTokenEvent("", TYPE_RESPONSE_COUNT_NOT_LOGIN, "", "")
                return null
            }
        }
        return response.request
    }

    private fun refreshWithOldMethod(response: Response): Request? {
        return try {
            val newToken = getTokenOld(response)
            if(newToken.isNotEmpty()) {
                // to check how many users success after fallback from gql
                logRefreshTokenEvent("", TYPE_SUCCESS_REFRESH_TOKEN_REST, "", accessToken = trimToken(newToken))
                networkRouter.doRelogin(newToken)
                updateRequestWithNewToken(response.request)
            } else {
                null
            }
        }catch (e: Exception) {
            logRefreshTokenEvent(formatThrowable(e), TYPE_RETRY_REFRESH_TOKEN_REST, "", "")
            null
        }
    }

    private fun checkForceLogoutInfo(): ForceLogoutData? {
        return try {
            val forceLogoutInfo = ForceLogoutUseCase(application.applicationContext, userSession, networkRouter).execute()
            return forceLogoutInfo?.data
        } catch (e: Exception) {
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
        if(accessToken.isNotEmpty() && accessToken.length >= TOKEN_MIN_LENGTH) {
            return accessToken.takeLast(TOKEN_MIN_LENGTH)
        }
        return accessToken
    }

    private fun responseCount(response: Response): Int {
        var result = 0
        var curResponse: Response? = response
        while (curResponse?.priorResponse != null) {
            result++
            curResponse = curResponse.priorResponse
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

    private fun logRefreshTokenEvent(error: String, type: String, path: String, accessToken: String) {
        val messageMap: MutableMap<String, String> = HashMap()
        messageMap["type"] = type
        messageMap["is_gql"] = "true"
        messageMap["path"] = path

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

        private const val TYPE_RETRY_REFRESH_TOKEN_REST = "retry_refresh_with_rest_api"
        private const val TYPE_REFRESH_WITH_GQL = "refresh_token_with_gql"

        const val ERROR_GQL_ACCESS_TOKEN_EMPTY = "gql access token is empty"
        const val ERROR_GQL_ACCESS_TOKEN_NULL = "gql access token is null"

        const val TYPE_FAILED_AUTHENTICATE = "failed_authenticate"
        const val TYPE_RESPONSE_COUNT = "response_count"
        const val TYPE_RESPONSE_COUNT_NOT_LOGIN = "response_count_not_logged_in"
        const val TYPE_SUCCESS_REFRESH_TOKEN_REST = "retry_refresh_with_rest_api_success"

        const val LIMIT_STACKTRACE = 1000
        const val PATH_LENGTH_SUBSTRING = 150

        const val TOKEN_MIN_LENGTH = 10

        fun formatThrowable(throwable: Throwable): String {
            return Log.getStackTraceString(throwable).take(LIMIT_STACKTRACE)
        }
    }
}