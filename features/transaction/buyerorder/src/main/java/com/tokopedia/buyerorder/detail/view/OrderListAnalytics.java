package com.tokopedia.buyerorder.detail.view;

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_DETAIL = "my purchase list detail - mp";
    private static final String EVENT_ACTION_DOWNLOAD_INVOICE = "click button download invoice";
    private static final String EVENT_ACTION_CLICK_LIHAT_INVOICE = "click lihat invoice";
    private static final String EVENT_ACTION_CLICK_COPY_BUTTON = "click copy button";
    private static final String EVENT_ACTION_CLICK_PRIMARY_BUTTON = "click primary button";
    private static final String EVENT_ACTION_CLICK_SECONDARY_BUTTON = "click secondary button";

    private static final String ID = "id";
    private static final String CATEGORY_DEALS = "deals";
    private static final String CATEGORY_EVENTS = "hiburan";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String EVENT_TRANSACTION = "transaction";
    private static final String EVENT_CARTEGORY = "digital-deals";
    private static final String EVENT_CATEGORY_BUY_AGAIN_DETAIL_DG = "my purchase list detail - dg";
    private static final String EVENT_CATEGORY_BUY_AGAIN_DETAIL_MP = "my purchase list detail - mp";
    private static final String EVENT_CATEGORY_ORDER_DETAIL_PAGE = "digital - order detail page";

    private static final String ACTION = "view purchase attempt";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String QUANTITY = "quantity";
    private static final String PAYMENT_TYPE = "payment_type";
    private static final String PAYMENT_STATUS = "payment_status";
    private static final String REVENUE = "revenue";
    private static final String AFFILIATION = "affiliation";
    private static final String BRAND = "brand";
    private static final String VARIANT = "variant";
    private static final String SHIPPING = "shipping";
    private static final String CATEGORY = "category";
    private static final String ATTRIBUTION = "attribution";
    private static final String TAX = "tax";
    private static final String COUPON_CODE = "coupon";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_PURCHASE = "purchase";
    private static final String KEY_ACTION_FIELD = "actionField";
    private static final String DEALS_SCREEN_NAME = "/digital/deals/thanks";
    private static final String Events_SCREEN_NAME = "/digital/events/thanks";
    private static final String DEALS_SCREEN_NAME_OMP = "/digital/omp/order-detail";
    private static final String DEALS_SCREEN_NAME_OMS = "/digital/oms/order-detail";
    public static String DIGITAL_EVENT = "digital - event";

    private static final String PRODUCT_CLICK = "productClick";
    private static final String CLICK_ON_WIDGET_RECOMMENDATION = "click on widget recommendation";
    private static final String PRODUCT_VIEW = "productView";
    private static final String IMPRESSION_ON_WIDGET_RECOMMENDATION = "impression on widget recommendation";
    private static final String CLICK_CHECKOUT = "clickCheckout";
    private static final String EVENT = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";
    private static final String ECOMMERCE = "ecommerce";
    private static final String LIST = "list";
    private static final String POSITION = "position";
    private static final String ACTION_FIELD = "actionField";
    private static final String PRODUCTS = "products";
    private static final String CLICK = "click";
    private static final String IMPRESSIONS = "impressions";

    private static final String BUSINESS_UNIT = "businessUnit";
    private static final String CURRENT_SITE = "currentSite";
    private static final String BUSINESS_UNIT_DEALS = "travel & entertainment";
    private static final String CURRENT_SITE_DEALS = "tokopediadigitaldeals";
    private static final String USER_ID = "userId";
    private static final String IS_LOGIN_STATUS = "isLoggedInStatus";

    @Inject
    public OrderListAnalytics() {
    }

    private void sendGtmDataDetails(String eventAction, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_DETAIL, eventAction, eventLabel));

    }

    public void sendDownloadEventData(String eventLabel){
        sendGtmDataDetails(EVENT_ACTION_DOWNLOAD_INVOICE, eventLabel);
    }

    public void sendThankYouEvent(int entityProductId, String entityProductName, int totalTicketPrice, int quantity, String brandName, String orderId, int categoryType, String paymentType, String paymentStatus) {
        Map<String, Object> products = new HashMap<>();
        Map<String, Object> purchase = new HashMap<>();
        Map<String, Object> ecommerce = new HashMap<>();
        Map<String, Object> actionField = new HashMap<>();

        products.put(ID, String.valueOf(entityProductId));
        products.put(NAME, entityProductName);
        products.put(PRICE, totalTicketPrice);
        if (categoryType == 1) {
            products.put("Category", CATEGORY_DEALS);
        } else {
            products.put("Category", CATEGORY_EVENTS);
        }
        products.put(QUANTITY, quantity);
        products.put(BRAND, brandName);
        products.put(COUPON_CODE, "none");
        products.put(VARIANT, "none");

        actionField.put(ID, String.valueOf(orderId));
        actionField.put(REVENUE, totalTicketPrice);
        actionField.put(AFFILIATION, brandName);
        actionField.put(SHIPPING, "0");
        actionField.put(TAX, "none");
        actionField.put(COUPON_CODE, "none");

        purchase.put(KEY_ACTION_FIELD, actionField);
        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_PRODUCTS, Collections.singletonList(products));
        ecommerce.put(KEY_PURCHASE, purchase);

        Map<String, Object> map = new HashMap<>();
        map.put("event", EVENT_TRANSACTION);
        if (categoryType == 1) {
            map.put("eventCategory", EVENT_CARTEGORY);
            map.put("eventLabel", brandName);
        } else {
            map.put("eventCategory", DIGITAL_EVENT);
            map.put("eventLabel", String.format("%s - %s", CATEGORY_EVENTS, entityProductName));
        }
        map.put("eventAction", ACTION);
        map.put("currentSite", "tokopediadigital");
        map.put(PAYMENT_STATUS, paymentStatus);
        map.put(PAYMENT_TYPE, paymentType);
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);
        if (categoryType == 1) {
            TrackApp.getInstance().getGTM().sendScreenAuthenticated(DEALS_SCREEN_NAME);
        } else{
            TrackApp.getInstance().getGTM().sendScreenAuthenticated(Events_SCREEN_NAME);
        }
    }

    public void sendOpenScreenDeals(Boolean isOMP){
        HashMap<String, String> map = new HashMap<>();
        map.put(BUSINESS_UNIT, BUSINESS_UNIT_DEALS);
        map.put(CURRENT_SITE, CURRENT_SITE_DEALS);
        String screenName = DEALS_SCREEN_NAME_OMS;
        if (isOMP){
            screenName = DEALS_SCREEN_NAME_OMP;
        }

        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName, map);
    }

    public static void eventWidgetListView(@NotNull com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationItem contentItemTab, int position) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, "my purchase list - " + contentItemTab.getTitle(),
                EVENT_ACTION, IMPRESSION_ON_WIDGET_RECOMMENDATION,
                EVENT_LABEL, contentItemTab.getTrackingData().getItemType() + " - " + contentItemTab.getTrackingData().getCategoryName() + " - " + (1 + position),
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(DataLayer.mapOf(
                                NAME, contentItemTab.getSubtitle(),
                                ID, contentItemTab.getTrackingData().getProductID(),
                                PRICE, 0,
                                LIST, contentItemTab.getTitle(),
                                POSITION, position + 1,
                                CATEGORY, contentItemTab.getTrackingData().getCategoryName(),
                                VARIANT, contentItemTab.getTrackingData().getItemType()
                                )
                        )
                )

        ));

    }

    public static void eventWidgetClick(@NotNull com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationItem item, int position) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_BUY_AGAIN_DETAIL_DG,
                EVENT_ACTION, CLICK_ON_WIDGET_RECOMMENDATION,
                EVENT_LABEL, item.getTrackingData().getItemType() + " - " + item.getTrackingData().getCategoryName() + " - " + (1 + position),
                ECOMMERCE, DataLayer.mapOf(
                        CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                        LIST, item.getTitle()
                                ),
                                PRODUCTS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                NAME, item.getSubtitle(),
                                                ID, item.getTrackingData().getProductID(),
                                                PRICE, 0,
                                                BRAND, "none",
                                                CATEGORY, item.getTrackingData().getCategoryName(),
                                                VARIANT, item.getTrackingData().getItemType(),
                                                LIST, item.getTitle(),
                                                POSITION, position + 1,
                                                ATTRIBUTION, "none"
                                        )
                                )
                        )

                )

        ));
    }

    public void sendOrderDetailImpression(String userId) {
        String isLoggedInStatus = "true";
        if (userId.isEmpty()) {
            isLoggedInStatus = "false";
        }
        TrackApp.getInstance().getGTM().sendScreenAuthenticated("order-detail-digital", DataLayer.mapStringsOf(
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId,
                IS_LOGIN_STATUS, isLoggedInStatus
        ));
    }

    public void sendInvoiceClickEvent(String categoryName, String productName, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_ORDER_DETAIL_PAGE,
                TrackAppUtils.EVENT_ACTION, EVENT_ACTION_CLICK_LIHAT_INVOICE,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, productName),
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ));
    }

    public void sendCopyButtonClickEvent(String categoryName, String productName, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_ORDER_DETAIL_PAGE,
                TrackAppUtils.EVENT_ACTION, EVENT_ACTION_CLICK_COPY_BUTTON,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s", categoryName, productName),
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ));
    }

    public void sendActionButtonClickEvent(
            String categoryName,
            String productName,
            String buttonId,
            String buttonName,
            String userId
    ) {
        String eventAction;
        if (buttonId == ActionButton.PRIMARY_BUTTON) {
            eventAction = EVENT_ACTION_CLICK_PRIMARY_BUTTON;
        } else {
            eventAction = EVENT_ACTION_CLICK_SECONDARY_BUTTON;
        }
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, CLICK_CHECKOUT,
                TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_ORDER_DETAIL_PAGE,
                TrackAppUtils.EVENT_ACTION, eventAction,
                TrackAppUtils.EVENT_LABEL, String.format("%s - %s - %s", categoryName, productName, buttonName),
                BUSINESS_UNIT, "recharge",
                CURRENT_SITE, "tokopediadigital",
                USER_ID, userId
        ));
    }
}
