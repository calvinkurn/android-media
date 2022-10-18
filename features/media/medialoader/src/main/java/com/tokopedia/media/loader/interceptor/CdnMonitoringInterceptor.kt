package com.tokopedia.media.loader.interceptor

import android.content.Context
import com.tokopedia.user.session.UserSession
import okhttp3.Interceptor
import okhttp3.Response

class CdnMonitoringInterceptor(private val applicationContext: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response = chain.proceed(chain.request())

        val cdnName: String = response.header("x-tkpd-cdn-name", "") ?: ""
        if (cdnName.isNotBlank()) {
            setCdnNameUserSession(cdnName)
        }

        return response
    }

    private fun setCdnNameUserSession(cdnName: String) {
        val userSession = UserSession(applicationContext)
        userSession.cdnName = cdnName
    }
}
