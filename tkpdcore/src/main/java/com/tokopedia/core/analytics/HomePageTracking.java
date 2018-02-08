package com.tokopedia.core.analytics;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;

import java.util.Map;

/**
 * Created by nakama on 2/6/18.
 */

public class HomePageTracking extends TrackingUtils {

    public static String DEFAULT_VALUE_EVENT_NAME = "clickHomepage";
    public static String DEFAULT_VALUE_EVENT_CATEGORY = "homepage";
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
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
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
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "click 5 use cases",
                title
        ).getEvent());
    }

    public static void eventClickDynamicIcons(String title) {
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "click 5 dynamic icons",
                title
        ).getEvent());
    }

    public static void eventClickSeeAllProductSprint() {
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "sprint sale click view all",
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
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                "curated list click view all",
                applink
        ).getEvent());
    }

    public static void eventClickExplorerItem(String action, String label) {
        flushEventTracker();
        sendGTMEvent(new EventTracking(
                DEFAULT_VALUE_EVENT_NAME,
                DEFAULT_VALUE_EVENT_CATEGORY,
                action,
                label
        ).getEvent());
    }
}
