package com.tokopedia.network.interceptor

import android.content.Context
import android.util.Log
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.refreshtoken.AccessTokenRefresh
import com.tokopedia.user.session.UserSession
import okhttp3.*
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by Yoris Prayogo on 23/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class TkpdAuthenticator(
        val context: Context,
        val networkRouter: NetworkRouter,
        val userSession: UserSession): Authenticator {

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

    override fun authenticate(route: Route?, response: Response): Request? {
        if(isNeedRefresh()) {
            val path: String = getRefreshQueryPath(response.request(), response)
            return if(responseCount(response) == 0)
                try {
                    val originalRequest = response.request()
                    val accessTokenRefresh = AccessTokenRefresh()
                    val newAccessToken = accessTokenRefresh.refreshToken(context, userSession, networkRouter, path)
                    if(newAccessToken.isNullOrEmpty()) {
                        null
                    } else {
                        networkRouter.doRelogin(newAccessToken)
                        updateRequestWithNewToken(originalRequest)
                    }
                } catch (ex: Exception) {
                    Timber.w("P2#USER_AUTHENTICATOR#'%s';oldToken='%s';error='%s';path='%s'", "failed_authenticate", userSession.accessToken, formatThrowable(ex), path)
                    null
                }
            else {
                networkRouter.showForceLogoutTokenDialog("/")
                Timber.w("P2#USER_AUTHENTICATOR#'%s'", "response_count")
                return null
            }
        } else {
            if(responseCount(response)!=0) {
                Timber.w("P2#USER_AUTHENTICATOR#'%s'", "response_count")
                return null
            }
        }
        return response.request()
    }

    private fun createNewForceLogoutBody(response: Response): Response? {
        val jsonObject = JSONObject()
        return try {
            jsonObject.put("message", FORCE_LOGOUT_AUTHENTICATOR)
            val contentType = response.body()!!.contentType()
            val body: ResponseBody = ResponseBody.create(contentType, jsonObject.toString())
            return response.newBuilder().body(body).build()
        } catch (e: JSONException) {
            null
        }
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


    private fun updateRequestWithNewToken(request: Request): Request{
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

        const val FORCE_LOGOUT_AUTHENTICATOR = "FORCED_LOGOUT_AUTHENTICATOR"
        const val BYTE_COUNT = 512L

        const val AUTHENTICATOR_REMOTE_CONFIG_KEY: String = "android_enable_authenticator"

        fun formatThrowable(throwable: Throwable): String {
            return try{
                Log.getStackTraceString(throwable).take(1000)
            } catch (e: Exception){
                e.toString()
            }
        }

        fun createAuthenticator(context: Context, networkRouter: NetworkRouter, userSession: UserSession): TkpdAuthenticator? {
                return TkpdAuthenticator(context, networkRouter, userSession)
        }
    }
}