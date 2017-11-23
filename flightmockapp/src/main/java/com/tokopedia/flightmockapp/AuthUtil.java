package com.tokopedia.flightmockapp;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Base64;

import com.tokopedia.abstraction.utils.GlobalConfig;

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
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_USER_ID = "X-User-ID";
    private static final String HEADER_X_TKPD_USER_ID = "X-Tkpd-UserId";
    private static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    public static final String HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name";
    private static final String HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version";
    private static final String HEADER_CACHE_CONTROL = "cache-control";
    private static final String HEADER_PATH = "x-tkpd-path";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String HEADER_X_MSISDN = "x-msisdn";

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

    public static Map<String, String> getDefaultHeaderMap(String path, String strParam, String method,
                                                          String contentType, String authKey, String dateFormat) {
        String date = generateDate(dateFormat);
        String contentMD5 = generateContentMd5(strParam);
        String userId = "523123";

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
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
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
}
