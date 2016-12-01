package com.tokopedia.core.payment.model.responsedynamicpayment;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 19/05/2016.
 */
public class Parameter implements Parcelable {
    private static final String TAG = Parameter.class.getSimpleName();

    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("gateway_code")
    @Expose
    private String gatewayCode;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("customer_email")
    @Expose
    private String customerEmail;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("items")
    @Expose
    private List<Item> items = new ArrayList<Item>();
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("profile_code")
    @Expose
    private String profileCode;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGatewayCode() {
        return gatewayCode;
    }

    public void setGatewayCode(String gatewayCode) {
        this.gatewayCode = gatewayCode;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfileCode() {
        return profileCode;
    }

    public void setProfileCode(String profileCode) {
        this.profileCode = profileCode;
    }

    protected Parameter(Parcel in) {
        customerName = in.readString();
        gatewayCode = in.readString();
        transactionDate = in.readString();
        merchantCode = in.readString();
        signature = in.readString();
        amount = in.readString();
        currency = in.readString();
        customerEmail = in.readString();
        transactionId = in.readString();
        if (in.readByte() == 0x01) {
            items = new ArrayList<Item>();
            in.readList(items, Item.class.getClassLoader());
        } else {
            items = null;
        }
        language = in.readString();
        profileCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerName);
        dest.writeString(gatewayCode);
        dest.writeString(transactionDate);
        dest.writeString(merchantCode);
        dest.writeString(signature);
        dest.writeString(amount);
        dest.writeString(currency);
        dest.writeString(customerEmail);
        dest.writeString(transactionId);
        if (items == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(items);
        }
        dest.writeString(language);
        dest.writeString(profileCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Parameter> CREATOR = new Parcelable.Creator<Parameter>() {
        @Override
        public Parameter createFromParcel(Parcel in) {
            return new Parameter(in);
        }

        @Override
        public Parameter[] newArray(int size) {
            return new Parameter[size];
        }
    };
}
