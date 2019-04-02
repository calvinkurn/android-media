package com.tokopedia.payment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 2/2/17.
 */

public class PaymentPassData implements Parcelable {

    private String paymentId;
    private String queryString;
    private String redirectUrl;
    private String callbackUrl;

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

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
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
        dest.writeString(this.callbackUrl);
    }

    public PaymentPassData() {
    }

    protected PaymentPassData(Parcel in) {
        this.paymentId = in.readString();
        this.queryString = in.readString();
        this.redirectUrl = in.readString();
        this.callbackUrl = in.readString();
    }

    public static final Parcelable.Creator<PaymentPassData> CREATOR =
            new Parcelable.Creator<PaymentPassData>() {
                @Override
                public PaymentPassData createFromParcel(Parcel source) {
                    return new PaymentPassData(source);
                }

                @Override
                public PaymentPassData[] newArray(int size) {
                    return new PaymentPassData[size];
                }
            };
}
