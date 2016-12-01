package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Indomaret
 * Created by Angga.Prasetiyo on 11/07/2016.
 */
public class Indomaret implements Parcelable {
    @SerializedName("charge_real")
    @Expose
    private String chargeReal;
    @SerializedName("charge")
    @Expose
    private String charge;
    @SerializedName("charge_real_idr")
    @Expose
    private String chargeRealIdr;
    @SerializedName("charge_idr")
    @Expose
    private String chargeIdr;
    @SerializedName("total_idr")
    @Expose
    private String totalIdr;
    @SerializedName("total_charge_real_idr")
    @Expose
    private String totalChargeRealIdr;
    @SerializedName("total")
    @Expose
    private String total;

    public String getChargeReal() {
        return chargeReal;
    }

    public void setChargeReal(String chargeReal) {
        this.chargeReal = chargeReal;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getChargeRealIdr() {
        return chargeRealIdr;
    }

    public void setChargeRealIdr(String chargeRealIdr) {
        this.chargeRealIdr = chargeRealIdr;
    }

    public String getChargeIdr() {
        return chargeIdr;
    }

    public void setChargeIdr(String chargeIdr) {
        this.chargeIdr = chargeIdr;
    }

    public String getTotalIdr() {
        return totalIdr;
    }

    public void setTotalIdr(String totalIdr) {
        this.totalIdr = totalIdr;
    }

    public String getTotalChargeRealIdr() {
        return totalChargeRealIdr;
    }

    public void setTotalChargeRealIdr(String totalChargeRealIdr) {
        this.totalChargeRealIdr = totalChargeRealIdr;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    protected Indomaret(Parcel in) {
        chargeReal = in.readString();
        charge = in.readString();
        chargeRealIdr = in.readString();
        chargeIdr = in.readString();
        totalIdr = in.readString();
        totalChargeRealIdr = in.readString();
        total = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chargeReal);
        dest.writeString(charge);
        dest.writeString(chargeRealIdr);
        dest.writeString(chargeIdr);
        dest.writeString(totalIdr);
        dest.writeString(totalChargeRealIdr);
        dest.writeString(total);
    }


    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Indomaret> CREATOR = new Parcelable.Creator<Indomaret>() {
        @Override
        public Indomaret createFromParcel(Parcel in) {
            return new Indomaret(in);
        }

        @Override
        public Indomaret[] newArray(int size) {
            return new Indomaret[size];
        }
    };
}
