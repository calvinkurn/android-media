package com.tokopedia.core.network.v4;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by m.normansyah on 03/11/2015.
 */

@Deprecated
public class NetworkConfigUtil {
    public static String md5(String s) {
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

    static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


    public static String generateHMACSignature(String auth) {
        String hmacSignature = "";
        try {
            SecretKeySpec keySpec = new SecretKeySpec(NetworkConfig.key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(auth.getBytes("UTF-8"));
            hmacSignature = Base64.encodeToString(rawHmac, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("Network Handler", hmacSignature);
        return hmacSignature.trim();
    }

    public static String generateTkpdAuthString(String  Request_Method, String Content_MD5, String Content_Type,
                                  String Date, String X_Tkpd_Path  ){
        String temp = "";
        temp += Request_Method + breakString +
                Content_MD5 + breakString +
                Content_Type + breakString +
                Date + breakString +
                X_Tkpd_Path;
        return temp;
    }

    public static String generateTkpdPath(String url) {
        Uri uri = Uri.parse(url);
        String TkpdPath = "";
        for (int i = 0; i < uri.getPathSegments().size(); i++) {
            Log.i("Network Handler", TkpdPath + " " + i);
            TkpdPath = TkpdPath + "/" + uri.getPathSegments().get(i);
        }

        return TkpdPath;
    }

    public final static String breakString = "\n";
}
