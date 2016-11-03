
package com.tokopedia.tkpd.selling.model.orderShipping;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class OrderProduct {

    @SerializedName("order_deliver_quantity")
    @Expose
    Integer orderDeliverQuantity;
    @SerializedName("product_picture")
    @Expose
    String productPicture;
    @SerializedName("product_price")
    @Expose
    String productPrice;
    @SerializedName("order_detail_id")
    @Expose
    Integer orderDetailId;
    @SerializedName("product_notes")
    @Expose
    String productNotes;
    @SerializedName("product_status")
    @Expose
    String productStatus;
    @SerializedName("order_subtotal_price")
    @Expose
    String orderSubtotalPrice;
    @SerializedName("product_id")
    @Expose
    Integer productId;
    @SerializedName("product_quantity")
    @Expose
    Integer productQuantity;
    @SerializedName("product_weight")
    @Expose
    String productWeight;
    @SerializedName("order_subtotal_price_idr")
    @Expose
    String orderSubtotalPriceIdr;
    @SerializedName("product_reject_quantity")
    @Expose
    Integer productRejectQuantity;
    @SerializedName("product_name")
    @Expose
    String productName;
    @SerializedName("product_url")
    @Expose
    String productUrl;
    @SerializedName("product_weight_unit")
    @Expose
    String productWeightUnit;
    @SerializedName("product_description")
    @Expose
    String productDescription;
    @SerializedName("product_normal_price")
    @Expose
    String productNormalPrice;
    @SerializedName("product_price_currency")
    @Expose
    String productPriceCurrency;
    int shopIsGold;

    public String getProductWeightUnit() {
        return productWeightUnit;
    }

    public void setProductWeightUnit(String productWeightUnit) {
        this.productWeightUnit = productWeightUnit;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductNormalPrice() {
        return productNormalPrice;
    }

    public void setProductNormalPrice(String productNormalPrice) {
        this.productNormalPrice = productNormalPrice;
    }

    public String getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(String productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    /**
     *
     * @return
     *     The orderDeliverQuantity
     */
    public Integer getOrderDeliverQuantity() {
        return orderDeliverQuantity;
    }

    /**
     *
     * @param orderDeliverQuantity
     *     The order_deliver_quantity
     */
    public void setOrderDeliverQuantity(Integer orderDeliverQuantity) {
        this.orderDeliverQuantity = orderDeliverQuantity;
    }

    /**
     *
     * @return
     *     The productPicture
     */
    public String getProductPicture() {
        return productPicture;
    }

    /**
     *
     * @param productPicture
     *     The product_picture
     */
    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    /**
     *
     * @return
     *     The productPrice
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     *
     * @param productPrice
     *     The product_price
     */
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    /**
     *
     * @return
     *     The orderDetailId
     */
    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    /**
     *
     * @param orderDetailId
     *     The order_detail_id
     */
    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     *
     * @return
     *     The productNotes
     */
    public String getProductNotes() {
        return productNotes;
    }

    /**
     *
     * @param productNotes
     *     The product_notes
     */
    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    /**
     *
     * @return
     *     The productStatus
     */
    public String getProductStatus() {
        return productStatus;
    }

    /**
     *
     * @param productStatus
     *     The product_status
     */
    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    /**
     *
     * @return
     *     The orderSubtotalPrice
     */
    public String getOrderSubtotalPrice() {
        return orderSubtotalPrice;
    }

    /**
     *
     * @param orderSubtotalPrice
     *     The order_subtotal_price
     */
    public void setOrderSubtotalPrice(String orderSubtotalPrice) {
        this.orderSubtotalPrice = orderSubtotalPrice;
    }

    /**
     *
     * @return
     *     The productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     *
     * @param productId
     *     The product_id
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     *
     * @return
     *     The productQuantity
     */
    public Integer getProductQuantity() {
        return productQuantity;
    }

    /**
     *
     * @param productQuantity
     *     The product_quantity
     */
    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    /**
     *
     * @return
     *     The productWeight
     */
    public String getProductWeight() {
        return productWeight;
    }

    /**
     *
     * @param productWeight
     *     The product_weight
     */
    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    /**
     *
     * @return
     *     The orderSubtotalPriceIdr
     */
    public String getOrderSubtotalPriceIdr() {
        return orderSubtotalPriceIdr;
    }

    /**
     *
     * @param orderSubtotalPriceIdr
     *     The order_subtotal_price_idr
     */
    public void setOrderSubtotalPriceIdr(String orderSubtotalPriceIdr) {
        this.orderSubtotalPriceIdr = orderSubtotalPriceIdr;
    }

    /**
     *
     * @return
     *     The productRejectQuantity
     */
    public Integer getProductRejectQuantity() {
        return productRejectQuantity;
    }

    /**
     *
     * @param productRejectQuantity
     *     The product_reject_quantity
     */
    public void setProductRejectQuantity(Integer productRejectQuantity) {
        this.productRejectQuantity = productRejectQuantity;
    }

    /**
     *
     * @return
     *     The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     *     The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     *     The productUrl
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     *
     * @param productUrl
     *     The product_url
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public int getShopIsGold() {
        return shopIsGold;
    }
}

