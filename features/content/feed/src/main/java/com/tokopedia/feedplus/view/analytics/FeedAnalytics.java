package com.tokopedia.feedplus.view.analytics;

import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking.Event.PROMO_CLICK;
import static com.tokopedia.feedplus.view.analytics.FeedEnhancedTracking.Event.PROMO_VIEW;
import static com.tokopedia.kol.analytics.KolEventTracking.Category.CONTENT_FEED;
import static com.tokopedia.kol.analytics.KolEventTracking.Category.CONTENT_FEED_TIMELINE;

/**
 * @author by nisie on 10/3/18.
 */
public class FeedAnalytics {
    private static final String EVENT_CLICK_FEED = "clickFeed";
    private static final String EVENT_NAME = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String EVENT_ECOMMERCE = "ecommerce";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_ID_MOD = "userIdmodulo";

    private static final String CATEGORY_FEED = "Feed";

    private static final String ACTION_IMPRESSION = "Impression";
    private static final String ACTION_VIEW = "View";
    private static final String ACTION_CLICK = "Click";


    private static final String STATIC_VALUE_PRODUCT_VIEW = "productView";
    private static final String STATIC_VALUE_PRODUCT_CLICK = "productClick";
    private static final String STATIC_VALUE_HOMEPAGE = "homepage";
    private static final String STATIC_VALUE_FEED_ITEM_IMPRESSION = "feed - item impression";
    private static final String STATIC_VALUE_FEED_CLICK_CARD_ITEM = "feed - click card item";

    private static final String DASH = " - ";
    private static final String SINGLE = "single";
    private static final String MULTIPLE = "multiple";
    private static final String FORMAT_PROMOTION_NAME = "%s - %s - %s - %s";

    private UserSessionInterface userSession;

    @Inject
    public FeedAnalytics(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    public interface Element {
        String AVATAR = "avatar";
        String IMAGE = "image";
        String TAG = "tag";
        String SHARE = "share";
        String FOLLOW = "follow";
        String UNFOLLOW = "unfollow";
        String OPTION = "option ";
        String VIDEO = "video";
        String PRODUCT = "product";
        String LIKE = "like";
        String UNLIKE = "unlike";
        String COMMENT = "comment";
    }

    public void trackScreen(String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(mapEvent);
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(mapEvent);
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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(mapEvent);

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

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(mapEvent);
    }

    public void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(trackingData);
    }

    private Map<String, Object> getEventEcommerceView(String action, String label,
                                                      List<FeedEnhancedTracking.Promotion> promotions,
                                                      int userId) {
        return DataLayer.mapOf(
                EVENT_NAME, PROMO_VIEW,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, FeedEnhancedTracking.Ecommerce.getEcommerceView(promotions)
        );
    }

    private Map<String, Object> getEventEcommerceClick(String action,
                                                       String label,
                                                       List<FeedEnhancedTracking.Promotion> promotions,
                                                       int userId) {
        return DataLayer.mapOf(
                EVENT_NAME, PROMO_CLICK,
                EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                EVENT_ACTION, action,
                EVENT_LABEL, label,
                KEY_USER_ID, userId,
                KEY_USER_ID_MOD, userId % 50,
                EVENT_ECOMMERCE, FeedEnhancedTracking.Ecommerce.getEcommerceClick(promotions)
        );
    }

    private String singleOrMultiple(int totalContent) {
        if (totalContent == 1) return SINGLE;
        else return MULTIPLE;
    }

