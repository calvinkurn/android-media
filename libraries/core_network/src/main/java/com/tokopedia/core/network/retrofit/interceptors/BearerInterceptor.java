package com.tokopedia.core.network.retrofit.interceptors;

import androidx.collection.ArrayMap;
import android.text.TextUtils;

import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

@Deprecated
public class BearerInterceptor extends TkpdAuthInterceptor {
    private static final String HEADER_DEVICE = "X-Device";
    public static final String ANDROID = "android";
    private static final String PARAM_AUTHORIZATION = "Authorization";
    private static final String PARAM_BEARER = "Bearer";

    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {
        Map<String, String> headerMap = new ArrayMap<>();
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String accessToken = userSession.getAccessToken();
        if (!TextUtils.isEmpty(accessToken)) {
            headerMap.put(PARAM_AUTHORIZATION, PARAM_BEARER + " " + accessToken);
        }
        headerMap.put(HEADER_DEVICE, ANDROID);
        return headerMap;
    }
}
