package com.tokopedia.topads.sdk.analytics;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.iris.util.ConstantKt;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.LabelGroup;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.collections.CollectionsKt;

/**
 * Author errysuprayogi on 24,January,2019
 */
public class TopAdsGtmTracker {

    private ArrayList dataLayerList;
    private ArrayList dataBundleList;
    private static volatile TopAdsGtmTracker topAdsGtmTracker = new TopAdsGtmTracker();

    public TopAdsGtmTracker() {
        dataLayerList = new ArrayList();
        dataBundleList = new ArrayList();
    }

    public static TopAdsGtmTracker getInstance() {
        return topAdsGtmTracker;
    }

    public void clearDataLayerList() {
        dataLayerList.clear();
        dataBundleList.clear();
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
                                "variant", "none/other",
                                "list", "/homepage - product topads - product upload",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "variant", "none/other",
                                        "category", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public void eventSearchResultProductView(TrackingQueue trackingQueue, String keyword, String screenName, String irisSessionId, String userId) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "search result",
                    "eventAction", "impression - product - topads",
                    "eventLabel", keyword,
                    "userId", userId,
                    "businessUnit", "Ads Solution",
                    "currentSite", "tokopediamarketplace",
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    dataLayerList.toArray(new Object[dataLayerList.size()])
                            )
                    ));
            if(!TextUtils.isEmpty(irisSessionId))
                map.put(ConstantKt.KEY_SESSION_IRIS, irisSessionId);
            trackingQueue.putEETracking((HashMap<String, Object>) map);
            clearDataLayerList();
        }
    }

    public void addSearchResultProductViewImpressions(Product item, int position, String dimension90) {
        this.dataLayerList.add(DataLayer.mapOf("name", item.getName(),
                "id", item.getId(),
                "price", item.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "variant", "none/other",
                "category", getCategoryBreadcrumb(item),
                "list", "/searchproduct - topads productlist",
                "position", position,
                "dimension83", setFreeOngkirDataLayer(item),
                "dimension90", dimension90));

        //GTMv5
        Bundle product = new Bundle();
        product.putString(FirebaseAnalytics.Param.ITEM_ID, item.getId());
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, item.getName());
        product.putString(FirebaseAnalytics.Param.ITEM_BRAND, "none / other");
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, getCategoryBreadcrumb(item));
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, "none / other");
        product.putDouble(FirebaseAnalytics.Param.PRICE, safeParseDouble(item.getPriceFormat().replaceAll("[^0-9]", "")));
        product.putLong(FirebaseAnalytics.Param.INDEX, position);
        this.dataBundleList.add(product);
    }

    private static String getCategoryBreadcrumb(Product product) {
        return !TextUtils.isEmpty(product.getCategoryBreadcrumb()) ?
                product.getCategoryBreadcrumb() : "none / other";
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
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "product recommendation click " + (isLogin ? "-" : "- non login -") + " topads",
                    "eventLabel", tabName,
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField",
                                    DataLayer.mapOf("list", "/ - p2" + (isLogin ? " - " : " - non login - ") + tabName + " - rekomendasi untuk anda - " + recomType + " - product topads"),
                                    "products", DataLayer.listOf(DataLayer.mapOf(
                                            "name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", categoryBreadcrumbs,
                                            "variant", "none/other",
                                            "position", position,
                                            "dimension83", product.getFreeOngkir().isActive() ? "bebas ongkir" : "none / other"))))
            );
            tracker.sendEnhanceEcommerceEvent(map);
        }
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
                                                       boolean isLogin, int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", product.getName(),
                "id", product.getId(),
                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "variant", "none/other",
                "category", categoryBreadcrumbs,
                "list", "/ - p2" + (isLogin ? " - " : " - non login - ") + tabName + " - rekomendasi untuk anda - " + recomendationType + " - product topads",
                "position", position,
                "dimension83", product.getFreeOngkir().isActive() ? "bebas ongkir" : "none / other"));
    }

    public void addInboxProductViewImpressions(Product product, int position, String recommendationType) {
        this.dataLayerList.add(DataLayer.mapOf("name", product.getName(),
                "id", product.getId(),
                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "variant", "none/other",
                "category", product.getCategory().getId(),
                "list", "/inbox - topads - rekomendasi untuk anda - " + recommendationType,
                "position", position));
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public static void eventSearchResultProductClick(Context context, String keyword, Product item, int position, String screenName, String userId) {
        Analytics tracker = getTracker();
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "search result",
                    "eventAction", "click - product - topads",
                    "eventLabel", keyword,
                    "userId", userId,
                    "businessUnit", "Ads Solution",
                    "currentSite", "tokopediamarketplace",
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/searchproduct - topads productlist"),
                                    "products", DataLayer.listOf(DataLayer.mapOf(
                                            "name", item.getName(),
                                            "id", item.getId(),
                                            "price", item.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", getCategoryBreadcrumb(item),
                                            "variant", "none/other",
                                            "position", position,
                                            "dimension83", setFreeOngkirDataLayer(item)))))
            );
            IrisSession irisSession = new IrisSession(context);
            if(!TextUtils.isEmpty(irisSession.getSessionId()))
                map.put(ConstantKt.KEY_SESSION_IRIS, irisSession.getSessionId());
            tracker.sendEnhanceEcommerceEvent(map);
        }
    }

    private static String setFreeOngkirDataLayer(Product item) {
        boolean isFreeOngkirActive = isFreeOngkirActive(item);
        boolean hasLabelGroupFulfillment = hasLabelGroupFulfillment(item.getLabelGroupList());

        if (isFreeOngkirActive && hasLabelGroupFulfillment) {
            return "bebas ongkir extra";
        }
        else if (isFreeOngkirActive && !hasLabelGroupFulfillment) {
            return "bebas ongkir";
        }
        else {
            return "none / other";
        }
    }

    private static boolean isFreeOngkirActive(Product product) {
        return product != null
                && product.getFreeOngkir() != null
                && product.getFreeOngkir().isActive();
    }

    private static boolean hasLabelGroupFulfillment(List<LabelGroup> labelGroupList) {
        return CollectionsKt.any(labelGroupList, labelGroup -> labelGroup.getPosition().equals("fulfillment"));
    }

    public void eventInboxProductClick(Context context, Product product, int position, String recommendationType) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "inbox page",
                "eventAction", "click on product recommendation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/inbox - rekomendasi untuk anda - " + recommendationType + " - product topads"),
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/productdetail - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/intermediary page - topads - promoted",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/hotlist - topads - promoted",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/category/" + product.getCategory().getId() + " - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
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
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public static void eventWishlistProductView(Context context, Product product, String keyword, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productView",
                "eventCategory", "wishlist page",
                "eventAction", "impression on product recommendation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "impressions", DataLayer.listOf(
                                DataLayer.mapOf(
                                        "name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategoryBreadcrumb(),
                                        "variant", "none/other",
                                        "list", "/wishlist - rekomendasi untuk anda - " + product.getRecommendationType() + (product.isTopAds() ? " - product topads" : ""),
                                        "position", String.valueOf(position)))
                ));
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/wishlist - product topads - product upload",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public static void eventWishlistProductClick(Context context, Product product, String keyword, int position) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "wishlist page",
                "eventAction", "click on product recommendation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/wishlist - rekomendasi untuk anda - " + product.getRecommendationType() + (product.isTopAds() ? " - product topads" : ""),
                                        "products", DataLayer.listOf(DataLayer.mapOf(
                                                "name", product.getName(),
                                                "id", product.getId(),
                                                "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                                "brand", "none/other",
                                                "category", product.getCategoryBreadcrumb(),
                                                "variant", "none/other",
                                                "position", String.valueOf(position)))))
                ));
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public static void eventRecommendationWishlistClick(boolean isAdded) {
        getTracker().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                "event", "clickWishlist",
                "eventCategory", "wishlist page",
                "eventAction", "click" + (isAdded ? " add " : " remove ") + "wishlist on product recommendation",
                "eventLabel", ""
        ));
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
                                "products", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/empty cart - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/cart - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/cart - topads",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "variant", "none/other",
                                "list", "/recent view - topads - promoted",
                                "position", position + 1)))
        );
        tracker.sendEnhanceEcommerceEvent(map);
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
                                "products", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                        "id", product.getId(),
                                        "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getCategory().getId(),
                                        "variant", "none/other",
                                        "position", position + 1))))
        );
        tracker.sendEnhanceEcommerceEvent(map);
    }

    private static double safeParseDouble(String price) {
        try {
            return Double.parseDouble(price);
        } catch (Exception e) {
            return 0;
        }
    }


    public static void eventTopAdsHeadlineShopView(int index, CpmData cpm, String keyword, String userId) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "topads headline",
                "eventAction", "impression - topads headline banner",
                "eventLabel", cpm.getCpm().getCpmShop().getId() +", "+ cpm.getId()+ ", " + keyword,
                "ecommerce", DataLayer.mapOf(
                        "promoView", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/search - headline",
                                                "creative", cpm.getRedirect(),
                                                "position", index + 1))
                        )),
                "userId",userId
        );
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public static void eventTopAdsHeadlineProductClick(int index, String keyword, CpmData cpm, String userId) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "Headline Ads on SRP",
                "eventAction", "click - product topads headline",
                "eventLabel", cpm.getCpm().getCpmShop().getId() +", "+cpm.getId()+", "+ cpm.getCpm().getPromotedText(),
                "businessUnit", "TopAds",
                "creative_slot", index,
                "item_list","{}",
                "currentSite", "tokopediamarketplace",
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/search - headline product",
                                                "creative", cpm.getRedirect(),
                                                "position", index + 1))
                        )),
                "userId",userId
        );
        tracker.sendEnhanceEcommerceEvent(map);
    }

    public static void eventTopAdsHeadlineShopClick(int index, String keyword, CpmData cpm, String userId) {
        Analytics tracker = getTracker();
        Map<String, Object> map = DataLayer.mapOf(
                "event", "promoClick",
                "eventCategory", "topads headline",
                "eventAction", "click - shop topads headline",
                "eventLabel", cpm.getCpm().getCpmShop().getId() +", "+cpm.getId()+", "+ keyword,
                "ecommerce", DataLayer.mapOf(
                        "promoClick", DataLayer.mapOf(
                                "promotions", DataLayer.listOf(
                                        DataLayer.mapOf(
                                                "id", cpm.getId(),
                                                "name", "/search - headline",
                                                "creative", cpm.getRedirect(),
                                                "position", index + 1))
                        )),
                "userId",userId
        );
        tracker.sendEnhanceEcommerceEvent(map);
    }
}
