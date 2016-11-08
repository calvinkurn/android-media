package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogImage implements Parcelable {

    @SerializedName("image_src_full")
    @Expose
    private String imageSrcFull;
    @SerializedName("image_primary")
    @Expose
    private String imagePrimary;
    @SerializedName("image_src")
    @Expose
    private String imageSrc;

    public String getImageSrcFull() {
        return imageSrcFull;
    }

    public String getImagePrimary() {
        return imagePrimary;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageSrcFull);
        dest.writeString(this.imagePrimary);
        dest.writeString(this.imageSrc);
    }

    public CatalogImage() {
    }

    protected CatalogImage(Parcel in) {
        this.imageSrcFull = in.readString();
        this.imagePrimary = in.readString();
        this.imageSrc = in.readString();
    }

    public static final Parcelable.Creator<CatalogImage> CREATOR = new Parcelable.Creator<CatalogImage>() {
        @Override
        public CatalogImage createFromParcel(Parcel source) {
            return new CatalogImage(source);
        }

        @Override
        public CatalogImage[] newArray(int size) {
            return new CatalogImage[size];
        }
    };
}
