package com.tokopedia.pms.bankaccount.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListModel implements Parcelable {

    private String id;
    private String bankName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.bankName);
    }

    public BankListModel() {
    }

    protected BankListModel(Parcel in) {
        this.id = in.readString();
        this.bankName = in.readString();
    }

    public static final Parcelable.Creator<BankListModel> CREATOR = new Parcelable.Creator<BankListModel>() {
        @Override
        public BankListModel createFromParcel(Parcel source) {
            return new BankListModel(source);
        }

        @Override
        public BankListModel[] newArray(int size) {
            return new BankListModel[size];
        }
    };
}
