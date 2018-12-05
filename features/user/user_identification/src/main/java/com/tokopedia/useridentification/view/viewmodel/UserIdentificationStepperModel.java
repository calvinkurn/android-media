package com.tokopedia.useridentification.view.viewmodel;

import android.os.Parcel;

import com.tokopedia.abstraction.base.view.model.StepperModel;

/**
 * @author by alvinatin on 12/11/18.
 */

public class UserIdentificationStepperModel implements StepperModel {

    private String ktpFile;
    private String faceFile;

    public String getKtpFile() {
        return ktpFile;
    }

    public void setKtpFile(String ktpFile) {
        this.ktpFile = ktpFile;
    }

    public String getFaceFile() {
        return faceFile;
    }

    public void setFaceFile(String faceFile) {
        this.faceFile = faceFile;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.ktpFile);
        dest.writeSerializable(this.faceFile);
    }

    public UserIdentificationStepperModel() {
    }

    protected UserIdentificationStepperModel(Parcel in) {
        this.ktpFile = (String) in.readSerializable();
        this.faceFile = (String) in.readSerializable();
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
