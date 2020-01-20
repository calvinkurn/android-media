package com.tokopedia.core.network.entity.wishlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FreeOngkir implements Parcelable {
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Creator<FreeOngkir> getCREATOR() {
        return CREATOR;
    }

    protected FreeOngkir(Parcel in) {
        byte tmpIsActive = in.readByte();
        isActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        imageUrl = in.readString();
    }

    public static final Creator<FreeOngkir> CREATOR = new Creator<FreeOngkir>() {
        @Override
        public FreeOngkir createFromParcel(Parcel in) {
            return new FreeOngkir(in);
        }

        @Override
        public FreeOngkir[] newArray(int size) {
            return new FreeOngkir[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isActive == null ? 0 : isActive ? 1 : 2));
        dest.writeString(imageUrl);
    }
}
