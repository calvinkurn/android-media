package com.tokopedia.checkout.domain.datamodel.cartshipmentform;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class UserAddress implements Parcelable {
    private int addressId;
    private String addressName;
    private String address;
    private String postalCode;
    private String phone;
    private String receiverName;
    private int status;
    private String country;
    private int provinceId;
    private String provinceName;
    private int cityId;
    private String cityName;
    private int districtId;
    private String districtName;
    private String address2;
    private String latitude;
    private String longitude;
    private String cornerId;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCornerId() {
        return cornerId;
    }

    public void setCornerId(String cornerId) {
        this.cornerId = cornerId;
    }

    public UserAddress() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.addressId);
        dest.writeString(this.addressName);
        dest.writeString(this.address);
        dest.writeString(this.postalCode);
        dest.writeString(this.phone);
        dest.writeString(this.receiverName);
        dest.writeInt(this.status);
        dest.writeString(this.country);
        dest.writeInt(this.provinceId);
        dest.writeString(this.provinceName);
        dest.writeInt(this.cityId);
        dest.writeString(this.cityName);
        dest.writeInt(this.districtId);
        dest.writeString(this.districtName);
        dest.writeString(this.address2);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.cornerId);
    }

    protected UserAddress(Parcel in) {
        this.addressId = in.readInt();
        this.addressName = in.readString();
        this.address = in.readString();
        this.postalCode = in.readString();
        this.phone = in.readString();
        this.receiverName = in.readString();
        this.status = in.readInt();
        this.country = in.readString();
        this.provinceId = in.readInt();
        this.provinceName = in.readString();
        this.cityId = in.readInt();
        this.cityName = in.readString();
        this.districtId = in.readInt();
        this.districtName = in.readString();
        this.address2 = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.cornerId = in.readString();
    }

    public static final Creator<UserAddress> CREATOR = new Creator<UserAddress>() {
        @Override
        public UserAddress createFromParcel(Parcel source) {
            return new UserAddress(source);
        }

        @Override
        public UserAddress[] newArray(int size) {
            return new UserAddress[size];
        }
    };
}
