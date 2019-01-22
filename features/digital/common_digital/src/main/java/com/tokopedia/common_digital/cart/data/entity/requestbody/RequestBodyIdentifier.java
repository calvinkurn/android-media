package com.tokopedia.common_digital.cart.data.entity.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 27/08/18.
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
