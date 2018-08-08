package com.tokopedia.payment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kris on 3/14/17. Tokopedia
 */

public class PaymentPassData implements Parcelable {

    private String paymentId;
    private String queryString;
    private String redirectUrl;
    private String callbackSuccessUrl;
    private String callbackFailedUrl;
    private String transactionId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getCallbackSuccessUrl() {
        return callbackSuccessUrl;
    }

    public void setCallbackSuccessUrl(String callbackSuccessUrl) {
        this.callbackSuccessUrl = callbackSuccessUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCallbackFailedUrl() {
        return callbackFailedUrl;
    }

    public void setCallbackFailedUrl(String callbackFailedUrl) {
        this.callbackFailedUrl = callbackFailedUrl;
    }

    public void convertToPaymenPassData(TopPayBaseModel data) {
        queryString = data.getQueryStringToPass();
        redirectUrl = data.getRedirectUrlToPass();
        callbackSuccessUrl = data.getCallbackSuccessUrlToPass();
        transactionId = data.getTransactionIdToPass();
        callbackFailedUrl = data.getCallbackFailedUrlToPass();
    }

    public PaymentPassData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.paymentId);
        dest.writeString(this.queryString);
        dest.writeString(this.redirectUrl);
        dest.writeString(this.callbackSuccessUrl);
        dest.writeString(this.callbackFailedUrl);
        dest.writeString(this.transactionId);
    }

    protected PaymentPassData(Parcel in) {
        this.paymentId = in.readString();
        this.queryString = in.readString();
        this.redirectUrl = in.readString();
        this.callbackSuccessUrl = in.readString();
        this.callbackFailedUrl = in.readString();
        this.transactionId = in.readString();
    }

    public static final Creator<PaymentPassData> CREATOR =
            new Creator<PaymentPassData>() {
                @Override
                public PaymentPassData createFromParcel(Parcel source) {
                    return new PaymentPassData(source);
                }

                @Override
                public PaymentPassData[] newArray(int size) {
                    return new PaymentPassData[size];
                }
            };

    @Override
    public String toString() {
        return "Payment ID = " + paymentId + " | " + "Redirect URL = " + redirectUrl;
    }
}
