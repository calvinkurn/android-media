package com.tokopedia.gamification.network;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 3/29/18.
 */

public class GamificationAuthInterceptor extends TkpdAuthInterceptor {

    private static final String HEADER_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization";
    private static final String BEARER_SPACE = "Bearer ";
    @Inject
    public GamificationAuthInterceptor(@ApplicationContext Context context,
                                       AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
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

        if (userSession.isLoggedIn()) {
            headerMap.put(AuthUtil.HEADER_TKPD_USER_ID, userSession.getUserId());
            headerMap.put(AuthUtil.HEADER_X_TKPD_USER_ID, userSession.getUserId());
        }
        
        return headerMap;
    }
}
