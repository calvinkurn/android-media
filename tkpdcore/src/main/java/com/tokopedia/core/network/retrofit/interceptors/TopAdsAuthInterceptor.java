package com.tokopedia.core.network.retrofit.interceptors;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Nathaniel on 11/23/2016.
 */

public class TopAdsAuthInterceptor extends TkpdAuthInterceptor {

    private static final String HEADER_DATE_FORMAT = "dd MMM yy HH:mm ZZZ";

    private static final String CONTENT_TYPE = "";
    private static final String HEADER_DATE = "X-Date";
    private static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_USER_ID = "Tkpd-UserId";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_X_AUTHORIZATION = "X-Tkpd-Authorization";

    private String bearerToken;

    public TopAdsAuthInterceptor(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey) {
        Map<String, String> headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, CONTENT_TYPE, authKey, HEADER_DATE_FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(HEADER_DATE_FORMAT, Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_USER_ID, SessionHandler.getLoginID(MainApplication.getAppContext()));
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);

        headerMap.put(HEADER_X_AUTHORIZATION, headerMap.get(HEADER_AUTHORIZATION));
        headerMap.put(HEADER_AUTHORIZATION, bearerToken);
        return headerMap;
    }
}