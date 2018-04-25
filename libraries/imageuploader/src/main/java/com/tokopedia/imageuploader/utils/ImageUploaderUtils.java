package com.tokopedia.imageuploader.utils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageUploaderUtils {
    private UserSession userSession;
    private String deviceTime;

    public ImageUploaderUtils(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getDeviceTime() {
        if (deviceTime == null || deviceTime.length() > 0) {
            deviceTime = String.valueOf((System.currentTimeMillis() / 1000L));
        }
        return deviceTime;
    }

    public String getHash() {
        return md5(userSession.getUserId() + "~" + userSession.getDeviceId());
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
}
