package com.tokopedia.feedplus.oldFeed.data

import android.content.Context
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/08/22
 */
class FeedAuthInterceptor @Inject constructor(@ApplicationContext context: Context, userSession: UserSessionInterface, abstractionRouter: NetworkRouter) :
    TkpdAuthInterceptor(context, abstractionRouter, userSession) {

    override fun getHeaderMap(
        path: String,
        strParam: String,
        method: String,
        authKey: String,
        contentTypeHeader: String
    ): Map<String, String> {
        return AuthUtil.generateHeadersWithXUserId(
            path,
            strParam,
            method,
            authKey,
            contentTypeHeader,
            userSession.userId,
            userSession.deviceId,
            userSession
        ).apply {
           this[com.tokopedia.feedplus.oldFeed.data.FeedAuthInterceptor.Companion.HEADER_TKPD_USER_ID] = userSession.userId
           this[com.tokopedia.feedplus.oldFeed.data.FeedAuthInterceptor.Companion.HEADER_ACC_AUTH] = com.tokopedia.feedplus.oldFeed.data.FeedAuthInterceptor.Companion.BEARER + userSession.accessToken
           this[com.tokopedia.feedplus.oldFeed.data.FeedAuthInterceptor.Companion.HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString()
        }
    }

    companion object {
        private const val HEADER_TKPD_USER_ID = "Tkpd-UserId"
        private const val HEADER_ACC_AUTH = "Accounts-Authorization"
        private const val BEARER = "Bearer "
        private const val HEADER_OS_VERSION = "os_version"
    }
}
