
package com.tokopedia.core.network.entity.home;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand implements Parcelable
{
    @SerializedName("shop_id")
    @Expose
    private int shopId;

    @SerializedName("shop_url")
    @Expose
    private String shopUrl;

    @SerializedName("shop_name")
    @Expose
    private String shopName;

    @SerializedName("logo_url")
    @Expose
    private String logoUrl;

    @SerializedName("is_new")
    @Expose
    private int isNew;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shopId);
        dest.writeString(this.shopUrl);
        dest.writeString(this.shopName);
        dest.writeString(this.logoUrl);
        dest.writeInt(this.isNew);
    }

    public Brand() {
    }

    protected Brand(Parcel in) {
        this.shopId = in.readInt();
        this.shopUrl = in.readString();
        this.shopName = in.readString();
        this.logoUrl = in.readString();
        this.isNew = in.readInt();
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}
