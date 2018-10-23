package com.tokopedia.core.product.model.productdetail.discussion;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alifa on 8/24/17.
 */

public class TalkCreateTimeList implements Parcelable {

    @SerializedName("date_time_android")
    @Expose
    private String dateTimeAndroid;

    public String getDateTimeAndroid() {
        return dateTimeAndroid;
    }

    public void setDateTimeAndroid(String dateTimeAndroid) {
        this.dateTimeAndroid = dateTimeAndroid;
    }

    protected TalkCreateTimeList(Parcel in) {
        dateTimeAndroid = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateTimeAndroid);
    }

    @SuppressWarnings("unused")
    public static final Creator<TalkCreateTimeList> CREATOR = new Creator<TalkCreateTimeList>() {
        @Override
        public TalkCreateTimeList createFromParcel(Parcel in) {
            return new TalkCreateTimeList(in);
        }

        @Override
        public TalkCreateTimeList[] newArray(int size) {
            return new TalkCreateTimeList[size];
        }
    };
}