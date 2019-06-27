package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 4/9/18.
 */

public class TokenEmptyStateEntity implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("buttonText")
    @Expose
    private String buttonText;

    @SerializedName("buttonApplink")
    @Expose
    private String buttonApplink;

    @SerializedName("buttonURL")
    @Expose
    private String buttonURL;

    @SerializedName("backgroundImgUrl")
    @Expose
    private String backgroundImgUrl;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("version")
    @Expose
    private Integer version;

    protected TokenEmptyStateEntity(Parcel in) {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(buttonText);
        dest.writeString(buttonApplink);
        dest.writeString(buttonURL);
        dest.writeString(backgroundImgUrl);
        dest.writeString(imageUrl);
        if (version == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(version);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TokenEmptyStateEntity> CREATOR = new Creator<TokenEmptyStateEntity>() {
        @Override
        public TokenEmptyStateEntity createFromParcel(Parcel in) {
            return new TokenEmptyStateEntity(in);
        }

        @Override
        public TokenEmptyStateEntity[] newArray(int size) {
            return new TokenEmptyStateEntity[size];
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
}
