package com.tokopedia.logisticaddaddress.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Irfan Khoirul on 31/10/17.
 */

public class AddressEntity {

    @SerializedName("district_id")
    @Expose
    private long districtId;
    @SerializedName("district_name")
    @Expose
    private String districtName;
    @SerializedName("city_id")
    @Expose
    private long cityId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("province_id")
    @Expose
    private long provinceId;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("zip_code")
    @Expose
    private ArrayList<String> zipCodes;

    public long getDistrictId() {
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

    public long getCityId() {
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

    public long getProvinceId() {
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

}
