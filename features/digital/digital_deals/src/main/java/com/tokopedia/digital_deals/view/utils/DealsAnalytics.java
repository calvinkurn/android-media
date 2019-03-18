package com.tokopedia.digital_deals.view.utils;

import android.content.Context;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.digital_deals.view.adapter.DealsBrandAdapter;
import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.model.ProductItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class DealsAnalytics {
    private static final String POSITION = "position";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String LIST = "list";
    public static final String LIST_DEALS_TRENDING = "/deals - trending";
    private static final String LIST_DEALS_SEARCH_BY_LOCATION = "/deals - search by location";
    private static final String HASH_PRODUCTS = "products";
    private static final String HASH_IMPRESSIONS = "impressions";
    public static final String LIST_DEALS_RECOMMENDED_PDP = "/deals - recommendation pdp";
    public static final String LIST_DEALS_SEARCH_TRENDING = "/deals - search by trending";
    private static final String HASH_ACTION_FIELD = "actionField";
    private static final String HASH_CLICK = "click";
    private static final String CATEGORY = "category";
    private static final String QUANTITY = "quantity";
    private static final String HASH_CHECKOUT = "checkout";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String HASH_DETAIL = "detail";
    private static final String LIST_DEALS_CATEGORY = "/deals - category";
    private static final String LIST_DEALS_BRAND = "/deals - brand";
    private static final String CREATIVE = "creative";
    public static final String LIST_DEALS_TOP_BANNER = "/deals - top banner";
    private static final String HASH_ADD = "add";


    public static String EVENT_DEALS_CLICK = "digitalDealsClick";
    public static String EVENT_PRODUCT_VIEW = "productView";
    public static String EVENT_PRODUCT_CLICK = "productClick";
    public static String EVENT_VIEW_PRODUCT = "viewProduct";
    public static String EVENT_DEALS_VIEW = "digitalDealsView";
    public static String EVENT_ADD_TO_CART = "addToCart";
    public static String EVENT_PROMO_VIEW = "promoView";
    public static String EVENT_PROMO_CLICK = "promoClick";
    public static String DIGITAL_DEALS = "digital - deals";


    public static String EVENT_QUERY_LOCATION = "cari lokasi";
    public static String EVENT_CLICK_ON_POPULAR_LOCATION = "click on popular location";
    public static String EVENT_CLICK_ON_LOCATION = "click on %s";
    public static String EVENT_NO_LOCATION = "view no locations found";
    public static String EVENT_CLICK_SEE_ALL_PROMO = "click see all promo";
    public static String EVENT_SELECT_VOUCHER_CATEGORY = "select voucher category";
    public static String EVENT_CLICK_SHARE = "click share";
    public static String EVENT_CLICK_LOVE = "click love";
    public static String EVENT_CLICK_DAFTAR_TRANSAKSI = "click transaction list";
    public static String EVENT_CLICK_BANTUAN = "click bantuan";
    public static String EVENT_CLICK_PROMO = "click promo";
    public static String EVENT_SEARCH_VOUCHER_OR_OUTLET = "search voucher or outlet";
    public static String EVENT_CLICK_SEARCH_BRAND = "click search brand";
    public static String EVENT_CLICK_SEARCH_BRAND_RESULT = "click search brand result";
    public static String EVENT_CLICK_POPULAR_BRAND = "click popular brand";
    public static String EVENT_CLICK_SEE_MORE_BRAND_DETAIL = "click selengkapnya - brand detail";
    public static String EVENT_CLICK_CHECK_LOCATION_PRODUCT_DETAIL = "click check location - product detail";
    public static String EVENT_CLICK_CHECK_TNC_PRODUCT_DETAIL = "click check term and condition - product detail";
    public static String EVENT_CLICK_CHECK_DESCRIPTION_PRODUCT_DETAIL = "check what you will get - product detail";
    public static String EVENT_CLICK_CHECK_REDEEM_INS_PRODUCT_DETAIL = "check how to redeem - product detail";
    public static String EVENT_NO_DEALS = "view no deals found";
    public static String EVENT_NO_DEALS_AVAILABLE_ON_YOUR_LOCATION = "view deals not available on your location";
    public static String EVENT_NO_BRAND_FOUND = "view no brand found";
    public static String EVENT_VIEW_BRAND_DETAIL = "view brand detail";
    public static String EVENT_CLICK_BELI = "click beli";
    public static String EVENT_IMPRESSION_PROMO_BANNER = "impression promo banner";
    public static String EVENT_CLICK_PROMO_BANNER = "click promo banner";
    public static String EVENT_IMPRESSION_TRENDING_DEALS = "impression trending deals";
    public static String EVENT_CLICK_TRENDING_DEALS = "click trending deals";
    public static String EVENT_IMPRESSION_POPULAR_BRAND = "impression popular brand";
    public static String EVENT_CLICK_SEE_ALL_BRANDS = "click see all brands";
    public static String EVENT_IMPRESSION_SEARCH_RESULT = "impression search result";
    public static String EVENT_CLICK_SEARCH_RESULT = "click search result";
    public static String EVENT_CLICK_SEARCH_TRENDING = "click search trending deals click";
    public static String EVENT_IMPRESSION_SEARCH_TRENDING = "impression search trending deals";
    public static String EVENT_VIEW_PRODUCT_BRAND_DETAIL = "view product - brand detail";
    public static String EVENT_CLICK_PRODUCT_BRAND_DETAIL = "click product - brand detail";
    public static String EVENT_VIEW_RECOMMENDED_PDT_DETAIL = "impression recommended product - product detail";
    public static String EVENT_CLICK_RECOMMENDED_PDT_DETAIL = "click recommended product - product detail";
    public static String EVENT_VIEW_PRODUCT_CATEGORY_DETAIL = "view product - category detail";
    public static String EVENT_CLICK_PRODUCT_CATEGORY_DETAIL = "click product - category detail";
    public static String EVENT_VIEW_PRODUCT_DETAILS = "view product detail";
    public static String EVENT_CHECKOUT = "checkout";
    public static String EVENT_CLICK_PROCEED_TO_PAYMENT = "click proceed to payment";
    public static String EVENT_VIEW_SEARCH_BRAND_RESULT = "view search brand result";




    @Inject
    public DealsAnalytics() {
    }

    public void sendEventDealsDigitalClick(String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_DEALS_CLICK, DIGITAL_DEALS, action, label == null ? "" : label.toLowerCase());

    }

    public void sendEventDealsDigitalView(String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_DEALS_VIEW, DIGITAL_DEALS, action, label == null ? "" : label.toLowerCase());

    }

    public void sendEventEcommerce(String event, String action, String label,
                                   HashMap<String, Object> ecommerce) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("event", event);
        map.put("eventCategory", DIGITAL_DEALS);
        map.put("eventAction", action);
        map.put("eventLabel", label == null ? "" : label.toLowerCase());
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(map);
    }

    public void sendDealImpressionEvent(boolean isHeaderAdded, boolean isBrandHeaderAdded, boolean topDealsLayout, ProductItem productItem, String categoryName, int pageType, int position) {
        try {
            String event = null, action = null, label = null;
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> impressions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            event = DealsAnalytics.EVENT_PRODUCT_VIEW;
            if (isHeaderAdded || isBrandHeaderAdded)
                position -= 1;
            productMap.put(POSITION, position);
            productMap.put(ID, productItem.getId());
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(PRICE, productItem.getSalesPrice());
            label = String.format("%s - %s - %s", productItem.getBrand().getTitle()
                    , productItem.getDisplayName()
                    , position);
            if (pageType == DealsCategoryAdapter.HOME_PAGE) {
                productMap.put(LIST, LIST_DEALS_TRENDING);
                action = DealsAnalytics.EVENT_IMPRESSION_TRENDING_DEALS;

            } else if (pageType == DealsCategoryAdapter.SEARCH_PAGE) {

                productMap.put(LIST, LIST_DEALS_SEARCH_BY_LOCATION);
                if (isHeaderAdded && topDealsLayout)
                    action = DealsAnalytics.EVENT_IMPRESSION_SEARCH_TRENDING;
                else
                    action = DealsAnalytics.EVENT_IMPRESSION_SEARCH_RESULT;

            } else if (pageType == DealsCategoryAdapter.CATEGORY_PAGE) {
                productMap.put(LIST, "/deals - " + categoryName);
                action = DealsAnalytics.EVENT_VIEW_PRODUCT_CATEGORY_DETAIL;
                label = String.format("%s - %s - %s - %s", categoryName, productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position);

            } else if (pageType == DealsCategoryAdapter.BRAND_PAGE) {
                productMap.put(LIST, "/deals - " + productItem.getBrand().getTitle());
                action = DealsAnalytics.EVENT_VIEW_PRODUCT_BRAND_DETAIL;
            } else if (pageType == DealsCategoryAdapter.DETAIL_PAGE) {
                productMap.put(LIST, LIST_DEALS_RECOMMENDED_PDP);
                action = DealsAnalytics.EVENT_VIEW_RECOMMENDED_PDT_DETAIL;
                event = DealsAnalytics.EVENT_VIEW_PRODUCT;
            }
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            impressions.put(HASH_PRODUCTS, productMaps);
            ecommerce.put(HASH_IMPRESSIONS, impressions);
            sendEventEcommerce(event
                    , action
                    , label == null ? "" : label.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendDealClickEvent(ProductItem productItem, String categoryName, int pageType, int position) {
        try {
            String action = null, label;

            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> list = new HashMap<>();
            HashMap<String, Object> click = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            productMap.put(ID, productItem.getId());
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(PRICE, productItem.getSalesPrice());
            productMap.put(POSITION, position);
            label = String.format("%s - %s - %s", productItem.getBrand().getTitle()
                    , productItem.getDisplayName()
                    , position);
            if (pageType == DealsCategoryAdapter.HOME_PAGE) {
                list.put(LIST, LIST_DEALS_TRENDING);
                productMap.put(LIST, LIST_DEALS_TRENDING);
                action = DealsAnalytics.EVENT_CLICK_TRENDING_DEALS;

            } else if (pageType == DealsCategoryAdapter.SEARCH_PAGE) {
                list.put(LIST, LIST_DEALS_SEARCH_TRENDING);
                productMap.put(LIST, LIST_DEALS_SEARCH_TRENDING);
                action = DealsAnalytics.EVENT_CLICK_SEARCH_RESULT;

            } else if (pageType == DealsCategoryAdapter.CATEGORY_PAGE) {

                label = String.format("%s - %s - %s - %s", categoryName, productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position);
                list.put(LIST, LIST_DEALS_CATEGORY);
                productMap.put(LIST, "/deals - " + categoryName);
                action = DealsAnalytics.EVENT_CLICK_PRODUCT_CATEGORY_DETAIL;

            } else if (pageType == DealsCategoryAdapter.BRAND_PAGE) {
                list.put(LIST, LIST_DEALS_BRAND);
                productMap.put("list", "/deals - " + productItem.getBrand().getTitle());
                action = DealsAnalytics.EVENT_CLICK_PRODUCT_BRAND_DETAIL;

            }
            click.put(HASH_ACTION_FIELD, list);
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            click.put(HASH_PRODUCTS, productMaps);
            ecommerce.put(HASH_CLICK, click);
            sendEventEcommerce(DealsAnalytics.EVENT_PRODUCT_CLICK
                    , action
                    , label == null ? "" : label.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceClickEvent(ProductItem productItem, int position, String action, String listt) {

        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, productItem.getId());
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(PRICE, productItem.getSalesPrice());
            productMap.put(POSITION, position);
            HashMap<String, Object> list = new HashMap<>();
            HashMap<String, Object> click = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            list.put(LIST, listt);
            productMap.put(LIST, listt);

            click.put(HASH_ACTION_FIELD, list);
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            click.put(HASH_PRODUCTS, productMaps);
            ecommerce.put(HASH_CLICK, click);
            sendEventEcommerce(DealsAnalytics.EVENT_PRODUCT_CLICK
                    , action
                    , String.format("%s - %s - %s", productItem.getBrand().getTitle()
                            , productItem.getDisplayName()
                            , position).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }

    public void sendEcommercePayment(int id, int quantity, int salesPrice, String displayName, String brandName, boolean promoApplied) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, id);
            productMap.put(NAME, displayName);
            productMap.put(PRICE, (quantity * salesPrice));
            productMap.put(CATEGORY, "deals");
            productMap.put(QUANTITY, quantity);
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            HashMap<String, Object> checkout = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            checkout.put(HASH_PRODUCTS, productMaps);
            ecommerce.put(HASH_CHECKOUT, checkout);
            String promo;
            if (promoApplied)
                promo = "promo";
            else
                promo = "non promo";
            sendEventEcommerce(DealsAnalytics.EVENT_CHECKOUT
                    , DealsAnalytics.EVENT_CLICK_PROCEED_TO_PAYMENT
                    , String.format("%s - %s - %s", brandName
                            , displayName, promo).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceQuantity(int id, int quantity, int salesPrice, String displayName, String brandName) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, id);
            productMap.put(NAME, displayName);
            productMap.put(PRICE, (salesPrice * quantity));
            productMap.put(CATEGORY, "deals");
            productMap.put(QUANTITY, quantity);
            HashMap<String, Object> add = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            add.put(HASH_PRODUCTS, productMaps);
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_ADD, add);
            sendEventEcommerce(DealsAnalytics.EVENT_ADD_TO_CART
                    , DealsAnalytics.EVENT_CHECKOUT
                    , String.format("%s - %s - %s - %s", brandName
                            , displayName, quantity, salesPrice).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceDealDetail(int id, int salesPrice, String displayName, String brandName) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, id);
            productMap.put(NAME, displayName);
            productMap.put(PRICE, salesPrice);
            productMap.put(LIST, "");
            HashMap<String, Object> list = new HashMap<>();
            HashMap<String, Object> detail = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            list.put(LIST, "");
            detail.put(HASH_ACTION_FIELD, list);
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            detail.put(HASH_PRODUCTS, productMaps);
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_DETAIL, detail);
            sendEventEcommerce(DealsAnalytics.EVENT_VIEW_PRODUCT
                    , DealsAnalytics.EVENT_VIEW_PRODUCT_DETAILS
                    , String.format("%s - %s", brandName
                            , displayName).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceBrand(int id, int position, String creative, String event, String action, String name) {
        try {
            HashMap<String, Object> bannerMap = new HashMap<>();
            bannerMap.put(ID, id);
            bannerMap.put(NAME, name);
            bannerMap.put(POSITION, position);
            bannerMap.put(CREATIVE, creative);
            HashMap<String, Object> ecommerce = new HashMap<>();
            ecommerce.put(event, bannerMap);
            sendEventEcommerce(event, action,
                    String.format("%s - %s", creative
                            , position).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }

}
