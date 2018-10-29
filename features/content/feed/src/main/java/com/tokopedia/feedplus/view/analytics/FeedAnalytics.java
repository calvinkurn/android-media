package com.tokopedia.feedplus.view.analytics;

import android.app.Activity;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.feedplus.view.analytics.FeedTrackingEventLabel.SCREEN_UNIFY_HOME_FEED;

/**
 * @author by nisie on 10/3/18.
 */
public class FeedAnalytics {

    private static final String EVENT_R3 = "r3";
    private static final String EVENT_CLICK_FEED = "clickFeed";
    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String EVENT_ECOMMERCE = "ecommerce";
    private static final String EVENT_USER_INTERACTION_HOMEPAGE = "userInteractionHomePage";

    private static final String CATEGORY_R3_USER = "r3User";
    private static final String CATEGORY_FEED = "Feed";
    private static final String CATEGORY_HOMEPAGE = "Homepage";

    private static final String ACTION_IMPRESSION = "Impression";
    private static final String ACTION_VIEW = "View";
    private static final Object ACTION_CLICK = "Click";
    private static final String FEED_VIEW_ALL_KOL_RECOMMENDATION = "feed - view all kol recommendation";
    private static final String FEED_UNFOLLOW_KOL_RECOMMENDATION = "feed - unfollow kol recommendation";
    private static final String FEED_FOLLOW_KOL_RECOMMENDATION = "feed - follow kol recommendation";
    private static final String FEED_CLICK_KOL_RECOMMENDATION_PROFILE = "feed - click kol recommendation profile";


    private static final String LABEL_FEED_RECOMMENDATION = "Feed - Inspirasi";
    private static final String FEED_KOL_RECOMMENDATION_VIEW_ALL = "kol discovery page";

    private static final String STATIC_VALUE_PRODUCT_VIEW = "productView";
    private static final String STATIC_VALUE_PRODUCT_CLICK = "productClick";
    private static final String STATIC_VALUE_HOMEPAGE = "homepage";
    private static final String STATIC_VALUE_FEED_ITEM_IMPRESSION = "feed - item impression";
    private static final String STATIC_VALUE_FEED_CLICK_CARD_ITEM = "feed - click card item";
    private static final String STATIC_FORMAT_ACTION_FIELD_FEED_PRODUCT = "/feed - product %d - %s";
    private static final String ACTION_CLICK_VIEW_ALL = "click view all";

    private AnalyticTracker analyticTracker;
    private UserSessionInterface userSession;

    @Inject
    public FeedAnalytics(AnalyticTracker analyticTracker, UserSessionInterface userSession) {
        this.analyticTracker = analyticTracker;
        this.userSession = userSession;
    }


    public void trackScreen(Activity activity, String screenName) {
        analyticTracker.sendScreen(activity, screenName);
    }

    public void trackImpressionFeedRecommendation() {
        analyticTracker.sendEventTracking(
                EVENT_R3,
                CATEGORY_R3_USER,
                ACTION_IMPRESSION,
                LABEL_FEED_RECOMMENDATION
        );
    }

