package com.tokopedia.home.analytics;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.utils.text.currency.CurrencyFormatHelper;
import com.tokopedia.home.analytics.v2.BaseTracking;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel;
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PlayCardDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.dynamic_icon.HomeIconItem;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.spotlight.SpotlightItemDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel;
import com.tokopedia.home_component.model.ChannelGrid;
import com.tokopedia.home_component.model.ChannelModel;
import com.tokopedia.home_component.model.TrackingAttributionModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.iris.util.ConstantKt;

/**
 * Created by Akmal on 2/6/18.
 */

public class HomePageTracking {


    public static final String FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    public static final String FORMAT_2_VALUE_UNDERSCORE = "%s_%s";

    public static final String FORMAT_3_VALUE_SPACE = "%s %s %s";

    public static final String BELI_INI_ITU_CLICK = "beli ini itu click";
    public static final String BAYAR_INI_ITU_CLICK = "bayar ini itu click";
    public static final String PESAN_INI_ITU_CLICK = "pesan ini itu click";
    public static final String AJUKAN_INI_ITU_CLICK = "ajukan ini itu click";
    public static final String JUAL_INI_ITU_CLICK = "jual ini itu click";
    public static final String LEGO_BANNER_3_IMAGE_CLICK = "lego banner 3 image click";
    public static final String LEGO_BANNER_4_IMAGE_CLICK = "lego banner 4 image click";

    private static final String ACTION_CLICK_POINT = "click point & tier status";

    private static final String EVENT_CLICK_HOME_PAGE = "clickHomepage";
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
    private static final String CATEGORY_HOMEPAGE_TOKOPOINTS = "homepage-tokopoints";

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
    private static final String EVENT_ACTION_CLICK_ON_TOKOPOINTS_NEW_COUPON = "click on tokopoints new coupon";

    private static final String LABEL_TOKOPOINTS = "tokopoints";
    private static final String LABEL_EMPTY = "";
    public static final String EVENT = "event";
    public static final String PROMO_VIEW = "promoView";

    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION_CLICK_ON_HOMEPAGE_RECOMMENDATION_TAB = "click on homepage recommendation tab";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String ATTRIBUTION = "attribution";
    public static final String AFFINITY_LABEL = "affinityLabel";
    public static final String GALAXY_CATEGORY_ID = "categoryId";
    public static final String SHOP_ID = "shopId";
    public static final String CAMPAIGN_CODE = "campaignCode";
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
    public static final String EVENT_LEGO_BANNER_IMPRESSION = "home banner impression";
    public static final String EVENT_LEGO_BANNER = "lego banner gif impression";

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
    public static final String OVO_TOPUP = "top up ovo";
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
    public static final String FIELD_DIMENSION_84 = "dimension84";
    public static final String PRODUCT_VIEW_IRIS = "productViewIris";
    public static final String EVENT_ACTION_IMPRESSION_ON_LEGO_PRODUCT = "impression on lego product";
    public static final String NONE_OTHER = "none / other";
    public static final String PROMO_VIEW_IRIS = "promoViewIris";
    public static final String EVENT_ACTION_LEGO_BANNER_IMPRESSION = "lego banner impression";
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
    public static final String EVENT_ACTION_CLICK_ON_BANNER_SPOTLIGHT = "click on banner spotlight";
    public static final String EVENT_ACTION_LEGO_BANNER_CLICK = "lego banner click";
    public static final String EVENT_ACTION_CLICK_ON_BANNER_INSIDE_RECOMMENDATION_TAB = "click on banner inside recommendation tab";
    public static final String VALUE_CREATIVE_BANNER_INSIDE_RECOM_TAB = "/ - banner inside recom tab - %s - ";
    public static final String FIELD_PROMO_ID = "promo_id";
    public static final String EVENT_ACTION_CLICK_ON_BANNER_DYNAMIC_CHANNEL_MIX = "click on banner dynamic channel mix";
    public static final String VALUE_DYNAMIC_CHANNEL_MIX_BANNER_NAME = "/ - p1 - dynamic channel mix - banner - ";

