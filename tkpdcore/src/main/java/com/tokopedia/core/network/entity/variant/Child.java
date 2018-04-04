
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Child implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("stock")
    @Expose
    private int stock;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("option_ids")
    @Expose
    private List<Integer> optionIds = null;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("is_buyable")
    @Expose
    private boolean isBuyable;
    @SerializedName("picture")
    @Expose
    private PictureChild picture;
    @SerializedName("price_fmt")
    @Expose
    private String priceFmt;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("is_wishlist")
    @Expose
    private boolean isWishlist;
    @SerializedName("campaign")
    @Expose
    private Campaign campaign;


    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Integer> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Integer> optionIds) {
        this.optionIds = optionIds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsBuyable() {
        return isBuyable;
    }

    public void setIsBuyable(boolean isBuyable) {
        this.isBuyable = isBuyable;
    }

    public PictureChild getPicture() {
        return picture;
    }

    public void setPicture(PictureChild picture) {
        this.picture = picture;
    }

    public String getPriceFmt() {
        return priceFmt;
    }

    public void setPriceFmt(String priceFmt) {
        this.priceFmt = priceFmt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isWishlist() {
        return isWishlist;
    }

    public void setWishlist(boolean wishlist) {
        isWishlist = wishlist;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    protected Child(Parcel in) {
        productId = in.readInt();
        price = in.readInt();
        stock = in.readInt();
        sku = in.readString();
        if (in.readByte() == 0x01) {
            optionIds = new ArrayList<Integer>();
            in.readList(optionIds, Integer.class.getClassLoader());
        } else {
            optionIds = null;
        }
        enabled = in.readByte() != 0x00;
        name = in.readString();
        isBuyable = in.readByte() != 0x00;
        picture = (PictureChild) in.readValue(PictureChild.class.getClassLoader());
        priceFmt = in.readString();
        url = in.readString();
        isWishlist = in.readByte() != 0x00;
        campaign = (Campaign) in.readValue(Campaign.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productId);
        dest.writeInt(price);
        dest.writeInt(stock);
        dest.writeString(sku);
        if (optionIds == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(optionIds);
        }
        dest.writeByte((byte) (enabled ? 0x01 : 0x00));
        dest.writeString(name);
        dest.writeByte((byte) (isBuyable ? 0x01 : 0x00));
        dest.writeValue(picture);
        dest.writeString(priceFmt);
        dest.writeString(url);
        dest.writeByte((byte) (isWishlist ? 0x01 : 0x00));
        dest.writeValue(campaign);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Child> CREATOR = new Parcelable.Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };
}