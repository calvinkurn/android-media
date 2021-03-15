package com.tokopedia.logisticCommon.data.entity.address;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author Aghny A. Putra on 25/01/18
 */

public class RecipientAddressModel implements Parcelable {

    public static final int TAB_ACTIVE_ADDRESS_DEFAULT = 0;
    public static final int TAB_ACTIVE_ADDRESS_TRADE_IN = 1;

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
    private boolean selected;

    private boolean isCornerAddress;
    private String cornerId;
    private String userCornerId;
    private int partnerId;
    private String partnerName;

    // Temporary fix for address adapter bug, will refactor later
    private boolean isHeader;
    private boolean isFooter;

    // TradeIn DropOff
    private boolean isTradeIn;
    private boolean isTradeInDropOffEnable;
    private int selectedTabIndex;
    private boolean ignoreSelectionAction;
    private String dropOffAddressName;
    private String dropOffAddressDetail;
    private LocationDataModel locationDataModel;
    private List<String> disabledAddress;

    // Localizing Address
    private boolean isStateChosenAddress;
    private boolean radioButtonChecked;

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

    public boolean isCornerAddress() {
        return isCornerAddress;
    }

    public void setCornerAddress(boolean cornerAddress) {
        isCornerAddress = cornerAddress;
    }

    public String getCornerId() {
        return cornerId;
    }

    public void setCornerId(String cornerId) {
        this.cornerId = cornerId;
    }

    public String getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(String userCornerId) {
        this.userCornerId = userCornerId;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean footer) {
        isFooter = footer;
    }

    public boolean isTradeIn() {
        return isTradeIn;
    }

    public void setTradeIn(boolean tradeIn) {
        isTradeIn = tradeIn;
    }

    public boolean isTradeInDropOffEnable() {
        return isTradeInDropOffEnable;
    }

    public void setTradeInDropOffEnable(boolean tradeInDropOffEnable) {
        isTradeInDropOffEnable = tradeInDropOffEnable;
    }

    public int getSelectedTabIndex() {
        return selectedTabIndex;
    }

    public void setSelectedTabIndex(int selectedTabIndex) {
        this.selectedTabIndex = selectedTabIndex;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getDropOffAddressName() {
        return dropOffAddressName;
    }

    public void setDropOffAddressName(String dropOffAddressName) {
        this.dropOffAddressName = dropOffAddressName;
    }

    public String getDropOffAddressDetail() {
        return dropOffAddressDetail;
    }

    public void setDropOffAddressDetail(String dropOffAddressDetail) {
        this.dropOffAddressDetail = dropOffAddressDetail;
    }

    public boolean isStateChosenAddress() {
        return isStateChosenAddress;
    }

    public void setStateChosenAddress(boolean stateChosenAddress) {
        isStateChosenAddress = stateChosenAddress;
    }


    public boolean isRadioButtonChecked() {
        return radioButtonChecked;
    }

    public void setRadioButtonChecked(boolean radioButtonChecked) {
        this.radioButtonChecked = radioButtonChecked;
    }

    public LocationDataModel getLocationDataModel() {
        return locationDataModel;
    }

    public void setLocationDataModel(LocationDataModel locationDataModel) {
        this.locationDataModel = locationDataModel;
    }

    public List<String> getDisabledAddress() {
        return disabledAddress;
    }

    public void setDisabledAddress(List<String> disabledAddress) {
        this.disabledAddress = disabledAddress;
    }

    public boolean isIgnoreSelectionAction() {
        return ignoreSelectionAction;
    }

    public void setIgnoreSelectionAction(boolean ignoreSelectionAction) {
        this.ignoreSelectionAction = ignoreSelectionAction;
    }

    public boolean equalCorner(RecipientAddressModel that) {
        return getCityId().equals(that.getCityId()) &&
                getDestinationDistrictId().equals(that.getDestinationDistrictId()) &&
                getId().equals(that.getId()) && getPostalCode().equals(that.getPostalCode())
                && getProvinceId().equals(that.getProvinceId());
    }

    // A method to compare two address model in address list cart
    public boolean isIdentical(RecipientAddressModel model) {
        return (this.getId().equalsIgnoreCase(model.getId()) ||
                (this.getDestinationDistrictId().equals(model.getDestinationDistrictId()) &&
                        this.getCityId().equals(model.getCityId()) &&
                        this.getProvinceId().equals(model.getProvinceId()) &&
                        this.getStreet().equals(model.getStreet()) &&
                        this.getAddressName().equals(model.getAddressName()) &&
                        this.getPostalCode().equals(model.getPostalCode()) &&
                        this.getRecipientPhoneNumber().equals(model.getRecipientPhoneNumber()) &&
                        this.getRecipientName().equals(model.getRecipientName())));
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
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCornerAddress ? (byte) 1 : (byte) 0);
        dest.writeString(this.cornerId);
        dest.writeString(this.userCornerId);
        dest.writeInt(this.partnerId);
        dest.writeString(this.partnerName);
        dest.writeByte(this.isHeader ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFooter ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTradeIn ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTradeInDropOffEnable ? (byte) 1 : (byte) 0);
        dest.writeInt(this.selectedTabIndex);
        dest.writeByte(this.ignoreSelectionAction ? (byte) 1 : (byte) 0);
        dest.writeString(this.dropOffAddressName);
        dest.writeString(this.dropOffAddressDetail);
        dest.writeParcelable(this.locationDataModel, flags);
        dest.writeByte(this.isStateChosenAddress ? (byte) 1 : (byte) 0);
        dest.writeByte(this.radioButtonChecked? (byte) 1 : (byte) 0);
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
        this.selected = in.readByte() != 0;
        this.isCornerAddress = in.readByte() != 0;
        this.cornerId = in.readString();
        this.userCornerId = in.readString();
        this.partnerId = in.readInt();
        this.partnerName = in.readString();
        this.isHeader = in.readByte() != 0;
        this.isFooter = in.readByte() != 0;
        this.isTradeIn = in.readByte() != 0;
        this.isTradeInDropOffEnable = in.readByte() != 0;
        this.selectedTabIndex = in.readInt();
        this.ignoreSelectionAction = in.readByte() != 0;
        this.dropOffAddressName = in.readString();
        this.dropOffAddressDetail = in.readString();
        this.locationDataModel = in.readParcelable(LocationDataModel.class.getClassLoader());
        this.isStateChosenAddress = in.readByte() != 0;
        this.radioButtonChecked = in.readByte() != 0;
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
