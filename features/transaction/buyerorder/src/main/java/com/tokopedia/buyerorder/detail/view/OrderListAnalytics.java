package com.tokopedia.buyerorder.detail.view;

import android.os.Bundle;

import javax.inject.Inject;

import com.tokopedia.analyticconstant.DataLayer;
import com.google.gson.Gson;
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel;
import com.tokopedia.atc_common.domain.model.response.AtcMultiData;
import com.tokopedia.buyerorder.detail.data.ActionButton;
import com.tokopedia.buyerorder.detail.data.Items;
import com.tokopedia.buyerorder.detail.data.MetaDataInfo;
import com.tokopedia.buyerorder.detail.data.ShopInfo;
import com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo.RecommendationsItem;
import com.tokopedia.buyerorder.detail.data.recommendationPojo.WidgetGridItem;
import com.tokopedia.buyerorder.list.data.Order;
import com.tokopedia.buyerorder.list.view.adapter.viewmodel.OrderListRecomUiModel;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListAnalytics {

    private static final String PRODUCT_EVENT_NAME = "clickPurchaseList";
    private static final String PRODUCT_EVENT_CATEGORY = "my purchase list - mp";
    private static final String PRODUCT_EVENT_DETAIL = "my purchase list detail - mp";
    private static final String PRODUCT_EVENT_ACTION = "click product";
    private static final String CLICK_PRODUCT_NAME = "click product name";
    private static final String FILTER_EVENT_ACTION = "click quick filter";
    private static final String DATE_EVENT_ACTION = "submit date filter";
    private static final String DATE_FILTER_CLICK_EVENT_ACTION = "click filter button";
    private static final String EVENT_ACTION_CLICK_HELP = "click disini for help";
    private static final String EVENT_ACTION_LIHAT_INVOICE = "click lihat on invoice";
    private static final String EVENT_ACTION_LIHAT_STATUS = "click lihat on status order";
    private static final String EVENT_ACTION_CLICK_SHOP_NAME = "click shop name";
    private static final String EVENT_TICKER_CLICK_ACTION = "click see more on ticker";
    private static final String EVENT_TICKER_CLICK_LINK_ACTION = "click link on ticker";
    private static final String EVENT_ACTION_DOWNLOAD_INVOICE = "click button download invoice";
    private static final String EVENT_ACTION_TULIS_REVIEW = "click button tulis review";
    private static final String TEVENT_TICKER_CLOSE_ACTION = "click x on ticker";
    private static final String EVENT_ACTION_ORDER_DETAIL_IMPRESSION = "impression order detail page";
    private static final String EVENT_ACTION_CLICK_LIHAT_INVOICE = "click lihat invoice";
    private static final String EVENT_ACTION_CLICK_COPY_BUTTON = "click copy button";
    private static final String EVENT_ACTION_CLICK_PRIMARY_BUTTON = "click primary button";
    private static final String EVENT_ACTION_CLICK_SECONDARY_BUTTON = "click secondary button";

    private static final String SEARCH_EVENT_ACTION = "submit search";
    private static final String SEARCH_EVENT_CANCEL_ACTION = "click cancel search";
    private static final String INVOICE_EVENT_ACTION = "click view invoice";
    private static final String LOAD_MORE_EVENT_ACTION = "scroll load more";
    private static final String EVENT_ADD_TO_CART = "addToCart";

    private static final String ID = "id";
    private static final String CATEGORY_DEALS = "deals";
    private static final String CATEGORY_MARKETPLACE = "marketplace";
    private static final String CATEGORY_EVENTS = "hiburan";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String EVENT_TRANSACTION = "transaction";
    private static final String EVENT_CARTEGORY = "digital-deals";
    private static final String EVENT_CATEGORY_BUY_AGAIN_DETAIL = "my purchase list detail - mp";
    private static final String EVENT_CATEGORY_BUY_AGAIN = "my purchase list - mp";
    private static final String EVENT_ACTION_BUY_AGAIN = "click beli lagi";
    private static final String EVENT_CATEGORY_ORDER_DETAIL_PAGE = "digital - order detail page";

    private static final String EVENT_LABEL_BUY_AGAIN_SUCCESS = "success";
    private static final String EVENT_LABEL_BUY_AGAIN_FAILURE = "failure";
    private static final String KEY_ADD = "add";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_SHOP_ID = "shop_id";
    private static final String KEY_CART_ID = "cart_id";
    private static final String KEY_SHOP_TYPE= "shop_type";
    private static final String KEY_SHOP_NAME= "shop_name";
    private static final String KEY_DIMENSION_45 = "dimension45";
    private static final String KEY_DIMENSION_40 = "dimension40";
    private static final String KEY_DIMENSION_38 = "dimension38";
    private static final String ACTION = "view purchase attempt";
    private static final String LABEL = "purchase attempt status";
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
    private static final String ACTION_CLICK_SEE_BUTTON_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster";
    private static final String NONE = "none/other";
    private static final String RECOMMENDATION_PRODUCT_EVENT_CATEGORY = "my purchase list - mp - bom_empty";
    private static final String ADD_WISHLIST = "click add - wishlist on product recommendation";
    private static final String REMOVE_WISHLIST = "click remove - wishlist on product recommendation";
    private List<Object> dataLayerList = new ArrayList<>();
    private String recomTitle;
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
    private static final String CLICK_BOM = "clickBOM";
    private static final String IMPRESSIONS = "impressions";
    private static final String BUY_AGAIN_OPTION_PRODUCT = "product";
    private static final String TICKER_EVENT_ACTION = "view ticker";
    private static final String TICKER_EVENT_NAME = "viewPurchaseList";

    private static final String BUSINESS_UNIT = "businessUnit";
    private static final String CURRENT_SITE = "currentSite";
    private static final String USER_ID = "userId";
    private static final String IS_LOGIN_STATUS = "isLoggedInStatus";

    private static final String ORDER_LIST = "/order list";
    private static final String MAIN_APP = "main app android";

    @Inject
    public OrderListAnalytics() {
    }

    private void sendGtmData(String action, String eventLable) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_CATEGORY, action, eventLable));
    }

    private void sendGtmDataDetails(String eventAction, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(PRODUCT_EVENT_NAME, PRODUCT_EVENT_DETAIL, eventAction, eventLabel));

    }

    public void sendDownloadEventData(String eventLabel){
        sendGtmDataDetails(EVENT_ACTION_DOWNLOAD_INVOICE, eventLabel);
    }

    public void sendTulisReviewEventData(String eventLabel) {
        sendGtmDataDetails(EVENT_ACTION_TULIS_REVIEW, eventLabel);
    }

    public void sendHelpEventData(String eventLabel) {
        sendGtmDataDetails(EVENT_ACTION_CLICK_HELP, eventLabel);
    }

    public void sendProductClickEvent(String eventLabel) {
        sendGtmData(PRODUCT_EVENT_ACTION, eventLabel);
    }

    public void sendLihatStatusClick(String eventLabel) {
        sendGtmDataDetails(EVENT_ACTION_LIHAT_STATUS, eventLabel);
    }

    public void sendClickShopName(String eventLabel) {
        sendGtmDataDetails(EVENT_ACTION_CLICK_SHOP_NAME, eventLabel);
    }

    public void sendDateFilterClickEvent() {
        sendGtmData(DATE_FILTER_CLICK_EVENT_ACTION, "");
    }

    public void sendLihatInvoiceClick(String eventLabel) {
        sendGtmDataDetails(EVENT_ACTION_LIHAT_INVOICE, eventLabel);
    }

    public void sendQuickFilterClickEvent(String filterLabel) {

        sendGtmData(FILTER_EVENT_ACTION, filterLabel);
    }

    public void sendDateFilterSubmitEvent() {

        sendGtmData(DATE_EVENT_ACTION, "");
    }

    public void sendSearchFilterClickEvent(String keyword) {

        sendGtmData(SEARCH_EVENT_ACTION, keyword);
    }

    public void sendSearchFilterCancelClickEvent() {

        sendGtmData(SEARCH_EVENT_CANCEL_ACTION, "");
    }

    public void sendViewInvoiceClickEvent() {

        sendGtmData(INVOICE_EVENT_ACTION, "");
    }

    public void sendActionButtonClickEvent(String eventAction) {

        sendGtmDataDetails(eventAction, "");
    }

    public void sendActionButtonClickEvent(String eventAction,String eventLabel) {

        sendGtmDataDetails(eventAction, eventLabel);
    }

    public void sendActionButtonClickEventList(String eventAction,String eventLabel) {

        sendGtmData(eventAction, eventLabel);
    }

    public void sendLoadMoreEvent(String eventLabel) {

        sendGtmData(LOAD_MORE_EVENT_ACTION, eventLabel);
    }


    public void sendViewTickerEvent() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(TICKER_EVENT_NAME, PRODUCT_EVENT_CATEGORY, TICKER_EVENT_ACTION, ""));
    }


    public void sendClickTickerEvent(String tickerId) {
        sendGtmData(EVENT_TICKER_CLICK_ACTION, tickerId);
    }

    public void sendClickLinkTickerEvent(String link) {
        sendGtmData(EVENT_TICKER_CLICK_LINK_ACTION, link);
    }

    public void sendClickCloseTickerEvent() {
        sendGtmData(TEVENT_TICKER_CLOSE_ACTION, "");

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


    public void sendBuyAgainEvent(List<Items> items, ShopInfo shopInfo, List<AtcMultiData.AtcMulti.BuyAgainData.AtcProduct> responseBuyAgainList, boolean isSuccess, boolean fromDetail, String eventActionLabel, String statusCode) {
        ArrayList<Map<String, Object>> products = new ArrayList<>();
        Map<String, Object> add = new HashMap<>();
        Map<String, Object> ecommerce = new HashMap<>();
        Gson gson = new Gson();
        String eventLabel = isSuccess ? EVENT_LABEL_BUY_AGAIN_SUCCESS : EVENT_LABEL_BUY_AGAIN_FAILURE;

        for (Items item : items) {
            MetaDataInfo metaDataInfo = gson.fromJson(item.getMetaData(), MetaDataInfo.class);
            HashMap<String, Object> product = new HashMap<>();
            product.put(ID, item.getId());
            product.put(NAME, metaDataInfo != null && metaDataInfo.getEntityProductName() != null
                    ? metaDataInfo.getEntityProductName() : item.getTitle());
            product.put(PRICE, item.getTotalPrice());
            product.put(KEY_CATEGORY, item.getCategory());
            product.put(QUANTITY, item.getQuantity());
            product.put(BRAND, metaDataInfo != null && metaDataInfo.getEntityBrandName() != null ? metaDataInfo.getEntityBrandName() : NONE);
            product.put(VARIANT, NONE);
            product.put(KEY_CATEGORY_ID, String.valueOf(item.getCategoryID()));
            product.put(KEY_SHOP_ID, String.valueOf(shopInfo.getShopId()));
            product.put(KEY_SHOP_NAME, shopInfo.getShopName());
            product.put(KEY_SHOP_TYPE, NONE);
            String cartId = NONE;
            for (AtcMultiData.AtcMulti.BuyAgainData.AtcProduct atcProduct : responseBuyAgainList) {
                if (item.getId().equalsIgnoreCase(String.valueOf(atcProduct.getProductId()))) {
                    cartId = String.valueOf(atcProduct.getCartId());
                    break;
                }
            }
            product.put(KEY_CART_ID, cartId);
            product.put(KEY_DIMENSION_45, cartId);
            product.put(KEY_DIMENSION_40, ORDER_LIST + " - " + statusCode);
            product.put(KEY_DIMENSION_38, NONE);
            products.add(product);
        }

        add.put(KEY_PRODUCTS, products);

        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_ADD, add);

        Map<String, Object> map = new HashMap<>();
        map.put("event", EVENT_ADD_TO_CART);

        if(fromDetail)
            map.put("eventCategory", EVENT_CATEGORY_BUY_AGAIN_DETAIL);
        else
            map.put("eventCategory", EVENT_CATEGORY_BUY_AGAIN);
        map.put("eventAction", EVENT_ACTION_BUY_AGAIN + eventActionLabel);
        map.put("eventLabel", eventLabel + " - " + statusCode);
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);

    }

    public void eventEmptyBOMRecommendationProductClick(RecommendationItem item, int position, String recomTitle) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                "event", "productClick",
                "eventCategory", "my purchase list - mp - bom_empty",
                "eventAction", "click - product recommendation",
                "eventLabel", recomTitle,
                "ecommerce", DataLayer.mapOf(
                        "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : "")),
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", item.getName(),
                                        "id", item.getProductId(),
                                        "price", item.getPrice().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", item.getCategoryBreadcrumbs(),
                                        "variant", "none/other",
                                        "list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                                        "position", String.valueOf(position))))
                )));
    }

    public void clearDataLayerList() {
        dataLayerList.clear();
    }

    public void sendEmptyWishlistProductImpression(TrackingQueue trackingQueue) {
        if (!dataLayerList.isEmpty()) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "my purchase list - mp - bom_empty",
                    "eventAction", "impression - product recommendation",
                    "eventLabel", recomTitle,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(
                                    dataLayerList.toArray(new Object[dataLayerList.size()])
                            )
                    ));
            trackingQueue.putEETracking((HashMap<String, Object>) map);
            clearDataLayerList();
        }
    }

    public void eventRecommendationProductImpression(RecommendationItem item, int position, String recomTitle) {
        this.recomTitle = recomTitle;
        this.dataLayerList.add(DataLayer.mapOf(
                "name", item.getName(),
                "id", item.getProductId(),
                "price", item.getPrice().replaceAll("[^0-9]", ""),
                "brand", "none/other",
                "category", item.getCategoryBreadcrumbs(),
                "variant", "none/other",
                "list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + item.getRecommendationType() + (item.isTopAds() ? " - product topads" : ""),
                "position", String.valueOf(position)));
    }

    public void eventRecommendationAddToCart(OrderListRecomUiModel orderListRecomUiModel, AddToCartDataModel addToCartDataModel){
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent((HashMap<String, Object>) DataLayer.mapOf(
                "event", "addToCart",
                "eventCategory", "my purchase list - mp - bom_empty",
                "eventAction", "click add to cart on my purchase list page",
                "eventLabel", orderListRecomUiModel.getRecomTitle(),
                "ecommerce", DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "add", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + orderListRecomUiModel.getRecommendationItem().getRecommendationType() + (orderListRecomUiModel.getRecommendationItem().isTopAds() ? " - product topads" : "")),
                                "products", DataLayer.listOf(DataLayer.mapOf(
                                        "name", orderListRecomUiModel.getRecommendationItem().getName(),
                                        "id", orderListRecomUiModel.getRecommendationItem().getProductId(),
                                        "price", orderListRecomUiModel.getRecommendationItem().getPrice().replaceAll("[^0-9]", ""),
                                        "brand", "none/other",
                                        "category", orderListRecomUiModel.getRecommendationItem().getCategoryBreadcrumbs(),
                                        "variant", "none/other",
                                        "list", "/my_purchase_list_bom_empty - rekomendasi untuk anda - " + orderListRecomUiModel.getRecommendationItem().getRecommendationType() + (orderListRecomUiModel.getRecommendationItem().isTopAds() ? " - product topads" : ""),
                                        "dimension45", addToCartDataModel.getData().getCartId(),
                                        "quantity", orderListRecomUiModel.getRecommendationItem().getMinOrder(),
                                        "shop_id", String.valueOf(orderListRecomUiModel.getRecommendationItem().getShopId()),
                                        "shop_type", orderListRecomUiModel.getRecommendationItem().getShopType(),
                                        "shop_name", orderListRecomUiModel.getRecommendationItem().getShopName(),
                                        "category_id", NONE
                                )))
                )));
    }

    public void sendWishListClickEvent(Boolean isAdd) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                TrackAppUtils.gtmData(
                        PRODUCT_EVENT_NAME,
                        RECOMMENDATION_PRODUCT_EVENT_CATEGORY,
                        isAdd ? ADD_WISHLIST : REMOVE_WISHLIST,
                        recomTitle));
    }

    public static void eventWidgetListView(@NotNull com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationItem contentItemTab, int position) {

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, "my purchase list - " + contentItemTab.getTitle(),
                EVENT_ACTION, IMPRESSION_ON_WIDGET_RECOMMENDATION,
                EVENT_LABEL, "historical - " + contentItemTab.getTitle() + " - " + (1 + position),
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(DataLayer.mapOf(
                                NAME, contentItemTab.getTitle(),
                                ID, contentItemTab.getId(),
                                PRICE, "", // TODO: [Misael] ini ngga ada harga jg
                                LIST, contentItemTab.getTitle(),
                                POSITION, position + 1
                                )
                        )
                )

        ));

    }

    public static void eventWidgetClick(@NotNull com.tokopedia.buyerorder.detail.data.recommendation.recommendationMPPojo2.RecommendationItem item, int position) {

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_BUY_AGAIN_DETAIL,
                EVENT_ACTION, CLICK_ON_WIDGET_RECOMMENDATION,
                EVENT_LABEL, "recommendation - " + item.getTitle() + " - " + (1 + position),
                ECOMMERCE, DataLayer.mapOf(
                        CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                        LIST, item.getTitle()
                                ),
                                PRODUCTS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                NAME, item.getTitle(),
                                                ID, item.getId(),
                                                PRICE, "", // item.getPrice(), // TODO: [Misael] ini response baru ngga ada harga
                                                BRAND, "none",
                                                CATEGORY, item.getTitle(),
                                                VARIANT, "none",
                                                LIST, item.getTitle(),
                                                POSITION, position + 1,
                                                ATTRIBUTION, "none"
                                        )
                                )
                        )

                )

        ));
    }

    public static void eventRecommendationClick(@NotNull RecommendationsItem item, int position) {

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, EVENT_CATEGORY_BUY_AGAIN_DETAIL,
                EVENT_ACTION, CLICK_ON_WIDGET_RECOMMENDATION,
                EVENT_LABEL, "historical - " + item.getCategoryName() + " - " + (1 + position),
                ECOMMERCE, DataLayer.mapOf(
                        CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                        LIST, item.getCategoryName(),
                                        PRODUCTS, DataLayer.listOf(
                                                DataLayer.mapOf(
                                                        NAME, item.getProductName(),
                                                        ID, item.getProductId(),
                                                        PRICE, item.getProductPrice(),
                                                        BRAND,"none",
                                                        CATEGORY , item.getCategoryName(),
                                                        VARIANT, "none",
                                                        LIST, item.getCategoryName(),
                                                        POSITION, position + 1,
                                                        ATTRIBUTION, "none"
                                                )
                                        )
                                )
                        )

                )

        ));
    }

    public static void eventRecommendationListView(@NotNull RecommendationsItem recommendationsItem, int position) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, EVENT_CATEGORY_BUY_AGAIN_DETAIL,
                EVENT_ACTION, IMPRESSION_ON_WIDGET_RECOMMENDATION,
                EVENT_LABEL, "historical - " + recommendationsItem.getCategoryName() + " - " + (1 + position),
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(DataLayer.mapOf(
                                NAME, recommendationsItem.getCategoryName(),
                                ID, recommendationsItem.getProductId(),
                                PRICE, recommendationsItem.getProductPrice(),
                                BRAND, "none",
                                CATEGORY, recommendationsItem.getCategoryName(),
                                VARIANT, "none",
                                LIST, recommendationsItem.getCategoryName(),
                                POSITION, position + 1
                                )
                        )
                )

        ));
    }

    public void sendPageClickEvent(String page) {
        Map<String, Object> map = new HashMap<>();
        map.put("event", "openScreen");
        map.put("screenName", page);
        map.put("isLoggedInStatus", "true");
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);

    }


    public void sendProductViewEvent(Order order, String categoryName, int position, String total) {

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, PRODUCT_EVENT_DETAIL,
                EVENT_ACTION, "view product list",
                EVENT_LABEL, order.status(),
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        IMPRESSIONS, DataLayer.listOf(DataLayer.mapOf(
                                NAME, categoryName,
                                ID, order.getOrderId(),
                                PRICE, total,
                                LIST, "/order list "+order.status(),
                                KEY_CATEGORY, NONE,
                                BRAND, NONE,
                                VARIANT, NONE,
                                POSITION, position + 1
                                )))));

    }

    public void sendProductClickDetailsEvent(Items items, int position, String status) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, PRODUCT_CLICK,
                EVENT_CATEGORY, PRODUCT_EVENT_DETAIL,
                EVENT_ACTION, PRODUCT_EVENT_ACTION,
                EVENT_LABEL, status,
                ECOMMERCE, DataLayer.mapOf(
                        CURRENCY_CODE, IDR,
                        CLICK, DataLayer.mapOf(
                                ACTION_FIELD, DataLayer.mapOf(
                                        LIST, "/order list "+status),

                                PRODUCTS, DataLayer.listOf(
                                        DataLayer.mapOf(
                                                NAME, items.getTitle(),
                                                ID, items.getId(),
                                                PRICE, items.getUnformattedPrice(),
                                                KEY_CATEGORY, NONE,
                                                BRAND, NONE,
                                                VARIANT, NONE,
                                                POSITION, position + 1
                                        ))))));

    }

    public void sendOrderDetailImpression(String categoryName, String productName, String userId) {
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

    public void hitClickProductName(String statusOrder, String userId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(DataLayer.mapOf(
                TrackAppUtils.EVENT, CLICK_BOM,
                TrackAppUtils.EVENT_CATEGORY, PRODUCT_EVENT_CATEGORY,
                TrackAppUtils.EVENT_ACTION, CLICK_PRODUCT_NAME,
                TrackAppUtils.EVENT_LABEL, statusOrder,
                BUSINESS_UNIT, CATEGORY_MARKETPLACE,
                CURRENT_SITE, MAIN_APP,
                USER_ID, userId
        ));
    }
}
