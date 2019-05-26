package com.tokopedia.checkout.domain.datamodel.addressoptions;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.shipping_recommendation.domain.shipping.RecipientAddressModel;

/**
 * Created by fajarnuha on 09/02/19.
 */
public class CornerAddressModel implements Parcelable {

    private int cornerId;
    private String cornerName;
    private String cornerBranchName;
    private String cornerBranchDesc;
    private String districtName;
    private String cityName;
    private String recipientFullName;
    private String provinceId;
    private String cityId;
    private String districtId;
    private String postalCode;
    private String latitude;
    private String longitude;
    private String userCornerId;
    private boolean isSelected;

    private RecipientAddressModel cornerModel;

    public CornerAddressModel() {
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }

    public String getCornerName() {
        return cornerName;
    }

    public void setCornerName(String cornerName) {
        this.cornerName = cornerName;
    }

    public String getCornerBranchName() {
        return cornerBranchName;
    }

    public void setCornerBranchName(String cornerBranchName) {
        this.cornerBranchName = cornerBranchName;
    }

    public String getCornerBranchDesc() {
        return cornerBranchDesc;
    }

    public void setCornerBranchDesc(String cornerBranchDesc) {
        this.cornerBranchDesc = cornerBranchDesc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRecipientFullName() {
        return recipientFullName;
    }

    public void setRecipientFullName(String recipientFullName) {
        this.recipientFullName = recipientFullName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public String getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(String userCornerId) {
        this.userCornerId = userCornerId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public RecipientAddressModel getCornerModel() {
        return cornerModel;
    }

    public void setCornerModel(RecipientAddressModel cornerModel) {
        this.cornerModel = cornerModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cornerId);
        dest.writeString(this.cornerName);
        dest.writeString(this.cornerBranchName);
        dest.writeString(this.cornerBranchDesc);
        dest.writeString(this.districtName);
        dest.writeString(this.cityName);
        dest.writeString(this.recipientFullName);
        dest.writeString(this.provinceId);
        dest.writeString(this.cityId);
        dest.writeString(this.districtId);
        dest.writeString(this.postalCode);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.userCornerId);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.cornerModel, flags);
    }

    protected CornerAddressModel(Parcel in) {
        this.cornerId = in.readInt();
        this.cornerName = in.readString();
        this.cornerBranchName = in.readString();
        this.cornerBranchDesc = in.readString();
        this.districtName = in.readString();
        this.cityName = in.readString();
        this.recipientFullName = in.readString();
        this.provinceId = in.readString();
        this.cityId = in.readString();
        this.districtId = in.readString();
        this.postalCode = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.userCornerId = in.readString();
        this.isSelected = in.readByte() != 0;
        this.cornerModel = in.readParcelable(RecipientAddressModel.class.getClassLoader());
    }

    public static final Creator<CornerAddressModel> CREATOR = new Creator<CornerAddressModel>() {
        @Override
        public CornerAddressModel createFromParcel(Parcel source) {
            return new CornerAddressModel(source);
        }

        @Override
        public CornerAddressModel[] newArray(int size) {
            return new CornerAddressModel[size];
        }
    };
}
