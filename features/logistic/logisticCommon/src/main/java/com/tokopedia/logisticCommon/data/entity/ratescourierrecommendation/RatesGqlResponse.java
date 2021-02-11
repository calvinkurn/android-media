package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class RatesGqlResponse implements Parcelable {

    @SerializedName("ratesV3")
    @Expose
    private RatesData ratesData;

    public RatesGqlResponse() {
    }

    protected RatesGqlResponse(Parcel in) {
        ratesData = in.readParcelable(RatesData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ratesData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RatesGqlResponse> CREATOR = new Creator<RatesGqlResponse>() {
        @Override
        public RatesGqlResponse createFromParcel(Parcel in) {
            return new RatesGqlResponse(in);
        }

        @Override
        public RatesGqlResponse[] newArray(int size) {
            return new RatesGqlResponse[size];
        }
    };

    public RatesData getRatesData() {
        return ratesData;
    }

    public void setRatesData(RatesData ratesData) {
        this.ratesData = ratesData;
    }

}
