package com.tokopedia.network.utils;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Base64;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.MapNulRemover;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;

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
 *         Modified by kulomady add method without params
 */
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
    private static final String HEADER_USER_ID = "X-User-ID";
    public static final String HEADER_X_TKPD_USER_ID = "X-Tkpd-UserId";
    public static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";
    public static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    public static final String HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name";
    private static final String HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version";
    private static final String HEADER_CACHE_CONTROL = "cache-control";
    private static final String HEADER_PATH = "x-tkpd-path";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String HEADER_X_MSISDN = "x-msisdn";
    private static final String HEADER_OS_TYPE = "os-type";
    private static final String HEADER_SESSION_ID = "tkpd-SessionId";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";
    private static final String PARAM_X_TKPD_USER_ID = "x-tkpd-userid";

    public static final String WEBVIEW_FLAG_PARAM_FLAG_APP = "flag_app";
    public static final String WEBVIEW_FLAG_PARAM_DEVICE = "device";
    public static final String WEBVIEW_FLAG_PARAM_UTM_SOURCE = "utm_source";
    public static final String WEBVIEW_FLAG_PARAM_APP_VERSION = "app_version";
    public static final String WEBVIEW_FLAG_PARAM_OS_VERSION = "os_version";

    public static final String DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_FLAG_APP = "1";
    public static final String DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_DEVICE = "android";
    public static final String DEFAULT_VALUE_WEBVIEW_FLAG_PARAM_UTM_SOURCE = "android";

    /**
     * default key is KEY_WSV$
     */
    public static class KEY {
        public static final String KEY_WSV4 = "web_service_v4";
        public static final String KEY_MOJITO = "mojito_api_v1";
        public static final String KEY_KEROPPI = "Keroppi";
        public static final String TOKO_CASH_HMAC = "CPAnAGpC3NIg7ZSj";
        public static String KEY_CREDIT_CARD_VAULT = "AdKc1ag2NmYgRUF97eQQ8J";
        public static String ZEUS_WHITELIST = "abf49d067c9ca8585f3a1059464d22b9";
    }

    public static Map<String, String> generateHeadersWithXUserId(
            String path, String strParam, String method, String authKey, String contentType, String userId
    ) {
        String date = generateDate(DATE_FORMAT);
        String contentMD5 = generateContentMd5(strParam);

        String authString = method
                + "\n" + contentMD5
                + "\n" + contentType
                + "\n" + date
                + "\n" + PARAM_X_TKPD_USER_ID + ":" + userId
                + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(HEADER_CONTENT_TYPE, contentType != null ? contentType : CONTENT_TYPE);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_X_TKPD_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateHeadersWithXUserId(
            String path, String strParam, String method, String authKey, String contentType, String userId, String deviceId
    ) {
        String date = generateDate(DATE_FORMAT);
        String contentMD5 = generateContentMd5(strParam);

        String authString = method
                + "\n" + contentMD5
                + "\n" + contentType
                + "\n" + date
                + "\n" + PARAM_X_TKPD_USER_ID + ":" + userId
                + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(HEADER_CONTENT_TYPE, contentType != null ? contentType : CONTENT_TYPE);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_SESSION_ID, deviceId);
        headerMap.put(HEADER_OS_TYPE, "1");
        headerMap.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_X_TKPD_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateHeadersWithXUserIdXMsisdn(
            String path, String method, String authKey, String contentType,
            String msisdn, String userId
    ) {
        String date = generateDate(DATE_FORMAT);

        String authString = method
                + "\n" + ""
                + "\n" + ""
                + "\n" + date
                + "\n" + PARAM_X_TKPD_USER_ID + ":" + userId
                + "\n" + HEADER_X_MSISDN + ":" + msisdn
                + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(HEADER_CONTENT_TYPE, contentType != null ? contentType : CONTENT_TYPE);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(HEADER_X_MSISDN, msisdn);
        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_X_TKPD_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateHeaders(String path, String strParam, String method, String authKey, String userId) {
        Map<String, String> finalHeader = getDefaultHeaderMap(path, strParam, method, CONTENT_TYPE, authKey, DATE_FORMAT, userId);
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        return finalHeader;
    }

    public static Map<String, String> generateHeaders(
            String path, String strParam, String method, String authKey, String contentType,
            String userId) {
        Map<String, String> finalHeader = getDefaultHeaderMap(
                path, strParam, method, contentType != null ? contentType : CONTENT_TYPE,
                authKey, DATE_FORMAT, userId
        );
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        return finalHeader;
    }


    public static Map<String, String> generateHeadersWithLoginId(String path, String method, String authKey, String userId) {
        Map<String, String> finalHeader = getDefaultHeaderMap(path, "", method, CONTENT_TYPE_JSON, authKey, DATE_FORMAT, userId);
        finalHeader.put(HEADER_USER_ID, userId);
        finalHeader.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return finalHeader;
    }

    public static Map<String, String> getDefaultHeaderMap(String path, String strParam, String method,
                                                          String contentType, String authKey, String dateFormat,
                                                          String userId) {
        String date = generateDate(dateFormat);
        String contentMD5 = generateContentMd5(strParam);

        String authString = method + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> headerMap = new ArrayMap<>();
        headerMap.put(HEADER_CONTENT_TYPE, contentType);
        headerMap.put(HEADER_X_METHOD, method);
        headerMap.put(HEADER_REQUEST_METHOD, method);
        headerMap.put(HEADER_CONTENT_MD5, contentMD5);
        headerMap.put(HEADER_DATE, date);
        headerMap.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);

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

    /**
     * This function generate the HMAC (Authorization value) using the path, message, method, date and authKey
     *
     * @param path     api path
     * @param strParam message
     * @param method   request method type e.g. POST
     * @param date     date in format @param authKey
     * @param authKey  secret key
     * @return hmac value
     */
    public static String generateHmacForContentTypeJson(String path, String strParam, String method, String date, String authKey) {
        String contentMD5 = generateContentMd5(strParam);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        String authString = String.format("%s\n%s\n%s\n%s\n%s", method, contentMD5, CONTENT_TYPE_JSON, date, path);
        String signature = calculateRFC2104HMAC(authString, authKey);

        return String.format("TKPD Authorization:%s", signature.trim());
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
     * @see #generateParamsNetwork(Context, TKPDMapParam, String, String)
     */
    @Deprecated
    public static Map<String, String> generateParams(Context context, Map<String, String> params, String deviceId, String userId) {
        params = MapNulRemover.removeNull(params);
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
     * @see #generateParamsNetwork(Context, String, String)
     */
    @Deprecated
    public static Map<String, String> generateParams(Context context, String deviceId, String userId) {
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


    public static TKPDMapParam<String, String> generateParamsNetwork(String userId, String deviceId, TKPDMapParam<String, String> params) {
        String hash = md5(userId + "~" + deviceId);
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        return params;
    }

    public static TKPDMapParam<String, Object> generateParamsNetwork2(Context context, TKPDMapParam<String, Object> params, String deviceId, String userId) {
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        return params;
    }

    public static TKPDMapParam<String, String> generateParamsNetwork(Context context, TKPDMapParam<String, String> params,
                                                                     String userId, String deviceId) {
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
//        params.put(PARAM_X_TKPD_USER_ID, userId);
        return params;
    }

    public static TKPDMapParam<String, String> generateParamsNetwork(Context context, String userId, String deviceId) {
        String hash = md5(userId + "~" + deviceId);
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
        //      params.put(PARAM_X_TKPD_USER_ID, userId);
        return params;
    }

    private static String calculateRFC2104HMAC(String authString, String authKey) {
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
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
