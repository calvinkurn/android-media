package com.tokopedia.digital.utils.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 6/19/17.
 */

public class RequestBodyIdentifier {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("os_type")
    @Expose
    private String osType;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }
}
