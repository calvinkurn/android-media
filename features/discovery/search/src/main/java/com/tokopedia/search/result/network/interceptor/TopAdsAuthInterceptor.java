package com.tokopedia.search.result.network.interceptor;

import android.content.Context;

import com.tokopedia.authentication.AuthConstant;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.user.session.UserSessionInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class TopAdsAuthInterceptor extends TkpdAuthInterceptor {

    public static String VERSION_NAME = "1.0";

    TopAdsAuthInterceptor(Context context, NetworkRouter networkRouter, UserSessionInterface userSessionInterface) {
        super(context, networkRouter, userSessionInterface);
    }

    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        String contentType = "";
        switch (method) {
            case "PATCH":
            case "POST":
                contentType = "application/json";
                break;
            default:
            case "GET":
                break;
        }

        Map<String, String> headerMap = AuthHelper.getDefaultHeaderMap(
                path, strParam, method, contentType,
                authKey, "dd MMM yy HH:mm ZZZ", userSession);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy HH:mm ZZZ", Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        String headerAuth = headerMap.get(AuthConstant.HEADER_AUTHORIZATION);

        headerMap.put("X-Date", date);
        headerMap.put("X-Device", "android-" + VERSION_NAME);
        headerMap.put("X-Tkpd-Authorization", headerAuth == null ? "" : headerAuth);
        headerMap.put("Authorization", "Bearer " + userSession.getAccessToken());
        headerMap.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);

        return headerMap;
    }
}
