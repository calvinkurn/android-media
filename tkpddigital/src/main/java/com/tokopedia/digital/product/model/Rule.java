package com.tokopedia.digital.product.model;

/**
 * @author anggaprasetiyo on 5/3/17.
 */
public class Rule {


    private String productText;
    private int productViewStyle;
    private boolean showPrice;
    private boolean enableVoucher;

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public int getProductViewStyle() {
        return productViewStyle;
    }

    public void setProductViewStyle(int productViewStyle) {
        this.productViewStyle = productViewStyle;
    }

    public boolean isShowPrice() {
        return showPrice;
    }

    public void setShowPrice(boolean showPrice) {
        this.showPrice = showPrice;
    }

    public boolean isEnableVoucher() {
        return enableVoucher;
    }

    public void setEnableVoucher(boolean enableVoucher) {
        this.enableVoucher = enableVoucher;
    }
}
