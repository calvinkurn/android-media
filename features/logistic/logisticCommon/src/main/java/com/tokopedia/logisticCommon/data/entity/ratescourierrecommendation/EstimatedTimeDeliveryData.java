package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class EstimatedTimeDeliveryData implements Parcelable {

    @SerializedName("min_etd")
    @Expose
    private int minEtd;
    @SerializedName("max_etd")
    @Expose
    private int maxEtd;

    public EstimatedTimeDeliveryData() {
    }

    protected EstimatedTimeDeliveryData(Parcel in) {
        minEtd = in.readInt();
        maxEtd = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minEtd);
        dest.writeInt(maxEtd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EstimatedTimeDeliveryData> CREATOR = new Creator<EstimatedTimeDeliveryData>() {
        @Override
        public EstimatedTimeDeliveryData createFromParcel(Parcel in) {
            return new EstimatedTimeDeliveryData(in);
        }

        @Override
        public EstimatedTimeDeliveryData[] newArray(int size) {
            return new EstimatedTimeDeliveryData[size];
        }
    };

    public int getMinEtd() {
        return minEtd;
    }

    public void setMinEtd(int minEtd) {
        this.minEtd = minEtd;
    }

    public int getMaxEtd() {
        return maxEtd;
    }

    public void setMaxEtd(int maxEtd) {
        this.maxEtd = maxEtd;
    }
}
