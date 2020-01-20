package com.tokopedia.similarsearch.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.track.TrackAppUtils.EVENT;
import static com.tokopedia.track.TrackAppUtils.EVENT_ACTION;
import static com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY;
import static com.tokopedia.track.TrackAppUtils.EVENT_LABEL;

public class SimilarSearchTracking {
    public static final String ECOMMERCE = "ecommerce";
    private static String USER_ID = "userId";

    public static void eventUserSeeSimilarProduct(Context context, String productId, List<Object> productsItem) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SimilarSearchAppEventTracking.Event.GenericProductView,
                        EVENT_CATEGORY, SimilarSearchAppEventTracking.Category.SIMILAR_PRODUCT,
                        EVENT_ACTION, SimilarSearchAppEventTracking.Action.EventImpressionProduct,
                        EVENT_LABEL, String.format(SimilarSearchAppEventTracking.Label.LabelProductIDTitle,productId),
                        ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        productsItem.toArray(new Object[productsItem.size()])
                                )
                        )
                )
        );
    }

    public static void eventClickSimilarProduct(String screenName,String productsId,Object productsItem) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SimilarSearchAppEventTracking.Event.GenericProductClick,
                        EVENT_CATEGORY, SimilarSearchAppEventTracking.Category.SIMILAR_PRODUCT,
                        EVENT_ACTION, SimilarSearchAppEventTracking.Action.EventClickSimilarProduct,
                        EVENT_LABEL, String.format(SimilarSearchAppEventTracking.Label.LableOriginProductId, productsId, screenName),
                        ECOMMERCE, DataLayer.mapOf(
                                "click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", "'/similarproduct'"),
                                        "products", DataLayer.listOf(productsItem)
                                )
                        )
                )
        );
    }

    public static void eventUserSeeNoSimilarProduct(String productId,String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SimilarSearchAppEventTracking.Event.GenericViewSearchResult,
                SimilarSearchAppEventTracking.Category.SIMILAR_PRODUCT,
                SimilarSearchAppEventTracking.Action.EventNoSimilarProduct,
                String.format(SimilarSearchAppEventTracking.Label.LableOriginProductId, productId,screenName)
        ));
    }

    public static void eventSuccessWishlistSimilarProduct(WishlistTrackingModel wishlistTrackingModel) {
        Map<String, Object> eventTrackingMap = new HashMap<>();

        eventTrackingMap.put(EVENT, SimilarSearchAppEventTracking.Event.CLICK_WISHLIST);
        eventTrackingMap.put(EVENT_CATEGORY, SimilarSearchAppEventTracking.Category.SIMILAR_PRODUCT.toLowerCase());
        eventTrackingMap.put(EVENT_ACTION, generateWishlistClickEventAction(wishlistTrackingModel.isAddWishlist(), wishlistTrackingModel.isUserLoggedIn()));
        eventTrackingMap.put(EVENT_LABEL, generateWishlistClickEventLabel(wishlistTrackingModel.getProductId(), wishlistTrackingModel.isTopAds(), wishlistTrackingModel.getKeyword()));

        TrackApp.getInstance().getGTM().sendGeneralEvent(eventTrackingMap);
    }

    private static String generateWishlistClickEventAction(boolean isAddWishlist, boolean isLoggedIn) {
        return getAddOrRemoveWishlistAction(isAddWishlist)
                + " - "
                + SimilarSearchAppEventTracking.Action.MODULE
                + " - "
                + getIsLoggedInWishlistAction(isLoggedIn);
    }

    private static String getAddOrRemoveWishlistAction(boolean isAddWishlist) {
        return isAddWishlist ? SimilarSearchAppEventTracking.Action.ADD_WISHLIST : SimilarSearchAppEventTracking.Action.REMOVE_WISHLIST;
    }

    private static String getIsLoggedInWishlistAction(boolean isLoggedIn) {
        return isLoggedIn ? SimilarSearchAppEventTracking.Action.LOGIN : SimilarSearchAppEventTracking.Action.NON_LOGIN;
    }

    private static String generateWishlistClickEventLabel(String productId, boolean isTopAds, String keyword) {
        return productId
                + " - "
                + getTopAdsOrGeneralLabel(isTopAds)
                + " - "
                + keyword;
    }

    private static String getTopAdsOrGeneralLabel(boolean isTopAds) {
        return isTopAds ? SimilarSearchAppEventTracking.Label.TOPADS : SimilarSearchAppEventTracking.Label.GENERAL;
    }
}
