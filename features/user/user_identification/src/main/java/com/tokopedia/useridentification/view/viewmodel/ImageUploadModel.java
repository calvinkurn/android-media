package com.tokopedia.useridentification.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvinatin on 21/11/18.
 */

public class ImageUploadModel implements Parcelable {
    private String filePath;
    private String url;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageUploadModel(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filePath);
        dest.writeString(this.url);
    }

    protected ImageUploadModel(Parcel in) {
        this.filePath = in.readString();
        this.url = in.readString();
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
