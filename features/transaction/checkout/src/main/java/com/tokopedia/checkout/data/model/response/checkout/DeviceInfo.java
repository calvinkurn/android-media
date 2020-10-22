package com.tokopedia.checkout.data.model.response.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class DeviceInfo {
    @SerializedName("device_name")
    @Expose
    private String deviceName;
    @SerializedName("device_version")
    @Expose
    private String deviceVersion;

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }
}
