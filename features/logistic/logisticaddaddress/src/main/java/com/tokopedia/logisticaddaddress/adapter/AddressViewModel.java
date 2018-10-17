package com.tokopedia.logisticaddaddress.adapter;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.logisticdata.data.entity.address.AddressModel;

/**
 * Created by Fajar Ulin Nuha on 15/10/18.
 */
public class AddressViewModel implements Visitable<AddressTypeFactory> {


    private String addressId;

    private String receiverPhone;

    private Integer addressStatus;

    private String postalCode;

    private String latitude;

    private String addressStreet;

    private String cityName;

    private String districtId;

    private String cityId;

    private String countryName;

    private String longitude;

    private String provinceId;

    private String addressName;

    private String receiverName;

    private String provinceName;

    private String districtName;

    public AddressViewModel() {
    }

    public AddressViewModel(String addressId, String receiverPhone, Integer addressStatus, String postalCode, String latitude, String addressStreet, String cityName, String districtId, String cityId, String countryName, String longitude, String provinceId, String addressName, String receiverName, String provinceName, String districtName) {
        this.addressId = addressId;
        this.receiverPhone = receiverPhone;
        this.addressStatus = addressStatus;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.addressStreet = addressStreet;
        this.cityName = cityName;
        this.districtId = districtId;
        this.cityId = cityId;
        this.countryName = countryName;
        this.longitude = longitude;
        this.provinceId = provinceId;
        this.addressName = addressName;
        this.receiverName = receiverName;
        this.provinceName = provinceName;
        this.districtName = districtName;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
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

    public Integer getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(Integer addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddressFull() {
        return getReceiverName() + "\n" +
                getAddressStreet() + "\n" +
                getDistrictName() + ", " +
                getCityName() + ", " +
                getPostalCode() + "\n" +
                getProvinceName() + "\n" +
                getReceiverPhone();
    }

    @Override
    public int type(AddressTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    // Verify this later
    public boolean equals(AddressModel addressModel) {
        return addressModel.getAddressId().equals(this.addressId);
    }
}
