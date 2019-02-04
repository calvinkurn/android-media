package com.tokopedia.gamification.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenAssetEntity implements Parcelable {

    @SerializedName("floatingImgUrl")
    @Expose
    private String floatingImgUrl;

    @SerializedName("smallImgUrl")
    @Expose
    private String smallImgUrl;

    @SerializedName("imageUrls")
    @Expose
    private List<String> imageUrls;

    @SerializedName("spriteUrl")
    @Expose
    private String spriteUrl;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("version")
    @Expose
    private int version;

    protected TokenAssetEntity(Parcel in) {
        floatingImgUrl = in.readString();
        smallImgUrl = in.readString();
        imageUrls = in.createStringArrayList();
        spriteUrl = in.readString();
        name = in.readString();
        version = in.readInt();
    }

    public static final Creator<TokenAssetEntity> CREATOR = new Creator<TokenAssetEntity>() {
        @Override
        public TokenAssetEntity createFromParcel(Parcel in) {
            return new TokenAssetEntity(in);
        }

        @Override
        public TokenAssetEntity[] newArray(int size) {
            return new TokenAssetEntity[size];
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

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public String getFloatingImgUrl() {
        return floatingImgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(floatingImgUrl);
        dest.writeString(smallImgUrl);
        dest.writeStringList(imageUrls);
        dest.writeString(spriteUrl);
        dest.writeString(name);
        dest.writeInt(version);
    }
}
