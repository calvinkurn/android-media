package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class RecipientAddressModel implements Parcelable, ShipmentData {

    private String id;
    private int addressStatus;
    private String addressName;
    private String provinceName;
    private String postalCode;
    private String cityName;
    private String street;
    private String countryName;
    private String recipientName;
    private String recipientPhoneNumber;
    private String destinationDistrictId;
    private String destinationDistrictName;
    private String latitude;
    private String longitude;
    private String cityId;
    private String provinceId;

    // For PickupPoint Alfamart
    private String tokenPickup;
    private String unixTime;

    private boolean selected;
    private boolean stateExtraPaddingTop;

    // Flag for one click shipment
    private boolean isDisableMultipleAddress;

    private boolean isCornerAddress;
    private String userCornerId;

    public RecipientAddressModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAddressStatus() {
        return addressStatus;
    }

    public void setAddressStatus(int addressStatus) {
        this.addressStatus = addressStatus;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getDestinationDistrictId() {
        return destinationDistrictId;
    }

    public void setDestinationDistrictId(String destinationDistrictId) {
        this.destinationDistrictId = destinationDistrictId;
    }

    public String getDestinationDistrictName() {
        return destinationDistrictName;
    }

    public void setDestinationDistrictName(String destinationDistrictName) {
        this.destinationDistrictName = destinationDistrictName;
    }

    public String getTokenPickup() {
        return tokenPickup;
    }

    public void setTokenPickup(String tokenPickup) {
        this.tokenPickup = tokenPickup;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public boolean isStateExtraPaddingTop() {
        return stateExtraPaddingTop;
    }

    public void setStateExtraPaddingTop(boolean stateExtraPaddingTop) {
        this.stateExtraPaddingTop = stateExtraPaddingTop;
    }

    public boolean isDisableMultipleAddress() {
        return isDisableMultipleAddress;
    }

    public void setDisableMultipleAddress(boolean disableMultipleAddress) {
        isDisableMultipleAddress = disableMultipleAddress;
    }

    public boolean isCornerAddress() {
        return isCornerAddress;
    }

    public void setCornerAddress(boolean cornerAddress) {
        isCornerAddress = cornerAddress;
    }

    public String getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(String userCornerId) {
        this.userCornerId = userCornerId;
    }

    public RecipientAddressModel(String id, String addressName, String cityName, String street,
                                 String recipientName, String destinationDistrictName,
                                 boolean isCornerAddress, String recipientPhoneNumber,
                                 String provinceName, String districtId, String postalCode,
                                 String latitude, String longitude, String userCornerId) {
        this.id = id;
        this.addressName = addressName;
        this.cityName = cityName;
        this.street = street;
        this.recipientName = recipientName;
        this.destinationDistrictName = destinationDistrictName;
        this.isCornerAddress = isCornerAddress;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.provinceName = provinceName;
        this.destinationDistrictId = districtId;
        this.postalCode = postalCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userCornerId = userCornerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipientAddressModel)) return false;

        RecipientAddressModel that = (RecipientAddressModel) o;

        if (getAddressStatus() != that.getAddressStatus()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getAddressName() != null ? !getAddressName().equals(that.getAddressName()) : that.getAddressName() != null)
            return false;
        if (getProvinceName() != null ? !getProvinceName().equals(that.getProvinceName()) : that.getProvinceName() != null)
            return false;
        if (getPostalCode() != null ? !getPostalCode().equals(that.getPostalCode()) : that.getPostalCode() != null)
            return false;
        if (getCityName() != null ? !getCityName().equals(that.getCityName()) : that.getCityName() != null)
            return false;
        if (getStreet() != null ? !getStreet().equals(that.getStreet()) : that.getStreet() != null)
            return false;
        if (getCountryName() != null ? !getCountryName().equals(that.getCountryName()) : that.getCountryName() != null)
            return false;
        if (getRecipientName() != null ? !getRecipientName().equals(that.getRecipientName()) : that.getRecipientName() != null)
            return false;
        if (getRecipientPhoneNumber() != null ? !getRecipientPhoneNumber().equals(that.getRecipientPhoneNumber()) : that.getRecipientPhoneNumber() != null)
            return false;
        if (getDestinationDistrictId() != null ? !getDestinationDistrictId().equals(that.getDestinationDistrictId()) : that.getDestinationDistrictId() != null)
            return false;
        if (getCityId() != null ? !getCityId().equals(that.getCityId()) : that.getCityId() != null)
            return false;
        if (getProvinceId() != null ? !getProvinceId().equals(that.getProvinceId()) : that.getProvinceId() != null)
            return false;
        return getDestinationDistrictName() != null ? getDestinationDistrictName().equals(that.getDestinationDistrictName()) : that.getDestinationDistrictName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getAddressStatus();
        result = 31 * result + (getAddressName() != null ? getAddressName().hashCode() : 0);
        result = 31 * result + (getProvinceName() != null ? getProvinceName().hashCode() : 0);
        result = 31 * result + (getPostalCode() != null ? getPostalCode().hashCode() : 0);
        result = 31 * result + (getCityName() != null ? getCityName().hashCode() : 0);
        result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
        result = 31 * result + (getCountryName() != null ? getCountryName().hashCode() : 0);
        result = 31 * result + (getRecipientName() != null ? getRecipientName().hashCode() : 0);
        result = 31 * result + (getRecipientPhoneNumber() != null ? getRecipientPhoneNumber().hashCode() : 0);
        result = 31 * result + (getDestinationDistrictId() != null ? getDestinationDistrictId().hashCode() : 0);
        result = 31 * result + (getDestinationDistrictName() != null ? getDestinationDistrictName().hashCode() : 0);
        result = 31 * result + (getCityId() != null ? getCityId().hashCode() : 0);
        result = 31 * result + (getProvinceId() != null ? getProvinceId().hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.addressStatus);
        dest.writeString(this.addressName);
        dest.writeString(this.provinceName);
        dest.writeString(this.postalCode);
        dest.writeString(this.cityName);
        dest.writeString(this.street);
        dest.writeString(this.countryName);
        dest.writeString(this.recipientName);
        dest.writeString(this.recipientPhoneNumber);
        dest.writeString(this.destinationDistrictId);
        dest.writeString(this.destinationDistrictName);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.cityId);
        dest.writeString(this.provinceId);
        dest.writeString(this.tokenPickup);
        dest.writeString(this.unixTime);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.stateExtraPaddingTop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDisableMultipleAddress ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCornerAddress ? (byte) 1 : (byte) 0);
        dest.writeString(this.userCornerId);
    }

    protected RecipientAddressModel(Parcel in) {
        this.id = in.readString();
        this.addressStatus = in.readInt();
        this.addressName = in.readString();
        this.provinceName = in.readString();
        this.postalCode = in.readString();
        this.cityName = in.readString();
        this.street = in.readString();
        this.countryName = in.readString();
        this.recipientName = in.readString();
        this.recipientPhoneNumber = in.readString();
        this.destinationDistrictId = in.readString();
        this.destinationDistrictName = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.cityId = in.readString();
        this.provinceId = in.readString();
        this.tokenPickup = in.readString();
        this.unixTime = in.readString();
        this.selected = in.readByte() != 0;
        this.stateExtraPaddingTop = in.readByte() != 0;
        this.isDisableMultipleAddress = in.readByte() != 0;
        this.isCornerAddress = in.readByte() != 0;
        this.userCornerId = in.readString();
    }

    public static final Creator<RecipientAddressModel> CREATOR = new Creator<RecipientAddressModel>() {
        @Override
        public RecipientAddressModel createFromParcel(Parcel source) {
            return new RecipientAddressModel(source);
        }

        @Override
        public RecipientAddressModel[] newArray(int size) {
            return new RecipientAddressModel[size];
        }
    };
}
