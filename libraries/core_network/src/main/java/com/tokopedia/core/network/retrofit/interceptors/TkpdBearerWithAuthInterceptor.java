package com.tokopedia.core.network.retrofit.interceptors;

import android.support.annotation.NonNull;

import com.tokopedia.core.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public abstract class TkpdBearerWithAuthInterceptor extends TkpdAuthInterceptor{
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, getContentType(), authKey, DATE_FORMAT);

        String xTkpdAuthorization = headerMap.get(HEADER_AUTHORIZATION);
        headerMap.put(X_TKPD_HEADER_AUTHORIZATION, xTkpdAuthorization);

        headerMap.remove(HEADER_AUTHORIZATION);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String bearerAutorization = BEARER + userSession.getAccessToken();
        headerMap.put(HEADER_AUTHORIZATION, bearerAutorization);

        return headerMap;
    }

    @NonNull
    protected abstract String getContentType();
}
