package com.tokopedia.navigation.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author errysuprayogi on 15,March,2019
 */
public class InboxGtmTracker {
    private final String DATA_DIMENSION_83 = "dimension83";
    private final String VALUE_BEBAS_ONGKIR = "bebas ongkir";
    private final String VALUE_NONE_OTHER = "none / other";

    private List<Object> dataLayerList;
    private static volatile InboxGtmTracker gtmTracker = new InboxGtmTracker();

    public static InboxGtmTracker getInstance() {
        return gtmTracker;
    }

    public static ContextAnalytics getTracker(Context context) {
        return TrackApp.getInstance().getGTM();
    }

    public InboxGtmTracker() {
        dataLayerList = new ArrayList<>();
    }

    public void clearDataLayerList() {
        dataLayerList.clear();
    }

    public void eventInboxProductView(TrackingQueue trackingQueue) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "inbox page",
                    "eventAction", "impression on product recommendation",
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

    public void addInboxProductViewImpressions(RecommendationItem recommendationItem, int position, boolean isTopAds) {
        this.dataLayerList.add(DataLayer.mapOf("name", recommendationItem.getName(),
                "id", recommendationItem.getProductId(),
                "price", recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "varian", "none/other",
                "category", recommendationItem.getDepartmentId(),
                "list", "/inbox - rekomendasi untuk anda - " + recommendationItem.getRecommendationType() + (isTopAds ? " - product topads" : ""),
                "position", String.valueOf(position),
                DATA_DIMENSION_83, recommendationItem.isFreeOngkirActive()?VALUE_BEBAS_ONGKIR:VALUE_NONE_OTHER)
                );
    }

    public void eventClickRecommendationWishlist(Context context, RecommendationItem recommendationItem, boolean isAdd){
        ContextAnalytics tracker = getTracker(context);
        if(tracker != null) {
            Map<String, Object> map =
                    DataLayer.mapOf(
                            "event", "clickInbox",
                            "eventCategory", "inbox page",
                            "eventAction", String.format("click %s wishlist on product recommendation", isAdd ? "add" : "remove"),
                            "eventLabel", ""
                    );
            tracker.sendEnhanceEcommerceEvent(map);
        }
    }

    public void eventInboxProductClick(Context context, RecommendationItem recommendationItem, int position, boolean isTopAds) {
        ContextAnalytics tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "inbox page",
                    "eventAction", "click on product recommendation",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField",
                                    DataLayer.mapOf("list", "/inbox - rekomendasi untuk anda - "+recommendationItem.getRecommendationType() + (isTopAds ? " - product topads":"")),
                                    "products", DataLayer.listOf(DataLayer.mapOf(
                                            "name", recommendationItem.getName(),
                                            "id", recommendationItem.getProductId(),
                                            "price", recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", recommendationItem.getCategoryBreadcrumbs(),
                                            "varian", "none/other",
                                            "position", String.valueOf(position),
                                            DATA_DIMENSION_83, recommendationItem.isFreeOngkirActive()?VALUE_BEBAS_ONGKIR:VALUE_NONE_OTHER))))
            );
            tracker.sendEnhanceEcommerceEvent(map);
        }
    }
}
