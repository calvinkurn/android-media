package com.tokopedia.product.manage.list.utils

import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ProductManageGmInterceptor(val userSession: UserSessionInterface) : Interceptor {

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"

        private const val BEARER = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        return chain.proceed(getScoreAuthorizationHeader(newRequest).build())
    }

    private fun getScoreAuthorizationHeader(newRequest: Request.Builder): Request.Builder {
        if (userSession.isLoggedIn) {
            newRequest.addHeader(HEADER_AUTHORIZATION, BEARER + " "
                    + userSession.accessToken)
        }
        return  newRequest
    }
}