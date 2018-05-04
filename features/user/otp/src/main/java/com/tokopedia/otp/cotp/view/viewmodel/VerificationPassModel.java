package com.tokopedia.otp.cotp.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 12/28/17.
 */

public class VerificationPassModel implements Parcelable{

    private String phoneNumber;
    private String email;
    private int otpType;
    private boolean canUseOtherMethod;

    public VerificationPassModel(String phoneNumber, int otpType, boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = "";
        this.otpType = otpType;
        this.canUseOtherMethod = canUseOtherMethod;
    }

    public VerificationPassModel(String phoneNumber, String email, int otpType,
                                 boolean canUseOtherMethod) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.otpType = otpType;
        this.canUseOtherMethod = canUseOtherMethod;

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


    protected VerificationPassModel(Parcel in) {
        phoneNumber = in.readString();
        email = in.readString();
        otpType = in.readInt();
        canUseOtherMethod = in.readByte() != 0;
    }

    public static final Creator<VerificationPassModel> CREATOR = new Creator<VerificationPassModel>() {
        @Override
        public VerificationPassModel createFromParcel(Parcel in) {
            return new VerificationPassModel(in);
        }

        @Override
        public VerificationPassModel[] newArray(int size) {
            return new VerificationPassModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeInt(otpType);
        dest.writeByte((byte) (canUseOtherMethod ? 1 : 0));
    }
}
