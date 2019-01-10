package com.tokopedia.topchat.common.network;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

/**
 * @author anggaprasetiyo on 3/3/17.
 */

public class XUserIdInterceptor extends TkpdAuthInterceptor {
    private static final String TAG = XUserIdInterceptor.class.getSimpleName();

    public XUserIdInterceptor(@ApplicationContext Context context,
                              NetworkRouter networkRouter,
                              UserSession userSession) {
        super(context, networkRouter, userSession, "web_service_v4");
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader
    ) {
        return AuthUtil.generateHeadersWithXUserId(
                path, strParam, method, authKey, contentTypeHeader,
                userSession.getUserId(), userSession.getDeviceId()
        );
    }
}
