package com.tokopedia.posapp.common;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author okasurya on 3/26/18.
 */

public class PosAuthInterceptor extends TkpdAuthInterceptor {
    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String BEARER = "Bearer ";

    @Inject
    public PosAuthInterceptor(@ApplicationContext Context context,
                               AbstractionRouter abstractionRouter,
                               UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = AuthUtil.generateHeaders(path, strParam, method, authKey, CONTENT_TYPE_JSON, userSession.getUserId());

        String xTkpdAuthorization = headerMap.get(HEADER_AUTHORIZATION);
        headerMap.put(X_TKPD_HEADER_AUTHORIZATION, xTkpdAuthorization);

        headerMap.remove(HEADER_AUTHORIZATION);
        // TODO: 3/28/18 change this
        // hard code, dont do dis at home
        String bearerAutorization = BEARER + "LdBnlBuwQ3G41VzUwQZH7A";
        headerMap.put(HEADER_AUTHORIZATION, bearerAutorization);

        return headerMap;
    }
}
