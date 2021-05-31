package com.tokopedia.logisticcart.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Irfan Khoirul on 25/01/18.
 */

public class ShipmentDetailData implements Parcelable {

    private List<ShipmentItemData> shipmentItemData;
    private int totalQuantity;
    private String shipmentTickerInfo;
    private ShipmentCartData shipmentCartData;
    private ShipmentItemData selectedShipment;
    private CourierItemData selectedCourier;
    private CourierItemData selectedCourierTradeInDropOff;
    private Boolean useInsurance;
    private boolean usePartialOrder;
    private Boolean useDropshipper;
    private String dropshipperName;
    private String dropshipperPhone;
    private boolean dropshipperNameValid;
    private boolean dropshipperPhoneValid;
    private boolean courierPromoApplied;
    private String shopId;
    private List<ShippingCourierUiModel> shippingCourierUiModels;
    private boolean isBlackbox;
    private String addressId;
    private boolean preorder;
    private boolean isTradein;
    private Boolean isOrderPriority;

    public ShipmentDetailData() {
    }

    protected ShipmentDetailData(Parcel in) {
        shipmentItemData = in.createTypedArrayList(ShipmentItemData.CREATOR);
        totalQuantity = in.readInt();
        shipmentTickerInfo = in.readString();
        shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        selectedShipment = in.readParcelable(ShipmentItemData.class.getClassLoader());
        selectedCourier = in.readParcelable(CourierItemData.class.getClassLoader());
        selectedCourierTradeInDropOff = in.readParcelable(CourierItemData.class.getClassLoader());
        byte tmpUseInsurance = in.readByte();
        useInsurance = tmpUseInsurance == 0 ? null : tmpUseInsurance == 1;
        usePartialOrder = in.readByte() != 0;
        byte tmpUseDropshipper = in.readByte();
        useDropshipper = tmpUseDropshipper == 0 ? null : tmpUseDropshipper == 1;
        dropshipperName = in.readString();
        dropshipperPhone = in.readString();
        dropshipperNameValid = in.readByte() != 0;
        dropshipperPhoneValid = in.readByte() != 0;
        courierPromoApplied = in.readByte() != 0;
        shopId = in.readString();
        shippingCourierUiModels = in.createTypedArrayList(ShippingCourierUiModel.CREATOR);
        isBlackbox = in.readByte() != 0;
        addressId = in.readString();
        preorder = in.readByte() != 0;
        isTradein = in.readByte() != 0;
        byte tmpIsOrderPriority = in.readByte();
        isOrderPriority = tmpIsOrderPriority == 0 ? null : tmpIsOrderPriority == 1;
    }

    public static final Creator<ShipmentDetailData> CREATOR = new Creator<ShipmentDetailData>() {
        @Override
        public ShipmentDetailData createFromParcel(Parcel in) {
            return new ShipmentDetailData(in);
        }

        @Override
        public ShipmentDetailData[] newArray(int size) {
            return new ShipmentDetailData[size];
        }
    };

    public boolean isDropshipperNameValid() {
        return dropshipperNameValid;
    }

    public void setDropshipperNameValid(boolean dropshipperNameValid) {
        this.dropshipperNameValid = dropshipperNameValid;
    }

    public boolean isDropshipperPhoneValid() {
        return dropshipperPhoneValid;
    }

    public void setDropshipperPhoneValid(boolean dropshipperPhoneValid) {
        this.dropshipperPhoneValid = dropshipperPhoneValid;
    }

    public List<ShipmentItemData> getShipmentItemData() {
        return shipmentItemData;
    }

    public void setShipmentItemData(List<ShipmentItemData> shipmentItemData) {
        this.shipmentItemData = shipmentItemData;
    }

    public String getShipmentTickerInfo() {
        return shipmentTickerInfo;
    }

    public void setShipmentTickerInfo(String shipmentTickerInfo) {
        this.shipmentTickerInfo = shipmentTickerInfo;
    }

    public ShipmentCartData getShipmentCartData() {
        return shipmentCartData;
    }

    public void setShipmentCartData(ShipmentCartData shipmentCartData) {
        this.shipmentCartData = shipmentCartData;
    }

