package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;

import java.util.Map;

/**
 * Created by nisie on 3/7/17.
 */

public class AuthorizationInterceptor extends TkpdAuthInterceptor {
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String CONTENT_TYPE_JSON_UT = "application/json; charset=UTF-8";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey) {
        Map<String, String> headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, CONTENT_TYPE_JSON_UT, authKey, DATE_FORMAT);

        String xTkpdAuthorization = headerMap.get(HEADER_AUTHORIZATION);
        headerMap.put(X_TKPD_HEADER_AUTHORIZATION, xTkpdAuthorization);

        headerMap.remove(HEADER_AUTHORIZATION);
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        String bearerAutorization = BEARER + sessionHandler.getAccessToken(MainApplication.getAppContext());
        headerMap.put(HEADER_AUTHORIZATION, bearerAutorization);

        return headerMap;
    }
}
