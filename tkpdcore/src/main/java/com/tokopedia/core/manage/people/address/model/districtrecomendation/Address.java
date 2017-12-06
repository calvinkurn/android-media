package com.tokopedia.core.manage.people.address.model.districtrecomendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class Address implements Parcelable {

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
    @SerializedName("district_id")
    @Expose
    private int districtId;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("city_id")
    @Expose
    private int cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("province_id")
    @Expose
    private int provinceId;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("zip_code")
    @Expose
    private ArrayList<String> zipCodes;

    protected Address(Parcel in) {
        districtId = in.readInt();
        districtName = in.readString();
        cityId = in.readInt();
        cityName = in.readString();
        provinceId = in.readInt();
        provinceName = in.readString();
        zipCodes = in.createStringArrayList();
    }

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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(districtId);
        parcel.writeString(districtName);
        parcel.writeInt(cityId);
        parcel.writeString(cityName);
        parcel.writeInt(provinceId);
        parcel.writeString(provinceName);
        parcel.writeStringList(zipCodes);
    }
}
