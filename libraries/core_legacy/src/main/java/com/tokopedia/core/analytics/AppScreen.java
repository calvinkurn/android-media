package com.tokopedia.core.analytics;

import com.appsflyer.AFInAppEventType;


/**
 * @author by Nanda J.A on 6/16/2015.
 *         This class for casting and collection screen name
 */
public final class AppScreen {

    public static final String IDENTIFIER_HOME_ACTIVITY = "ParentIndexHome";

    public static final String STATISTIC_PAGE = "Statistic Page";
    public static final String SCREEN_NATIVE_RECHARGE = "Native Recharge";
    public static final String SCREEN_LOGIN = "Login page";
    public static final String SCREEN_BROWSE_PRODUCT = "Browse Product";
    public static final String SCREEN_CATALOG = "Browse Catalog";
    public static final String SCREEN_BROWSE_HOT_LIST = "Browse Hot List";
    public static final String SCREEN_PRODUCT_INFO = "Product Info";
    public static final String SCREEN_HOME_HOTLIST = "Home - hot list";
    public static final String SCREEN_TOPADS = "Top Ads Screen";
    public static final String SCREEN_FIND = "Find";
    public static final String SCREEN_FINISH_TX = "Finish Transaction";
    public static final String SCREEN_CONTACT_US = "Contact us page";
    public static final String SCREEN_WEBVIEW = "General Web View";
    public static final String SCREEN_SHOP_INFO = "Shop info detail page";
    public static final String SCREEN_RECOMMENDATION = "Recommendation page";
    public static final String DEALS_PAGE = "Deals page";
    public static final String SCREEN_SIMILAR_PRODUCT = "Similar Product";
    public static final String SCREEN_DOWNLOAD_INVOICE = "Download invoice page";
    public static final String SCREEN_DEEP_LINK = "Deeplink page";
    public static final String SCREEN_SELLER_REP_HISTORY = "Reputation History";
    public static final String SCREEN_SELLER_SHOP_SCORE = "Shop Score";
    public static final String GOLD_MERCHANT_REDIRECT = "Gold Merchant Redirect Page";
    public static final String SCREEN_DEEPLINK_APPLINKHANDLER = "AppLink Handler";
    public static final String CREATE_SHOP_REDIRECT = "Create Shop Redirect Page";
    public static final String SCREEN_DISCOVERY_PAGE = "Discovery Page";


    public static String GROUP_CHAT = "Group Chat Page";

    public static class UnifyScreenTracker {
        public static final String SCREEN_UNIFY_HOME_BERANDA = "/";
    }

    private AppScreen() {

    }

    public static String convertAFActivityEvent(String tag) {
        if (tag.equals(IDENTIFIER_HOME_ACTIVITY)) {
            return AFInAppEventType.LOGIN;
        } else {
            return AFInAppEventType.CONTENT_VIEW;
        }
    }
}
