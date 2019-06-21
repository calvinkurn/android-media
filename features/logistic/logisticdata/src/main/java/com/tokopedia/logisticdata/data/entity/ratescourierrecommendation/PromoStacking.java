package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 01/04/19.
 */
public class PromoStacking {

    @SerializedName("is_promo")
    @Expose
    private Integer isPromo;
    @SerializedName("promo_code")
    @Expose
    private String promoCode;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("shipper_id")
    @Expose
    private Integer shipperId;
    @SerializedName("shipper_product_id")
    @Expose
    private Integer shipperProductId;
    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("shipper_desc")
    @Expose
    private String shipperDesc;
    @SerializedName("promo_detail")
    @Expose
    private String promoDetail;
    @SerializedName("benefit_desc")
    @Expose
    private String benefitDesc;
    @SerializedName("promo_tnc_html")
    @Expose
    private String promoTncHtml;
    @SerializedName("shipper_disable_text")
    @Expose
    private String shipperDisableText;

    public Integer getIsPromo() {
        return isPromo;
    }

    public void setIsPromo(Integer isPromo) {
        this.isPromo = isPromo;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getShipperId() {
        return shipperId;
    }

    public void setShipperId(Integer shipperId) {
        this.shipperId = shipperId;
    }

    public Integer getShipperProductId() {
        return shipperProductId;
    }

    public void setShipperProductId(Integer shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperDesc() {
        return shipperDesc;
    }

    public void setShipperDesc(String shipperDesc) {
        this.shipperDesc = shipperDesc;
    }

    public String getPromoDetail() {
        return promoDetail;
    }

    public void setPromoDetail(String promoDetail) {
        this.promoDetail = promoDetail;
    }

    public String getBenefitDesc() {
        return benefitDesc;
    }

    public void setBenefitDesc(String benefitDesc) {
        this.benefitDesc = benefitDesc;
    }

    public String getPromoTncHtml() {
        return promoTncHtml;
    }

    public void setPromoTncHtml(String promoTncHtml) {
        this.promoTncHtml = promoTncHtml;
    }

    public String getShipperDisableText() {
        return shipperDisableText;
    }

    public void setShipperDisableText(String shipperDisableText) {
        this.shipperDisableText = shipperDisableText;
    }
}