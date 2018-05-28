package com.tokopedia.feedplus.data;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author by milhamj on 28/05/18.
 */

public class FeedAuthInterceptor extends TkpdAuthInterceptor {

    private static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";

    @Inject
    public FeedAuthInterceptor(@ApplicationContext Context context,
                              AbstractionRouter abstractionRouter,
                              UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }


    @Override
    protected Map<String, String> getHeaderMap(String path,
                                               String strParam,
                                               String method,
                                               String authKey,
                                               String contentTypeHeader) {
        Map<String, String> headerMap = AuthUtil.generateHeadersWithXUserId(path,
                strParam,
                method,
                authKey,
                contentTypeHeader,
                userSession.getUserId(),
                userSession.getDeviceId());
        headerMap.put(HEADER_TKPD_USER_ID, userSession.getUserId());
        return headerMap;
    }
}
