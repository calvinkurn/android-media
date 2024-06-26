package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;
import android.util.Base64;

import androidx.collection.ArrayMap;

import com.tokopedia.network.authentication.AuthConstant;
import com.tokopedia.network.authentication.AuthHelper;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.user.session.UserSession;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Angga.Prasetiyo on 25/11/2015.
 * Modified by kulomady add method without params
 */

@Deprecated
public class AuthUtil {
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String MAC_ALGORITHM = "HmacSHA1";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_X_METHOD = "X-Method";
    private static final String HEADER_REQUEST_METHOD = "Request-Method";
    private static final String HEADER_CONTENT_MD5 = "Content-MD5";
    private static final String HEADER_DATE = "Date";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_ACCOUNTS_AUTHORIZATION = "accounts-authorization";
    private static final String HEADER_TKPD_SESSION_ID = "tkpd-sessionid";
    private static final String HEADER_TKPD_USER_AGENT = "tkpd-useragent";

    private static final String HEADER_USER_ID = "X-User-ID";
    private static final String HEADER_X_TKPD_USER_ID = "X-Tkpd-UserId";
    public static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    public static final String HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name";
    private static final String HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version";
    private static final String HEADER_CACHE_CONTROL = "cache-control";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";
    private static final String PARAM_X_TKPD_USER_ID = "x-tkpd-userid";
    private static final String PARAM_BEARER = "Bearer ";

    public static final String WEBVIEW_FLAG_PARAM_FLAG_APP = "flag_app";
    public static final String WEBVIEW_FLAG_PARAM_DEVICE = "device";
    public static final String WEBVIEW_FLAG_PARAM_UTM_SOURCE = "utm_source";
    public static final String WEBVIEW_FLAG_PARAM_APP_VERSION = "app_version";

    public static final String DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_FLAG_APP = "1";
    public static final String DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE = "android";
    public static final String DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_UTM_SOURCE = "android";

    public static final String HEADER_HMAC_SIGNATURE_KEY = "TKPDROID AndroidApps:";

    /**
     * default key is KEY_WSV$
     */
    public static class KEY {
        private static final int[] RAW_KEY_WSV4 = new int[]{65, 107, 102, 105, 101, 119, 56, 51, 52, 50, 57, 56, 80, 79, 105, 110, 118};
        private static final int[] RAW_SCROOGE_KEY = new int[]{49, 50, 69, 56, 77, 105, 69, 55, 89, 69, 54, 86, 122, 115, 69, 80, 66, 80, 101, 77};
        private static final int[] RAW_ZEUS_KEY = new int[]{102, 100, 100, 98, 100, 56, 49, 101, 101, 52, 49, 49, 54, 98, 56, 99, 98, 55, 97, 52, 48, 56, 100, 55, 102, 98, 102, 98, 57, 99, 49, 55};
        private static final int[] RAW_BRANCHIO_KEY_ID = new int[]{107, 101, 121, 95, 108, 105, 118, 101, 95, 97, 98, 104, 72, 103, 73, 104, 49, 68, 81, 105, 117, 80, 120, 100, 66, 78, 103, 57, 69, 88, 101, 112, 100, 68, 117, 103, 119, 119, 107, 72, 114};

        public static final String KEY_WSV4_NEW = convert(RAW_KEY_WSV4);
        public static final String KEY_WSV4 = "web_service_v4";
        public static final String KEY_MOJITO = "mojito_api_v1";
        public static String KEY_BRANCHIO = convert(RAW_BRANCHIO_KEY_ID);
    }

