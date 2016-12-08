package com.tokopedia.core.notification.model;

/**
 * @author  by alvarisi on 12/8/16.
 */

public class FcmClientIdUpdate {
    private String mOldClientId;
    private String mNewClientId;
    private String mOsType;

    public String getOldClientId() {
        return mOldClientId;
    }

    public void setOldClientId(String oldClientId) {
        mOldClientId = oldClientId;
    }

    public String getNewClientId() {
        return mNewClientId;
    }

    public void setNewClientId(String newClientId) {
        mNewClientId = newClientId;
    }

    public String getOsType() {
        return mOsType;
    }

    public void setOsType(String osType) {
        mOsType = osType;
    }
}
