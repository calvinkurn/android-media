
package com.tokopedia.tkpd.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartProduct {

    @SerializedName("product_total_weight")
    @Expose
    private String productTotalWeight;
    @SerializedName("product_error_msg")
    @Expose
    private String productErrorMsg;
    @SerializedName("product_preorder")
    @Expose
    private ProductPreorder productPreorder;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_quantity")
    @Expose
    private Integer productQuantity;
    @SerializedName("product_must_insurance")
    @Expose
    private String productMustInsurance;
    @SerializedName("product_cart_id")
    @Expose
    private String productCartId;
    @SerializedName("product_price_currency_value")
    @Expose
    private Integer productPriceCurrencyValue;
    @SerializedName("product_url")
    @Expose
    private String productUrl;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_pic")
    @Expose
    private String productPic;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("product_price_currency")
    @Expose
    private String productPriceCurrency;
    @SerializedName("product_returnable")
    @Expose
    private Integer productReturnable;
    @SerializedName("product_total_price")
    @Expose
    private String productTotalPrice;
    @SerializedName("product_price_last")
    @Expose
    private String productPriceLast;
    @SerializedName("product_price_updated")
    @Expose
    private String productPriceUpdated;
    @SerializedName("product_notes")
    @Expose
    private String productNotes;
    @SerializedName("product_price_idr")
    @Expose
    private String productPriceIdr;
    @SerializedName("product_price_original")
    @Expose
    private String productPriceOriginal;
    @SerializedName("product_use_insurance")
    @Expose
    private Integer productUseInsurance;
    @SerializedName("product_total_price_idr")
    @Expose
    private String productTotalPriceIdr;
    @SerializedName("product_min_order")
    @Expose
    private String productMinOrder;

    public String getProductTotalWeight() {
        return productTotalWeight;
    }

    public void setProductTotalWeight(String productTotalWeight) {
        this.productTotalWeight = productTotalWeight;
    }

    public String getProductErrorMsg() {
        return productErrorMsg;
    }

    public void setProductErrorMsg(String productErrorMsg) {
        this.productErrorMsg = productErrorMsg;
    }

    public ProductPreorder getProductPreorder() {
        return productPreorder;
    }

    public void setProductPreorder(ProductPreorder productPreorder) {
        this.productPreorder = productPreorder;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductMustInsurance() {
        return productMustInsurance;
    }

    public void setProductMustInsurance(String productMustInsurance) {
        this.productMustInsurance = productMustInsurance;
    }

    public String getProductCartId() {
        return productCartId;
    }

    public void setProductCartId(String productCartId) {
        this.productCartId = productCartId;
    }

    public Integer getProductPriceCurrencyValue() {
        return productPriceCurrencyValue;
    }

    public void setProductPriceCurrencyValue(Integer productPriceCurrencyValue) {
        this.productPriceCurrencyValue = productPriceCurrencyValue;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductPriceCurrency() {
        return productPriceCurrency;
    }

    public void setProductPriceCurrency(String productPriceCurrency) {
        this.productPriceCurrency = productPriceCurrency;
    }

    public Integer getProductReturnable() {
        return productReturnable;
    }

    public void setProductReturnable(Integer productReturnable) {
        this.productReturnable = productReturnable;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getProductPriceLast() {
        return productPriceLast;
    }

    public void setProductPriceLast(String productPriceLast) {
        this.productPriceLast = productPriceLast;
    }

    public String getProductPriceUpdated() {
        return productPriceUpdated;
    }

    public void setProductPriceUpdated(String productPriceUpdated) {
        this.productPriceUpdated = productPriceUpdated;
    }

    public String getProductNotes() {
        return productNotes;
    }

    public void setProductNotes(String productNotes) {
        this.productNotes = productNotes;
    }

    public String getProductPriceIdr() {
        return productPriceIdr;
    }

    public void setProductPriceIdr(String productPriceIdr) {
        this.productPriceIdr = productPriceIdr;
    }

    public String getProductPriceOriginal() {
        return productPriceOriginal;
    }

    public void setProductPriceOriginal(String productPriceOriginal) {
        this.productPriceOriginal = productPriceOriginal;
    }

    public Integer getProductUseInsurance() {
        return productUseInsurance;
    }

    public void setProductUseInsurance(Integer productUseInsurance) {
        this.productUseInsurance = productUseInsurance;
    }

    public String getProductTotalPriceIdr() {
        return productTotalPriceIdr;
    }

    public void setProductTotalPriceIdr(String productTotalPriceIdr) {
        this.productTotalPriceIdr = productTotalPriceIdr;
    }

    public String getProductMinOrder() {
        return productMinOrder;
    }

    public void setProductMinOrder(String productMinOrder) {
        this.productMinOrder = productMinOrder;
    }
}
