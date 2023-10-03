package com.tokopedia.profilecompletion.di

import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

@ProfileCompletionSettingScope
class ProfileManagementInterceptor @Inject constructor(
    private val userSessionInterface: UserSessionInterface
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        requestBuilder.apply {
            addHeader(KEY_AUTHORIZATION, "$VALUE_BEARER ${userSessionInterface.accessToken}")
            addHeader(KEY_X_CLIENT_ID, VALUE_CLIENT_ID)
            addHeader(KEY_X_APP_ID, VALUE_APP_ID)
            addHeader(KEY_LANGUAGE, VALUE_LANGUAGE)
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val KEY_AUTHORIZATION = "Authorization"
        private const val KEY_X_CLIENT_ID = "X-ClientID"
        private const val KEY_X_APP_ID = "X-AppID"
        private const val KEY_LANGUAGE = "Accept-Language"
        private const val VALUE_LANGUAGE = "in"
        private const val VALUE_BEARER = "Bearer"
        private const val VALUE_CLIENT_ID = "tokopedia:consumer:app"
        private const val VALUE_APP_ID = "com.tokopedia.app"
    }
}
