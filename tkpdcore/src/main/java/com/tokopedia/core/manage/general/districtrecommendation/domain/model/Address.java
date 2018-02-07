package com.tokopedia.core.manage.general.districtrecommendation.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 17/11/17.
 */

public class Address implements Parcelable {

    private int districtId;
    private String districtName;
    private int cityId;
    private String cityName;
    private int provinceId;
    private String provinceName;
    private ArrayList<String> zipCodes;

    public Address() {
    }

    protected Address(Parcel in) {
        districtId = in.readInt();
        districtName = in.readString();
        cityId = in.readInt();
        cityName = in.readString();
        provinceId = in.readInt();
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

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(districtId);
        dest.writeString(districtName);
        dest.writeInt(cityId);
        dest.writeString(cityName);
        dest.writeInt(provinceId);
        dest.writeString(provinceName);
        dest.writeStringList(zipCodes);
        dest.writeList(zipCodes);
    }
}
