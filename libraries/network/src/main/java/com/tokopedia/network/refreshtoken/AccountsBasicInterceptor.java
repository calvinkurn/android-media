package com.tokopedia.network.refreshtoken;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;

/**
 * @author by nisie on 1/17/18.
 */

public class AccountsBasicInterceptor extends TkpdAuthInterceptor {
    private static final String X_TKPD_PATH = "x-tkpd-path";

    public AccountsBasicInterceptor(Context context, NetworkRouter networkRouter,
                                    UserSessionInterface userSession) {
        super(context, networkRouter, userSession);
    }


    @Override
    protected void generateHmacAuthRequest(Request originRequest, Request.Builder newRequest)
            throws IOException {
        Map<String, String> authHeaders = AuthUtil.generateHeadersAccount("");
        authHeaders.put(X_TKPD_PATH, originRequest.url().uri().getPath());
        generateHeader(authHeaders, originRequest, newRequest);
    }
}
