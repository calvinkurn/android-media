package com.tokopedia.flight.orderlist.network;

import android.content.Context;
import android.net.Network;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.constant.ErrorNetMessage;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

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

public class FlightOrderAuthInterceptor extends TkpdAuthInterceptor {
    private static final String PARAM_CONTENT_ENCODING = "Content-Encoding";
    private static final String KEY_ZIP_ENCODING = "gzip";

    @Inject
    public FlightOrderAuthInterceptor(@ApplicationContext Context context, UserSessionInterface userSession, NetworkRouter abstractionRouter) {
        super(context, abstractionRouter, userSession);
        this.maxRetryAttempt = 0;
    }


    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        String newPath = path.replace("/travel", "");
        this.authKey = AuthUtil.KEY.KEY_WSV4;
        return AuthUtil.generateHeadersWithXUserId(newPath,strParam,method,this.authKey,
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
