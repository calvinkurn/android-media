package com.tokopedia.graphql.interceptor

import android.content.Context
import com.tokopedia.user.session.UserSession
import okhttp3.Interceptor
import okhttp3.Response

class CdnMonitoringInterceptor(private val applicationContext: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val cdnName: String = request.headers.get("x-tkpd-cdn-name") ?: ""
        if (cdnName.isNotBlank()) {
            setCdnNameUserSession(cdnName)
        }

        return chain.proceed(request)
    }

    private fun setCdnNameUserSession(cdnName: String) {
        val userSession = UserSession(applicationContext)
        userSession.cdnName = cdnName
    }
}
