package com.tokopedia.instantloan.network

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class InstantLoanAuthInterceptor @Inject
constructor(@ApplicationContext context: Context, networkRouter: NetworkRouter, userSessionInterface: UserSessionInterface) : TkpdAuthInterceptor(context, networkRouter, userSessionInterface) {

    override fun getHeaderMap(path: String, strParam: String,
                              method: String, authKey: String,
                              contentTypeHeader: String): Map<String, String> {
        val headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader)
        val accessToken = userSession.accessToken
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap[PARAM_AUTHORIZATION] = "$PARAM_BEARER $accessToken"
        }
        return headerMap
    }

    companion object {
        private val PARAM_AUTHORIZATION = "Authorization"
        private val PARAM_BEARER = "Bearer"
    }
}