package com.tokopedia.core.network.retrofit.utils;

import android.content.Context;
import android.util.Base64;

import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.GlobalConfig;
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
 * Created by stevenfredian on 5/25/16.
 */
public class AccountsUtil extends AuthUtil{
    private static final String TAG = AuthUtil.class.getSimpleName();

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
    private static final String HEADER_DEVICE = "X-Device";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    /**
     * default key is KEY_WSV$
     */
    public static class KEY {
        public static String KEY_WSV4 = "web_service_v4";
        public static String KEY_MOJITO = "mojito_api_v1";
    }


    public static HeaderParam getHeaderParam(Context context, String path, Map<String, String> params, String method) {

        String deviceId = GCMHandler.getRegistrationId(context);
        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        String hash = md5(userId + "~" + deviceId);

        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_DEVICE_ID, deviceId);
        params.put(PARAM_HASH, hash);
        params.put(PARAM_OS_TYPE, "1");
        params.put(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));

        String date = generateDate();
        String contentMD5 = generateContentMd5(params.toString());

        String authString = method + "\n" + contentMD5 + "\n" + CONTENT_TYPE + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, KEY.KEY_WSV4);

        Map<String, String> finalHeader = new HashMap<>();
        finalHeader.put(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        finalHeader.put(HEADER_X_METHOD, method);
        finalHeader.put(HEADER_REQUEST_METHOD, method);
        finalHeader.put(HEADER_CONTENT_MD5, contentMD5);
        finalHeader.put(HEADER_DATE, date);
        finalHeader.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());

        HeaderParam headerParam = new HeaderParam();
        headerParam.setHeaders(finalHeader);
        headerParam.setParams(params);

        return headerParam;
    }

    public static Map<String, String> generateHeaders(String path, Map<String, String> params, String method, String authKey) {
        String date = generateDate();
        String contentMD5 = generateContentMd5(params.toString());

        String authString = method + "\n" + contentMD5 + "\n" + CONTENT_TYPE + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        Map<String, String> finalHeader = new HashMap<>();
        finalHeader.put(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        finalHeader.put(HEADER_X_METHOD, method);
        finalHeader.put(HEADER_REQUEST_METHOD, method);
        finalHeader.put(HEADER_CONTENT_MD5, contentMD5);
        finalHeader.put(HEADER_DATE, date);
        finalHeader.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());

        return finalHeader;
    }

    public static Map<String, String> generateHeaders(String path, String method, String authKey) {
        String date = generateDate();
        String contentMD5 = generateContentMd5("");

        String authString = method + "\n" + contentMD5 + "\n" + CONTENT_TYPE_JSON + "\n" + date + "\n" + path;
        String signature = calculateRFC2104HMAC(authString, authKey);

        UserSession userSession = new UserSession(CoreNetworkApplication.getAppContext());
        String userId = userSession.getUserId();
        Map<String, String> finalHeader = new HashMap<>();
        finalHeader.put(HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
        finalHeader.put(HEADER_X_METHOD, method);
        finalHeader.put(HEADER_REQUEST_METHOD, method);
        finalHeader.put(HEADER_DATE, date);
        finalHeader.put(HEADER_CONTENT_MD5, contentMD5);
        finalHeader.put(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature.trim());
        finalHeader.put(HEADER_USER_ID, userId);
        finalHeader.put(HEADER_DEVICE, "android-"+ GlobalConfig.VERSION_NAME);

        return finalHeader;
    }

    public static Map<String, String> generateParams(Context context, Map<String, String> params) {

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

    public static Map<String, String> generateParams(Context context, Map<String, String> params, String userId) {

        String deviceId = GCMHandler.getRegistrationId(context);
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

    private static String generateContentMd5(String s) {
        return md5(s);
    }

    private static String generateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(new Date());
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

}
