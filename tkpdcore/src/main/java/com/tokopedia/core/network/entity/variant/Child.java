
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
    private long productId;
    @SerializedName("price")
    @Expose
    private long price;
    @SerializedName("stock")
    @Expose
    private long stock;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("option_ids")
    @Expose
    private List<Long> optionIds = null;
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Long> getOptionIds() {
        return optionIds;
    }

    public void setOptionIds(List<Long> optionIds) {
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


    protected Child(Parcel in) {
        productId = in.readLong();
        price = in.readLong();
        stock = in.readLong();
        sku = in.readString();
        if (in.readByte() == 0x01) {
            optionIds = new ArrayList<Long>();
            in.readList(optionIds, Long.class.getClassLoader());
        } else {
            optionIds = null;
        }
        enabled = in.readByte() != 0x00;
        name = in.readString();
        isBuyable = in.readByte() != 0x00;
        picture = (PictureChild) in.readValue(PictureChild.class.getClassLoader());
        priceFmt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(productId);
        dest.writeLong(price);
        dest.writeLong(stock);
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