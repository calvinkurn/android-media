package com.tokopedia.navigation.analytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.navigation.domain.model.Recomendation;
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

    public void addInboxProductViewImpressions(Recomendation product, int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", product.getProductName(),
                "id", product.getProductId(),
                "price", product.getPrice().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "varian", "none/other",
                "category", product.getDepartementId(),
                "list", "/inbox - rekomendasi untuk anda "+product.getRecommendationType(),
                "position", position + 1));
    }

    public void eventInboxProductClick(TrackingQueue trackingQueue, Recomendation product, int position) {
        Map<String, Object> map = DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "inbox",
                "eventAction", "click on product recommendation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/inbox - rekomendasi untuk anda - "+product.getRecommendationType()),
                                "product", DataLayer.listOf(DataLayer.mapOf(
                                        "name", product.getProductName(),
                                        "id", product.getProductId(),
                                        "price", product.getPrice().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", product.getDepartementId(),
                                        "varian", "none/other",
                                        "position", position + 1))))
        );
        trackingQueue.putEETracking((HashMap<String, Object>) map);
    }
}
