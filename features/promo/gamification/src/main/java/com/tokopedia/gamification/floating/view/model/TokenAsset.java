package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenAsset implements Parcelable{

    private String name;
    private int version;
    private String backgroundImgUrl;
    private String smallImgUrl;
    private List<String> imageUrls;
    private String spriteUrl;

    protected TokenAsset(Parcel in) {
        name = in.readString();
        version = in.readInt();
        smallImgUrl = in.readString();
        imageUrls = in.createStringArrayList();
        spriteUrl = in.readString();
        backgroundImgUrl = in.readString();
    }

    public static final Creator<TokenAsset> CREATOR = new Creator<TokenAsset>() {
        @Override
        public TokenAsset createFromParcel(Parcel in) {
            return new TokenAsset(in);
        }

        @Override
        public TokenAsset[] newArray(int size) {
            return new TokenAsset[size];
        }
    };

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getBackgroundImgUrl() {
        return backgroundImgUrl;
    }

    public void setBackgroundImgUrl(String backgroundImgUrl) {
        this.backgroundImgUrl = backgroundImgUrl;
    }

    public TokenAsset() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(version);
        parcel.writeString(smallImgUrl);
        parcel.writeStringList(imageUrls);
        parcel.writeString(spriteUrl);
        parcel.writeString(backgroundImgUrl);
    }
}
