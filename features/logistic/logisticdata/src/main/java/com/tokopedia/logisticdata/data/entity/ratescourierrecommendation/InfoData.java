package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 05/03/19.
 */
public class InfoData implements Parcelable {

    @SerializedName("info")
    @Expose
    private BlackboxInfoData info;

    public InfoData() {
    }

    protected InfoData(Parcel in) {
        info = in.readParcelable(BlackboxInfoData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(info, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InfoData> CREATOR = new Creator<InfoData>() {
        @Override
        public InfoData createFromParcel(Parcel in) {
            return new InfoData(in);
        }

        @Override
        public InfoData[] newArray(int size) {
            return new InfoData[size];
        }
    };

    public BlackboxInfoData getInfo() {
        return info;
    }

    public void setInfo(BlackboxInfoData info) {
        this.info = info;
    }
}
