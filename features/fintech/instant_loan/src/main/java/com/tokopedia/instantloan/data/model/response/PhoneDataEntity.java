package com.tokopedia.instantloan.data.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lavekush on 30/03/18.
 */

public class PhoneDataEntity {
    @SerializedName("mobile_device_id")
    private int mobileDeviceId;

    public int getMobileDeviceId() {
        return mobileDeviceId;
    }

    public void setMobileDeviceId(int mobileDeviceId) {
        this.mobileDeviceId = mobileDeviceId;
    }

    @Override
    public String toString() {
        return "PhoneDataEntity{" +
                "mobileDeviceId=" + mobileDeviceId +
                '}';
    }
}
