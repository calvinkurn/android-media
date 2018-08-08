package com.tokopedia.tkpd.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ashwanityagi on 30/03/18.
 */

public class InAppMessageItemModel implements Parcelable {
    @SerializedName("id")
    public String id;

    @SerializedName("image_url")
    public String imageUrl;

    @SerializedName("deeplink")
    public String deeplink;

    @SerializedName("title")
    public String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDeeplink() {
        return deeplink;
    }

    public void setDeeplink(String deeplink) {
        this.deeplink = deeplink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.imageUrl);
        dest.writeString(this.deeplink);
        dest.writeString(this.title);
    }

    public InAppMessageItemModel() {
    }

    protected InAppMessageItemModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.deeplink = in.readString();
        this.title = in.readString();
    }

    public static final Creator<InAppMessageItemModel> CREATOR = new Creator<InAppMessageItemModel>() {
        @Override
        public InAppMessageItemModel createFromParcel(Parcel source) {
            return new InAppMessageItemModel(source);
        }

        @Override
        public InAppMessageItemModel[] newArray(int size) {
            return new InAppMessageItemModel[size];
        }
    };
}