    public void eventBannerImpression(String templateType, String activityName, String mediaType,
                                      String bannerUrl, String applink, int postId,
                                      int bannerPosition, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                postId,
                String.format("%s - %s - %s", CONTENT_FEED, activityName, mediaType),
                bannerUrl,
                applink,
                bannerPosition,
                "",
                postId,
                templateType
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        "impression banner",
                        String.valueOf(postId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventBannerClick(String templateType, String activityName, String mediaType,
                                 String bannerUrl, String applink, int totalBanner, int postId,
                                 int bannerPosition, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                postId,
                String.format("%s - %s - %s", CONTENT_FEED, activityName, mediaType),
                bannerUrl,
                applink,
                bannerPosition,
                String.valueOf(totalBanner),
                postId,
                templateType
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        "click banner",
                        String.valueOf(postId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventCardPostImpression(String templateType, String activityName,
                                        String trackingType, String mediaType, String tagsType,
                                        String redirectUrl,
                                        int totalContent, int postId, int position, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                        postId,
                        String.format(FORMAT_PROMOTION_NAME,
                                CONTENT_FEED,
                                activityName,
                                tagsType,
                                singleOrMultiple(totalContent)),
                        String.format("%s - 0", redirectUrl),
                        position,
                        "",
                        0,
                        ""
                )
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        ACTION_IMPRESSION.toLowerCase() + DASH + templateType + DASH
                                + activityName + DASH + trackingType,
                        mediaType,
                        promotionList,
                        userId
                )
        );
    }

    public void eventCardPostClick(String templateType, String activityName,
                                   String trackingType, String mediaType, String tagsType,
                                   String redirectUrl, String element, int totalContent,
                                   int postId, int position, String contentPosition, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                        postId,
                        String.format(FORMAT_PROMOTION_NAME,
                                CONTENT_FEED,
                                activityName,
                                tagsType,
                                singleOrMultiple(totalContent)),
                        redirectUrl + DASH + contentPosition,
                        position,
                        "",
                        0,
                        ""
                )
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        ACTION_CLICK.toLowerCase() + DASH + templateType + DASH
                                + activityName + DASH + trackingType + DASH + element,
                        mediaType,
                        promotionList,
                        userId
                )
        );
    }

    public void eventCardPostElementClick(String element, String activityName, String mediaType,
                                          String activityId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_FEED,
                CONTENT_FEED_TIMELINE,
                String.format("click %s - %s - %s", element, activityName, mediaType),
                activityId
        );
    }

    public void eventRecommendationImpression(String templateType, String activityName,
                                              String trackingType, String mediaType,
                                              String authorName, String authorType, int authorId,
                                              int feedPosition, int cardPosition, int userId) {

        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        String promoName = CONTENT_FEED + DASH + activityName;
        if (!TextUtils.isEmpty(authorType)) {
            promoName += DASH + authorType;
        }
        promotionList.add(new FeedEnhancedTracking.Promotion(
                        authorId,
                        promoName,
                        authorName + DASH + cardPosition,
                        feedPosition,
                        "",
                        0,
                        ""
                )
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        ACTION_IMPRESSION.toLowerCase() + DASH + templateType + DASH
                                + activityName + DASH + trackingType,
                        mediaType,
                        promotionList,
                        userId
                )
        );
    }

    public void eventRecommendationClick(String templateType, String activityName,
                                         String trackingType, String mediaType,
                                         String authorName, String authorType, String element,
                                         int authorId, int feedPosition, int cardPosition,
                                         int userId) {

        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        String promoName = CONTENT_FEED + DASH + activityName;
        if (!TextUtils.isEmpty(authorType)) {
            promoName += DASH + authorType;
        }
        promotionList.add(new FeedEnhancedTracking.Promotion(
                        authorId,
                        promoName,
                        authorName + DASH + cardPosition,
                        feedPosition,
                        "",
                        0,
                        ""
                )
        );
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        ACTION_CLICK.toLowerCase() + DASH + templateType + DASH
                                + activityName + DASH + trackingType + DASH + element,
                        mediaType,
                        promotionList,
                        userId
                )
        );
    }

    //#FEED015
    public void trackClickCreatePost(String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_FEED,
                CATEGORY_FEED,
                "click buat post",
                userId
        );
    }

    //#FEED016
    public void trackClickCreatePostAs(String applink, String userId, String shopId) {
        if (applink.equals(ApplinkConst.AFFILIATE_EXPLORE)) {
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT_CLICK_FEED,
                    CATEGORY_FEED,
                    "click post sebagai - user",
                    userId
            );
        } else {
            TrackApp.getInstance().getGTM().sendGeneralEvent(
                    EVENT_CLICK_FEED,
                    CATEGORY_FEED,
                    "click post sebagai - shop",
                    shopId
            );
        }
    }
}