    public static Map<String, String> generateHeadersWithXUserId(
            String path, String strParam, String method, String authKey, String contentType
    ) {
        String date = generateDate(DATE_FORMAT);
        String contentMD5 = generateContentMd5(strParam);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();

        String authString = method
                + "\n" + contentMD5
                + "\n" + contentType
                + "\n" + date
                + "\n" + PARAM_X_TKPD_USER_ID + ":" + userId
                + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(AuthConstant.HEADER_USER_AGENT, AuthHelper.getUserAgent());
        headerMap.put(HEADER_CONTENT_TYPE, contentType != null ? contentType : CONTENT_TYPE);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        headerMap.remove(HEADER_ACCOUNTS_AUTHORIZATION);
        headerMap.put(HEADER_ACCOUNTS_AUTHORIZATION,String.format("%s %s", "Bearer",
                userSession.getAccessToken()));
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);
        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_X_TKPD_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateWebviewHeaders(String path,
                                                             String strParam,
                                                             String method,
                                                             String authKey) {
        Map<String, String> finalHeader = getDefaultHeaderMap(
                path,
                strParam,
                method,
                CONTENT_TYPE,
                authKey,
                DATE_FORMAT
        );
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        finalHeader.remove(HEADER_ACCOUNTS_AUTHORIZATION);
        finalHeader.put(HEADER_ACCOUNTS_AUTHORIZATION, PARAM_BEARER + userSession.getAccessToken()
        );
        finalHeader.put(
                HEADER_TKPD_SESSION_ID,
                FCMCacheManager.getRegistrationIdWithTemp(CoreNetworkApplication.getAppContext())
        );
        finalHeader.put(
                HEADER_TKPD_USER_AGENT,
                DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE
        );
        return finalHeader;
    }

    public static Map<String, String> generateHeaders(
            String path, String strParam, String method, String authKey, String contentType
    ) {
        Map<String, String> finalHeader = getDefaultHeaderMap(
                path, strParam, method, contentType != null ? contentType : CONTENT_TYPE,
                authKey, DATE_FORMAT
        );
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        return finalHeader;
    }

    public static Map<String, String> generateHeadersNew(
            String path, String strParam, String method, String authKey, String contentType
    ) {
        Map<String, String> finalHeader = getDefaultHeaderMapNew(
                path, strParam, method, contentType != null ? contentType : CONTENT_TYPE,
                authKey, DATE_FORMAT
        );
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        return finalHeader;
    }

    public static Map<String, String> generateHeaders(String path, String method, String authKey) {
        Map<String, String> finalHeader = getDefaultHeaderMap(path, "", method, CONTENT_TYPE_JSON, authKey, DATE_FORMAT);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        finalHeader.put(HEADER_USER_ID, userId);
        finalHeader.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return finalHeader;
    }

