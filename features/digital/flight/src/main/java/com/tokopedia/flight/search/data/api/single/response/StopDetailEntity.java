package com.tokopedia.flight.search.data.api.single.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StopDetailEntity implements Parcelable{
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("city")
    @Expose
    private String city;

    public StopDetailEntity() {
    }

    protected StopDetailEntity(Parcel in) {
        code = in.readString();
        city = in.readString();
    }

    public static final Creator<StopDetailEntity> CREATOR = new Creator<StopDetailEntity>() {
        @Override
        public StopDetailEntity createFromParcel(Parcel in) {
            return new StopDetailEntity(in);
        }

        @Override
        public StopDetailEntity[] newArray(int size) {
            return new StopDetailEntity[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(city);
    }
}
