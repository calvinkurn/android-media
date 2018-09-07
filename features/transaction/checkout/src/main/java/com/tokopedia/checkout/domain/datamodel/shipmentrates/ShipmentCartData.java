package com.tokopedia.checkout.domain.datamodel.shipmentrates;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.checkout.domain.datamodel.cartshipmentform.ShopShipment;

import java.util.List;

/**
 * Created by Irfan Khoirul on 22/02/18.
 */

public class ShipmentCartData implements Parcelable {
    private List<ShopShipment> shopShipments;
    private int deliveryPriceTotal;
    private String shippingServices;
    private String shippingNames;
    private String originDistrictId;
    private String originPostalCode;
    private String originLatitude;
    private String originLongitude;
    private String destinationDistrictId;
    private String destinationPostalCode;
    private String destinationLatitude;
    private String destinationLongitude;
    private String destinationAddress;
    private double weight;
    private String token;
    private String ut;
    private int insurance;
    private int productInsurance;
    private int orderValue;
    private String categoryIds;

    public ShipmentCartData() {
    }

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public void setShopShipments(List<ShopShipment> shopShipments) {
        this.shopShipments = shopShipments;
    }

    public int getDeliveryPriceTotal() {
        return deliveryPriceTotal;
    }

    public void setDeliveryPriceTotal(int deliveryPriceTotal) {
        this.deliveryPriceTotal = deliveryPriceTotal;
    }

    public String getShippingServices() {
        return shippingServices;
    }

    public void setShippingServices(String shippingServices) {
        this.shippingServices = shippingServices;
    }

    public String getShippingNames() {
        return shippingNames;
    }

    public void setShippingNames(String shippingNames) {
        this.shippingNames = shippingNames;
    }

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

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.shopShipments);
        dest.writeInt(this.deliveryPriceTotal);
        dest.writeString(this.shippingServices);
        dest.writeString(this.shippingNames);
        dest.writeString(this.originDistrictId);
        dest.writeString(this.originPostalCode);
        dest.writeString(this.originLatitude);
        dest.writeString(this.originLongitude);
        dest.writeString(this.destinationDistrictId);
        dest.writeString(this.destinationPostalCode);
        dest.writeString(this.destinationLatitude);
        dest.writeString(this.destinationLongitude);
        dest.writeString(this.destinationAddress);
        dest.writeDouble(this.weight);
        dest.writeString(this.token);
        dest.writeString(this.ut);
        dest.writeInt(this.insurance);
        dest.writeInt(this.productInsurance);
        dest.writeInt(this.orderValue);
        dest.writeString(this.categoryIds);
    }

    protected ShipmentCartData(Parcel in) {
        this.shopShipments = in.createTypedArrayList(ShopShipment.CREATOR);
        this.deliveryPriceTotal = in.readInt();
        this.shippingServices = in.readString();
        this.shippingNames = in.readString();
        this.originDistrictId = in.readString();
        this.originPostalCode = in.readString();
        this.originLatitude = in.readString();
        this.originLongitude = in.readString();
        this.destinationDistrictId = in.readString();
        this.destinationPostalCode = in.readString();
        this.destinationLatitude = in.readString();
        this.destinationLongitude = in.readString();
        this.destinationAddress = in.readString();
        this.weight = in.readDouble();
        this.token = in.readString();
        this.ut = in.readString();
        this.insurance = in.readInt();
        this.productInsurance = in.readInt();
        this.orderValue = in.readInt();
        this.categoryIds = in.readString();
    }

    public static final Creator<ShipmentCartData> CREATOR = new Creator<ShipmentCartData>() {
        @Override
        public ShipmentCartData createFromParcel(Parcel source) {
            return new ShipmentCartData(source);
        }

        @Override
        public ShipmentCartData[] newArray(int size) {
            return new ShipmentCartData[size];
        }
    };
}
