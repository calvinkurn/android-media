package com.tokopedia.purchase_platform.common.data.api;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * @author anggaprasetiyo on 23/04/18.
 */
public class CartApiInterceptor extends TkpdAuthInterceptor {

    private static final String RESPONSE_STATUS_REQUEST_DENIED = "REQUEST_DENIED";
    private static final String CART_ERROR_GLOBAL = "Maaf, terjadi sedikit kendala. Coba ulangi beberapa saat lagi ya";

    @Inject
    public CartApiInterceptor(Context context,
                              NetworkRouter networkRouter,
                              UserSessionInterface userSession,
                              String authKey) {
        super(context, networkRouter, userSession, authKey);
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String responseError = response.peekBody(512).string();
        if (!responseError.contains(RESPONSE_STATUS_REQUEST_DENIED))
            if (!responseError.isEmpty() && responseError.contains("header")) {
                throw new CartResponseErrorException(CART_ERROR_GLOBAL);
            }
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method,
                                               String authKey, String contentTypeHeader) {

        Map<String, String> mapHeader = AuthUtil.getDefaultHeaderMap(
                path,
                strParam,
                method,
                contentTypeHeader != null ? contentTypeHeader : "application/x-www-form-urlencoded",
                authKey,
                "EEE, dd MMM yyyy HH:mm:ss ZZZ", userSession.getUserId());

        mapHeader.put("X-APP-VERSION", GlobalConfig.VERSION_NAME);
        mapHeader.put("Tkpd-UserId", userSession.getUserId());
        mapHeader.put("X-Device", "android");
        mapHeader.put("Tkpd-SessionId", userSession.getDeviceId());
        mapHeader.put("Accounts-Authorization", String.format("%s %s", "Bearer",
                userSession.getAccessToken()));


        return mapHeader;
    }
}
