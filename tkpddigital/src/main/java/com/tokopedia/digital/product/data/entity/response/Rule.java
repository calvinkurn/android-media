package com.tokopedia.digital.product.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class Rule {

    @SerializedName("product_text")
    @Expose
    private String productText;
    @SerializedName("product_view_style")
    @Expose
    private int productViewStyle;
    @SerializedName("show_price")
    @Expose
    private boolean showPrice;
    @SerializedName("enable_voucher")
    @Expose
    private boolean enableVoucher;
    @SerializedName("button_text")
    @Expose
    private String buttonText;

    public String getProductText() {
        return productText;
    }

    public int getProductViewStyle() {
        return productViewStyle;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public boolean isEnableVoucher() {
        return enableVoucher;
    }

    public String getButtonText() {
        return buttonText;
    }

}