    public static ContextAnalytics getTracker() {
        return TrackApp.getInstance().getGTM();
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

    public static void sendScreen(Activity activity, String screenName, boolean isUserLoggedIn) {
        if (activity != null) {
            ContextAnalytics tracker = getTracker();
            if (tracker != null) {
                HashMap<String, String> customDimensions = new HashMap<>();
                customDimensions.put(SCREEN_DIMENSION_IS_LOGGED_IN_STATUS, String.valueOf(isUserLoggedIn));
                customDimensions.put(ConstantKt.KEY_SESSION_IRIS, new IrisSession(activity).getSessionId());
                customDimensions.put("currentSite", "tokopediamarketplace");
                customDimensions.put("businessUnit", "home & browse");
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

    public static void eventEnhancedClickDynamicIconHomePage(Context context, DynamicHomeIcon.DynamicIcon homeIconItem, int position) {
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

    public static void eventEnhancedClickDynamicChannelHomePage(Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    data
            );
        }
    }

    public static Map<String, Object> getEnhanceClickLegoBannerHomePage(ChannelGrid grid,
                                                                        ChannelModel channel,
                                                                        int position) {
        TrackingAttributionModel trackingAttributionModel = channel.getTrackingAttributionModel();
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_LEGO_BANNER_CLICK,
                EVENT_LABEL, grid.getAttribution(),
                CHANNEL_ID, channel.getId(),
                ATTRIBUTION, trackingAttributionModel.getGalaxyAttribution(),
                AFFINITY_LABEL, trackingAttributionModel.getPersona(),
                GALAXY_CATEGORY_ID, trackingAttributionModel.getCategoryPersona(),
                SHOP_ID, trackingAttributionModel.getBrandId(),
                CAMPAIGN_CODE, trackingAttributionModel.getCampaignCode(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, channel.getId() + "_" + grid.getId()+ "_" + trackingAttributionModel.getPersoType()+ "_" + trackingAttributionModel.getCategoryId(),
                                                FIELD_NAME, trackingAttributionModel.getPromoName(),
                                                FIELD_CREATIVE, grid.getAttribution(),
                                                FIELD_CREATIVE_URL, grid.getImageUrl(),
                                                FIELD_POSITION, String.valueOf(position)
                                        )
                                )
                        )
                )
        );
    }

    public static Map<String, Object> getEnhanceClickThreeLegoBannerHomePage(ChannelModel channel,
                                                                             ChannelGrid grid,
                                                                             int position) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, LEGO_BANNER_3_IMAGE_CLICK,
                EVENT_LABEL, grid.getAttribution(),
                CHANNEL_ID, channel.getId(),
                ATTRIBUTION, channel.getTrackingAttributionModel().getGalaxyAttribution(),
                AFFINITY_LABEL, channel.getTrackingAttributionModel().getPersona(),
                GALAXY_CATEGORY_ID, channel.getTrackingAttributionModel().getCategoryPersona(),
                SHOP_ID, channel.getTrackingAttributionModel().getBrandId(),
                CAMPAIGN_CODE, channel.getTrackingAttributionModel().getCampaignCode(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, String.format(
                                                        FORMAT_4_VALUE_UNDERSCORE,
                                                        channel.getId(),
                                                        grid.getId(),
                                                        channel.getTrackingAttributionModel().getPersoType(),
                                                        channel.getTrackingAttributionModel().getCategoryId()),
                                                FIELD_NAME, channel.getTrackingAttributionModel().getPromoName(),
                                                FIELD_CREATIVE, grid.getAttribution(),
                                                FIELD_CREATIVE_URL, grid.getImageUrl(),
                                                FIELD_POSITION, String.valueOf(position)
                                        )
                                )
                        )
                )
        );
    }

    //old tracking for old dynamic lego banner
    public static Map<String, Object> getEnhanceClickLegoBannerHomePage(DynamicHomeChannel.Grid grid,
                                                                        DynamicHomeChannel.Channels channel,
                                                                        int position) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_LEGO_BANNER_CLICK,
                EVENT_LABEL, channel.getId() + " - " + channel.getHeader().getName(),
                CHANNEL_ID, channel.getId(),
                ATTRIBUTION, channel.getGalaxyAttribution(),
                AFFINITY_LABEL, channel.getPersona(),
                GALAXY_CATEGORY_ID, channel.getCategoryPersona(),
                SHOP_ID, channel.getBrandId(),
                CAMPAIGN_CODE, channel.getCampaignCode(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, channel.getId() + "_" + grid.getId()+ "_" + channel.getPersoType()+ "_" + channel.getCategoryID(),
                                                FIELD_NAME, channel.getPromoName(),
                                                FIELD_CREATIVE, grid.getAttribution(),
                                                FIELD_POSITION, String.valueOf(position)
                                        )
                                )
                        )
                )
        );
    }

    public static Map<String, Object> getEnhanceClickThreeLegoBannerHomePage(DynamicHomeChannel.Grid grid,
                                                                             DynamicHomeChannel.Channels channel,
                                                                             int position) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, LEGO_BANNER_3_IMAGE_CLICK,
                EVENT_LABEL, channel.getId() + " - " + channel.getHeader().getName(),
                CHANNEL_ID, channel.getId(),
                ATTRIBUTION, channel.getGalaxyAttribution(),
                AFFINITY_LABEL, channel.getPersona(),
                GALAXY_CATEGORY_ID, channel.getCategoryPersona(),
                SHOP_ID, channel.getBrandId(),
                CAMPAIGN_CODE, channel.getCampaignCode(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, String.format(
                                                        FORMAT_4_VALUE_UNDERSCORE,
                                                        channel.getId(),
                                                        grid.getId(),
                                                        channel.getPersoType(),
                                                        channel.getCategoryID()),
                                                FIELD_NAME, channel.getPromoName(),
                                                FIELD_CREATIVE, grid.getAttribution(),
                                                FIELD_POSITION, String.valueOf(position)
                                        )
                                )
                        )
                )
        );
    }

    public static Map<String, Object> getEnhanceClickFourLegoBannerHomePage(DynamicHomeChannel.Grid grid,
                                                                             DynamicHomeChannel.Channels channel,
                                                                             int position) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, LEGO_BANNER_4_IMAGE_CLICK,
                EVENT_LABEL, channel.getId() + " - " + channel.getHeader().getName(),
                CHANNEL_ID, channel.getId(),
                ATTRIBUTION, channel.getGalaxyAttribution(),
                AFFINITY_LABEL, channel.getPersona(),
                GALAXY_CATEGORY_ID, channel.getCategoryPersona(),
                SHOP_ID, channel.getBrandId(),
                CAMPAIGN_CODE, channel.getCampaignCode(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, String.format(
                                                        FORMAT_4_VALUE_UNDERSCORE,
                                                        channel.getId(),
                                                        grid.getId(),
                                                        channel.getPersoType(),
                                                        channel.getCategoryID()),
                                                FIELD_NAME, channel.getPromoName(),
                                                FIELD_CREATIVE, grid.getAttribution(),
                                                FIELD_CREATIVE_URL, grid.getImageUrl(),
                                                FIELD_POSITION, String.valueOf(position)
                                        )
                                )
                        )
                )
        );
    }

    public static Map<String, Object> getEventEnhancedClickSpotlightHomePage(int position,
                                                                             SpotlightItemDataModel spotlightItemDataModel) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_BANNER_SPOTLIGHT,
                EVENT_LABEL, spotlightItemDataModel.getTitle(),
                CHANNEL_ID, spotlightItemDataModel.getChanneldId(),
                ATTRIBUTION, spotlightItemDataModel.getGalaxyAttribution(),
                AFFINITY_LABEL, spotlightItemDataModel.getAffinityLabel(),
                GALAXY_CATEGORY_ID, spotlightItemDataModel.getCategoryPersona(),
                SHOP_ID, spotlightItemDataModel.getShopId(),
                CAMPAIGN_CODE,
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, spotlightItemDataModel.getChanneldId()+"_"+ spotlightItemDataModel.getId(),
                                                FIELD_NAME, spotlightItemDataModel.getPromoName(),
                                                FIELD_POSITION, String.valueOf((position + 1)),
                                                FIELD_CREATIVE, spotlightItemDataModel.getTitle(),
                                                FIELD_CREATIVE_URL, spotlightItemDataModel.getBackgroundImageUrl()
                                        )
                                )
                        )
                )
        );
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

    public static void eventClickSeeAllLegoBannerChannel(String headerName,
                                                         String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_LEGO_BANNER_CHANNEL);
        map.put(EVENT_LABEL, channelId+" - "+headerName);
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

    public static void eventClickSeeAllThreeLegoBannerChannel(String headerName,
                                                              String channelId) {
        Map<String, Object> map = new HashMap<>();
        map.put(EVENT, EVENT_CLICK_HOME_PAGE);
        map.put(EVENT_CATEGORY, CATEGORY_HOME_PAGE);
        map.put(EVENT_ACTION, ACTION_CLICK_SEE_ALL_LEGO_THREE_IMAGE_BANNER_CHANNEL);
        map.put(EVENT_LABEL, channelId+" - "+headerName);
        map.put(CHANNEL_ID, channelId);
        getTracker().sendGeneralEvent(map);
    }

    public static void eventClickSeeAllBannerMixChannel(String channelId, String headerName) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendGeneralEvent(
                    DataLayer.mapOf(
                            EVENT, EVENT_CLICK_HOME_PAGE,
                            EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                            EVENT_ACTION, ACTION_CLICK_VIEW_ALL_ON_DYNAMIC_CHANNEL_MIX,
                            EVENT_LABEL, headerName,
                            CHANNEL_ID, channelId
                    )
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
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventClickOnHomePageRecommendationTab(
            Context context,
            RecommendationTabDataModel recommendationTabDataModel) {

        ContextAnalytics tracker = getTracker();

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_HOMEPAGE_RECOMMENDATION_TAB,
                EVENT_LABEL, recommendationTabDataModel.getName(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        recommendationTabDataModel.convertFeedTabModelToDataObject()
                                )
                        )
                )
        );
        tracker.sendEnhanceEcommerceEvent(data);
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
            BannerRecommendationDataModel bannerRecommendationDataModel,
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
                                convertBannerFeedViewModelListToObjectData(bannerRecommendationDataModel, tabName)
                        )
                )
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    private static List<Object> convertBannerFeedViewModelListToObjectData(
            BannerRecommendationDataModel bannerRecommendationDataModel,
            String tabName
    ) {
        List<Object> objects = new ArrayList<>();
        objects.add(
                DataLayer.mapOf(
                        FIELD_ID, bannerRecommendationDataModel.getId(),
                        FIELD_NAME, String.format(
                                VALUE_CREATIVE_BANNER_INSIDE_RECOM_TAB, tabName
                        ),
                        FIELD_CREATIVE, String.format(
                                FORMAT_2_VALUE_UNDERSCORE, bannerRecommendationDataModel.getBuAttribution(), bannerRecommendationDataModel.getCreativeName()
                        ),
                        FIELD_CREATIVE_URL, bannerRecommendationDataModel.getImageUrl(),
                        FIELD_POSITION, String.valueOf(bannerRecommendationDataModel.getPosition()),
                        FIELD_PROMO_ID, LABEL_EMPTY,
                        FIELD_PROMO_CODE, LABEL_EMPTY
                )
        );
        return objects;
    }

    /**
     * Banner always in position 1 because only 1 banner shown
     */
    public static Map<String, Object> getEnhanceClickBannerChannelMix(DynamicHomeChannel.Channels channel) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_BANNER_DYNAMIC_CHANNEL_MIX,
                EVENT_LABEL, channel.getHeader().getName(),
                CHANNEL_ID, channel.getId(),
                ATTRIBUTION, channel.getGalaxyAttribution(),
                AFFINITY_LABEL, channel.getPersona(),
                GALAXY_CATEGORY_ID, channel.getCategoryPersona(),
                SHOP_ID, channel.getBrandId(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, channel.getId() + "_" + channel.getBanner().getId()+ "_" + channel.getPersoType()+ "_" + channel.getCategoryID(),
                                                FIELD_NAME, VALUE_DYNAMIC_CHANNEL_MIX_BANNER_NAME+channel.getHeader().getName(),
                                                FIELD_CREATIVE, channel.getBanner().getAttribution(),
                                                FIELD_CREATIVE_URL, channel.getBanner().getImageUrl(),
                                                FIELD_POSITION, String.valueOf(1)
                                        )
                                )
                        )
                )
        );
    }

    public static void eventClickOnBannerFeed(
            BannerRecommendationDataModel bannerRecommendationDataModel,
            String tabName) {

        ContextAnalytics tracker = getTracker();

        Map<String, Object> data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_BANNER_INSIDE_RECOMMENDATION_TAB,
                EVENT_LABEL, tabName,
                ATTRIBUTION, bannerRecommendationDataModel.getGalaxyAttribution(),
                AFFINITY_LABEL, bannerRecommendationDataModel.getAffinityLabel(),
                GALAXY_CATEGORY_ID, bannerRecommendationDataModel.getCategoryPersona(),
                SHOP_ID, bannerRecommendationDataModel.getShopId(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS,
                                convertBannerFeedViewModelListToObjectData(bannerRecommendationDataModel, tabName)
                        )
                )
        );
        tracker.sendEnhanceEcommerceEvent(data);
    }

    public static void eventEnhanceImpressionPlayBanner(TrackingQueue trackingQueue, PlayCardDataModel playCardDataModel) {
        trackingQueue.putEETracking((HashMap<String, Object>) playCardDataModel.getEnhanceImpressionPlayBanner());
    }

    public static HashMap<String, Object> eventEnhanceImpressionIrisPlayBanner(PlayCardDataModel playCardDataModel) {
        return  (HashMap<String, Object>) playCardDataModel.getEnhanceImpressionIrisPlayBanner();
    }

    public static void eventClickPlayBanner(PlayCardDataModel playCardDataModel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(playCardDataModel.getEnhanceClickPlayBanner());
        }
    }

    public static HashMap<String, Object> getEventEnhanceImpressionBannerGif(DynamicHomeChannel.Channels bannerChannel) {
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_LEGO_BANNER_IMPRESSION,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, bannerChannel.getBanner().getId()+"_"+bannerChannel.getId(),
                                                FIELD_NAME, bannerChannel.getPromoName(),
                                                FIELD_CREATIVE, bannerChannel.getBanner().getAttribution(),
                                                FIELD_POSITION, String.valueOf(1)
                                        )
                                )
                        )

                )
        );
    }

    public static void eventEnhanceClickBannerGif(DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    DataLayer.mapOf(
                            EVENT, PROMO_CLICK,
                            EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                            EVENT_ACTION, EVENT_LEGO_BANNER_CLICK,
                            EVENT_LABEL, bannerChannel.getName(),
                            CHANNEL_ID, bannerChannel.getId(),
                            ATTRIBUTION, bannerChannel.getGalaxyAttribution(),
                            AFFINITY_LABEL, bannerChannel.getPersona(),
                            GALAXY_CATEGORY_ID, bannerChannel.getCategoryPersona(),
                            SHOP_ID, bannerChannel.getBrandId(),
                            CAMPAIGN_CODE, bannerChannel.getCampaignCode(),
                            ECOMMERCE, DataLayer.mapOf(
                                    PROMO_CLICK, DataLayer.mapOf(
                                            PROMOTIONS, DataLayer.listOf(
                                                    DataLayer.mapOf(
                                                            FIELD_ID, bannerChannel.getId() + "_" + bannerChannel.getBanner().getId()+ "_" + bannerChannel.getPersoType()+ "_" + bannerChannel.getCategoryID(),
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

    public static void eventClickProductChannelMix(String type,
                                                   DynamicHomeChannel.Channels bannerChannel,
                                                   boolean isFreeOngkir,
                                                   int gridPosition) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    bannerChannel.getEnhanceClickProductChannelMix(gridPosition, isFreeOngkir, type)
            );
        }
    }

    public static void eventClickBannerChannelMix(DynamicHomeChannel.Channels bannerChannel) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    HomePageTracking.getEnhanceClickBannerChannelMix(bannerChannel)
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
                                FIELD_PRICE, Integer.toString(CurrencyFormatHelper.INSTANCE.convertRupiahToInt(
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
                                FIELD_PRICE, Integer.toString(CurrencyFormatHelper.INSTANCE.convertRupiahToInt(
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

    /**
     * Position always 1 cause just 1 gif dc
     * @param channel
     * @return
     */
    public static HashMap<String, Object> getEnhanceImpressionPromoGifBannerDC(DynamicHomeChannel.Channels channel){
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, PROMO_VIEW_IRIS,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_LEGO_BANNER,
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

    private static List<Object> convertProductEnhanceProductMixDataLayer(DynamicHomeChannel.Grid[] grids, String headerName, String type, String channelId) {
        List<Object> list = new ArrayList<>();

        if (grids != null) {
            for (int i = 0; i < grids.length; i++) {
                DynamicHomeChannel.Grid grid = grids[i];
                list.add(
                        DataLayer.mapOf(
                                FIELD_NAME, grid.getName(),
                                FIELD_ID, grid.getId(),
                                FIELD_PRICE, Integer.toString(CurrencyFormatHelper.INSTANCE.convertRupiahToInt(
                                        grid.getPrice()
                                )),
                                FIELD_BRAND, NONE_OTHER,
                                FIELD_CATEGORY, NONE_OTHER,
                                FIELD_VARIANT, NONE_OTHER,
                                LIST, "/ - p1 - dynamic channel mix - product - "+headerName+" - "+type,
                                FIELD_POSITION, String.valueOf(i + 1),
                                FIELD_DIMENSION_84, channelId
                        )
                );
            }
        }
        return list;
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

    public static HashMap<String, Object> getEnhanceImpressionLegoBannerHomePage(
            String channelId,
            List<ChannelGrid> grids,
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

    public static HashMap<String, Object> getIrisEnhanceImpressionLegoThreeBannerHomePage(
            String channelId,
            List<ChannelGrid> grids,
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

    private static List<Object> convertPromoEnhanceLegoBannerDataLayer(List<ChannelGrid> grids, String promoName) {
        List<Object> list = new ArrayList<>();

        if (grids != null) {
            for (int i = 0; i < grids.size(); i++) {
                ChannelGrid grid = grids.get(i);
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
            List<SpotlightItemDataModel> spotlights,
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

    private static List<Object> convertPromoEnhanceSpotlight(List<SpotlightItemDataModel> spotlights,
                                                             int position) {
        List<Object> list = new ArrayList<>();
        String promoName = String.format(
                VALUE_PROMO_NAME_SPOTLIGHT_BANNER,
                String.valueOf(position)
        );

        if (spotlights != null) {
            for (int i = 0; i < spotlights.size(); i++) {
                SpotlightItemDataModel item = spotlights.get(i);
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

    public static Map<String, Object> getEnhanceClickDynamicIconHomePage(int position, DynamicHomeIcon.DynamicIcon homeIconItem) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_DYNAMIC_ICON,
                EVENT_LABEL, LABEL_EMPTY,
                ATTRIBUTION, homeIconItem.getGalaxyAttribution(),
                AFFINITY_LABEL, homeIconItem.getPersona(),
                GALAXY_CATEGORY_ID, homeIconItem.getCategoryPersona(),
                SHOP_ID, homeIconItem.getBrandId(),
                ECOMMERCE, DataLayer.mapOf(
                        PROMO_CLICK, DataLayer.mapOf(
                                PROMOTIONS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                FIELD_ID, homeIconItem.getId(),
                                                FIELD_NAME, VALUE_NAME_DYNAMIC_ICON,
                                                FIELD_CREATIVE, homeIconItem.getBu_identifier(),
                                                FIELD_CREATIVE_URL, homeIconItem.getImageUrl(),
                                                FIELD_POSITION, String.valueOf(position+1)
                                        )
                                )
                        )
                )
        );
    }

    private static Map<String, Object> convertOverlaySliderBannerImpressionDataLayer(BannerSlidesModel bannerSlidesModel) {
        return DataLayer.mapOf(
                FIELD_ID, String.valueOf(bannerSlidesModel.getId()),
                FIELD_NAME, VALUE_NAME_PROMO_OVERLAY,
                FIELD_CREATIVE, bannerSlidesModel.getCreativeName(),
                FIELD_CREATIVE_URL, bannerSlidesModel.getImageUrl(),
                FIELD_POSITION, String.valueOf(bannerSlidesModel.getPosition())
        );
    }

    private static Map<String, Object> convertSliderBannerImpressionDataLayer(BannerSlidesModel bannerSlidesModel) {
        return DataLayer.mapOf(
                FIELD_ID, String.valueOf(bannerSlidesModel.getId()),
                FIELD_NAME, VALUE_NAME_PROMO,
                FIELD_CREATIVE, bannerSlidesModel.getCreativeName(),
                FIELD_CREATIVE_URL, bannerSlidesModel.getImageUrl(),
                FIELD_POSITION, String.valueOf(bannerSlidesModel.getPosition())
        );
    }

    public static Map<String, Object> getEnhanceImpressionDynamicIconHomePage(List<DynamicHomeIcon.DynamicIcon> homeIconItem) {
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

    private static List<Object> convertEnhanceDynamicIcon(List<DynamicHomeIcon.DynamicIcon> homeIconItem) {
        List<Object> list = new ArrayList<>();

        if (homeIconItem != null) {
            for (int i = 0; i < homeIconItem.size(); i++) {
                DynamicHomeIcon.DynamicIcon item = homeIconItem.get(i);
                list.add(
                        DataLayer.mapOf(
                                FIELD_ID, item.getId(),
                                FIELD_NAME, VALUE_NAME_DYNAMIC_ICON,
                                FIELD_CREATIVE, item.getBu_identifier(),
                                FIELD_CREATIVE_URL, item.getImageUrl(),
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
            String productId,
            String channelId
    ) {
        trackingQueue.putEETracking(getHomeReviewImpression(reviewData, position, orderId, productId, channelId, false));
    }

    public static HashMap<String, Object>  getHomeReviewImpressionIris(
            SuggestedProductReviewResponse reviewData,
            int position,
            String orderId,
            String productId,
            String channelId) {
        return getHomeReviewImpression(reviewData, position, orderId, productId, channelId,true);
    }

    private static HashMap<String, Object> getHomeReviewImpression(SuggestedProductReviewResponse reviewData,
                                                                   int position,
                                                                   String orderId,
                                                                   String productId,
                                                                  String channelId,
                                                                  boolean isToIris) {
        List<Object> promotionBody = DataLayer.listOf(DataLayer.mapOf(
                "id", orderId + " - " + productId + " - " + channelId,
                "name", "product review notification - " + orderId + " - " + productId,
                "creative", "product review notification - " + orderId + " - " + productId,
                "creative_url", reviewData.getImageUrl(),
                "position", Integer.toString(position + 1, 10),
                "category", "",
                "promo_id", null,
                "promo_code", null
        ));
        return (HashMap<String, Object>) DataLayer.mapOf(
                EVENT, isToIris? "promoViewIris" : "promoView",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "view - product review notification",
                EVENT_LABEL, orderId + " - " + productId,
                ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", promotionBody
                        )
                )
        );

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

    public static void homeReviewOnBlankSpaceClickTracker(String orderId, String productId, String channelId) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - home product review widget",
                EVENT_LABEL, orderId + " - " + productId,
                CHANNEL_ID, channelId
        ));
    }

    public static void sendClickOnTokopointsNewCouponTracker() {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, EVENT_TOKO_POINT,
                EVENT_CATEGORY, CATEGORY_HOMEPAGE_TOKOPOINTS,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_TOKOPOINTS_NEW_COUPON,
                EVENT_LABEL, LABEL_EMPTY
        ));
    }
}
