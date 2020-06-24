package com.tokopedia.network.interceptor

import android.content.Context
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.refreshtoken.AccessTokenRefresh
import com.tokopedia.user.session.UserSession
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

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
            return try {
                val originalRequest = response.request()
                val accessTokenRefresh = AccessTokenRefresh()
                val newAccessToken = accessTokenRefresh.refreshToken(context, userSession, networkRouter)
                networkRouter.doRelogin(newAccessToken)
                updateRequestWithNewToken(originalRequest)
            } catch (ex: Exception) {
                response.request()
            }
        }
        return response.request()
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
        const val BYTE_COUNT = 512L
    }
}