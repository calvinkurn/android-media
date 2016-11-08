package com.tokopedia.core.payment.model.responsecartstep2;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.payment.model.responsecartstep1.CreditCardData;
import com.tokopedia.core.payment.model.responsecartstep1.Veritrans;

import java.util.ArrayList;
import java.util.List;

/**
 * CartStep2Data
 * Created by Angga.Prasetiyo on 14/07/2016.
 */
public class CartStep2Data implements Parcelable {
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
    @SerializedName("system_bank")
    @Expose
    private List<SystemBank> systemBankList = new ArrayList<>();

    public List<SystemBank> getSystemBankList() {
        return systemBankList;
    }

    public void setSystemBankList(List<SystemBank> systemBankList) {
        this.systemBankList = systemBankList;
    }

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

    protected CartStep2Data(Parcel in) {
        veritrans = (Veritrans) in.readValue(Veritrans.class.getClassLoader());
        transaction = (Transaction) in.readValue(Transaction.class.getClassLoader());
        yearNow = in.readString();
        creditCardData = (CreditCardData) in.readValue(CreditCardData.class.getClassLoader());
        if (in.readByte() == 0x01) {
            systemBankList = new ArrayList<SystemBank>();
            in.readList(systemBankList, SystemBank.class.getClassLoader());
        } else {
            systemBankList = null;
        }
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
        if (systemBankList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(systemBankList);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CartStep2Data> CREATOR = new Parcelable.Creator<CartStep2Data>() {
        @Override
        public CartStep2Data createFromParcel(Parcel in) {
            return new CartStep2Data(in);
        }

        @Override
        public CartStep2Data[] newArray(int size) {
            return new CartStep2Data[size];
        }
    };
}
