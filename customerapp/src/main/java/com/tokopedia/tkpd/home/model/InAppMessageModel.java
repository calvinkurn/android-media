package com.tokopedia.tkpd.home.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashwanityagi on 30/03/18.
 */

public class InAppMessageModel implements Parcelable{
   private String id;
   private String imageUrl;
   private String deeplink;
   private String title;

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

    public InAppMessageModel() {
    }

    protected InAppMessageModel(Parcel in) {
        this.id = in.readString();
        this.imageUrl = in.readString();
        this.deeplink = in.readString();
        this.title = in.readString();
    }

    public static final Creator<InAppMessageModel> CREATOR = new Creator<InAppMessageModel>() {
        @Override
        public InAppMessageModel createFromParcel(Parcel source) {
            return new InAppMessageModel(source);
        }

        @Override
        public InAppMessageModel[] newArray(int size) {
            return new InAppMessageModel[size];
        }
    };
}
