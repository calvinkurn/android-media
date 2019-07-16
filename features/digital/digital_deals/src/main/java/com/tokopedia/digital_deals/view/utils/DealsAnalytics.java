package com.tokopedia.digital_deals.view.utils;

import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.digital_deals.view.adapter.DealsCategoryAdapter;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.ProductItem;
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

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
    private static final String VARIANT = "variant";
    private static final String QUANTITY = "quantity";
    private static final String CART_ID = "cart_id";
    private static final String HASH_CHECKOUT = "checkout";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String HASH_DETAIL = "detail";
    private static final String LIST_DEALS_CATEGORY = "/deals - category";
    private static final String LIST_DEALS_BRAND = "/deals - brand";
    private static final String CREATIVE = "creative";
    private static final String CREATIVE_URL = "creative_url";
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

    public void sendEventEcommerceCurrentSite(String event, String action, String label,
                                              HashMap<String, Object> ecommerce) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("event", event);
        map.put("eventCategory", DIGITAL_DEALS);
        map.put("eventAction", action);
        map.put("eventLabel", label == null ? "" : label.toLowerCase());
        map.put("currentSite", "tokopediadigital");
        map.put("ecommerce", ecommerce);
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(map);
    }

    public void sendDealImpressionEvent(boolean isHeaderAdded, boolean isBrandHeaderAdded, boolean topDealsLayout, List<ProductItem> productItems, String categoryName, int pageType, int position, String searchText, boolean isFromSearchFResult) {

        try {
            String event = null, action = null, label = null;
            List<HashMap<String, Object>> promotions = new ArrayList<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            event = DealsAnalytics.EVENT_PRODUCT_VIEW;

            if (!TextUtils.isEmpty(searchText)) {
                label = String.format("%s - %s - %s", categoryName
                        , String.valueOf(position)
                        , searchText);
            } else {
                label = String.format("%s - %s", categoryName
                        , String.valueOf(position));
            }
            for (ProductItem productItem : productItems) {
                if (isHeaderAdded || isBrandHeaderAdded)
                    position -= 1;

                HashMap<String, Object> promotionsItem = new HashMap<>();
                promotionsItem.put(NAME, productItem.getDisplayName());
                promotionsItem.put(ID, String.valueOf(position));
                promotionsItem.put(PRICE, productItem.getSalesPrice());
                promotionsItem.put(BRAND, productItem.getBrand().getTitle());
                promotionsItem.put(CATEGORY, DEALS);
                promotionsItem.put(VARIANT, "none");
                promotionsItem.put(POSITION, String.valueOf(position));
                if (pageType == DealsCategoryAdapter.HOME_PAGE) {
                    promotionsItem.put(LIST, LIST_DEALS_TRENDING);
                } else if (pageType == DealsCategoryAdapter.CATEGORY_PAGE) {
                    promotionsItem.put(LIST, "/deals - " + categoryName);
                } else if (pageType == DealsCategoryAdapter.BRAND_PAGE) {
                    promotionsItem.put(LIST, "/deals - " + categoryName);
                } else if (pageType == DealsCategoryAdapter.DETAIL_PAGE) {
                    promotionsItem.put(LIST, LIST_DEALS_RECOMMENDED_PDP);
                } else if (pageType == DealsCategoryAdapter.SEARCH_PAGE) {
                    promotionsItem.put(LIST, String.format("%s - %s - %s - %s", DEALS, BRAND, String.valueOf(position), productItem.getDisplayName()));
                }
                promotions.add(promotionsItem);

            }


            if (pageType == DealsCategoryAdapter.HOME_PAGE) {
                action = DealsAnalytics.EVENT_IMPRESSION_TRENDING_DEALS;

            } else if (pageType == DealsCategoryAdapter.SEARCH_PAGE) {

                if (!isFromSearchFResult)
                    action = DealsAnalytics.EVENT_IMPRESSION_SEARCH_TRENDING;
                else
                    action = DealsAnalytics.EVENT_IMPRESSION_SEARCH_RESULT;

            } else if (pageType == DealsCategoryAdapter.CATEGORY_PAGE) {

                action = DealsAnalytics.EVENT_VIEW_PRODUCT_CATEGORY_DETAIL;
                label = String.format("%s - %s", categoryName
                        , String.valueOf(position));

            } else if (pageType == DealsCategoryAdapter.BRAND_PAGE) {
                action = DealsAnalytics.EVENT_VIEW_PRODUCT_BRAND_DETAIL;
            } else if (pageType == DealsCategoryAdapter.DETAIL_PAGE) {
                action = DealsAnalytics.EVENT_VIEW_RECOMMENDED_PDT_DETAIL;
                event = DealsAnalytics.EVENT_VIEW_PRODUCT;
            }
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(KEY_IMPRESSIONS, promotions);
            sendEventEcommerce(event
                    , action
                    , label == null ? "" : label.toLowerCase(), ecommerce);
        } catch (Exception e) {
            Log.d("Naveen", "Error in" + e.getStackTrace());
        }
    }

    public void sendBrandsSuggestionImpressionEvent(List<Brand> brandItems, String searchText) {
        try {
            String event = null, label = null, action = null;
            List<HashMap<String, Object>> products = new ArrayList<>();
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> promoView = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            event = DealsAnalytics.EVENT_PROMO_VIEW;
            for (int index = 0; index < brandItems.size(); index++) {
                promotions.put(POSITION, String.valueOf(index));
                promotions.put(ID, String.valueOf(brandItems.get(index).getId()));
                promotions.put(NAME, brandItems.get(index).getTitle());
                promotions.put(CREATIVE, brandItems.get(index).getTitle());
                promotions.put(CATEGORY, DEALS);
                promotions.put(CREATIVE_URL, "none");
                products.add(promotions);
            }

            label = searchText;
            action = DealsAnalytics.EVENT_ACTION_BRANDS_IMPRESSION;
            promoView.put(KEY_PROMOTIONS, products);
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

            productMap.put(ID, String.valueOf(productItem.getId()));
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(PRICE, productItem.getSalesPrice());
            productMap.put(BRAND, productItem.getBrand().getTitle());
            productMap.put(POSITION, String.valueOf(position));
            productMap.put(VARIANT, "none");
            productMap.put(CATEGORY, DEALS);

            String list = String.format("%s - %s - %s - %s", "/" + DEALS, productItem.getBrand().getTitle(), position, productItem.getDisplayName());
            actionfield.put(LIST, list);

            click.put(HASH_ACTION_FIELD, actionfield);
            click.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_CLICK, click);

            label = String.format("%s - %s", productItem.getDisplayName()
                    , String.valueOf(position));
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
            HashMap<String, Object> actionField = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            productMap.put(ID, String.valueOf(productItem.getId()));
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(POSITION, String.valueOf(position));
            productMap.put(PRICE, productItem.getSalesPrice());
            productMap.put(BRAND, productItem.getBrand().getTitle());
            productMap.put(CATEGORY, DEALS);
            productMap.put(VARIANT, "none");

            actionField.put(LIST, String.format("%s - %s - %s - %s", DEALS, BRAND, String.valueOf(position), productItem.getDisplayName()));
            promoClick.put(HASH_ACTION_FIELD, actionField);
            promoClick.put(KEY_PRODUCTS, Collections.singletonList(productMap));

            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_CLICK, promoClick);

            label = String.format("%s - %s", productItem.getBrand().getTitle()
                    , String.valueOf(position));
            sendEventEcommerce(DealsAnalytics.EVENT_PRODUCT_CLICK
                    , action
                    , label.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceClickEvent(ProductItem productItem, int position, String action, String listt, String searchText) {

        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, String.valueOf(productItem.getId()));
            productMap.put(NAME, productItem.getDisplayName());
            productMap.put(PRICE, productItem.getSalesPrice());
            productMap.put(BRAND, productItem.getBrand().getTitle());
            productMap.put(VARIANT, "none");
            productMap.put(POSITION, String.valueOf(position));
            productMap.put(CATEGORY, DEALS);
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            promotions.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            promotions.put(HASH_ACTION_FIELD, String.format("%s - %s - %s - %s", DEALS, BRAND, String.valueOf(position), productItem.getDisplayName()));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_CLICK, promotions);
            String label = null;
            if (!TextUtils.isEmpty(searchText)) {
                label = String.format("%s - %s - %s", productItem.getBrand().getTitle(), String.valueOf(position), searchText);
            } else {
                label = String.format("%s - %s", productItem.getBrand().getTitle(), String.valueOf(position));
            }
            sendEventEcommerce(DealsAnalytics.EVENT_PRODUCT_CLICK
                    , action
                    , label.toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }

    public void sendEcommercePayment(int id, int quantity, int salesPrice, String displayName, String brandName, boolean promoApplied) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, String.valueOf(id));
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
            sendEventEcommerceCurrentSite(DealsAnalytics.EVENT_CHECKOUT
                    , DealsAnalytics.EVENT_CLICK_PROCEED_TO_PAYMENT
                    , String.format("%s - %s", brandName
                            , promo).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceQuantity(int id, int quantity, int salesPrice, String displayName, String brandName, int categoryID) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, String.valueOf(id));
            productMap.put(NAME, displayName);
            productMap.put(PRICE, (salesPrice * quantity));
            productMap.put(CATEGORY, DEALS);
            productMap.put(CATEGORY_ID, String.valueOf(categoryID));
            productMap.put(QUANTITY, quantity);
            HashMap<String, Object> checkout = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String, Object> actionField = new HashMap<>();
            actionField.put(KEY_STEP, 1);
            actionField.put(KEY_OPTIONS, "cart page loaded");
            checkout.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            checkout.put(HASH_ACTION_FIELD, actionField);
            ecommerce.put(HASH_CHECKOUT, checkout);
            sendEventEcommerce(DealsAnalytics.EVENT_CHECKOUT
                    , DealsAnalytics.ACTION_CHECKOUT
                    , brandName.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceDealDetail(int id, int salesPrice, String displayName, String brandName) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put(ID, id);
            productMap.put(NAME, displayName);
            productMap.put(BRAND, brandName);
            productMap.put(CATEGORY, DEALS);
            productMap.put(VARIANT, "none");
            productMap.put(PRICE, salesPrice);
            productMap.put(LIST, "");
            HashMap<String, Object> list = new HashMap<>();
            HashMap<String, Object> detail = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            list.put(LIST, "");
            detail.put(HASH_ACTION_FIELD, list);
            List<HashMap<String, Object>> productMaps = new ArrayList<>();
            productMaps.add(productMap);
            detail.put(KEY_PRODUCTS, productMaps);
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_DETAIL, detail);
            sendEventEcommerce(DealsAnalytics.EVENT_VIEW_PRODUCT
                    , DealsAnalytics.EVENT_VIEW_PRODUCT_DETAILS
                    , brandName.toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendEcommerceBrand(List<Brand> brands, int position, String creative, String event, String action, String name) {
        try {

            List<HashMap<String, Object>> banners = new ArrayList<>();

            HashMap<String, Object> bannerMap = new HashMap<>();
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            for (Brand brand : brands) {
                bannerMap.put(ID, String.valueOf(brand.getId()));
                bannerMap.put(NAME, name);
                bannerMap.put(POSITION, String.valueOf(position));
                bannerMap.put(CREATIVE, brand.getTitle());
                bannerMap.put(CATEGORY, DEALS);
                banners.add(bannerMap);
            }

            promotions.put(KEY_PROMOTIONS, banners);
            ecommerce.put(KEY_PROMOVIEW, promotions);
            sendEventEcommerce(event, action,
                    String.format("%s - %s", creative
                            , position).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }


    public void sendBrandImpressionEvent(List<Brand> brands, int position, String creative, String event, String action, String name) {
        try {

            List<HashMap<String, Object>> banners = new ArrayList<>();

            HashMap<String, Object> bannerMap = new HashMap<>();
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            for (Brand brand : brands) {
                bannerMap.put(ID, String.valueOf(brand.getId()));
                bannerMap.put(NAME, name);
                bannerMap.put(POSITION, String.valueOf(position));
                bannerMap.put(CATEGORY, DEALS);
                bannerMap.put(CREATIVE, creative);
                banners.add(bannerMap);
            }

            promotions.put(KEY_PROMOTIONS, banners);
            ecommerce.put(KEY_PROMOVIEW, promotions);

            sendEventEcommerce(event, action,
                    String.format("%s - %s", creative
                            , String.valueOf(position)).toLowerCase(), ecommerce);

        } catch (Exception e) {

        }
    }

    public void sendPromoClickEvent(ProductItem productItem, int position, String creative, String event, String action, String name) {
        try {
            HashMap<String, Object> bannerMap = new HashMap<>();
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            bannerMap.put(ID, String.valueOf(productItem.getId()));
            bannerMap.put(NAME, name);
            bannerMap.put(POSITION, String.valueOf(position));
            bannerMap.put(CATEGORY, DEALS);
            bannerMap.put(CREATIVE, creative);

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
            } else if (pageType == CLICK_BRANDS_HOME) {
                action = EVENT_CLICK_BRAND_HOME_PAGE;
            } else {
                action = EVENT_CLICK_BRAND;
            }
            HashMap<String, Object> bannerMap = new HashMap<>();
            bannerMap.put(ID, String.valueOf(brand.getId()));
            bannerMap.put(NAME, brand.getTitle());
            bannerMap.put(POSITION, String.valueOf(position));
            bannerMap.put(CATEGORY, DEALS);
            bannerMap.put(CREATIVE, brand.getTitle());
            bannerMap.put(CREATIVE_URL, brand.getSeoUrl());
            HashMap<String, Object> promotions = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            promotions.put(KEY_PROMOTIONS, Collections.singletonList(bannerMap));
            ecommerce.put(EVENT_PROMO_CLICK, promotions);
            if (!TextUtils.isEmpty(searchtext)) {
                label = String.format("%s - %s - %s", brand.getTitle()
                        , String.valueOf(position), searchtext).toLowerCase();
            } else {
                label = String.format("%s - %s", brand.getTitle()
                        , String.valueOf(position)).toLowerCase();
            }
            sendEventEcommerce(EVENT_PROMO_CLICK, action,
                    label, ecommerce);

        } catch (Exception e) {

        }
    }

    public void sendProductBrandDealImpression(String event, String action, List<ProductItem> items, int index, String categoryName) {
        String list = null;
        List<HashMap<String, Object>> products = new ArrayList<>();

        HashMap<String, Object> productmap = new HashMap<>();
        for (ProductItem item : items) {
            list = String.format("%s - %s - %s - %s", DEALS, BRAND, String.valueOf(index), item.getDisplayName());
            productmap.put(NAME, item.getDisplayName());
            productmap.put(ID, String.valueOf(item.getId()));
            productmap.put(PRICE, item.getSalesPrice());
            productmap.put(BRAND, item.getBrand().getTitle());
            productmap.put(CATEGORY, DEALS);
            productmap.put(LIST, list);
            productmap.put(POSITION, String.valueOf(index));
            products.add(productmap);
        }

        HashMap<String, Object> ecommerce = new HashMap<>();

        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_IMPRESSIONS, productmap);
        sendEventEcommerce(event, action,
                String.format("%s - %s", categoryName, String.valueOf(index)).toLowerCase(), ecommerce);

    }

    public void sendTrendingDealImpression(String event, String action, List<ProductItem> items, int index, String categoryName) {

        List<HashMap<String, Object>> products = new ArrayList<>();
        HashMap<String, Object> ecommerce = new HashMap<>();
        HashMap<String, Object> productmap = new HashMap<>();

        for (ProductItem item : items) {
            productmap.put(NAME, DEALS_HOME_PAGE);
            productmap.put(ID, String.valueOf(item.getId()));
            productmap.put(CREATIVE, item.getBrand().getTitle());
            productmap.put(CATEGORY, item.getDisplayName());
            productmap.put(POSITION, String.valueOf(index));
            productmap.put(PRICE, item.getSalesPrice());
            products.add(productmap);
        }
        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_IMPRESSIONS, products);
        sendEventEcommerce(event, action,
                String.format("%s - %s", categoryName, String.valueOf(index)).toLowerCase(), ecommerce);

    }

    public void sendSeeAllTrendingDeals() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(EVENT_CLICK_DEALS, DIGITAL_DEALS, EVENT_CLICK_SEE_ALL_TRENDING_DEALS, "");
    }

    public void sendPromoImpressionEvent(List<ProductItem> items, int index) {
        HashMap<String, Object> promo = new HashMap<>();
        HashMap<String, Object> promoViews = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();

        List<HashMap<String, Object>> products = new ArrayList<>();

        for (ProductItem item : items) {
            promo.put(ID, String.valueOf(item.getId()));
            promo.put(NAME, DEALS_HOME_PAGE);
            promo.put(CREATIVE, item.getBrand().getTitle());
            promo.put(POSITION, String.valueOf(index));
            promo.put(CATEGORY, DEALS);
            promo.put(PROMO_ID, String.valueOf(item.getId()));
            products.add(promo);
        }

        promoViews.put(KEY_PROMOTIONS, products);
        ecommerce.put(KEY_PROMOVIEW, promoViews);
        sendEventEcommerce(EVENT_PROMO_VIEW, EVENT_IMPRESSION_PROMO_BANNER,
                String.format("%s - %s", "Product", String.valueOf(index)), ecommerce);
    }

    public void sendRecommendedDealImpressionEvent(List<ProductItem> items, int index, String categoryName) {
        try {
            HashMap<String, Object> recommendedDeals = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();

            List<HashMap<String, Object>> products = new ArrayList<>();

            for (ProductItem item : items) {
                recommendedDeals.put(ID, String.valueOf(item.getId()));
                recommendedDeals.put(NAME, item.getDisplayName());
                recommendedDeals.put(PRICE, item.getSalesPrice());
                recommendedDeals.put(BRAND, item.getBrand().getTitle());
                recommendedDeals.put(CATEGORY, DEALS);
                recommendedDeals.put(VARIANT, "none");
                String list = String.format("%s - %s - %s - %s - %s", "/" + DEALS, item.getBrand().getTitle(), "recommendation", index, item.getDisplayName());
                recommendedDeals.put(LIST, list);
                recommendedDeals.put(POSITION, String.valueOf(index));
                products.add(recommendedDeals);
            }

            ecommerce.put(KEY_IMPRESSIONS, products);
            ecommerce.put(CURRENCY_CODE, IDR);

            sendEventEcommerce(EVENT_PRODUCT_VIEW, EVENT_IMPRESSION_RECOMMENDED_DEALS,
                    String.format("%s - %s", categoryName, String.valueOf(index)).toLowerCase(), ecommerce);
        } catch (Exception e) {

        }
    }

    public void sendBuyNowClickEvent(DealsDetailsResponse dealDetail, String action) {
        try {
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String, Object> add = new HashMap<>();

            productMap.put(NAME, dealDetail.getDisplayName());
            productMap.put(ID, String.valueOf(dealDetail.getId()));
            productMap.put(PRICE, dealDetail.getSalesPrice());
            productMap.put(CATEGORY, DEALS);
            productMap.put(QUANTITY, dealDetail.getMaxQty());
            productMap.put(VARIANT, "none");
            add.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_ADD, add);
            sendEventEcommerce(EVENT_ADD_TO_CART, action, dealDetail.getBrand().getTitle().toLowerCase(), ecommerce);
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

    public void sendCategoryDealsImpressionEvent(String event, String action, List<ProductItem> items, int index, String categoryName) {
        HashMap<String, Object> promotions = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();
        List<HashMap<String, Object>> promotionsList = new ArrayList<>();

        for (ProductItem item : items) {
            promotions.put(ID, String.valueOf(item.getId()));
            promotions.put(NAME, item.getDisplayName());
            promotions.put(PRICE, item.getSalesPrice());
            promotions.put(BRAND, item.getBrand().getTitle());
            promotions.put(CREATIVE, item.getBrand().getTitle());
            promotions.put(POSITION, String.valueOf(index));
            promotions.put(CATEGORY, DEALS);
            promotions.put(VARIANT, "none");
            promotions.put(LIST, String.format("%s - %s - %s - %s", DEALS, BRAND, String.valueOf(index), item.getDisplayName()));
            promotionsList.add(promotions);
        }

        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(KEY_IMPRESSIONS, promotionsList);

        sendEventEcommerce(event, action,
                String.format("%s - %s", categoryName, String.valueOf(index)).toLowerCase(), ecommerce);
    }

    public void sendCategoryDealClickEvent(ProductItem item, int position, String action) {
        HashMap<String, Object> promotions = new HashMap<>();
        HashMap<String, Object> promoClick = new HashMap<>();
        HashMap<String, Object> ecommerce = new HashMap<>();
        HashMap<String, Object> actionField = new HashMap<>();

        promotions.put(ID, String.valueOf(item.getId()));
        promotions.put(NAME, item.getDisplayName());
        promotions.put(CREATIVE, item.getBrand().getTitle());
        promotions.put(POSITION, String.valueOf(position));
        promotions.put(BRAND, item.getBrand());
        promotions.put(PRICE, item.getSalesPrice());
        promotions.put(CATEGORY, DEALS);

        actionField.put(LIST, String.format("%s - %s - %s - %s", DEALS, BRAND, String.valueOf(position), item.getDisplayName()));
        promoClick.put(HASH_ACTION_FIELD, actionField);
        promoClick.put(KEY_PRODUCTS, Collections.singletonList(promotions));
        ecommerce.put(CURRENCY_CODE, IDR);
        ecommerce.put(HASH_CLICK, promoClick);

        sendEventEcommerce(EVENT_PRODUCT_CLICK, action,
                String.format("%s - %s", item.getBrand().getTitle(), String.valueOf(position)).toLowerCase(), ecommerce);
    }
}
