
package com.tokopedia.challenges.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaTags implements Parcelable {

    @SerializedName("og:title")
    @Expose
    private String ogTitle;
    @SerializedName("og:description")
    @Expose
    private String ogDescription;
    @SerializedName("og:image")
    @Expose
    private String ogImage;

    protected MetaTags(Parcel in) {
        ogTitle = in.readString();
        ogDescription = in.readString();
        ogImage = in.readString();
    }

    public static final Creator<MetaTags> CREATOR = new Creator<MetaTags>() {
        @Override
        public MetaTags createFromParcel(Parcel in) {
            return new MetaTags(in);
        }

        @Override
        public MetaTags[] newArray(int size) {
            return new MetaTags[size];
        }
    };

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ogTitle);
        dest.writeString(ogDescription);
        dest.writeString(ogImage);
    }
}
