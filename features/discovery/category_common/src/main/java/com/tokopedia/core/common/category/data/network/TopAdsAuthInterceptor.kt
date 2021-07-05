package com.tokopedia.core.common.category.data.network

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface

class TopAdsAuthInterceptor(context: Context, abstractionRouter: NetworkRouter, userSession : UserSessionInterface) : TkpdAuthInterceptor(context, abstractionRouter, userSession) {

    override fun getHeaderMap(path: String, strParam: String, method: String, authKey: String, contentTypeHeader: String): Map<String, String> {
        val headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader)
        val accessToken = userSession.accessToken
        headerMap[PARAM_X_DATE] = headerMap[PARAM_DATE]
        headerMap[PARAM_X_AUTHORIZATION] = headerMap[PARAM_AUTHORIZATION]
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap[PARAM_AUTHORIZATION] = "$PARAM_BEARER $accessToken"
        }
        headerMap[PARAM_TKPD_USER_ID] = userSession.userId
        return headerMap
    }

    companion object {
        private const val PARAM_AUTHORIZATION = "Authorization"
        private const val PARAM_X_AUTHORIZATION = "X-Tkpd-Authorization"
        private const val PARAM_TKPD_USER_ID = "Tkpd-UserId"
        private const val PARAM_X_DATE = "X-Date"
        private const val PARAM_DATE = "Date"
        private const val PARAM_BEARER = "Bearer"
    }
}