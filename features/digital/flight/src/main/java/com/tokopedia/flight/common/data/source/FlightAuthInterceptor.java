package com.tokopedia.flight.common.data.source;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by zulfikarrahman on 11/16/17.
 */

public class FlightAuthInterceptor extends TkpdAuthInterceptor {
    private static final String PARAM_CONTENT_ENCODING = "Content-Encoding";
    private static final String KEY_ZIP_ENCODING = "gzip";

    @Inject
    public FlightAuthInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
        this.maxRetryAttempt = 0;
    }


    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        String newPath = path.replace("/travel", "");
        return AuthUtil.generateHeadersWithXUserId(newPath,strParam,method,authKey,
                contentTypeHeader,userSession.getUserId(), userSession.getDeviceId(), userSession);
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        Response oldResponse = super.getResponse(chain, request);
        String contentEncoding = oldResponse.header(PARAM_CONTENT_ENCODING);
        if (contentEncoding != null && contentEncoding.equalsIgnoreCase(KEY_ZIP_ENCODING)) {
            if (oldResponse.body() != null) {
                GzipSource source = new GzipSource(oldResponse.body().source());
                String bodyString = Okio.buffer(source).readUtf8();
                System.out.println(bodyString);
                ResponseBody body = ResponseBody.create(oldResponse.body().contentType(), bodyString);

                Response.Builder builder = new Response.Builder();
                builder.body(body)
                        .headers(oldResponse.headers())
                        .message(oldResponse.message())
                        .handshake(oldResponse.handshake())
                        .protocol(oldResponse.protocol())
                        .cacheResponse(oldResponse.cacheResponse())
                        .priorResponse(oldResponse.priorResponse())
                        .code(oldResponse.code())
                        .request(oldResponse.request())
                        .networkResponse(oldResponse.networkResponse());

                return builder.build();
            }
            throw new UnknownHostException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        }
        return oldResponse;
    }
}
