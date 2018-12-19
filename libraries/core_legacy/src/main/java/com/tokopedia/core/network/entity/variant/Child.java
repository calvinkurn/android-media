
package com.tokopedia.core.network.entity.variant;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isBuyable = false;
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
    @SerializedName("stock_wording")
    @Expose
    private String stockWording;
    @SerializedName("stock_wording_html")
    @Expose
    private String stockWordingHtml;
    @SerializedName("is_limited_stock")
    @Expose
    private boolean isLimitedStock;
    @SerializedName("variant_type")
    @Expose
    private String variantType;
    @SerializedName("always_available")
    @Expose
    private boolean alwaysAvailable;

    public boolean isAlwaysAvailable() {
        return alwaysAvailable;
    }

    public void setAlwaysAvailable(boolean alwaysAvailable) {
        this.alwaysAvailable = alwaysAvailable;
    }

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

    public String getStockWordingHtml() {
        return stockWordingHtml;
    }

    public void setStockWordingHtml(String stockWordingHtml) {
        this.stockWordingHtml = stockWordingHtml;
    }

    public String getStockWording() {
        return stockWording;
    }

    public void setStockWording(String stockWording) {
        this.stockWording = stockWording;
    }

    public boolean isLimitedStock() {
        return isLimitedStock;
    }

    public void setLimitedStock(boolean limitedStock) {
        isLimitedStock = limitedStock;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.productId);
        dest.writeInt(this.price);
        dest.writeInt(this.stock);
        dest.writeString(this.sku);
        dest.writeList(this.optionIds);
        dest.writeByte(this.enabled ? (byte) 1 : (byte) 0);
        dest.writeString(this.name);
        dest.writeByte(this.isBuyable ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.picture, flags);
        dest.writeString(this.priceFmt);
        dest.writeString(this.url);
        dest.writeByte(this.isWishlist ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.campaign, flags);
        dest.writeString(this.stockWording);
        dest.writeString(this.stockWordingHtml);
        dest.writeByte(this.isLimitedStock ? (byte) 1 : (byte) 0);
        dest.writeString(this.variantType);
        dest.writeByte(this.alwaysAvailable ? (byte) 1 : (byte) 0);
    }

    public Child() {
    }

    protected Child(Parcel in) {
        this.productId = in.readInt();
        this.price = in.readInt();
        this.stock = in.readInt();
        this.sku = in.readString();
        this.optionIds = new ArrayList<Integer>();
        in.readList(this.optionIds, Integer.class.getClassLoader());
        this.enabled = in.readByte() != 0;
        this.name = in.readString();
        this.isBuyable = in.readByte() != 0;
        this.picture = in.readParcelable(PictureChild.class.getClassLoader());
        this.priceFmt = in.readString();
        this.url = in.readString();
        this.isWishlist = in.readByte() != 0;
        this.campaign = in.readParcelable(Campaign.class.getClassLoader());
        this.stockWording = in.readString();
        this.stockWordingHtml = in.readString();
        this.isLimitedStock = in.readByte() != 0;
        this.variantType = in.readString();
        this.alwaysAvailable = in.readByte() != 0;
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel source) {
            return new Child(source);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };
}