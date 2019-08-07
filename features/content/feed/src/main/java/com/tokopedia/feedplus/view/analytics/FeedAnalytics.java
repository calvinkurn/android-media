package com.tokopedia.feedplus.view.analytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.feedcomponent.view.viewmodel.banner.TrackingBannerModel;
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.TrackingRecommendationModel;
import com.tokopedia.kolcommon.data.pojo.Author;
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
import static com.tokopedia.kol.analytics.KolEventTracking.Category.CONTENT_FEED_TIMELINE_DETAIL;

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

    private static final String PRODUCT_VIEW = "productView";
    private static final String PRODUCT_CLICK = "productClick";

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

    public void eventBannerImpression(List<TrackingBannerModel> trackingBannerModels, int userId) {
        int firstPostId = trackingBannerModels.isEmpty() ? 0 : trackingBannerModels.get(0).getPostId();
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        for (TrackingBannerModel banner : trackingBannerModels) {
            promotionList.add(new FeedEnhancedTracking.Promotion(
                    banner.getPostId(),
                    String.format("%s - %s - %s", CONTENT_FEED, banner.getActivityName(), banner.getMediaType()),
                    banner.getBannerUrl(),
                    banner.getApplink(),
                    banner.getBannerPosition(),
                    "",
                    banner.getPostId(),
                    banner.getTemplateType()
            ));
        }
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        "impression banner",
                        String.valueOf(firstPostId),
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

    public void eventCardPostElementClick(String element, String activityName, String mediaType,
                                          String activityId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_FEED,
                CONTENT_FEED_TIMELINE,
                String.format("click %s - %s - %s", element, activityName, mediaType),
                activityId
        );
    }

    public void eventRecommendationImpression(List<TrackingRecommendationModel> trackingList,
                                              int userId) {
        int firstAuthorId = trackingList.isEmpty() ? 0 : trackingList.get(0).getAuthorId();
        String firstAuthorType = trackingList.isEmpty() ? "" : trackingList.get(0).getAuthorType();
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        for (TrackingRecommendationModel tracking : trackingList) {
            promotionList.add(new FeedEnhancedTracking.Promotion(
                    tracking.getAuthorId(),
                    String.format("/content feed - %s - %s", tracking.getActivityName(), tracking.getAuthorType()),
                    tracking.getAuthorName(),
                    "",
                    tracking.getCardPosition(),
                    "",
                    0,
                    tracking.getTemplateType()
            ));
        }
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        String.format("impression - %s recommendation", firstAuthorType),
                        String.valueOf(firstAuthorId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventRecommendationClick(String templateType, String activityName,
                                         String authorName, String authorType, int authorId,
                                         int cardPosition, int userId) {

        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                authorId,
                String.format("/content feed - %s - %s", activityName, authorType),
                authorName,
                "",
                cardPosition,
                "",
                0,
                templateType
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        String.format("click avatar - %s recommendation", authorType),
                        String.valueOf(authorId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventTopadsRecommendationImpression(List<TrackingRecommendationModel> trackingList,
                                                    int userId) {
        int firstAuthorId = trackingList.isEmpty() ? 0 : trackingList.get(0).getAuthorId();
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        for (TrackingRecommendationModel tracking : trackingList) {
            promotionList.add(new FeedEnhancedTracking.Promotion(
                    tracking.getAuthorId(),
                    "/content feed - topads - shop",
                    "",
                    "",
                    tracking.getCardPosition(),
                    "",
                    tracking.getAdId(),
                    tracking.getTemplateType()
            ));
        }
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        "impression - topads shop recommendation",
                        String.valueOf(firstAuthorId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventTopadsRecommendationClick(String templateType, int adId, int authorId,
                                               int cardPosition, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                authorId,
                "/content feed - topads - shop",
                "",
                "",
                cardPosition,
                "",
                adId,
                templateType
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        "click avatar - topads shop recommendation",
                        String.valueOf(authorId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventFollowRecommendation(String action, String authorType, String authorId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_FEED,
                CONTENT_FEED_TIMELINE,
                String.format("click %s - %s recommendation", action, authorType),
                authorId
        );
    }

    public void eventFollowCardPost(String action, String activityName, String activityId,
                                    String mediaType) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_FEED,
                CONTENT_FEED_TIMELINE,
                String.format("click %s - %s - %s", action, activityName, mediaType),
                activityId
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
    public void trackClickCreatePostAs(String type, String userId, String shopId) {
        if (type.toLowerCase().contains(Author.TYPE_AFFILIATE)) {
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

    public void eventCardPostImpression(String templateType, String activityName, String mediaType,
                                        String redirectUrl, String imageUrl, String authorId,
                                        int totalContent, int postId, int userId,
                                        int feedPosition) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                postId,
                String.format("/content feed - %s - %s", activityName, mediaType),
                imageUrl,
                redirectUrl,
                feedPosition,
                "",
                formatStringToInt(authorId),
                String.format("%s - %s", templateType, singleOrMultiple(totalContent))
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        String.format("impression - %s - %s", activityName, mediaType),
                        String.valueOf(postId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventCardPostClick(String templateType, String activityName, String mediaType,
                                   String redirectUrl, String imageUrl, String authorId,
                                   int totalContent, int postId, int userId,
                                   int feedPosition) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                postId,
                String.format("/content feed - %s - %s", activityName, mediaType),
                imageUrl,
                redirectUrl,
                feedPosition,
                "",
                formatStringToInt(authorId),
                String.format("%s - %s", templateType, singleOrMultiple(totalContent))
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceClick(
                        String.format("click post - %s - %s", activityName, mediaType),
                        String.valueOf(postId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventProductGridImpression(List<ProductEcommerce> productList,
                                           String activityName, int postId, int userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT_NAME, PRODUCT_VIEW,
                        EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                        EVENT_ACTION, String.format("impression product - %s", activityName),
                        EVENT_LABEL, postId,
                        KEY_USER_ID, userId,
                        KEY_USER_ID_MOD, userId % 50,
                        EVENT_ECOMMERCE, getProductEcommerceImpressions(
                                productList,
                                "/feed - system generated content"
                        )
                )
        );
    }

    public void eventProductGridClick(ProductEcommerce product,
                                      String activityName, int postId, int userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT_NAME, PRODUCT_CLICK,
                        EVENT_CATEGORY, CONTENT_FEED_TIMELINE,
                        EVENT_ACTION, String.format("click product - %s", activityName),
                        EVENT_LABEL, postId,
                        KEY_USER_ID, userId,
                        KEY_USER_ID_MOD, userId % 50,
                        EVENT_ECOMMERCE, getProductEcommerceClick(
                                product,
                                "/feed - system generated content"
                        )
                )
        );
    }

    public void eventVoteImpression(String activityName, String mediaType, String pollId,
                                    int postId, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                postId,
                String.format("/content feed - %s - %s", activityName, mediaType),
                "",
                "",
                0,
                "",
                0,
                ""
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        String.format("impression - %s - %s", activityName, mediaType),
                        String.valueOf(pollId),
                        promotionList,
                        userId
                )
        );
    }

    public void eventVoteClick(String activityName, String mediaType, String pollId, String optionId,
                               String optionName, String imageUrl, int postId, int userId) {
        List<FeedEnhancedTracking.Promotion> promotionList = new ArrayList<>();
        promotionList.add(new FeedEnhancedTracking.Promotion(
                postId,
                String.format("/content feed - %s - %s", activityName, mediaType),
                optionId,
                imageUrl,
                0,
                "",
                0,
                ""
        ));
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getEventEcommerceView(
                        String.format("click post - %s - %s", activityName, mediaType),
                        String.format("%s - %s - %s", pollId, optionId, optionName),
                        promotionList,
                        userId
                )
        );
    }

    public void eventGoToFeedDetail(int postId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_FEED,
                CONTENT_FEED_TIMELINE,
                "click product to feed detail",
                String.valueOf(postId)
        );
    }

    public void eventDetailProductImpression(List<ProductEcommerce> productList, int userId) {
        String firstProductId = productList.isEmpty() ? "0" : productList.get(0).getProductId();
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT_NAME, PRODUCT_VIEW,
                        EVENT_CATEGORY, CONTENT_FEED_TIMELINE_DETAIL,
                        EVENT_ACTION, "impression product",
                        EVENT_LABEL, firstProductId,
                        KEY_USER_ID, userId,
                        KEY_USER_ID_MOD, userId % 50,
                        EVENT_ECOMMERCE, getProductEcommerceImpressions(
                                productList,
                                "/feed detail - product list"
                        )
                )
        );
    }

    public void eventDetailProductClick(ProductEcommerce product, int userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                DataLayer.mapOf(
                        EVENT_NAME, PRODUCT_CLICK,
                        EVENT_CATEGORY, CONTENT_FEED_TIMELINE_DETAIL,
                        EVENT_ACTION, "click product",
                        EVENT_LABEL, product.getProductId(),
                        KEY_USER_ID, userId,
                        KEY_USER_ID_MOD, userId % 50,
                        EVENT_ECOMMERCE, getProductEcommerceClick(
                                product,
                                "/feed detail - product list"
                        )
                )
        );
    }

    private HashMap<String, Object> getProductEcommerceImpressions(List<ProductEcommerce> productList,
                                                                   String list) {
        ArrayList<Object> products = new ArrayList<>();
        for (ProductEcommerce product : productList) {
            HashMap<String, Object> productItem = new HashMap<>();
            productItem.put("name", product.getProductName());
            productItem.put("id", product.getProductId());
            productItem.put("price", formatStringToInt(product.getProductPrice()));
            productItem.put("list", list);
            productItem.put("position", product.getPosition());
            products.add(productItem);
        }

        HashMap<String, Object> ecommerce = new HashMap<>();
        ecommerce.put("currencyCode", "IDR");
        ecommerce.put("impressions", products);
        return ecommerce;
    }

    private HashMap<String, Object> getProductEcommerceClick(ProductEcommerce product,
                                                             String list) {
        HashMap<String, Object> productItem = new HashMap<>();
        productItem.put("name", product.getProductName());
        productItem.put("id", product.getProductId());
        productItem.put("price", formatStringToInt(product.getProductPrice()));
        productItem.put("list", list);
        productItem.put("position", product.getPosition());

        ArrayList<Object> products = new ArrayList<>();
        products.add(productItem);

        HashMap<String, Object> actionField = new HashMap<>();
        actionField.put("list", list);

        HashMap<String, Object> click = new HashMap<>();
        click.put("actionField", actionField);
        click.put("products", products);

        HashMap<String, Object> ecommerce = new HashMap<>();
        ecommerce.put("click", click);
        return ecommerce;
    }

    private int formatStringToInt(String price) {
        try {
            return Integer.valueOf(price.replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
