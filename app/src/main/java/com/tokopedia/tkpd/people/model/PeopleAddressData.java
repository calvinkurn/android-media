package com.tokopedia.tkpd.people.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 6/1/16.
 */
public class PeopleAddressData {

    @SerializedName("paging")
    private Paging paging;
    @SerializedName("list")
    private List<PeopleAddress> list;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<PeopleAddress> getList() {
        return list;
    }

    public void setList(List<PeopleAddress> list) {
        this.list = list;
    }

    public static class Paging {

        @SerializedName("uri_next")
        private String uriNext;
        @SerializedName("uri_previous")
        private String uriPrevious;

        public String getUriNext() {
            return uriNext;
        }

        public void setUriNext(String uriNext) {
            this.uriNext = uriNext;
        }

        public String getUriPrevious() {
            return uriPrevious;
        }

        public void setUriPrevious(String uriPrevious) {
            this.uriPrevious = uriPrevious;
        }

    }

    public static class PeopleAddress {
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("receiver_phone")
        private String receiverPhone;
        @SerializedName("address_street")
        private String addressStreet;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("postal_code")
        private String postalCode;
        @SerializedName("district_id")
        private String districtId;
        @SerializedName("address_name")
        private String addressName;
        @SerializedName("country_name")
        private String countryName;
        @SerializedName("receiver_name")
        private String receiverName;
        @SerializedName("city_id")
        private String cityId;
        @SerializedName("city_name")
        private String cityName;
        @SerializedName("province_id")
        private String provinceId;
        @SerializedName("district_name")
        private String districtName;
        @SerializedName("province_name")
        private String provinceName;
        @SerializedName("address_id")
        private String addressId;
        @SerializedName("address_status")
        private int addressStatus;

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getReceiverPhone() {
            return receiverPhone;
        }

        public void setReceiverPhone(String receiverPhone) {
            this.receiverPhone = receiverPhone;
        }

        public String getAddressStreet() {
            return addressStreet;
        }

        public void setAddressStreet(String addressStreet) {
            this.addressStreet = addressStreet;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
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

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getDistrictName() {
            return districtName;
        }

        public void setDistrictName(String districtName) {
            this.districtName = districtName;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public int getAddressStatus() {
            return addressStatus;
        }

        public void setAddressStatus(int addressStatus) {
            this.addressStatus = addressStatus;
        }

        public String getPeopleAddress() {
            return getDistrictName() + ", " +
                    getCityName() + ", " +
                    getProvinceName() + ", " +
                    getCountryName();
        }
    }
}
