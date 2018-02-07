package com.tokopedia.core.analytics;

import com.google.android.gms.tagmanager.DataLayer;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 12/14/17.
 */

public class FeedTracking extends TrackingUtils {

    public static final String EVENT_NAME = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";
    public static final String EVENT_ECOMMERCE = "ecommerce";
    public static final String STATIC_VALUE_PRODUCT_VIEW = "productView";
    public static final String STATIC_VALUE_PRODUCT_CLICK = "productClick";
    public static final String STATIC_VALUE_HOMEPAGE = "homepage";
    public static final String STATIC_VALUE_FEED_ITEM_IMPRESSION = "feed - item impression";
    public static final String STATIC_VALUE_FEED_CLICK_CARD_ITEM = "feed - click card item";
    public static final String STATIC_FORMAT_ACTION_FIELD_FEED_PRODUCT = "/feed - product %d - %s";

    public static void trackEventClickProductUploadEnhanced(String name,
                                                            String id,
                                                            String price,
                                                            String productUrl,
                                                            int rowNumber,
                                                            int itemPosition,
                                                            String userId,
                                                            String eventLabel) {
        String actionField = String.format(STATIC_FORMAT_ACTION_FIELD_FEED_PRODUCT, rowNumber, eventLabel);
        eventClickFeedProductItem(createProductList(name, id, price, itemPosition, actionField, userId),
                eventLabel,
                actionField,
                productUrl
        );
    }

    public static void trackEventClickInspirationEnhanced(String name,
                                                          String id,
                                                          String price,
                                                          String productUrl,
                                                          int rowNumber,
                                                          int itemPosition,
                                                          String source,
                                                          String userId,
                                                          String eventLabel) {
        String actionField = String.format(STATIC_FORMAT_ACTION_FIELD_FEED_PRODUCT, rowNumber, eventLabel);
        eventClickFeedProductItem(createProductList(name, id, price, itemPosition, actionField, userId),
                eventLabel,
                actionField,
                productUrl
        );
    }

    private static String generateClickRecomItemEventLabel(String cardType, String cardDetail) {
        return cardType + " - " + cardDetail;
    }

    private static Map<String, Object> createProductList(String name, String id, String price,
                                                         int itemPosition,
                                                         String actionField, String userId) {
        com.tokopedia.core.analytics.model.Product product = new com.tokopedia.core.analytics.model.Product();
        product.setName(name);
        product.setId(id);
        product.setPrice(price);
        product.setBrand("");
        product.setCategoryId("");
        product.setCategoryName("");
        product.setVariant("");
        product.setList(actionField);
        product.setPosition(itemPosition);
        product.setUserId(userId);

        return product.getProductAsDataLayerForFeedRecomItemClick();
    }

    public static void eventImpressionFeedInspiration(List<Object> list, String eventLabel) {
        eventImpressionFeedProductItem(list, eventLabel);
    }

    public static void eventImpressionFeedUploadedProduct(List<Object> list, String eventLabel) {
        eventImpressionFeedProductItem(list, eventLabel);
    }

    private static void eventTrackingEnhanceFeed(Map<String, Object> trackingData) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(trackingData);
    }

    private static void eventImpressionFeedProductItem(List<Object> list, String eventLabel) {
        eventTrackingEnhanceFeed(
                DataLayer.mapOf(EVENT_NAME, STATIC_VALUE_PRODUCT_VIEW,
                        EVENT_CATEGORY, STATIC_VALUE_HOMEPAGE,
                        EVENT_ACTION, STATIC_VALUE_FEED_ITEM_IMPRESSION,
                        EVENT_LABEL, eventLabel,
                        EVENT_ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        list.toArray(new Object[list.size()])
                                ))
                )
        );
    }

    private static void eventClickFeedProductItem(Map<String, Object> objects,
                                                  String eventLabel,
                                                  String actionField,
                                                  String productUrl) {
        eventTrackingEnhanceFeed(
                DataLayer.mapOf(EVENT_NAME, STATIC_VALUE_PRODUCT_CLICK,
                        EVENT_CATEGORY, STATIC_VALUE_HOMEPAGE,
                        EVENT_ACTION, STATIC_VALUE_FEED_CLICK_CARD_ITEM,
                        EVENT_LABEL, eventLabel,
                        EVENT_ECOMMERCE, DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", actionField),
                                        "products", DataLayer.listOf(objects)
                                )
                        )
                )
        );
    }
}
