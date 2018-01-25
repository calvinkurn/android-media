package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.network.entity.topads.TopAds;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Created by normansyahputa on 10/20/17.
 */

public class TopAdsAuthInterceptor extends TkpdAuthInterceptor {
    @Nullable
    private SessionHandler sessionHandler;

    public TopAdsAuthInterceptor(SessionHandler sessionHandler){
        this.sessionHandler = sessionHandler;
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
        Map headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, contentType, authKey, "dd MMM yy HH:mm ZZZ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy HH:mm ZZZ", Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        headerMap.put("X-Date", date);
        // headerMap.put("Tkpd-UserId", SessionHandler.getLoginID(MainApplication.getAppContext()));
        headerMap.put("X-Device", "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put("X-Tkpd-Authorization", headerMap.get("Authorization"));
        if(sessionHandler == null) {
            headerMap.put("Authorization", "Bearer " + SessionHandler.getAccessToken());
        }else{
            headerMap.put("Authorization", "Bearer " + sessionHandler.getAuthAccessToken());
        }
        return headerMap;
    }
}
