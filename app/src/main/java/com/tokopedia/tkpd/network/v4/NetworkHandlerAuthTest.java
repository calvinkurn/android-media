package com.tokopedia.tkpd.network.v4;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.tokopedia.tkpd.gcm.GCMHandler;
import com.tokopedia.tkpd.util.SessionHandler;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import nope.yuuji.kirisame.network.entity.NetError;

/**
 * Created by Angga.Prasetiyo on 04/11/2015.
 */
public class NetworkHandlerAuthTest extends NetworkHandler {
    private static final String TAG = NetworkHandlerAuthTest.class.getSimpleName();

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String KEY_HMAC = "web_service_v4";
    private static final String MAC_ALGORITHM = "HmacSHA1";
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_X_METHOD = "X-Method";
    private static final String HEADER_REQUEST_METHOD = "Request-Method";
    private static final String HEADER_CONTENT_MD5 = "Content-MD5";
    private static final String HEADER_DATE = "Date";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_DEVICE_ID = "device_id";
    private static final String PARAM_HASH = "hash";
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String PARAM_TIMESTAMP = "device_time";

    private final String deviceId;
    private final String hash;
    private final String userId;
    private final String methodRequest;
    private final String pathURL;

    private String date;
    private String contentMD5;
    private String signature;


    public NetworkHandlerAuthTest(Context context, String url, int method) {
        super(context, url);
        setMethod(method);
        pathURL = generateTkpdPath();
        methodRequest = initialMethodRequest(method);
        deviceId = GCMHandler.getRegistrationId(context);
        userId = SessionHandler.getLoginID(context);
//        deviceId = "b";
//        userId = "5245263";
        hash = md5(userId + "~" + deviceId);
        setTokopediaDefaultParam();
    }

    private String initialMethodRequest(int method) {
        switch (method) {
            case Request.Method.POST:
                return "POST";
            case Request.Method.GET:
                return "GET";
            default:
                return "POST";
        }
    }

    private void generateAllAuthRequirement() {
        this.date = generateDate();
        this.contentMD5 = generateContentMd5();
        this.signature = calculateRFC2104HMAC(generateTkpdAuthString());
        header.clear();
        setHMACHeader();
        Log.d(TAG, "URL REQUEST = " + url);
        Log.d(TAG, "HEADER REQUEST = " + header.toString());
        Log.d(TAG, "PARAM REQUEST = " + param.toString());
    }

    private String generateContentMd5() {
        return md5(param.toString());
    }

    @Override
    public void onRequestResponse(String s) {
        super.onRequestResponse(s);
    }

    @Override
    public void onRequestError(NetError netError, int i) {
        super.onRequestError(netError, i);
    }

    private void setHMACHeader() {
        addHeader(HEADER_CONTENT_TYPE, CONTENT_TYPE);
        addHeader(HEADER_X_METHOD, methodRequest);
        addHeader(HEADER_REQUEST_METHOD, methodRequest);
        addHeader(HEADER_CONTENT_MD5, contentMD5);
        addHeader(HEADER_DATE, date);
        addHeader(HEADER_AUTHORIZATION, "TKPD Tokopedia:" + signature);
    }

    public static String calculateRFC2104HMAC(String data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY_HMAC.getBytes(), MAC_ALGORITHM);
            Mac mac = Mac.getInstance(MAC_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            String asB64 = Base64.encodeToString(rawHmac, Base64.DEFAULT);
            System.out.println(asB64);
            return asB64;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "";
        }
    }


    private String generateTkpdAuthString() {
        return methodRequest + "\n" + contentMD5 + "\n" + CONTENT_TYPE + "\n" + date + "\n" + pathURL;
    }

    private void setTokopediaDefaultParam() {
        addParam(PARAM_USER_ID, userId);
        addParam(PARAM_DEVICE_ID, deviceId);
        addParam(PARAM_HASH, hash);
        addParam(PARAM_OS_TYPE, "1");
        addParam(PARAM_TIMESTAMP, String.valueOf((new Date().getTime()) / 1000));
    }

    private String md5(String s) {
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

    private String generateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    private String generateTkpdPath() {
        Uri uri = Uri.parse(url);
        StringBuilder builder = new StringBuilder();
        List<String> pathSegments = uri.getPathSegments();
        for (String segment : pathSegments) {
            builder.append("/").append(segment);
        }
        return builder.toString();
    }

    @Override
    public void commit() {
        generateAllAuthRequirement();
        super.commit();
    }

    public void commit(Object tag) {
        generateAllAuthRequirement();
        super.commit();
    }

    public static class Builder {
        protected String url;
        protected Context context;
        private int method = 0;

        private Builder() {
        }

        public static Builder aNetworkHandlerAuthTest() {
            return new Builder();
        }

        public Builder setMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder but() {
            return aNetworkHandlerAuthTest().setMethod(method).setUrl(url).setContext(context);
        }

        public NetworkHandlerAuthTest build() {
            return new NetworkHandlerAuthTest(context, url, method);
        }
    }
}
