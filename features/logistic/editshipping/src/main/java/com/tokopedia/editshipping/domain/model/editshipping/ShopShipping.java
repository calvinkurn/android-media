package com.tokopedia.editshipping.domain.model.editshipping;


import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kris on 3/2/2016.
 */
public class ShopShipping implements Parcelable{
    @SuppressLint("Invalid Data Type")
    @SerializedName("city_id")
    @Expose
    public Long cityId = 0L;
    @SuppressLint("Invalid Data Type")
    @SerializedName("shipping_id")
    @Expose
    public Object shippingId;
    @SerializedName("city_name")
    @Expose
    public String cityName;
    @SerializedName("addr_street")
    @Expose
    public String addrStreet;
    @SerializedName("province_name")
    @Expose
    public String provinceName;
    @SerializedName("district_name")
    @Expose
    public String districtName;
    @SuppressLint("Invalid Data Type")
    @SerializedName("province_id")
    @Expose
    public Long provinceId = 0L;
    @SerializedName("postal_code")
    @Expose
    public String postalCode;
    @SuppressLint("Invalid Data Type")
    @SerializedName("district_id")
    @Expose
    public Long districtId = 0L;
    @SerializedName("origin")
    @Expose
    public Integer origin;
    @SerializedName("longitude")
    @Expose
    public String longitude = "";
    @SerializedName("latitude")
    @Expose
    public String latitude = "";

    public String getShopLatitude(){
        return latitude;
    }

    public String getShopLongitude(){
        return longitude;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setShopProvinceName(String provinceName){
        this.provinceName = provinceName;
    }

    public void setShopCityName(String cityName){
        this.cityName = cityName;
    }

    public void setShopDistrictName(String districtName){
        this.districtName = districtName;
    }

    public void setShopProvinceId(Long provinceId){
        this.provinceId = provinceId;
    }

    public void setShopCityId(Long cityId){
        this.cityId = cityId;
    }

    public void setShopDistrictId(Long districtId){
        this.districtId = districtId;
    }

    public void setShopLatitude(String latitude){
        this.latitude = latitude;
    }

    public void setShopLongitude(String longitude){
        this.longitude = longitude;
    }

    public void setShopAddrStreet(String addressStreet){
        this.addrStreet = addressStreet;
    }

    public ShopShipping(){

    }

    protected ShopShipping(Parcel in) {
        cityId = in.readByte() == 0x00 ? null : in.readLong();
        shippingId = (Object) in.readValue(Object.class.getClassLoader());
        cityName = in.readString();
        addrStreet = in.readString();
        provinceName = in.readString();
        districtName = in.readString();
        provinceId = in.readByte() == 0x00 ? null : in.readLong();
        postalCode = in.readString();
        districtId = in.readByte() == 0x00 ? null : in.readLong();
        origin = in.readByte() == 0x00 ? null : in.readInt();
        longitude = in.readString();
        latitude = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (cityId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(cityId);
        }
        dest.writeValue(shippingId);
        dest.writeString(cityName);
        dest.writeString(addrStreet);
        dest.writeString(provinceName);
        dest.writeString(districtName);
        if (provinceId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(provinceId);
        }
        dest.writeString(postalCode);
        if (districtId == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(districtId);
        }
        if (origin == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(origin);
        }
        dest.writeString(longitude);
        dest.writeString(latitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShopShipping> CREATOR = new Parcelable.Creator<ShopShipping>() {
        @Override
        public ShopShipping createFromParcel(Parcel in) {
            return new ShopShipping(in);
        }

        @Override
        public ShopShipping[] newArray(int size) {
            return new ShopShipping[size];
        }
    };

}
