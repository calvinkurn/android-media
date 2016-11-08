package com.tokopedia.core.network.v4;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.tkpd.library.kirisame.Kirisame;
import com.tkpd.library.kirisame.network.entity.NetError;

/**
 * Created by ricoharisin on 8/24/2015.
 */
@Deprecated
public class NetworkHandlerAuth extends NetworkHandler {

    private String deviceId;
    private String date;
    private String userId = "";

    public NetworkHandlerAuth(Context context, String url) {
        super(context, url);
        deviceId = GCMHandler.getRegistrationId(context);
        date = generateDate();
        userId = SessionHandler.getLoginID(context);
        setHMACHeader();
        setTokopediaDefaultParam();
    }

    public void setData(){
        deviceId = GCMHandler.getRegistrationId(context);
        date = generateDate();
        userId = SessionHandler.getLoginID(context);
        setHMACHeader();
        setTokopediaDefaultParam();
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
        addHeader("method", "GET");
        String md5 = "";
        addHeader("Content-MD5", md5 += md5(getContent().toString()));
        addHeader("Content-Type", "application/x-www-form-urlencoded");
        addHeader("Date", date); //TODO Datenya gak sesuai kebutuhan
        addHeader("X-Tkpd-Path", generateTkpdPath());
        String StringToSign = generateTkpdAuthString("GET", md5, "application/x-www-form-urlencoded", date, generateTkpdPath());
        String signature = generateHMACSignature(StringToSign);// generateTkpdAuthString()
        addHeader("Authorization", "TKPD Tokopedia:" + signature);
        addHeader("X-Tkpd-Authorization", "TKPD Tokopedia:" + signature);
    }

    private String generateHMACSignature(String auth) {
        ///auth = "POST\\n1234567890asdfghjkl\\napplication/x-www-form-urlencoded\\nThu, 27 Aug 2015 17:59:05 +0700\\n/v4/home/get_hotlist.pl\"";
        String hmacSignature = "";
        String key = "web_service_v4";
        Kirisame.print("IN " + auth);
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(auth.getBytes("UTF-8"));
//            String hexHmac = bytesToHex(rawHmac).toLowerCase(); TODO use this instead of rawHmac if want to use hexstring


            hmacSignature = Base64.encodeToString(rawHmac, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("Network Handler", hmacSignature);
        return hmacSignature;
    }

    String generateTkpdAuthString(String  Request_Method, String Content_MD5, String Content_Type,
        String Date, String X_Tkpd_Path  ){
        String temp = "";
        temp += Request_Method + breakString +
                Content_MD5 + breakString +
                Content_Type + breakString +
                Date + breakString +
                X_Tkpd_Path;
        return temp;
    }

    final static String breakString = "\n";

    private void setTokopediaDefaultParam() {
        addParam("user_id", userId);// TODO Tambahin apa aja yg dirasa butuh
        addParam("device_id", deviceId);
        addParam("hash", md5(userId + "~" + deviceId));
    }


    private String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();

            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b & 0xff));
            }

            //Create hex String
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String generateDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ", Locale.ENGLISH);
        return dateFormat.format(new Date());
    }

    private String generateTkpdPath() {
        Uri uri = Uri.parse(url);
        String TkpdPath = "";
        for (int i = 0; i < uri.getPathSegments().size(); i++) {
            Log.i("Network Handler", TkpdPath + " " + i);
            TkpdPath = TkpdPath + "/" + uri.getPathSegments().get(i);
        }

        return TkpdPath;
    }

}