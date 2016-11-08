package com.tokopedia.core.purchase.model.response.txverification;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 25/05/2016.
 */
public class ImgProof implements Parcelable {
    private static final String TAG = ImgProof.class.getSimpleName();

    @SerializedName("img_path")
    @Expose
    private String imgPath;
    @SerializedName("img_name")
    @Expose
    private String imgName;

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    protected ImgProof(Parcel in) {
        imgPath = in.readString();
        imgName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgPath);
        dest.writeString(imgName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ImgProof> CREATOR = new Parcelable.Creator<ImgProof>() {
        @Override
        public ImgProof createFromParcel(Parcel in) {
            return new ImgProof(in);
        }

        @Override
        public ImgProof[] newArray(int size) {
            return new ImgProof[size];
        }
    };
}
