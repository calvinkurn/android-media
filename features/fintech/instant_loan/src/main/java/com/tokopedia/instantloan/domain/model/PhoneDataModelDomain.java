package com.tokopedia.instantloan.domain.model;

/**
 * Created by lavekush on 21/03/18.
 */

public class PhoneDataModelDomain {
    private int mobileDeviceId;

    public PhoneDataModelDomain(int mobileDeviceId) {
        this.mobileDeviceId = mobileDeviceId;
    }

    public int getMobileDeviceId() {
        return mobileDeviceId;
    }

    public void setMobileDeviceId(int mobileDeviceId) {
        this.mobileDeviceId = mobileDeviceId;
    }

    @Override
    public String toString() {
        return "PhoneDataModelDomain{" +
                "mobileDeviceId=" + mobileDeviceId +
                '}';
    }
}
