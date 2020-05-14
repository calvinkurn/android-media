package com.tokopedia.kyc_centralized.view.viewmodel;

import android.os.Parcel;

import com.tokopedia.abstraction.base.view.model.StepperModel;

import java.util.ArrayList;

/**
 * @author by alvinatin on 12/11/18.
 */

public class UserIdentificationStepperModel implements StepperModel {

    private String ktpFile;
    private String faceFile;

    private ArrayList<Integer> listRetake;
    private ArrayList<String> listMessage;
    private String titleText;
    private String subtitleText;
    private String buttonText;

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

    public ArrayList<Integer> getListRetake() {
        return listRetake;
    }

    public void setListRetake(ArrayList<Integer> listRetake) {
        this.listRetake = listRetake;
    }

    public ArrayList<String> getListMessage() {
        return listMessage;
    }

    public void setListMessage(ArrayList<String> listMessage) {
        this.listMessage = listMessage;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSubtitleText() {
        return subtitleText;
    }

    public void setSubtitleText(String subtitleText) {
        this.subtitleText = subtitleText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
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
