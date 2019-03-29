package com.tokopedia.logisticdata.data.entity.address.db;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by m.normansyah on 2/2/16.
 * modified by m.normansyah on 6/10/2016
 */
public class Province implements Parcelable {


    public Province() {
    }

    public long Id;
    public String provinceId;
    public String provinceName;
    public long expiredTime = 0;

    List<City> cities;

    protected Province(Parcel in) {
        Id = in.readLong();
        provinceId = in.readString();
        provinceName = in.readString();
        expiredTime = in.readLong();
    }

    public static final Creator<Province> CREATOR = new Creator<Province>() {
        @Override
        public Province createFromParcel(Parcel in) {
            return new Province(in);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

    public String getProvinceId() {
        return provinceId;
    }

    public long getId() {
        return Id;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public String toString() {
        return "Province{" +
                "provinceId='" + provinceId + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", expiredTime=" + expiredTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeString(provinceId);
        dest.writeString(provinceName);
        dest.writeLong(expiredTime);
    }
}
