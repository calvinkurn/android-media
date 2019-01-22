package com.tokopedia.loyalty.domain.entity.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kris on 12/5/17. Tokopedia
 */

public class Voucher implements Parcelable{
    @SerializedName("voucher_amount_idr")
    @Expose
    private String voucherAmountIdr;
    @SerializedName("voucher_amount")
    @Expose
    private String voucherAmount;
    @SerializedName("voucher_no_other_promotion")
    @Expose
    private String voucherNoOtherPromotion;
    @SerializedName("voucher_promo_desc")
    @Expose
    private String voucherPromoDesc;

    public String getVoucherAmountIdr() {
        return voucherAmountIdr;
    }

    public void setVoucherAmountIdr(String voucherAmountIdr) {
        this.voucherAmountIdr = voucherAmountIdr;
    }

    public String getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(String voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public String getVoucherNoOtherPromotion() {
        return voucherNoOtherPromotion;
    }

    public void setVoucherNoOtherPromotion(String voucherNoOtherPromotion) {
        this.voucherNoOtherPromotion = voucherNoOtherPromotion;
    }

    public String getVoucherPromoDesc() {
        return voucherPromoDesc;
    }

    public void setVoucherPromoDesc(String voucherPromoDesc) {
        this.voucherPromoDesc = voucherPromoDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.voucherAmountIdr);
        dest.writeString(this.voucherAmount);
        dest.writeString(this.voucherNoOtherPromotion);
        dest.writeString(this.voucherPromoDesc);
    }

    public Voucher() {
    }

    protected Voucher(Parcel in) {
        this.voucherAmountIdr = in.readString();
        this.voucherAmount = in.readString();
        this.voucherNoOtherPromotion = in.readString();
        this.voucherPromoDesc = in.readString();
    }

    public static final Parcelable.Creator<Voucher> CREATOR = new Parcelable.Creator<Voucher>() {
        @Override
        public Voucher createFromParcel(Parcel source) {
            return new Voucher(source);
        }

        @Override
        public Voucher[] newArray(int size) {
            return new Voucher[size];
        }
    };
}
