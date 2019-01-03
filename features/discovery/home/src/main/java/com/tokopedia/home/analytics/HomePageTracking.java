package com.tokopedia.home.analytics;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.home.beranda.data.model.Promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Akmal on 2/6/18.
 */

public class HomePageTracking {

    public static final String BELI_INI_ITU_CLICK = "beli ini itu click";
    public static final String BAYAR_INI_ITU_CLICK = "bayar ini itu click";
    public static final String PESAN_INI_ITU_CLICK = "pesan ini itu click";
    public static final String AJUKAN_INI_ITU_CLICK = "ajukan ini itu click";
    public static final String JUAL_INI_ITU_CLICK = "jual ini itu click";

    private static final String EVENT_CLICK_HOME_PAGE = "clickHomePage";
    private static final String EVENT_GIMMICK = "clickGimmick";
    private static final String EVENT_USER_INTERACTION_HOMEPAGE = "userInteractionHomePage";
    private static final String EVENT_TOKO_POINT = "eventTokopoint";
    private static final String EVENT_IMPRESSION_HOME_PAGE = "eventImpressionHomePage";

    private static final String CATEGORY_HOME_PAGE = "homepage";
    private static final String CATEGORY_GIMMICK = "Gimmick";
    private static final String CATEGORY_HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget";
    private static final String CATEGORY_HOMEPAGE_DIGITAL = "homepage digital";
    private static final String CATEGORY_TOKOPOINTS_USER_PAGE = "tokopoints - user profile page";
    private static final String CATEGORY_HOMEPAGE_TOKOCASH_WIDGET = "homepage tokocash widget";

    private static final String ACTION_CLICK_HOME_PAGE = "clickHomePage";
    private static final String ACTION_CLICK_VIEW_ALL_PROMO = "slider banner click view all";
    private static final String ACTION_GIMMICK_CLICK = "Click";
    private static final String ACTION_CLICK_WIDGET_BAR = "click widget";
    private static final String ACTION_CLICK_LIHAT_SEMUA_PRODUK = "click lihat semua produk";
    private static final String ACTION_CLICK_TOKO_POINTS = "click tokopoints";
    private static final String ACTION_CLICK_SALDO = "click saldo";
    private static final String ACTION_CLICK_ACTIVATE = "click activate";
    private static final String ACTION_CLICK_JUMP_RECOMENDATION = "cek rekomendasi jumper click";
    private static final String ACTION_IMPRESSION_JUMP_RECOMENDATION = "cek rekomendasi jumper impression";
    private static final String ACTION_CLICK_HOME_USE_CASE = "click 5 use cases";
    private static final String ACTION_CLICK_TAB_EXPLORER = "click explorer tab";
    private static final String ACTION_CLICK_DYNAMIC_ICONS = "click 5 dynamic icons";
    private static final String ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT = "sprint sale click view all";
    private static final String ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT_BACKGROUND = "sprint sale with backgroud click view all";
    private static final String ACTION_CLICK_SEE_ALL_DYNAMIC_CHANNEL = "curated list click view all";
    private static final String ACTION_CLICK_SEE_ALL_LEGO_BANNER_CHANNEL = "lego banner click view all";
    private static final String ACTION_CLICK_OPEN_SHOP = "jual ini itu buka toko";
    private static final String ACTION_CLICK_EDIT_SHOP = "jual ini itu click ubah";

    private static final String LABEL_TOKOPOINTS = "tokopoints";
    private static final String LABEL_EMPTY = "";

    public static AnalyticTracker getTracker(Context context){
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return null;
        }
        return ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
    }

    // GA request
    // replaced by eventPromoImpression(Context context, List<Promotion>promotions) to cater one shot GA.
    @Deprecated
    public static void eventPromoImpression(Context context,
                                            Promotion promotion) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(promotion.getImpressionDataLayer());
        }
    }

    public static void eventPromoImpression(Context context,
                                            List<Promotion> promotions) {
        if (promotions == null || promotions.size() == 0) {
            return;
        }
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < promotions.size(); i++) {
            Promotion promotion = promotions.get(i);
            list.add(promotion.getImpressionDataLayerItem());
        }
        if (list.size() == 0) {
            return;
        }
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage",
                    "eventAction", "slider banner impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            list.toArray()
                                    )
                            )
                    ),
                    "attribution", "1 - sliderBanner"
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventPromoClick(Context context, Promotion promotion) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(promotion.getClickDataLayer());
        }
    }

    public static void eventClickViewAllPromo(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if(tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_VIEW_ALL_PROMO,
                    LABEL_EMPTY
            );
        }
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
                    ACTION_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_JUMP_RECOMENDATION,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventImpressionJumpRecomendation(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if(tracker != null){
            tracker.sendEventTracking(
                    EVENT_IMPRESSION_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_IMPRESSION_JUMP_RECOMENDATION,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickHomeUseCase(Context context, String title) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_HOME_USE_CASE,
                    title
            );
        }
    }

    public static void eventClickTabExplorer(Context context,
                                             String title) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_TAB_EXPLORER,
                    title
            );
        }
    }

    public static void eventClickDynamicIcons(Context context, String title) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_DYNAMIC_ICONS,
                    title
            );
        }
    }

    public static void eventClickSeeAllProductSprint(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickSeeAllProductSprintBackground(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT_BACKGROUND,
                    LABEL_EMPTY
            );
        }
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

    public static void eventEnhancedClickDynamicChannelHomePage(Context context,
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
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_SEE_ALL_DYNAMIC_CHANNEL,
                    applink
            );
        }
    }

    public static void eventClickSeeAllLegoBannerChannel(Context context,
                                                         String applink) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_SEE_ALL_LEGO_BANNER_CHANNEL,
                    applink
            );
        }
    }

    public static void eventClickExplorerItem(Context context, String action, String label) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
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
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_OPEN_SHOP,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickEditShop(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_EDIT_SHOP,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventHomeGimmick(Context context, String label) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_GIMMICK,
                    CATEGORY_GIMMICK,
                    ACTION_GIMMICK_CLICK,
                    label
            );
        }
    }

    public static void eventClickWidgetBar(Context context, String categoryItem) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_DIGITAL_WIDGET,
                    ACTION_CLICK_WIDGET_BAR,
                    categoryItem
            );
        }
    }

    public static void eventClickLihatSemua(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_DIGITAL,
                    ACTION_CLICK_LIHAT_SEMUA_PRODUK,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventUserProfileTokopoints(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_TOKO_POINT,
                    CATEGORY_TOKOPOINTS_USER_PAGE,
                    ACTION_CLICK_TOKO_POINTS,
                    LABEL_TOKOPOINTS
            );
        }
    }

    public static void eventTokoCashActivateClick(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_TOKOCASH_WIDGET,
                    ACTION_CLICK_ACTIVATE,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventTokoCashCheckSaldoClick(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_TOKOCASH_WIDGET,
                    ACTION_CLICK_SALDO,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventEnhanceImpressionLegoAndCuratedHomePage(
            Context context,
            List<Object> legoAndCuratedList) {

        AnalyticTracker tracker = getTracker(context);

        Map<String, Object> data = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "homepage",
                "eventAction", "home banner impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        legoAndCuratedList.toArray(new Object[legoAndCuratedList.size()])
                                )
                        )
                ),
                "attribution", "2 - homeBanner"
        );
        tracker.sendEnhancedEcommerce(data);
    }

}
