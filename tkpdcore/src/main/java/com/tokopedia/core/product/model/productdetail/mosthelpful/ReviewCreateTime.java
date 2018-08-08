
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewCreateTime implements Parcelable {

    @SerializedName("date_time_android")
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

    public ReviewCreateTime() {
    }

    protected ReviewCreateTime(Parcel in) {
        this.dateTimeFmt1 = in.readString();
    }

    public static final Parcelable.Creator<ReviewCreateTime> CREATOR = new Parcelable.Creator<ReviewCreateTime>() {
        @Override
        public ReviewCreateTime createFromParcel(Parcel source) {
            return new ReviewCreateTime(source);
        }

        @Override
        public ReviewCreateTime[] newArray(int size) {
            return new ReviewCreateTime[size];
        }
    };
}
