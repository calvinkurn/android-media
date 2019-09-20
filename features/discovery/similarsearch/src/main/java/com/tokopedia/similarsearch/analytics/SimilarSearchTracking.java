package com.tokopedia.similarsearch.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.List;

import static com.tokopedia.track.TrackAppUtils.EVENT;
import static com.tokopedia.track.TrackAppUtils.EVENT_ACTION;
import static com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY;
import static com.tokopedia.track.TrackAppUtils.EVENT_LABEL;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class SimilarSearchTracking {
    public static final String ECOMMERCE = "ecommerce";

    public static void eventUserSeeSimilarProduct(Context context, String productId, List<Object> productsItem) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                DataLayer.mapOf(EVENT, SimilarSearchAppEventTracking.Event.GenericProductView,
                        EVENT_CATEGORY, SimilarSearchAppEventTracking.Category.EventSimilarProduct,
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
                        EVENT_CATEGORY, SimilarSearchAppEventTracking.Category.EventSimilarProduct,
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
                SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                SimilarSearchAppEventTracking.Action.EventNoSimilarProduct,
                String.format(SimilarSearchAppEventTracking.Label.LableOriginProductId, productId,screenName)
        ));
    }

    public static void eventAddWishList(String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SimilarSearchAppEventTracking.Event.GenericClickWishlist,
                SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                SimilarSearchAppEventTracking.Action.EventAddWishList,
                String.format(SimilarSearchAppEventTracking.Label.LabelProductID, productId)
        ));
    }

    public static void eventRemoveWishList(String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SimilarSearchAppEventTracking.Event.GenericClickWishlist,
                SimilarSearchAppEventTracking.Category.EventSimilarProduct,
                SimilarSearchAppEventTracking.Action.EventRemoveWishList,
                String.format(SimilarSearchAppEventTracking.Label.LabelProductID, productId)
        ));
    }
}
