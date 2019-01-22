package com.tokopedia.otp.cotp.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 11/30/17.
 */

public class VerificationViewModel implements Parcelable {

    private String phoneNumber;
    private String email;
    private int otpType;
    private String imageUrl;
    private String appScreen;
    private String mode;
    private int iconResId;
    private String message;
    private boolean canUseOtherMethod;

    /**
     * Passing Model for default verification page (without choose otp method).
     *
     * @param otpType
     * @param mode
     * @param iconResId
     * @param message
     * @param appScreen
     * @param canUseOtherMethod
     */
    public VerificationViewModel(String phoneNumber, String email,
                                 int otpType, String mode, int iconResId,
                                 String message, String appScreen, boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.mode = mode;
        this.iconResId = iconResId;
        this.message = message;
        this.appScreen = appScreen;
        this.imageUrl = "";
        this.canUseOtherMethod = canUseOtherMethod;
    }

    /**
     * Passing model after choosing method from otp method list.
     *
     * @param mode
     * @param imageUrl
     * @param message
     * @param appScreen
     * @param canUseOtherMethod
     */
    public VerificationViewModel(String phoneNumber, String email, int otpType,
                                 String mode, String imageUrl, String message,
                                 String appScreen, boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.mode = mode;
        this.iconResId = 0;
        this.message = message;
        this.appScreen = appScreen;
        this.imageUrl = imageUrl;
        this.canUseOtherMethod = canUseOtherMethod;
    }

    protected VerificationViewModel(Parcel in) {
        phoneNumber = in.readString();
        email = in.readString();
        otpType = in.readInt();
        imageUrl = in.readString();
        appScreen = in.readString();
        mode = in.readString();
        iconResId = in.readInt();
        message = in.readString();
        canUseOtherMethod = in.readByte() != 0;
    }

    public static final Creator<VerificationViewModel> CREATOR = new Creator<VerificationViewModel>() {
        @Override
        public VerificationViewModel createFromParcel(Parcel in) {
            return new VerificationViewModel(in);
        }

        @Override
        public VerificationViewModel[] newArray(int size) {
            return new VerificationViewModel[size];
        }
    };

    public String getAppScreen() {
        return appScreen;
    }

    public String getType() {
        return mode;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public String getMode() {
        return mode;
    }

    public boolean canUseOtherMethod() {
        return canUseOtherMethod;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOtpType(int otpType) {
        this.otpType = otpType;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAppScreen(String appScreen) {
        this.appScreen = appScreen;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCanUseOtherMethod(boolean canUseOtherMethod) {
        this.canUseOtherMethod = canUseOtherMethod;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeInt(otpType);
        dest.writeString(imageUrl);
        dest.writeString(appScreen);
        dest.writeString(mode);
        dest.writeInt(iconResId);
        dest.writeString(message);
        dest.writeByte((byte) (canUseOtherMethod ? 1 : 0));
    }
}
