package com.tokopedia.logisticaddaddress.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.logisticaddaddress.features.district_recommendation.adapter.DistrictTypeFactory;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class Address implements Parcelable, Visitable<DistrictTypeFactory> {

    private long districtId;
    private String districtName;
    private long cityId;
    private String cityName;
    private long provinceId;
    private String provinceName;
    private ArrayList<String> zipCodes;

    public Address() {
    }

    protected Address(Parcel in) {
        districtId = in.readLong();
        districtName = in.readString();
        cityId = in.readLong();
        cityName = in.readString();
        provinceId = in.readLong();
        provinceName = in.readString();
        zipCodes = in.createStringArrayList();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public ArrayList<String> getZipCodes() {
        return zipCodes;
    }

    public void setZipCodes(ArrayList<String> zipCodes) {
        this.zipCodes = zipCodes;
    }

    @Override
    public int type(DistrictTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(districtId);
        dest.writeString(districtName);
        dest.writeLong(cityId);
        dest.writeString(cityName);
        dest.writeLong(provinceId);
        dest.writeString(provinceName);
        dest.writeStringList(zipCodes);
        dest.writeList(zipCodes);
    }
}
