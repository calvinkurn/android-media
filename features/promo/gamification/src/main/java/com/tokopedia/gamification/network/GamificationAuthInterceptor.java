package com.tokopedia.gamification.network;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import java.util.Map;

import javax.inject.Inject;

import okhttp3.Request;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GamificationAuthInterceptor extends TkpdAuthInterceptor {

    private static final String HEADER_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";
    private static final String BEARER_SPACE = "Bearer ";
    @Inject
    public GamificationAuthInterceptor(@ApplicationContext Context context,
                                       AbstractionRouter abstractionRouter,
                                       UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {
        Map<String, String> headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
        String accessToken = userSession.getAccessToken();
        headerMap.remove(HEADER_ACCOUNTS_AUTHORIZATION);
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap.put(HEADER_ACCOUNTS_AUTHORIZATION, BEARER_SPACE + accessToken);
        }
        return headerMap;
    }
}
