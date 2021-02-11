package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

public class RatesData implements Parcelable {

    @SerializedName("ratesv3")
    @Expose
    private RatesDetailData ratesDetailData;

    public RatesData() {
    }

    protected RatesData(Parcel in) {
        ratesDetailData = in.readParcelable(RatesDetailData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ratesDetailData, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RatesData> CREATOR = new Creator<RatesData>() {
        @Override
        public RatesData createFromParcel(Parcel in) {
            return new RatesData(in);
        }

        @Override
        public RatesData[] newArray(int size) {
            return new RatesData[size];
        }
    };

    public RatesDetailData getRatesDetailData() {
        return ratesDetailData;
    }

    public void setRatesDetailData(RatesDetailData ratesDetailData) {
        this.ratesDetailData = ratesDetailData;
    }

}
