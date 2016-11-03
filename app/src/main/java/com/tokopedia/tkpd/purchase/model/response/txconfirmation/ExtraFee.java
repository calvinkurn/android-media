package com.tokopedia.tkpd.purchase.model.response.txconfirmation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 12/05/2016.
 */
public class ExtraFee implements Parcelable {
    private static final String TAG = ExtraFee.class.getSimpleName();

    @SerializedName("extra_fee_amount")
    @Expose
    private String extraFeeAmount;
    @SerializedName("extra_fee_amount_idr")
    @Expose
    private String extraFeeAmountIdr;
    @SerializedName("extra_fee_type")
    @Expose
    private String extraFeeType;

    public String getExtraFeeAmount() {
        return extraFeeAmount;
    }

    public void setExtraFeeAmount(String extraFeeAmount) {
        this.extraFeeAmount = extraFeeAmount;
    }

    public String getExtraFeeAmountIdr() {
        return extraFeeAmountIdr;
    }

    public void setExtraFeeAmountIdr(String extraFeeAmountIdr) {
        this.extraFeeAmountIdr = extraFeeAmountIdr;
    }

    public String getExtraFeeType() {
        return extraFeeType;
    }

    public void setExtraFeeType(String extraFeeType) {
        this.extraFeeType = extraFeeType;
    }

    protected ExtraFee(Parcel in) {
        extraFeeAmount = in.readString();
        extraFeeAmountIdr = in.readString();
        extraFeeType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(extraFeeAmount);
        dest.writeString(extraFeeAmountIdr);
        dest.writeString(extraFeeType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ExtraFee> CREATOR = new Parcelable.Creator<ExtraFee>() {
        @Override
        public ExtraFee createFromParcel(Parcel in) {
            return new ExtraFee(in);
        }

        @Override
        public ExtraFee[] newArray(int size) {
            return new ExtraFee[size];
        }
    };
}
