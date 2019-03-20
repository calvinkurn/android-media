package com.tokopedia.wishlist.common.data.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;

import java.util.Map;

public class MojitoInterceptor extends TkpdAuthInterceptor {

    private static final String KEY_MOJITO = "mojito_api_v1";

    private static final String HEADER_DEVICE = "X-Device";
    public static final String ANDROID = "android";
    private static final String PARAM_AUTHORIZATION = "accounts-authorization";
    private static final String PARAM_BEARER = "Bearer";

    public MojitoInterceptor(Context context, AbstractionRouter abstractionRouter) {
        super(context, abstractionRouter, KEY_MOJITO);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {
        Map<String, String> headerMap = super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
        String accessToken = userSession.getAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap.put(PARAM_AUTHORIZATION, PARAM_BEARER + " " + accessToken);
        }
        headerMap.put(HEADER_DEVICE, ANDROID);
        return headerMap;
    }
}