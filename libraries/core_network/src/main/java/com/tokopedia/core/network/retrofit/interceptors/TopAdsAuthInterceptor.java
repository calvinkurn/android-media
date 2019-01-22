package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.user.session.UserSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by normansyahputa on 10/20/17.
 */

public class TopAdsAuthInterceptor extends TkpdAuthInterceptor {

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
        Map headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, contentType, authKey, "dd MMM yy HH:mm ZZZ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy HH:mm ZZZ", Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        headerMap.put("X-Date", date);
        headerMap.put("X-Device", "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put("X-Tkpd-Authorization", headerMap.get("Authorization"));
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        headerMap.put("Authorization", "Bearer " + userSession.getAccessToken());
        return headerMap;
    }
}
