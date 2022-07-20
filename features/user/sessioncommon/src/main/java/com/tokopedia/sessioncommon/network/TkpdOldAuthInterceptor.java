package com.tokopedia.sessioncommon.network;

import android.content.Context;
import android.os.Build;
import androidx.collection.ArrayMap;

import com.tokopedia.network.authentication.AuthConstant;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.network.authentication.AuthKey;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.sessioncommon.di.SessionModule;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.Map;

import javax.inject.Named;

/**
 * @author by nisie on 10/16/18.
 */
public class TkpdOldAuthInterceptor extends TkpdAuthInterceptor {
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_X_METHOD = "X-Method";
    private static final String HEADER_REQUEST_METHOD = "Request-Method";
    private static final String HEADER_CONTENT_MD5 = "Content-MD5";
    private static final String HEADER_DATE = "Date";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_USER_ID = "X-User-ID";
    private static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name";
    private static final String HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version";
    private static final String HEADER_OS_VERSION = "os_version";
    private static final String HEADER_HMAC_SIGNATURE_KEY = "TKPD Tokopedia:";
    private static final String PARAM_X_TKPD_USER_ID = "x-tkpd-userid";
    private static final String HEADER_PATH = "x-tkpd-path";


    public TkpdOldAuthInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession) {
        super(context, networkRouter, userSession);
    }

    public TkpdOldAuthInterceptor(Context context, NetworkRouter networkRouter,
                                  @Named(SessionModule.SESSION_MODULE) UserSessionInterface userSession) {
        super(context, networkRouter, userSession);
    }

    public TkpdOldAuthInterceptor(Context context, NetworkRouter networkRouter, UserSession userSession, String authKey) {
        super(context, networkRouter, userSession, authKey);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {

        Map<String, String> finalHeader = getDefaultHeaderMap(
                path, strParam, method, contentTypeHeader != null ? contentTypeHeader : CONTENT_TYPE,
                authKey, DATE_FORMAT, userSession.getUserId()
        );
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        return finalHeader;
    }

    public static Map<String, String> getDefaultHeaderMap(String path, String strParam, String method,
                                                          String contentType, String authKey,
                                                          String dateFormat, String userId) {
        String date = AuthHelper.generateDate(dateFormat);
        String contentMD5 = AuthHelper.getMD5Hash(strParam);

        String authString = method + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + path;
        String signature = AuthHelper.calculateRFC2104HMAC(authString, AuthKey.KEY_WSV4);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, HEADER_HMAC_SIGNATURE_KEY + signature.trim());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);
        headerMap.put(HEADER_OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));

        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(HEADER_PATH, path);

        return headerMap;
    }


}
