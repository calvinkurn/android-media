package com.tokopedia.checkout.view.view.shipment.viewmodel;

import com.tokopedia.checkout.domain.datamodel.MultipleAddressItemData;

/**
 * @author Irfan Khoirul on 23/04/18.
 */

public class ShipmentMultipleAddressCartItem extends ShipmentCartItem {

    private MultipleAddressItemData multipleAddressItemData;
    private int invoicePosition;
    private String productImageUrl;
    private int productPrice;
    private String productName;
    private int subTotal;
    private int productPriceNumber;
    private boolean productIsPreorder;
    private boolean productIsFreeReturns;
    private boolean productReturnable;
    private boolean produstHasCasback;
    private String cashback;

    public ShipmentMultipleAddressCartItem() {
    }

    public MultipleAddressItemData getMultipleAddressItemData() {
        return multipleAddressItemData;
    }

    public void setMultipleAddressItemData(MultipleAddressItemData multipleAddressItemData) {
        this.multipleAddressItemData = multipleAddressItemData;
    }

    public int getInvoicePosition() {
        return invoicePosition;
    }

    public void setInvoicePosition(int invoicePosition) {
        this.invoicePosition = invoicePosition;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public long getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(int subTotal) {
        this.subTotal = subTotal;
    }

    public int getProductPriceNumber() {
        return productPriceNumber;
    }

    public void setProductPriceNumber(int productPriceNumber) {
        this.productPriceNumber = productPriceNumber;
    }

    public boolean isProductIsFreeReturns() {
        return productIsFreeReturns;
    }

    public void setProductIsFreeReturns(boolean productIsFreeReturns) {
        this.productIsFreeReturns = productIsFreeReturns;
    }

    public boolean isProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(boolean productReturnable) {
        this.productReturnable = productReturnable;
    }

    public boolean isProdustHasCasback() {
        return produstHasCasback;
    }

    public void setProdustHasCasback(boolean produstHasCasback) {
        this.produstHasCasback = produstHasCasback;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public boolean isProductIsPreorder() {
        return productIsPreorder;
    }

    public void setProductIsPreorder(boolean productIsPreorder) {
        this.productIsPreorder = productIsPreorder;
    }

}
