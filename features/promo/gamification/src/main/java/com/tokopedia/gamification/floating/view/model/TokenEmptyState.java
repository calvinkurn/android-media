package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 4/9/18.
 */

public class TokenEmptyState implements Parcelable {

    private String title;
    private String buttonText;
    private String buttonApplink;
    private String buttonURL;
    private String backgroundImgUrl;
    private String imageUrl;
    private Integer version;

    public TokenEmptyState() {
    }

    protected TokenEmptyState(Parcel in) {
        title = in.readString();
        buttonText = in.readString();
        buttonApplink = in.readString();
        buttonURL = in.readString();
        backgroundImgUrl = in.readString();
        imageUrl = in.readString();
        if (in.readByte() == 0) {
            version = null;
        } else {
            version = in.readInt();
        }
    }

    public static final Creator<TokenEmptyState> CREATOR = new Creator<TokenEmptyState>() {
        @Override
        public TokenEmptyState createFromParcel(Parcel in) {
            return new TokenEmptyState(in);
        }

        @Override
        public TokenEmptyState[] newArray(int size) {
            return new TokenEmptyState[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public void setButtonApplink(String buttonApplink) {
        this.buttonApplink = buttonApplink;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getTitle() {
        return this.title;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    public String getButtonApplink() {
        return this.buttonApplink;
    }

    public String getButtonURL() {
        return this.buttonURL;
    }

    public String getBackgroundImgUrl() {
        return this.backgroundImgUrl;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Integer getVersion() {
        return this.version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(buttonText);
        parcel.writeString(buttonApplink);
        parcel.writeString(buttonURL);
        parcel.writeString(backgroundImgUrl);
        parcel.writeString(imageUrl);
        if (version == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(version);
        }
    }
}
