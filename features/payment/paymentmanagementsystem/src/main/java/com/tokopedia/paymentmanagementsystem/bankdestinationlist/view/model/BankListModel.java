package com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.paymentmanagementsystem.bankdestinationlist.view.adapter.BankListAdapterTypeFactory;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public class BankListModel implements Visitable<BankListAdapterTypeFactory>,Parcelable {

    private int id;
    private String bankName;

    @Override
    public int type(BankListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        dest.writeInt(this.id);
        dest.writeString(this.bankName);
    }

    public BankListModel() {
    }

    protected BankListModel(Parcel in) {
        this.id = in.readInt();
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
