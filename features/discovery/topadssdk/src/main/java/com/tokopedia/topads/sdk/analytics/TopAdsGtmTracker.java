package com.tokopedia.topads.sdk.analytics;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.iris.util.ConstantKt;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.LabelGroup;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
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


    private static String getCategoryBreadcrumb(Product product) {
        return !TextUtils.isEmpty(product.getCategoryBreadcrumb()) ?
                product.getCategoryBreadcrumb() : "none / other";
    }

    public void eventImpressionSearchResultProduct(
            TrackingQueue trackingQueue,
            Product item,
            int position,
            String dimension90,
            String keyword,
            String userId,
            String irisSessionId,
            int topadsTag,
            String dimension115,
            String dimension131,
            String componentId
    ) {
        List<Object> impressionList = createSearchResultProductImpressionDataLayer(
                item,
                position,
                dimension90,
                topadsTag,
                dimension115,
                dimension131,
                componentId
        );

        Map<String, Object> map = DataLayer.mapOf(
                TrackAppUtils.EVENT, TopAdsGtmTrackerConstant.PRODUCT_VIEW,
                TrackAppUtils.EVENT_CATEGORY, TopAdsGtmTrackerConstant.SEARCH_RESULT,
                TrackAppUtils.EVENT_ACTION, TopAdsGtmTrackerConstant.IMPRESSION_PRODUCT_TOPADS,
                TrackAppUtils.EVENT_LABEL, keyword,
                TopAdsGtmTrackerConstant.USER_ID, userId,
                TopAdsGtmTrackerConstant.BUSINESS_UNIT, TopAdsGtmTrackerConstant.ADS_SOLUTION,
                TopAdsGtmTrackerConstant.CURRENT_SITE, TopAdsGtmTrackerConstant.TOKOPEDIAMARKETPLACE,
                TopAdsGtmTrackerConstant.ECOMMERCE, DataLayer.mapOf(
                        TopAdsGtmTrackerConstant.CURRENCY_CODE, TopAdsGtmTrackerConstant.IDR,
                        TopAdsGtmTrackerConstant.IMPRESSIONS, DataLayer.listOf(
                                impressionList.toArray(new Object[impressionList.size()])
                        )
                )
        );

        if(!TextUtils.isEmpty(irisSessionId))
            map.put(ConstantKt.KEY_SESSION_IRIS, irisSessionId);

        trackingQueue.putEETracking((HashMap<String, Object>) map);
    }

    @NonNull
    private List<Object> createSearchResultProductImpressionDataLayer(
            Product item,
            int position,
            String dimension90,
            int topadsTag,
            String dimension115,
            String dimension131,
            String componentId
    ) {
        List<Object> impressionList = new ArrayList<>();

        String list = String.format(
                TopAdsGtmTrackerConstant.SEARCH_PRODUCT_TOPADS_PRODUCTLIST,
                String.valueOf(topadsTag),
                componentId
        );
        Object impression = DataLayer.mapOf(
                TopAdsGtmTrackerConstant.Product.NAME, item.getName(),
                TopAdsGtmTrackerConstant.Product.ID, item.getId(),
                TopAdsGtmTrackerConstant.Product.PRICE, item.getPriceFormat().replaceAll("[^0-9]", ""),
                TopAdsGtmTrackerConstant.Product.BRAND, TopAdsGtmTrackerConstant.NONE_OTHER,
                TopAdsGtmTrackerConstant.Product.VARIANT, TopAdsGtmTrackerConstant.NONE_OTHER,
                TopAdsGtmTrackerConstant.Product.CATEGORY, getCategoryBreadcrumb(item),
                TopAdsGtmTrackerConstant.Product.LIST, list,
                TopAdsGtmTrackerConstant.Product.INDEX, String.valueOf(position),
                TopAdsGtmTrackerConstant.DIMENSION83, setFreeOngkirDataLayer(item),
                TopAdsGtmTrackerConstant.DIMENSION90, dimension90,
                TopAdsGtmTrackerConstant.DIMENSION115, dimension115,
                TopAdsGtmTrackerConstant.DIMENSION131, dimension131
        );

        impressionList.add(impression);

        return impressionList;
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

    public static void eventSearchResultPromoView(CpmData cpm, int position) {
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

    public static void eventSearchResultPromoShopClick(CpmData cpm, int position) {
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

    public static void eventSearchResultPromoProductClick(CpmData cpm, int position) {
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

    public static void eventSearchResultProductClick(
            Context context,
            String keyword,
            Product item,
            int position,
            String userId,
            String dimension90,
            int topadsTag,
            String dimension115,
            String dimension131,
            String componentId
    ) {
        Analytics tracker = getTracker();
        if (tracker != null) {
            String list = String.format(
                    TopAdsGtmTrackerConstant.SEARCH_PRODUCT_TOPADS_PRODUCTLIST,
                    String.valueOf(topadsTag),
                    componentId
            );

            Map<String, Object> productItemMap = DataLayer.mapOf(
                "name", item.getName(),
                "id", item.getId(),
                "price", item.getPriceFormat().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "category", getCategoryBreadcrumb(item),
                "variant", "none/other",
                TopAdsGtmTrackerConstant.Product.INDEX, String.valueOf(position),
                "dimension83", setFreeOngkirDataLayer(item),
                "dimension90", dimension90,
                "dimension115", dimension115,
                "dimension131", dimension131
            );

            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "search result",
                    "eventAction", "click - product - topads",
                    "eventLabel", keyword,
                    "userId", userId,
                    "businessUnit", "Ads Solution",
                    "currentSite", "tokopediamarketplace",
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", list),
                                    "products", DataLayer.listOf(productItemMap)
                            )
                    )
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

    public static void eventTopAdsHeadlineProductClick(int index, CpmData cpm, String userId) {
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
