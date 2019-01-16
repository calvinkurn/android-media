package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
import com.tokopedia.saldodetails.viewmodel.ParcelableViewModel;

public class DepositHistoryList implements ParcelableViewModel<SaldoDetailTransactionFactory> {

    @SerializedName("note")
    private String note;

    @SerializedName("amount")
    private int amount;

    @SerializedName("class")
    private String transactionClass;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("create_time")
    private String createTime;

    protected DepositHistoryList(Parcel in) {
        note = in.readString();
        amount = in.readInt();
        transactionClass = in.readString();
        imageURL = in.readString();
        createTime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(note);
        parcel.writeInt(amount);
        parcel.writeString(transactionClass);
        parcel.writeString(imageURL);
        parcel.writeString(createTime);
    }

    public static final Creator<DepositHistoryList> CREATOR = new Creator<DepositHistoryList>() {
        @Override
        public DepositHistoryList createFromParcel(Parcel in) {
            return new DepositHistoryList(in);
        }

        @Override
        public DepositHistoryList[] newArray(int size) {
            return new DepositHistoryList[size];
        }
    };


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransactionClass() {
        return transactionClass;
    }

    public void setTransactionClass(String transactionClass) {
        this.transactionClass = transactionClass;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int type(SaldoDetailTransactionFactory typeFactory) {
        return typeFactory.type(this);
    }
}