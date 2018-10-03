package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.Promotion;

import java.util.Map;

/**
 * Created by nakama on 2/6/18.
 */

public class HomePageTracking extends TrackingUtils {

    public static final String CLICK_HOME_PAGE = "clickHomePage";
    public static final String EVENT_IMPRESSION_HOME_PAGE = "eventImpressionHomePage";
    private static String STATIC_VALUE_CLICK_HOMEPAGE = "clickHomePage";
    private static String STATIC_VALUE_HOMEPAGE = "homepage";
    public static final String BELI_INI_ITU_CLICK = "beli ini itu click";
    public static final String BAYAR_INI_ITU_CLICK = "bayar ini itu click";
    public static final String PESAN_INI_ITU_CLICK = "pesan ini itu click";
    public static final String AJUKAN_INI_ITU_CLICK = "ajukan ini itu click";
    public static final String JUAL_INI_ITU_CLICK = "jual ini itu click";

    public static void eventPromoImpression(Context context, Promotion promotion) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventTrackingEnhancedEcommerce(promotion.getImpressionDataLayer());
    }

    public static void eventPromoClick(Context context, Promotion promotion) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventTrackingEnhancedEcommerce(promotion.getClickDataLayer());
    }

    public static void eventClickViewAllPromo(Context context) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "slider banner click view all",
                ""
        ).getEvent());
    }

    private static void flushEventTracker(Context context) {
        sendGTMEvent(context, new EventTracking(
                null, null, null, null
        ).getEvent());
    }

    public static void eventClickJumpRecomendation(Context context) {
        sendGTMEvent(context, new EventTracking(
                CLICK_HOME_PAGE,
                STATIC_VALUE_HOMEPAGE,
                "cek rekomendasi jumper click",
                ""
        ).getEvent());
    }

    public static void eventImpressionJumpRecomendation(Context context) {
        sendGTMEvent(context, new EventTracking(
                EVENT_IMPRESSION_HOME_PAGE,
                STATIC_VALUE_HOMEPAGE,
                "cek rekomendasi jumper impression",
                ""
        ).getEvent());
    }

    public static void eventClickHomeUseCase(Context context, String title) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "click 5 use cases",
                title
        ).getEvent());
    }

    public static void eventClickTabExplorer(Context context, String title) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "click explorer tab",
                title
        ).getEvent());
    }

    public static void eventClickDynamicIcons(Context context, String title) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "click 5 dynamic icons",
                title
        ).getEvent());
    }

    public static void eventClickSeeAllProductSprint(Context context) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "sprint sale click view all",
                ""
        ).getEvent());
    }

    public static void eventClickSeeAllProductSprintBackground(Context context) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "sprint sale with backgroud click view all",
                ""
        ).getEvent());
    }

    public static void eventEnhancedImpressionSprintSaleHomePage(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context,data);
    }

    public static void eventEnhancedClickSprintSaleProduct(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventEnhancedImpressionDynamicChannelHomePage(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventEnhancedClickDynamicChannelHomePage(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventClickSeeAllDynamicChannel(Context context,  String applink) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "curated list click view all",
                applink
        ).getEvent());
    }

    public static void eventClickSeeAllLegoBannerChannel(Context context, String applink) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "lego banner click view all",
                applink
        ).getEvent());
    }

    public static void eventClickExplorerItem(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                action,
                label
        ).getEvent());
    }

    public static void eventEnhancedImpressionFavoriteCategory(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventEnhancedClickFavoriteCategory(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventEnhancedImpressionProductHomePage(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventEnhancedClickProductHomePage(Context context, Map<String, Object> data) {
        eventTrackingEnhancedEcommerce(context, data);
    }

    public static void eventClickOpenShop(Context context) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "jual ini itu buka toko",
                ""
        ).getEvent());
    }

    public static void eventClickEditShop(Context context) {
        sendGTMEvent(context, new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "jual ini itu click ubah",
                ""
        ).getEvent());
    }

}
