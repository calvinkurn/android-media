package com.tokopedia.wishlist.common.data.interceptor;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

public class MojitoInterceptor extends TkpdAuthInterceptor {

    private static final String KEY_MOJITO = "mojito_api_v1";

    public MojitoInterceptor(Context context, NetworkRouter networkRouter, UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface, KEY_MOJITO);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {

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