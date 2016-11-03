package com.tokopedia.tkpd.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * BCAParam
 * Created by Angga.Prasetiyo on 11/07/2016.
 */
public class BCAParam implements Parcelable {
    @SerializedName("bca_descp")
    @Expose
    private String bcaDescp;
    @SerializedName("bca_code")
    @Expose
    private String bcaCode;
    @SerializedName("bca_amt")
    @Expose
    private String bcaAmt;
    @SerializedName("bca_url")
    @Expose
    private String bcaUrl;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("miscFee")
    @Expose
    private String miscFee;
    @SerializedName("bca_date")
    @Expose
    private String bcaDate;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("callback")
    @Expose
    private String callback;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payType")
    @Expose
    private String payType;

    public String getBcaDescp() {
        return bcaDescp;
    }

    public void setBcaDescp(String bcaDescp) {
        this.bcaDescp = bcaDescp;
    }

    public String getBcaCode() {
        return bcaCode;
    }

    public void setBcaCode(String bcaCode) {
        this.bcaCode = bcaCode;
    }

    public String getBcaAmt() {
        return bcaAmt;
    }

    public void setBcaAmt(String bcaAmt) {
        this.bcaAmt = bcaAmt;
    }

    public String getBcaUrl() {
        return bcaUrl;
    }

    public void setBcaUrl(String bcaUrl) {
        this.bcaUrl = bcaUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMiscFee() {
        return miscFee;
    }

    public void setMiscFee(String miscFee) {
        this.miscFee = miscFee;
    }

    public String getBcaDate() {
        return bcaDate;
    }

    public void setBcaDate(String bcaDate) {
        this.bcaDate = bcaDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }


    protected BCAParam(Parcel in) {
        bcaDescp = in.readString();
        bcaCode = in.readString();
        bcaAmt = in.readString();
        bcaUrl = in.readString();
        currency = in.readString();
        miscFee = in.readString();
        bcaDate = in.readString();
        signature = in.readString();
        callback = in.readString();
        paymentId = in.readString();
        payType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bcaDescp);
        dest.writeString(bcaCode);
        dest.writeString(bcaAmt);
        dest.writeString(bcaUrl);
        dest.writeString(currency);
        dest.writeString(miscFee);
        dest.writeString(bcaDate);
        dest.writeString(signature);
        dest.writeString(callback);
        dest.writeString(paymentId);
        dest.writeString(payType);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BCAParam> CREATOR = new Parcelable.Creator<BCAParam>() {
        @Override
        public BCAParam createFromParcel(Parcel in) {
            return new BCAParam(in);
        }

        @Override
        public BCAParam[] newArray(int size) {
            return new BCAParam[size];
        }
    };
}
