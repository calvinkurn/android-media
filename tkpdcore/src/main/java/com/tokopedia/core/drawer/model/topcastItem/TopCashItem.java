
package com.tokopedia.core.drawer.model.topcastItem;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TopCashItem implements Parcelable{

    @SerializedName("code")
    private String mCode;
    @SerializedName("data")
    private Data mData;
    @SerializedName("error")
    private String error;

    protected TopCashItem(Parcel in) {
        mCode = in.readString();
        mData = in.readParcelable(Data.class.getClassLoader());
        error = in.readString();
    }

    public static final Creator<TopCashItem> CREATOR = new Creator<TopCashItem>() {
        @Override
        public TopCashItem createFromParcel(Parcel in) {
            return new TopCashItem(in);
        }

        @Override
        public TopCashItem[] newArray(int size) {
            return new TopCashItem[size];
        }
    };

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCode);
        dest.writeParcelable(mData, flags);
        dest.writeString(error);
    }
}
