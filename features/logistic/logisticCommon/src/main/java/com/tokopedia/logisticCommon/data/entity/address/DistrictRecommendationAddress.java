package com.tokopedia.logisticCommon.data.entity.address;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DistrictRecommendationAddress implements Parcelable {

    private long districtId;
    private String districtName;
    private long cityId;
    private String cityName;
    private long provinceId;
    private String provinceName;
    private ArrayList<String> zipCodes;

    public DistrictRecommendationAddress() {

    }

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

    public static Creator<DistrictRecommendationAddress> getCREATOR() {
        return CREATOR;
    }

    protected DistrictRecommendationAddress(Parcel in) {
        districtId = in.readLong();
        districtName = in.readString();
        cityId = in.readLong();
        cityName = in.readString();
        provinceId = in.readLong();
        provinceName = in.readString();
        zipCodes = in.createStringArrayList();
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DistrictRecommendationAddress> CREATOR = new Creator<DistrictRecommendationAddress>() {
        @Override
        public DistrictRecommendationAddress createFromParcel(Parcel in) {
            return new DistrictRecommendationAddress(in);
        }

        @Override
        public DistrictRecommendationAddress[] newArray(int size) {
            return new DistrictRecommendationAddress[size];
        }
    };
}
