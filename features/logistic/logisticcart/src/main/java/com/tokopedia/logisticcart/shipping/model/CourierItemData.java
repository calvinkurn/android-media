package com.tokopedia.logisticcart.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.CodProductData;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class CourierItemData implements Parcelable, ShipmentOptionData {
    private int shipperId;
    private int shipperProductId;
    private int serviceId;
    private String name;
    private String serviceName;
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

    private String logPromoCode;
    private String logPromoMsg;
    private int discountedRate;
    private int shippingRate;
    private int benefitAmount;
    private String promoTitle;
    private boolean hideShipperName;
    private String logPromoDesc;

    private String checksum;
    private String ut;
    private String blackboxInfo;
    private Boolean isNow;
    private int priorityPrice;
    private String priorityInnactiveMessage;
    private String priorityFormattedPrice;
    private String priorityInactiveMessage;
    private String priorityDurationMessage;
    private String priorityCheckboxMessage;
    private String priorityWarningboxMessage;
    private String priorityFeeMessage;
    private String priorityPdpMessage;
    private OntimeDelivery ontimeDelivery;
    private CashOnDeliveryProduct codProductData;
    private String etaText;
    private int etaErrorCode;
    private String shipperName;
    private MerchantVoucherProductModel merchantVoucherProductModel;

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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLogPromoCode() {
        return logPromoCode;
    }

    public void setLogPromoCode(String logPromoCode) {
        this.logPromoCode = logPromoCode;
    }

    public String getLogPromoMsg() {
        return logPromoMsg;
    }

    public void setLogPromoMsg(String logPromoMsg) {
        this.logPromoMsg = logPromoMsg;
    }

    public Boolean getNow() {
        return isNow;
    }

    public void setNow(Boolean now) {
        isNow = now;
    }

    public int getPriorityPrice() {
        return priorityPrice;
    }

    public void setPriorityPrice(int priorityPrice) {
        this.priorityPrice = priorityPrice;
    }

    public String getPriorityInnactiveMessage() {
        return priorityInnactiveMessage;
    }

    public void setPriorityInnactiveMessage(String priorityInnactiveMessage) {
        this.priorityInnactiveMessage = priorityInnactiveMessage;
    }

    public String getPriorityFormattedPrice() {
        return priorityFormattedPrice;
    }

    public void setPriorityFormattedPrice(String priorityFormattedPrice) {
        this.priorityFormattedPrice = priorityFormattedPrice;
    }

    public int getDiscountedRate() {
        return discountedRate;
    }

    public void setDiscountedRate(int discountedRate) {
        this.discountedRate = discountedRate;
    }

    public int getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(int shippingRate) {
        this.shippingRate = shippingRate;
    }

    public int getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(int benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public String getPriorityInactiveMessage() {
        return priorityInactiveMessage;
    }

    public void setPriorityInactiveMessage(String priorityInactiveMessage) {
        this.priorityInactiveMessage = priorityInactiveMessage;
    }

    public String getPriorityDurationMessage() {
        return priorityDurationMessage;
    }

    public void setPriorityDurationMessage(String priorityDurationMessage) {
        this.priorityDurationMessage = priorityDurationMessage;
    }

    public String getPriorityCheckboxMessage() {
        return priorityCheckboxMessage;
    }

    public void setPriorityCheckboxMessage(String priorityCheckboxMessage) {
        this.priorityCheckboxMessage = priorityCheckboxMessage;
    }

    public String getPriorityWarningboxMessage() {
        return priorityWarningboxMessage;
    }

    public void setPriorityWarningboxMessage(String priorityWarningboxMessage) {
        this.priorityWarningboxMessage = priorityWarningboxMessage;
    }

    public String getPriorityFeeMessage() {
        return priorityFeeMessage;
    }

    public void setPriorityFeeMessage(String priorityFeeMessage) {
        this.priorityFeeMessage = priorityFeeMessage;
    }

    public String getPriorityPdpMessage() {
        return priorityPdpMessage;
    }

    public void setPriorityPdpMessage(String priorityPdpMessage) {
        this.priorityPdpMessage = priorityPdpMessage;
    }

    public OntimeDelivery getOntimeDelivery() {
        return ontimeDelivery;
    }

    public void setOntimeDelivery(OntimeDelivery ontimeDelivery) {
        this.ontimeDelivery = ontimeDelivery;
    }

    public String getPromoTitle() {
        return promoTitle;
    }

    public void setPromoTitle(String promoTitle) {
        this.promoTitle = promoTitle;
    }

    public boolean isHideShipperName() {
        return hideShipperName;
    }

    public void setHideShipperName(boolean hideShipperName) {
        this.hideShipperName = hideShipperName;
    }

    public String getLogPromoDesc() {
        return logPromoDesc;
    }

    public void setLogPromoDesc(String logPromoDesc) {
        this.logPromoDesc = logPromoDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.shipperId);
        dest.writeInt(this.shipperProductId);
        dest.writeInt(this.serviceId);
        dest.writeString(this.name);
        dest.writeString(this.serviceName);
        dest.writeString(this.deliverySchedule);
        dest.writeString(this.estimatedTimeDelivery);
        dest.writeInt(this.minEtd);
        dest.writeInt(this.maxEtd);
        dest.writeInt(this.shipperPrice);
        dest.writeString(this.shipperFormattedPrice);
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
        dest.writeString(this.promoCode);
        dest.writeString(this.logPromoCode);
        dest.writeString(this.logPromoMsg);
        dest.writeInt(this.discountedRate);
        dest.writeInt(this.shippingRate);
        dest.writeInt(this.benefitAmount);
        dest.writeString(this.promoTitle);
        dest.writeByte(this.hideShipperName ? (byte) 1 : (byte) 0);
        dest.writeString(this.logPromoDesc);
        dest.writeString(this.checksum);
        dest.writeString(this.ut);
        dest.writeString(this.blackboxInfo);
        dest.writeValue(this.isNow);
        dest.writeInt(this.priorityPrice);
        dest.writeString(this.priorityInnactiveMessage);
        dest.writeString(this.priorityFormattedPrice);
        dest.writeString(this.priorityInactiveMessage);
        dest.writeString(this.priorityDurationMessage);
        dest.writeString(this.priorityCheckboxMessage);
        dest.writeString(this.priorityWarningboxMessage);
        dest.writeString(this.priorityFeeMessage);
        dest.writeString(this.priorityPdpMessage);
        dest.writeParcelable(this.ontimeDelivery, flags);
        dest.writeParcelable(this.codProductData, flags);
    }

    protected CourierItemData(Parcel in) {
        this.shipperId = in.readInt();
        this.shipperProductId = in.readInt();
        this.serviceId = in.readInt();
        this.name = in.readString();
        this.serviceName = in.readString();
        this.deliverySchedule = in.readString();
        this.estimatedTimeDelivery = in.readString();
        this.minEtd = in.readInt();
        this.maxEtd = in.readInt();
        this.shipperPrice = in.readInt();
        this.shipperFormattedPrice = in.readString();
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
        this.promoCode = in.readString();
        this.logPromoCode = in.readString();
        this.logPromoMsg = in.readString();
        this.discountedRate = in.readInt();
        this.shippingRate = in.readInt();
        this.benefitAmount = in.readInt();
        this.promoTitle = in.readString();
        this.hideShipperName = in.readByte() != 0;
        this.logPromoDesc = in.readString();
        this.checksum = in.readString();
        this.ut = in.readString();
        this.blackboxInfo = in.readString();
        this.isNow = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.priorityPrice = in.readInt();
        this.priorityInnactiveMessage = in.readString();
        this.priorityFormattedPrice = in.readString();
        this.priorityInactiveMessage = in.readString();
        this.priorityDurationMessage = in.readString();
        this.priorityCheckboxMessage = in.readString();
        this.priorityWarningboxMessage = in.readString();
        this.priorityFeeMessage = in.readString();
        this.priorityPdpMessage = in.readString();
        this.ontimeDelivery = in.readParcelable(OntimeDelivery.class.getClassLoader());
        this.codProductData = in.readParcelable(CodProductData.class.getClassLoader());
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

    public CashOnDeliveryProduct getCodProductData() {
        return codProductData;
    }

    public void setCodProductData(CashOnDeliveryProduct codProductData) {
        this.codProductData = codProductData;
    }

    public int getEtaErrorCode() {
        return etaErrorCode;
    }

    public void setEtaErrorCode(int etaErrorCode) {
        this.etaErrorCode = etaErrorCode;
    }

    public String getEtaText() {
        return etaText;
    }

    public void setEtaText(String etaText) {
        this.etaText = etaText;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public MerchantVoucherProductModel getMerchantVoucherProductModel() {
        return merchantVoucherProductModel;
    }

    public void setMerchantVoucherProductModel(MerchantVoucherProductModel merchantVoucherProductModel) {
        this.merchantVoucherProductModel = merchantVoucherProductModel;
    }
}
