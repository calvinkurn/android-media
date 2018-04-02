package com.tokopedia.gamification.floating.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenAsset implements Parcelable{

    private String smallImgUrl;
    private List<String> imageUrls;
    private String spriteUrl;

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.smallImgUrl);
        dest.writeStringList(this.imageUrls);
        dest.writeString(this.spriteUrl);
    }

    public TokenAsset() {
    }

    protected TokenAsset(Parcel in) {
        this.smallImgUrl = in.readString();
        this.imageUrls = in.createStringArrayList();
        this.spriteUrl = in.readString();
    }

    public static final Creator<TokenAsset> CREATOR = new Creator<TokenAsset>() {
        @Override
        public TokenAsset createFromParcel(Parcel source) {
            return new TokenAsset(source);
        }

        @Override
        public TokenAsset[] newArray(int size) {
            return new TokenAsset[size];
        }
    };
}
