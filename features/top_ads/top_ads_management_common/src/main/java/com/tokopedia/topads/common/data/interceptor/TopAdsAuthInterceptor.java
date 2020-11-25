package com.tokopedia.topads.common.data.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class TopAdsAuthInterceptor extends TkpdAuthInterceptor {
    private static final String PARAM_AUTHORIZATION = "Authorization";
    private static final String PARAM_X_AUTHORIZATION = "X-Tkpd-Authorization";
    private static final String PARAM_TKPD_USER_ID = "Tkpd-UserId";
    private static final String PARAM_X_DATE = "X-Date";
    private static final String PARAM_DATE = "Date";
    private static final String PARAM_BEARER = "Bearer";

    public TopAdsAuthInterceptor(Context context, UserSession userSession, NetworkRouter abstractionRouter) {
        super(context, abstractionRouter, userSession);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
        String accessToken = userSession.getAccessToken();
        headerMap.put(PARAM_X_DATE, headerMap.get(PARAM_DATE));
        headerMap.put(PARAM_X_AUTHORIZATION, headerMap.get(PARAM_AUTHORIZATION));
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap.put(PARAM_AUTHORIZATION, PARAM_BEARER + " " + accessToken);
        }
        headerMap.put(PARAM_TKPD_USER_ID, userSession.getUserId());
        return headerMap;
    }
}
