package com.tokopedia.digital.cart.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherDigital implements Parcelable {

    private String type;

    private String id;

    private VoucherAttributeDigital attributeVoucher;

    private Relation cart;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public VoucherAttributeDigital getAttributeVoucher() {
        return attributeVoucher;
    }

    public void setAttributeVoucher(VoucherAttributeDigital attributeVoucher) {
        this.attributeVoucher = attributeVoucher;
    }

    public Relation getCart() {
        return cart;
    }

    public void setCart(Relation cart) {
        this.cart = cart;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeParcelable(this.attributeVoucher, flags);
        dest.writeParcelable(this.cart, flags);
    }

    public VoucherDigital() {
    }

    protected VoucherDigital(Parcel in) {
        this.type = in.readString();
        this.id = in.readString();
        this.attributeVoucher = in.readParcelable(VoucherAttributeDigital.class.getClassLoader());
        this.cart = in.readParcelable(Relation.class.getClassLoader());
    }

    public static final Parcelable.Creator<VoucherDigital> CREATOR = new Parcelable.Creator<VoucherDigital>() {
        @Override
        public VoucherDigital createFromParcel(Parcel source) {
            return new VoucherDigital(source);
        }

        @Override
        public VoucherDigital[] newArray(int size) {
            return new VoucherDigital[size];
        }
    };
}
