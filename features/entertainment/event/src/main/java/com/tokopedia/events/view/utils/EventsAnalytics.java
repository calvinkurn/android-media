package com.tokopedia.events.view.utils;

import android.util.Log;
import android.util.StateSet;

import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class EventsAnalytics {


    private static final Object EVENT_ADD_CART = "addToCart";
    private static final Object ACTION_CLICK_BELI = "click beli";
    private static final Object ACTION_CLICK_PACKAGE = "click package";
    private static String EVENT = "hiburan";
    private static String EVENT_NAME = "event";
    private static String EVENT_CATEGORY = "eventCategory";
    private static String EVENT_ACTION = "eventAction";
    private static String EVENT_LABEL = "eventLabel";
    private static String ECOMMERCE_LABEL = "ecommerce";
    private static final String CURRENCY_CODE = "currencyCode";
    private static final String IDR = "IDR";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String BRAND = "brand";
    private static final String CATEGORY = "category";
    private static final String CATEGORY_ID = "category_id";
    private static final String VARIANT = "variant";
    private static final String QUANTITY = "quantity";
    private static final String CART_ID = "cart_id";
    private static final String POSITION = "position";
    private static final String ID = "id";
    private static final String LIST = "list";

    public static String EVENT_DIGITAL_EVENT = "digitalGeneralEvent";
    public static String DIGITAL_EVENT = "digital - event";

    public static String EVENT_CLICK_SEARCH = "clickSearch";
    public static String CLICK_SEARCH_ACTION = "click search box";
    public static String CLICK_SEARCH_ICON = "click search";

    public static String EVENT_PRODUCT_VIEW = "productView";
    public static String EVENT_PRODUCT_IMRESSION = "impression event suggestion";
    public static String EVENT_PRODUCT_RESULT = "impression product result";
    public static String EVENT_TOP_EVENT_IMPRESSION = "impression top event";
    public static String EVENT_KEY_IMRESSIONS = "impressions";

    public static String EVENT_PRODUCT_CLICK = "productClick";
    public static String ACTION_PRODUCT_CLICK = "click event suggestion";
    public static String ACTION_PRODUCT_CLICK_RESULT = "click product result";
    public static String ACTION_TOP_EVENT_CLICK = "click top event";
    private static final String HASH_ACTION_FIELD = "actionField";
    private static final String KEY_PRODUCTS = "products";
    private static final String KEY_PROMOTIONS = "promotions";
    private static final String KEY_PROMO_VIEW = "promoView";
    private static final String KEY_PROMO_CLICK = "promoClick";
    private static final String ACTION_PROMO_CLICK = "click banner";
    private static final String HASH_CLICK = "click";

    public static String EVENT_CATEGORY_CLICK = "clickEvent";
    public static String ACTION_CATEGORY_CLICK = "click category icon";

    public static String EVENT_PROMO_VIEW = "promoView";
    public static String ACTION_PROMO_IMPRESSION = "impression banner";

    public static String VIEW_PRODUCT = "viewProduct";
    public static String ACTION_VIEW_PRODUCT = "view product detail";
    public static String KEY_DETAIL = "detail";
    public static String ACTION_CLICK_LANJUKTAN = "click lanjutkan";
    private static final String HASH_ADD = "add";
    private static final String EVENT_CHECKOUT = "checkout";
    private static final String ACTION_VIEW_CHECKOUT = "view checkout";
    private static final String ACTION_PROCEED_PAYMENT = "click proceed payment";
    private static final String KEY_OPTIONS = "option";
    private static final String KEY_STEP = "step";
    private static final String CREATIVE = "creative";
    private static final String CREATIVE_URL = "creative_url";


    @Inject
    public EventsAnalytics() {
    }

    public void eventDigitalEventTracking(String action, String label) {
        Log.d("EVENTSGA", action + " - " + label);
//        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(EVENT_DIGITAL_EVENT, DIGITAL_EVENT, action, label));
    }

    public void sendScreenNameEvent(String screenName) {
        TrackApp.getInstance().getGTM().sendScreenAuthenticated(screenName);
    }

    public void sendGeneralEvent(String eventName, String eventCategory, String eventAction, String eventLabel) {
        Map<String, Object> generalEvent = new HashMap<>();
        generalEvent.put(EVENT_NAME, eventName);
        generalEvent.put(EVENT_CATEGORY, eventCategory);
        generalEvent.put(EVENT_ACTION, eventAction);
        generalEvent.put(EVENT_LABEL, eventLabel);

        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvent);
    }


    public void sendGeneralEcommerceEvent(String eventName, String eventCategory, String eventAction, String eventLabel, Map<String, Object> ecommerce) {
        Map<String, Object> generalEvent = new HashMap<>();
        generalEvent.put(EVENT_NAME, eventName);
        generalEvent.put(EVENT_CATEGORY, eventCategory);
        generalEvent.put(EVENT_ACTION, eventAction);
        generalEvent.put(EVENT_LABEL, eventLabel);
        generalEvent.put(ECOMMERCE_LABEL, ecommerce);
        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvent);
    }

    public void sendSearchProductImpressions(String eventProductView, String digitalEvent, String eventProductImression, List<CategoryItemsViewModel> categoryItems) {

        Log.d("Naveen", "impression event" + eventProductImression);
        List<HashMap<String, Object>> impressionList = new ArrayList<>();
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, eventProductView);
        generalEvents.put(EVENT_CATEGORY, digitalEvent);
        generalEvents.put(EVENT_ACTION, eventProductImression);
        generalEvents.put(EVENT_LABEL, "");

        Map<String, Object> ecommerce = new HashMap<>();
        ecommerce.put(CURRENCY_CODE, IDR);

        for (int index = 0; index< categoryItems.size(); index++) {
            HashMap<String, Object> impressions = new HashMap<>();
            impressions.put(NAME, categoryItems.get(index).getTitle());
            impressions.put(ID, String.valueOf(categoryItems.get(index).getId()));
            impressions.put(PRICE, String.valueOf(categoryItems.get(index).getSalesPrice()));
            impressions.put(CATEGORY, EVENT);
            impressions.put(VARIANT, "none");
            impressions.put(BRAND, "none");
            impressions.put(LIST, String.format("%s - %s - %s", EVENT, index, categoryItems.get(index).getDisplayName()));
            impressions.put(POSITION, index);
            impressionList.add(impressions);
        }

        ecommerce.put(EVENT_KEY_IMRESSIONS, impressionList);
        generalEvents.put(ECOMMERCE_LABEL, ecommerce);

        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
    }


    public void sendPromoImpressions(String eventProductView, String digitalEvent, String eventProductImression, List<CategoryItemsViewModel> categoryItems) {

        List<Map<String, Object>> promotionList = new ArrayList<>();
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, eventProductView);
        generalEvents.put(EVENT_CATEGORY, digitalEvent);
        generalEvents.put(EVENT_ACTION, eventProductImression);
        generalEvents.put(EVENT_LABEL, "");

        Map<String, Object> ecommerce = new HashMap<>();
        Map<String, Object> promoView = new HashMap<>();

        for (int index = 0; index< categoryItems.size(); index++) {
            Map<String, Object> promotions = new HashMap<>();
            promotions.put(NAME, categoryItems.get(index).getTitle());
            promotions.put(ID, String.valueOf(categoryItems.get(index).getId()));
            promotions.put(PRICE, String.valueOf(categoryItems.get(index).getSalesPrice()));
            promotions.put(CATEGORY, EVENT);
            promotions.put(VARIANT, "none");
            promotions.put(CREATIVE, categoryItems.get(index).getDisplayName());
            promotions.put(BRAND, "none");
            promotions.put(LIST, String.format("%s - %s - %s", EVENT, index, categoryItems.get(index).getDisplayName()));
            promotions.put(POSITION, index);
            promotionList.add(promotions);
        }


        promoView.put(KEY_PROMOTIONS, promotionList);
        ecommerce.put(KEY_PROMO_VIEW, promoView);
        generalEvents.put(ECOMMERCE_LABEL, ecommerce);

        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
    }

    public void sendEventSuggestionClickEvent(String eventProductClick, String digitalEvent, String actionProductClick, CategoryItemsViewModel model, int position) {
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, eventProductClick);
        generalEvents.put(EVENT_CATEGORY, digitalEvent);
        generalEvents.put(EVENT_ACTION, actionProductClick);
        generalEvents.put(EVENT_LABEL, String.format("%s - %s", model.getTitle(), position));

        Map<String, Object> ecommerce = new HashMap<>();

        Map<String, Object> actionField = new HashMap<>();
        actionField.put(LIST, String.format("%s - %s - %s", EVENT, String.valueOf(position), model.getDisplayName()));

        Map<String, Object> products = new HashMap<>();
        products.put(NAME, model.getTitle());
        products.put(ID, String.valueOf(model.getId()));
        products.put(PRICE, String.valueOf(model.getSalesPrice()));
        products.put(CATEGORY, EVENT);
        products.put(VARIANT, "none");
        products.put(BRAND, "none");
        products.put(LIST, String.format("%s - %s - %s", EVENT, String.valueOf(position), model.getDisplayName()));
        products.put(POSITION, position);

        Map<String, Object> click = new HashMap<>();
        click.put(HASH_ACTION_FIELD, actionField);
        ecommerce.put(HASH_CLICK, actionField);
        ecommerce.put(KEY_PRODUCTS, Collections.singletonList(products));

        generalEvents.put(ECOMMERCE_LABEL, ecommerce);
        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
    }

    public void sendProductLoadEvent(String viewProduct, String digitalEvent, String actionViewProduct, EventsDetailsViewModel data) {
        Map<String, Object> generalEvent = new HashMap<>();
        generalEvent.put(EVENT_NAME, viewProduct);
        generalEvent.put(EVENT_CATEGORY, digitalEvent);
        generalEvent.put(EVENT_ACTION, actionViewProduct);

        Map<String, Object> ecommerce = new HashMap<>();
        ecommerce.put(CURRENCY_CODE, IDR);

        Map<String, Object> actionField = new HashMap<>();
        actionField.put(LIST, String.format("%s - %s", EVENT, data.getTitle()));
        Map<String, Object> product = new HashMap<>();
        product.put(NAME, data.getTitle());
        product.put(ID, String.valueOf(data.getId()));
        product.put(PRICE, String.valueOf(data.getSalesPrice()));
        product.put(BRAND, "none");
        product.put(CATEGORY, EVENT);
        product.put(VARIANT, "none");
        product.put(LIST, String.format("%s - %s", EVENT, data.getTitle()));

        Map<String, Object> detail = new HashMap<>();
        detail.put(HASH_ACTION_FIELD, actionField);
        detail.put(KEY_PRODUCTS, Collections.singletonList(product));

        ecommerce.put(KEY_DETAIL, detail);
        generalEvent.put(ECOMMERCE_LABEL, ecommerce);

        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvent);
    }

    public void sendPayTicketEvent(String title) {
        Map<String, Object> generalEvent = new HashMap<>();
        generalEvent.put(EVENT_NAME, EVENT_CATEGORY_CLICK);
        generalEvent.put(EVENT_CATEGORY, DIGITAL_EVENT);
        generalEvent.put(EVENT_ACTION, ACTION_CLICK_LANJUKTAN);
        generalEvent.put(EVENT_LABEL, title);

        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvent);
    }

    public void sendSelectPackageEvent(String packageName, int selectedQuantity) {
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, EVENT_CATEGORY_CLICK);
        generalEvents.put(EVENT_CATEGORY, DIGITAL_EVENT);
        generalEvents.put(EVENT_ACTION, ACTION_CLICK_PACKAGE);
        generalEvents.put(EVENT_LABEL, String.format("%s - %s", packageName, String.valueOf(selectedQuantity)));
        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
    }

    public void sendATCEvent(PackageViewModel selectedPackageViewModel) {
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, EVENT_ADD_CART);
        generalEvents.put(EVENT_CATEGORY, DIGITAL_EVENT);
        generalEvents.put(EVENT_ACTION, ACTION_CLICK_BELI);
        generalEvents.put(EVENT_LABEL, String.format("%s - %s", EVENT, selectedPackageViewModel.getDisplayName()));

        try {
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String, Object> add = new HashMap<>();

            productMap.put(NAME, selectedPackageViewModel.getDisplayName());
            productMap.put(ID, String.valueOf(selectedPackageViewModel.getId()));
            productMap.put(PRICE, String.valueOf(selectedPackageViewModel.getSalesPrice()));
            productMap.put(CATEGORY, EVENT);
            productMap.put(CATEGORY_ID, String.valueOf(selectedPackageViewModel.getCategoryId()));
            productMap.put(QUANTITY, selectedPackageViewModel.getSelectedQuantity());
            productMap.put(VARIANT, "none");
            productMap.put(CART_ID, "0");
            add.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(HASH_ADD, add);

            generalEvents.put(ECOMMERCE_LABEL, ecommerce);
            TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
        } catch (Exception e) {

        }

    }


    public void sendViewCheckoutEvent(PackageViewModel selectedPackageViewModel) {
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, EVENT_CHECKOUT);
        generalEvents.put(EVENT_CATEGORY, DIGITAL_EVENT);
        generalEvents.put(EVENT_ACTION, ACTION_VIEW_CHECKOUT);
        generalEvents.put(EVENT_LABEL, String.format("%s - %s", EVENT, selectedPackageViewModel.getDisplayName()));

        try {
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String, Object> checkout = new HashMap<>();
            HashMap<String, Object> actionfield = new HashMap<>();

            actionfield.put(KEY_STEP, "1");
            actionfield.put(KEY_OPTIONS, "cart page loaded");
            checkout.put(HASH_ACTION_FIELD, actionfield);

            productMap.put(NAME, selectedPackageViewModel.getDisplayName());
            productMap.put(ID, String.valueOf(selectedPackageViewModel.getId()));
            productMap.put(PRICE, String.valueOf(selectedPackageViewModel.getSalesPrice()));
            productMap.put(CATEGORY, EVENT);
            productMap.put(BRAND, "none");
            productMap.put(QUANTITY, selectedPackageViewModel.getSelectedQuantity());
            productMap.put(VARIANT, "none");
            checkout.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(EVENT_CHECKOUT, checkout);

            generalEvents.put(ECOMMERCE_LABEL, ecommerce);
            TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
        } catch (Exception e) {

        }
    }

    public void sendProceedToPaymentEvent(PackageViewModel selectedPackageViewModel) {
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, EVENT_CHECKOUT);
        generalEvents.put(EVENT_CATEGORY, DIGITAL_EVENT);
        generalEvents.put(EVENT_ACTION, ACTION_PROCEED_PAYMENT);
        generalEvents.put(EVENT_LABEL, String.format("%s - %s", EVENT, selectedPackageViewModel.getDisplayName()));

        try {
            HashMap<String, Object> productMap = new HashMap<>();
            HashMap<String, Object> ecommerce = new HashMap<>();
            HashMap<String, Object> checkout = new HashMap<>();
            HashMap<String, Object> actionfield = new HashMap<>();

            actionfield.put(KEY_STEP, "2");
            actionfield.put(KEY_OPTIONS, "click payment option button");
            checkout.put(HASH_ACTION_FIELD, actionfield);

            productMap.put(NAME, selectedPackageViewModel.getDisplayName());
            productMap.put(ID, String.valueOf(selectedPackageViewModel.getId()));
            productMap.put(PRICE, String.valueOf(selectedPackageViewModel.getSalesPrice()));
            productMap.put(CATEGORY, EVENT);
            productMap.put(BRAND, "none");
            productMap.put(QUANTITY, selectedPackageViewModel.getSelectedQuantity());
            productMap.put(VARIANT, "none");
            checkout.put(KEY_PRODUCTS, Collections.singletonList(productMap));
            ecommerce.put(CURRENCY_CODE, IDR);
            ecommerce.put(EVENT_CHECKOUT, checkout);

            generalEvents.put(ECOMMERCE_LABEL, ecommerce);
            TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);
        } catch (Exception e) {

        }
    }

    public void sendPromoClickEvent(CategoryItemsViewModel model, int position) {
        Map<String, Object> generalEvents = new HashMap<>();
        generalEvents.put(EVENT_NAME, KEY_PROMO_CLICK);
        generalEvents.put(EVENT_CATEGORY, DIGITAL_EVENT);
        generalEvents.put(EVENT_ACTION, ACTION_PROMO_CLICK);
        generalEvents.put(EVENT_LABEL, String.format("%s - %s", model.getDisplayName(), String.valueOf(position)));

        Map<String, Object> ecommerce = new HashMap<>();
        Map<String, Object> promoClick = new HashMap<>();

        Map<String, Object> promotions = new HashMap<>();

            promotions.put(NAME, model.getTitle());
            promotions.put(ID, String.valueOf(model.getId()));
            promotions.put(PRICE, String.valueOf(model.getSalesPrice()));
            promotions.put(PRICE, String.valueOf(model.getSalesPrice()));
            promotions.put(CATEGORY, EVENT);
            promotions.put(VARIANT, "none");
            promotions.put(CREATIVE, model.getDisplayName());
            promotions.put(CREATIVE_URL, model.getImageApp());
            promotions.put(BRAND, "none");
            promotions.put(LIST, String.format("%s - %s - %s", EVENT, String.valueOf(position), model.getDisplayName()));
            promotions.put(POSITION, position);


        promoClick.put(KEY_PROMOTIONS, Collections.singletonList(promotions));
        ecommerce.put(KEY_PROMO_CLICK, promoClick);
        generalEvents.put(ECOMMERCE_LABEL, ecommerce);

        TrackApp.getInstance().getGTM().sendGeneralEvent(generalEvents);

    }
}
