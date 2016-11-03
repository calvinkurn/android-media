package com.tokopedia.tkpd.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 23/11/2015.
 */
@Parcel
public class GetVerificationNumberForm {
    public static final String MSISDN_TAG = "msisdn";

    @SerializedName("show_dialog")
    @Expose
    int showDialog;
    public static final int DONT_SHOW_PHONE_NUMBER_DIALOG = 0;
    public static final int SHOW_PHONE_NUMBER_DIALOG = 1;

    @SerializedName("is_verified")
    @Expose
    int isVerified;
    public static final int PHONE_NUMBER_NOT_VERIFIED = 0;
    public static final int PHONE_NUMBER_VERIFIED = 1;

    @SerializedName("user_phone")
    String userPhone;

    public int getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(int showDialog) {
        this.showDialog = showDialog;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
