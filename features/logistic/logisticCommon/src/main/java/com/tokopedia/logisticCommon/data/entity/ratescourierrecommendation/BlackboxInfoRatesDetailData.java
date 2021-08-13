package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 05/03/19.
 */
public class BlackboxInfoRatesDetailData implements Parcelable {

    @SerializedName("text_info")
    @Expose
    private String textInfo;

    public BlackboxInfoRatesDetailData() {
    }

    protected BlackboxInfoRatesDetailData(Parcel in) {
        textInfo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BlackboxInfoRatesDetailData> CREATOR = new Creator<BlackboxInfoRatesDetailData>() {
        @Override
        public BlackboxInfoRatesDetailData createFromParcel(Parcel in) {
            return new BlackboxInfoRatesDetailData(in);
        }

        @Override
        public BlackboxInfoRatesDetailData[] newArray(int size) {
            return new BlackboxInfoRatesDetailData[size];
        }
    };

    public String getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(String textInfo) {
        this.textInfo = textInfo;
    }
}
