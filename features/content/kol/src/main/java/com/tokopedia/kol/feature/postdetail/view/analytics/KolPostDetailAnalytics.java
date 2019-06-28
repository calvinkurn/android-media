package com.tokopedia.kol.feature.postdetail.view.analytics;

import com.tokopedia.track.TrackApp;

import javax.inject.Inject;

/**
 * @author by yoasfs on 2019-06-28
 */
public class KolPostDetailAnalytics {

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

    private static final String EVENT_CLICK_SOCIAL_COMMERCE = "clickSocialCommerce";
    private static final String USER_PROFILE_SOCIALCOMMERCE = "user profile socialcommerce";
    private static final String USER_PROFILE_SOCIALCOMMERCE_CONTENT_DETAIL = USER_PROFILE_SOCIALCOMMERCE + "content detail";


    private static final String CLICK_COMMENT = "click comment";
    private static final String CLICK_LIKE = "click like";


    public void eventClickLike(String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SOCIAL_COMMERCE,
                USER_PROFILE_SOCIALCOMMERCE_CONTENT_DETAIL,
                CLICK_LIKE,
                userId
        );
    }
    public void eventClickComment(String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                EVENT_CLICK_SOCIAL_COMMERCE,
                USER_PROFILE_SOCIALCOMMERCE_CONTENT_DETAIL,
                CLICK_COMMENT,
                userId
        );
    }

}
