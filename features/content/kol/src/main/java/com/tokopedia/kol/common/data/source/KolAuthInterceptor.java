package com.tokopedia.kol.common.data.source;

import android.content.Context;
import android.net.Network;
import android.os.Build;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author by milhamj on 06/03/18.
 */

public class KolAuthInterceptor extends TkpdAuthInterceptor {
    private static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";
    private static final String HEADER_OS_VERSION = "os_version";

    @Inject
    public KolAuthInterceptor(@ApplicationContext Context context,
                              NetworkRouter networkRouter,
                              UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
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
        headerMap.put(HEADER_OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));
        return headerMap;
    }
}
