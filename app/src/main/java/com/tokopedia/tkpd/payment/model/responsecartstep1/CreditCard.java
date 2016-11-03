package com.tokopedia.tkpd.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CreditCard
 * Created by Angga.Prasetiyo on 11/07/2016.
 */
public class CreditCard implements Parcelable {
    @SerializedName("charge")
    @Expose
    private String charge;
    @SerializedName("charge_idr")
    @Expose
    private String chargeIdr;
    @SerializedName("total_idr")
    @Expose
    private String totalIdr;
    @SerializedName("charge_25")
    @Expose
    private String charge25;
    @SerializedName("total")
    @Expose
    private String total;

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
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

    public String getCharge25() {
        return charge25;
    }

    public void setCharge25(String charge25) {
        this.charge25 = charge25;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    protected CreditCard(Parcel in) {
        charge = in.readString();
        chargeIdr = in.readString();
        totalIdr = in.readString();
        charge25 = in.readString();
        total = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(charge);
        dest.writeString(chargeIdr);
        dest.writeString(totalIdr);
        dest.writeString(charge25);
        dest.writeString(total);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CreditCard> CREATOR = new Parcelable.Creator<CreditCard>() {
        @Override
        public CreditCard createFromParcel(Parcel in) {
            return new CreditCard(in);
        }

        @Override
        public CreditCard[] newArray(int size) {
            return new CreditCard[size];
        }
    };

}
