package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fwidjaja on 05/03/19.
 */
public class BlackboxInfoData implements Parcelable {

    @SerializedName("blackbox_info")
    @Expose
    private String blackboxInfo;

    public BlackboxInfoData() {
    }

    protected BlackboxInfoData(Parcel in) {
        blackboxInfo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(blackboxInfo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BlackboxInfoData> CREATOR = new Creator<BlackboxInfoData>() {
        @Override
        public BlackboxInfoData createFromParcel(Parcel in) {
            return new BlackboxInfoData(in);
        }

        @Override
        public BlackboxInfoData[] newArray(int size) {
            return new BlackboxInfoData[size];
        }
    };

    public String getBlackboxInfo() {
        return blackboxInfo;
    }

    public void setBlackboxInfo(String blackboxInfo) {
        this.blackboxInfo = blackboxInfo;
    }
}
