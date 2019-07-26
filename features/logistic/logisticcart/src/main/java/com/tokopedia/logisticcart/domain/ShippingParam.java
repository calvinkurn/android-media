package com.tokopedia.logisticcart.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 08/01/19.
 */

public class ShippingParam implements Parcelable {
    private String originDistrictId;
    private String originPostalCode;
    private String originLatitude;
    private String originLongitude;
    private String destinationDistrictId;
    private String destinationPostalCode;
    private String destinationLatitude;
    private String destinationLongitude;
    private double weightInKilograms;
    private String shopId;
    private String token;
    private String ut;
    private int insurance;
    private int productInsurance;
    private long orderValue;
    private String categoryIds;
    private boolean isBlackbox;
    private int addressId;
    private boolean preorder;
    private boolean isTradein;

    public ShippingParam() {
    }

    protected ShippingParam(Parcel in) {
        originDistrictId = in.readString();
        originPostalCode = in.readString();
        originLatitude = in.readString();
        originLongitude = in.readString();
        destinationDistrictId = in.readString();
        destinationPostalCode = in.readString();
        destinationLatitude = in.readString();
        destinationLongitude = in.readString();
        weightInKilograms = in.readDouble();
        shopId = in.readString();
        token = in.readString();
        ut = in.readString();
        insurance = in.readInt();
        productInsurance = in.readInt();
        orderValue = in.readLong();
        categoryIds = in.readString();
        isBlackbox = in.readByte() != 0;
        addressId = in.readInt();
        preorder = in.readByte() != 0;
        isTradein = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originDistrictId);
        dest.writeString(originPostalCode);
        dest.writeString(originLatitude);
        dest.writeString(originLongitude);
        dest.writeString(destinationDistrictId);
        dest.writeString(destinationPostalCode);
        dest.writeString(destinationLatitude);
        dest.writeString(destinationLongitude);
        dest.writeDouble(weightInKilograms);
        dest.writeString(shopId);
        dest.writeString(token);
        dest.writeString(ut);
        dest.writeInt(insurance);
        dest.writeInt(productInsurance);
        dest.writeLong(orderValue);
        dest.writeString(categoryIds);
        dest.writeByte((byte) (isBlackbox ? 1 : 0));
        dest.writeInt(addressId);
        dest.writeByte((byte) (preorder ? 1 : 0));
        dest.writeByte((byte) (isTradein ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShippingParam> CREATOR = new Creator<ShippingParam>() {
        @Override
        public ShippingParam createFromParcel(Parcel in) {
            return new ShippingParam(in);
        }

        @Override
        public ShippingParam[] newArray(int size) {
            return new ShippingParam[size];
        }
    };

    public String getOriginDistrictId() {
        return originDistrictId;
    }

    public void setOriginDistrictId(String originDistrictId) {
        this.originDistrictId = originDistrictId;
    }

    public String getOriginPostalCode() {
        return originPostalCode;
    }

    public void setOriginPostalCode(String originPostalCode) {
        this.originPostalCode = originPostalCode;
    }

    public String getOriginLatitude() {
        return originLatitude;
    }

    public void setOriginLatitude(String originLatitude) {
        this.originLatitude = originLatitude;
    }

    public String getOriginLongitude() {
        return originLongitude;
    }

    public void setOriginLongitude(String originLongitude) {
        this.originLongitude = originLongitude;
    }

    public String getDestinationDistrictId() {
        return destinationDistrictId;
    }

    public void setDestinationDistrictId(String destinationDistrictId) {
        this.destinationDistrictId = destinationDistrictId;
    }

    public String getDestinationPostalCode() {
        return destinationPostalCode;
    }

    public void setDestinationPostalCode(String destinationPostalCode) {
        this.destinationPostalCode = destinationPostalCode;
    }

    public String getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public double getWeightInKilograms() {
        return weightInKilograms;
    }

    public void setWeightInKilograms(double weightInKilograms) {
        this.weightInKilograms = weightInKilograms;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public int getInsurance() {
        return insurance;
    }

    public void setInsurance(int insurance) {
        this.insurance = insurance;
    }

    public int getProductInsurance() {
        return productInsurance;
    }

    public void setProductInsurance(int productInsurance) {
        this.productInsurance = productInsurance;
    }

    public long getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(long orderValue) {
        this.orderValue = orderValue;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public boolean getIsBlackbox() { return isBlackbox; }

    public void setIsBlackbox(boolean blackbox) { isBlackbox = blackbox; }

    public int getAddressId() { return addressId; }

    public void setAddressId(int addressId) { this.addressId = addressId; }

    public boolean getIsPreorder() { return preorder; }

    public void setIsPreorder(boolean preorder) { this.preorder = preorder; }

    public boolean isTradein() {
        return isTradein;
    }

    public void setTradein(boolean tradein) {
        isTradein = tradein;
    }
}
