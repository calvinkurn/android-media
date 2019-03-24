package com.tokopedia.home.analytics;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.home.beranda.data.model.Promotion;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    private static final String ACTION_CLICK_SEE_ALL_LEGO_PRODUCT = "click view all on lego product";
    private static final String ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT_BACKGROUND = "sprint sale with backgroud click view all";
    private static final String ACTION_CLICK_SEE_ALL_DYNAMIC_CHANNEL = "curated list click view all";
    private static final String ACTION_CLICK_SEE_ALL_LEGO_BANNER_CHANNEL = "lego banner click view all";
    private static final String ACTION_CLICK_SEE_ALL_LEGO_THREE_IMAGE_BANNER_CHANNEL = "lego banner 3 image click view all";
    private static final String ACTION_CLICK_OPEN_SHOP = "jual ini itu buka toko";
    private static final String ACTION_CLICK_EDIT_SHOP = "jual ini itu click ubah";

    private static final String LABEL_TOKOPOINTS = "tokopoints";
    private static final String LABEL_EMPTY = "";
    public static final String EVENT = "event";
    public static final String PROMO_VIEW = "promoView";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION_CLICK_ON_HOMEPAGE_RECOMMENDATION_TAB = "click on homepage recommendation tab";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String ECOMMERCE = "ecommerce";
    public static final String PROMO_CLICK = "promoClick";
    public static final String PROMOTIONS = "promotions";
    public static final String PRODUCT_VIEW = "productView";
    public static final String EVENT_ACTION_PRODUCT_RECOMMENDATION_IMPRESSION = "product recommendation impression";
    public static final String EVENT_ACTION_PRODUCT_RECOMMENDATION_IMPRESSION_NON_LOGIN =
            "product recommendation impression - non login";
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String IDR = "IDR";
    public static final String IMPRESSIONS = "impressions";
    public static final String EVENT_ACTION_PRODUCT_RECOMMENDATION_CLICK = "product recommendation click";
    public static final String EVENT_ACTION_PRODUCT_RECOMMENDATION_CLICK_NON_LOGIN = "product recommendation click - non login";
    public static final String CLICK = "click";
    public static final String ACTION_FIELD = "actionField";
    public static final String LIST = "list";
    public static final String LIST_CLICK_FEED_HOME = "/ - p2 - %s - rekomendasi untuk anda - %s";
    public static final String LIST_CLICK_FEED_HOME_NON_LOGIN = "/ - p2 - non login - %s - rekomendasi untuk anda - %s";
    public static final String PRODUCTS = "products";
    public static final String PRODUCT_CLICK = "productClick";
    public static final String ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION = "add wishlist on product recommendation";
    public static final String ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_NON_LOGIN = "add wishlist on product recommendation - non login";
    public static final String ACTION_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION = "remove wishlist on product recommendation";
    public static final String EVENT_CLICK_TICKER = "clickTicker";
    public static final String EVENT_CATEGORY_TICKER_HOMEPAGE = "ticker homepage";
    public static final String EVENT_ACTION_CLICK_TICKER = "click ticker";
    public static final String EVENT_ACTION_CLICK_ON_CLOSE_TICKER = "click on close ticker";

    public static final String ON = "on";
    public static final String NON_LOGIN = "non login";
    public static final String QR_CODE = "qr code";
    public static final String OVO = "ovo";

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

    public static void eventEnhancedClickDynamicIconHomePage(Context context,
                                                                Map<String, Object> data) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEnhancedEcommerce(
                    data
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

    public static void eventClickSeeAllLegoProduct(Context context, String headerName) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_SEE_ALL_LEGO_PRODUCT,
                    headerName
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

    public static void eventEnhancedImpressionDynamicIconHomePage(Context context,
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

    public static void eventClickSeeAllThreeLegoBannerChannel(Context context,
                                                         String headerName) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_SEE_ALL_LEGO_THREE_IMAGE_BANNER_CHANNEL,
                    headerName
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

    public static void eventClickOnHomePageRecommendationTab(
            TrackingQueue trackingQueue,
            FeedTabModel feedTabModel) {

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_HOMEPAGE_RECOMMENDATION_TAB,
                EVENT_LABEL, feedTabModel.getName(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        feedTabModel.convertFeedTabModelToDataObject()
                                )
                        )
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventImpressionOnProductRecommendationForLoggedInUser(
            TrackingQueue trackingQueue,
            HomeFeedViewModel feedViewModel,
            String tabName) {

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_PRODUCT_RECOMMENDATION_IMPRESSION,
                EVENT_LABEL, tabName,
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS,
                        convertHomeFeedViewModelListToObjectForLoggedInUser(feedViewModel, tabName)
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventImpressionOnProductRecommendationForNonLoginUser(
            TrackingQueue trackingQueue,
            HomeFeedViewModel feedViewModel,
            String tabName) {

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_PRODUCT_RECOMMENDATION_IMPRESSION_NON_LOGIN,
                EVENT_LABEL, tabName,
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS,
                        convertHomeFeedViewModelListToObjectForNonLoginUser(feedViewModel, tabName)
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    private static List<Object> convertHomeFeedViewModelListToObjectForLoggedInUser(
            HomeFeedViewModel feedViewModel,
            String tabName
    ) {
        List<Object> objects = new ArrayList<>();
        objects.add(feedViewModel.convertFeedTabModelToImpressionDataForLoggedInUser(tabName));
        return objects;
    }

    private static List<Object> convertHomeFeedViewModelListToObjectForNonLoginUser(
            HomeFeedViewModel feedViewModel,
            String tabName
    ) {
        List<Object> objects = new ArrayList<>();
        objects.add(feedViewModel.convertFeedTabModelToImpressionDataForNonLoginUser(tabName));
        return objects;
    }

    public static void eventClickOnHomeProductFeedForLoggedInUser(
            TrackingQueue trackingQueue,
            HomeFeedViewModel homeFeedViewModel,
            String tabName) {

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_PRODUCT_RECOMMENDATION_CLICK,
                EVENT_LABEL, tabName,
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                        LIST, String.format(
                                                LIST_CLICK_FEED_HOME,
                                                tabName,
                                                homeFeedViewModel.getRecommendationType()
                                        )),
                                PRODUCTS, DataLayer.listOf(
                                        homeFeedViewModel.convertFeedTabModelToClickData()
                                )
                        )
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventClickOnHomeProductFeedForNonLoginUser(
            TrackingQueue trackingQueue,
            HomeFeedViewModel homeFeedViewModel,
            String tabName) {

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_PRODUCT_RECOMMENDATION_CLICK_NON_LOGIN,
                EVENT_LABEL, tabName,
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                        LIST, String.format(
                                                LIST_CLICK_FEED_HOME_NON_LOGIN,
                                                tabName,
                                                homeFeedViewModel.getRecommendationType()
                                        )),
                                PRODUCTS, DataLayer.listOf(
                                        homeFeedViewModel.convertFeedTabModelToClickData()
                                )
                        )
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventClickWishlistOnProductRecommendation(Context context, String tabName) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION,
                    tabName
            );
        }
    }

    public static void eventClickRemoveWishlistOnProductRecommendation(Context context, String tabName) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION,
                    tabName
            );
        }
    }

    public static void eventClickWishlistOnProductRecommendationForNonLogin(Context context, String tabName) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_NON_LOGIN,
                    tabName
            );
        }
    }

    public static void eventClickTickerHomePage(Context context, String tickerTitle) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_TICKER,
                    EVENT_CATEGORY_TICKER_HOMEPAGE,
                    EVENT_ACTION_CLICK_TICKER,
                    tickerTitle
            );
        }
    }

    public static void eventClickOnCloseTickerHomePage(Context context, String tickerTitle) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_TICKER,
                    EVENT_CATEGORY_TICKER_HOMEPAGE,
                    EVENT_ACTION_CLICK_ON_CLOSE_TICKER,
                    tickerTitle
            );
        }
    }

    public static void eventOvo(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    String.format("%s %s %s", CLICK, ON, OVO),
                    ""
            );
        }
    }

    public static void eventQrCode(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    String.format("%s %s %s", CLICK, ON, QR_CODE),
                    ""
            );
        }
    }

    public static void eventTokopointNonLogin(Context context) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    String.format("%s %s %s - %s", CLICK, ON, LABEL_TOKOPOINTS, NON_LOGIN),
                    ""
            );
        }
    }

    public static void eventEnhancedImpressionHomeWidget(
            @Nullable TrackingQueue trackingQueue,
            @NonNull String id,
            @NonNull String name,
            @NonNull String alias,
            @NonNull String creativeUrl,
            @NonNull String position,
            @NonNull String promoCode
    ) {
        if (trackingQueue == null) {
            return;
        }
        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_TICKER_HOMEPAGE,
                EVENT_ACTION, "impression on bu widget",
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", id,
                                                "name", name,
                                                "creative", alias,
                                                "creative_url", creativeUrl,
                                                "position", position,
                                                "promo_code", !TextUtils.isEmpty(promoCode) ? promoCode : "NoPromoCode"
                                        )
                                )
                        )
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventEnhancedClickHomeWidget(
            @Nullable Context context,
            @NonNull String id,
            @NonNull String name,
            @NonNull String alias,
            @NonNull String creativeUrl,
            @NonNull String position,
            @NonNull String promoCode
    ) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null){
            Map<String, Object> data = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage",
                    "eventAction", "click on bu widget",
                    "eventLabel", name,
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", id,
                                                    "name", name,
                                                    "creative", alias,
                                                    "creative_url", creativeUrl,
                                                    "position", position,
                                                    "promo_code", !TextUtils.isEmpty(promoCode) ? promoCode : "NoPromoCode"
                                            )
                                    )
                            )
                    )
            );
            tracker.sendEnhancedEcommerce(data);
        }
    }

    public static void eventClickTabHomeWidget(Context context, String headerName) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            tracker.sendEventTracking(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    "click on bu widget tab",
                    headerName
            );
        }
    }
}
