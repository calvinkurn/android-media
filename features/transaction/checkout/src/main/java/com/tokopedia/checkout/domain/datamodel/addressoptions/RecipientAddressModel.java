package com.tokopedia.checkout.domain.datamodel.addressoptions;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.view.feature.shipment.ShipmentData;
import com.tokopedia.transaction.common.data.pickuppoint.Store;

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
    private Double latitude;
    private Double longitude;
    private String cityId;
    private String provinceId;

    // For PickupPoint Alfamart
    private String tokenPickup;
    private String unixTime;
    private Store store;

    private boolean selected;
    private boolean stateExtraPaddingTop;

    public RecipientAddressModel() {
    }

    protected RecipientAddressModel(Parcel in) {
        id = in.readString();
        addressStatus = in.readInt();
        addressName = in.readString();
        provinceName = in.readString();
        postalCode = in.readString();
        cityName = in.readString();
        street = in.readString();
        countryName = in.readString();
        recipientName = in.readString();
        recipientPhoneNumber = in.readString();
        destinationDistrictId = in.readString();
        destinationDistrictName = in.readString();
        cityId = in.readString();
        provinceId = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        tokenPickup = in.readString();
        unixTime = in.readString();
        store = in.readParcelable(Store.class.getClassLoader());
        selected = in.readByte() != 0;
        stateExtraPaddingTop = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(addressStatus);
        dest.writeString(addressName);
        dest.writeString(provinceName);
        dest.writeString(postalCode);
        dest.writeString(cityName);
        dest.writeString(street);
        dest.writeString(countryName);
        dest.writeString(recipientName);
        dest.writeString(recipientPhoneNumber);
        dest.writeString(destinationDistrictId);
        dest.writeString(destinationDistrictName);
        dest.writeString(cityId);
        dest.writeString(provinceId);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(tokenPickup);
        dest.writeString(unixTime);
        dest.writeParcelable(store, flags);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (stateExtraPaddingTop ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipientAddressModel> CREATOR = new Creator<RecipientAddressModel>() {
        @Override
        public RecipientAddressModel createFromParcel(Parcel in) {
            return new RecipientAddressModel(in);
        }

        @Override
        public RecipientAddressModel[] newArray(int size) {
            return new RecipientAddressModel[size];
        }
    };

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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

}
