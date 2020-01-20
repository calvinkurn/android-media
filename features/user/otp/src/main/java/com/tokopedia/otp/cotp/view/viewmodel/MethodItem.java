package com.tokopedia.otp.cotp.view.viewmodel;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.otp.R;

/**
 * @author by nisie on 11/30/17.
 */

public class MethodItem implements Parcelable {

    private String modeName;
    private int iconResId;
    private String methodText;
    private String imageUrl;
    private String verificationText;
    private boolean usingPopUp;
    private String popUpHeader;
    private String popUpBody;
    private int numberOtpDigit;

    /**
     * Use this constructor for getting method item from ws.
     * @param mode should be aligned with
     * {@link com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase}'s mode list
     * @param imageUrl icon url
     * @param methodText description of method, used in {@link com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment}
     * @param verificationText description of method, used in
     * {@link com.tokopedia.otp.cotp.view.fragment.VerificationFragment}.
     * @param usingPopUp if there is interrupt pop up
     * @param popUpHeader title of pop up
     * @param popUpBody message of pop up. E.g : "Dengan verifikasi anda akan mengaktifkan ..."
     */
    public MethodItem(String mode, String imageUrl, String methodText, String verificationText,
                      boolean usingPopUp, String popUpHeader, String popUpBody, int numberOtpDigit) {
        this.modeName = mode;
        this.iconResId = 0;
        this.imageUrl = imageUrl;
        this.methodText = methodText;
        this.verificationText = verificationText;
        this.usingPopUp = usingPopUp;
        this.popUpHeader = popUpHeader;
        this.popUpBody = popUpBody;
        this.numberOtpDigit = numberOtpDigit;
    }

    /**
     * Use this constructor for using local list of verification method.
     * @param mode should be aligned with
     * {@link com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase}'s mode list
     * @param iconResId icon res id
     * @param methodText description of method, used in {@link com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment}
     * @param verificationText description of method, used in
     * {@link com.tokopedia.otp.cotp.view.fragment.VerificationFragment}.
     * @param usingPopUp if there is interrupt pop up
     * @param popUpHeader title of pop up
     * @param popUpBody message of pop up. E.g : "Dengan verifikasi anda akan mengaktifkan ..."
     */
    public MethodItem(String mode, int iconResId, String methodText, String verificationText,
                      boolean usingPopUp, String popUpHeader, String popUpBody, int numberOtpDigit) {
        this.modeName = mode;
        this.iconResId = iconResId;
        this.imageUrl = "";
        this.methodText = methodText;
        this.verificationText = verificationText;
        this.usingPopUp = usingPopUp;
        this.popUpHeader = popUpHeader;
        this.popUpBody = popUpBody;
        this.numberOtpDigit = numberOtpDigit;
    }

    /**
     * Use this constructor for using local list of verification method without pop up header.
     * @param mode should be aligned with
     * {@link com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase}'s mode list
     * @param iconResId icon res id
     * @param methodText description of method, used in {@link com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment}
     * @param verificationText description of method, used in
     * {@link com.tokopedia.otp.cotp.view.fragment.VerificationFragment}.
     */
    public MethodItem(String mode, int iconResId, String methodText, String verificationText, int numberOtpDigit) {
        this.modeName = mode;
        this.iconResId = iconResId;
        this.imageUrl = "";
        this.methodText = methodText;
        this.verificationText = verificationText;
        this.usingPopUp = false;
        this.popUpHeader = "";
        this.popUpBody = "";
        this.numberOtpDigit = numberOtpDigit;
    }

    public String getModeName() {
        return modeName;
    }

    public String getMethodText() {
        return methodText;
    }

    public static String getSmsMethodText(String phoneNumber, Context context) {
        return context.getString(R.string.verification_sms_to) + " " +
                phoneNumber;
    }

    public static String getCallMethodText(String phoneNumber, Context context) {
        return context.getString(R.string.verification_call_to) + " " +
                phoneNumber;
    }

    public static String getMaskedPhoneNumber(String phone) {
        phone = phone.substring(phone.length() - 4);
        return String.format(("**** - **** - %s"), phone);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVerificationText() {
        return verificationText;
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

    public int getIconResId() {
        return iconResId;
    }

    public int getNumberOtpDigit() {
        return numberOtpDigit;
    }

    protected MethodItem(Parcel in) {
        modeName = in.readString();
        iconResId = in.readInt();
        methodText = in.readString();
        imageUrl = in.readString();
        verificationText = in.readString();
        usingPopUp = in.readByte() != 0;
        popUpHeader = in.readString();
        popUpBody = in.readString();
        numberOtpDigit = in.readInt();
    }

    public static final Creator<MethodItem> CREATOR = new Creator<MethodItem>() {
        @Override
        public MethodItem createFromParcel(Parcel in) {
            return new MethodItem(in);
        }

        @Override
        public MethodItem[] newArray(int size) {
            return new MethodItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modeName);
        dest.writeInt(iconResId);
        dest.writeString(methodText);
        dest.writeString(imageUrl);
        dest.writeString(verificationText);
        dest.writeByte((byte) (usingPopUp ? 1 : 0));
        dest.writeString(popUpHeader);
        dest.writeString(popUpBody);
        dest.writeInt(numberOtpDigit);
    }
}
