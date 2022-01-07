package com.tokopedia.gm.common.data.interceptor

import android.content.Context
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.network.NetworkRouter
import com.tokopedia.authentication.HEADER_AUTHORIZATION
import com.tokopedia.network.interceptor.TkpdAuthInterceptor

class GMAuthInterceptor(
    context: Context?,
    userSession: UserSessionInterface,
    abstractionRouter: NetworkRouter
) : TkpdAuthInterceptor(context, abstractionRouter, userSession) {

    override fun getHeaderMap(
        path: String,
        strParam: String,
        method: String,
        authKey: String,
        contentTypeHeader: String
    ): Map<String, String>? {
        return super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader)?.apply {
            get(HEADER_AUTHORIZATION)?.let { xTkpdAuthorization ->
                this[X_TKPD_HEADER_AUTHORIZATION] = xTkpdAuthorization
            }
            remove(HEADER_AUTHORIZATION)
            val bearerAutorization = BEARER + userSession.accessToken
            this[HEADER_AUTHORIZATION] = bearerAutorization
        }
    }

    companion object {
        private const val X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization"
        private const val BEARER = "Bearer "
    }
}