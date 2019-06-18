package com.tokopedia.saldodetails.response.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class GqlMerchantSaldoDetailsResponse implements Parcelable {

    @SerializedName("sp_getmerchantstatus")
    private GqlDetailsResponse data;

    protected GqlMerchantSaldoDetailsResponse(Parcel in) {
        this.data = ((GqlDetailsResponse) in.readValue((GqlDetailsResponse.class.getClassLoader())));
    }

    public static final Creator<GqlMerchantSaldoDetailsResponse> CREATOR = new Creator<GqlMerchantSaldoDetailsResponse>() {
        @Override
        public GqlMerchantSaldoDetailsResponse createFromParcel(Parcel in) {
            return new GqlMerchantSaldoDetailsResponse(in);
        }

        @Override
        public GqlMerchantSaldoDetailsResponse[] newArray(int size) {
            return new GqlMerchantSaldoDetailsResponse[size];
        }
    };

    public GqlDetailsResponse getData() {
        return data;
    }

    public void setData(GqlDetailsResponse data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}
