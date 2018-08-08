package com.tokopedia.product.manage.item.wholesale.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.product.manage.item.common.util.ItemType;


/**
 * @author normansyahputa on 4/20/17.
 */
public class WholesaleModel implements Parcelable, ItemType {
    public static final int TYPE = 1212834;
    public static final Parcelable.Creator<WholesaleModel> CREATOR = new Parcelable.Creator<WholesaleModel>() {
        @Override
        public WholesaleModel createFromParcel(Parcel source) {
            return new WholesaleModel(source);
        }

        @Override
        public WholesaleModel[] newArray(int size) {
            return new WholesaleModel[size];
        }
    };
    /**
     * Main value : qty one, two, and price
     */
    private long qtyMin = 0;
    private double qtyPrice = 0;
    private String statusPrice = "";
    private String statusQty = "";
    private boolean focusPrice = false;
    private boolean focusQty = false;
    private int level;

    public WholesaleModel(long quantityOne, double wholeSalePrice) {
        this.qtyMin = quantityOne;
        this.qtyPrice = wholeSalePrice;
    }

    protected WholesaleModel(Parcel in) {
        this.qtyMin = in.readLong();
        this.qtyPrice = in.readDouble();
        this.level = in.readInt();
    }

    public static WholesaleModel invalidWholeSaleModel() {
        WholesaleModel wholesaleModel = new WholesaleModel(-1, -1);
        return wholesaleModel;
    }

    public long getQtyMin() {
        return qtyMin;
    }

    public void setQtyMin(int qtyMin) {
        this.qtyMin = qtyMin;
    }

    public double getQtyPrice() {
        return qtyPrice;
    }

    public void setQtyPrice(double qtyPrice) {
        this.qtyPrice = qtyPrice;
    }

    @Override
    public String toString() {
        return " qty one : " + qtyMin + " qty price : " + qtyPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.qtyMin);
        dest.writeDouble(this.qtyPrice);
        dest.writeInt(this.level);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    public boolean isFocusPrice() {
        return focusPrice;
    }

    public void setFocusPrice(boolean focusPrice) {
        this.focusPrice = focusPrice;
    }

    public boolean isFocusQty() {
        return focusQty;
    }

    public void setFocusQty(boolean focusQty) {
        this.focusQty = focusQty;
    }

    public String getStatusPrice() {
        return statusPrice;
    }

    public void setStatusPrice(String statusPrice) {
        this.statusPrice = statusPrice;
    }

    public String getStatusQty() {
        return statusQty;
    }

    public void setStatusQty(String statusQty) {
        this.statusQty = statusQty;
    }
}
