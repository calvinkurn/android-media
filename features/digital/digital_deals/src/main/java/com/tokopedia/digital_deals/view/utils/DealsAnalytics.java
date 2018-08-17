package com.tokopedia.digital_deals.view.utils;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.digital_deals.DealsModuleRouter;

import java.util.HashMap;

public class DealsAnalytics {
    public static String EVENT_DEALS_CLICK = "digitalDealsClick";
    public static String EVENT_PRODUCT_VIEW = "productView";
    public static String EVENT_PRODUCT_CLICK = "productClick";
    public static String EVENT_VIEW_PRODUCT = "viewProduct";
    public static String EVENT_DEALS_VIEW = "digitalDealsView";
    public static String EVENT_ADD_TO_CART = "addToCart";
    public static String EVENT_PROMO_VIEW = "promoView";
    public static String EVENT_PROMO_CLICK = "promoClick";
    public static String DIGITAL_DEALS = "digital - deals";


    public static String EVENT_QUERY_LOCATION = "query %s";
    public static String EVENT_CLICK_ON_POPULAR_LOCATION = "click on popular location";
    public static String EVENT_CLICK_ON_LOCATION = "click on %s";
    public static String EVENT_NO_LOCATION = "view no locations found";
    public static String EVENT_CLICK_SEE_ALL_PROMO = "click see all promo";
    public static String EVENT_SELECT_VOUCHER_CATEGORY = "select voucher category";
    public static String EVENT_CLICK_SHARE = "click share";
    public static String EVENT_CLICK_LOVE = "click love";
    public static String EVENT_CLICK_DAFTAR_TRANSAKSI = "click daftar transaksi";
    public static String EVENT_CLICK_BANTUAN = "click bantuan";
    public static String EVENT_CLICK_PROMO = "click promo";
    public static String EVENT_SEARCH_VOUCHER_OR_OUTLET = "search voucher or outlet";
    public static String EVENT_CLICK_SEARCH_BRAND = "click search brand";
    public static String EVENT_CLICK_SEARCH_BRAND_RESULT = "click search brand result";
    public static String EVENT_CLICK_SEE_MORE_BRAND_DETAIL = "click selengkapnya - brand detail";
    public static String EVENT_CLICK_CHECK_LOCATION_PRODUCT_DETAIL = "click check location - product detail";
    public static String EVENT_CLICK_CHECK_TNC_PRODUCT_DETAIL = "click check term and condition - product detail";
    public static String EVENT_CLICK_CHECK_DESCRIPTION_PRODUCT_DETAIL = "check what you will get - product detail";
    public static String EVENT_CLICK_CHECK_REDEEM_INS_PRODUCT_DETAIL = "check how to redeem - product detail";
    public static String EVENT_NO_DEALS = "view no deals found";
    public static String EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION = "view deals not available on your location";
    public static String EVENT_NO_BRAND_FOUND = "view no brand found";
    public static String EVENT_VIEW_BRAND_DETAIL = "view brand detail";
    public static String EVENT_CLICK_BELI = "click beli";
    public static String EVENT_IMPRESSION_PROMO_BANNER = "impression promo banner";
    public static String EVENT_CLICK_PROMO_BANNER = "click promo banner";
    public static String EVENT_IMPRESSION_TRENDING_DEALS = "impression trending deals";
    public static String EVENT_CLICK_TRENDING_DEALS = "click trending deals";
    public static String EVENT_IMPRESSION_POPULAR_BRAND = "impression popular brand";
    public static String EVENT_CLICK_SEE_ALL_BRANDS = "click see all brands";
    public static String EVENT_IMPRESSION_SEARCH_RESULT = "impression search result";
    public static String EVENT_CLICK_SEARCH_RESULT = "click search result";
    public static String EVENT_CLICK_SEARCH_TRENDING = "click search trending deals click";
    public static String EVENT_IMPRESSION_SEARCH_TRENDING = "impression search trending deals";
    public static String EVENT_VIEW_PRODUCT_BRAND_DETAIL = "view product - brand detail";
    public static String EVENT_CLICK_PRODUCT_BRAND_DETAIL = "click product - brand detail";
    public static String EVENT_VIEW_RECOMMENDED_PDT_DETAIL = "impression recommended product - product detail";
    public static String EVENT_CLICK_RECOMMENDED_PDT_DETAIL = "click recommended product - product detail";
    public static String EVENT_VIEW_PRODUCT_CATEGORY_DETAIL = "view product - category detail";
    public static String EVENT_CLICK_PRODUCT_CATEGORY_DETAIL = "click product - category detail";
    public static String EVENT_VIEW_PRODUCT_DETAILS = "view product details";
    public static String EVENT_CHECKOUT = "checkout";
    public static String EVENT_CLICK_PROCEED_TO_PAYMENT = "click proceed to payment";


    public static void sendEventDealsDigitalClick(Context context, String action, String label) {
        if (context == null || !(context.getApplicationContext() instanceof DealsModuleRouter)) {
            return;
        }
        ((DealsModuleRouter) context.getApplicationContext()).getAnalyticTracker()
                .sendEventTracking(EVENT_DEALS_CLICK, DIGITAL_DEALS, action, label);
    }

    public static void sendEventDealsDigitalView(Context context, String action, String label) {
        if (context == null || !(context.getApplicationContext() instanceof DealsModuleRouter)) {
            return;
        }
        ((DealsModuleRouter) context.getApplicationContext()).getAnalyticTracker()
                .sendEventTracking(EVENT_DEALS_VIEW, DIGITAL_DEALS, action, label);

    }

    public static void sendEventEcommerce(Context context, String event, String action, String label,
                                          HashMap<String, Object> ecommerce) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("event", event);
        map.put("eventCategory", DIGITAL_DEALS);
        map.put("eventAction", action);
        map.put("eventLabel", label);
        map.put("ecommerce", ecommerce);
        ((DealsModuleRouter) context.getApplicationContext()).getAnalyticTracker()
                .sendEnhancedEcommerce(map);
    }


}
