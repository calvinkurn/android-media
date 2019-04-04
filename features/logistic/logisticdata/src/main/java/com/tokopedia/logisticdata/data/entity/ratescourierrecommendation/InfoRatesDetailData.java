package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 05/03/19.
 */
public class InfoRatesDetailData implements Parcelable {

    @SerializedName("blackbox_info")
    @Expose
    private BlackboxInfoRatesDetailData blackboxInfo;

    public InfoRatesDetailData() {
    }

    protected InfoRatesDetailData(Parcel in) {
        blackboxInfo = in.readParcelable(BlackboxInfoRatesDetailData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(blackboxInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InfoRatesDetailData> CREATOR = new Creator<InfoRatesDetailData>() {
        @Override
        public InfoRatesDetailData createFromParcel(Parcel in) {
            return new InfoRatesDetailData(in);
        }

        @Override
        public InfoRatesDetailData[] newArray(int size) {
            return new InfoRatesDetailData[size];
        }
    };

    public BlackboxInfoRatesDetailData getBlackboxInfo() {
        return blackboxInfo;
    }

    public void setBlackboxInfo(BlackboxInfoRatesDetailData blackboxInfo) {
        this.blackboxInfo = blackboxInfo;
    }
}
