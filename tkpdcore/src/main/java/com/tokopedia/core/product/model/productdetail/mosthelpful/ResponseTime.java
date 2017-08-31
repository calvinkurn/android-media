
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseTime implements Parcelable {

    @SerializedName("date_time_fmt1")
    @Expose
    private String dateTimeFmt1;

    public String getDateTimeFmt1() {
        return dateTimeFmt1;
    }

    public void setDateTimeFmt1(String dateTimeFmt1) {
        this.dateTimeFmt1 = dateTimeFmt1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dateTimeFmt1);
    }

    public ResponseTime() {
    }

    protected ResponseTime(Parcel in) {
        this.dateTimeFmt1 = in.readString();
    }

    public static final Parcelable.Creator<ResponseTime> CREATOR = new Parcelable.Creator<ResponseTime>() {
        @Override
        public ResponseTime createFromParcel(Parcel source) {
            return new ResponseTime(source);
        }

        @Override
        public ResponseTime[] newArray(int size) {
            return new ResponseTime[size];
        }
    };
}
