package com.tokopedia.otp.cotp.view.viewmodel;

/**
 * @author by nisie on 12/28/17.
 */

public class VerificationPassModel {

    private String phoneNumber;
    private String email;
    private int otpType;
    private boolean canUseOtherMethod;
    private boolean showChooseMethod;

    public VerificationPassModel(String phoneNumber, int otpType, boolean canUseOtherMethod,
                                 boolean showChooseMethod) {
        this.phoneNumber = phoneNumber;
        this.email = "";
        this.otpType = otpType;
        this.canUseOtherMethod = canUseOtherMethod;
        this.showChooseMethod = showChooseMethod;
    }

    public VerificationPassModel(String phoneNumber, String email, int otpType,
                                 boolean canUseOtherMethod, boolean showChooseMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.canUseOtherMethod = canUseOtherMethod;
        this.showChooseMethod = showChooseMethod;

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public int getOtpType() {
        return otpType;
    }

    public boolean canUseOtherMethod() {
        return canUseOtherMethod;
    }

    public boolean isShowChooseMethod() {
        return showChooseMethod;
    }
}
