package com.tokopedia.logisticCommon.data.entity.address.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by noiz354 on 2/2/16.
 * modified by m.normansyah on 6/10/2016
 */
public class City implements Parcelable {

    List<District> districts;
    private Province province;
    public String cityId;
    public String cityName;
    public long Id;

    public City() {
    }

    public long getId() {
        return Id;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "City{" +
                "province=" + province +
                ", cityId='" + cityId + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected City(Parcel in) {
        districts = in.createTypedArrayList(District.CREATOR);
        province = in.readParcelable(Province.class.getClassLoader());
        cityId = in.readString();
        cityName = in.readString();
        Id = in.readLong();
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(districts);
        dest.writeParcelable(province, flags);
        dest.writeString(cityId);
        dest.writeString(cityName);
        dest.writeLong(Id);
    }
}
