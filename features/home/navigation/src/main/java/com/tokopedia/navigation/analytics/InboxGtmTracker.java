package com.tokopedia.navigation.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.navigation.domain.model.Recomendation;
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
                    "eventCategory", "inbox",
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

    public void addInboxProductViewImpressions(RecommendationItem recommendationItem, int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", recommendationItem.getName(),
                "id", recommendationItem.getProductId(),
                "price", recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "varian", "none/other",
                "category", recommendationItem.getDepartmentId(),
                "list", "/inbox - rekomendasi untuk anda - "+recommendationItem.getRecommendationType(),
                "position", position));
    }

    public void eventInboxProductClick(Context context, RecommendationItem recommendationItem, int position) {
        ContextAnalytics tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "inbox",
                    "eventAction", "click on product recommendation",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField",
                                    DataLayer.mapOf("list", "/inbox - rekomendasi untuk anda - "+recommendationItem.getRecommendationType()),
                                    "product", DataLayer.listOf(DataLayer.mapOf(
                                            "name", recommendationItem.getName(),
                                            "id", recommendationItem.getProductId(),
                                            "price", recommendationItem.getPrice().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", recommendationItem.getDepartmentId(),
                                            "varian", "none/other",
                                            "position", position))))
            );
            tracker.sendEnhanceEcommerceEvent(map);
        }
    }
}
