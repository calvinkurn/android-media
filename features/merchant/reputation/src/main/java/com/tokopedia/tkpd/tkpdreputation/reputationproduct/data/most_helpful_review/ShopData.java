
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.most_helpful_review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopData implements Parcelable{

    @SerializedName("shop_reputation_set")
    @Expose
    private ShopReputationSet shopReputationSet;
    @SerializedName("shop_img_uri")
    @Expose
    private String shopImgUri;
    @SerializedName("shop_name")
    @Expose
    private String shopName;

    protected ShopData(Parcel in) {
        shopImgUri = in.readString();
        shopName = in.readString();
        shopReputationSet = (ShopReputationSet) in.readValue(ShopReputationSet.class.getClassLoader());
    }

    public static final Creator<ShopData> CREATOR = new Creator<ShopData>() {
        @Override
        public ShopData createFromParcel(Parcel in) {
            return new ShopData(in);
        }

        @Override
        public ShopData[] newArray(int size) {
            return new ShopData[size];
        }
    };

    /**
     * 
     * @return
     *     The shopReputationSet
     */
    public ShopReputationSet getShopReputationSet() {
        return shopReputationSet;
    }

    /**
     * 
     * @param shopReputationSet
     *     The shop_reputation_set
     */
    public void setShopReputationSet(ShopReputationSet shopReputationSet) {
        this.shopReputationSet = shopReputationSet;
    }

    /**
     * 
     * @return
     *     The shopImgUri
     */
    public String getShopImgUri() {
        return shopImgUri;
    }

    /**
     * 
     * @param shopImgUri
     *     The shop_img_uri
     */
    public void setShopImgUri(String shopImgUri) {
        this.shopImgUri = shopImgUri;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopImgUri);
        dest.writeString(shopName);
        dest.writeValue(shopReputationSet);
    }
}
