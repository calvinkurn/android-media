package com.tokopedia.useridentification.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvinatin on 21/11/18.
 */

public class ImageUploadModel implements Parcelable {
    private int kycType;
    private String picObjKyc;
    private String filePath;
    private int isSuccess;
    private String error;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getKycType() {
        return kycType;
    }

    public void setKycType(int kycType) {
        this.kycType = kycType;
    }

    public String getPicObjKyc() {
        return picObjKyc;
    }

    public void setPicObjKyc(String picObjKyc) {
        this.picObjKyc = picObjKyc;
    }

    public ImageUploadModel(int kycType, String filePath) {
        this.kycType = kycType;
        this.filePath = filePath;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.kycType);
        dest.writeString(this.picObjKyc);
        dest.writeString(this.filePath);
        dest.writeInt(this.isSuccess);
        dest.writeString(this.error);
    }

    protected ImageUploadModel(Parcel in) {
        this.kycType = in.readInt();
        this.picObjKyc = in.readString();
        this.filePath = in.readString();
        this.isSuccess = in.readInt();
        this.error = in.readString();
    }

    public static final Creator<ImageUploadModel> CREATOR = new Creator<ImageUploadModel>() {
        @Override
        public ImageUploadModel createFromParcel(Parcel source) {
            return new ImageUploadModel(source);
        }

        @Override
        public ImageUploadModel[] newArray(int size) {
            return new ImageUploadModel[size];
        }
    };
}
