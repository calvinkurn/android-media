package com.tokopedia.kol.common.data.source

import android.content.Context
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by milhamj on 06/03/18.
 */
class KolAuthInterceptor @Inject constructor(
    @ApplicationContext context: Context?,
    networkRouter: NetworkRouter?,
    userSessionInterface: UserSessionInterface?
) : TkpdAuthInterceptor(context, networkRouter, userSessionInterface) {

    override fun getHeaderMap(
        path: String,
        strParam: String,
        method: String,
        authKey: String,
        contentTypeHeader: String
    ): Map<String, String> {
        val headerMap = AuthUtil.generateHeadersWithXUserId(path,
            strParam,
            method,
            authKey,
            contentTypeHeader,
            userSession.userId,
            userSession.deviceId,
            userSession)
        headerMap[HEADER_TKPD_USER_ID] = userSession.userId
        headerMap[HEADER_OS_VERSION] = Build.VERSION.SDK_INT.toString()
        return headerMap
    }

    companion object {
        private const val HEADER_TKPD_USER_ID = "Tkpd-UserId"
        private const val HEADER_OS_VERSION = "os_version"
    }
}
