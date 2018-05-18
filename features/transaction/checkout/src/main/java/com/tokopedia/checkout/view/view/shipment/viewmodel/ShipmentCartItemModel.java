package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentCartData;
import com.tokopedia.checkout.domain.datamodel.shipmentrates.ShipmentDetailData;
import com.tokopedia.checkout.view.view.shipment.ShipmentData;
import com.tokopedia.transaction.common.data.pickuppoint.Store;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public abstract class ShipmentCartItemModel implements ShipmentData {

    private boolean isError;
    private String errorMessage;
    private boolean isWarning;
    private String warningMessage;

    private int shopId;
    private ShipmentCartData shipmentCartData;
    private ShipmentDetailData selectedShipmentDetailData;
    private String shopName;
    private int cartId;

    private int weightUnit;
    private boolean productFinsurance;
    private boolean productFcancelPartial;
    private boolean productIsPreorder;

    // For pickup point
    private String destinationDistrictId;
    private String destinationDistrictName;
    private String tokenPickup;
    private String unixTime;
    private Store store;

    private boolean stateDetailSubtotalViewExpanded;
    private boolean stateAllItemViewExpanded = true;
    private boolean stateDropshipperDetailExpanded;
    private boolean stateDropshipperHasError;

    public ShipmentCartItemModel() {
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public void setWarning(boolean warning) {
        isWarning = warning;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
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

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }
}
