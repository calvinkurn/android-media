package com.tokopedia.common_digital.cart.data.entity.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Varys Prasetyo on 14.09.17.
 */

public class RequestBodyAppsFlyer {

    @SerializedName("appsflyer_id")
    @Expose
    private String appsflyerId;
    @SerializedName("device_id")
    @Expose
    private String deviceId;

    public void setAppsflyerId(String appsflyerId) {
        this.appsflyerId = appsflyerId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