    //depecrated, use getDefaultHeaderMapNew() instead
    @Deprecated
    public static Map<String, String> getDefaultHeaderMap(String path, String strParam, String method,
                                                          String contentType, String authKey, String dateFormat) {
        String date = generateDate(dateFormat);
        String contentMD5 = generateContentMd5(strParam);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();

        String authString = method + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(AuthConstant.HEADER_USER_AGENT, AuthHelper.getUserAgent());
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        headerMap.remove(HEADER_ACCOUNTS_AUTHORIZATION);
        headerMap.put(HEADER_ACCOUNTS_AUTHORIZATION, "Bearer " + userSession.getAccessToken());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);

        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> getDefaultHeaderMapNew(String path, String strParam, String method,
                                                             String contentType, String authKey, String dateFormat) {
        String date = generateDate(dateFormat);
        String contentMD5 = generateContentMd5(strParam);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();

        String authString = method + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(AuthConstant.HEADER_USER_AGENT, AuthHelper.getUserAgent());
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, HEADER_HMAC_SIGNATURE_KEY + signature.trim());
        headerMap.remove(HEADER_ACCOUNTS_AUTHORIZATION);
        headerMap.put(HEADER_ACCOUNTS_AUTHORIZATION, "Bearer " + userSession.getAccessToken());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);

        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateBothAuthHeadersAccount(String path, String strParam, String method,
                                                                     String contentType, String authKey, String dateFormat) {

        String date = generateDate(dateFormat);
        String contentMD5 = generateContentMd5(strParam);
        String authString = method + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, KEY.KEY_WSV4);

        Map<String, String> finalHeader = generateHeadersAccount(authKey);
        finalHeader.put(X_TKPD_HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        finalHeader.put(HEADER_REQUEST_METHOD, method);
        finalHeader.put(HEADER_CONTENT_MD5, contentMD5);
        finalHeader.put(HEADER_DATE, date);

        return finalHeader;
    }

    public static Map<String, String> generateHeadersAccount(String authKey) {
        String clientID = "7ea919182ff";
        String clientSecret = "b36cbf904d14bbf90e7f25431595a364";
        String encodeString = clientID + ":" + clientSecret;

        String asB64 = Base64.encodeToString(encodeString.getBytes(), Base64.NO_WRAP);

        Map<String, String> finalHeader = new HashMap<>();
        finalHeader.put(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        finalHeader.put(HEADER_CACHE_CONTROL, "no-cache");
        if (authKey.length() == 0) {
            finalHeader.put(HEADER_AUTHORIZATION, "Basic " + asB64);
        } else {
            finalHeader.put(HEADER_AUTHORIZATION, authKey);
        }
        finalHeader.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        finalHeader.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        finalHeader.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        finalHeader.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        finalHeader.put(AuthConstant.HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX);
        return finalHeader;
    }

    @Deprecated
    public static Map<String, String> generateParams(String userId, String deviceId, Map<String, String> params) {
        String hash = md5(userId + "~" + deviceId);
        params = MapNulRemover.removeNull(params);
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }

    /**
     * Tolong jangan dipake lagi
     *
     * @param context context
     * @param params  map
     * @return generatedMap
     * @see #generateParamsNetwork(Context, TKPDMapParam)
     */
    @Deprecated
    public static Map<String, String> generateParams(Context context, Map<String, String> params) {
        params = MapNulRemover.removeNull(params);
        String deviceId = GCMHandler.getRegistrationId(context);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }

    /**
     * Tolong jangan dipake lagi
     *
     * @param context context
     * @return generatedMap
     * @see #generateParamsNetwork(Context)
     */
    @Deprecated
    public static Map<String, String> generateParams(Context context) {
        String deviceId = GCMHandler.getRegistrationId(context);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);
        Map<String, String> params = new HashMap<>();
        params = MapNulRemover.removeNull(params);
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }


    /**
     * Tolong jangan dipake lagi
     *
     * @param context context
     * @param params  param
     * @param userId  user id
     * @return generatedMap
     * @see #generateParamsNetwork(Context, TKPDMapParam, String)
     */
    @Deprecated
    public static Map<String, String> generateParams(Context context, Map<String, String> params, String userId) {
        params = MapNulRemover.removeNull(params);
        String deviceId = GCMHandler.getRegistrationId(context);
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }

    public static TKPDMapParam<String, String> generateParamsNetwork(String userId, String deviceId, TKPDMapParam<String, String> params) {
        String hash = md5(userId + "~" + deviceId);
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return params;
    }

    public static TKPDMapParam<String, Object> generateParamsNetwork2(Context context, TKPDMapParam<String, Object> params) {
        String deviceId = GCMHandler.getRegistrationId(context);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }

    public static TKPDMapParam<String, String> generateParamsNetwork(Context context, TKPDMapParam<String, String> params) {
        String deviceId = GCMHandler.getRegistrationId(context);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
//        params.put(PARAM_X_TKPD_USER_ID, userId);
        return params;
    }

    public static String calculateRFC2104HMAC(String authString, String authKey) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(authKey.getBytes(), MAC_ALGORITHM);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(authString.getBytes());
            String asB64 = Base64.encodeToString(rawHmac, Base64.DEFAULT);
            return asB64;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String generateContentMd5(String s) {
        return md5(s);
    }

    private static String generateDate(String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return simpleDateFormat.format(new Date());
    }

    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String convert(int[] key) {
        String finalKey = "";
        for (int i : key) {
            finalKey = finalKey + Character.toString((char) i);
        }
        return finalKey;
    }
}
