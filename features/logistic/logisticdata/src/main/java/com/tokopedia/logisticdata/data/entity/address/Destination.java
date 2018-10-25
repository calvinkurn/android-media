package com.tokopedia.logisticdata.data.entity.address;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

/**
 * @author anggaprasetiyo on 11/18/16.
 */

public class Destination implements Parcelable {

    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("district_id")
    @Expose
    private String districtId;
    @SerializedName("province_id")
    @Expose
    private String provinceId;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("address_name")
    @Expose
    private String addressName;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("receiver_phone")
    @Expose
    private String receiverPhone;
    @SerializedName("province_name")
    @Expose
    private String provinceName;
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
    @SerializedName("district_name")
    @Expose
    private String districtName;

    private String geoLocation;

    private String password;

    public Destination() {
    }

    protected Destination(Parcel in) {
        longitude = in.readString();
        countryName = in.readString();
        cityId = in.readString();
        districtId = in.readString();
        provinceId = in.readString();
        receiverName = in.readString();
        addressName = in.readString();
        addressId = in.readString();
        receiverPhone = in.readString();
        provinceName = in.readString();
        postalCode = in.readString();
        latitude = in.readString();
        addressStreet = in.readString();
        cityName = in.readString();
        districtName = in.readString();
        geoLocation = in.readString();
        password = in.readString();
    }

    public static final Creator<Destination> CREATOR = new Creator<Destination>() {
        @Override
        public Destination createFromParcel(Parcel in) {
            return new Destination(in);
        }

        @Override
        public Destination[] newArray(int size) {
            return new Destination[size];
        }
    };

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
     * @return The receiverName
     */
    public String getReceiverName() {
        return receiverName;
    }

    /**
     * @param receiverName The receiver_name
     */
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    /**
     * @return The addressName
     */
    public String getAddressName() {
        return addressName;
    }

    /**
     * @param addressName The address_name
     */
    public void setAddressName(String addressName) {
        this.addressName = addressName;
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
        return MethodChecker.fromHtml(addressStreet.replace("\n", "<br/>")).toString();
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

    public String getAddressDetail() {
        return receiverName
                + "<br>" + addressStreet
                + "<br>" + districtName
                + ", " + cityName
                + ", " + postalCode
                + "<br>" + provinceName
                + "<br>" + receiverPhone;
    }

    public boolean isCompleted() {
        return (addressId != null && !addressId.equals("0") && !cityId.equals("0") && !districtId.equals("0")
                && !provinceId.equals("0"));
    }

    public void setLatLng(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(longitude);
        parcel.writeString(countryName);
        parcel.writeString(cityId);
        parcel.writeString(districtId);
        parcel.writeString(provinceId);
        parcel.writeString(receiverName);
        parcel.writeString(addressName);
        parcel.writeString(addressId);
        parcel.writeString(receiverPhone);
        parcel.writeString(provinceName);
        parcel.writeString(postalCode);
        parcel.writeString(latitude);
        parcel.writeString(addressStreet);
        parcel.writeString(cityName);
        parcel.writeString(districtName);
        parcel.writeString(geoLocation);
        parcel.writeString(password);
    }
}
