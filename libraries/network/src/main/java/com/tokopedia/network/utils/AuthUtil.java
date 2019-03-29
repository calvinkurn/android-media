package com.tokopedia.network.utils;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Base64;

import com.tokopedia.config.GlobalConfig;

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

import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.data.model.FingerprintModel;
import com.tokopedia.user.session.UserSession;
import com.google.gson.Gson;
import com.tokopedia.user.session.UserSessionInterface;

import android.content.Context;

/**
 * @author Angga.Prasetiyo on 25/11/2015.
 *         Modified by kulomady add method without params
 */
public class AuthUtil {
    public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String MAC_ALGORITHM = "HmacSHA1";
    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_X_METHOD = "X-Method";
    public static final String HEADER_REQUEST_METHOD = "Request-Method";
    public static final String HEADER_CONTENT_MD5 = "Content-MD5";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_PARAM_BEARER = "Bearer";
    public static final String HEADER_USER_ID = "X-User-ID";
    public static final String HEADER_X_TKPD_USER_ID = "X-Tkpd-UserId";
    public static final String HEADER_TKPD_USER_ID = "Tkpd-UserId";
    public static final String HEADER_DEVICE = "X-Device";
    public static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    public static final String HEADER_X_TKPD_APP_NAME = "X-Tkpd-App-Name";
    public static final String HEADER_X_TKPD_APP_VERSION = "X-Tkpd-App-Version";
    private static final String HEADER_CACHE_CONTROL = "cache-control";
    private static final String HEADER_PATH = "X-Tkpd-Path";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String HEADER_X_MSISDN = "x-msisdn";
    private static final String HEADER_OS_TYPE = "os-type";
    public static final String HEADER_SESSION_ID = "Tkpd-SessionId";
    public static final String HEADER_OS_VERSION = "os_version";

    private static final String KEY_FINGERPRINT_DATA = "Fingerprint-Data";
    private static final String KEY_FINGERPRINT_HASH = "Fingerprint-Hash";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    public static final String PARAM_OS_TYPE = "os_type";
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

    private static final String HEADER_HMAC_SIGNATURE_KEY = "TKPDROID AndroidApps:";
    private static final String HEADER_ACCOUNT_AUTHORIZATION = "Accounts-Authorization";

    /**
     * default key is KEY_WSV$
     */
    public static class KEY {
        private static final int[] RAW_KEY_WSV4 = new int[]{65, 107, 102, 105, 101, 119, 56, 51, 52, 50, 57, 56, 80, 79, 105, 110, 118};
        private static final int[] RAW_SCROOGE_KEY = new int[]{49, 50, 69, 56, 77, 105, 69, 55, 89, 69, 54, 86, 122, 115, 69, 80, 66, 80, 101, 77};
        private static final int[] RAW_ZEUS_KEY = new int[]{102, 100, 100, 98, 100, 56, 49, 101, 101, 52, 49, 49, 54, 98, 56, 99, 98, 55, 97, 52, 48, 56, 100, 55, 102, 98, 102, 98, 57, 99, 49, 55};
        private static final int[] RAW_NOTP_KEY = new int[]{110, 117, 108, 97, 121, 117, 107, 97, 119, 111, 106, 117};
        private static final int[] RAW_ALIYUN_SECRET_KEY = new int[]{101, 74, 76, 86, 51, 80, 74, 67, 67, 69, 110, 55, 115, 113, 102, 53, 118, 86, 114, 73, 122, 97, 69, 83, 84, 102, 115, 78, 100, 109};
        private static final int[] RAW_ALIYUN_ACCESS_KEY_ID = new int[]{76, 84, 65, 73, 85, 101, 69, 87, 83, 118, 105, 97, 49, 75, 107, 87};
        private static final int[] RAW_BRANCHIO_KEY_ID = new int[]{107, 101, 121, 95, 108, 105, 118, 101, 95, 97, 98, 104, 72, 103, 73, 104, 49, 68, 81, 105, 117, 80, 120, 100, 66, 78, 103, 57, 69, 88, 101, 112, 100, 68, 117, 103, 119, 119, 107, 72, 114};
        private static final int[] RAW_INDI_API_KEY = new int[]{69, 69, 82, 73, 120, 119, 88, 70, 54, 52, 52, 99, 49, 69, 49, 84, 111, 53, 112, 117, 76, 56, 120, 78, 80, 53, 80, 118, 76, 72, 83, 118, 50, 52, 48, 80, 121, 78, 89, 102};

        public static final String KEY_WSV4_NEW = convert(RAW_KEY_WSV4);
        public static final String KEY_WSV4 = "web_service_v4";
        public static final String KEY_MOJITO = "mojito_api_v1";
        public static final String KEY_KEROPPI = "Keroppi";
        public static final String TOKO_CASH_HMAC = "CPAnAGpC3NIg7ZSj";
        public static String KEY_CREDIT_CARD_VAULT = convert(RAW_SCROOGE_KEY);
        public static String ZEUS_WHITELIST = convert(RAW_ZEUS_KEY);
        public static String KEY_NOTP = convert(RAW_NOTP_KEY);
        public static String KEY_BRANCHIO = convert(RAW_BRANCHIO_KEY_ID);
        public static String ALIYUN_SECRET_KEY = convert(RAW_ALIYUN_SECRET_KEY);
        public static String ALIYUN_ACCESS_KEY_ID = convert(RAW_ALIYUN_ACCESS_KEY_ID);
        public static String INDI_API_KEY = convert(RAW_INDI_API_KEY);
    }


