package com.tokopedia.tokocash.qrpayment.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class QrPaymentTokoCash implements Parcelable {

    private long paymentId;
    private String status;
    private long transactionId;
    private String dateTime;
    private String logo;
    private long amount;

    public QrPaymentTokoCash() {
    }

    protected QrPaymentTokoCash(Parcel in) {
        paymentId = in.readLong();
        status = in.readString();
        transactionId = in.readLong();
        dateTime = in.readString();
        logo = in.readString();
        amount = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(paymentId);
        dest.writeString(status);
        dest.writeLong(transactionId);
        dest.writeString(dateTime);
        dest.writeString(logo);
        dest.writeLong(amount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QrPaymentTokoCash> CREATOR = new Creator<QrPaymentTokoCash>() {
        @Override
        public QrPaymentTokoCash createFromParcel(Parcel in) {
            return new QrPaymentTokoCash(in);
        }

        @Override
        public QrPaymentTokoCash[] newArray(int size) {
            return new QrPaymentTokoCash[size];
        }
    };

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
