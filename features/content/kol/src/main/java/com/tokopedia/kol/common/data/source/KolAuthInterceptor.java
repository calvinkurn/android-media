package com.tokopedia.kol.common.data.source;

import android.content.Context;
import android.os.Build;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author by milhamj on 06/03/18.
 */

public class KolAuthInterceptor extends TkpdAuthInterceptor {
    private static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";
    private static final String HEADER_OS_TYPE = "os_type";

    @Inject
    public KolAuthInterceptor(@ApplicationContext Context context,
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
        headerMap.put(HEADER_OS_TYPE, Build.VERSION.RELEASE);
        return headerMap;
    }
}
