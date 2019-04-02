
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewUpdateTime implements Parcelable {

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

    public ReviewUpdateTime() {
    }

    protected ReviewUpdateTime(Parcel in) {
        this.dateTimeFmt1 = in.readString();
    }

    public static final Creator<ReviewUpdateTime> CREATOR = new Creator<ReviewUpdateTime>() {
        @Override
        public ReviewUpdateTime createFromParcel(Parcel source) {
            return new ReviewUpdateTime(source);
        }

        @Override
        public ReviewUpdateTime[] newArray(int size) {
            return new ReviewUpdateTime[size];
        }
    };
}
