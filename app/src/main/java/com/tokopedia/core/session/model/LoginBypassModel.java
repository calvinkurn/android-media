package com.tokopedia.core.session.model;

import org.parceler.Parcel;

/**
 * Created by ricoharisin on 2/24/16.
 */
@Parcel
public class LoginBypassModel {
    String userID;
    String deviceID;

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
