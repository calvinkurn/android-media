package com.tokopedia.network.interceptor

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.refreshtoken.AccessTokenRefresh
import com.tokopedia.user.session.UserSession
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

/**
 * Created by Yoris Prayogo on 23/06/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class TkpdAuthenticator(
        val context: Context,
        val networkRouter: NetworkRouter,
        val userSession: UserSession): Authenticator {

    private fun isNeedRefresh() = userSession.isLoggedIn

    override fun authenticate(route: Route?, response: Response): Request? {
        if(isNeedRefresh()) {
            return if(responseCount(response) == 0)
                try {
                    val originalRequest = response.request()
                    val accessTokenRefresh = AccessTokenRefresh()
                    val newAccessToken = accessTokenRefresh.refreshToken(context, userSession, networkRouter)
                    networkRouter.doRelogin(newAccessToken)
                    updateRequestWithNewToken(originalRequest)
                } catch (ex: Exception) {
                    Timber.w("P2#AUTHENTICATOR#Failed refresh token;oldToken='%s';exception='%s'", userSession.accessToken, ex.toString());                    response.request()
                }
            else {
                networkRouter.showForceLogoutTokenDialog("/")
                Timber.w("P2#AUTHENTICATOR#Response Count > 0");
                return response.request()
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

        fun createAuthenticator(context: Context, networkRouter: NetworkRouter, userSession: UserSession): TkpdAuthenticator? {
                return TkpdAuthenticator(context, networkRouter, userSession)
        }
    }
}