    public static Map<String, String> getDefaultHeaderMap(String path, String strParam, String method,
                                                          String contentType, String authKey,
                                                          String dateFormat, String userId,
                                                          UserSessionInterface session) {

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
        headerMap.put(HEADER_AUTHORIZATION, HEADER_HMAC_SIGNATURE_KEY + signature.trim());
        headerMap.remove(HEADER_ACCOUNT_AUTHORIZATION);
        headerMap.put(HEADER_ACCOUNT_AUTHORIZATION, String.format("%s %s", HEADER_PARAM_BEARER,
                session.getAccessToken()));
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(HEADER_OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));

        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateHeaders(
            String path, String strParam, String method, String authKey, String contentType,
            String userId, UserSessionInterface userSessionInterface
    ) {
        Map<String, String> finalHeader = getDefaultHeaderMap(
                path, strParam, method, contentType != null ? contentType : CONTENT_TYPE,
                authKey, DATE_FORMAT, userId, userSessionInterface
        );
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
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
        return finalHeader;
    }

    public static Map<String, String> generateHeadersWithPath(
            String path, String strParam, String method, String authKey, String contentType,
            String userId, UserSessionInterface userSessionInterface
    ) {
        Map<String, String> finalHeader = getDefaultHeaderMapOld(
                path, strParam, method, contentType != null ? contentType : CONTENT_TYPE,
                authKey, DATE_FORMAT, userId, userSessionInterface
        );
        finalHeader.put(HEADER_X_APP_VERSION, Integer.toString(GlobalConfig.VERSION_CODE));
        finalHeader.put(HEADER_PATH, path);
        return finalHeader;
    }

    public static Map<String, String> getDefaultHeaderMapOld(String path, String strParam, String method,
                                                          String contentType, String authKey,
                                                          String dateFormat, String userId,
                                                             UserSessionInterface session) {
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
        headerMap.remove(HEADER_ACCOUNT_AUTHORIZATION);
        headerMap.put(HEADER_ACCOUNT_AUTHORIZATION, String.format("%s %s", HEADER_PARAM_BEARER,
                session.getAccessToken()));
        headerMap.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        headerMap.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        headerMap.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put(HEADER_OS_VERSION, String.valueOf(Build.VERSION.SDK_INT));

        headerMap.put(HEADER_USER_ID, userId);
        headerMap.put(HEADER_DEVICE, "android-" + GlobalConfig.VERSION_NAME);
        return headerMap;
    }

    public static Map<String, String> generateParamsNetwork(String userId, String deviceId, Map<String, String> params) {
        String hash = md5(userId + "~" + deviceId);
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
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

    public static String generateContentMd5(String s) {
        return md5(s);
    }

    public static String generateDate(String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return simpleDateFormat.format(new Date());
    }

    private static String convert(int[] key) {
        String finalKey = "";
        for (int i : key) {
            finalKey = finalKey + Character.toString((char) i);
        }
        return finalKey;
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

    public static String getHeaderRequestReactNative(Context context) {
        UserSession session = new UserSession(context);
        Map<String, String> header = new HashMap<>();
        header.put(HEADER_SESSION_ID, session.getDeviceId());
        header.put(HEADER_TKPD_USER_ID, session.isLoggedIn() ? session.getUserId() : "0");
        header.put(HEADER_AUTHORIZATION, String.format("Bearer %s", session.getAccessToken()));
        header.remove(HEADER_ACCOUNT_AUTHORIZATION);
        header.put(HEADER_ACCOUNT_AUTHORIZATION, String.format("%s %s", HEADER_PARAM_BEARER, session.getAccessToken()));
        header.put(PARAM_OS_TYPE, "1");
        header.put(HEADER_DEVICE, String.format("android-%s", GlobalConfig.VERSION_NAME));
        header.put(HEADER_USER_ID, session.isLoggedIn() ? session.getUserId() : "0");
        header.put(HEADER_X_APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        header.put(HEADER_X_TKPD_USER_ID, session.isLoggedIn() ? session.getUserId() : "0");
        header.put(HEADER_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName());
        header.put(HEADER_X_TKPD_APP_VERSION, "android-" + GlobalConfig.VERSION_NAME);
        Gson gson = new Gson();
        return gson.toJson(header);
    }

    public static Map<String, String> getAuthHeaderReact(Context context,
                                                         String path,
                                                         String strParam,
                                                         String method,
                                                         String contentType) {
        UserSession session = new UserSession(context);
        Map<String, String> headers = getDefaultHeaderMap(path, strParam, method, contentType,
                KEY.KEY_WSV4_NEW, DATE_FORMAT, session.getUserId(), session);
        headers.put(HEADER_SESSION_ID, session.getDeviceId());
        headers.put(HEADER_TKPD_USER_ID, session.isLoggedIn() ? session.getUserId() : "0");
        headers.remove(HEADER_ACCOUNT_AUTHORIZATION);
        headers.put(HEADER_ACCOUNT_AUTHORIZATION, String.format("%s %s", HEADER_PARAM_BEARER, session.getAccessToken()));
        headers.put(PARAM_OS_TYPE, "1");
        headers.put(HEADER_DEVICE, String.format("android-%s", GlobalConfig.VERSION_NAME));
        headers.put(HEADER_X_TKPD_USER_ID, session.isLoggedIn() ? session.getUserId() : "0");

        if(context.getApplicationContext() instanceof NetworkRouter) {
            FingerprintModel fingerprintModel = ((NetworkRouter) context.getApplicationContext()).getFingerprintModel();
            String json = fingerprintModel.getFingerprintHash();
            headers.put(KEY_FINGERPRINT_HASH, AuthUtil.md5(json + "+" + session.getUserId()));
            headers.put(KEY_FINGERPRINT_DATA, json);
        }

        return headers;
    }
}
