package com.tokopedia.home.analytics;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.home.beranda.data.model.Promotion;

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

    public static AnalyticTracker getTracker(Context context){
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return null;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        return tracker;
    }

    public static void eventPromoImpression(Promotion promotion) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(promotion.getImpressionDataLayer());
    }

    public static void eventPromoClick(Promotion promotion) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(promotion.getClickDataLayer());
    }

    public static void eventClickViewAllPromo(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if(tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "slider banner click view all",
                    ""
            );
        }
    }

    private static void flushEventTracker() {
        sendGTMEvent(new EventTracking(
                null, null, null, null
        ).getEvent());
    }

    public static void sendScreen(Activity activity, String screenName) {
        if(activity != null){
            AnalyticTracker tracker = getTracker(activity.getBaseContext());
            if(tracker != null){
                tracker.sendScreen(activity, screenName);
            }
        }
    }

    public static void eventClickJumpRecomendation(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if(tracker != null){
            tracker.sendEventTracking(
                    CLICK_HOME_PAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "cek rekomendasi jumper click",
                    ""
            );
        }
    }

    //TODO homeFragment done
    public static void eventImpressionJumpRecomendation(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if(tracker != null){
            tracker.sendEventTracking(
                    EVENT_IMPRESSION_HOME_PAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "cek rekomendasi jumper impression",
                    ""
            );
        }
    }

    public static void eventClickHomeUseCase(Context context, String title) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "click 5 use cases",
                    title
            );
        }
    }

    public static void eventClickTabExplorer(Context context,
                                             String title) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "click explorer tab",
                    title
            );
        }
    }

    public static void eventClickDynamicIcons(Context context, String title) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "click 5 dynamic icons",
                    title
            );
        }
    }

    public static void eventClickSeeAllProductSprint(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "sprint sale click view all",
                    ""
            );
        }
    }

    public static void eventClickSeeAllProductSprintBackground() {
        sendGTMEvent(new EventTracking(
                STATIC_VALUE_CLICK_HOMEPAGE,
                STATIC_VALUE_HOMEPAGE,
                "sprint sale with backgroud click view all",
                ""
        ).getEvent());
    }

    public static void eventEnhancedImpressionSprintSaleHomePage(Context context,
                                                                 Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventEnhancedClickSprintSaleProduct(Context context,
                                                           Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventEnhancedImpressionDynamicChannelHomePage(Context context,
                                                                     Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventEnhancedClickDynamicChannelHomePage(
            Context context,
            Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(
                    data
            );
        }
    }

    public static void eventClickSeeAllDynamicChannel(Context context, String applink) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "curated list click view all",
                    applink
            );
        }
    }

    public static void eventClickSeeAllLegoBannerChannel(Context context,
                                                         String applink) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "lego banner click view all",
                    applink
            );
        }
    }

    public static void eventClickExplorerItem(Context context, String action, String label) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    action,
                    label
            );
        }
    }

    public static void eventEnhancedImpressionFavoriteCategory(Context context,
                                                               Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventEnhancedClickFavoriteCategory(Context context,
                                                          Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventEnhancedImpressionProductHomePage(Context context,
                                                              Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventEnhancedClickProductHomePage(Context context,
                                                         Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventClickOpenShop(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "jual ini itu buka toko",
                    ""
            );
        }
    }

    public static void eventClickEditShop(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    STATIC_VALUE_CLICK_HOMEPAGE,
                    STATIC_VALUE_HOMEPAGE,
                    "jual ini itu click ubah",
                    ""
            );
        }
    }
}
