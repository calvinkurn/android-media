package com.tokopedia.editshipping.domain.model.editshipping;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kris on 3/2/2016.
 */
public class ProvinceCitiesDistrict implements Parcelable {
    @SerializedName("province_id")
    @Expose
    public long provinceId;
    @SerializedName("cities")
    @Expose
    public List<City> cities;
    @SerializedName("province_name")
    @Expose
    public String provinceName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.provinceId);
        dest.writeTypedList(this.cities);
        dest.writeString(this.provinceName);
    }

    public ProvinceCitiesDistrict() {
        provinceId = 0;
        cities = new ArrayList<>();
    }

    protected ProvinceCitiesDistrict(Parcel in) {
        this.provinceId = (Long) in.readValue(Long.class.getClassLoader());
        this.cities = in.createTypedArrayList(City.CREATOR);
        this.provinceName = in.readString();
    }

    public static final Parcelable.Creator<ProvinceCitiesDistrict> CREATOR = new Parcelable.Creator<ProvinceCitiesDistrict>() {
        @Override
        public ProvinceCitiesDistrict createFromParcel(Parcel source) {
            return new ProvinceCitiesDistrict(source);
        }

        @Override
        public ProvinceCitiesDistrict[] newArray(int size) {
            return new ProvinceCitiesDistrict[size];
        }
    };
}
