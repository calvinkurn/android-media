package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class EtdData implements Parcelable {

    @SerializedName("min_etd")
    @Expose
    private int minEtd;
    @SerializedName("max_etd")
    @Expose
    private int max_etd;

    public EtdData() {
    }

    protected EtdData(Parcel in) {
        minEtd = in.readInt();
        max_etd = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minEtd);
        dest.writeInt(max_etd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EtdData> CREATOR = new Creator<EtdData>() {
        @Override
        public EtdData createFromParcel(Parcel in) {
            return new EtdData(in);
        }

        @Override
        public EtdData[] newArray(int size) {
            return new EtdData[size];
        }
    };

    public int getMinEtd() {
        return minEtd;
    }

    public void setMinEtd(int minEtd) {
        this.minEtd = minEtd;
    }

    public int getMax_etd() {
        return max_etd;
    }

    public void setMax_etd(int max_etd) {
        this.max_etd = max_etd;
    }
}
