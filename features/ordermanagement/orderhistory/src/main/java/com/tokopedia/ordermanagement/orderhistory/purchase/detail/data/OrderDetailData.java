package com.tokopedia.ordermanagement.orderhistory.purchase.detail.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by kris on 11/2/17. Tokopedia
 */

public class OrderDetailData implements Parcelable {

    private String orderId;

    private String orderCode;

    private String orderStatus;

    private String resoId;

    private String orderImage;

    private String purchaseDate;

    private String responseTimeLimit;

    private String deadlineColorString;

    private boolean isRequestCancel;

    private String requestCancelReason;

    private String shopId;

    private String shopName;

    private String shopLogo;

    private String buyerName;

    private String buyerUserName;

    private String buyerId;

    private String buyerLogo = "";

    private String courierName;

    private String shipmentName;

    private String shipmentId;

    private String shipmentServiceName;

    private String shipmentServiceId;

    private String shippingAddress;

    private String awb;

    private String partialOrderStatus;

    private String preorderPeriod;

    private String preorderPeriodText;

    private boolean isPreorder;

    private String dropshipperName;

    private String dropshipperPhone;

    private String invoiceNumber;

    private String invoiceUrl;

    private List<OrderDetailItemData> itemList;

    private String totalItemQuantity;

    private String totalItemWeight;

    private String productPrice;

    private String deliveryPrice;

    private String insurancePrice;

    private String additionalFee;

    private boolean isHavingCod;

    private String codFee;

    private String totalPayment;

    private ButtonData buttonData;

    private String driverName;

    private String driverPhone;

    private String driverImage;

    private String driverVehicle;

    private boolean showInsuranceNotification;

    private String insuranceNotification;

    private String pickupPinCode;

    private String liveTrackingUrl;

    private boolean showUploadAwb;

    private String awbUploadProofText;

    private String awbUploadProofUrl;

    private String bookingCode;

    private String barcodeType;

    private int totalProtectionItem;

    private String totalProtectionFee;

    private List<String> bookingCodeMessage = null;

    private boolean isFulfillment;

    private final String ATTRIBUTE_ID = "id";

    private boolean isFreeShipping;

    private String originAddress;

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public List<String> getBookingCodeMessage() {
        return bookingCodeMessage;
    }

    public void setBookingCodeMessage(List<String> bookingCodeMessage) {
        this.bookingCodeMessage = bookingCodeMessage;
    }

    public String getAwbUploadProofUrl() {
        return awbUploadProofUrl;
    }

    public void setAwbUploadProofUrl(String awbUploadProofUrl) {
        this.awbUploadProofUrl = awbUploadProofUrl;
    }

    public OrderDetailData() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getResoId() {
        return resoId;
    }

