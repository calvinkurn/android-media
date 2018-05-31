package com.tokopedia.instantloan.view.model;

/**
 * Created by lavekush on 20/03/18.
 */

public class PhoneDataViewModel {
    private int mobileDeviceId;

    public PhoneDataViewModel(int mobileDeviceId) {
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
        return "PhoneDataViewModel{" +
                "mobileDeviceId=" + mobileDeviceId +
                '}';
    }
}
