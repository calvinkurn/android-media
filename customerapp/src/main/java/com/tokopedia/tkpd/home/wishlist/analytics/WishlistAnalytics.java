package com.tokopedia.tkpd.home.wishlist.analytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by meta on 18/09/18.
 */
public class WishlistAnalytics {

    private static final String WISHLIST_PAGE = "wishlist page";
    private static final String CLICK_CART_WISHLIST = "click - cek keranjang on wishlist";
    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    private static final String CLICK_WISHLIST = "Click Wishlist";
    private static final String LONG_PRESS_SHORTCUT_WISHLIST = "Share";
    private List<Object> dataLayerList = new ArrayList<>();
    private static volatile WishlistAnalytics wishlistAnalytics = new WishlistAnalytics();

    public static WishlistAnalytics getInstance() {
        return wishlistAnalytics;
    }

    public void trackEventAddToCardProductWishlist(Object dataItem) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "addToCart",
                        "eventCategory", "wishlist page",
                        "eventAction", "click - beli on wishlist",
                        "eventLabel", DEFAULT_VALUE_NONE_OTHER,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "add",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/wishlist"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        )
                )
        );
    }

    public void trackEventClickOnProductWishlist(String position,
                                                 Object dataItem) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "wishlist page",
                        "eventAction", "click product",
                        "eventLabel", position,
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "click",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/wishlist"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        )
                )
        );
    }

    public void trackEventImpressionOnProductWishlist(List<Object> dataItemList) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "wishlist page",
                        "eventAction", "product impressions",
                        "eventLabel", "",
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        dataItemList.toArray(new Object[dataItemList.size()])
                                ))
                )
        );
    }

    public WishlistAnalytics() {
    }

    public void eventClickCariWishlist(String query) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_SEARCH_ITEM_WISHLIST,
                query
        ));

    }

    public void eventSearchWishlist(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                AppEventTracking.Event.HOME_WISHLIST,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.WISHLIST_SEARCH_ITEM,
                label
        ));
    }

    public void eventRemoveWishlist() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_REMOVE_WISHLIST,
                ""
        ));

    }

    public void eventClickCariEmptyWishlist() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_EMPTY_SEARCH_WISHLIST,
                ""
        ));

    }

    public void eventClickCartWishlist() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                AppEventTracking.Event.WISHLIST,
                WISHLIST_PAGE,
                CLICK_CART_WISHLIST,
                ""
        ));

    }

    public void eventWishlistShortcut() {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                new EventTracking(
                        AppEventTracking.Event.LONG_CLICK,
                        AppEventTracking.Category.LONG_PRESS,
                        CLICK_WISHLIST,
                        LONG_PRESS_SHORTCUT_WISHLIST
                ).setUserId(MainApplication.getTkpdCoreRouter().legacySessionHandler().getUserId()).getEvent());
    }

    public void clearDataLayerList() {
        dataLayerList.clear();
    }

    public void sendEmptyWishlistProductImpression(TrackingQueue trackingQueue) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "wishlist page",
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

    public void eventEmptyWishlistProductImpressions(RecommendationItem item, int position) {
        this.dataLayerList.add(DataLayer.mapOf("name", item.getName(),
                "id", item.getProductId(),
                "price", item.getPrice().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "variant", "none/other",
                "category", item.getCategoryBreadcrumbs(),
                "list", "/wishlist - rekomendasi untuk anda - empty_wishlist - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                "position", String.valueOf(position)));
    }

    public void eventEmptyWishlistProductClick(RecommendationItem item, int position) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "wishlist page",
                "eventAction", "click on product recommendation",
                "eventLabel", "",
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField",
                                DataLayer.mapOf("list", "/wishlist - rekomendasi untuk anda - empty_wishlist - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                                        "products", DataLayer.listOf(DataLayer.mapOf(
                                                "name", item.getName(),
                                                "id", item.getProductId(),
                                                "price", item.getPrice().replaceAll("[^0-9]", ""),
                                                "brand", "none/other",
                                                "category", item.getCategoryBreadcrumbs(),
                                                "varian", "none/other",
                                                "position", String.valueOf(position)))))
                )));
    }

}
