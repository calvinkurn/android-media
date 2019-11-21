package com.tokopedia.home.analytics;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerFeedViewModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.FeedTabModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeFeedViewModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String CATEGORY_HOMEPAGE_TOKOPOINTS = "homepage-tokopoints";
    private static final String ACTION_CLICK_POINT = "click point & tier status";

    private static final String EVENT_CLICK_HOME_PAGE = "clickHomePage";
    private static final String EVENT_CLICK_HOME_PAGE_WISHLIST = "clickHomepage";
    private static final String EVENT_GIMMICK = "clickGimmick";
    private static final String EVENT_USER_INTERACTION_HOMEPAGE = "userInteractionHomePage";
    private static final String EVENT_TOKO_POINT = "eventTokopoint";
    private static final String EVENT_IMPRESSION_HOME_PAGE = "eventImpressionHomePage";

    public static final String CATEGORY_HOME_PAGE = "homepage";
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
    private static final String ACTION_CLICK_SEE_ALL_DC_BANNER_CHANNEL = "lego banner gif click view all";
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
    public static final String EVENT_LEGO_BANNER_IMPRESSION = "lego banner gif impression";
    public static final String EVENT_LEGO_BANNER_CLICK = "lego banner gif click";
    public static final String EVENT_ACTION_PRODUCT_RECOMMENDATION_CLICK_NON_LOGIN = "product recommendation click - non login";
    public static final String CLICK = "click";
    public static final String ACTION_FIELD = "actionField";
    public static final String LIST = "list";
    public static final String LIST_CLICK_FEED_HOME = "/ - p2 - %s - rekomendasi untuk anda - %s";
    public static final String LIST_CLICK_FEED_HOME_NON_LOGIN = "/ - p2 - non login - %s - rekomendasi untuk anda - %s";
    public static final String PRODUCTS = "products";
    public static final String PRODUCT_CLICK = "productClick";
    public static final String PROMOTIONS_NAME = "/ - p1 - lego banner gif - %s";
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
    public static final String EVENT_ACTION_CLICK_ON_ALLOW_GEOLOCATION = "click on allow geolocation";
    public static final String EVENT_ACTION_CLICK_ON_NOT_ALLOW_GEOLOCATION = "click on not allow geolocation";
    public static final String EVENT_ACTION_CLICK_ON_GEOLOCATION_COMPONENT = "click on geolocation component";
    public static final String EVENT_ACTION_CLICK_CLOSE_ON_GEOLOCATION_COMPONENT = "click close on geolocation component";
    public static final String EVENT_ACTION_CLICK_ON_ATUR = "click on atur";
    public static final String ACTION_CLICK_VIEW_ALL_ON_DYNAMIC_CHANNEL_MIX = "click view all on dynamic channel mix";

    public static final String CHANNEL_ID = "channelId";

    private static final String VALUE_PROMO_NAME_SIX_BANNER = "/ - p%s - lego banner - %s";
    private static final String VALUE_PROMO_NAME_THREE_BANNER = "/ - p%s - lego banner 3 image - %s";
    private static final String VALUE_PROMO_NAME_PRODUCT = "/ - p%s - %s";
    private static final String VALUE_PROMO_NAME_SPOTLIGHT_BANNER = "/ - p%s - spotlight banner";
    public static final String EVENT_PROMO_VIEW_IRIS = "promoViewIris";
    public static final String EVENT_ACTION_IMPRESSION_ON_BANNER_SPOTLIGHT = "impression on banner spotlight";
    public static final String EVENT_ACTION_LEGO_BANNER_3_IMAGE_IMPRESSION = "lego banner 3 image impression";
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CREATIVE = "creative";
    public static final String FIELD_CREATIVE_URL = "creative_url";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_BRAND = "brand";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_VARIANT = "variant";
    public static final String PRODUCT_VIEW_IRIS = "productViewIris";
    public static final String EVENT_ACTION_IMPRESSION_ON_LEGO_PRODUCT = "impression on lego product";
    public static final String NONE_OTHER = "none / other";
    public static final String PROMO_VIEW_IRIS = "promoViewIris";
    public static final String EVENT_ACTION_LEGO_BANNER_IMPRESSION = "lego banner impression";
    public static final String EVENT_ACTION_IMPRESSION_ON_PRODUCT_DYNAMIC_CHANNEL_MIX = "impression on product dynamic channel mix";
    public static final String EVENT_ACTION_IMPRESSION_ON_BANNER_DYNAMIC_CHANNEL_MIX = "impression on banner dynamic channel mix";
    public static final String VALUE_NAME_PROMO_OVERLAY = "/ - p1 - promo overlay";
    public static final String ACTION_OVERLAY_SLIDER_BANNER_IMPRESSION = "overlay slider banner impression";
    public static final String VALUE_NAME_PROMO = "/ - p1 - promo";
    public static final String ACTION_SLIDER_BANNER_IMPRESSION = "slider banner impression";
    public static final String VALUE_EVENT_ACTION_SLIDER_BANNER_CLICK = "slider banner click";
    public static final String VALUE_EVENT_ACTION_SLIDER_BANNER_OVERLAY_CLICK = "overlay slider banner click";
    public static final String FIELD_PROMO_CODE = "promo_code";
    public static final String NO_PROMO_CODE = "NoPromoCode";
    public static final String EVENT_ACTION_CLICK_ON_DYNAMIC_ICON = "click on dynamic icon";
    public static final String VALUE_NAME_DYNAMIC_ICON = "/ - dynamic icon";
    public static final String EVENT_ACTION_IMPRESSION_ON_DYNAMIC_ICON = "impression on dynamic icon";
    public static final String SCREEN_DIMENSION_IS_LOGGED_IN_STATUS = "isLoggedInStatus";

    public static ContextAnalytics getTracker() {
        return TrackApp.getInstance().getGTM();
    }

    public static void eventPromoClick(Context context, BannerSlidesModel slidesModel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(getSliderBannerClick(
                    slidesModel
            ));
        }
    }

    public static void eventPromoOverlayClick(Context context, BannerSlidesModel slidesModel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(getSliderBannerOverlayClick(
                    slidesModel
            ));
        }
    }

    private static Map<String, Object> getSliderBannerClick(BannerSlidesModel slidesModel) {
        String promoCode = "";
        if (slidesModel.getPromoCode() != null) {
            promoCode = slidesModel.getPromoCode().isEmpty()?slidesModel.getPromoCode(): NO_PROMO_CODE;
        }
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, VALUE_EVENT_ACTION_SLIDER_BANNER_CLICK,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, String.valueOf(slidesModel.getId()),
                                                FIELD_NAME, VALUE_NAME_PROMO,
                                                FIELD_CREATIVE, slidesModel.getCreativeName(),
                                                FIELD_CREATIVE_URL, slidesModel.getImageUrl(),
                                                FIELD_POSITION, String.valueOf(slidesModel.getPosition()),
                                                FIELD_PROMO_CODE, promoCode
                                        )
                                )
                        )
                )
        );
    }

    private static Map<String, Object> getSliderBannerOverlayClick(BannerSlidesModel slidesModel) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, VALUE_EVENT_ACTION_SLIDER_BANNER_OVERLAY_CLICK,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, String.valueOf(slidesModel.getId()),
                                                FIELD_NAME, VALUE_NAME_PROMO_OVERLAY,
                                                FIELD_CREATIVE, slidesModel.getCreativeName(),
                                                FIELD_CREATIVE_URL, slidesModel.getImageUrl(),
                                                FIELD_POSITION, String.valueOf(slidesModel.getPosition()),
                                                FIELD_PROMO_CODE, slidesModel.getPromoCode().isEmpty()?slidesModel.getPromoCode():NO_PROMO_CODE
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickViewAllPromo(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_VIEW_ALL_PROMO,
                    LABEL_EMPTY
            );
        }
    }

    public static void sendScreen(Activity activity, String screenName, Boolean isUserLoggedIn) {
        if (activity != null) {
            ContextAnalytics tracker = getTracker();
            if (tracker != null) {
                HashMap<String, String> customDimensions = new HashMap<>();
                customDimensions.put(SCREEN_DIMENSION_IS_LOGGED_IN_STATUS, isUserLoggedIn.toString());

                tracker.sendScreenAuthenticated(
                        screenName, customDimensions);
            }
        }
    }

    public static void eventClickJumpRecomendation(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    ACTION_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_JUMP_RECOMENDATION,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickHomeUseCase(Context context, String title) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_HOME_USE_CASE,
                    title
            );
        }
    }

    public static void eventClickTabExplorer(Context context,
                                             String title) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_TAB_EXPLORER,
                    title
            );
        }
    }

    public static void eventEnhancedClickDynamicIconHomePage(Context context, HomeIconItem homeIconItem, int position) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    getEnhanceClickDynamicIconHomePage(position, homeIconItem)
            );
        }
    }

    public static void eventClickSeeAllProductSprint(Context context, String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT);
        map.put(EVENT_LABEL, LABEL_EMPTY);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllLegoProduct(Context context, String headerName, String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_LEGO_PRODUCT);
        map.put(EVENT_LABEL, headerName);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllProductSprintBackground(Context context, String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_PRODUCT_SPRINT_BACKGROUND);
        map.put(EVENT_LABEL, LABEL_EMPTY);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventEnhancedClickSprintSaleProduct(Context context,
                                                           Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventEnhancedImpressionWidgetHomePage(TrackingQueue trackingQueue,
                                                                     Map<String, Object> data) {
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventEnhancedImpressionDynamicIconHomePage(TrackingQueue trackingQueue,
                                                                  Map<String, Object> data) {
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventEnhancedClickDynamicChannelHomePage(Context context,
                                                                Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    data
            );
        }
    }

    public static void eventClickSeeAllDynamicChannel(Context context, String applink, String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_DYNAMIC_CHANNEL);
        map.put(EVENT_LABEL, applink);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllLegoBannerChannel(Context context,
                                                         String applink,
                                                         String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_LEGO_BANNER_CHANNEL);
        map.put(EVENT_LABEL, applink);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllGifDCBannerChannel(Context context,
                                                         String headerName,
                                                         String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_DC_BANNER_CHANNEL);
        map.put(EVENT_LABEL, headerName);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllThreeLegoBannerChannel(Context context,
                                                              String headerName,
                                                              String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_LEGO_THREE_IMAGE_BANNER_CHANNEL);
        map.put(EVENT_LABEL, headerName);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllBannerMixChannel(Context context, String channelId, String headerName) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_VIEW_ALL_ON_DYNAMIC_CHANNEL_MIX,
                    headerName
            );
        }
    }

    public static void eventClickExplorerItem(Context context, String action, String label) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    action,
                    label
            );
        }
    }

    public static void eventEnhancedImpressionFavoriteCategory(Context context,
                                                               Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventEnhancedClickFavoriteCategory(Context context,
                                                          Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventEnhancedImpressionProductHomePage(Context context,
                                                              Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventEnhancedClickProductHomePage(Context context,
                                                         Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventClickOpenShop(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_OPEN_SHOP,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickEditShop(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    ACTION_CLICK_EDIT_SHOP,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventHomeGimmick(Context context, String label) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_GIMMICK,
                    CATEGORY_GIMMICK,
                    ACTION_GIMMICK_CLICK,
                    label
            );
        }
    }

    public static void eventClickWidgetBar(Context context, String categoryItem) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_DIGITAL_WIDGET,
                    ACTION_CLICK_WIDGET_BAR,
                    categoryItem
            );
        }
    }

    public static void eventClickLihatSemua(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_DIGITAL,
                    ACTION_CLICK_LIHAT_SEMUA_PRODUK,
                    LABEL_EMPTY
            ));
        }
    }

    public static void eventUserProfileTokopoints(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_TOKO_POINT,
                    CATEGORY_TOKOPOINTS_USER_PAGE,
                    ACTION_CLICK_TOKO_POINTS,
                    LABEL_TOKOPOINTS
            ));
        }
    }

    public static void eventTokoCashActivateClick(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_TOKOCASH_WIDGET,
                    ACTION_CLICK_ACTIVATE,
                    LABEL_EMPTY
            ));
        }
    }

    public static void eventTokoCashCheckSaldoClick(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_USER_INTERACTION_HOMEPAGE,
                    CATEGORY_HOMEPAGE_TOKOCASH_WIDGET,
                    ACTION_CLICK_SALDO,
                    LABEL_EMPTY
            ));
        }
    }

    public static void eventEnhanceImpressionLegoAndCuratedHomePage(
            TrackingQueue trackingQueue,
            List<Object> legoAndCuratedList) {

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
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventClickOnHomePageRecommendationTab(
            Context context,
            FeedTabModel feedTabModel) {

        ContextAnalytics tracker = getTracker();

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
        tracker.sendEnhanceEcommerceEvent(data);
    }

    public static void eventImpressionOnProductRecommendationForLoggedInUser(
            TrackingQueue trackingQueue,
            HomeFeedViewModel feedViewModel,
            String tabName) {
        if (trackingQueue == null) {
            return;
        }

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

        if (trackingQueue == null) {
            return;
        }

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
            Context context,
            HomeFeedViewModel homeFeedViewModel,
            String tabName) {

        ContextAnalytics tracker = getTracker();

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
        tracker.sendEnhanceEcommerceEvent(data);
    }

    public static void eventClickOnHomeProductFeedForNonLoginUser(
            Context context,
            HomeFeedViewModel homeFeedViewModel,
            String tabName) {

        ContextAnalytics tracker = getTracker();

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
        tracker.sendEnhanceEcommerceEvent(data);
    }

    public static void eventClickWishlistOnProductRecommendation(Context context, String tabName) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_HOME_PAGE_WISHLIST,
                    CATEGORY_HOME_PAGE,
                    ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION,
                    tabName
            ));
        }
    }

    public static void eventClickRemoveWishlistOnProductRecommendation(Context context, String tabName) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_HOME_PAGE_WISHLIST,
                    CATEGORY_HOME_PAGE,
                    ACTION_REMOVE_WISHLIST_ON_PRODUCT_RECOMMENDATION,
                    tabName
            ));
        }
    }

    public static void eventClickWishlistOnProductRecommendationForNonLogin(Context context, String tabName) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(TrackAppUtils.gtmData(
                    EVENT_CLICK_HOME_PAGE_WISHLIST,
                    CATEGORY_HOME_PAGE,
                    ACTION_ADD_WISHLIST_ON_PRODUCT_RECOMMENDATION_NON_LOGIN,
                    tabName
            ));
        }
    }

    public static void eventClickTickerHomePage(Context context, String tickerId) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_TICKER,
                    EVENT_CATEGORY_TICKER_HOMEPAGE,
                    EVENT_ACTION_CLICK_TICKER,
                    tickerId
            );
        }
    }

    public static void eventClickOnCloseTickerHomePage(Context context, String tickerId) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_TICKER,
                    EVENT_CATEGORY_TICKER_HOMEPAGE,
                    EVENT_ACTION_CLICK_ON_CLOSE_TICKER,
                    tickerId
            );
        }
    }

    public static void eventOvo(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    String.format("%s %s %s", CLICK, ON, OVO),
                    ""
            );
        }
    }

    public static void eventQrCode(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    String.format("%s %s %s", CLICK, ON, QR_CODE),
                    ""
            );
        }
    }

    public static void eventTokopointNonLogin(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
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
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, "impression on bu widget",
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, id,
                                                FIELD_NAME, name,
                                                FIELD_CREATIVE, alias,
                                                FIELD_CREATIVE_URL, creativeUrl,
                                                FIELD_POSITION, position,
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
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            Map<String, Object> data = DataLayer.mapOf(
                    "event", PROMO_CLICK,
                    "eventCategory", CATEGORY_HOME_PAGE,
                    "eventAction", "click on bu widget",
                    "eventLabel", alias,
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    FIELD_ID, id,
                                                    FIELD_NAME, name,
                                                    FIELD_CREATIVE, alias,
                                                    FIELD_CREATIVE_URL, creativeUrl,
                                                    FIELD_POSITION, position,
                                                    "promo_code", !TextUtils.isEmpty(promoCode) ? promoCode : "NoPromoCode"
                                            )
                                    )
                            )
                    )
            );
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventClickTabHomeWidget(Context context, String headerName) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    "click on bu widget tab",
                    headerName
            );
        }
    }

    //on permission
    public static void eventClickAllowGeolocation(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    EVENT_ACTION_CLICK_ON_ALLOW_GEOLOCATION,
                    LABEL_EMPTY
            );
        }
    }

    //on permission
    public static void eventClickNotAllowGeolocation(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    EVENT_ACTION_CLICK_ON_NOT_ALLOW_GEOLOCATION,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickGeolocationComponent(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    EVENT_ACTION_CLICK_ON_GEOLOCATION_COMPONENT,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickCloseGeolocationComponent(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    EVENT_ACTION_CLICK_CLOSE_ON_GEOLOCATION_COMPONENT,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventClickOnAtur(Context context) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    EVENT_CLICK_HOME_PAGE,
                    CATEGORY_HOME_PAGE,
                    EVENT_ACTION_CLICK_ON_ATUR,
                    LABEL_EMPTY
            );
        }
    }

    public static void eventImpressionOnBannerFeed(
            TrackingQueue trackingQueue,
            BannerFeedViewModel bannerFeedViewModel,
            String tabName) {

        if (trackingQueue == null) {
            return;
        }

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, "impression on banner inside recommendation tab",
                EVENT_LABEL, tabName,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS,
                                convertBannerFeedViewModelListToObjectData(bannerFeedViewModel, tabName)
                        )
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    private static List<Object> convertBannerFeedViewModelListToObjectData(
            BannerFeedViewModel bannerFeedViewModel,
            String tabName
    ) {
        List<Object> objects = new ArrayList<>();
        objects.add(bannerFeedViewModel.convertBannerFeedModelToDataLayer(tabName));
        return objects;
    }

    public static void eventClickOnBannerFeed(
            Context context,
            BannerFeedViewModel bannerFeedViewModel,
            String tabName) {

        ContextAnalytics tracker = getTracker();

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, "click on banner inside recommendation tab",
                EVENT_LABEL, tabName,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS,
                                convertBannerFeedViewModelListToObjectData(bannerFeedViewModel, tabName)
                        )
                )
        );
        tracker.sendEnhanceEcommerceEvent(data);
    }

    public static void eventEnhanceImpressionBanner(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceImpressionBannerChannelMix()
                    );
        }
    }

    public static void eventEnhanceImpressionPlayBanner(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceImpressionPlayBanner()
            );
        }
    }

    public static void eventClickPlayBanner(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceClickPlayBanner()
            );
        }
    }

    public static void eventEnhanceImpressionBannerGif(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            EVENT, PROMO_VIEW,
                            EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                            EVENT_ACTION, EVENT_LEGO_BANNER_IMPRESSION,
                            EVENT_LABEL, "",
                            CHANNEL_ID, bannerChannel.getId(),
                            ECOMMERCE, DataLayer.mapOf(
                                    PROMO_VIEW, DataLayer.mapOf(
                                            PROMOTIONS, DataLayer.listOf(
                                                    DataLayer.mapOf(
                                                            FIELD_ID, bannerChannel.getBanner().getId(),
                                                            FIELD_NAME, String.format(PROMOTIONS_NAME, bannerChannel.getHeader().getName()),
                                                            FIELD_CREATIVE, bannerChannel.getBanner().getAttribution(),
                                                            FIELD_CREATIVE_URL, bannerChannel.getBanner().getImageUrl(),
                                                            FIELD_POSITION, String.valueOf(1)
                                                    )
                                            )
                                    )

                            )
                    )
            );
        }
    }

    public static void eventEnhanceClickBannerGif(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            EVENT, PROMO_CLICK,
                            EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                            EVENT_ACTION, EVENT_LEGO_BANNER_CLICK,
                            EVENT_LABEL, bannerChannel.getName(),
                            CHANNEL_ID, bannerChannel.getId(),
                            ECOMMERCE, DataLayer.mapOf(
                                    PROMO_CLICK, DataLayer.mapOf(
                                            PROMOTIONS, DataLayer.listOf(
                                                    DataLayer.mapOf(
                                                            FIELD_ID, bannerChannel.getBanner().getId(),
                                                            FIELD_NAME, String.format(PROMOTIONS_NAME, bannerChannel.getHeader().getName()),
                                                            FIELD_CREATIVE, bannerChannel.getBanner().getAttribution(),
                                                            FIELD_CREATIVE_URL, bannerChannel.getBanner().getImageUrl(),
                                                            FIELD_POSITION, String.valueOf(1)
                                                    )
                                            )
                                    )

                            )
                    )
            );
        }
    }

    public static void eventClickProductChannelMix(Context context,
                                                   DynamicHomeChannel.Channels bannerChannel,
                                                   boolean isFreeOngkir,
                                                   int gridPosition) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceClickProductChannelMix(gridPosition, isFreeOngkir)
            );
        }
    }

    public static void eventClickBannerChannelMix(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceClickBannerChannelMix()
            );
        }
    }

    public static void eventClickBannerButtonChannelMix(Context context, DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceClickBannerButtonChannelMix()
            );
        }
    }

    private static String getHomeAttribution(int position, String creativeName, String homeAttribution) {
        if (homeAttribution != null)
            return homeAttribution.replace("$1", Integer.toString(position)).replace("$2", (creativeName != null) ? creativeName : "");
        return "";
    }

    public static HashMap<String, Object> getEnhanceImpressionSprintSaleHomePage(
            String channelId,
            DynamicHomeChannel.Grid[] grid,
            int position
    ) {
        List<Object> list = convertProductEnhanceSprintSaleDataLayer(grid);
        return (HashMap<String, Object>) DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "homepage",
                "eventAction", "sprint sale impression",
                "eventLabel", "",
                CHANNEL_ID, channelId,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                list.toArray(new Object[list.size()])

                        ))
        );
    }

    private static List<Object> convertProductEnhanceSprintSaleDataLayer(DynamicHomeChannel.Grid[] grids) {
        List<Object> list = new ArrayList<>();

        if (grids != null) {
            for (int i = 0; i < grids.length; i++) {
                DynamicHomeChannel.Grid grid = grids[i];
                list.add(
                        DataLayer.mapOf(
                                FIELD_NAME, grid.getName(),
                                FIELD_ID, grid.getId(),
                                FIELD_PRICE, Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                        grid.getPrice()
                                )),
                                FIELD_BRAND, "none / other",
                                FIELD_CATEGORY, "none / other",
                                FIELD_VARIANT, "none / other",
                                LIST, "/ - p1 - sprint sale",
                                FIELD_POSITION, String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }

    public static HashMap<String, Object> getIrisEnhanceImpressionDynamicSprintLegoHomePage(String channelId,
                                                                                            DynamicHomeChannel.Grid[] grids,
                                                                                            String headerName) {
        List<Object> list = convertPromoEnhanceDynamicSprintLegoDataLayer(grids, headerName);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PRODUCT_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_ON_LEGO_PRODUCT,
                EVENT_LABEL, LABEL_EMPTY,
                CHANNEL_ID, channelId,
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(
                                list.toArray(new Object[list.size()])
                        )
                )
        );
    }

    private static List<Object> convertPromoEnhanceDynamicSprintLegoDataLayer(DynamicHomeChannel.Grid[] grids,
                                                                       String headerName) {
        List<Object> list = new ArrayList<>();

        if (grids != null) {
            for (int i = 0; i < grids.length; i++) {
                DynamicHomeChannel.Grid grid = grids[i];
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, grid.getId(),
                                FIELD_NAME, grid.getName(),
                                FIELD_PRICE, Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                        grid.getPrice()
                                )),
                                FIELD_BRAND, NONE_OTHER,
                                FIELD_CATEGORY, NONE_OTHER,
                                FIELD_VARIANT, NONE_OTHER,
                                LIST, "/ - p1 - lego product - " + headerName,
                                FIELD_POSITION, String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }

    public static HashMap<String, Object> getEnhanceImpressionProductChannelMix(DynamicHomeChannel.Channels channel,
                                                                     String type){
        List<Object> list = convertProductEnhanceProductMixDataLayer(channel.getGrids(), channel.getHeader().getName(), type);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PRODUCT_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_ON_PRODUCT_DYNAMIC_CHANNEL_MIX,
                EVENT_LABEL, LABEL_EMPTY,
                CHANNEL_ID, channel.getId(),
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(
                                list.toArray(new Object[list.size()])

                        ))
        );
    }

    /**
     * Position always 1 cause just 1 gif dc
     * @param channel
     * @return
     */
    public static HashMap<String, Object> getEnhanceImpressionPromoGifBannerDC(DynamicHomeChannel.Channels channel){
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_LEGO_BANNER_IMPRESSION,
                EVENT_LABEL, "",
                CHANNEL_ID, channel.getId(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, channel.getBanner().getId(),
                                                FIELD_NAME, String.format(PROMOTIONS_NAME, channel.getHeader().getName()),
                                                FIELD_CREATIVE, channel.getBanner().getAttribution(),
                                                FIELD_CREATIVE_URL, channel.getBanner().getImageUrl(),
                                                FIELD_POSITION, String.valueOf(1)
                                        )
                                )
                        )

                )
        );
    }

    private static List<Object> convertProductEnhanceProductMixDataLayer(DynamicHomeChannel.Grid[] grids, String headerName, String type) {
        List<Object> list = new ArrayList<>();

        if (grids != null) {
            for (int i = 0; i < grids.length; i++) {
                DynamicHomeChannel.Grid grid = grids[i];
                list.add(
                        DataLayer.mapOf(
                                FIELD_NAME, grid.getName(),
                                FIELD_ID, grid.getId(),
                                FIELD_PRICE, Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                                        grid.getPrice()
                                )),
                                FIELD_BRAND, NONE_OTHER,
                                FIELD_CATEGORY, NONE_OTHER,
                                FIELD_VARIANT, NONE_OTHER,
                                LIST, "/ - p1 - dynamic channel mix - product - "+headerName+" - "+type,
                                FIELD_POSITION, String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }

    public static HashMap<String, Object> getIrisEnhanceImpressionBannerChannelMix(DynamicHomeChannel.Channels channel) {
        List<Object> list = convertPromoEnhanceBannerChannelMix(channel);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_ON_BANNER_DYNAMIC_CHANNEL_MIX,
                EVENT_LABEL, LABEL_EMPTY,
                CHANNEL_ID, channel.getId(),
                ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )

                )
        );
    }

    private static List<Object> convertPromoEnhanceBannerChannelMix(DynamicHomeChannel.Channels channel) {
        List<Object> list = new ArrayList<>();

        /**
         * Banner always in position 1 because only 1 banner shown
         */
        list.add(
                DataLayer.mapOf(
                        FIELD_ID, channel.getBanner().getId(),
                        FIELD_NAME, "/ - p1 - dynamic channel mix - banner - "+channel.getHeader().getName(),
                        FIELD_CREATIVE, channel.getBanner().getAttribution(),
                        FIELD_CREATIVE_URL, channel.getBanner().getImageUrl(),
                        FIELD_POSITION, String.valueOf(1)
                )
        );
        return list;
    }

    public static HashMap<String, Object> getEnhanceImpressionLegoBannerHomePage(
            String channelId,
            DynamicHomeChannel.Grid[] grids,
            String headerName,
            int position) {
        String promoName = String.format(
                VALUE_PROMO_NAME_SIX_BANNER,
                String.valueOf(position),
                headerName
        );

        List<Object> list = convertPromoEnhanceLegoBannerDataLayer(
                grids,
                promoName);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_LEGO_BANNER_IMPRESSION,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                ),
                CHANNEL_ID, channelId
        );
    }

    public static HashMap<String, Object> getIrisEnhanceImpressionLegoThreeBannerHomePage(
            String channelId,
            DynamicHomeChannel.Grid[] grids,
            String headerName,
            int position) {
        String promoName = String.format(
                VALUE_PROMO_NAME_THREE_BANNER,
                String.valueOf(position),
                headerName
        );

        List<Object> list = convertPromoEnhanceLegoBannerDataLayer(
                grids,
                promoName);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, EVENT_PROMO_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_LEGO_BANNER_3_IMAGE_IMPRESSION,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                ),
                CHANNEL_ID, channelId
        );
    }

    private static List<Object> convertPromoEnhanceLegoBannerDataLayer(DynamicHomeChannel.Grid[] grids, String promoName) {
        List<Object> list = new ArrayList<>();

        if (grids != null) {
            for (int i = 0; i < grids.length; i++) {
                DynamicHomeChannel.Grid grid = grids[i];
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, grid.getId(),
                                FIELD_NAME, promoName,
                                FIELD_CREATIVE, grid.getAttribution(),
                                FIELD_CREATIVE_URL, grid.getImageUrl(),
                                FIELD_POSITION, String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }

    public static HashMap<String, Object> getIrisEnhanceImpressionSpotlightHomePage(
            String channelId,
            List<SpotlightItemViewModel> spotlights,
            int position) {
        List<Object> list = convertPromoEnhanceSpotlight(spotlights, position);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, EVENT_PROMO_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_ON_BANNER_SPOTLIGHT,
                EVENT_LABEL, LABEL_EMPTY,
                CHANNEL_ID, channelId,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                )
        );
    }

    private static List<Object> convertPromoEnhanceSpotlight(List<SpotlightItemViewModel> spotlights,
                                                             int position) {
        List<Object> list = new ArrayList<>();
        String promoName = String.format(
                VALUE_PROMO_NAME_SPOTLIGHT_BANNER,
                String.valueOf(position)
        );

        if (spotlights != null) {
            for (int i = 0; i < spotlights.size(); i++) {
                SpotlightItemViewModel item = spotlights.get(i);
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, String.valueOf(item.getId()),
                                FIELD_NAME, promoName,
                                FIELD_CREATIVE, item.getTitle(),
                                FIELD_CREATIVE_URL, item.getBackgroundImageUrl(),
                                FIELD_POSITION, String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }

    public static HashMap<String,Object> getBannerImpressionDataLayer(List<BannerSlidesModel> bannerOverlaySlides) {
        List<Map<String, Object>> listBanner = convertSliderBannerImpressionDataLayer(bannerOverlaySlides);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, ACTION_SLIDER_BANNER_IMPRESSION,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        listBanner
                                )
                        )
                )
        );
    }

    public static Map<String, Object> getEnhanceClickDynamicIconHomePage(int position, HomeIconItem homeIconItem) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_DYNAMIC_ICON,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, homeIconItem.getId(),
                                                FIELD_NAME, VALUE_NAME_DYNAMIC_ICON,
                                                FIELD_CREATIVE, homeIconItem.getBuIdentifier(),
                                                FIELD_CREATIVE_URL, homeIconItem.getIcon(),
                                                FIELD_POSITION, String.valueOf(position+1)
                                        )
                                )
                        )
                )
        );
    }

    public static HashMap<String, Object> getBannerOverlayPersoImpressionDataLayer(List<BannerSlidesModel> bannerOverlaySlides) {
        List<Object> listBanner = convertOverlaySliderBannerImpressionDataLayer(bannerOverlaySlides);
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, ACTION_OVERLAY_SLIDER_BANNER_IMPRESSION,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        listBanner
                                )
                        )
                )
        );
    }

    private static List<Object> convertOverlaySliderBannerImpressionDataLayer(List<BannerSlidesModel> bannerSlidesModels) {
        List<Object> list = new ArrayList<>();
        if (bannerSlidesModels != null) {
            for (int i = 0; i < bannerSlidesModels.size(); i++) {
                BannerSlidesModel bannerSlidesModel = bannerSlidesModels.get(i);
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, String.valueOf(bannerSlidesModel.getId()),
                                FIELD_NAME, VALUE_NAME_PROMO_OVERLAY,
                                FIELD_CREATIVE, bannerSlidesModel.getCreativeName(),
                                FIELD_CREATIVE_URL, bannerSlidesModel.getImageUrl(),
                                FIELD_POSITION, String.valueOf(bannerSlidesModel.getPosition())
                        )
                );
            }
        }
        return list;
    }

    private static List<Map<String, Object>> convertSliderBannerImpressionDataLayer(List<BannerSlidesModel> bannerSlidesModels) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (bannerSlidesModels != null) {
            for (int i = 0; i < bannerSlidesModels.size(); i++) {
                BannerSlidesModel bannerSlidesModel = bannerSlidesModels.get(i);
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, String.valueOf(bannerSlidesModel.getId()),
                                FIELD_NAME, VALUE_NAME_PROMO,
                                FIELD_CREATIVE, bannerSlidesModel.getCreativeName(),
                                FIELD_CREATIVE_URL, bannerSlidesModel.getImageUrl(),
                                FIELD_POSITION, String.valueOf(bannerSlidesModel.getPosition())
                        )
                );
            }
        }
        return list;
    }

    public static Map<String, Object> getEnhanceImpressionDynamicIconHomePage(List<HomeIconItem> homeIconItem) {
        List<Object> list = convertEnhanceDynamicIcon(homeIconItem);
        return DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_IMPRESSION_ON_DYNAMIC_ICON,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                )
                        )
                )
        );
    }

    private static List<Object> convertEnhanceDynamicIcon(List<HomeIconItem> homeIconItem) {
        List<Object> list = new ArrayList<>();

        if (homeIconItem != null) {
            for (int i = 0; i < homeIconItem.size(); i++) {
                HomeIconItem item = homeIconItem.get(i);
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, item.getId(),
                                FIELD_NAME, VALUE_NAME_DYNAMIC_ICON,
                                FIELD_CREATIVE, item.getBuIdentifier(),
                                FIELD_CREATIVE_URL, item.getIcon(),
                                FIELD_POSITION, String.valueOf(i + 1)
                        )
                );
            }
        }
        return list;
    }

    public static void sendTokopointTrackerClick() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                TrackAppUtils.gtmData(
                        HomePageTracking.EVENT_TOKO_POINT,
                        HomePageTracking.CATEGORY_HOMEPAGE_TOKOPOINTS,
                        HomePageTracking.ACTION_CLICK_POINT,
                        HomePageTracking.LABEL_TOKOPOINTS));
    }

    public static void homeReviewImpression(
            TrackingQueue trackingQueue,
            SuggestedProductReviewResponse reviewData,
            int position,
            String orderId,
            String productId
    ) {
        List<Object> promotionBody = DataLayer.listOf(DataLayer.mapOf(
                "id", orderId + " - " + productId,
                "name", "product review notification - " + orderId + " - " + productId,
                "creative", "product review notification - " + orderId + " - " + productId,
                "creative_url", reviewData.getImageUrl(),
                "position", Integer.toString(position + 1, 10),
                "category", "",
                "promo_id", null,
                "promo_code", null
        ));


        trackingQueue.putEETracking((HashMap<String, Object>) DataLayer.mapOf(
                EVENT, "promoView",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "view - product review notification",
                EVENT_LABEL, orderId + " - " + productId,
                ECOMMERCE, DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                        "promotions", promotionBody
                )
            )
        ));
    }

    public static void homeReviewOnCloseTracker(String orderId, String productId) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - back button on home product review widget",
                EVENT_LABEL, orderId + " - " + productId
        ));
    }

    public static void homeReviewOnRatingChangedTracker(String orderId, String productId, int starCount) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - product rating stars on home product review widget",
                EVENT_LABEL, orderId + " - " + productId + " - " + Integer.toString(starCount, 10)
        ));
    }

    public static void homeReviewOnBlankSpaceClickTracker(String orderId, String productId) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - home product review widget",
                EVENT_LABEL, orderId + " - " + productId
        ));
    }
}
