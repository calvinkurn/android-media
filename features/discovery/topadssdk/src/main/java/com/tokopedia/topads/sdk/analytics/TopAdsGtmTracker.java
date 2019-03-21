package com.tokopedia.topads.sdk.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author errysuprayogi on 24,January,2019
 */
public class TopAdsGtmTracker {

    private List<Object> dataLayerList;
    private static volatile TopAdsGtmTracker topAdsGtmTracker = new TopAdsGtmTracker();

    public TopAdsGtmTracker() {
        dataLayerList = new ArrayList<>();
    }

    public static TopAdsGtmTracker getInstance() {
        return topAdsGtmTracker;
    }

    public void clearDataLayerList() {
        dataLayerList.clear();
    }

    public static Analytics getTracker() {
        return TrackApp.getInstance().getGTM();
    }


    public static void eventHomeProductView(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "homepage",
                "eventAction", "homepage - product impression - topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", "none/other",
                                "varian", "none/other",
                                "list", "/homepage - product topads - product upload",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventHomeProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "homepage",
                "eventAction", "homepage - product click - topads",
                "eventLabel", 1 + "." + (position + 1) + " - topads",
                "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                        "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/homepage - product topads - product upload"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "varian", "none/other",
                                        "category", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public void eventSearchResultProductView(TrackingQueue trackingQueue, String keyword) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "search result",
                    "eventAction", "impression - product - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    dataLayerList.toArray(new Object[dataLayerList.size()])
                            )
                    ));
            trackingQueue.putEETracking((HashMap<String, Object>) map);
            clearDataLayerList();
        }
    }

    public void addSearchResultProductViewImpressions(Product product, int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", product.getName(),
                "id", product.getId(),
                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "varian", "none/other",
                "category", product.getCategory().getId(),
                "list", "/searchproduct - topads  productlist",
                "position", position));
    }

    public void eventInboxProductView(TrackingQueue trackingQueue) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "inbox",
                    "eventAction", "impression on topads product",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    dataLayerList.toArray(new Object[dataLayerList.size()])
                            )
                    ));
            trackingQueue.putEETracking((HashMap<String, Object>) map);
            clearDataLayerList();
        }
    }

    public void eventRecomendationProductClick(Context context, Product product,
                                               String tabName, String recomType,
                                               String categoryBreadcrumbs, boolean isLogin,
                                               int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "homepage",
                "eventAction", "product recommendation click " + (isLogin ? "-" : "- non login -") + " topads",
                "eventLabel", tabName,
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/ - p2 - " + tabName + " - rekomendasi untuk anda - " + recomType + " - product topads"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", categoryBreadcrumbs,
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public void eventRecomendationProductView(TrackingQueue trackingQueue, String tabName, boolean isLogin) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "product recommendation impression " + (isLogin ? "-" : "- non login -") + " topads",
                    "eventLabel", tabName,
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    dataLayerList.toArray(new Object[dataLayerList.size()])
                            )
                    ));
            trackingQueue.putEETracking((HashMap<String, Object>) map);
            clearDataLayerList();
        }
    }

    public void addRecomendationProductViewImpressions(Product product, String categoryBreadcrumbs,
                                                       String tabName, String recomendationType,
                                                       int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", product.getName(),
                "id", product.getId(),
                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "varian", "none/other",
                "category", categoryBreadcrumbs,
                "list", "/ - p2 - " + tabName + " rekomendasi untuk anda " + recomendationType + " - product topads",
                "position", position + 1));
    }

    public void addInboxProductViewImpressions(Product product, int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", product.getName(),
                "id", product.getId(),
                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "varian", "none/other",
                "category", product.getCategory().getId(),
                "list", "/inbox - topads rekomendasi untuk anda",
                "position", position + 1));
    }

    public static void eventSearchResultPromoView(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "search result",
                "eventAction", "topads headline impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/search - headline",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventSearchResultPromoShopClick(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "search result",
                "eventAction", "topads headline shop",
                "eventLabel", cpm.getRedirect(),
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/search - headline shop",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventSearchResultPromoProductClick(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "search result",
                "eventAction", "topads headline product",
                "eventLabel", cpm.getRedirect(),
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/search - headline product'",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventSearchResultProductClick(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "search result",
                "eventAction", "click - product - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/searchproduct - topads  productlist"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public void eventInboxProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "inbox",
                "eventAction", "click on topads product",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/inbox - topads - rekomendasi untuk anda"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventProductDetailProductView(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "product detail page",
                "eventAction", "impression product - topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/productdetail - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventProductDetailProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "product detail page",
                "eventAction", "click product - topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/productdetail - topads"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventProductDetailPromoView(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "product detail page",
                "eventAction", "topads headline impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/product detail - topads headline",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventHotlistPromoView(Context context, String hotlistKey, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "hotlist page",
                "eventAction", "topads headline impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/hot topads headline/" + hotlistKey + " - hotlist lainnya",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventProductDetailShopPromoClick(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "product detail page",
                "eventAction", "topads headline shop click",
                "eventLabel", cpm.getApplinks(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/product detail - topads headline shop",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventProductDetailProductPromoClick(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "product detail page",
                "eventAction", "topads headline product click",
                "eventLabel", cpm.getApplinks(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/product detail page - topads headline product",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventHotlistShopPromoClick(Context context, String keyword, String hotlistKey, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "hotlist page",
                "eventAction", "topads headline shop click",
                "eventLabel", cpm.getApplinks(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/hot topads headline shop/" + hotlistKey + " - hotlist lainnya",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventHotlistProductPromoClick(Context context, String keyword, String hotlistKey, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "hotlist page",
                "eventAction", "topads headline product click",
                "eventLabel", "keyword: " + keyword + " - applink: " + cpm.getApplinks(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/hot topads headline product/" + hotlistKey + " - hotlist lainnya",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventIntermediaryShopPromoClick(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "intermediary page",
                "eventAction", "topads headline shop click",
                "eventLabel", cpm.getApplinks(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "intermediary - topads headline shop",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventIntermediaryProductPromoClick(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "intermediary page",
                "eventAction", "topads headline product click",
                "eventLabel", cpm.getApplinks(),
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "intermediary - topads headline product",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventIntermediaryPromoView(Context context, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "intermediary page",
                "eventAction", "topads headline impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/intermediary - headline",
                                                "creative", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventIntermediaryProductView(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "intermediary page",
                "eventAction", "impression - product - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/intermediary page - topads - promoted",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventIntermediaryProductClick(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "intermediary page",
                "eventAction", "click - product - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/intermediary page - topads - promoted"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventHotlistProductView(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "hotlist page",
                "eventAction", "impression - product - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/hotlist - topads - promoted",
                                "position", position + 1),
                                "attribution", ""))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventHotlistProductClick(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "hotlist page",
                "eventAction", "click - product - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/hotlist - topads - promoted"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCategoryProductView(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "category page",
                "eventAction", "impression product - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/category/" + product.getCategory().getId() + " - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCategoryProductClick(Context context, String keyword, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "category page",
                "eventAction", "click product - topads",
                "eventLabel", keyword + " - url:" + product.getUri(),
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/category/" + product.getCategory().getId() + " - topads"),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1),
                                        "attribution", "")))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCategoryPromoView(Context context, String categoryName, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "category page",
                "eventAction", "topads headline impression",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", categoryName + " - topads headline - subcategory",
                                                "creative", cpm.getCpm().getName(),
                                                "creative_url", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCategoryPromoProductClick(Context context, String categoryName, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "category page",
                "eventAction", "topads headline product click",
                "eventLabel", "keyword: " + categoryName + " url: " + cpm.getRedirect(),
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", categoryName + " - topads headline product - subcategory",
                                                "creative", cpm.getCpm().getName(),
                                                "creative_url", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCategoryPromoShopClick(Context context, String categoryName, CpmData cpm, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "category page",
                "eventAction", "topads headline shop click",
                "eventLabel", "keyword: " + categoryName + " url: " + cpm.getRedirect(),
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", categoryName + " - topads headline shop - subcategory",
                                                "creative", cpm.getCpm().getName(),
                                                "creative_url", cpm.getRedirect(),
                                                "position", position + 1))
                        ))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventWishlistProductView(Context context, Product product, String keyword, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "wishlist page",
                "eventAction", "product impression - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/wishlist - product topads - product upload",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventWishlistEmptyProductView(Context context, Product product, String keyword, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "wishlist page",
                "eventAction", "empty wishlist - product impression - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/wishlist - product topads - product upload",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventWishlistProductClick(Context context, Product product, String keyword, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "wishlist page",
                "eventAction", "product click - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/wishlist - product topads - product upload"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventWishlistEmptyProductClick(Context context, Product product, String keyword, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "wishlist page",
                "eventAction", "empty wishlist - product click - topads",
                "eventLabel", keyword,
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/empty wishlist - product topads - product upload"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCartEmptyProductView(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "cart",
                "eventAction", "empty cart - product impression topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/empty cart - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCartEmptyProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "cart",
                "eventAction", "empty cart - product click topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/empty cart - topads"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCartProductView(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "cart",
                "eventAction", "product impression topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/cart - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCartProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "cart",
                "eventAction", "cart - product click topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/cart - topads"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCartAfterProductView(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "cart",
                "eventAction", "after_cart - product impression topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/cart - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventCartAfterProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "after_cart",
                "eventAction", "after_cart - product click topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/cart - topads"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventRecentViewProductView(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "recent view",
                "eventAction", "impression - product - topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(DataLayer.mapOf(
                                "name", product.getName(),
                                "id", product.getId(),
                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                "brand", "none/other",
                                "category", product.getCategory().getId(),
                                "varian", "none/other",
                                "list", "/recent view - topads - promoted",
                                "position", position + 1)))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }

    public static void eventRecentViewProductClick(Context context, Product product, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "recent view",
                "eventAction", "click - product - topads",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "click", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/recent view - topads - promoted"),
                                "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceECommerceEvent(map);
    }
}
