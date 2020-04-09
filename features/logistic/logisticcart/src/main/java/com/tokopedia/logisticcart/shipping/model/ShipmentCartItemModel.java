package com.tokopedia.logisticcart.shipping.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel;
import com.tokopedia.promocheckout.common.view.uimodel.VoucherOrdersItemUiModel;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentCartItemModel implements ShipmentData, Parcelable {

    private boolean allItemError;
    private boolean isError;
    private String errorTitle;
    private String errorDescription;
    private boolean isWarning;
    private String warningTitle;
    private String warningDescription;

    private ShipmentCartData shipmentCartData;
    private ShipmentDetailData selectedShipmentDetailData;
    private List<ShopShipment> shopShipmentList;

    // Shop data
    private int shopId;
    private String shopName;
    private boolean isGoldMerchant;
    private boolean isOfficialStore;
    private String shopBadge;

    // Cart item state
    private String cartString;
    private int shippingId;
    private int spId;
    private String dropshiperName;
    private String dropshiperPhone;
    private boolean isInsurance;
    private boolean saveStateFlag;

    private int weightUnit;
    private boolean productFinsurance;
    private boolean productFcancelPartial;
    private boolean productIsPreorder;

    private List<CartItemModel> cartItemModels = new ArrayList<>();

    // For pickup point
    private String destinationDistrictId;
    private String destinationDistrictName;
    private String tokenPickup;
    private String unixTime;

    // View state
    private boolean stateDetailSubtotalViewExpanded;
    private boolean stateAllItemViewExpanded = true;
    private boolean stateDropshipperDetailExpanded;
    private boolean stateDropshipperHasError;
    private boolean stateLoadingCourierState;
    private boolean stateHasLoadCourierState;
    private boolean stateHasLoadCourierTradeInDropOffState;
    private boolean stateHasExtraMarginTop;

    // Address Model for multiple address shipment, null if single address shipment
    private RecipientAddressModel recipientAddressModel;

    // Flag for courier recommendation
    private boolean useCourierRecommendation;
    private boolean isHidingCourier;

    // for robinhood III
    private boolean isBlackbox;
    private int addressId;
    private String blackboxInfo;

    private boolean isFulfillment;
    private int fulfillmentId;
    private String fulfillmentName;

    // promo stacking
    private boolean hasPromoList;
    private VoucherOrdersItemUiModel voucherOrdersItemUiModel;
    private VoucherLogisticItemUiModel voucherLogisticItemUiModel;

    private boolean isLeasingProduct;
    private int bookingFee;
    private List<String> listPromoCodes;

    private boolean isDropshipperDisable;
    private boolean isOrderPrioritasDisable;

    private boolean hasSetDropOffLocation;

    public ShipmentCartItemModel() {
    }

    protected ShipmentCartItemModel(Parcel in) {
        allItemError = in.readByte() != 0;
        isError = in.readByte() != 0;
        errorTitle = in.readString();
        errorDescription = in.readString();
        isWarning = in.readByte() != 0;
        warningTitle = in.readString();
        warningDescription = in.readString();
        shipmentCartData = in.readParcelable(ShipmentCartData.class.getClassLoader());
        selectedShipmentDetailData = in.readParcelable(ShipmentDetailData.class.getClassLoader());
        shopShipmentList = in.createTypedArrayList(ShopShipment.CREATOR);
        shopId = in.readInt();
        shopName = in.readString();
        isGoldMerchant = in.readByte() != 0;
        isOfficialStore = in.readByte() != 0;
        shopBadge = in.readString();
        cartString = in.readString();
        shippingId = in.readInt();
        spId = in.readInt();
        dropshiperName = in.readString();
        dropshiperPhone = in.readString();
        isInsurance = in.readByte() != 0;
        saveStateFlag = in.readByte() != 0;
        weightUnit = in.readInt();
        productFinsurance = in.readByte() != 0;
        productFcancelPartial = in.readByte() != 0;
        productIsPreorder = in.readByte() != 0;
        cartItemModels = in.createTypedArrayList(CartItemModel.CREATOR);
        destinationDistrictId = in.readString();
        destinationDistrictName = in.readString();
        tokenPickup = in.readString();
        unixTime = in.readString();
        stateDetailSubtotalViewExpanded = in.readByte() != 0;
        stateAllItemViewExpanded = in.readByte() != 0;
        stateDropshipperDetailExpanded = in.readByte() != 0;
        stateDropshipperHasError = in.readByte() != 0;
        stateLoadingCourierState = in.readByte() != 0;
        stateHasLoadCourierState = in.readByte() != 0;
        stateHasLoadCourierTradeInDropOffState = in.readByte() != 0;
        stateHasExtraMarginTop = in.readByte() != 0;
        recipientAddressModel = in.readParcelable(RecipientAddressModel.class.getClassLoader());
        useCourierRecommendation = in.readByte() != 0;
        isHidingCourier = in.readByte() != 0;
        isBlackbox = in.readByte() != 0;
        addressId = in.readInt();
        blackboxInfo = in.readString();
        isFulfillment = in.readByte() != 0;
        fulfillmentId = in.readInt();
        fulfillmentName = in.readString();
        hasPromoList = in.readByte() != 0;
        voucherOrdersItemUiModel = in.readParcelable(VoucherOrdersItemUiModel.class.getClassLoader());
        voucherLogisticItemUiModel = in.readParcelable(VoucherLogisticItemUiModel.class.getClassLoader());
        isLeasingProduct = in.readByte() != 0;
        bookingFee = in.readInt();
        hasSetDropOffLocation = in.readByte() != 0;
        listPromoCodes = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (allItemError ? 1 : 0));
        dest.writeByte((byte) (isError ? 1 : 0));
        dest.writeString(errorTitle);
        dest.writeString(errorDescription);
        dest.writeByte((byte) (isWarning ? 1 : 0));
        dest.writeString(warningTitle);
        dest.writeString(warningDescription);
        dest.writeParcelable(shipmentCartData, flags);
        dest.writeParcelable(selectedShipmentDetailData, flags);
        dest.writeTypedList(shopShipmentList);
        dest.writeInt(shopId);
        dest.writeString(shopName);
        dest.writeByte((byte) (isGoldMerchant ? 1 : 0));
        dest.writeByte((byte) (isOfficialStore ? 1 : 0));
        dest.writeString(shopBadge);
        dest.writeString(cartString);
        dest.writeInt(shippingId);
        dest.writeInt(spId);
        dest.writeString(dropshiperName);
        dest.writeString(dropshiperPhone);
        dest.writeByte((byte) (isInsurance ? 1 : 0));
        dest.writeByte((byte) (saveStateFlag ? 1 : 0));
        dest.writeInt(weightUnit);
        dest.writeByte((byte) (productFinsurance ? 1 : 0));
        dest.writeByte((byte) (productFcancelPartial ? 1 : 0));
        dest.writeByte((byte) (productIsPreorder ? 1 : 0));
        dest.writeTypedList(cartItemModels);
        dest.writeString(destinationDistrictId);
        dest.writeString(destinationDistrictName);
        dest.writeString(tokenPickup);
        dest.writeString(unixTime);
        dest.writeByte((byte) (stateDetailSubtotalViewExpanded ? 1 : 0));
        dest.writeByte((byte) (stateAllItemViewExpanded ? 1 : 0));
        dest.writeByte((byte) (stateDropshipperDetailExpanded ? 1 : 0));
        dest.writeByte((byte) (stateDropshipperHasError ? 1 : 0));
        dest.writeByte((byte) (stateLoadingCourierState ? 1 : 0));
        dest.writeByte((byte) (stateHasLoadCourierState ? 1 : 0));
        dest.writeByte((byte) (stateHasLoadCourierTradeInDropOffState ? 1: 0));
        dest.writeByte((byte) (stateHasExtraMarginTop ? 1 : 0));
        dest.writeParcelable(recipientAddressModel, flags);
        dest.writeByte((byte) (useCourierRecommendation ? 1 : 0));
        dest.writeByte((byte) (isHidingCourier ? 1 : 0));
        dest.writeByte((byte) (isBlackbox ? 1 : 0));
        dest.writeInt(addressId);
        dest.writeString(blackboxInfo);
        dest.writeByte((byte) (isFulfillment ? 1 : 0));
        dest.writeInt(fulfillmentId);
        dest.writeString(fulfillmentName);
        dest.writeByte((byte) (hasPromoList ? 1 : 0));
        dest.writeParcelable(voucherOrdersItemUiModel, flags);
        dest.writeParcelable(voucherLogisticItemUiModel, flags);
        dest.writeByte((byte) (isLeasingProduct ? 1 : 0));
        dest.writeInt(bookingFee);
        dest.writeByte((byte) (hasSetDropOffLocation ? 1 : 0));
        dest.writeStringList(listPromoCodes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShipmentCartItemModel> CREATOR = new Creator<ShipmentCartItemModel>() {
        @Override
        public ShipmentCartItemModel createFromParcel(Parcel in) {
            return new ShipmentCartItemModel(in);
        }

        @Override
        public ShipmentCartItemModel[] newArray(int size) {
            return new ShipmentCartItemModel[size];
        }
    };

    public static ShipmentCartItemModel clone(ShipmentCartItemModel shipmentCartItemModel, List<CartItemModel> cartItemModels) {
        ShipmentCartItemModel newShipmentCartItemModel = new ShipmentCartItemModel();
        newShipmentCartItemModel.setSelectedShipmentDetailData(shipmentCartItemModel.getSelectedShipmentDetailData());
        newShipmentCartItemModel.setCartItemModels(cartItemModels);
        newShipmentCartItemModel.setAllItemError(shipmentCartItemModel.isAllItemError());
        newShipmentCartItemModel.setErrorTitle(shipmentCartItemModel.getErrorTitle());
        newShipmentCartItemModel.setErrorDescription(shipmentCartItemModel.getErrorDescription());
        newShipmentCartItemModel.setError(shipmentCartItemModel.isError());
        newShipmentCartItemModel.setWeightUnit(shipmentCartItemModel.getWeightUnit());
        newShipmentCartItemModel.setWarningTitle(shipmentCartItemModel.getWarningTitle());
        newShipmentCartItemModel.setWarningDescription(shipmentCartItemModel.getWarningDescription());
        newShipmentCartItemModel.setWarning(shipmentCartItemModel.isWarning());
        newShipmentCartItemModel.setUnixTime(shipmentCartItemModel.getUnixTime());
        newShipmentCartItemModel.setTokenPickup(shipmentCartItemModel.getTokenPickup());
        newShipmentCartItemModel.setStateDetailSubtotalViewExpanded(shipmentCartItemModel.isStateDetailSubtotalViewExpanded());
        newShipmentCartItemModel.setStateAllItemViewExpanded(shipmentCartItemModel.isStateAllItemViewExpanded());
        newShipmentCartItemModel.setStateDropshipperDetailExpanded(shipmentCartItemModel.isStateDropshipperDetailExpanded());
        newShipmentCartItemModel.setStateDropshipperHasError(shipmentCartItemModel.isStateDropshipperHasError());
        newShipmentCartItemModel.setShopName(shipmentCartItemModel.getShopName());
        newShipmentCartItemModel.setShopId(shipmentCartItemModel.getShopId());
        newShipmentCartItemModel.setProductIsPreorder(shipmentCartItemModel.isProductIsPreorder());
        newShipmentCartItemModel.setProductFinsurance(shipmentCartItemModel.isProductFinsurance());
        newShipmentCartItemModel.setProductFcancelPartial(shipmentCartItemModel.isProductFcancelPartial());
        newShipmentCartItemModel.setDestinationDistrictName(shipmentCartItemModel.getDestinationDistrictName());
        newShipmentCartItemModel.setDestinationDistrictId(shipmentCartItemModel.getDestinationDistrictId());
        newShipmentCartItemModel.setRecipientAddressModel(shipmentCartItemModel.getRecipientAddressModel());
        newShipmentCartItemModel.setOfficialStore(shipmentCartItemModel.isOfficialStore());
        newShipmentCartItemModel.setGoldMerchant(shipmentCartItemModel.isGoldMerchant());
        newShipmentCartItemModel.setShopShipmentList(shipmentCartItemModel.getShopShipmentList());
        newShipmentCartItemModel.setUseCourierRecommendation(shipmentCartItemModel.isUseCourierRecommendation());
        newShipmentCartItemModel.setHidingCourier(shipmentCartItemModel.isHidingCourier());
        newShipmentCartItemModel.setCartString(shipmentCartItemModel.getCartString());
        newShipmentCartItemModel.setShippingId(shipmentCartItemModel.getShippingId());
        newShipmentCartItemModel.setSpId(shipmentCartItemModel.getSpId());
        newShipmentCartItemModel.setDropshiperName(shipmentCartItemModel.getDropshiperName());
        newShipmentCartItemModel.setDropshiperPhone(shipmentCartItemModel.getDropshiperPhone());
        newShipmentCartItemModel.setInsurance(shipmentCartItemModel.isInsurance());
        newShipmentCartItemModel.setSaveStateFlag(shipmentCartItemModel.isSaveStateFlag());
        newShipmentCartItemModel.setStateLoadingCourierState(shipmentCartItemModel.isStateLoadingCourierState());
        newShipmentCartItemModel.setStateHasLoadCourierState(shipmentCartItemModel.isStateHasLoadCourierState());
        newShipmentCartItemModel.setStateHasExtraMarginTop(shipmentCartItemModel.isStateHasExtraMarginTop());
        newShipmentCartItemModel.setShopBadge(shipmentCartItemModel.getShopBadge());
        newShipmentCartItemModel.setIsBlackbox(shipmentCartItemModel.getIsBlackbox());
        newShipmentCartItemModel.setAddressId(shipmentCartItemModel.getAddressId());
        newShipmentCartItemModel.setFulfillment(shipmentCartItemModel.isFulfillment);
        newShipmentCartItemModel.setFulfillmentId(shipmentCartItemModel.getFulfillmentId());
        newShipmentCartItemModel.setFulfillmentName(shipmentCartItemModel.getFulfillmentName());
        newShipmentCartItemModel.setBlackboxInfo(shipmentCartItemModel.getBlackboxInfo());
        newShipmentCartItemModel.setHasPromoList(shipmentCartItemModel.getHasPromoList());
        newShipmentCartItemModel.setVoucherOrdersItemUiModel(shipmentCartItemModel.getVoucherOrdersItemUiModel());
        newShipmentCartItemModel.setVoucherLogisticItemUiModel(shipmentCartItemModel.getVoucherLogisticItemUiModel());
        newShipmentCartItemModel.setIsLeasingProduct(shipmentCartItemModel.getIsLeasingProduct());
        newShipmentCartItemModel.setListPromoCodes(shipmentCartItemModel.getListPromoCodes());
        return newShipmentCartItemModel;
    }

    public boolean isAllItemError() {
        return allItemError;
    }

    public void setAllItemError(boolean allItemError) {
        this.allItemError = allItemError;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getWarningDescription() {
        return warningDescription;
    }

    public void setWarningDescription(String warningDescription) {
        this.warningDescription = warningDescription;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningTitle() {
        return warningTitle;
    }

    public void setWarningTitle(String warningTitle) {
        this.warningTitle = warningTitle;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public ShipmentCartData getShipmentCartData() {
        return shipmentCartData;
    }

    public void setShipmentCartData(ShipmentCartData shipmentCartData) {
        this.shipmentCartData = shipmentCartData;
    }

    public ShipmentDetailData getSelectedShipmentDetailData() {
        return selectedShipmentDetailData;
    }

    public void setSelectedShipmentDetailData(ShipmentDetailData selectedShipmentDetailData) {
        this.selectedShipmentDetailData = selectedShipmentDetailData;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isProductFinsurance() {
        return productFinsurance;
    }

    public void setProductFinsurance(boolean productFinsurance) {
        this.productFinsurance = productFinsurance;
    }

    public boolean isProductFcancelPartial() {
        return productFcancelPartial;
    }

    public void setProductFcancelPartial(boolean productFcancelPartial) {
        this.productFcancelPartial = productFcancelPartial;
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

    public int getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(int weightUnit) {
        this.weightUnit = weightUnit;
    }

    public boolean isStateDetailSubtotalViewExpanded() {
        return stateDetailSubtotalViewExpanded;
    }

    public void setStateDetailSubtotalViewExpanded(boolean stateDetailSubtotalViewExpanded) {
        this.stateDetailSubtotalViewExpanded = stateDetailSubtotalViewExpanded;
    }

    public boolean isStateAllItemViewExpanded() {
        return stateAllItemViewExpanded;
    }

    public void setStateAllItemViewExpanded(boolean stateAllItemViewExpanded) {
        this.stateAllItemViewExpanded = stateAllItemViewExpanded;
    }

    public boolean isStateDropshipperDetailExpanded() {
        return stateDropshipperDetailExpanded;
    }

    public void setStateDropshipperDetailExpanded(boolean stateDropshipperDetailExpanded) {
        this.stateDropshipperDetailExpanded = stateDropshipperDetailExpanded;
    }

    public boolean isProductIsPreorder() {
        return productIsPreorder;
    }

    public void setProductIsPreorder(boolean productIsPreorder) {
        this.productIsPreorder = productIsPreorder;
    }

    public boolean isStateDropshipperHasError() {
        return stateDropshipperHasError;
    }

    public void setStateDropshipperHasError(boolean stateDropshipperHasError) {
        this.stateDropshipperHasError = stateDropshipperHasError;
    }

    public RecipientAddressModel getRecipientAddressModel() {
        return recipientAddressModel;
    }

    public void setRecipientAddressModel(RecipientAddressModel recipientAddressModel) {
        this.recipientAddressModel = recipientAddressModel;
    }

    public List<CartItemModel> getCartItemModels() {
        return cartItemModels;
    }

    public void setCartItemModels(List<CartItemModel> cartItemModels) {
        this.cartItemModels = cartItemModels;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean officialStore) {
        isOfficialStore = officialStore;
    }

    public List<ShopShipment> getShopShipmentList() {
        return shopShipmentList;
    }

    public void setShopShipmentList(List<ShopShipment> shopShipmentList) {
        this.shopShipmentList = shopShipmentList;
    }

    public boolean isUseCourierRecommendation() {
        return useCourierRecommendation;
    }

    public void setUseCourierRecommendation(boolean useCourierRecommendation) {
        this.useCourierRecommendation = useCourierRecommendation;
    }

    public boolean isHidingCourier() {
        return isHidingCourier;
    }

    public void setHidingCourier(boolean hidingCourier) {
        isHidingCourier = hidingCourier;
    }

    public String getCartString() { return cartString; }

    public void setCartString(String cartString) { this.cartString = cartString; }

    public int getShippingId() {
        return shippingId;
    }

    public void setShippingId(int shippingId) {
        this.shippingId = shippingId;
    }

    public int getSpId() {
        return spId;
    }

    public void setSpId(int spId) {
        this.spId = spId;
    }

    public String getDropshiperName() {
        return dropshiperName;
    }

    public void setDropshiperName(String dropshiperName) {
        this.dropshiperName = dropshiperName;
    }

    public String getDropshiperPhone() {
        return dropshiperPhone;
    }

    public void setDropshiperPhone(String dropshiperPhone) {
        this.dropshiperPhone = dropshiperPhone;
    }

    public boolean isInsurance() {
        return isInsurance;
    }

    public void setInsurance(boolean insurance) {
        isInsurance = insurance;
    }

    public boolean isStateLoadingCourierState() {
        return stateLoadingCourierState;
    }

    public void setStateLoadingCourierState(boolean stateLoadingCourierState) {
        this.stateLoadingCourierState = stateLoadingCourierState;
    }

    public boolean isStateHasLoadCourierState() {
        return stateHasLoadCourierState;
    }

    public void setStateHasLoadCourierState(boolean stateHasLoadCourierState) {
        this.stateHasLoadCourierState = stateHasLoadCourierState;
    }

    public boolean isStateHasLoadCourierTradeInDropOffState() {
        return stateHasLoadCourierTradeInDropOffState;
    }

    public void setStateHasLoadCourierTradeInDropOffState(boolean stateHasLoadCourierTradeInDropOffState) {
        this.stateHasLoadCourierTradeInDropOffState = stateHasLoadCourierTradeInDropOffState;
    }

    public boolean isStateHasExtraMarginTop() {
        return stateHasExtraMarginTop;
    }

    public void setStateHasExtraMarginTop(boolean stateHasExtraMarginTop) {
        this.stateHasExtraMarginTop = stateHasExtraMarginTop;
    }

    public String getShopBadge() {
        return shopBadge;
    }

    public void setShopBadge(String shopBadge) {
        this.shopBadge = shopBadge;
    }

    public boolean getIsBlackbox() {
        return isBlackbox;
    }

    public void setIsBlackbox(boolean blackbox) {
        this.isBlackbox = blackbox;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public boolean isFulfillment() {
        return isFulfillment;
    }

    public void setFulfillment(boolean fulfillment) {
        isFulfillment = fulfillment;
    }

    public int getFulfillmentId() {
        return fulfillmentId;
    }

    public void setFulfillmentId(int fulfillmentId) {
        this.fulfillmentId = fulfillmentId;
    }

    public String getFulfillmentName() {
        return fulfillmentName;
    }

    public void setFulfillmentName(String fulfillmentName) {
        this.fulfillmentName = fulfillmentName;
    }

    public String getBlackboxInfo() { return blackboxInfo; }

    public void setBlackboxInfo(String blackboxInfo) { this.blackboxInfo = blackboxInfo; }

    public boolean getHasPromoList() { return hasPromoList; }

    public void setHasPromoList(boolean hasPromoList) {
        this.hasPromoList = hasPromoList;
    }

    public VoucherOrdersItemUiModel getVoucherOrdersItemUiModel() {
        return voucherOrdersItemUiModel;
    }

    public void setVoucherOrdersItemUiModel(VoucherOrdersItemUiModel voucherOrdersItemUiModel) {
        this.voucherOrdersItemUiModel = voucherOrdersItemUiModel;
    }

    public VoucherLogisticItemUiModel getVoucherLogisticItemUiModel() {
        return voucherLogisticItemUiModel;
    }

    public void setVoucherLogisticItemUiModel(VoucherLogisticItemUiModel voucherLogisticItemUiModel) {
        this.voucherLogisticItemUiModel = voucherLogisticItemUiModel;
    }

    public boolean isSaveStateFlag() {
        return saveStateFlag;
    }

    public void setSaveStateFlag(boolean saveStateFlag) {
        this.saveStateFlag = saveStateFlag;
    }

    public boolean getIsLeasingProduct() { return isLeasingProduct; }

    public void setIsLeasingProduct(boolean leasingProduct) { isLeasingProduct = leasingProduct; }

    public int getBookingFee() { return bookingFee; }

    public void setBookingFee(int bookingFee) { this.bookingFee = bookingFee; }

    public boolean isDropshipperDisable() {
        return isDropshipperDisable;
    }

    public void setDropshipperDisable(boolean dropshipperDisable) {
        isDropshipperDisable = dropshipperDisable;
    }

    public boolean isOrderPrioritasDisable() {
        return isOrderPrioritasDisable;
    }

    public void setOrderPrioritasDisable(boolean orderPrioritasDisable) {
        isOrderPrioritasDisable = orderPrioritasDisable;
    }

    public boolean isHasSetDropOffLocation() {
        return hasSetDropOffLocation;
    }

    public void setHasSetDropOffLocation(boolean hasSetDropOffLocation) {
        this.hasSetDropOffLocation = hasSetDropOffLocation;
    }

    public List<String> getListPromoCodes() {
        return listPromoCodes;
    }

    public void setListPromoCodes(List<String> listPromoCodes) {
        this.listPromoCodes = listPromoCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ShipmentCartItemModel)) return false;

        ShipmentCartItemModel that = (ShipmentCartItemModel) o;

        return new EqualsBuilder()
                .append(isAllItemError(), that.isAllItemError())
                .append(isError(), that.isError())
                .append(isWarning(), that.isWarning())
                .append(getShopId(), that.getShopId())
                .append(isGoldMerchant(), that.isGoldMerchant())
                .append(isOfficialStore(), that.isOfficialStore())
                .append(getWeightUnit(), that.getWeightUnit())
                .append(isProductFinsurance(), that.isProductFinsurance())
                .append(isProductFcancelPartial(), that.isProductFcancelPartial())
                .append(isProductIsPreorder(), that.isProductIsPreorder())
                .append(getErrorTitle(), that.getErrorTitle())
                .append(getErrorDescription(), that.getErrorDescription())
                .append(getWarningTitle(), that.getWarningTitle())
                .append(getWarningDescription(), that.getWarningDescription())
                .append(getShopName(), that.getShopName())
                .append(getCartItemModels(), that.getCartItemModels())
                .append(getDestinationDistrictId(), that.getDestinationDistrictId())
                .append(getDestinationDistrictName(), that.getDestinationDistrictName())
                .append(getTokenPickup(), that.getTokenPickup())
                .append(getUnixTime(), that.getUnixTime())
                .append(getRecipientAddressModel(), that.getRecipientAddressModel())
                .append(isUseCourierRecommendation(), that.isUseCourierRecommendation())
                .append(getIsBlackbox(), that.getIsBlackbox())
                .append(getAddressId(), that.getAddressId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(isAllItemError())
                .append(isError())
                .append(getErrorTitle())
                .append(getErrorDescription())
                .append(isWarning())
                .append(getWarningTitle())
                .append(getWarningDescription())
                .append(getShopId())
                .append(getShopName())
                .append(isGoldMerchant())
                .append(isOfficialStore())
                .append(getWeightUnit())
                .append(isProductFinsurance())
                .append(isProductFcancelPartial())
                .append(isProductIsPreorder())
                .append(getCartItemModels())
                .append(getDestinationDistrictId())
                .append(getDestinationDistrictName())
                .append(getTokenPickup())
                .append(getUnixTime())
                .append(getRecipientAddressModel())
                .append(isUseCourierRecommendation())
                .append(getIsBlackbox())
                .append(getAddressId())
                .toHashCode();
    }
}
