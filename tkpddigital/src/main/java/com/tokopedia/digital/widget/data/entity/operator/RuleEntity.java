package com.tokopedia.digital.widget.data.entity.operator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/19/17.
 */

public class RuleEntity {

    @SerializedName("product_text")
    @Expose
    private String productText;
    @SerializedName("products_view_style")
    @Expose
    private int productsViewStyle;
    @SerializedName("show_price")
    @Expose
    private boolean showPrice;
    @SerializedName("show_product")
    @Expose
    private boolean showProduct;
    @SerializedName("show_product_list_page")
    @Expose
    private boolean showProductListPage;
    @SerializedName("allow_alphanumeric_number")
    @Expose
    private boolean allowAphanumericNumber;
    @SerializedName("button_text")
    @Expose
    private String buttonLabel;

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public int getProductsViewStyle() {
        return productsViewStyle;
    }

    public void setProductsViewStyle(int productsViewStyle) {
        this.productsViewStyle = productsViewStyle;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    public boolean isShowProduct() {
        return showProduct;
    }

    public void setShowProduct(boolean showProduct) {
        this.showProduct = showProduct;
    }

    public boolean isShowProductListPage() {
        return showProductListPage;
    }

    public void setShowProductListPage(boolean showProductListPage) {
        this.showProductListPage = showProductListPage;
    }

    public boolean isAllowAphanumericNumber() {
        return allowAphanumericNumber;
    }

    public void setAllowAphanumericNumber(boolean allowAphanumericNumber) {
        this.allowAphanumericNumber = allowAphanumericNumber;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }
}