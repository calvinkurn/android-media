package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CouponDetailEntity {
    @Expose
    @SerializedName("id")
    private int id;

    @Expose
    @SerializedName("owner")
    private int owner;

    @Expose
    @SerializedName("promo_id")
    private int promoId;

    @Expose
    @SerializedName("code")
    private String code;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("cta")
    private String cta;

    @Expose
    @SerializedName("cta_desktop")
    private String ctaDesktop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getPromoId() {
        return promoId;
    }

    public void setPromoId(int promoId) {
        this.promoId = promoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCta() {
        return cta;
    }

    public void setCta(String cta) {
        this.cta = cta;
    }

    public String getCtaDesktop() {
        return ctaDesktop;
    }

    public void setCtaDesktop(String ctaDesktop) {
        this.ctaDesktop = ctaDesktop;
    }

    @Override
    public String toString() {
        return "RedeemCoupon{" +
                "id=" + id +
                ", owner=" + owner +
                ", promoId=" + promoId +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cta='" + cta + '\'' +
                ", ctaDesktop='" + ctaDesktop + '\'' +
                '}';
    }
}
