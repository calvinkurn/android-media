package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CartShop
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class CartShop implements Parcelable{
    @SerializedName("lucky_merchant")
    @Expose
    private Integer luckyMerchant;
    @SerializedName("shop_status")
    @Expose
    private Integer shopStatus;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("shop_name")
    @Expose
    private String shopName;

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getLuckyMerchant() {
        return luckyMerchant;
    }

    public void setLuckyMerchant(Integer luckyMerchant) {
        this.luckyMerchant = luckyMerchant;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
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

    protected CartShop(Parcel in) {
        luckyMerchant = in.readByte() == 0x00 ? null : in.readInt();
        shopStatus = in.readByte() == 0x00 ? null : in.readInt();
        shopImage = in.readString();
        shopId = in.readString();
        shopUrl = in.readString();
        shopName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (luckyMerchant == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(luckyMerchant);
        }
        if (shopStatus == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(shopStatus);
        }
        dest.writeString(shopImage);
        dest.writeString(shopId);
        dest.writeString(shopUrl);
        dest.writeString(shopName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CartShop> CREATOR = new Parcelable.Creator<CartShop>() {
        @Override
        public CartShop createFromParcel(Parcel in) {
            return new CartShop(in);
        }

        @Override
        public CartShop[] newArray(int size) {
            return new CartShop[size];
        }
    };
}
