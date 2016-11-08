package com.tokopedia.core.purchase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by herdimac on 4/8/16.
 * modified by Angga.Prasetiyo implement parcelable
 */
public class SysBank implements Parcelable {

    @SerializedName("acc_name")
    @Expose
    private String accName;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("cabang")
    @Expose
    private String cabang;
    @SerializedName("acc_no")
    @Expose
    private String accNo;

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String cabang) {
        this.cabang = cabang;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    protected SysBank(Parcel in) {
        accName = in.readString();
        bankName = in.readString();
        cabang = in.readString();
        accNo = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accName);
        dest.writeString(bankName);
        dest.writeString(cabang);
        dest.writeString(accNo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SysBank> CREATOR = new Parcelable.Creator<SysBank>() {
        @Override
        public SysBank createFromParcel(Parcel in) {
            return new SysBank(in);
        }

        @Override
        public SysBank[] newArray(int size) {
            return new SysBank[size];
        }
    };
}