    public void setResoId(String resoId) {
        this.resoId = resoId;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getResponseTimeLimit() {
        return responseTimeLimit;
    }

    public void setResponseTimeLimit(String responseTimeLimit) {
        this.responseTimeLimit = responseTimeLimit;
    }

    public String getDeadlineColorString() {
        return deadlineColorString;
    }

    public void setDeadlineColorString(String deadlineColorString) {
        this.deadlineColorString = deadlineColorString;
    }

    public boolean isRequestCancel() {
        return isRequestCancel;
    }

    public void setRequestCancel(boolean requestCancel) {
        isRequestCancel = requestCancel;
    }

    public String getRequestCancelReason() {
        return requestCancelReason;
    }

    public void setRequestCancelReason(String requestCancelReason) {
        this.requestCancelReason = requestCancelReason;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerUserName() {
        return buyerUserName;
    }

    public void setBuyerUserName(String buyerUserName) {
        this.buyerUserName = buyerUserName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerLogo() {
        return buyerLogo;
    }

    public void setBuyerLogo(String buyerLogo) {
        this.buyerLogo = buyerLogo;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getShipmentName() {
        return shipmentName;
    }

    public void setShipmentName(String shipmentName) {
        this.shipmentName = shipmentName;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentServiceName() {
        return shipmentServiceName;
    }

    public void setShipmentServiceName(String shipmentServiceName) {
        this.shipmentServiceName = shipmentServiceName;
    }

    public String getShipmentServiceId() {
        return shipmentServiceId;
    }

    public void setShipmentServiceId(String shipmentServiceId) {
        this.shipmentServiceId = shipmentServiceId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getAwb() {
        return awb;
    }

    public void setAwb(String awb) {
        this.awb = awb;
    }

    public String getPartialOrderStatus() {
        return partialOrderStatus;
    }

    public void setPartialOrderStatus(String partialOrderStatus) {
        this.partialOrderStatus = partialOrderStatus;
    }

    public boolean isShowUploadAwb() {
        return showUploadAwb;
    }

    public void setShowUploadAwb(boolean showUploadAwb) {
        this.showUploadAwb = showUploadAwb;
    }

    public String getAwbUploadProofText() {
        return awbUploadProofText;
    }

    public void setAwbUploadProofText(String awbUploadProofText) {
        this.awbUploadProofText = awbUploadProofText;
    }

    public String getPreorderPeriod() {
        return preorderPeriod;
    }

    public void setPreorderPeriod(String preorderPeriod) {
        this.preorderPeriod = preorderPeriod;
    }

    public String getPreorderPeriodText() {
        return preorderPeriodText;
    }

    public void setPreorderPeriodText(String preorderPeriodText) {
        this.preorderPeriodText = preorderPeriodText;
    }

    public boolean isPreorder() {
        return isPreorder;
    }

    public void setPreorder(boolean preorder) {
        isPreorder = preorder;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public List<OrderDetailItemData> getItemList() {
        return itemList;
    }

    public void setItemList(List<OrderDetailItemData> itemList) {
        this.itemList = itemList;
    }

    public String getTotalItemQuantity() {
        return totalItemQuantity;
    }

    public void setTotalItemQuantity(String totalItemQuantity) {
        this.totalItemQuantity = totalItemQuantity;
    }

    public String getTotalItemWeight() {
        return totalItemWeight;
    }

    public void setTotalItemWeight(String totalItemWeight) {
        this.totalItemWeight = totalItemWeight;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getInsurancePrice() {
        return insurancePrice;
    }

    public void setInsurancePrice(String insurancePrice) {
        this.insurancePrice = insurancePrice;
    }

    public String getPickupPinCode() {
        return pickupPinCode;
    }

    public void setPickupPinCode(String pickupPinCode) {
        this.pickupPinCode = pickupPinCode;
    }

    public String getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(String additionalFee) {
        this.additionalFee = additionalFee;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public ButtonData getButtonData() {
        return buttonData;
    }

    public void setButtonData(ButtonData buttonData) {
        this.buttonData = buttonData;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverVehicle() {
        return driverVehicle;
    }

    public void setDriverVehicle(String driverVehicle) {
        this.driverVehicle = driverVehicle;
    }

    public boolean isShowInsuranceNotification() {
        return showInsuranceNotification;
    }

    public void setShowInsuranceNotification(boolean showInsuranceNotification) {
        this.showInsuranceNotification = showInsuranceNotification;
    }

    public String getInsuranceNotification() {
        return insuranceNotification;
    }

    public void setInsuranceNotification(String insuranceNotification) {
        this.insuranceNotification = insuranceNotification;
    }

    public int getTotalProtectionItem() {
        return totalProtectionItem;
    }

    public void setTotalProtectionItem(int totalProtectionItem) {
        this.totalProtectionItem = totalProtectionItem;
    }

    public String getTotalProtectionFee() {
        return totalProtectionFee;
    }

    public void setTotalProtectionFee(String totalProtectionFee) {
        this.totalProtectionFee = totalProtectionFee;
    }

    public String getLiveTrackingUrl() {
        if (TextUtils.isEmpty(liveTrackingUrl))
            return "";
        return liveTrackingUrl;
    }

    public void setLiveTrackingUrl(String liveTrackingUrl) {
        this.liveTrackingUrl = liveTrackingUrl;
    }

    public boolean isHavingCod() {
        return isHavingCod;
    }

    public void setHavingCod(boolean havingCod) {
        isHavingCod = havingCod;
    }

    public String getCodFee() {
        return codFee;
    }

    public void setCodFee(String codFee) {
        this.codFee = codFee;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public void setFulfillment(boolean fulfillment) {
        isFulfillment = fulfillment;
    }

    public boolean isFreeShipping() { return isFreeShipping; }

    public void setFreeShipping(boolean freeShipping) { isFreeShipping = freeShipping; }

    public String getOriginAddress() { return originAddress; }

    public void setOriginAddress(String originAddress) { this.originAddress = originAddress; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeString(this.orderCode);
        dest.writeString(this.orderStatus);
        dest.writeString(this.resoId);
        dest.writeString(this.orderImage);
        dest.writeString(this.purchaseDate);
        dest.writeString(this.responseTimeLimit);
        dest.writeString(this.deadlineColorString);
        dest.writeByte(this.isRequestCancel ? (byte) 1 : (byte) 0);
        dest.writeString(this.requestCancelReason);
        dest.writeString(this.shopId);
        dest.writeString(this.shopName);
        dest.writeString(this.shopLogo);
        dest.writeString(this.buyerName);
        dest.writeString(this.buyerUserName);
        dest.writeString(this.buyerId);
        dest.writeString(this.buyerLogo);
        dest.writeString(this.courierName);
        dest.writeString(this.shipmentName);
        dest.writeString(this.shipmentId);
        dest.writeString(this.shipmentServiceName);
        dest.writeString(this.shipmentServiceId);
        dest.writeString(this.shippingAddress);
        dest.writeString(this.awb);
        dest.writeString(this.partialOrderStatus);
        dest.writeString(this.preorderPeriod);
        dest.writeString(this.preorderPeriodText);
        dest.writeByte(this.isPreorder ? (byte) 1 : (byte) 0);
        dest.writeString(this.dropshipperName);
        dest.writeString(this.dropshipperPhone);
        dest.writeString(this.invoiceNumber);
        dest.writeString(this.invoiceUrl);
        dest.writeTypedList(this.itemList);
        dest.writeString(this.totalItemQuantity);
        dest.writeString(this.totalItemWeight);
        dest.writeString(this.productPrice);
        dest.writeString(this.deliveryPrice);
        dest.writeString(this.insurancePrice);
        dest.writeString(this.additionalFee);
        dest.writeByte(this.isHavingCod ? (byte) 1 : (byte) 0);
        dest.writeString(this.codFee);
        dest.writeString(this.totalPayment);
        dest.writeParcelable(this.buttonData, flags);
        dest.writeString(this.driverName);
        dest.writeString(this.driverPhone);
        dest.writeString(this.driverImage);
        dest.writeString(this.driverVehicle);
        dest.writeByte(this.showInsuranceNotification ? (byte) 1 : (byte) 0);
        dest.writeString(this.insuranceNotification);
        dest.writeString(this.pickupPinCode);
        dest.writeString(this.liveTrackingUrl);
        dest.writeByte(this.showUploadAwb ? (byte) 1 : (byte) 0);
        dest.writeString(this.awbUploadProofText);
        dest.writeString(this.awbUploadProofUrl);
        dest.writeString(this.bookingCode);
        dest.writeString(this.barcodeType);
        dest.writeInt(this.totalProtectionItem);
        dest.writeString(this.totalProtectionFee);
        dest.writeStringList(this.bookingCodeMessage);
        dest.writeByte(this.isFulfillment ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFreeShipping ? (byte) 1 : (byte) 0);
        dest.writeString(this.originAddress);
    }

    protected OrderDetailData(Parcel in) {
        this.orderId = in.readString();
        this.orderCode = in.readString();
        this.orderStatus = in.readString();
        this.resoId = in.readString();
        this.orderImage = in.readString();
        this.purchaseDate = in.readString();
        this.responseTimeLimit = in.readString();
        this.deadlineColorString = in.readString();
        this.isRequestCancel = in.readByte() != 0;
        this.requestCancelReason = in.readString();
        this.shopId = in.readString();
        this.shopName = in.readString();
        this.shopLogo = in.readString();
        this.buyerName = in.readString();
        this.buyerUserName = in.readString();
        this.buyerId = in.readString();
        this.buyerLogo = in.readString();
        this.courierName = in.readString();
        this.shipmentName = in.readString();
        this.shipmentId = in.readString();
        this.shipmentServiceName = in.readString();
        this.shipmentServiceId = in.readString();
        this.shippingAddress = in.readString();
        this.awb = in.readString();
        this.partialOrderStatus = in.readString();
        this.preorderPeriod = in.readString();
        this.preorderPeriodText = in.readString();
        this.isPreorder = in.readByte() != 0;
        this.dropshipperName = in.readString();
        this.dropshipperPhone = in.readString();
        this.invoiceNumber = in.readString();
        this.invoiceUrl = in.readString();
        this.itemList = in.createTypedArrayList(OrderDetailItemData.CREATOR);
        this.totalItemQuantity = in.readString();
        this.totalItemWeight = in.readString();
        this.productPrice = in.readString();
        this.deliveryPrice = in.readString();
        this.insurancePrice = in.readString();
        this.additionalFee = in.readString();
        this.isHavingCod = in.readByte() != 0;
        this.codFee = in.readString();
        this.totalPayment = in.readString();
        this.buttonData = in.readParcelable(ButtonData.class.getClassLoader());
        this.driverName = in.readString();
        this.driverPhone = in.readString();
        this.driverImage = in.readString();
        this.driverVehicle = in.readString();
        this.showInsuranceNotification = in.readByte() != 0;
        this.insuranceNotification = in.readString();
        this.pickupPinCode = in.readString();
        this.liveTrackingUrl = in.readString();
        this.showUploadAwb = in.readByte() != 0;
        this.awbUploadProofText = in.readString();
        this.awbUploadProofUrl = in.readString();
        this.bookingCode = in.readString();
        this.barcodeType = in.readString();
        this.totalProtectionItem = in.readInt();
        this.totalProtectionFee = in.readString();
        this.bookingCodeMessage = in.createStringArrayList();
        this.isFulfillment = in.readByte() != 0;
        this.isFreeShipping = in.readByte() != 0;
        this.originAddress = in.readString();
    }

    public static final Creator<OrderDetailData> CREATOR = new Creator<OrderDetailData>() {
        @Override
        public OrderDetailData createFromParcel(Parcel source) {
            return new OrderDetailData(source);
        }

        @Override
        public OrderDetailData[] newArray(int size) {
            return new OrderDetailData[size];
        }
    };

    public String getInvoiceId() {
        String invoiceUrl = getInvoiceUrl();
        Uri invoiceUri = Uri.parse(invoiceUrl);

        return invoiceUri.getQueryParameter(ATTRIBUTE_ID);
    }

    public String getProductName() {
        String productName = "";

        if (!itemList.isEmpty()) {
            productName = itemList.get(0).getItemName();
        }

        return productName;
    }

    public String getProductImageUrl() {
        String productImageUrl = "";

        if (!itemList.isEmpty()) {
            productImageUrl = itemList.get(0).getImageUrl();
        }

        return productImageUrl;
    }
}
