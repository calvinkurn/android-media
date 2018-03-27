package com.tokopedia.core.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;

import java.util.Map;

/**
 * Created by nakama on 2/6/18.
 */

public class HomePageTracking extends TrackingUtils {

    private static String STATIC_VALUE_CLICK_HOMEPAGE = "clickHomePage";
    private static String STATIC_VALUE_HOMEPAGE = "homepage";
    public static final String BELI_INI_ITU_CLICK = "beli ini itu click";
    public static final String BAYAR_INI_ITU_CLICK = "bayar ini itu click";
    public static final String PESAN_INI_ITU_CLICK = "pesan ini itu click";
    public static final String AJUKAN_INI_ITU_CLICK = "ajukan ini itu click";
    public static final String JUAL_INI_ITU_CLICK = "jual ini itu click";

    public static void eventPromoImpression(Promotion promotion) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(promotion.getImpressionDataLayer());
    }

    public static void eventPromoClick(Promotion promotion) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(promotion.getClickDataLayer());
    }

    public static void eventClickViewAllPromo() {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "slider banner click view all",
                ""
        ).getEvent());
    }

    private static void flushEventTracker() {
        sendGTMEvent(new EventTracking(
                null, null, null, null
        ).getEvent());
    }

    public static void eventClickHomeUseCase(String title) {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "click 5 use cases",
                title
        ).getEvent());
    }

    public static void eventClickTabExplorer(String title) {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "click explorer tab",
                title
        ).getEvent());
    }

    public static void eventClickDynamicIcons(String title) {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "click 5 dynamic icons",
                title
        ).getEvent());
    }

    public static void eventClickSeeAllProductSprint() {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "sprint sale click view all",
                ""
        ).getEvent());
    }

    public static void eventClickSeeAllProductSprintBackground() {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "sprint sale with backgroud click view all",
                ""
        ).getEvent());
    }

    public static void eventEnhancedImpressionSprintSaleHomePage(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventEnhancedClickSprintSaleProduct(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventEnhancedImpressionDynamicChannelHomePage(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventEnhancedClickDynamicChannelHomePage(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventClickSeeAllDynamicChannel(String applink) {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "curated list click view all",
                applink
        ).getEvent());
    }

    public static void eventClickExplorerItem(String action, String label) {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                action,
                label
        ).getEvent());
    }

    public static void eventEnhancedImpressionFavoriteCategory(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventEnhancedClickFavoriteCategory(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventEnhancedImpressionProductHomePage(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventEnhancedClickProductHomePage(Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(data);
    }

    public static void eventClickOpenShop() {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "jual ini itu buka toko",
                ""
        ).getEvent());
    }

    public static void eventClickEditShop() {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "jual ini itu click ubah",
                ""
        ).getEvent());
    }

}
