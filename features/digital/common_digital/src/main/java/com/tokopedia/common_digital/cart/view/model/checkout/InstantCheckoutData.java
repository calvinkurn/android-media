package com.tokopedia.common_digital.cart.view.model.checkout;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 3/23/17.
 */

public class InstantCheckoutData implements Parcelable {

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

    public InstantCheckoutData() {
    }

    protected InstantCheckoutData(Parcel in) {
        this.successCallbackUrl = in.readString();
        this.failedCallbackUrl = in.readString();
        this.redirectUrl = in.readString();
        this.transactionId = in.readString();
        this.stringQuery = in.readString();
    }

    public static final Creator<InstantCheckoutData> CREATOR =
            new Creator<InstantCheckoutData>() {
                @Override
                public InstantCheckoutData createFromParcel(Parcel source) {
                    return new InstantCheckoutData(source);
                }

                @Override
                public InstantCheckoutData[] newArray(int size) {
                    return new InstantCheckoutData[size];
                }
            };
}
