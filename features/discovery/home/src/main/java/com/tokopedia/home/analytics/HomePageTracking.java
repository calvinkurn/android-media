package com.tokopedia.home.analytics;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel;
import com.tokopedia.home.beranda.domain.model.DynamicHomeIcon;
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.BannerRecommendationDataModel;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel;
import com.tokopedia.iris.util.ConstantKt;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.recommendation_widget_common.infinite.foryou.banner.BannerRecommendationModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.builder.BaseTrackerBuilder;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import com.tokopedia.utils.text.currency.CurrencyFormatHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akmal on 2/6/18.
 */

public class HomePageTracking {


    public static final String FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    public static final String FORMAT_2_VALUE_UNDERSCORE = "%s_%s";

    private static final String EVENT_CLICK_HOME_PAGE = "clickHomepage";

    public static final String CATEGORY_HOME_PAGE = "homepage";

    private static final String ACTION_CLICK_HOME_PAGE = "clickHomePage";
    private static final String ACTION_CLICK_VIEW_ALL_PROMO = "slider banner click view all";
    private static final String ACTION_CLICK_JUMP_RECOMENDATION = "cek rekomendasi jumper click";private static final String ACTION_CLICK_HOME_USE_CASE = "click 5 use cases";
    private static final String ACTION_CLICK_SEE_ALL_DYNAMIC_CHANNEL = "curated list click view all";

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
    public static final String CURRENCY_CODE = "currencyCode";
    public static final String IDR = "IDR";
    public static final String IMPRESSIONS = "impressions";
    public static final String EVENT_LEGO_BANNER_IMPRESSION = "home banner impression";

    public static final String LIST = "list";
    public static final String EVENT_CLICK_TICKER = "clickTicker";
    public static final String EVENT_CATEGORY_TICKER_HOMEPAGE = "ticker homepage";
    public static final String EVENT_ACTION_CLICK_TICKER = "click ticker";
    public static final String EVENT_ACTION_CLICK_ON_CLOSE_TICKER = "click on close ticker";

    public static final String EVENT_ACTION_CLICK_ON_ALLOW_GEOLOCATION = "click on allow geolocation";
    public static final String EVENT_ACTION_CLICK_ON_ATUR = "click on atur";

    public static final String CHANNEL_ID = "channelId";

    public static final String EVENT_PROMO_VIEW_IRIS = "promoViewIris";
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
    public static final String NONE_OTHER = "none / other";
    public static final String PROMO_VIEW_IRIS = "promoViewIris";
    public static final String FIELD_PROMO_CODE = "promo_code";
    public static final String EVENT_ACTION_CLICK_ON_DYNAMIC_ICON = "click on dynamic icon";
    public static final String VALUE_NAME_DYNAMIC_ICON = "/ - dynamic icon";
    public static final String EVENT_ACTION_IMPRESSION_ON_DYNAMIC_ICON = "impression on dynamic icon";
    public static final String SCREEN_DIMENSION_IS_LOGGED_IN_STATUS = "isLoggedInStatus";
    public static final String EVENT_ACTION_CLICK_ON_BANNER_INSIDE_RECOMMENDATION_TAB = "click on banner inside recommendation tab";
    public static final String VALUE_CREATIVE_BANNER_INSIDE_RECOM_TAB = "/ - banner inside recom tab - %s - ";
    public static final String FIELD_PROMO_ID = "promo_id";

    public static final String BUSINESS_UNIT_VALUE = "home & browse";
    public static final String CURRENT_SITE_VALUE = "tokopediamarketplace";

    public static ContextAnalytics getTracker() {
        return TrackApp.getInstance().getGTM();
    }

