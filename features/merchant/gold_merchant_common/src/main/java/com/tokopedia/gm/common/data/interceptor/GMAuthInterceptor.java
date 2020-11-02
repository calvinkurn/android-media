package com.tokopedia.gm.common.data.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


public class GMAuthInterceptor extends TkpdAuthInterceptor {

    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String BEARER = "Bearer ";

    public GMAuthInterceptor(@Nullable Context context, @NotNull UserSessionInterface userSession, @NotNull NetworkRouter abstractionRouter) {
        super(context, abstractionRouter, userSession);
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
