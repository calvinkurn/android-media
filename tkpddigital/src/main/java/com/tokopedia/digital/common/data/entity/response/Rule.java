
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Vishal Gupta 7th May, 2018
 */
public class Rule {

    @SerializedName("maximum_length")
    @Expose
    private Integer maximumLength;
    @SerializedName("product_text")
    @Expose
    private String productText;
    @SerializedName("product_view_style")
    @Expose
    private Integer productViewStyle;
    @SerializedName("show_price")
    @Expose
    private boolean showPrice;
    @SerializedName("enable_voucher")
    @Expose
    private boolean enableVoucher;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("__typename")
    @Expose
    private String typename;

    public Integer getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(Integer maximumLength) {
        this.maximumLength = maximumLength;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public Integer getProductViewStyle() {
        return productViewStyle;
    }

    public void setProductViewStyle(Integer productViewStyle) {
        this.productViewStyle = productViewStyle;
    }

    public boolean getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    public boolean getEnableVoucher() {
        return enableVoucher;
    }

    public void setEnableVoucher(boolean enableVoucher) {
        this.enableVoucher = enableVoucher;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

}
