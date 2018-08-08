package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class GetRatesCourierRecommendationData implements Parcelable {

    @SerializedName("ratesV3")
    @Expose
    private RatesData ratesData;

    public GetRatesCourierRecommendationData() {
    }

    protected GetRatesCourierRecommendationData(Parcel in) {
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

    public static final Creator<GetRatesCourierRecommendationData> CREATOR = new Creator<GetRatesCourierRecommendationData>() {
        @Override
        public GetRatesCourierRecommendationData createFromParcel(Parcel in) {
            return new GetRatesCourierRecommendationData(in);
        }

        @Override
        public GetRatesCourierRecommendationData[] newArray(int size) {
            return new GetRatesCourierRecommendationData[size];
        }
    };

    public RatesData getRatesData() {
        return ratesData;
    }

    public void setRatesData(RatesData ratesData) {
        this.ratesData = ratesData;
    }

}
