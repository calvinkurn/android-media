package com.tokopedia.train.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class TrainInterceptor extends TkpdAuthInterceptor {
    private static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";

    public TrainInterceptor(@ApplicationContext Context context,
                            NetworkRouter networkRouter,
                            UserSessionInterface userSession) {
        super(context, networkRouter, userSession);
        this.maxRetryAttempt = 0;
    }

    @Override
    protected Map<String, String> getHeaderMap(String path,
                                               String strParam,
                                               String method,
                                               String authKey,
                                               String contentTypeHeader) {

        Map<String, String> headerMap = AuthUtil.generateHeadersWithBearer(path,
                strParam,
                method,
                authKey,
                userSession.getUserId(),
                userSession.getAccessToken()
        );
        headerMap.put(HEADER_TKPD_USER_ID, userSession.getUserId());
        return headerMap;
    }


    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            return chain.proceed(request);
        } catch (Error e) {
            throw new UnknownHostException();
        }
    }
}