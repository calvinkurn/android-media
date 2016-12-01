
package com.tokopedia.core.rescenter.inbox.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionShop implements Parcelable {

    @SerializedName("shop_image_300")
    @Expose
    private String shopImage300;
    @SerializedName("shop_image")
    @Expose
    private String shopImage;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;

    /**
     * 
     * @return
     *     The shopImage300
     */
    public String getShopImage300() {
        return shopImage300;
    }

    /**
     * 
     * @param shopImage300
     *     The shop_image_300
     */
    public void setShopImage300(String shopImage300) {
        this.shopImage300 = shopImage300;
    }

    /**
     * 
     * @return
     *     The shopImage
     */
    public String getShopImage() {
        return shopImage;
    }

    /**
     * 
     * @param shopImage
     *     The shop_image
     */
    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    /**
     * 
     * @return
     *     The shopId
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * 
     * @param shopId
     *     The shop_id
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * 
     * @return
     *     The shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 
     * @param shopName
     *     The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * 
     * @return
     *     The shopUrl
     */
    public String getShopUrl() {
        return shopUrl;
    }

    /**
     * 
     * @param shopUrl
     *     The shop_url
     */
    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopImage300);
        dest.writeString(this.shopImage);
        dest.writeValue(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopUrl);
    }

    public ResolutionShop() {
    }

    protected ResolutionShop(Parcel in) {
        this.shopImage300 = in.readString();
        this.shopImage = in.readString();
        this.shopId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.shopName = in.readString();
        this.shopUrl = in.readString();
    }

    public static final Parcelable.Creator<ResolutionShop> CREATOR = new Parcelable.Creator<ResolutionShop>() {
        @Override
        public ResolutionShop createFromParcel(Parcel source) {
            return new ResolutionShop(source);
        }

        @Override
        public ResolutionShop[] newArray(int size) {
            return new ResolutionShop[size];
        }
    };
}
