package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation;

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
    @SerializedName("is_applied")
    @Expose
    private Integer isApplied;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("discounted_rate")
    @Expose
    private int discontedRate;
    @SerializedName("shipping_rate")
    @Expose
    private int shippingRate;
    @SerializedName("benefit_amount")
    @Expose
    private int benefitAmount;
    @SerializedName("disabled")
    @Expose
    private boolean disabled;
    @SerializedName("hide_shipper_name")
    @Expose
    private boolean hideShipperName;
    @SerializedName("cod")
    @Expose
    private CodDataPromo cod;
    @SerializedName("eta")
    @Expose
    private EstimatedTimeArrivalPromo eta;
    @SerializedName("is_bebas_ongkir_extra")
    @Expose
    private boolean isBebasOngkirExtra;

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

    public Integer getIsApplied() {
        return isApplied;
    }

    public void setIsApplied(Integer isApplied) {
        this.isApplied = isApplied;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDiscontedRate() {
        return discontedRate;
    }

    public void setDiscontedRate(int discontedRate) {
        this.discontedRate = discontedRate;
    }

    public int getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(int shippingRate) {
        this.shippingRate = shippingRate;
    }

    public int getBenefitAmount() {
        return benefitAmount;
    }

    public void setBenefitAmount(int benefitAmount) {
        this.benefitAmount = benefitAmount;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isHideShipperName() {
        return hideShipperName;
    }

    public void setHideShipperName(boolean hideShipperName) {
        this.hideShipperName = hideShipperName;
    }

    public CodDataPromo getCod() {
        return cod;
    }

    public void setCod(CodDataPromo cod) {
        this.cod = cod;
    }

    public EstimatedTimeArrivalPromo getEta() {
        return eta;
    }

    public void setEta(EstimatedTimeArrivalPromo eta) {
        this.eta = eta;
    }

    public boolean isBebasOngkirExtra() {
        return isBebasOngkirExtra;
    }
}