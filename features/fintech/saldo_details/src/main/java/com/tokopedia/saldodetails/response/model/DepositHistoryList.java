package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.tokopedia.saldodetails.adapter.SaldoDetailTransactionFactory;
import com.tokopedia.saldodetails.viewmodel.ParcelableViewModel;

public class DepositHistoryList implements ParcelableViewModel<SaldoDetailTransactionFactory> {

    @SerializedName("deposit_id")
    private long depositId;

    @SerializedName("saldo")
    private long saldo;

    @SerializedName("note")
    private String note;

    @SerializedName("amount")
    private int amount;

    @SerializedName("type_description")
    private String typeDesc;

    @SerializedName("type")
    private int type;

    @SerializedName("class")
    private String transactionClass;

    @SerializedName("image")
    private String imageURL;

    @SerializedName("create_time")
    private String createTime;

    @SerializedName("withdrawal_date")
    private String withdrawalDate;

    @SerializedName("withdrawal_status")
    private int withdrawStatus;


    protected DepositHistoryList(Parcel in) {
        depositId = in.readLong();
        saldo = in.readLong();
        note = in.readString();
        amount = in.readInt();
        typeDesc = in.readString();
        type = in.readInt();
        transactionClass = in.readString();
        imageURL = in.readString();
        createTime = in.readString();
        withdrawalDate = in.readString();
        withdrawStatus = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(depositId);
        parcel.writeLong(saldo);
        parcel.writeString(note);
        parcel.writeInt(amount);
        parcel.writeString(typeDesc);
        parcel.writeInt(type);
        parcel.writeString(transactionClass);
        parcel.writeString(imageURL);
        parcel.writeString(createTime);
        parcel.writeString(withdrawalDate);
        parcel.writeInt(withdrawStatus);
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


    public long getDepositId() {
        return depositId;
    }

    public void setDepositId(long depositId) {
        this.depositId = depositId;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }

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

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getWithdrawalDate() {
        return withdrawalDate;
    }

    public void setWithdrawalDate(String withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }

    public int getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(int withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
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