    public ShipmentItemData getSelectedShipment() {
        return selectedShipment;
    }

    public void setSelectedShipment(ShipmentItemData selectedShipment) {
        this.selectedShipment = selectedShipment;
    }

    public CourierItemData getSelectedCourier() {
        return selectedCourier;
    }

    public void setSelectedCourier(CourierItemData selectedCourier) {
        this.selectedCourier = selectedCourier;
    }

    public Boolean getUseInsurance() {
        return useInsurance;
    }

    public void setUseInsurance(Boolean useInsurance) {
        this.useInsurance = useInsurance;
    }

    public boolean getUsePartialOrder() {
        return usePartialOrder;
    }

    public void setUsePartialOrder(boolean usePartialOrder) {
        this.usePartialOrder = usePartialOrder;
    }

    public Boolean getUseDropshipper() {
        return useDropshipper;
    }

    public void setUseDropshipper(Boolean useDropshipper) {
        this.useDropshipper = useDropshipper;
    }

    public String getDropshipperName() {
        return dropshipperName;
    }

    public void setDropshipperName(String dropshipperName) {
        this.dropshipperName = dropshipperName;
    }

    public String getDropshipperPhone() {
        return dropshipperPhone;
    }

    public void setDropshipperPhone(String dropshipperPhone) {
        this.dropshipperPhone = dropshipperPhone;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<ShippingCourierUiModel> getShippingCourierViewModels() {
        return shippingCourierUiModels;
    }

    public void setShippingCourierViewModels(List<ShippingCourierUiModel> shippingCourierUiModels) {
        this.shippingCourierUiModels = shippingCourierUiModels;
    }

    public boolean isCourierPromoApplied() {
        return courierPromoApplied;
    }

    public void setCourierPromoApplied(boolean courierPromoApplied) {
        this.courierPromoApplied = courierPromoApplied;
    }

    public boolean getIsBlackbox() {
        return isBlackbox;
    }

    public void setIsBlackbox(boolean blackbox) {
        isBlackbox = blackbox;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public boolean getPreorder() {
        return preorder;
    }

    public void setPreorder(boolean preorder) {
        this.preorder = preorder;
    }

    public boolean isTradein() {
        return isTradein;
    }

    public void setTradein(boolean tradein) {
        isTradein = tradein;
    }

    public Boolean isOrderPriority() {
        return isOrderPriority;
    }

    public void setOrderPriority(Boolean orderPriority) {
        this.isOrderPriority = orderPriority;
    }

    public CourierItemData getSelectedCourierTradeInDropOff() {
        return selectedCourierTradeInDropOff;
    }

    public void setSelectedCourierTradeInDropOff(CourierItemData selectedCourierTradeInDropOff) {
        this.selectedCourierTradeInDropOff = selectedCourierTradeInDropOff;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(shipmentItemData);
        dest.writeInt(totalQuantity);
        dest.writeString(shipmentTickerInfo);
        dest.writeParcelable(shipmentCartData, flags);
        dest.writeParcelable(selectedShipment, flags);
        dest.writeParcelable(selectedCourier, flags);
        dest.writeParcelable(selectedCourierTradeInDropOff, flags);
        dest.writeByte((byte) (useInsurance == null ? 0 : useInsurance ? 1 : 2));
        dest.writeByte((byte) (usePartialOrder ? 1 : 0));
        dest.writeByte((byte) (useDropshipper == null ? 0 : useDropshipper ? 1 : 2));
        dest.writeString(dropshipperName);
        dest.writeString(dropshipperPhone);
        dest.writeByte((byte) (dropshipperNameValid ? 1 : 0));
        dest.writeByte((byte) (dropshipperPhoneValid ? 1 : 0));
        dest.writeByte((byte) (courierPromoApplied ? 1 : 0));
        dest.writeString(shopId);
        dest.writeByte((byte) (isBlackbox ? 1 : 0));
        dest.writeString(addressId);
        dest.writeByte((byte) (preorder ? 1 : 0));
        dest.writeTypedList(shippingCourierUiModels);
        dest.writeByte((byte) (isTradein ? 1 : 0));
        dest.writeByte((byte) (isOrderPriority == null ? 0 : isOrderPriority ? 1 : 2));
    }
}
