package com.tokopedia.feedplus.data;

import android.content.Context;
import android.os.Build;

import com.tokopedia.abstraction.AbstractionRouter;
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
    private static final String HEADER_ACC_AUTH = "Accounts-Authorization";
    private static final String BEARER = "Bearer ";
    private static final String HEADER_OS_VERSION = "os_version";

    @Inject
    public FeedAuthInterceptor(@ApplicationContext Context context,
                              AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter);
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
                userSession.getDeviceId(),
                userSession);
        headerMap.put(HEADER_TKPD_USER_ID, userSession.getUserId());
        headerMap.put(HEADER_ACC_AUTH, BEARER + userSession.getAccessToken());
        headerMap.put(HEADER_OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));
        return headerMap;
    }
}
