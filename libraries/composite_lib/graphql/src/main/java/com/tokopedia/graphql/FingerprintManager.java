package com.tokopedia.graphql;

import com.tokopedia.user.session.UserSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
