package com.tokopedia.shipping_recommendation.domain.shipping;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class CourierItemData implements Parcelable, ShipmentOptionData {
    private int shipperId;
    private int shipperProductId;
    private String name;
    private String deliverySchedule;
    private String estimatedTimeDelivery;
    private int minEtd;
    private int maxEtd;
    private int shipperPrice;
    private String shipperFormattedPrice;
    private int insurancePrice;
    private int additionalPrice;
    private String courierInfo;
    private int insuranceType;
    private int insuranceUsedType;
    private String insuranceUsedInfo;
    private int insuranceUsedDefault;
    private boolean usePinPoint;
    private boolean allowDropshiper;
    private boolean selected;
    private String shipmentItemDataEtd;
    private String shipmentItemDataType;
    private String promoCode;
    private String checksum;
    private String ut;
    private String blackboxInfo;

    public CourierItemData() {
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getShipperProductId() {
        return shipperProductId;
    }

    public void setShipperProductId(int shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getDeliverySchedule() {
        return deliverySchedule;
    }

    public void setDeliverySchedule(String deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    public int getShipperPrice() {
        return shipperPrice;
    }

    public void setShipperPrice(int shipperPrice) {
        this.shipperPrice = shipperPrice;
    }

    public int getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(int insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public int getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(int insuranceType) {
        this.insuranceType = insuranceType;
    }

    public int getInsuranceUsedType() {
        return insuranceUsedType;
    }

    public void setInsuranceUsedType(int insuranceUsedType) {
        this.insuranceUsedType = insuranceUsedType;
    }

    public String getInsuranceUsedInfo() {
        return insuranceUsedInfo;
    }

    public void setInsuranceUsedInfo(String insuranceUsedInfo) {
        this.insuranceUsedInfo = insuranceUsedInfo;
    }

    public int getInsuranceUsedDefault() {
        return insuranceUsedDefault;
    }

    public void setInsuranceUsedDefault(int insuranceUsedDefault) {
        this.insuranceUsedDefault = insuranceUsedDefault;
    }

    public int getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(int additionalPrice) {
        this.additionalPrice = additionalPrice;
    }

    public String getCourierInfo() {
        return courierInfo;
    }

    public void setCourierInfo(String courierInfo) {
        this.courierInfo = courierInfo;
    }

    public boolean isUsePinPoint() {
        return usePinPoint;
    }

    public void setUsePinPoint(boolean usePinPoint) {
        this.usePinPoint = usePinPoint;
    }

    public boolean isAllowDropshiper() {
        return allowDropshiper;
    }

    public void setAllowDropshiper(boolean allowDropshiper) {
        this.allowDropshiper = allowDropshiper;
    }

    public int getMinEtd() {
        return minEtd;
    }

    public void setMinEtd(int minEtd) {
        this.minEtd = minEtd;
    }

    public int getMaxEtd() {
        return maxEtd;
    }

    public void setMaxEtd(int maxEtd) {
        this.maxEtd = maxEtd;
    }

    public String getEstimatedTimeDelivery() {
        return estimatedTimeDelivery;
    }

    public void setEstimatedTimeDelivery(String estimatedTimeDelivery) {
        this.estimatedTimeDelivery = estimatedTimeDelivery;
    }

    public String getShipmentItemDataEtd() {
        return shipmentItemDataEtd;
    }

    public void setShipmentItemDataEtd(String shipmentItemDataEtd) {
        this.shipmentItemDataEtd = shipmentItemDataEtd;
    }

    public String getShipmentItemDataType() {
        return shipmentItemDataType;
    }

    public void setShipmentItemDataType(String shipmentItemDataType) {
        this.shipmentItemDataType = shipmentItemDataType;
    }

    public String getShipperFormattedPrice() {
        return shipperFormattedPrice;
    }

    public void setShipperFormattedPrice(String shipperFormattedPrice) {
        this.shipperFormattedPrice = shipperFormattedPrice;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public String getBlackboxInfo() { return blackboxInfo; }

    public void setBlackboxInfo(String blackboxInfo) { this.blackboxInfo = blackboxInfo; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shipperId);
        dest.writeInt(this.shipperProductId);
        dest.writeString(this.name);
        dest.writeString(this.deliverySchedule);
        dest.writeString(this.estimatedTimeDelivery);
        dest.writeInt(this.minEtd);
        dest.writeInt(this.maxEtd);
        dest.writeInt(this.shipperPrice);
        dest.writeInt(this.insurancePrice);
        dest.writeInt(this.additionalPrice);
        dest.writeString(this.courierInfo);
        dest.writeInt(this.insuranceType);
        dest.writeInt(this.insuranceUsedType);
        dest.writeString(this.insuranceUsedInfo);
        dest.writeInt(this.insuranceUsedDefault);
        dest.writeByte(this.usePinPoint ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowDropshiper ? (byte) 1 : (byte) 0);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
        dest.writeString(this.shipmentItemDataEtd);
        dest.writeString(this.shipmentItemDataType);
        dest.writeString(this.shipperFormattedPrice);
        dest.writeString(this.promoCode);
        dest.writeString(this.checksum);
        dest.writeString(this.ut);
        dest.writeString(this.blackboxInfo);
    }

    protected CourierItemData(Parcel in) {
        this.shipperId = in.readInt();
        this.shipperProductId = in.readInt();
        this.name = in.readString();
        this.deliverySchedule = in.readString();
        this.estimatedTimeDelivery = in.readString();
        this.minEtd = in.readInt();
        this.maxEtd = in.readInt();
        this.shipperPrice = in.readInt();
        this.insurancePrice = in.readInt();
        this.additionalPrice = in.readInt();
        this.courierInfo = in.readString();
        this.insuranceType = in.readInt();
        this.insuranceUsedType = in.readInt();
        this.insuranceUsedInfo = in.readString();
        this.insuranceUsedDefault = in.readInt();
        this.usePinPoint = in.readByte() != 0;
        this.allowDropshiper = in.readByte() != 0;
        this.selected = in.readByte() != 0;
        this.shipmentItemDataEtd = in.readString();
        this.shipmentItemDataType = in.readString();
        this.shipperFormattedPrice = in.readString();
        this.promoCode = in.readString();
        this.checksum = in.readString();
        this.ut = in.readString();
        this.blackboxInfo = in.readString();
    }

    public static final Creator<CourierItemData> CREATOR = new Creator<CourierItemData>() {
        @Override
        public CourierItemData createFromParcel(Parcel source) {
            return new CourierItemData(source);
        }

        @Override
        public CourierItemData[] newArray(int size) {
            return new CourierItemData[size];
        }
    };
}
