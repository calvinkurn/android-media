package com.tokopedia.logisticCommon.data.entity.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class AddressModel implements Parcelable {

    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("address_status")
    @Expose
    private Integer addressStatus;
    @SerializedName("postal_code")
    @Expose
    private String postalCode;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("address_street")
    @Expose
    private String addressStreet;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
    @SerializedName("district_name")
    @Expose
    private String districtName;

    public AddressModel(String addressId, String receiverPhone, Integer addressStatus, String postalCode, String latitude, String addressStreet, String cityName, String districtId, String cityId, String countryName, String longitude, String provinceId, String addressName, String receiverName, String provinceName, String districtName) {
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

    /**
     * @return The addressId
     */
    public String getAddressId() {
        return addressId;
    }

    /**
     * @param addressId The address_id
     */
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    /**
     * @return The receiverPhone
     */
    public String getReceiverPhone() {
        return receiverPhone;
    }

    /**
     * @param receiverPhone The receiver_phone
     */
    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    /**
     * @return The addressStatus
     */
    public Integer getAddressStatus() {
        return addressStatus;
    }

    /**
     * @param addressStatus The address_status
     */
    public void setAddressStatus(Integer addressStatus) {
        this.addressStatus = addressStatus;
    }

    /**
     * @return The postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode The postal_code
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * @param latitude The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * @return The addressStreet
     */
    public String getAddressStreet() {
        return MethodChecker.fromHtml(addressStreet).toString();
    }

    /**
     * @param addressStreet The address_street
     */
    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    /**
     * @return The cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * @param cityName The city_name
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * @return The districtId
     */
    public String getDistrictId() {
        return districtId;
    }

    /**
     * @param districtId The district_id
     */
    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    /**
     * @return The cityId
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * @param cityId The city_id
     */
    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * @return The countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @param countryName The country_name
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * @return The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * @param longitude The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * @return The provinceId
     */
    public String getProvinceId() {
        return provinceId;
    }

    /**
     * @param provinceId The province_id
     */
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    /**
     * @return The addressName
     */
    public String getAddressName() {
        return MethodChecker.fromHtml(addressName).toString();
    }

    /**
     * @param addressName The address_name
     */
    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    /**
     * @return The receiverName
     */
    public String getReceiverName() {
        return MethodChecker.fromHtml(receiverName).toString();
    }

    /**
     * @param receiverName The receiver_name
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * @return The provinceName
     */
    public String getProvinceName() {
        return provinceName;
    }

    /**
     * @param provinceName The province_name
     */
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    /**
     * @return The districtName
     */
    public String getDistrictName() {
        return districtName;
    }

    /**
     * @param districtName The district_name
     */
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressId);
        dest.writeString(this.receiverPhone);
        dest.writeValue(this.addressStatus);
        dest.writeString(this.postalCode);
        dest.writeString(this.latitude);
        dest.writeString(this.addressStreet);
        dest.writeString(this.cityName);
        dest.writeString(this.districtId);
        dest.writeString(this.cityId);
        dest.writeString(this.countryName);
        dest.writeString(this.longitude);
        dest.writeString(this.provinceId);
        dest.writeString(this.addressName);
        dest.writeString(this.receiverName);
        dest.writeString(this.provinceName);
        dest.writeString(this.districtName);
    }

    public AddressModel() {
    }

    protected AddressModel(Parcel in) {
        this.addressId = in.readString();
        this.receiverPhone = in.readString();
        this.addressStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.postalCode = in.readString();
        this.latitude = in.readString();
        this.addressStreet = in.readString();
        this.cityName = in.readString();
        this.districtId = in.readString();
        this.cityId = in.readString();
        this.countryName = in.readString();
        this.longitude = in.readString();
        this.provinceId = in.readString();
        this.addressName = in.readString();
        this.receiverName = in.readString();
        this.provinceName = in.readString();
        this.districtName = in.readString();
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel source) {
            return new AddressModel(source);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public Destination convertToDestination() {
        Destination destination = new Destination();
        destination.setAddressId(getAddressId());
        destination.setAddressName(getAddressName());
        destination.setAddressStatus(getAddressStatus());
        destination.setAddressStreet(getAddressStreet());
        destination.setCityId(getCityId());
        destination.setCityName(getCityName());
        destination.setCountryName(getCountryName());
        destination.setDistrictId(getDistrictId());
        destination.setDistrictName(getDistrictName());
        destination.setLatitude(getLatitude());
        destination.setLongitude(getLongitude());
        destination.setPostalCode(getPostalCode());
        destination.setProvinceId(getProvinceId());
        destination.setProvinceName(getProvinceName());
        destination.setReceiverName(getReceiverName());
        destination.setReceiverPhone(getReceiverPhone());
        return destination;
    }
}
