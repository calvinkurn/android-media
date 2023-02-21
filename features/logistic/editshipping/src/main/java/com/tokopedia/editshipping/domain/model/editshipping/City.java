package com.tokopedia.editshipping.domain.model.editshipping;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 3/2/2016.
 */
public class City implements Parcelable {

    @SerializedName("city_id")
    @Expose
    public long cityId;
    @SerializedName("districts")
    @Expose
    public List<District> districts;
    @SerializedName("city_name")
    @Expose
    public String cityName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.cityId);
        dest.writeTypedList(this.districts);
        dest.writeString(this.cityName);
    }

    public City() {
        cityId = 0;
        districts = new ArrayList<>();
    }

    protected City(Parcel in) {
        this.cityId = (Long) in.readValue(Long.class.getClassLoader());
        this.districts = in.createTypedArrayList(District.CREATOR);
        this.cityName = in.readString();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
