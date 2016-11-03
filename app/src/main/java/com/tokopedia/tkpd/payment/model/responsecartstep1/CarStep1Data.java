package com.tokopedia.tkpd.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * CarStep1Data
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class CarStep1Data implements Parcelable {
    @SerializedName("veritrans")
    @Expose
    private Veritrans veritrans;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;
    @SerializedName("year_now")
    @Expose
    private String yearNow;
    @SerializedName("credit_card_data")
    @Expose
    private CreditCardData creditCardData;

    public String getYearNow() {
        return yearNow;
    }

    public void setYearNow(String yearNow) {
        this.yearNow = yearNow;
    }

    public CreditCardData getCreditCardData() {
        return creditCardData;
    }

    public void setCreditCardData(CreditCardData creditCardData) {
        this.creditCardData = creditCardData;
    }

    public Veritrans getVeritrans() {
        return veritrans;
    }

    public void setVeritrans(Veritrans veritrans) {
        this.veritrans = veritrans;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    protected CarStep1Data(Parcel in) {
        veritrans = (Veritrans) in.readValue(Veritrans.class.getClassLoader());
        transaction = (Transaction) in.readValue(Transaction.class.getClassLoader());
        yearNow = in.readString();
        creditCardData = (CreditCardData) in.readValue(CreditCardData.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(veritrans);
        dest.writeValue(transaction);
        dest.writeString(yearNow);
        dest.writeValue(creditCardData);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CarStep1Data> CREATOR = new Parcelable.Creator<CarStep1Data>() {
        @Override
        public CarStep1Data createFromParcel(Parcel in) {
            return new CarStep1Data(in);
        }

        @Override
        public CarStep1Data[] newArray(int size) {
            return new CarStep1Data[size];
        }
    };
}
