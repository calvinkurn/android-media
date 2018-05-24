package com.tokopedia.instantloan.network;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import java.util.Map;

import javax.inject.Inject;

public class InstantLoanAuthInterceptor extends TkpdAuthInterceptor {
    private static final String PARAM_AUTHORIZATION = "Authorization";
    private static final String PARAM_BEARER = "Bearer";

    @Inject
    public InstantLoanAuthInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter, UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {
        Map<String, String> headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
        String accessToken = userSession.getAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap.put(PARAM_AUTHORIZATION, PARAM_BEARER + " " + accessToken);
        }
        return headerMap;
    }
}