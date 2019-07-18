package com.tokopedia.gm.common.data.interceptor

import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class PowerMerchantSubscribeInterceptor(val userSession: UserSessionInterface) : Interceptor {

    companion object {
        private const val KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization"
        private const val BEARER = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        return chain.proceed(getScoreAuthorizationHeader(newRequest).build())
    }

    private fun getScoreAuthorizationHeader(newRequest: Request.Builder): Request.Builder {
        if (userSession.isLoggedIn) {
            newRequest.addHeader(KEY_ACCOUNTS_AUTHORIZATION, BEARER + " "
                    + userSession.accessToken)
        }
        return  newRequest
    }
}