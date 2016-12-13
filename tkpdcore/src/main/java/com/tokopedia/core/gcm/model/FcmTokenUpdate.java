package com.tokopedia.core.gcm.model;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class FcmTokenUpdate {
    private String mOldToken;
    private String mNewToken;
    private String mOsType;

    public String getOldToken() {
        return mOldToken;
    }

    public void setOldToken(String oldToken) {
        mOldToken = oldToken;
    }

    public String getNewToken() {
        return mNewToken;
    }

    public void setNewToken(String newToken) {
        mNewToken = newToken;
    }

    public String getOsType() {
        return mOsType;
    }

    public void setOsType(String osType) {
        mOsType = osType;
    }
}
