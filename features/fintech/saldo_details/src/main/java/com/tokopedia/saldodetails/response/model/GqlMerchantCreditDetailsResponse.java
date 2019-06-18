package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GqlMerchantCreditDetailsResponse implements Parcelable {

    @SerializedName("mcl_getmclstatus")
    private GqlMerchantCreditResponse data;

    protected GqlMerchantCreditDetailsResponse(Parcel in) {
        this.data = ((GqlMerchantCreditResponse) in.readValue((GqlMerchantCreditResponse.class.getClassLoader())));
    }

    public static final Creator<GqlMerchantCreditDetailsResponse> CREATOR = new Creator<GqlMerchantCreditDetailsResponse>() {
        @Override
        public GqlMerchantCreditDetailsResponse createFromParcel(Parcel in) {
            return new GqlMerchantCreditDetailsResponse(in);
        }

        @Override
        public GqlMerchantCreditDetailsResponse[] newArray(int size) {
            return new GqlMerchantCreditDetailsResponse[size];
        }
    };

    public GqlMerchantCreditResponse getData() {
        return data;
    }

    public void setData(GqlMerchantCreditResponse data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }
}
