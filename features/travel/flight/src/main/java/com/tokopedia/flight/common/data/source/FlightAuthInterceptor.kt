package com.tokopedia.flight.common.data.source

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.network.AuthUtil
import com.tokopedia.network.NetworkRouter
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.GzipSource
import okio.buffer
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Created by Furqan on 09/06/2021.
 */
class FlightAuthInterceptor @Inject constructor(@ApplicationContext context: Context,
                                                networkRouter: NetworkRouter,
                                                userSessionInterface: UserSessionInterface)
    : TkpdAuthInterceptor(context, networkRouter, userSessionInterface) {

    init {
        maxRetryAttempt = 0
    }

    override fun getHeaderMap(path: String?, strParam: String?, method: String?, authKey: String?, contentTypeHeader: String?): MutableMap<String, String> {
        val newPath = path?.replace("/travel", "") ?: ""
        this.authKey = AuthUtil.KEY.KEY_WSV4
        return AuthUtil.generateHeadersWithXUserId(newPath, strParam, method, this.authKey,
                contentTypeHeader, userSession.userId, userSession.deviceId, userSession)
    }

    override fun getResponse(chain: Interceptor.Chain?, request: Request?): Response {
        val oldResponse = super.getResponse(chain, request)
        val contentEncoding = oldResponse.header(PARAM_CONTENT_ENCODING)
        contentEncoding?.let {
            if (it.equals(KEY_ZIP_ENCODING, true)) {
                oldResponse.body()?.let { oldBody ->
                    val source: GzipSource = GzipSource(oldBody.source())
                    val bodyString = source.buffer().readUtf8()
                    print(bodyString)

                    val responseBody = ResponseBody.create(oldBody.contentType(), bodyString)

                    val builder = Response.Builder()
                    builder.body(responseBody)
                            .headers(oldResponse.headers())
                            .message(oldResponse.message())
                            .handshake(oldResponse.handshake())
                            .protocol(oldResponse.protocol())
                            .cacheResponse(oldResponse.cacheResponse())
                            .priorResponse(oldResponse.priorResponse())
                            .code(oldResponse.code())
                            .request(oldResponse.request())
                            .networkResponse(oldResponse.networkResponse())

                    return builder.build()
                }
                throw UnknownHostException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            }
        }
        return oldResponse
    }

    companion object {
        private const val PARAM_CONTENT_ENCODING = "Content-Encoding"
        private const val KEY_ZIP_ENCODING = "gzip"
    }

}