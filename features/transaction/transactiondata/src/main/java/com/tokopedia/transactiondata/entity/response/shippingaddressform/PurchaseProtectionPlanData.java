package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Fajar Ulin Nuha on 15/11/18.
 */
public class PurchaseProtectionPlanData {
    @SerializedName("protection_available")
    @Expose
    private Boolean protectionAvailable;
    @SerializedName("protection_type_id")
    @Expose
    private Integer protectionTypeId;
    @SerializedName("protection_price_per_product")
    @Expose
    private Integer protectionPricePerProduct;
    @SerializedName("protection_price")
    @Expose
    private Integer protectionPrice;
    @SerializedName("protection_title")
    @Expose
    private String protectionTitle;
    @SerializedName("protection_subtitle")
    @Expose
    private String protectionSubtitle;
    @SerializedName("protection_link_text")
    @Expose
    private String protectionLinkText;
    @SerializedName("protection_link_url")
    @Expose
    private String protectionLinkUrl;
    @SerializedName("protection_opt_in")
    @Expose
    private Boolean protectionOptIn;

    public Boolean getProtectionAvailable() {
        return protectionAvailable;
    }

    public void setProtectionAvailable(Boolean protectionAvailable) {
        this.protectionAvailable = protectionAvailable;
    }

    public Integer getProtectionTypeId() {
        return protectionTypeId;
    }

    public void setProtectionTypeId(Integer protectionTypeId) {
        this.protectionTypeId = protectionTypeId;
    }

    public Integer getProtectionPricePerProduct() {
        return protectionPricePerProduct;
    }

    public void setProtectionPricePerProduct(Integer protectionPricePerProduct) {
        this.protectionPricePerProduct = protectionPricePerProduct;
    }

    public Integer getProtectionPrice() {
        return protectionPrice;
    }

    public void setProtectionPrice(Integer protectionPrice) {
        this.protectionPrice = protectionPrice;
    }

    public String getProtectionTitle() {
        return protectionTitle;
    }

    public void setProtectionTitle(String protectionTitle) {
        this.protectionTitle = protectionTitle;
    }

    public String getProtectionSubtitle() {
        return protectionSubtitle;
    }

    public void setProtectionSubtitle(String protectionSubtitle) {
        this.protectionSubtitle = protectionSubtitle;
    }

    public String getProtectionLinkText() {
        return protectionLinkText;
    }

    public void setProtectionLinkText(String protectionLinkText) {
        this.protectionLinkText = protectionLinkText;
    }

    public String getProtectionLinkUrl() {
        return protectionLinkUrl;
    }

    public void setProtectionLinkUrl(String protectionLinkUrl) {
        this.protectionLinkUrl = protectionLinkUrl;
    }

    public Boolean getProtectionOptIn() {
        return protectionOptIn;
    }

    public void setProtectionOptIn(Boolean protectionOptIn) {
        this.protectionOptIn = protectionOptIn;
    }
}
