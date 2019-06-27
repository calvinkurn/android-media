package com.tokopedia.digital_deals.view.utils;

import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class DealsAnalytics {
    public static final int SEE_ALL_BRANDS_HOME = 1;
    public static final int SEE_ALL_BRANDS_CATEGORY = 2;
    public static final int CLICK_BRANDS_CATEGORY = 1;
    public static final int CLICK_BRANDS_SEARCH = 2;
    public static final int CLICK_BRANDS_HOME = 3;
    public static final int CLICK_BRAND_NATIVE_PAGE = 4;
    public static final String ACTION_CLICK_BRAND = "click brand";
    public static final String TRENDING_DEALS = "trending deals";
    public static final String CURATED_DEALS = "curated deals";
    public static final String BRAND_DEALS = "brand deals";
    public static final String CATEGORY_DEALS = "category deals";
    private static final String POSITION = "position";
    private static final String ID = "id";
    private static final String CATEGORY_ID = "category_id";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String BRAND = "brand";
    private static final String DEALS = "deals";
    private static final String PROMO_ID = "promo_id";
    private static final String PROMO_CODE = "promo_code";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_IMPRESSIONS = "impressions";
    private static final String LIST = "list";
    public static final String LIST_DEALS_TRENDING = "/deals - trending";
    private static final String LIST_DEALS_SEARCH_BY_LOCATION = "/deals - search by location";
    private static final String KEY_PROMOTIONS = "promotions";
    private static final String KEY_OPTIONS = "option";
    private static final String KEY_STEP = "step";
    private static final String KEY_PROMOVIEW = "promoView";
    public static final String LIST_DEALS_RECOMMENDED_PDP = "/deals - recommendation pdp";
    public static final String LIST_DEALS_SEARCH_TRENDING = "/deals - search by trending";
    public static final String LIST_SUGGESTED_DEALS = "/deals - popular suggestion";
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
    public static final String DEALS_HOME_PAGE = "deals-homepage";
    private static final String EVENT_ACTION_BRANDS_IMPRESSION = "impression brand search result";
    public static final String EVENT_ACTION_CATEGORY_DEALS_IMPRESSION = "impression deals product on category page";


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
    public static String EVENT_IMPRESSION_RECOMMENDED_DEALS = "impression on deals recommendation";
    public static String EVENT_CLICK_PROMO_BANNER = "click promo banner";
    public static String EVENT_IMPRESSION_TRENDING_DEALS = "impression trending deals";
    public static String EVENT_IMPRESSION_CURATED_DEALS = "impression curated deals";
    public static String EVENT_IMPRESSION_PROMO = "impression promo banner";
    public static String EVENT_CLICK_TRENDING_DEALS = "click trending deals";
    public static String EVENT_CLICK_CURATED_DEALS = "click curated deals";
    public static String EVENT_IMPRESSION_POPULAR_BRAND_CATEGORY = "impression brand on category page";
    public static String EVENT_IMPRESSION_POPULAR_BRAND_HOME = "impression brand populer";
    public static String EVENT_IMPRESSION_POPULAR_BRAND_ALL = "impression brand";
    public static String EVENT_IMPRESSION_PRODUCT_BRAND = "impression product brand";
    public static String EVENT_CLICK_SEE_ALL_BRANDS = "click lihat semua brand populer";
    public static String EVENT_CLICK_SEE_ALL_TRENDING_DEALS = "click lihat semua trending deals";
    public static String EVENT_CLICK_SEE_ALL_BRANDS_CATEGORY = "click lihat semua brand on category page";
    public static String EVENT_IMPRESSION_SEARCH_RESULT = "impression deals search result";
    public static String EVENT_CLICK_SEARCH_RESULT = "click search result";
    public static String EVENT_CLICK_SEARCH_TRENDING = "click search trending deals click";
    public static String EVENT_IMPRESSION_SEARCH_TRENDING = "impression deals popular suggestion";
    public static String EVENT_VIEW_PRODUCT_BRAND_DETAIL = "view product - brand detail";
    public static String EVENT_CLICK_PRODUCT_BRAND_DETAIL = "click product - brand detail";
    public static String EVENT_VIEW_RECOMMENDED_PDT_DETAIL = "impression recommended product - product detail";
    public static String EVENT_CLICK_RECOMMENDED_PDT_DETAIL = "click recommended product - product detail";
    public static String EVENT_VIEW_PRODUCT_CATEGORY_DETAIL = "view product - category detail";
    public static String EVENT_CLICK_PRODUCT_CATEGORY_DETAIL = "click product - category detail";
    public static String EVENT_VIEW_PRODUCT_DETAILS = "view product detail";
    public static String EVENT_CHECKOUT = "checkout";
    public static String ACTION_CHECKOUT = "view checkout";
    public static String EVENT_CLICK_PROCEED_TO_PAYMENT = "click proceed to payment";
    public static String EVENT_VIEW_SEARCH_BRAND_RESULT = "view search brand result";
    public static String EVENT_CLICK_DEALS = "clickDeals";
    public static String EVENT_CLICK_SEARCH_DEALS = "click search";
    public static String EVENT_CLICK_BRAND_TAB = "click brand tab";
    public static String EVENT_CLICK_BRAND_SEARCH_RESULT = "click brand search result";
    public static String EVENT_CLICK_BRAND_CATEGORY_PAGE = "click brand on category page";
    public static String EVENT_CLICK_BRAND_HOME_PAGE = "click brand populer";
    public static String EVENT_CLICK_BRAND = "click brand";
    public static String EVENT_CLICK_CATEGORY = "click category icon";
    public static String EVENT_CLICK_POPULAR_SGGESTION = "click deals populer suggestion";
    public static String EVENT_CLICK_POPULAR_SEARCH_RESULT = "click product search result";
    public static String EVENT_CLICK_PRODUCT_BRAND = "click product brand";
    public static String EVENT_CLICK_RECOMMENDED_DEALS = "click on deals recommendation";
    public static String EVENT_CLICK_CATEGORY_DEALS = "click deals products on category page";




    @Inject
    public DealsAnalytics() {
    }

    public void sendEventDealsDigitalClick(String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_DEALS_CLICK, DIGITAL_DEALS, action, label == null ? "" : label.toLowerCase()));

    }

    public void sendEventDealsDigitalView(String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_DEALS_VIEW, DIGITAL_DEALS, action, label == null ? "" : label.toLowerCase()));

    }

    public void sendEventEcommerce(String event, String action, String label,
                                   HashMap<String, Object> ecommerce) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("event", event);
        map.put("eventCategory", DIGITAL_DEALS);
        map.put("eventAction", action);
        map.put("eventLabel", label == null ? "" : label.toLowerCase());
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);
    }

    public void sendDealImpressionEvent(boolean isHeaderAdded, boolean isBrandHeaderAdded, boolean topDealsLayout, ProductItem productItem, String categoryName, int pageType, int position, String searchText, boolean isFromSearchFResult) {
        try {
            String event = null, action = null, label = null;
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> promoView = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            event = DealsAnalytics.EVENT_PROMO_VIEW;
            if (isHeaderAdded || isBrandHeaderAdded)
                position -= 1;
            promotions.put(POSITION, position);
            promotions.put(ID, position);
            promotions.put(NAME, DEALS_HOME_PAGE);
            promotions.put(CREATIVE, productItem.getBrand().getTitle());
            promotions.put(CATEGORY, productItem.getDisplayName());
            promotions.put(CATEGORY_ID, productItem.getId());
            if (!TextUtils.isEmpty(searchText)) {
                label = String.format("%s - %s - %d - %s", productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position
                        , searchText);
            } else {
                label = String.format("%s - %s - %d", productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position);
            }
            if (pageType == DealsCategoryAdapter.HOME_PAGE) {
                promotions.put(LIST, LIST_DEALS_TRENDING);
                action = DealsAnalytics.EVENT_IMPRESSION_TRENDING_DEALS;

            } else if (pageType == DealsCategoryAdapter.SEARCH_PAGE) {

                if (!isFromSearchFResult)
                    action = DealsAnalytics.EVENT_IMPRESSION_SEARCH_TRENDING;
                else
                    action = DealsAnalytics.EVENT_IMPRESSION_SEARCH_RESULT;

            } else if (pageType == DealsCategoryAdapter.CATEGORY_PAGE) {
                promotions.put(LIST, "/deals - " + categoryName);
                action = DealsAnalytics.EVENT_VIEW_PRODUCT_CATEGORY_DETAIL;
                label = String.format("%s - %s - %s - %s", categoryName, productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position);

            } else if (pageType == DealsCategoryAdapter.BRAND_PAGE) {
                promotions.put(LIST, "/deals - " + productItem.getBrand().getTitle());
                action = DealsAnalytics.EVENT_VIEW_PRODUCT_BRAND_DETAIL;
            } else if (pageType == DealsCategoryAdapter.DETAIL_PAGE) {
                promotions.put(LIST, LIST_DEALS_RECOMMENDED_PDP);
                action = DealsAnalytics.EVENT_VIEW_RECOMMENDED_PDT_DETAIL;
                event = DealsAnalytics.EVENT_VIEW_PRODUCT;
            }
            promoView.put(KEY_PROMOTIONS, Collections.singletonList(promotions));
            ecommerce.put(KEY_PROMOVIEW, promoView);
            sendEventEcommerce(event
                    , action
                    , label == null ? "" : label.toLowerCase(), ecommerce);
        } catch (Exception e) {
            Log.d("Naveen", "Error in" + e.getStackTrace());
        }
    }

    public void sendBrandsSuggestionImpressionEvent(Brand brandItem, int position) {
        try {
            String event = null, label = null, action = null;
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> promoView = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            event = DealsAnalytics.EVENT_PROMO_VIEW;
            promotions.put(POSITION, position);
            promotions.put(ID, position);
            promotions.put(NAME, DEALS_HOME_PAGE);
            promotions.put(CREATIVE, brandItem.getTitle());
            label = String.format("%s - %s", brandItem.getTitle()
                    , position);
            action = DealsAnalytics.EVENT_ACTION_BRANDS_IMPRESSION;
            promoView.put(KEY_PROMOTIONS, Collections.singletonList(promotions));
            ecommerce.put(KEY_PROMOVIEW, promoView);
            sendEventEcommerce(event
                    , action
                    , label == null ? "" : label.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendDealClickEvent(ProductItem productItem, int position, String action) {
        try {
            String label;

            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> actionfield = new HashMap<>();
            HashMap<String, Object> click = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            productMap.put(ID, productItem.getId());
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(PRICE, productItem.getSalesPrice());
            productMap.put(POSITION, position);
            productMap.put(CATEGORY, productItem.getDisplayName());

            String list = String.format("%s - %s - %s - %s", "/"+ DEALS, productItem.getBrand().getTitle(), position, productItem.getDisplayName());
            actionfield.put(LIST, list);

            click.put(HASH_ACTION_FIELD, actionfield);
            click.put(KEY_PRODUCTS, Collections.singletonList(productMap));

            ecommerce.put(HASH_CLICK, click);

            label = String.format("%s - %s", productItem.getDisplayName()
                    , position);
            sendEventEcommerce(DealsAnalytics.EVENT_PRODUCT_CLICK
                    , action
                    , label.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendTrendingDealClickEvent(ProductItem productItem, String action, int position) {
        try {
            String label;

            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> promoClick = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            productMap.put(ID, productItem.getBrand().getId());
            productMap.put(NAME, LIST_SUGGESTED_DEALS);
            productMap.put(POSITION, position);
            productMap.put(CREATIVE, productItem.getBrand().getTitle());
            productMap.put(CATEGORY, productItem.getDisplayName());
            productMap.put(CATEGORY_ID, productItem.getId());

            promoClick.put(KEY_PROMOTIONS, Collections.singletonList(productMap));


            ecommerce.put(EVENT_PROMO_CLICK, promoClick);

            label = String.format("%s - %s - %s", productItem.getBrand().getTitle()
                    , productItem.getDisplayName()
                    , position);
            sendEventEcommerce(DealsAnalytics.EVENT_PROMO_CLICK
                    , action
                    , label.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceClickEvent(ProductItem productItem, int position, String action, String listt, String searchText) {

        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, position);
            productMap.put(NAME, listt);
            productMap.put(POSITION, position);
            productMap.put(CREATIVE, productItem.getBrand().getTitle());
            productMap.put(CATEGORY, productItem.getDisplayName());
            productMap.put(CATEGORY_ID, productItem.getId());
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            promotions.put(KEY_PROMOTIONS, Collections.singletonList(productMap));
            ecommerce.put(EVENT_PROMO_CLICK, promotions);
            String label = null;
            if (!TextUtils.isEmpty(searchText)) {
                label = String.format("%s - %s - %s - %s", productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position, searchText);
            } else {
                label = String.format("%s - %s - %s", productItem.getBrand().getTitle()
                        , productItem.getDisplayName()
                        , position);
            }
            sendEventEcommerce(DealsAnalytics.EVENT_PROMO_CLICK
                    , action
                    , label.toLowerCase(), ecommerce);

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
            HashMap<String, Object> checkout = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            checkout.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            HashMap<String, Object> actionField = new HashMap<>();
            actionField.put(KEY_STEP, 2);
            actionField.put(KEY_OPTIONS, "click payment option button");
            checkout.put(HASH_ACTION_FIELD, actionField);
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
            HashMap<String, Object> checkout = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String,Object> actionField = new HashMap<>();
            actionField.put(KEY_STEP, 1);
            actionField.put(KEY_OPTIONS, "cart page loaded");
            checkout.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            checkout.put(HASH_ACTION_FIELD, actionField);
            ecommerce.put(HASH_CHECKOUT, checkout);
            sendEventEcommerce(DealsAnalytics.EVENT_CHECKOUT
                    , DealsAnalytics.ACTION_CHECKOUT
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
            detail.put(KEY_PROMOTIONS, productMaps);
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
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            promotions.put(KEY_PROMOTIONS, Collections.singletonList(bannerMap));

            if (event.equalsIgnoreCase(EVENT_PROMO_CLICK)) {
                ecommerce.put(EVENT_CLICK_PROMO, promotions);
            } else {
                ecommerce.put(KEY_PROMOVIEW, promotions);
            }
            sendEventEcommerce(event, action,
                    String.format("%s - %s", creative
                            , position).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }


    public void sendBrandImpressionEvent(int id, int position, String creative, String event, String action, String name) {
        try {
            HashMap<String, Object> bannerMap = new HashMap<>();
            bannerMap.put(ID, id);
            bannerMap.put(NAME, name);
            bannerMap.put(POSITION, position);
            bannerMap.put(CREATIVE, creative);
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            promotions.put(KEY_PROMOTIONS, Collections.singletonList(bannerMap));

            if (event.equalsIgnoreCase(EVENT_PROMO_CLICK)) {
                ecommerce.put(EVENT_CLICK_PROMO, promotions);
            } else {
                ecommerce.put(KEY_PROMOVIEW, promotions);
            }
            sendEventEcommerce(event, action,
                    String.format("%s - %s", creative
                            , position).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }

    public void sendPromoClickEvent(ProductItem productItem, int position, String creative, String event, String action, String name) {
        try {
            HashMap<String, Object> bannerMap = new HashMap<>();
            bannerMap.put(ID, productItem.getId());
            bannerMap.put(NAME, name);
            bannerMap.put(POSITION, position);
            bannerMap.put(CREATIVE, creative);
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            promotions.put(KEY_PROMOTIONS, Collections.singletonList(bannerMap));

            ecommerce.put(EVENT_PROMO_CLICK, promotions);
            sendEventEcommerce(event, action,
                    String.format("%s - %s", creative
                            , position).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }


    public void sendSearchClickedEvent(String searchText) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_CLICK_DEALS, DIGITAL_DEALS, EVENT_CLICK_SEARCH_DEALS, searchText);
    }

    public void sendAllBrandsClickEvent(int pageType) {
        String event = null, action = null;
        if (pageType == SEE_ALL_BRANDS_HOME) {
            event = EVENT_CLICK_DEALS;
            action = EVENT_CLICK_SEE_ALL_BRANDS;
        } else {
            event = EVENT_PROMO_VIEW;
            action = EVENT_CLICK_SEE_ALL_BRANDS_CATEGORY;
        }
        TrackApp.getInstance().getGTM().sendGeneralEvent(event, DIGITAL_DEALS, action, "");
    }

    public void sendSelectedHeaderEvent(String selectedTab) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_CLICK_DEALS, DIGITAL_DEALS, EVENT_CLICK_BRAND_TAB, selectedTab);
    }

    public void sendCategoryClickEventHome(String name, int position) {
        String label = String.format("%s - %s", name, position);
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_CLICK_DEALS, DIGITAL_DEALS, EVENT_CLICK_CATEGORY, label);
    }

    public void sendBrandsSuggestionClickEvent(Brand brand, int position, String searchtext, int pageType) {
        try {
            String action = null, label = null;
            if (pageType == CLICK_BRANDS_SEARCH) {
                action = EVENT_CLICK_BRAND_SEARCH_RESULT;
            } else if (pageType == CLICK_BRANDS_CATEGORY) {
                action = EVENT_CLICK_BRAND_CATEGORY_PAGE;
            } else if (pageType == CLICK_BRANDS_HOME){
                action = EVENT_CLICK_BRAND_HOME_PAGE;
            } else {
                action = EVENT_CLICK_BRAND;
            }
            HashMap<String, Object> bannerMap = new HashMap<>();
            bannerMap.put(ID, brand.getId());
            bannerMap.put(NAME, LIST_SUGGESTED_DEALS);
            bannerMap.put(POSITION, position);
            bannerMap.put(CREATIVE, brand.getTitle());
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            promotions.put(KEY_PROMOTIONS, Collections.singletonList(bannerMap));
            ecommerce.put(EVENT_PROMO_CLICK, promotions);
            if (!TextUtils.isEmpty(searchtext)) {
                label = String.format("%s - %s - %s", brand.getTitle()
                        , position, searchtext).toLowerCase();
            } else {
                label = String.format("%s - %s", brand.getTitle()
                        , position).toLowerCase();
            }
            sendEventEcommerce(EVENT_PROMO_CLICK, action,
                    label, ecommerce);

        } catch (Exception e) {

        }
    }

    public void sendProductBrandDealImpression(String event, String action, ProductItem item, int index) {
        String list = String.format("%s - %s - %s - %s", DEALS, item.getBrand().getTitle(), index, item.getDisplayName());
        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put(NAME, item.getDisplayName());
        productmap.put(ID, item.getId());
        productmap.put(PRICE, item.getSalesPrice());
        productmap.put(BRAND, item.getBrand().getTitle());
        productmap.put(CATEGORY, item.getDisplayName());
        productmap.put(LIST, list);
        productmap.put(POSITION, index);

        HashMap<String, Object> impressions = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();

        impressions.put(KEY_PRODUCTS, Collections.singletonList(productmap));
        ecommerce.put(KEY_IMPRESSIONS, impressions);
        sendEventEcommerce(event, action,
                String.format("%s - %s - %s", item.getBrand().getTitle(), item.getDisplayName(), index).toLowerCase(), ecommerce);

    }

    public void sendTrendingDealImpression(String event, String action, ProductItem item, int index) {
        HashMap<String, Object> productmap = new HashMap<>();
        productmap.put(NAME, DEALS_HOME_PAGE);
        productmap.put(ID, item.getId());
        productmap.put(CREATIVE, item.getBrand().getTitle());
        productmap.put(CATEGORY, item.getDisplayName());
        productmap.put(POSITION, index);

        HashMap<String, Object> promotions = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();

        promotions.put(KEY_PROMOTIONS, Collections.singletonList(productmap));
        ecommerce.put(KEY_PROMOVIEW, promotions);
        sendEventEcommerce(event, action,
                String.format("%s - %s - %s", item.getBrand().getTitle(), item.getDisplayName(), index).toLowerCase(), ecommerce);

    }

    public void sendSeeAllTrendingDeals() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_CLICK_DEALS, DIGITAL_DEALS, EVENT_CLICK_SEE_ALL_TRENDING_DEALS, "");
    }

    public void sendPromoImpressionEvent(ProductItem item, int index) {
        HashMap<String, Object> promo = new HashMap<>();
        HashMap<String, Object> promoViews = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();

        promo.put(ID, item.getId());
        promo.put(NAME, DEALS_HOME_PAGE);
        promo.put(CREATIVE, item.getBrand().getTitle());
        promo.put(POSITION, index);
        promo.put(CATEGORY, item.getDisplayName());
        promo.put(CATEGORY_ID, item.getId());

        promoViews.put(KEY_PROMOTIONS, Collections.singletonList(promo));
        ecommerce.put(KEY_PROMOVIEW, promoViews);
        sendEventEcommerce(EVENT_PROMO_VIEW, EVENT_IMPRESSION_PROMO_BANNER,
                String.format("%s - %s", item.getDisplayName(), index), ecommerce);
    }

    public void sendRecommendedDealImpressionEvent(ProductItem item, int index) {
        try {
            HashMap<String, Object> recommendedDeals = new HashMap<>();
            HashMap<String, Object> impressions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            recommendedDeals.put(ID, item.getId());
            recommendedDeals.put(NAME, item.getDisplayName());
            recommendedDeals.put(PRICE, item.getSalesPrice());
            recommendedDeals.put(BRAND, item.getBrand().getTitle());
            recommendedDeals.put(CATEGORY, item.getDisplayName());
            String list = String.format("%s - %s - %s - %s - %s", "/" + DEALS, item.getBrand().getTitle(), "recommendation", index, item.getDisplayName());
            recommendedDeals.put(LIST, list);
            recommendedDeals.put(POSITION, index);

            impressions.put(KEY_PRODUCTS, Collections.singletonList(recommendedDeals));
            ecommerce.put(KEY_IMPRESSIONS, impressions);

            sendEventEcommerce(EVENT_PRODUCT_VIEW, EVENT_IMPRESSION_RECOMMENDED_DEALS,
                    String.format("%s - %s - %s", item.getBrand().getTitle(), item.getDisplayName(), index).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendBuyNowClickEvent(DealsDetailsResponse dealDetail, String action) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String, Object> add = new HashMap<>();

            productMap.put(NAME, dealDetail.getDisplayName());
            productMap.put(ID, dealDetail.getId());
            productMap.put(PRICE, dealDetail.getSalesPrice());
            productMap.put(CATEGORY, dealDetail.getDisplayName());
            productMap.put(CATEGORY_ID, dealDetail.getCategoryId());
            productMap.put(QUANTITY, dealDetail.getMaxQty());

            add.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_ADD, add);
            sendEventEcommerce(EVENT_ADD_TO_CART, action,
                    String.format("%s - %s", dealDetail.getBrand().getTitle(), dealDetail.getDisplayName()).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendPromoCodeClickEvent(DealsDetailsResponse dealDetails) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_CLICK_DEALS, DIGITAL_DEALS, EVENT_CLICK_PROMO,
                String.format("%s - %s -%s", dealDetails.getBrand().getTitle(), dealDetails.getDisplayName(), "promo/non promo").toLowerCase());
    }

    public void sendScreenNameEvent(String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    public void sendCategoryDealsImpressionEvent(String event, String action, ProductItem item, int index) {
        HashMap<String, Object> promotions = new HashMap<>();
        HashMap<String, Object> promoView = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();

        promotions.put(ID, item.getId());
        promotions.put(NAME, DEALS_HOME_PAGE);
        promotions.put(CREATIVE, item.getBrand().getTitle());
        promotions.put(POSITION, index);
        promotions.put(CATEGORY, item.getDisplayName());
        promotions.put(CATEGORY_ID, item.getId());

        promoView.put(KEY_PROMOTIONS, Collections.singletonList(promotions));
        ecommerce.put(KEY_PROMOVIEW, promoView);

        sendEventEcommerce(event, action,
                String.format("%s - %s - %s", item.getBrand().getTitle(), item.getDisplayName(), index).toLowerCase(), ecommerce);
    }

    public void sendCategoryDealClickEvent(ProductItem item, int position, String action) {
        HashMap<String, Object> promotions = new HashMap<>();
        HashMap<String, Object> promoClick = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();

        promotions.put(ID, item.getId());
        promotions.put(NAME, LIST_SUGGESTED_DEALS);
        promotions.put(CREATIVE, item.getBrand().getTitle());
        promotions.put(POSITION, position);
        promotions.put(CATEGORY, item.getDisplayName());
        promotions.put(CATEGORY_ID, item.getId());

        promoClick.put(KEY_PROMOTIONS, Collections.singletonList(promotions));
        ecommerce.put(EVENT_PROMO_CLICK, promoClick);

        sendEventEcommerce(EVENT_PROMO_CLICK, action,
                String.format("%s - %s - %s", item.getBrand().getTitle(), item.getDisplayName(), position).toLowerCase(), ecommerce);
    }
}
