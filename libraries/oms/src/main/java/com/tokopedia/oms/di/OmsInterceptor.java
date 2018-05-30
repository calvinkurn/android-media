package com.tokopedia.oms.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OmsInterceptor extends TkpdAuthInterceptor {

    private final static String BEARER = "Bearer";
    private final static String AUTHORIZATION = "authorization";

    private UserSession userSession;

    @Inject
    public OmsInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter,
                          UserSession userSession) {
        super(context, abstractionRouter, userSession);
        this.userSession = userSession;
    }

//    @Override
//    protected Map<String, String> getHeaderMap(
//            String path, String strParam, String method, String authKey, String contentTypeHeader) {
//        Map<String, String> header = new HashMap<>();
//        header.put(AUTHORIZATION, BEARER + " " + userSession.getAccessToken());
//        header.put("content-type", "application/json");
//        header.put("tkpd-userid", userSession.getUserId());
//        return header;
//    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;

        newRequest = request.newBuilder()
                .addHeader(AUTHORIZATION, BEARER + " " + userSession.getAccessToken())
                .addHeader("content-type", "application/json")
                .addHeader("tkpd-userid", userSession.getUserId())
                .build();
        return chain.proceed(newRequest);
    }
}