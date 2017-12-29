package com.tokopedia.core.analytics;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 12/14/17.
 */

public class FeedTracking extends TrackingUtils {

    public static final String CARD_TYPE_PRODUCT = "Product";
    public static final String CARD_TYPE_BANNER = "Banner";
    public static final String CARD_DETAIL_PRODUCT_UPLOAD = "Product Upload";
    public static final String CARD_DETAIL_INSPIRASI_PREFIX = "Inspirasi - inspirasi_";
    public static final String CARD_DETAIL_TOPPICKS = "Toppicks";
    public static final String CARD_DETAIL_TOPADS_PRODUCT = "Topads Product";
    public static final String CARD_DETAIL_OS_CAMPAIGN = "OS Campaign";
    public static final String CARD_DETAIL_TOPADS_SHOP = "Topads Shop";
    public static final String CARD_DETAIL_PROMO = "Promo";
    public static final String CARD_DETAIL_SELLER_STORY = "Seller Story";
    public static final String CARD_DETAIL_OS_BRAND = "OS Brand";

    public static void trackEventClickProductUploadEnhanced(String name,
                                                            String id,
                                                            String price,
                                                            String productUrl,
                                                            int rowNumber,
                                                            int itemPosition,
                                                            String userId,
                                                            String eventLabel) {
        String actionField = String.format("/feed - product %d - %s", rowNumber, eventLabel);
        enhanceClickFeedRecomItem(createProductList(name, id, price, itemPosition, actionField, userId),
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
        String actionField = String.format("/feed - product %d - %s", rowNumber, eventLabel);
        enhanceClickFeedRecomItem(createProductList(name, id, price, itemPosition, actionField, userId),
                eventLabel,
                actionField,
                productUrl
        );
    }

    private static void enhanceClickFeedRecomItem(Map<String, Object> objects,
                                                  String eventLabel,
                                                  String actionField,
                                                  String productUrl) {
        getGTMEngine().enhanceClickFeedRecomItem(objects, eventLabel, productUrl, actionField);
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
        getGTMEngine().eventImpressionFeedInspiration(list, eventLabel);
    }

    public static void eventImpressionFeedUploadedProduct(List<Object> list, String eventLabel) {
        getGTMEngine().eventImpressionFeedUploadedProduct(list, eventLabel);
    }
}
