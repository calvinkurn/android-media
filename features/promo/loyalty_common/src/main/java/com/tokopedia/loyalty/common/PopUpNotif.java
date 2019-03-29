package com.tokopedia.loyalty.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 6/25/18. Tokopedia
 */

public class PopUpNotif implements Parcelable{

    private String title;
    private String text;
    private String imageUrl = "";
    private String buttonText;
    private String buttonUrl;
    private String appLink;

    public PopUpNotif() {
    }

    protected PopUpNotif(Parcel in) {
        title = in.readString();
        text = in.readString();
        imageUrl = in.readString();
        buttonText = in.readString();
        buttonUrl = in.readString();
        appLink = in.readString();
    }

    public static final Creator<PopUpNotif> CREATOR = new Creator<PopUpNotif>() {
        @Override
        public PopUpNotif createFromParcel(Parcel in) {
            return new PopUpNotif(in);
        }

        @Override
        public PopUpNotif[] newArray(int size) {
            return new PopUpNotif[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeString(imageUrl);
        parcel.writeString(buttonText);
        parcel.writeString(buttonUrl);
        parcel.writeString(appLink);
    }
}
