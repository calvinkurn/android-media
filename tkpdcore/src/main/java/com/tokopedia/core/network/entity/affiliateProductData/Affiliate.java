
package com.tokopedia.core.network.entity.affiliateProductData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Affiliate {

    @SerializedName("ad_id")
    @Expose
    private Integer adId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("affiliated_by_user")
    @Expose
    private Boolean affiliatedByUser;
    @SerializedName("commission_percent")
    @Expose
    private Integer commissionPercent;
    @SerializedName("commission_percent_display")
    @Expose
    private String commissionPercentDisplay;
    @SerializedName("commission_value")
    @Expose
    private Integer commissionValue;
    @SerializedName("commission_value_display")
    @Expose
    private String commissionValueDisplay;
    @SerializedName("unique_url")
    @Expose
    private String uniqueUrl;

    public Integer getAdId() {
        return adId;
    }

    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Boolean getAffiliatedByUser() {
        return affiliatedByUser;
    }

    public void setAffiliatedByUser(Boolean affiliatedByUser) {
        this.affiliatedByUser = affiliatedByUser;
    }

    public Integer getCommissionPercent() {
        return commissionPercent;
    }

    public void setCommissionPercent(Integer commissionPercent) {
        this.commissionPercent = commissionPercent;
    }

    public String getCommissionPercentDisplay() {
        return commissionPercentDisplay;
    }

    public void setCommissionPercentDisplay(String commissionPercentDisplay) {
        this.commissionPercentDisplay = commissionPercentDisplay;
    }

    public Integer getCommissionValue() {
        return commissionValue;
    }

    public void setCommissionValue(Integer commissionValue) {
        this.commissionValue = commissionValue;
    }

    public String getCommissionValueDisplay() {
        return commissionValueDisplay;
    }

    public void setCommissionValueDisplay(String commissionValueDisplay) {
        this.commissionValueDisplay = commissionValueDisplay;
    }

    public String getUniqueUrl() {
        return uniqueUrl;
    }

    public void setUniqueUrl(String uniqueUrl) {
        this.uniqueUrl = uniqueUrl;
    }

}
