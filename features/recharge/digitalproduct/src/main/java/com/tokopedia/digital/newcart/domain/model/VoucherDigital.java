package com.tokopedia.digital.newcart.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by Nabilla Sabbaha on 3/7/2017.
 */

public class VoucherDigital implements Parcelable{

    private VoucherAttributeDigital attributeVoucher;

    protected VoucherDigital(Parcel in) {
        attributeVoucher = in.readParcelable(VoucherAttributeDigital.class.getClassLoader());
    }

    public static final Creator<VoucherDigital> CREATOR = new Creator<VoucherDigital>() {
        @Override
        public VoucherDigital createFromParcel(Parcel in) {
            return new VoucherDigital(in);
        }

        @Override
        public VoucherDigital[] newArray(int size) {
            return new VoucherDigital[size];
        }
    };

    public VoucherAttributeDigital getAttributeVoucher() {
        return attributeVoucher;
    }

    public void setAttributeVoucher(VoucherAttributeDigital attributeVoucher) {
        this.attributeVoucher = attributeVoucher;
    }

    public VoucherDigital() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(attributeVoucher, flags);
    }
}
