package com.tokopedia.core.analytics.nishikino.model;

import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.Map;

/**
 * Created by hangnadi on 9/14/17.
 */

public class Promotion {

    private String promotionID;
    private String promotionName;
    private String promotionAlias;
    private String promotionPosition;
    private String redirectUrl;
    private String promoCode;

    public void setPromotionID(String promotionID) {
        this.promotionID = promotionID;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public void setPromotionAlias(String promotionAlias) {
        this.promotionAlias = promotionAlias;
    }

    public void setPromotionPosition(int promotionPosition) {
        this.promotionPosition = String.valueOf(promotionPosition);
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getPromotionID() {
        return promotionID;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public String getPromotionAlias() {
        return promotionAlias;
    }

    public String getPromotionPosition() {
        return promotionPosition;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public Map<String, Object> getPromotionDataEvent() {
        return DataLayer.mapOf(
                "id", getPromotionID(),
                "name", getPromotionName(),
                "creative", getPromotionAlias(),
                "position", getPromotionPosition()
        );
    }

    public Map<String, Object> getNullPromotionDataEvent() {
        return DataLayer.mapOf(
                "id", null,
                "name", null,
                "creative", null,
                "position", null
        );
    }

    public Map<String, Object> getImpressionDataLayer() {
        return DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "homepage",
                "eventAction", "slider banner impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", getPromotionID(),
                                                "name", getPromotionName(),
                                                "creative", getPromotionAlias(),
                                                "position", getPromotionPosition(),
                                                "promo_code", !TextUtils.isEmpty(getPromoCode()) ? getPromoCode() : "NoPromoCode"
                                        )
                                )
                        )
                ),
                "attribution", String.format("1 - sliderBanner - %s - %s", getPromotionPosition(), getPromotionAlias())
        );
    }

    public Map<String, Object> getClickDataLayer() {
        return DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "homepage",
                "eventAction", "slider banner click",
                "eventLabel", getRedirectUrl(),
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", getPromotionID(),
                                                "name", getPromotionName(),
                                                "creative", getPromotionAlias(),
                                                "position", getPromotionPosition()
                                        )
                                )
                        )
                ),
                "attribution", String.format("1 - sliderBanner - %s - %s", getPromotionPosition(), getPromotionAlias())
        );
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
