package com.tokopedia.loyalty.view.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventTracking {
    private static final String KEY_PARAM_EVENT = "event";
    private static final String KEY_PARAM_CATEGORY = "eventCategory";
    private static final String KEY_PARAM_ACTION = "eventAction";
    private static final String KEY_PARAM_LABEL = "eventLabel";
    private static final String KEY_PARAM_ECOMMERCE = "ecommerce";

    private Map<String, Object> eventTracking;

    public EventTracking() {
        this.eventTracking = new HashMap<>();
    }

    public EventTracking(String event, String category, String action, String label) {
        this();
        this.eventTracking.put(KEY_PARAM_EVENT, event);
        this.eventTracking.put(KEY_PARAM_CATEGORY, category);
        this.eventTracking.put(KEY_PARAM_ACTION, action);
        this.eventTracking.put(KEY_PARAM_LABEL, label);
    }

    public EventTracking(String event, String category, String action,
                         String label, Ecommerce ecommerce) {
        this(event, category, action, label);
        this.eventTracking.put(KEY_PARAM_ECOMMERCE, ecommerce.getEventMap());
    }

    public void addCustomTracking(String key, Object tracking) {
        this.eventTracking.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.eventTracking;
    }

    public static Map<String, Object> getEventTracking(String event, String eventCategory,
                                                       String eventAction, String eventLabel,
                                                       final String id, final int page,
                                                       final int position, final String creative,
                                                       final String creativeUrl,
                                                       final String promoCode) {

        return new EventTracking(event, eventCategory, eventAction, eventLabel,
                new Ecommerce(
                        new PromoView(
                                new ArrayList<Object>() {{
                                    add((new Promotion(id, page, position, creative, creativeUrl,
                                            promoCode)
                                    ).getEventMap());
                                }}
                        )
                )
        ).getEventMap();
    }

}

class Ecommerce {
    private static final String KEY_PARAM_PROMO_VIEW = "promoView";
    private static final String KEY_PARAM_PROMO_CLICK = "promoClick";

    private Map<String, Object> ecommerce;

    public Ecommerce() {
        this.ecommerce = new HashMap<>();
    }

    public Ecommerce(PromoView promoView) {
        this();
        this.ecommerce.put(KEY_PARAM_PROMO_VIEW, promoView.getEventMap());
    }

    public Ecommerce(PromoClick promoClick) {
        this();
        this.ecommerce.put(KEY_PARAM_PROMO_CLICK, promoClick.getEventMap());
    }

    public void addCustomTracking(String key, Object tracking) {
        this.ecommerce.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.ecommerce;
    }

}

class PromoView {
    private static final String KEY_PARAM_PROMOTIONS = "promotions";

    private Map<String, Object> promotions;

    public PromoView() {
        this.promotions = new HashMap<>();
    }

    public PromoView(List<Object> promotions) {
        this();
        this.promotions.put(KEY_PARAM_PROMOTIONS, promotions);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.promotions.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.promotions;
    }

}

class PromoClick {
    private static final String KEY_PARAM_PROMOTIONS = "promotions";

    private Map<String, Object> promotions;

    public PromoClick() {
        this.promotions = new HashMap<>();
    }

    public PromoClick(List<Object> promotions) {
        this();
        this.promotions.put(KEY_PARAM_PROMOTIONS, promotions);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.promotions.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.promotions;
    }

}

class Promotion {
    private static final String KEY_PARAM_ID = "id";
    private static final String KEY_PARAM_NAME = "name";
    private static final String KEY_PARAM_POSITION = "position";
    private static final String KEY_PARAM_CREATIVE = "creative";
    private static final String KEY_PARAM_CREATIVE_URL = "creative_url";
    private static final String KEY_PARAM_PROMO_CODE = "promo_code";

    private Map<String, Object> promotion;

    public Promotion() {
        this.promotion = new HashMap<>();
    }

    public Promotion(String id, String promoCode) {
        this();
        this.promotion.put(KEY_PARAM_ID, id);
        this.promotion.put(KEY_PARAM_PROMO_CODE, promoCode);
    }

    public Promotion(String id, int page, int position, String creative, String creativeUrl, String promoCode) {
        this();
        this.promotion.put(KEY_PARAM_ID, id);
        this.promotion.put(KEY_PARAM_NAME, "/promo - p" + page + " - promo list banner");
        this.promotion.put(KEY_PARAM_POSITION, position);
        this.promotion.put(KEY_PARAM_CREATIVE, creative);
        this.promotion.put(KEY_PARAM_CREATIVE_URL, creativeUrl);
        this.promotion.put(KEY_PARAM_PROMO_CODE, promoCode);
    }

    public void addCustomTracking(String key, Object tracking) {
        this.promotion.put(key, tracking);
    }

    public Map<String, Object> getEventMap() {
        return this.promotion;
    }

}