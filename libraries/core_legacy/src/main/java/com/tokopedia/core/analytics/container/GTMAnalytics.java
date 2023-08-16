package com.tokopedia.core.analytics.container;

import static com.google.firebase.analytics.FirebaseAnalytics.Param.CREATIVE_NAME;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.CREATIVE_SLOT;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEMS;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_LIST;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.ITEM_NAME;
import static com.tokopedia.core.analytics.TrackingUtils.getAfUniqueId;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.analytics.mapper.model.EmbraceConfig;
import com.tokopedia.analytics.performance.util.EmbraceMonitoring;
import com.tokopedia.analyticsdebugger.cassava.AnalyticsSource;
import com.tokopedia.analyticsdebugger.cassava.Cassava;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.deeplink.DeeplinkUTMUtils;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.util.DateUtil;
import com.tokopedia.core.util.PriceUtil;
import com.tokopedia.device.info.DeviceConnectionInfo;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.relic.track.NewRelicUtil;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.interfaces.ContextAnalytics;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class GTMAnalytics extends ContextAnalytics {
    public static final String OPEN_SCREEN = "openScreen";
    public static final String CAMPAIGN_TRACK = "campaignTrack";
    public static final String KEY_GCLID = "gclid";
    public static final String CLIENT_ID = "clientId";
    public static final String SESSION_IRIS = "sessionIris";
    public static final String PROMOVIEW = "promoview";
    private static final String EMPTY_DEFAULT_VALUE = "none / other";
    private static final String KEY_DIMENSION_40 = "dimension40";
    private static final String KEY_EVENT = "event";
    private static final String KEY_CATEGORY = "eventCategory";
    private static final String KEY_ACTION = "eventAction";
    private static final String KEY_LABEL = "eventLabel";
    private static final String KEY_PROMOTIONS = "promotions";
    private static final String USER_ID = "userId";
    private static final String SHOP_ID = "shopId";
    private static final String SHOP_TYPE = "shopType";
    private static final String TRANSACTION = "transaction";
    private static final String PRODUCTVIEW = "productview";
    private static final String PRODUCTCLICK = "productclick";
    private static final String VIEWPRODUCT = "viewproduct";
    private static final String REMOVEFROMCART = "removefromcart";
    private static final String ADDTOCART = "addtocart";
    private static final String CHECKOUT = "checkout";
    private static final String BEGINCHECKOUT = "begin_checkout";
    // have status that describe pending.
    private static final String CHECKOUT_PROGRESS = "checkout_progress";
    private static final String PROMOCLICK = "promoclick";
    public static String[] GENERAL_EVENT_KEYS = new String[]{
            KEY_ACTION, KEY_CATEGORY, KEY_LABEL, KEY_EVENT
    };
    private static final String ECOMMERCE = "ecommerce";
    private final Iris iris;
    private final RemoteConfig remoteConfig;
    private final Long DELAY_GET_CONN = 120000L; //2 minutes
    private String clientIdString = "";
    private final UserSessionInterface userSession;
    private final SharedPreferences sharedPreferences;
    private String connectionTypeString = "";
    private Long lastGetConnectionTimeStamp = 0L;
    private String mGclid = "";
    private final String IM_CLICK_ID = "imclickid";

    private static final String GTM_SIZE_LOG_REMOTE_CONFIG_KEY = "android_gtm_size_log";
    private static final String ANDROID_GA_EVENT_LOGGING = "android_ga_event_logging";
    private static final long GTM_SIZE_LOG_THRESHOLD_DEFAULT = 6000;
    private static long gtmSizeThresholdLog = 0;
    private static final String EMBRACE_BREADCRUMB_FORMAT = "%s, %s";
    private static final String EMBRACE_KEY = "GTMAnalytics";
    private static final String EMBRACE_EVENT_NAME = "eventName";
    private static final String EMBRACE_EVENT_ACTION = "eventAction";
    private static final String EMBRACE_EVENT_LABEL = "eventLabel";

    private static String UTM_SOURCE_HOLDER = "";
    private static String UTM_MEDIUM_HOLDER = "";
    private static String UTM_CAMPAIGN_HOLDER = "";

    public static void setUTMParamsForSession(Map<String, Object> maps) {
        if (maps != null && maps.get(AppEventTracking.GTM.UTM_SOURCE) != null) {
            UTM_SOURCE_HOLDER = Objects.requireNonNull(maps.get(AppEventTracking.GTM.UTM_SOURCE)).toString();
        }
        if (maps != null && maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null) {
            UTM_MEDIUM_HOLDER = Objects.requireNonNull(maps.get(AppEventTracking.GTM.UTM_MEDIUM)).toString();
        }
        if (maps != null && maps.get(AppEventTracking.GTM.UTM_CAMPAIGN) != null) {
            UTM_CAMPAIGN_HOLDER = Objects.requireNonNull(maps.get(AppEventTracking.GTM.UTM_CAMPAIGN)).toString();
        }
    }

    public GTMAnalytics(Context context) {
        super(context);
        iris = IrisAnalytics.Companion.getInstance(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);
        userSession = new UserSession(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String bruteForceCastToString(Object object) {
        Integer integer = safeCast(object, Integer.class);

        if (integer != null)
            return integer.toString();

        Long aLong = safeCast(object, Long.class);

        if (aLong != null)
            return aLong.toString();

        Double aDouble = safeCast(object, Double.class);

        if (aDouble != null)
            return aDouble.toString();

        String aString = safeCast(object, String.class);

        return TextUtils.isEmpty(aString) ? "" : aString;
    }

    public static <T> T safeCast(Object o, Class<T> clazz) {
        return clazz != null && clazz.isInstance(o) ? clazz.cast(o) : null;
    }

    public static Map<String, Object> clone(Map<String, Object> original) {
        Map<String, Object> map = new HashMap<>();
        for (Iterator<String> iterator = original.keySet().iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            Object value = original.get(key);
            if (value != null) {
                if (value instanceof Map) {
                    map.put(key, clone((Map<String, Object>) value));
                } else if (value instanceof Object[]) {
                    map.put(key, clone((Object[]) value));
                } else if (value instanceof List) {
                    map.put(key, clone((List) value));
                } else {
                    map.put(key, value);
                }
            }
        }
        return map;
    }

    public static List clone(List original) {
        List result = new ArrayList();
        if (original != null) {
            for (int i = 0; i < original.size(); i++) {
                Object value = original.get(i);
                if (value != null) {
                    if (value instanceof Map) {
                        result.add(clone((Map<String, Object>) value));
                    } else if (value instanceof Object[]) {
                        result.add(clone((Object[]) value));
                    } else if (value instanceof List) {
                        result.add(clone((List) value));
                    } else {
                        result.add(value);
                    }
                }
            }
        }
        return result;
    }

    public static Object[] clone(Object[] original) {
        Object[] result = new Object[original.length];
        if (original != null) {
            for (int i = 0; i < original.length; i++) {
                Object value = original[i];
                if (value != null) {
                    if (value instanceof Map) {
                        result[i] = clone((Map<String, Object>) value);
                    } else if (value instanceof Object[]) {
                        result[i] = clone((Object[]) value);
                    } else if (value instanceof List) {
                        result[i] = clone((List) value);
                    } else {
                        result[i] = value;
                    }
                }
            }
        }
        return result;
    }

    private static Map<String, Object> bundleToMap(Bundle extras) {
        Map<String, Object> map = new HashMap<>();

        Set<String> ks = extras.keySet();
        for (String key : ks) {
            Object object = extras.get(key);
            if (object != null) {
                if (object instanceof ArrayList) {
                    object = convertAllBundleToMap((ArrayList) object);
                } else if (object instanceof Bundle) {
                    object = bundleToMap((Bundle) object);
                }
                map.put(key, object);
            }
        }
        return map;
    }

    private static List<Object> convertAllBundleToMap(ArrayList list) {
        List<Object> newList = new ArrayList<>();
        for (Object object : list) {
            if (object instanceof Bundle) {
                newList.add(bundleToMap((Bundle) object));
            } else {
                newList.add(object);
            }
        }
        return newList;
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        pushGeneralGtmV5Internal(value);
    }

    @Override
    public void sendGeneralEvent(String event, String category, String action, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EVENT, event);
        map.put(KEY_CATEGORY, category);
        map.put(KEY_ACTION, action);
        map.put(KEY_LABEL, label);

        pushGeneralGtmV5Internal(map);
    }

    @Override
    public void sendEnhanceEcommerceEvent(Map<String, Object> value) {

        // https://tokopedia.atlassian.net/browse/AN-19138
        if (!value.containsKey(ECOMMERCE)) {
            sendGeneralEvent(value);
            return;
        }
        // https://tokopedia.atlassian.net/browse/AN-19138

        Observable.just(value)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(this::sendEnhanceECommerceEventOrigin)
                .subscribe(getDefaultSubscriber());
    }

    private boolean sendEnhanceECommerceEventOrigin(Map<String, Object> value) {
        // V4
        clearEnhanceEcommerce();
        pushGeneralEcommerce(clone(value));

        // V5
        try {
            String keyEvent = keyEvent(clone(value));
            // prevent sending null keyevent
            if (keyEvent == null) return false;
            pushEECommerceInternal(keyEvent, factoryBundle(bruteForceCastToString(value.get("event")), clone(value)));
        } catch (Exception e) {
            StringBuilder stacktrace = new StringBuilder();
            for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                stacktrace.append(String.format("%s\n", ste.toString()));
            }
            Map<String, Object> logMap = Collections.singletonMap("stacktrace", stacktrace.toString());
            Cassava.save(logMap, null, AnalyticsSource.ERROR);
            if (!TextUtils.isEmpty(e.getMessage())) {
                Map<String, String> map = new HashMap<>();
                map.put("msg", e.getMessage());
                map.put("err", e.toString());
                ServerLogger.log(Priority.P2, "GTM_ANALYTIC_ERROR", map);
            }
        }
        return true;
    }

    public Bundle addWrapperValue(Bundle bundle) {
        bundle.putString(CLIENT_ID, getClientIDString());
        bundle.putString(USER_ID, userSession.getUserId());
        if (!CommonUtils.checkStringNotNull(bundle.getString(SESSION_IRIS)))
            bundle.putString(SESSION_IRIS, new IrisSession(context).getSessionId());
        return bundle;
    }

    @Override
    public void sendEnhanceEcommerceEvent(String eventName, Bundle value) {
        Bundle bundle = addWrapperValue(value);
        bundle = addGclIdIfNeeded(eventName, bundle);
        pushEventV5(eventName, bundle, context);
        pushIris(eventName, bundle);
    }

    @SuppressWarnings("unchecked")
    private String keyEvent(Map<String, Object> value) {
        String event = bruteForceCastToString(value.get("event"));
        if (event != null) {
            switch (event.toLowerCase()) {
                case CHECKOUT:
                    Map<String, Object> ecommerce = (Map<String, Object>) value.get("ecommerce");
                    Map<String, Object> checkout = (Map<String, Object>) ecommerce.get("checkout");
                    Map<String, Object> actionField = (Map<String, Object>) checkout.get("actionField");
                    String step = bruteForceCastToString(actionField.get("step"));
                    if (step != null) {
                        switch (step) {
                            case "1":
                                return FirebaseAnalytics.Event.BEGIN_CHECKOUT;
                            default:
                                return FirebaseAnalytics.Event.CHECKOUT_PROGRESS;
                        }
                    }
                case REMOVEFROMCART:
                    return FirebaseAnalytics.Event.REMOVE_FROM_CART;
                case ADDTOCART:
                    return FirebaseAnalytics.Event.ADD_TO_CART;
                default:
                    return event;
            }
        } else {
            return null;
        }
    }

    private Bundle factoryBundle(String keyEvent, Map<String, Object> value) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, value.remove(KEY_CATEGORY) + "");
        bundle.putString(KEY_ACTION, value.remove(KEY_ACTION) + "");
        bundle.putString(KEY_LABEL, value.remove(KEY_LABEL) + "");

        Map<String, Object> ecommerce = (Map<String, Object>) value.remove("ecommerce");
        if (keyEvent != null) {
            switch (keyEvent.toLowerCase()) {
                case PRODUCTVIEW:
                    productImpressionBundle(keyEvent, bundle, ecommerce);
                    break;
                case PRODUCTCLICK:
                case VIEWPRODUCT:
                    productBundle(keyEvent, bundle, ecommerce);
                    break;
                case REMOVEFROMCART:
                case ADDTOCART:
                    cartBundle(bundle, ecommerce);
                    break;
                case TRANSACTION:
                    transactionBundle(bundle, ecommerce);
                    break;
                case CHECKOUT:
                    checkoutBundle(bundle, ecommerce);
                    break;
                case PROMOVIEW:
                    promoView(bundle, ecommerce);
                    break;
                case PROMOCLICK:
                    promoClickBundle(bundle, ecommerce);
                    break;
            }
        }

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            bundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return bundle;
    }

    private void productImpressionBundle(String keyEvent, Bundle bundle, Map<String, Object> ecommerce) {
        Object impressions = ecommerce.remove("impressions");
        String list = "";
        if (impressions instanceof List) {
            List viewProduct = (List) impressions;
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            for (int j = 0; j < viewProduct.size(); j++) {
                Object promotionObj = viewProduct.get(j);
                if (promotionObj != null) {
                    if (promotionObj instanceof Object[]) {
                        Object[] promotions = (Object[]) promotionObj;

                        for (int i = 0; i < promotions.length; i++) {
                            Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                            ViewProductResult viewProductResult = viewProductMap(promotion, i + 1);
                            list = viewProductResult.list;
                            promotionBundles.add(viewProductResult.first);
                        }
                    } else if (promotionObj instanceof ArrayList) {
                        List promotions = (List) promotionObj;

                        for (int i = 0; i < promotions.size(); i++) {
                            Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                            ViewProductResult viewProductResult = viewProductMap(promotion, i + 1);
                            list = viewProductResult.list;
                            promotionBundles.add(viewProductResult.first);
                        }
                    } else if (promotionObj instanceof Map) {
                        Map<String, Object> promotions = (Map<String, Object>) promotionObj;
                        ViewProductResult viewProductResult = viewProductMap(promotions, j + 1);
                        list = viewProductResult.list;
                        promotionBundles.add(viewProductResult.first);
                    }
                }
            }
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, list);
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private void productBundle(String keyEvent, Bundle bundle, Map<String, Object> ecommerce) {
        Map<String, Object> viewProduct = null;
        switch (keyEvent.toLowerCase()) {
            case PRODUCTCLICK:
                viewProduct = (Map<String, Object>) ecommerce.remove("click");
                break;
            case VIEWPRODUCT:
                viewProduct = (Map<String, Object>) ecommerce.remove("detail");
                break;
        }


        if (viewProduct.get("actionField") != null) {
            Map<String, Object> actionField = (Map<String, Object>) viewProduct.remove("actionField");
            if (actionField.get("list") != null)
                bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bruteForceCastToString(actionField.remove("list")));
        }

        Object promotionObj = viewProduct.remove("products");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    ViewProductResult viewProductResult = viewProductMap(promotion, i + 1);
                    promotionBundles.add(viewProductResult.first);
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    ViewProductResult viewProductResult = viewProductMap(promotion, i + 1);
                    promotionBundles.add(viewProductResult.first);
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private void cartBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object promotionObj;
        Map<String, Object> container = null;
        if (ecommerce.get("remove") != null) {
            container = (Map<String, Object>) ecommerce.remove("remove");
        } else if (ecommerce.get("add") != null) {
            container = (Map<String, Object>) ecommerce.remove("add");
        }

        // no action field

        promotionObj = container.remove("products");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(atcMap(promotion));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(atcMap(promotion));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private double emptyDouble(String doubleRaw) {
        return TextUtils.isEmpty(doubleRaw) ? 0.0 :
                Double.valueOf(PriceUtil.from(doubleRaw));
    }

    private void transactionBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object promotionObj;
        Map<String, Object> purchase = (Map<String, Object>) ecommerce.remove("purchase");

        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "IDR");

        if (purchase.get("actionField") != null) {
            Map<String, Object> actionField = (Map<String, Object>) purchase.remove("actionField");

            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, bruteForceCastToString(actionField.remove(PurchaseKey.KEY_ID)));
            bundle.putString(FirebaseAnalytics.Param.AFFILIATION, bruteForceCastToString(actionField.remove(PurchaseKey.KEY_AFFILIATION)));
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, emptyDouble(bruteForceCastToString(actionField.remove(PurchaseKey.KEY_REVENUE)))); // Revenue
            bundle.putDouble(FirebaseAnalytics.Param.TAX, emptyDouble(bruteForceCastToString(actionField.remove(PurchaseKey.KEY_TAX))));
            bundle.putDouble(FirebaseAnalytics.Param.SHIPPING, emptyDouble(bruteForceCastToString(actionField.remove(PurchaseKey.KEY_SHIPPING))));
            bundle.putString(FirebaseAnalytics.Param.COUPON, bruteForceCastToString(actionField.remove(PurchaseKey.KEY_COUPON)));
        }

        // get products
        promotionObj = purchase.get("products");
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) purchase.get("products");
                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            } else if (promotionObj instanceof List) {
                List promotions = (List) purchase.get("products");

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private void promoClickBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object promotionObj;
        Map<String, Object> promoClick = (Map<String, Object>) ecommerce.remove("promoClick");

        promotionObj = promoClick.remove(KEY_PROMOTIONS);
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(promotionMap(promotion));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(promotionMap(promotion));
                }
            }
            bundle.putParcelableArrayList(KEY_PROMOTIONS, promotionBundles);
        }
    }

    private void promoView(Bundle bundle, Map<String, Object> ecommerce) {
        Map<String, Object> promoView = (Map<String, Object>) ecommerce.remove("promoView");
        Object promotionObj = promoView.remove(KEY_PROMOTIONS);
        if (promotionObj != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (promotionObj instanceof Object[]) {
                Object[] promotions = (Object[]) promotionObj;

                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(promotionMap(promotion));
                }
            } else if (promotionObj instanceof ArrayList) {
                List promotions = (List) promotionObj;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(promotionMap(promotion));
                }
            }
            bundle.putParcelableArrayList(KEY_PROMOTIONS, promotionBundles);
        }
    }

    private void checkoutBundle(Bundle bundle, Map<String, Object> ecommerce) {
        Object checkoutProducts;
        Map<String, Object> checkout = (Map<String, Object>) ecommerce.remove("checkout");

        // get step and option
        if (checkout.get("actionField") != null) {
            Map<String, Object> actionField = (Map<String, Object>) checkout.remove("actionField");

            String step = bruteForceCastToString(actionField.get("step"));
            String option = bruteForceCastToString(actionField.get("option"));

            bundle.putString(FirebaseAnalytics.Param.CHECKOUT_STEP, step);
            bundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, option);
        }

        // get products
        checkoutProducts = checkout.remove("products");
        if (checkoutProducts != null) {
            ArrayList<Bundle> promotionBundles = new ArrayList<>();
            if (checkoutProducts instanceof Object[]) {
                Object[] promotions = (Object[]) checkoutProducts;
                for (int i = 0; i < promotions.length; i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions[i];
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            } else if (checkoutProducts instanceof List) {
                List promotions = (List) checkoutProducts;

                for (int i = 0; i < promotions.size(); i++) {
                    Map<String, Object> promotion = (Map<String, Object>) promotions.get(i);
                    promotionBundles.add(checkoutProductMap(promotion));
                }
            }
            bundle.putParcelableArrayList("items", promotionBundles);
        }
    }

    private Bundle checkoutProductMap(Map<String, Object> value) {
        String id = bruteForceCastToString(value.remove(CheckoutKey.KEY_ID));
        String name = (String) value.remove(CheckoutKey.KEY_NAME);
        String brand = (String) value.remove(CheckoutKey.KEY_BRAND);
        String category = bruteForceCastToString(value.remove(CheckoutKey.KEY_CAT));
        String variant = (String) value.remove(CheckoutKey.KEY_VARIANT);
        String priceString = bruteForceCastToString(value.remove(CheckoutKey.KEY_PRICE));
        double price = TextUtils.isEmpty(priceString) ? 0.0 :
                Double.valueOf(PriceUtil.from(priceString));
        String qtyString = bruteForceCastToString(value.remove(CheckoutKey.KEY_QTY));
        int quantity = TextUtils.isEmpty(qtyString) ? 0 : Integer.valueOf(qtyString);

        Bundle checkoutBundle = new Bundle();
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, !TextUtils.isEmpty(brand) ? brand : "");
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        checkoutBundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, !TextUtils.isEmpty(variant) ? variant : "");
        checkoutBundle.putDouble(FirebaseAnalytics.Param.PRICE, price);
        checkoutBundle.putLong(FirebaseAnalytics.Param.QUANTITY, quantity);

        // custom dimension
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            checkoutBundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }

        return checkoutBundle;
    }

    private ViewProductResult viewProductMap(Map<String, Object> value, int index) {
        String id = bruteForceCastToString(value.remove(ProductKey.KEY_ID));
        String name = (String) value.remove(ProductKey.KEY_NAME);
        String price = bruteForceCastToString(value.remove(ProductKey.KEY_PRICE));
        String brand = (String) value.remove(ProductKey.KEY_BRAND);
        String category = bruteForceCastToString(value.remove(ProductKey.KEY_CAT));
        String variant = (String) value.remove(ProductKey.KEY_VARIANT);
        String position = bruteForceCastToString(value.remove(ProductKey.KEY_POSITION));
        String list = bruteForceCastToString(value.remove(ProductKey.KEY_LIST));

        if (TextUtils.isEmpty(brand)) {
            brand = EMPTY_DEFAULT_VALUE;
        }

        if (TextUtils.isEmpty(category)) {
            category = EMPTY_DEFAULT_VALUE;
        }

        if (TextUtils.isEmpty(variant)) {

            variant = (String) value.remove("varian"); // tolerate typo from developers

            if (TextUtils.isEmpty(variant)) {
                variant = EMPTY_DEFAULT_VALUE;
            }
        }

        Bundle product1 = new Bundle();
        product1.putString(FirebaseAnalytics.Param.ITEM_ID, id);                    // dimension69 (Product_ID), mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_NAME, name);   // Product Name, mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_BRAND, brand);        // if not applicable pass “none / other”, in the future, need brand name and also ID, optional
        product1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);      // Product category {{level1_name}} / {{level2_name}} / {{level3_name}} / {{childCatID}}
        product1.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variant);              // If not applicable pass “none / other”, optional
        product1.putString(KEY_DIMENSION_40, list);              // Using customDimension for substituting list that cannot be sent multiple in v5

        product1.putDouble(FirebaseAnalytics.Param.PRICE, Double.valueOf(PriceUtil.from(price)));
        if (position != null && !TextUtils.isEmpty(position)) {
            try {
                index = Integer.valueOf(position);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }
        product1.putLong(FirebaseAnalytics.Param.INDEX, index);

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            product1.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return new ViewProductResult(product1, list);

    }

    private Bundle atcMap(Map<String, Object> value) {
        String id = bruteForceCastToString(value.remove(ATCKey.KEY_ID));
        String name = (String) value.remove(ATCKey.KEY_NAME);
        String price = PriceUtil.from(bruteForceCastToString(value.remove(ATCKey.KEY_PRICE)));
        String brand = (String) value.remove(ATCKey.KEY_BRAND);
        String category = (String) value.remove(ATCKey.KEY_CAT);
        String variant = (String) value.remove(ATCKey.KEY_VARIANT);
        String quantity = bruteForceCastToString(value.remove(ATCKey.KEY_QTY));

        // bundle.putLong (Long.class) <-- Long <-- String<-- Long, Int, String
        // safecast (ke string) kalau null safecast ke integer kalau null safecast ke long
        // target class

        Bundle product1 = new Bundle();
        product1.putString(FirebaseAnalytics.Param.ITEM_ID, id);                    // dimension69 (Product_ID), mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_NAME, name);   // Product Name, mandatory
        product1.putString(FirebaseAnalytics.Param.ITEM_BRAND, brand);        // if not applicable pass “none / other”, in the future, need brand name and also ID, optional
        product1.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);      // Product category {{level1_name}} / {{level2_name}} / {{level3_name}} / {{childCatID}}
        product1.putString(FirebaseAnalytics.Param.ITEM_VARIANT, variant);              // If not applicable pass “none / other”, optional
        product1.putDouble(FirebaseAnalytics.Param.PRICE, TextUtils.isEmpty(quantity) ? 0 : Double.valueOf(price));                      // In double format, mandatory
        product1.putLong(FirebaseAnalytics.Param.QUANTITY, TextUtils.isEmpty(quantity) ? 0 : Long.valueOf(quantity));

        for (Map.Entry<String, Object> entry : value.entrySet()) {
            product1.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return product1;

    }

    private Bundle promotionMap(Map<String, Object> value) {
        String id = bruteForceCastToString(value.remove(PromotionKey.KEY_ID));
        String name = (String) value.remove(PromotionKey.KEY_NAME);
        String creative = (String) value.remove(PromotionKey.KEY_CREATIVE);
        String position = bruteForceCastToString(value.remove(PromotionKey.KEY_POSITION));

        Bundle promotionsBundle = new Bundle();
        promotionsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);  // ‘id’ or ‘name’ is required
        promotionsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);    // promotion slot name: ex: /p/fashion-wanita - p1 - slider banner
        promotionsBundle.putString(FirebaseAnalytics.Param.CREATIVE_NAME, creative);
        promotionsBundle.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, position);

        // custom dimension
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            promotionsBundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }
        return promotionsBundle;

    }

    public String getClientIDString() {
        try {
            if (TextUtils.isEmpty(clientIdString)) {
                Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA).metaData;
                clientIdString = GoogleAnalytics.getInstance(getContext()).newTracker(bundle.getString(AppEventTracking.GTM.GA_ID)).get("&cid");
            }
            return clientIdString;
        } catch (Exception e) {
            e.printStackTrace();
            return "NO_GA_ID";
        }
    }

    @Override
    public String getCachedClientIDString() {
        if (clientIdString == null) {
            return "";
        }

        return clientIdString;
    }

    @Override
    public String getIrisSessionId() {
        return iris.getSessionId();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void eventError(String screenName, String errorDesc) {

    }

    public void sendScreen(String screenName, Map<String, String> customDimension) {

        UserSessionInterface userSession = new UserSession(context);
        final String afUniqueId = !TextUtils.isEmpty(getAfUniqueId(context)) ? getAfUniqueId(context) : "none";

        // V5 sendScreen
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString(KEY_EVENT, OPEN_SCREEN);
        String userId = userSession.getUserId();
        if (!TextUtils.isEmpty(userId)) {
            bundle.putString("userId", userId);
        } else {
            bundle.putString("userId", "");
        }
        bundle.putString(CLIENT_ID, getClientIDString());
        bundle.putBoolean("isLoggedInStatus", userSession.isLoggedIn());
        if (!TextUtils.isEmpty(userSession.getShopId())) {
            bundle.putString("shopId", userSession.getShopId());
        } else {
            bundle.putString("shopId", "");
        }
        putDarkModeValue(bundle);
        putNetworkSpeed(bundle);

        if (customDimension != null) {
            for (String key : customDimension.keySet()) {
                if (customDimension.get(key) != null) {
                    bundle.putString(key, customDimension.get(key));
                }
            }
        }

        pushEventV5("openScreen", wrapWithSessionIris(bundle), context);
        iris.saveEvent(bundleToMap(bundle));
    }

    private void putDarkModeValue(Bundle bundle) {
        boolean isDarkMode = sharedPreferences.getBoolean(TkpdCache.Key.KEY_DARK_MODE, false);
        if (isDarkMode) {
            bundle.putString("theme", "dark");
        } else {
            bundle.putString("theme", "light");
        }
    }

    public void putNetworkSpeed(Bundle bundle) {
        if (TextUtils.isEmpty(connectionTypeString) ||
                (System.currentTimeMillis() - lastGetConnectionTimeStamp > DELAY_GET_CONN)) {
            connectionTypeString = DeviceConnectionInfo.getConnectionType(context);
            lastGetConnectionTimeStamp = System.currentTimeMillis();
        }
        bundle.putString("networkSpeed", connectionTypeString);
    }

    public void pushEvent(String eventName, Map<String, Object> values) {
        Map<String, Object> data = new HashMap<>(values);
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(it -> {
                    log(getContext(), eventName, it);
                    pushIris(eventName, it);
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    @Override
    public void sendGTMGeneralEvent(String event, String category, String action, String label,
                                    String shopId, String shopType, String userId,
                                    @Nullable Map<String, Object> customDimension) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EVENT, event);
        map.put(KEY_CATEGORY, category);
        map.put(KEY_ACTION, action);
        map.put(KEY_LABEL, label);
        map.put(USER_ID, userId);
        map.put(SHOP_TYPE, shopType);
        map.put(SHOP_ID, shopId);
        if (customDimension != null) {
            map.putAll(customDimension);
        }
        pushGeneral(map);
    }

    private void logV5(Context context, String eventName, Bundle bundle) {
        log(context, eventName, bundleToMap(bundle), true);
    }

    private void log(Context context, String eventName, Map<String, Object> values) {
        log(context, eventName, values, false);
    }

    private void log(Context context, String eventName, Map<String, Object> values, boolean isGtmV5) {
        // fix Caused by java.lang.NoSuchMethodError
        try {
            String name = eventName == null ? (String) values.get("event") : eventName;
            if (!isGtmV5) name += " (legacy_v4)";
            String source = (isGtmV5) ? AnalyticsSource.GTM : AnalyticsSource.LEGACY_GTM;
            Cassava.save(values, name, source);
            logEventSize(eventName, values);
            logEventForVerification(eventName, values);
        } catch (Exception e) {
            Timber.w(e);
        }
    }

    private long getGTMSizeLogThreshold() {
        if (gtmSizeThresholdLog == 0) {
            gtmSizeThresholdLog = remoteConfig.getLong(GTM_SIZE_LOG_REMOTE_CONFIG_KEY,
                    GTM_SIZE_LOG_THRESHOLD_DEFAULT);
        }
        return gtmSizeThresholdLog;
    }

    private void logEventSize(String eventName, Map<String, Object> values) {
        int size = values.toString().length();
        if (size < getGTMSizeLogThreshold()) {
            return;
        }
        String eventCategory = (String) values.get("eventCategory");
        if (!TextUtils.isEmpty(eventCategory)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "event_cat");
            messageMap.put("name", eventName);
            messageMap.put("size", String.valueOf(size));
            messageMap.put("value", eventCategory);
            ServerLogger.log(Priority.P1, "GTM_SIZE", messageMap);
            return;
        }
        String screenName = (String) values.get("screenName");
        if (!TextUtils.isEmpty(screenName)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "event_screen");
            messageMap.put("name", eventName);
            messageMap.put("size", String.valueOf(size));
            messageMap.put("value", screenName);
            ServerLogger.log(Priority.P1, "GTM_SIZE", messageMap);
            return;
        }
        String pageType = (String) values.get("pageType");
        if (!TextUtils.isEmpty(pageType)) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "event_screen");
            messageMap.put("name", eventName);
            messageMap.put("size", String.valueOf(size));
            messageMap.put("value", pageType);
            ServerLogger.log(Priority.P1, "GTM_SIZE", messageMap);
            return;
        }
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "event_others");
        messageMap.put("name", eventName);
        messageMap.put("size", String.valueOf(size));
        ServerLogger.log(Priority.P1, "GTM_SIZE", messageMap);
    }

    private void logEventForVerification(String eventName, Map<String, Object> values) {
        if (remoteConfig.getBoolean(ANDROID_GA_EVENT_LOGGING)) {
            if (values.containsKey(AppEventTracking.GTM.UTM_SOURCE) &&
                    values.get(AppEventTracking.GTM.UTM_SOURCE) != null &&
                    !TextUtils.isEmpty(values.get(AppEventTracking.GTM.UTM_SOURCE).toString())) {
                Map<String, String> messageMap = new HashMap<>();
                messageMap.put("type", "event_verification");
                messageMap.put("name", eventName);
                messageMap.put("click_time", String.valueOf((System.currentTimeMillis() / 1000L)));
                messageMap.put("clientId", TrackApp.getInstance().getGTM().getClientIDString());
                messageMap.put("utm_source", values.get(AppEventTracking.GTM.UTM_SOURCE).toString());
                messageMap.put("utm_medium", values.get(AppEventTracking.GTM.UTM_MEDIUM).toString());
                messageMap.put("campaign", values.get(AppEventTracking.GTM.UTM_CAMPAIGN).toString());
                ServerLogger.log(Priority.P1, "GA_EVENT_VERIFICATION", messageMap);
            }
        }
    }

    public void sendScreenAuthenticated(String screenName) {
        if (TextUtils.isEmpty(screenName)) return;
        sendScreen(screenName, null);
    }

    public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {
        if (TextUtils.isEmpty(screenName)) return;
        sendScreen(screenName, customDimension);
    }

    public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {
        if (TextUtils.isEmpty(screenName)) return;
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(Authenticated.KEY_SHOP_ID_SELLER, shopID);
        customDimension.put(Authenticated.KEY_PAGE_TYPE, pageType);
        customDimension.put(Authenticated.KEY_SHOP_TYPE, shopType);
        customDimension.put(Authenticated.KEY_PRODUCT_ID, productId);
        sendScreen(screenName, customDimension);
    }

    @Override
    public void sendEvent(String eventName, Map<String, Object> eventValue) {
        //no op, only for appsfyler and moengage
    }

    private void pushEECommerceInternal(String keyEvent, Bundle bundle) {
        // replace list
        if (TextUtils.isEmpty(bundle.getString(FirebaseAnalytics.Param.ITEM_LIST))
                && !TextUtils.isEmpty(bundle.getString("list"))) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
            bundle.remove("list");
        }

        switch (keyEvent.toLowerCase()) {
            case PRODUCTVIEW:
                String itemListString = bundle.getString(FirebaseAnalytics.Param.ITEM_LIST);
                if (TextUtils.isEmpty(bundle.getString(FirebaseAnalytics.Param.ITEM_LIST_NAME))){
                    bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, itemListString);
                }
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM_LIST;
                break;
            case PROMOCLICK:
            case PRODUCTCLICK:
                keyEvent = FirebaseAnalytics.Event.SELECT_CONTENT;
                break;
            case PROMOVIEW:
            case VIEWPRODUCT:
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM;
                break;
            case ADDTOCART:
                keyEvent = FirebaseAnalytics.Event.ADD_TO_CART;
                break;
            case BEGINCHECKOUT:
                keyEvent = FirebaseAnalytics.Event.BEGIN_CHECKOUT;
                break;
            case CHECKOUT_PROGRESS:
                keyEvent = FirebaseAnalytics.Event.CHECKOUT_PROGRESS;
                break;
            case TRANSACTION:
                keyEvent = FirebaseAnalytics.Event.ECOMMERCE_PURCHASE;
                break;
        }
        //
        bundle.putString(KEY_EVENT, keyEvent);
        pushEventV5(keyEvent, wrapWithSessionIris(bundle), context);
    }

    @Override
    public void sendCampaign(Activity activity,
                             String campaignUrl,
                             String screenName,
                             boolean isOriginalUrlAmp) {
        Campaign campaign = DeeplinkUTMUtils.convertUrlCampaign(activity, Uri.parse(campaignUrl), isOriginalUrlAmp);
        if (!TrackingUtils.isValidCampaign(campaign.getCampaign())) return;

        campaign.setScreenName(screenName);

        // V5
        sendCampaign(campaign.getCampaign());

        // v4
        pushEvent("campaignTrack", campaign.getCampaign());
        sendGeneralEvent(campaign.getNullCampaignMap());

        sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CAMPAIGN,
                AppEventTracking.Category.CAMPAIGN,
                AppEventTracking.Action.DEEPLINK,
                campaignUrl
        ).getEvent());
    }

    public void sendCampaign(Map<String, Object> param) {
        if (!TrackingUtils.isValidCampaign(param)) return;

        Bundle bundle = new Bundle();
        String afUniqueId = getAfUniqueId(context);

        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString("userId", userSession.getUserId());
        bundle.putString(CLIENT_ID, getClientIDString());
        bundle.putString(KEY_EVENT, CAMPAIGN_TRACK);
        bundle.putString("screenName", (String) param.get("screenName"));


        // AN-23730
        Object xClid = param.get(AppEventTracking.GTM.X_CLID);
        if (xClid != null && xClid instanceof String) {
            String xClid_ = (String) xClid;
            bundle.putString(AppEventTracking.GTM.X_CLID, xClid_);
        }


        String gclid = (String) param.get(AppEventTracking.GTM.UTM_GCLID);
        if (!TextUtils.isEmpty(gclid)) {
            bundle.putString("gclid", gclid);
            mGclid = gclid;
        }
        bundle.putString("utmSource", (String) param.get(AppEventTracking.GTM.UTM_SOURCE));
        bundle.putString("utmMedium", (String) param.get(AppEventTracking.GTM.UTM_MEDIUM));
        bundle.putString("utmCampaign", (String) param.get(AppEventTracking.GTM.UTM_CAMPAIGN));
        bundle.putString("utmContent", (String) param.get(AppEventTracking.GTM.UTM_CAMPAIGN));

        /**
         * if the utm_term equals {@value IM_CLICK_ID}
         */
        if (String.valueOf(param.get(AppEventTracking.GTM.UTM_TERM)).equals(IM_CLICK_ID)) {
            bundle.putString("utmTerm", DateUtil.getCurrentTime());
        } else {
            bundle.putString("utmTerm", (String) param.get(AppEventTracking.GTM.UTM_TERM));
        }

        bundle = wrapWithSessionIris(bundle);

        //v5
        pushEventV5("campaignTrack", bundle, context);

        //v4
        pushEvent("campaignTrack", bundleToMap(bundle));
    }

    public void pushGeneralGtmV5Internal(Map<String, Object> params) {
        Observable.fromCallable(() -> pushGeneralGtmV5InternalOrigin(params))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(getDefaultSubscriber());
    }

    private boolean pushGeneralGtmV5InternalOrigin(Map<String, Object> params) {
        pushGeneral(params);

        if (TextUtils.isEmpty((String) params.get(KEY_EVENT)))
            return false;

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, params.get(KEY_CATEGORY) + "");
        bundle.putString(KEY_ACTION, params.get(KEY_ACTION) + "");
        bundle.putString(KEY_LABEL, params.get(KEY_LABEL) + "");
        bundle.putString(KEY_EVENT, params.get(KEY_EVENT) + "");

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (!Arrays.asList(GENERAL_EVENT_KEYS).contains(entry.getKey()))
                bundle.putString(entry.getKey(), bruteForceCastToString(entry.getValue()));
        }

        pushEventV5(params.get(KEY_EVENT) + "", wrapWithSessionIris(bundle), context);
        return true;
    }

    public Bundle wrapWithSessionIris(Bundle bundle) {
        // AN-18166
        // globally put sessionIris
        String sessionIris = bundle.getString(SESSION_IRIS);
        if (TextUtils.isEmpty(sessionIris)) {
            bundle.putString(SESSION_IRIS, iris.getSessionId());
        }
        return bundle;
        // end of globally put sessionIris
    }

    @SuppressLint("MissingPermission")
    public void pushEventV5(String eventName, Bundle bundle, Context context) {
        try {
            if (!CommonUtils.checkStringNotNull(bundle.getString(SESSION_IRIS))) {
                bundle.putString(SESSION_IRIS, new IrisSession(context).getSessionId());
            }
            publishNewRelic(eventName, bundle);
            FirebaseAnalytics fa = FirebaseAnalytics.getInstance(context);
            fa.logEvent(eventName, bundle);
            mappingToGA4(fa, eventName, bundle);
            logV5(context, eventName, bundle);
            trackEmbraceBreadcrumb(eventName, bundle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // https://firebase.google.com/docs/analytics/measure-ecommerce
    private void mappingToGA4(FirebaseAnalytics fa, String eventName, Bundle bundle) {
        try {
            switch (eventName) {
                // https://tokopedia.atlassian.net/browse/AN-36119
                // view_item -> view_promotion
                case FirebaseAnalytics.Event.VIEW_ITEM:
                    Bundle bundleGA4 = createBundleGA4BundleViewPromotion(bundle);
                    if (bundleGA4 != null) {
                        fa.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, bundleGA4);
                    }
                    break;
                // select_content -> select_promotion / select_item
                case FirebaseAnalytics.Event.SELECT_CONTENT:
                    sendBundleGA4BundleClick(fa, bundle);
                    break;
                // checkout_progress -> begin_checkout
                case FirebaseAnalytics.Event.CHECKOUT_PROGRESS:
                    // https://tokopedia.atlassian.net/browse/AN-36179
                    // https://tokopedia.atlassian.net/browse/AN-36180
                    fa.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, bundle);
                    break;
                // view_search_results -> view_item_list
                case FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS:
                    // https://tokopedia.atlassian.net/browse/AN-36125
                    createBundleGA4BundleViewItemList(fa, bundle);
                    break;
                // ecommerce_purchase -> purchase
                case FirebaseAnalytics.Event.ECOMMERCE_PURCHASE:
                    // https://tokopedia.atlassian.net/browse/AN-36181
                    fa.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createBundleGA4BundleViewItemList(FirebaseAnalytics fa, Bundle oriBundle) {
        Bundle ecommerceBundleGA4 = new Bundle();
        copyBundleStringRoot(oriBundle, ecommerceBundleGA4);
        fa.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, ecommerceBundleGA4);
    }

    private void sendBundleGA4BundleClick(FirebaseAnalytics fa, Bundle oriBundle) {
        String eventCategory = oriBundle.getString(KEY_CATEGORY);
        boolean isPromotion = true;
        if ((eventCategory != null && eventCategory.contains("search result")) ||
                oriBundle.get(KEY_PROMOTIONS) == null) {
            isPromotion = false;
        }
        if (isPromotion) {
            // https://tokopedia.atlassian.net/browse/AN-36122
            Bundle gA4bundle = createBundleGA4BundleClickPromotion(oriBundle);
            if (!gA4bundle.isEmpty()) {
                fa.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, gA4bundle);
            }
        } else {
            // https://tokopedia.atlassian.net/browse/AN-36131
            Bundle gA4bundle = createBundleGA4BundleProductClick(oriBundle);
            fa.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, gA4bundle);
        }
    }

    private void copyBundleString(Bundle oriBundle, Bundle targetBundle) {
        // copy all key in original bundle to gav4 bundle, except for promotions
        for (String key : oriBundle.keySet()) {
            try {
                if (oriBundle.get(key) instanceof String) {
                    targetBundle.putString(key, oriBundle.get(key).toString());
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void copyBundleStringRoot(Bundle oriBundle, Bundle targetBundle) {
        // copy all key in original bundle to gav4 bundle, except for promotions
        // and if there is item_list, will be converted to item_list_name in GAv4
        for (String key : oriBundle.keySet()) {
            try {
                String targetkey;
                if (key.equals(ITEM_LIST)) {
                    targetkey = FirebaseAnalytics.Param.ITEM_LIST_NAME;
                } else {
                    targetkey = key;
                }
                Object value = oriBundle.get(key);
                if (value instanceof String) {
                    targetBundle.putString(targetkey, value.toString());
                }
            } catch (Exception ignored) {
            }
        }
    }

    @SuppressLint("PII Data Exposure")
    private Bundle createBundleGA4BundleViewPromotion(Bundle oriBundle) {
        Object promotions = oriBundle.get(KEY_PROMOTIONS);
        if (promotions != null) {
            List promotionList = (List) promotions;
            ArrayList<Bundle> promotionsGA4 = new ArrayList<>();
            String firstItemList = "";
            for (int i = 0, size = promotionList.size(); i < size; i++) {
                Bundle bundlePromotion = ((Bundle) promotionList.get(i));
                String itemId = bundlePromotion.getString(ITEM_ID);
                Bundle newBundle = new Bundle();
                copyBundleString(bundlePromotion, newBundle);
                if (itemId != null) {
                    newBundle.putString(FirebaseAnalytics.Param.PROMOTION_ID, itemId);
                }
                String itemName = bundlePromotion.getString(ITEM_NAME);
                if (itemName != null) {
                    newBundle.putString(FirebaseAnalytics.Param.PROMOTION_NAME, itemName);
                    if (TextUtils.isEmpty(firstItemList)) {
                        firstItemList = itemName;
                    }
                }
                promotionsGA4.add(newBundle);
            }
            Bundle ecommerceBundleGA4 = new Bundle();
            copyBundleStringRoot(oriBundle, ecommerceBundleGA4);
            ecommerceBundleGA4.putParcelableArrayList(ITEMS, promotionsGA4);
            if (TextUtils.isEmpty(ecommerceBundleGA4.getString(FirebaseAnalytics.Param.ITEM_LIST_NAME))) {
                ecommerceBundleGA4.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, firstItemList);
            }
            return ecommerceBundleGA4;
        } else {
            return null;
        }
    }

    private Bundle createBundleGA4BundleClickPromotion(Bundle oriBundle) {
        Object promotions = oriBundle.get(KEY_PROMOTIONS);
        List promotionList = (List) promotions;
        Bundle ecommerceBundleGA4 = new Bundle();

        if (promotionList.size() > 0) {
            Bundle bundlePromotion = ((Bundle) promotionList.get(0));
            String itemId = bundlePromotion.getString(ITEM_ID);
            if (itemId != null) {
                ecommerceBundleGA4.putString(FirebaseAnalytics.Param.PROMOTION_ID, itemId);
            }
            String itemName = bundlePromotion.getString(ITEM_NAME);
            if (itemName != null) {
                ecommerceBundleGA4.putString(FirebaseAnalytics.Param.PROMOTION_NAME, itemName);
            }
            String creativeName = bundlePromotion.getString(CREATIVE_NAME);
            if (creativeName != null) {
                ecommerceBundleGA4.putString(CREATIVE_NAME, creativeName);
            }
            String creativeSlot = bundlePromotion.getString(CREATIVE_SLOT);
            if (creativeSlot != null) {
                ecommerceBundleGA4.putString(CREATIVE_SLOT, creativeSlot);
            }
            copyBundleStringRoot(oriBundle, ecommerceBundleGA4);
        }
        return ecommerceBundleGA4;
    }

    private Bundle createBundleGA4BundleProductClick(Bundle oriBundle) {
        Object promotions = oriBundle.get(KEY_PROMOTIONS);
        Bundle ecommerceBundleGA4 = new Bundle();
        copyBundleStringRoot(oriBundle, ecommerceBundleGA4);
        if (promotions != null) {
            List promotionList = (List) promotions;
            ArrayList<Bundle> promotionsGA4 = new ArrayList<>();
            for (int i = 0, size = promotionList.size(); i < size; i++) {
                Bundle bundlePromotion = ((Bundle) promotionList.get(i));
                String itemId = bundlePromotion.getString(ITEM_ID);
                if (itemId != null) {
                    bundlePromotion.putString(FirebaseAnalytics.Param.PROMOTION_ID, itemId);
                }
                String itemName = bundlePromotion.getString(ITEM_NAME);
                if (itemName != null) {
                    bundlePromotion.putString(FirebaseAnalytics.Param.PROMOTION_NAME, itemName);
                }
                promotionsGA4.add(bundlePromotion);
            }
            ecommerceBundleGA4.putParcelableArrayList(ITEMS, promotionsGA4);
            return ecommerceBundleGA4;
        } else {
            ArrayList item = (ArrayList) oriBundle.get(ITEMS);
            if (item != null) {
                ecommerceBundleGA4.putParcelableArrayList(ITEMS, item);
            }
            return ecommerceBundleGA4;
        }
    }

    private void trackEmbraceBreadcrumb(String eventName, Bundle bundle) {
        String logEmbraceConfigString = remoteConfig.getString(RemoteConfigKey.ANDROID_EMBRACE_CONFIG);
        try {
            EmbraceConfig config =
                    new Gson().fromJson(logEmbraceConfigString, EmbraceConfig.class);
            if (bundle.containsKey(KEY_CATEGORY)) {
                String eventCategoryValue = bundle.getString(KEY_CATEGORY);
                if (config.getBreadcrumb_categories().contains(eventCategoryValue)) {
                    EmbraceMonitoring.INSTANCE.logBreadcrumb(
                            String.format(
                                    EMBRACE_BREADCRUMB_FORMAT,
                                    EMBRACE_KEY,
                                    createJsonFromBundle(eventName, bundle)
                            )
                    );
                }
            }
        } catch (Exception e) {
        }
    }

    private JSONObject createJsonFromBundle(String eventName, Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        try {
            json.put(EMBRACE_EVENT_NAME, eventName);

            if (bundle.containsKey(KEY_ACTION)) {
                String action = bundle.getString(KEY_ACTION);
                json.put(EMBRACE_EVENT_ACTION, action);
            }

            if (bundle.containsKey(KEY_LABEL)) {
                String label = bundle.getString(KEY_LABEL);
                json.put(EMBRACE_EVENT_LABEL, label);
            }
        } catch (JSONException e) {
            //Handle exception here
        }
        return json;
    }

    public void publishNewRelic(String eventName, Bundle bundle) {
        Map<String, Object> map = bundleToMap(bundle);
        for (Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            Object value = entry.getValue();
            if (value != null & value instanceof String) {
                String value2 = (String) value;
                if (TextUtils.isEmpty(value2)) {
                    it.remove();
                }
            }
        }
        if (GlobalConfig.isSellerApp()) {
            NewRelicUtil.sendTrack(eventName, map);
        }
    }

    private void pushGeneral(Map<String, Object> values) {
        Map<String, Object> data = new HashMap<>(values);
        // push Iris already launch in coroutine in background. No need to wrap this with Observable.
        pushIris("", data);
    }

    private void pushGeneralEcommerce(Map<String, Object> values) {
        Map<String, Object> data = new HashMap<>(values);
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(it -> {
                    if (!TextUtils.isEmpty(mGclid)) {
                        if (it.get("event") != null) {
                            String eventName = String.valueOf(it.get("event"));
                            addGclIdIfNeeded(eventName, it);
                        }
                    }
                    it.put(AppEventTracking.GTM.UTM_MEDIUM, UTM_MEDIUM_HOLDER);
                    it.put(AppEventTracking.GTM.UTM_CAMPAIGN, UTM_CAMPAIGN_HOLDER);
                    it.put(AppEventTracking.GTM.UTM_SOURCE, UTM_SOURCE_HOLDER);
                    pushIris("", it);
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    private void addGclIdIfNeeded(String eventName, Map<String, Object> values) {
        if (null == eventName) return;
        switch (eventName.toLowerCase()) {
            case FirebaseAnalytics.Event.ADD_TO_CART:
            case ADDTOCART:
            case FirebaseAnalytics.Event.VIEW_ITEM:
            case VIEWPRODUCT:
            case PRODUCTVIEW:
            case FirebaseAnalytics.Event.ECOMMERCE_PURCHASE:
            case TRANSACTION:
                values.put(KEY_GCLID, mGclid);
        }
    }

    private Bundle addGclIdIfNeeded(String eventName, Bundle values) {
        if (mGclid.isEmpty() || null == eventName) return values;
        switch (eventName.toLowerCase()) {
            case FirebaseAnalytics.Event.ADD_TO_CART:
            case ADDTOCART:
            case FirebaseAnalytics.Event.VIEW_ITEM:
            case VIEWPRODUCT:
            case PRODUCTVIEW:
            case FirebaseAnalytics.Event.ECOMMERCE_PURCHASE:
            case TRANSACTION:
                values.putString(KEY_GCLID, mGclid);
        }
        return values;
    }

    public void eventOnline(String uid) {
        pushEvent(
                "onapps", DataLayer.mapOf("LoginId", uid));
    }

    public void event(String name, Map<String, Object> data) {
        pushEvent(name, data);
    }

    /**
     * ada skema di gtm yang nge-cache nah ini gimana?
     */
    public void clearEnhanceEcommerce() {
        // no op. push Iris send null is always ignored.
    }

    private void pushIris(String eventName, Map<String, Object> values) {
        if (iris != null) {
            if (!eventName.isEmpty()) {
                values.put("event", eventName);
            }
            if (values.get("event") != null && !String.valueOf(values.get("event")).equals("")) {
                iris.saveEvent(values);
            }
        }
    }

    private void pushIris(String eventName, Bundle values) {
        if (iris != null) {
            if (!eventName.isEmpty()) {
                values.putString("event", eventName);
            }
            if (values.get("event") != null && !String.valueOf(values.get("event")).equals("")) {
                iris.saveEvent(values);
            }
        }
    }

    private Subscriber<Boolean> getDefaultSubscriber() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean ignored) {

            }
        };
    }

    public static class PurchaseKey {
        public static final String KEY_SHIPPING = "shipping";
        public static final String KEY_COUPON = "coupon";
        private static final String KEY_ID = "id";
        private static final String KEY_AFFILIATION = "affiliation";
        private static final String KEY_REVENUE = "revenue";
        private static final String KEY_TAX = "tax";
    }

    public static class CheckoutKey {

        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_PRICE = "price";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_CAT = "category";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_QTY = "quantity";
    }

    private static class ATCKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_PRICE = "price";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_CAT = "category";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_QTY = "quantity";
    }

    private static class ProductKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_PRICE = "price";
        private static final String KEY_BRAND = "brand";
        private static final String KEY_CAT = "category";
        private static final String KEY_VARIANT = "variant";
        private static final String KEY_POSITION = "position";
        private static final String KEY_LIST = "list";
    }

    private static class ViewProductResult {
        public Bundle first;
        public String list;

        public ViewProductResult(Bundle first, String list) {
            this.first = first;
            this.list = list;
        }
    }

    private static class PromotionKey {
        private static final String KEY_NAME = "name";
        private static final String KEY_ID = "id";
        private static final String KEY_CREATIVE = "creative";
        private static final String KEY_POSITION = "position";
    }
}
