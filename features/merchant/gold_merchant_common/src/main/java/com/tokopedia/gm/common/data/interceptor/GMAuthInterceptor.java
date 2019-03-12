package com.tokopedia.gm.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.util.Map;


public class GMAuthInterceptor extends TkpdAuthInterceptor {

    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String BEARER = "Bearer ";

    public GMAuthInterceptor(Context context,
                             AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = super.getHeaderMap(path,strParam, method, authKey, contentTypeHeader);

        String xTkpdAuthorization = headerMap.get(AuthUtil.HEADER_AUTHORIZATION);
        headerMap.put(X_TKPD_HEADER_AUTHORIZATION, xTkpdAuthorization);

        headerMap.remove(AuthUtil.HEADER_AUTHORIZATION);
        String bearerAutorization = BEARER + userSession.getAccessToken();
        headerMap.put(AuthUtil.HEADER_AUTHORIZATION, bearerAutorization);
        return headerMap;
    }
}
