package com.tokopedia.graphql;

import android.text.TextUtils;

import com.tokopedia.graphql.util.CacheHelper;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.graphql.data.model.GraphqlRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * For generating hashcode primarily use as a key of cache object
 */
public class FingerprintManager {

    private UserSession mUserSession;

    public FingerprintManager(UserSession userSession) {
        this.mUserSession = userSession;
    }

    public String generateFingerPrint(String key, boolean isSessionIncluded) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");

            if (mUserSession != null && isSessionIncluded) {
                digest.update((key + mUserSession.getUserId()).getBytes());
            } else {
                digest.update(key.getBytes());
            }

            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key.replace(" ", "");  //If exception caught then it will return plain string without spaces
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

    /**
     * To generate list of hash for incoming request.
     * @param requests
     * @return
     */
    public static String getQueryDigest(List<GraphqlRequest> requests) {
        if (requests == null) {
            return "";
        }

        StringBuilder digestBuilder = new StringBuilder();
        for (GraphqlRequest request : requests) {
            if (request == null || request.isNoCache() || TextUtils.isEmpty(request.getQuery())) {
                continue;
            }

            String oName;
            if (TextUtils.isEmpty(request.getOperationName())) {
                oName = CacheHelper.getQueryName(request.getQuery());
            } else {
                oName = request.getOperationName();
            }

            digestBuilder.append(oName)
                    .append('-')
                    .append(request.getMd5())
                    .append(',');
        }

        return digestBuilder.toString();
    }

    public String generateFingerPrint(String key) {
        return generateFingerPrint(key, false);
    }
}
