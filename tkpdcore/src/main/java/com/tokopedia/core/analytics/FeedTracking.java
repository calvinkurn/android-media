package com.tokopedia.core.analytics;

import java.util.List;

/**
 * Created by henrypriyono on 12/14/17.
 */

public class FeedTracking extends TrackingUtils {

    public static final String CARD_TYPE_PRODUCT = "Product";
    public static final String CARD_TYPE_BANNER = "Banner";
    public static final String CARD_DETAIL_PRODUCT_UPLOAD = "Product Upload";
    public static final String CARD_DETAIL_INSPIRASI = "Inspirasi";
    public static final String CARD_DETAIL_TOPPICKS = "Toppicks";
    public static final String CARD_DETAIL_TOPADS_PRODUCT = "Topads Product";
    public static final String CARD_DETAIL_OS_CAMPAIGN = "OS Campaign";
    public static final String CARD_DETAIL_TOPADS_SHOP = "Topads Shop";
    public static final String CARD_DETAIL_PROMO = "Promo";
    public static final String CARD_DETAIL_SELLER_STORY = "Seller Story";
    public static final String CARD_DETAIL_OS_BRAND = "OS Brand";

    public static void enhanceClickFeedRecomItem(List<Object> objects,
                                                 String eventLabel,
                                                 String actionField,
                                                 String productUrl) {
        getGTMEngine().enhanceClickFeedRecomItem(objects, eventLabel, productUrl, actionField);
    }

    public static String getClickTopPicksEventLabel() {
        return generateClickRecomItemEventLabel(FeedTracking.CARD_TYPE_PRODUCT, FeedTracking.CARD_DETAIL_TOPPICKS);
    }

    public static String getClickTopPicksActionField(int rowNumber) {
        return generateClickRecomItemActionField(rowNumber, FeedTracking.CARD_TYPE_PRODUCT, FeedTracking.CARD_DETAIL_TOPPICKS);
    }

    private static String generateClickRecomItemEventLabel(String cardType, String cardDetail) {
        return cardType + " - " + cardDetail;
    }

    private static String generateClickRecomItemActionField(int rowPosition, String cardType, String cardDetail) {
        return String.format("/feed - product row %d - %s - %s", rowPosition, cardType, cardDetail);
    }
}
