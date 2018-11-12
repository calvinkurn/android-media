package com.tokopedia.useridentification.view.viewmodel;

import android.os.Parcel;

import com.tokopedia.abstraction.base.view.model.StepperModel;

/**
 * @author by alvinatin on 12/11/18.
 */

public class UserIdentificationStepperModel implements StepperModel {

    private boolean isKtpValid;
    private boolean isFaceValid;

    public boolean isKtpValid() {
        return isKtpValid;
    }

    public void setKtpValid(boolean ktpValid) {
        isKtpValid = ktpValid;
    }

    public boolean isFaceValid() {
        return isFaceValid;
    }

    public void setFaceValid(boolean faceValid) {
        isFaceValid = faceValid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isKtpValid ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFaceValid ? (byte) 1 : (byte) 0);
    }

    public UserIdentificationStepperModel() {
    }

    protected UserIdentificationStepperModel(Parcel in) {
        this.isKtpValid = in.readByte() != 0;
        this.isFaceValid = in.readByte() != 0;
    }

    public static final Creator<UserIdentificationStepperModel> CREATOR = new
            Creator<UserIdentificationStepperModel>() {
        @Override
        public UserIdentificationStepperModel createFromParcel(Parcel source) {
            return new UserIdentificationStepperModel(source);
        }

        @Override
        public UserIdentificationStepperModel[] newArray(int size) {
            return new UserIdentificationStepperModel[size];
        }
    };
}
