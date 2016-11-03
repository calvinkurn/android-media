package com.tokopedia.tkpd.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ricoharisin on 3/1/16.
 */

@Parcel
public class LoginBypassSuccessModel {
    @SerializedName("is_register_device")
    int isRegisterDevice;

    public LoginBypassSuccessModel() {

    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
    }
}
