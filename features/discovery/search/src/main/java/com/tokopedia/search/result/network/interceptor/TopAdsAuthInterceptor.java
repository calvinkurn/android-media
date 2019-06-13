package com.tokopedia.search.result.network.interceptor;

import android.content.Context;

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.AuthUtil;
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
                // do nothing
                break;
        }
        Map headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, contentType, authKey, "dd MMM yy HH:mm ZZZ", userSession.getUserId(), userSession);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy HH:mm ZZZ", Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        headerMap.put("X-Date", date);
        headerMap.put("X-Device", "android-" + VERSION_NAME);
        headerMap.put("X-Tkpd-Authorization", headerMap.get("Authorization"));
        headerMap.put("Authorization", "Bearer " + userSession.getAccessToken());
        return headerMap;
    }
}
