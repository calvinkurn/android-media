package com.tokopedia.transactiondata.entity.shared.expresscheckout;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 03/01/19.
 */

public class AtcRequestParam implements Parcelable {
    @SerializedName("shop_id")
    public int shopId;

    @SerializedName("quantity")
    public int quantity;

    @SerializedName("notes")
    public String notes;

    @SerializedName("product_id")
    public int productId;

    public AtcRequestParam() {
    }

    protected AtcRequestParam(Parcel in) {
        shopId = in.readInt();
        quantity = in.readInt();
        notes = in.readString();
        productId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(shopId);
        dest.writeInt(quantity);
        dest.writeString(notes);
        dest.writeInt(productId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AtcRequestParam> CREATOR = new Creator<AtcRequestParam>() {
        @Override
        public AtcRequestParam createFromParcel(Parcel in) {
            return new AtcRequestParam(in);
        }

        @Override
        public AtcRequestParam[] newArray(int size) {
            return new AtcRequestParam[size];
        }
    };

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
