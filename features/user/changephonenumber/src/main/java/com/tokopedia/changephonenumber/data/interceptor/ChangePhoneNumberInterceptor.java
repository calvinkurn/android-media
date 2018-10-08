package com.tokopedia.changephonenumber.data.interceptor;

import android.content.Context;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import static com.tokopedia.network.utils.AuthUtil.generateContentMd5;
import static com.tokopedia.network.utils.AuthUtil.generateDate;

/**
 * @author by alvinatin on 08/10/18.
 */

public class ChangePhoneNumberInterceptor extends TkpdAuthInterceptor {

    public ChangePhoneNumberInterceptor(@ApplicationContext Context context,
                                        NetworkRouter networkRouter,
                                        UserSession userSession) {
        super(context, networkRouter, userSession);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method,
                                               String authKey, String contentTypeHeader) {
        return generateHeaders(strParam, method, contentTypeHeader,
                userSession.getUserId());
    }

    private Map<String, String> generateHeaders(String strParam, String method, String contentType,
                                                String userId) {
        Map<String, String> finalHeader = this.getDefaultHeaderMap(
                strParam, method, contentType != null ? contentType : AuthUtil.CONTENT_TYPE,
                AuthUtil.DATE_FORMAT, userId);
        finalHeader.put(AuthUtil.HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        return finalHeader;
    }

    private Map<String, String> getDefaultHeaderMap(String strParam, String method,
                                                    String contentType, String dateFormat,
                                                    String userId) {
        String date = generateDate(dateFormat);
        String contentMD5 = generateContentMd5(strParam);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(AuthUtil.HEADER_CONTENT_TYPE, contentType);
        headerMap.put(AuthUtil.HEADER_X_METHOD, method);
        headerMap.put(AuthUtil.HEADER_REQUEST_METHOD, method);
        headerMap.put(AuthUtil.HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(AuthUtil.HEADER_DATE, date);
        headerMap.put(AuthUtil.HEADER_AUTHORIZATION, HEADER_PARAM_BEARER + " " + getFreshToken());
        headerMap.put(AuthUtil.HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(AuthUtil.HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(AuthUtil.HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(AuthUtil.HEADER_OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));

        headerMap.put(AuthUtil.HEADER_USER_ID, userId);
        headerMap.put(AuthUtil.HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    private String getFreshToken() {
        return userSession.getAccessToken();
    }
}