    public static void eventClickViewAllPromo() {
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

    public static void eventClickJumpRecomendation() {
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

    public static void eventClickHomeUseCase(@NonNull String title) {
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

    public static void eventEnhancedClickDynamicIconHomePage(DynamicHomeIcon.DynamicIcon homeIconItem, int position) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(
                    getEnhanceClickDynamicIconHomePage(position, homeIconItem)
            );
        }
    }

    public static void eventEnhancedClickSprintSaleProduct(Map<String, Object> data) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            tracker.sendEnhanceEcommerceEvent(data);
        }
    }

    public static void eventEnhancedImpressionWidgetHomePage(TrackingQueue trackingQueue,
                                                                     Map<String, Object> data) {
        trackingQueue.putEETracking((HashMap<String, Object>) data);
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

    public static void eventEnhanceImpressionLegoAndCuratedHomePage(
            TrackingQueue trackingQueue,
            List<Object> legoAndCuratedList,
            String userId
    ) {

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
                "userId", userId
        );
        trackingQueue.putEETracking((HashMap<String, Object>) data);
    }

    public static void eventClickOnHomePageRecommendationTab(
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

    public static void eventClickTickerHomePage(String tickerId) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            BaseTrackerBuilder trackerBuilder = new BaseTrackerBuilder();
            trackerBuilder.constructBasicGeneralClick(
                    EVENT_CLICK_TICKER,
                    EVENT_CATEGORY_TICKER_HOMEPAGE,
                    EVENT_ACTION_CLICK_TICKER,
                    tickerId
            ).appendBusinessUnit(BUSINESS_UNIT_VALUE)
            .appendCurrentSite(CURRENT_SITE_VALUE);
            tracker.sendGeneralEvent(trackerBuilder.build());
        }
    }

    public static void eventClickOnCloseTickerHomePage(String tickerId) {
        ContextAnalytics tracker = getTracker();
        if (tracker != null) {
            BaseTrackerBuilder trackerBuilder = new BaseTrackerBuilder();
            trackerBuilder.constructBasicGeneralClick(
                    EVENT_CLICK_TICKER,
                    EVENT_CATEGORY_TICKER_HOMEPAGE,
                    EVENT_ACTION_CLICK_ON_CLOSE_TICKER,
                    tickerId
            ).appendBusinessUnit(BUSINESS_UNIT_VALUE)
            .appendCurrentSite(CURRENT_SITE_VALUE);
            tracker.sendGeneralEvent(trackerBuilder.build());
        }
    }

    public static void eventClickOnAtur() {
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


    private static List<Object> convertBannerFeedViewModelListToObjectData(
            BannerRecommendationModel bannerRecommendationDataModel,
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

    public static void eventClickOnBannerFeed(
            BannerRecommendationModel bannerRecommendationDataModel,
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

    public static Map<String, Object> getEnhanceClickDynamicIconHomePage(int position, DynamicHomeIcon.DynamicIcon homeIconItem) {
        return DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, CATEGORY_HOME_PAGE,
                EVENT_ACTION, EVENT_ACTION_CLICK_ON_DYNAMIC_ICON,
                EVENT_LABEL, homeIconItem.getName(),
                ATTRIBUTION, homeIconItem.getGalaxyAttribution(),
                AFFINITY_LABEL, homeIconItem.getPersona(),
                GALAXY_CATEGORY_ID, homeIconItem.getCategoryPersona(),
                SHOP_ID, homeIconItem.getBrandId(),
                CAMPAIGN_CODE, homeIconItem.getCampaignCode(),
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

    public static void homeReviewImpression(
            TrackingQueue trackingQueue,
            SuggestedProductReviewResponse reviewData,
            int position,
            String orderId,
            String productId,
            String channelId,
            String message
    ) {
        trackingQueue.putEETracking(getHomeReviewImpression(reviewData, position, orderId, productId, channelId, false, message));
    }

    public static HashMap<String, Object>  getHomeReviewImpressionIris(
            SuggestedProductReviewResponse reviewData,
            int position,
            String orderId,
            String productId,
            String channelId,
            String message) {
        return getHomeReviewImpression(reviewData, position, orderId, productId, channelId,true, message);
    }

    private static HashMap<String, Object> getHomeReviewImpression(SuggestedProductReviewResponse reviewData,
                                                                   int position,
                                                                   String orderId,
                                                                   String productId,
                                                                  String channelId,
                                                                  boolean isToIris,
                                                                   String message) {
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
                EVENT_LABEL, orderId + " - " + productId + " - message:" + message + ";",
                ECOMMERCE, DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", promotionBody
                        )
                )
        );

    }

    public static void homeReviewOnCloseTracker(String orderId, String productId, String message, String channelId) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - back button on home product review widget",
                EVENT_LABEL, orderId + " - " + productId + " - " + message,
                CHANNEL_ID, channelId
        ));
    }

    public static void homeReviewOnRatingChangedTracker(String orderId, String productId, int starCount, String message, String channelId) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - product rating stars on home product review widget",
                EVENT_LABEL, orderId + " - " + productId + " - " + Integer.toString(starCount, 10) + " - " + message,
                CHANNEL_ID, channelId
        ));
    }

    public static void homeReviewOnBlankSpaceClickTracker(String orderId, String productId, String channelId, String message) {
        getTracker().sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickReview",
                EVENT_CATEGORY, "homepage-pdp",
                EVENT_ACTION, "click - home product review widget",
                EVENT_LABEL, orderId + " - " + productId + " - " + message,
                CHANNEL_ID, channelId
        ));
    }
}
