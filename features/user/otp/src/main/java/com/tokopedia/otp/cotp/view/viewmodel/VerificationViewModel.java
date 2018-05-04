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
    private boolean usingPopUp;
    private String popUpHeader;
    private String popUpBody;
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
        this.usingPopUp = false;
        this.popUpHeader = "";
        this.popUpBody = "";
        this.canUseOtherMethod = canUseOtherMethod;
    }

    /**
     * Passing model after choosing method from otp method list.
     *
     * @param mode
     * @param imageUrl
     * @param message
     * @param appScreen
     * @param usingPopUp
     * @param popUpHeader
     * @param popUpBody
     * @param canUseOtherMethod
     */
    public VerificationViewModel(String phoneNumber, String email, int otpType,
                                 String mode, String imageUrl, String message,
                                 String appScreen, boolean usingPopUp,
                                 String popUpHeader, String popUpBody, boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.mode = mode;
        this.iconResId = 0;
        this.message = message;
        this.appScreen = appScreen;
        this.imageUrl = imageUrl;
        this.usingPopUp = usingPopUp;
        this.popUpHeader = popUpHeader;
        this.popUpBody = popUpBody;
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
        usingPopUp = in.readByte() != 0;
        popUpHeader = in.readString();
        popUpBody = in.readString();
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

    public boolean isUsingPopUp() {
        return usingPopUp;
    }

    public String getPopUpHeader() {
        return popUpHeader;
    }

    public String getPopUpBody() {
        return popUpBody;
    }

    public boolean canUseOtherMethod() {
        return canUseOtherMethod;
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
        dest.writeByte((byte) (usingPopUp ? 1 : 0));
        dest.writeString(popUpHeader);
        dest.writeString(popUpBody);
        dest.writeByte((byte) (canUseOtherMethod ? 1 : 0));
    }
}
