package com.tokopedia.interceptors.authenticator

import android.content.Context
import android.util.Log
import com.tokopedia.interceptors.refreshtoken.RefreshTokenGql
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSession
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okio.Buffer
import java.util.regex.Pattern

/**
 * Created by Yoris on 13/10/21.
 */

class TkpdAuthenticatorGql(
    val context: Context,
    val networkRouter: NetworkRouter,
    val userSession: UserSession,
    val refreshTokenUseCaseGql: RefreshTokenGql
): Authenticator {

    private fun isNeedRefresh() = userSession.isLoggedIn

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

    private fun createRefreshTokenUseCase() {
//        val graphqlapi = GraphqlApiSuspend.getResponseSuspend()
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        if(isNeedRefresh()) {
            val path: String = getRefreshQueryPath(response.request(), response)
            return if(responseCount(response) == 0)
                try {
                    val originalRequest = response.request()
//                    val newAccessToken = runBlocking { refreshTokenUseCaseGql("") }.loginToken.accessToken
                    val newAccessToken = refreshTokenUseCaseGql.refreshToken(context, userSession, networkRouter)?.accessToken ?: ""
                    if(newAccessToken.isNotEmpty()) {
                        null
                    } else {
                        networkRouter.doRelogin(newAccessToken)
                        updateRequestWithNewToken(originalRequest)
                    }
                } catch (ex: Exception) {
                    networkRouter.logRefreshTokenException(formatThrowable(ex), "failed_authenticate", path, trimToken(userSession.accessToken))
                    null
                }
            else {
                networkRouter.showForceLogoutTokenDialog("/")
                networkRouter.logRefreshTokenException("", "response_count", "", "")
                return null
            }
        } else {
            if(responseCount(response)!=0) {
                networkRouter.logRefreshTokenException("", "response_count_not_logged_in", "", "")
                return null
            }
        }
        return response.request()
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

    companion object {
        private const val HEADER_ACCOUNTS_AUTHORIZATION = "accounts-authorization"
        private const val HEADER_PARAM_BEARER = "Bearer"
        private const val AUTHORIZATION = "authorization"
        private const val BEARER = "Bearer"
        private const val HEADER_PARAM_AUTHORIZATION = "authorization"

        fun formatThrowable(throwable: Throwable): String {
            return try{
                Log.getStackTraceString(throwable).take(1000)
            } catch (e: Exception){
                e.toString()
            }
        }

        fun createAuthenticator(context: Context, networkRouter: NetworkRouter, userSession: UserSession, refreshTokenUseCaseGql: RefreshTokenGql): TkpdAuthenticatorGql {
            return TkpdAuthenticatorGql(context, networkRouter, userSession, refreshTokenUseCaseGql)
        }
    }
}