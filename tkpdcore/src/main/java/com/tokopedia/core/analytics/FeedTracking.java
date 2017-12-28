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

    public static void trackEventClickProductUploadEnhanced(String name, String id,
                                                          String price, String productUrl,
                                                          int rowNumber, int itemPosition,
                                                          String userId) {
        String actionField = getClickProductUploadActionField(rowNumber);
        enhanceClickFeedRecomItem(createProductList(name, id, price, itemPosition, actionField, userId),
                getClickProductUploadEventLabel(),
                actionField,
                productUrl
        );
    }

    private static String getClickProductUploadEventLabel() {
        return generateClickRecomItemEventLabel(FeedTracking.CARD_TYPE_PRODUCT, FeedTracking.CARD_DETAIL_PRODUCT_UPLOAD);
    }

    private static String getClickProductUploadActionField(int rowNumber) {
        return generateClickRecomItemActionField(rowNumber, FeedTracking.CARD_TYPE_PRODUCT, FeedTracking.CARD_DETAIL_PRODUCT_UPLOAD);
    }

    public static void trackEventClickInspirationEnhanced(String name, String id,
                                                          String price, String productUrl,
                                                          int rowNumber, int itemPosition,
                                                          String source, String userId) {
        String actionField = getClickInspirasiActionField(rowNumber, source);
        enhanceClickFeedRecomItem(createProductList(name, id, price, itemPosition, actionField, userId),
                getClickInspirasiEventLabel(source),
                actionField,
                productUrl
        );
    }

    private static String getClickInspirasiEventLabel(String source) {
        return generateClickRecomItemEventLabel(FeedTracking.CARD_TYPE_PRODUCT, getInspirasiCardDetail(source));
    }

    private static String getClickInspirasiActionField(int rowNumber, String source) {
        return generateClickRecomItemActionField(rowNumber, FeedTracking.CARD_TYPE_PRODUCT, getInspirasiCardDetail(source));
    }

    private static String getInspirasiCardDetail(String source) {
        return FeedTracking.CARD_DETAIL_INSPIRASI_PREFIX + source;
    }

    public static void trackEventClickTopPicksEnhanced(String name, String productUrl, int rowNumber, int itemPosition, String userId) {
        String actionField = getClickTopPicksActionField(rowNumber);
        enhanceClickFeedRecomItem(createProductList(name, "", "", itemPosition, actionField, userId),
                getClickTopPicksEventLabel(),
                actionField,
                productUrl
        );
    }

    private static String getClickTopPicksEventLabel() {
        return generateClickRecomItemEventLabel(FeedTracking.CARD_TYPE_PRODUCT, FeedTracking.CARD_DETAIL_TOPPICKS);
    }

    private static String getClickTopPicksActionField(int rowNumber) {
        return generateClickRecomItemActionField(rowNumber, FeedTracking.CARD_TYPE_PRODUCT, FeedTracking.CARD_DETAIL_TOPPICKS);
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

    private static String generateClickRecomItemActionField(int rowPosition, String cardType, String cardDetail) {
        return String.format("/feed - product %d - %s - %s", rowPosition, cardType, cardDetail);
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
