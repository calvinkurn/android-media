package com.tokopedia.useridentification.view.viewmodel;

import android.os.Parcel;

import com.tokopedia.abstraction.base.view.model.StepperModel;

import java.io.File;

/**
 * @author by alvinatin on 12/11/18.
 */

public class UserIdentificationStepperModel implements StepperModel {

    private File ktpFile;
    private File faceFile;

    public File getKtpFile() {
        return ktpFile;
    }

    public void setKtpFile(File ktpFile) {
        this.ktpFile = ktpFile;
    }

    public File getFaceFile() {
        return faceFile;
    }

    public void setFaceFile(File faceFile) {
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
        this.ktpFile = (File) in.readSerializable();
        this.faceFile = (File) in.readSerializable();
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