    public void eventFeedViewProduct(String screenName, String productId, String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", screenName);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_VIEW);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", productId);
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);
    }


    public void eventFeedViewProduct(String screenName, String productId, String action, String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", screenName);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", action);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", productId);
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);
    }

    public void eventR3Product(String productId, String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_CLICK);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", productId);
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);
    }

    public void eventOfficialStoreBrandSeeAll(String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", SCREEN_UNIFY_HOME_FEED);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_HOMEPAGE.toLowerCase());
        mapEvent.put("eventAction", "homepage - " + label + " " + ACTION_CLICK_VIEW_ALL);
        mapEvent.put("eventLabel", "");
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", "0");
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);
    }

    public void eventFeedClick(String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", SCREEN_UNIFY_HOME_FEED);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_CLICK);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", "0");
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);
    }

    public void eventFeedView(String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", SCREEN_UNIFY_HOME_FEED);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_VIEW);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", "0");
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);

    }

    public void eventFeedViewShop(String screenName, String shopId, String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", screenName);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_VIEW);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", "0");
        mapEvent.put("shopId", shopId);
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);


    }

    public void eventFeedClickProduct(String screenName, String productId, String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", screenName);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_CLICK);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", productId);
        mapEvent.put("shopId", "0");
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);

    }

    public void eventFeedClickShop(String screenName, String shopId, String label) {
        HashMap<String, Object> mapEvent = new HashMap<>();
        mapEvent.put("screenName", screenName);
        mapEvent.put("event", EVENT_CLICK_FEED);
        mapEvent.put("eventCategory", CATEGORY_FEED);
        mapEvent.put("eventAction", ACTION_CLICK);
        mapEvent.put("eventLabel", label);
        mapEvent.put("userId", userSession.getUserId());
        mapEvent.put("productId", "0");
        mapEvent.put("shopId", shopId);
        mapEvent.put("promoId", "0");

        analyticTracker.sendEventTracking(mapEvent);
    }

    public void trackEventClickProductUploadEnhanced(String name,
                                                     String id,
                                                     String price,
                                                     String productUrl,
                                                     int rowNumber,
                                                     int itemPosition,
                                                     String userId,
                                                     String eventLabel) {
        String actionField = String.format(STATIC_FORMAT_ACTION_FIELD_FEED_PRODUCT, rowNumber, eventLabel);
        eventClickFeedProductItem(createProductList(name, id, price, itemPosition, actionField, userId),
                eventLabel,
                actionField,
                productUrl
        );
    }

    public void trackEventClickInspirationEnhanced(String name,
                                                   String id,
                                                   String price,
                                                   String productUrl,
                                                   int rowNumber,
                                                   int itemPosition,
                                                   String source,
                                                   String userId,
                                                   String eventLabel) {
        String actionField = String.format(STATIC_FORMAT_ACTION_FIELD_FEED_PRODUCT, rowNumber, eventLabel);
        eventClickFeedProductItem(createProductList(name, id, price, itemPosition, actionField, userId),
                eventLabel,
                actionField,
                productUrl
        );
    }

    private String generateClickRecomItemEventLabel(String cardType, String cardDetail) {
        return cardType + " - " + cardDetail;
    }

    private Map<String, Object> createProductList(String name, String id, String price,
                                                  int itemPosition,
                                                  String actionField, String userId) {

        return DataLayer.mapOf(
                "name", name,
                "id", id,
                "price", price,
                "brand", "",
                "category", "",
                "variant", "",
                "list", actionField,
                "position", itemPosition,
                "userId", userId
        );
    }

    public void eventImpressionFeedInspiration(List<Object> list, String eventLabel) {
        eventImpressionFeedProductItem(list, eventLabel);
    }

    public void eventImpressionFeedUploadedProduct(List<Object> list, String eventLabel) {
        eventImpressionFeedProductItem(list, eventLabel);
    }

    private void eventTrackingEnhanceFeed(Map<String, Object> trackingData) {
        analyticTracker.sendEventTracking(trackingData);
    }

    private void eventImpressionFeedProductItem(List<Object> list, String eventLabel) {
        eventTrackingEnhanceFeed(
                DataLayer.mapOf(EVENT_NAME, STATIC_VALUE_PRODUCT_VIEW,
                        EVENT_CATEGORY, STATIC_VALUE_HOMEPAGE,
                        EVENT_ACTION, STATIC_VALUE_FEED_ITEM_IMPRESSION,
                        EVENT_LABEL, eventLabel,
                        EVENT_ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    private void eventClickFeedProductItem(Map<String, Object> objects,
                                           String eventLabel,
                                           String actionField,
                                           String productUrl) {
        eventTrackingEnhanceFeed(
                DataLayer.mapOf(EVENT_NAME, STATIC_VALUE_PRODUCT_CLICK,
                        EVENT_CATEGORY, STATIC_VALUE_HOMEPAGE,
                        EVENT_ACTION, STATIC_VALUE_FEED_CLICK_CARD_ITEM,
                        EVENT_LABEL, eventLabel,
                        EVENT_ECOMMERCE, DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", actionField),
                                        "products", DataLayer.listOf(objects)
                                )
                        )
                )
        );
    }

    public void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData) {
        analyticTracker.sendEventTracking(trackingData);
    }

    public void eventKolRecommendationViewAllClick() {
        analyticTracker.sendEventTracking(
                EVENT_USER_INTERACTION_HOMEPAGE,
                CATEGORY_HOMEPAGE,
                FEED_VIEW_ALL_KOL_RECOMMENDATION,
                FEED_KOL_RECOMMENDATION_VIEW_ALL
        );
    }

    private static String generateKolRecommendationEventLabel(String kolCategory, String kolName) {
        return kolCategory + " - " + kolName;
    }

    public void eventKolRecommendationUnfollowClick(String kolCategory, String kolName) {
        analyticTracker.sendEventTracking(
                EVENT_USER_INTERACTION_HOMEPAGE,
                CATEGORY_HOMEPAGE,
                FEED_UNFOLLOW_KOL_RECOMMENDATION,
                generateKolRecommendationEventLabel(kolCategory, kolName)
        );
    }

    public void eventKolRecommendationFollowClick(String kolCategory, String kolName) {
        analyticTracker.sendEventTracking(
                EVENT_USER_INTERACTION_HOMEPAGE,
                CATEGORY_HOMEPAGE,
                FEED_FOLLOW_KOL_RECOMMENDATION,
                generateKolRecommendationEventLabel(kolCategory, kolName)
        );
    }

    public void eventKolRecommendationGoToProfileClick(String kolCategory, String kolName) {
        analyticTracker.sendEventTracking(
                EVENT_USER_INTERACTION_HOMEPAGE,
                CATEGORY_HOMEPAGE,
                FEED_CLICK_KOL_RECOMMENDATION_PROFILE,
                generateKolRecommendationEventLabel(kolCategory, kolName)
        );
    }
}
