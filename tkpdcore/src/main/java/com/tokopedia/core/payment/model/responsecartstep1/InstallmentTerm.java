package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * InstallmentTerm
 * Created by Angga.Prasetiyo on 11/07/2016.
 */
public class InstallmentTerm implements Parcelable{
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("monthly_price")
    @Expose
    private String monthlyPrice;
    @SerializedName("total_price_idr")
    @Expose
    private String totalPriceIdr;
    @SerializedName("admin_price_idr")
    @Expose
    private String adminPriceIdr;
    @SerializedName("monthly_price_idr")
    @Expose
    private String monthlyPriceIdr;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("is_zero")
    @Expose
    private Integer isZero;
    @SerializedName("interest_price_idr")
    @Expose
    private String interestPriceIdr;
    @SerializedName("interest_price")
    @Expose
    private String interestPrice;
    @SerializedName("admin_price")
    @Expose
    private String adminPrice;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(String monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public String getTotalPriceIdr() {
        return totalPriceIdr;
    }

    public void setTotalPriceIdr(String totalPriceIdr) {
        this.totalPriceIdr = totalPriceIdr;
    }

    public String getAdminPriceIdr() {
        return adminPriceIdr;
    }

    public void setAdminPriceIdr(String adminPriceIdr) {
        this.adminPriceIdr = adminPriceIdr;
    }

    public String getMonthlyPriceIdr() {
        return monthlyPriceIdr;
    }

    public void setMonthlyPriceIdr(String monthlyPriceIdr) {
        this.monthlyPriceIdr = monthlyPriceIdr;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getIsZero() {
        return isZero;
    }

    public void setIsZero(Integer isZero) {
        this.isZero = isZero;
    }

    public String getInterestPriceIdr() {
        return interestPriceIdr;
    }

    public void setInterestPriceIdr(String interestPriceIdr) {
        this.interestPriceIdr = interestPriceIdr;
    }

    public String getInterestPrice() {
        return interestPrice;
    }

    public void setInterestPrice(String interestPrice) {
        this.interestPrice = interestPrice;
    }

    public String getAdminPrice() {
        return adminPrice;
    }

    public void setAdminPrice(String adminPrice) {
        this.adminPrice = adminPrice;
    }

    protected InstallmentTerm(Parcel in) {
        totalPrice = in.readString();
        monthlyPrice = in.readString();
        totalPriceIdr = in.readString();
        adminPriceIdr = in.readString();
        monthlyPriceIdr = in.readString();
        duration = in.readString();
        isZero = in.readByte() == 0x00 ? null : in.readInt();
        interestPriceIdr = in.readString();
        interestPrice = in.readString();
        adminPrice = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(totalPrice);
        dest.writeString(monthlyPrice);
        dest.writeString(totalPriceIdr);
        dest.writeString(adminPriceIdr);
        dest.writeString(monthlyPriceIdr);
        dest.writeString(duration);
        if (isZero == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(isZero);
        }
        dest.writeString(interestPriceIdr);
        dest.writeString(interestPrice);
        dest.writeString(adminPrice);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<InstallmentTerm> CREATOR = new Parcelable.Creator<InstallmentTerm>() {
        @Override
        public InstallmentTerm createFromParcel(Parcel in) {
            return new InstallmentTerm(in);
        }

        @Override
        public InstallmentTerm[] newArray(int size) {
            return new InstallmentTerm[size];
        }
    };
}
