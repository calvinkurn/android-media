package com.tokopedia.digital.cart.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.payment.model.TopPayBaseModel;


/**
 * @author anggaprasetiyo on 3/9/17.
 */

public class CheckoutDigitalData implements Parcelable, TopPayBaseModel {

    private String successCallbackUrl;
    private String failedCallbackUrl;
    private String redirectUrl;
    private String transactionId;
    private String stringQuery;

    public String getSuccessCallbackUrl() {
        return successCallbackUrl;
    }

    public void setSuccessCallbackUrl(String successCallbackUrl) {
        this.successCallbackUrl = successCallbackUrl;
    }

    public String getFailedCallbackUrl() {
        return failedCallbackUrl;
    }

    public void setFailedCallbackUrl(String failedCallbackUrl) {
        this.failedCallbackUrl = failedCallbackUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStringQuery() {
        return stringQuery;
    }

    public void setStringQuery(String stringQuery) {
        this.stringQuery = stringQuery;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.successCallbackUrl);
        dest.writeString(this.failedCallbackUrl);
        dest.writeString(this.redirectUrl);
        dest.writeString(this.transactionId);
        dest.writeString(this.stringQuery);
    }

    public CheckoutDigitalData() {
    }

    protected CheckoutDigitalData(Parcel in) {
        this.successCallbackUrl = in.readString();
        this.failedCallbackUrl = in.readString();
        this.redirectUrl = in.readString();
        this.transactionId = in.readString();
        this.stringQuery = in.readString();
    }

    public static final Parcelable.Creator<CheckoutDigitalData> CREATOR =
            new Parcelable.Creator<CheckoutDigitalData>() {
                @Override
                public CheckoutDigitalData createFromParcel(Parcel source) {
                    return new CheckoutDigitalData(source);
                }

                @Override
                public CheckoutDigitalData[] newArray(int size) {
                    return new CheckoutDigitalData[size];
                }
            };

    @Override
    public String getRedirectUrlToPass() {
        return redirectUrl;
    }

    @Override
    public String getQueryStringToPass() {
        return stringQuery;
    }

    @Override
    public String getCallbackSuccessUrlToPass() {
        return successCallbackUrl;
    }

    @Override
    public String getCallbackFailedUrlToPass() {
        return failedCallbackUrl;
    }

    @Override
    public String getTransactionIdToPass() {
        return transactionId;
    }
}
