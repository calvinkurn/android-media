package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.analytics.TrackingUtils.getAfUniqueId;

public class GTMAnalytics extends ContextAnalytics {
    private static final String TAG = GTMAnalytics.class.getSimpleName();
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = TimeUnit.MINUTES.toMillis(150); // 150 minutes (2.5 hours)

    private static final String KEY_EVENT = "event";
    private static final String KEY_CATEGORY = "eventCategory";
    private static final String KEY_ACTION = "eventAction";
    private static final String KEY_LABEL = "eventLabel";
    private static final String USER_ID = "userId";
    private static final String SHOP_ID = "shopId";
    private static final String SHOP_TYPE = "shopType";
    private final Iris iris;
    private final RemoteConfig remoteConfig;

    // have status that describe pending.

    public GTMAnalytics(Context context) {
        super(context);
        iris = IrisAnalytics.Companion.getInstance(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        pushGeneral(value);
    }

    @Override
    public void sendGeneralEvent(String event, String category, String action, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EVENT, event);
        map.put(KEY_CATEGORY, category);
        map.put(KEY_ACTION, action);
        map.put(KEY_LABEL, label);
        pushGeneral(map);
    }

    @Override
    public void sendEnhanceEcommerceEvent(Map<String, Object> value) {
        clearEnhanceEcommerce();
        pushGeneral(value);
    }

    public TagManager getTagManager() {
        return TagManager.getInstance(getContext());
    }


    public String getClientIDString() {
        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA).metaData;
            return GoogleAnalytics.getInstance(getContext()).newTracker(bundle.getString(AppEventTracking.GTM.GA_ID)).get("&cid");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        try {
            Bundle bundle = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = getTagManager();
            PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));

            pResult.setResultCallback(cHolder -> {
                cHolder.setContainerAvailableListener((containerHolder, s) -> {
                    if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GTM_REFRESH, true)) {
                        if (isAllowRefreshDefault(containerHolder)) {
                            Log.d("GTM TKPD", "Refreshed Container ");
                            containerHolder.refresh();
                        }
                    }
                });
            }, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            eventError(getContext().getClass().toString(), e.toString());
        }
    }

    private Boolean isAllowRefreshDefault(ContainerHolder containerHolder) {
        long lastRefresh = 0;
        if (containerHolder.getContainer() != null) {
            lastRefresh = containerHolder.getContainer().getLastRefreshTime();
        }
        return System.currentTimeMillis() - lastRefresh > EXPIRE_CONTAINER_TIME_DEFAULT;
    }

    public void eventError(String screenName, String errorDesc) {

    }

    public void sendScreen(String screenName) {
        pushEvent("openScreen", DataLayer.mapOf("screenName", screenName));
    }

    public void pushEvent(String eventName, Map<String, Object> values) {
        Map<String, Object> data = new HashMap<>(values);
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(it -> {
                    log(getContext(), eventName, it);
                    getTagManager().getDataLayer().pushEvent(eventName, it);
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
        if (customDimension!= null) {
            map.putAll(customDimension);
        }
        pushGeneral(map);
    }

    private static void log(Context context, String eventName, Bundle bundle) {
        log(context, eventName, bundleToMap(bundle));
    }

    private static void log(Context context, String eventName, Map<String, Object> values) {
        String name = eventName == null ? (String) values.get("event") : eventName;
        GtmLogger.getInstance(context).save(name, values);
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

    public GTMAnalytics sendCampaign(Campaign campaign) {
        pushEvent("campaignTrack", campaign.getCampaign());
        return this;
    }

    public GTMAnalytics clearCampaign(Campaign campaign) {
        pushGeneral(campaign.getNullCampaignMap());
        return this;
    }

    public GTMAnalytics eventCheckout(Checkout checkout, String paymentId) {

        pushGeneral(
                DataLayer.mapOf(
                        AppEventTracking.EVENT, AppEventTracking.Event.EVENT_CHECKOUT,
                        AppEventTracking.PAYMENT_ID, paymentId,
                        AppEventTracking.EVENT_CATEGORY, AppEventTracking.Category.ECOMMERCE,
                        AppEventTracking.EVENT_ACTION, AppEventTracking.Action.CHECKOUT,
                        AppEventTracking.EVENT_LABEL, checkout.getStep(),
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                AppEventTracking.Event.EVENT_CHECKOUT, checkout.getCheckoutMapEvent()
                        )));

        return this;
    }

    /**
     * look identical with {@link #eventCheckout(Checkout, String)}
     * @param checkout
     * @return
     */
    public GTMAnalytics eventCheckout(Checkout checkout) {

        pushGeneral(
                DataLayer.mapOf(
                        AppEventTracking.EVENT, AppEventTracking.Event.EVENT_CHECKOUT,
                        AppEventTracking.EVENT_CATEGORY, AppEventTracking.Category.ECOMMERCE,
                        AppEventTracking.EVENT_ACTION, AppEventTracking.Action.CHECKOUT,
                        AppEventTracking.EVENT_LABEL, checkout.getStep(),
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                AppEventTracking.Event.EVENT_CHECKOUT, checkout.getCheckoutMapEvent()
                        )));

        return this;
    }

    public void clearCheckoutDataLayer() {
        pushGeneral(DataLayer.mapOf("step", null, "products", null,
                "currencyCode", null, "actionField", null, "ecommerce", null));
    }

    public void sendScreenAuthenticated(String screenName) {
        if (TextUtils.isEmpty(screenName)) return;
        eventAuthenticate(null);
        sendScreen(screenName);
    }

    public void sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {
        if (TextUtils.isEmpty(screenName)) return;
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
    }

    public void sendScreenAuthenticated2(String screenName, String shopID, String shopType, String pageType, String productId) {
        if (TextUtils.isEmpty(screenName)) return;
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(Authenticated.KEY_SHOP_ID_SELLER, shopID);
        customDimension.put(Authenticated.KEY_PAGE_TYPE, pageType);
        customDimension.put(Authenticated.KEY_SHOP_TYPE, shopType);
        customDimension.put(Authenticated.KEY_PRODUCT_ID, productId);
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
    }

    public void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {
        if (TextUtils.isEmpty(screenName)) return;
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(Authenticated.KEY_SHOP_ID_SELLER, shopID);
        customDimension.put(Authenticated.KEY_PAGE_TYPE, pageType);
        customDimension.put(Authenticated.KEY_SHOP_TYPE, shopType);
        customDimension.put(Authenticated.KEY_PRODUCT_ID, productId);
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
    }

    @Override
    public void sendEvent(String eventName, Map<String, Object> eventValue) {
        //no op, only for appsfyler and moengage
    }

    public void eventAuthenticate() {
        eventAuthenticate(null);
    }

    public void eventAuthenticate(Map<String, String> customDimension) {
        String afUniqueId = getAfUniqueId(context);
        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        Map<String, Object> map = DataLayer.mapOf(
                Authenticated.KEY_CONTACT_INFO, DataLayer.mapOf(
                        Authenticated.KEY_USER_SELLER, (sessionHandler.isUserHasShop() ? 1 : 0),
                        Authenticated.KEY_USER_FULLNAME, sessionHandler.getLoginName(),
                        Authenticated.KEY_USER_ID, sessionHandler.getGTMLoginID(),
                        Authenticated.KEY_SHOP_ID, sessionHandler.getShopID(),
                        Authenticated.KEY_AF_UNIQUE_ID, (afUniqueId != null ? afUniqueId : "none"),
                        Authenticated.KEY_USER_EMAIL, sessionHandler.getEmail()
                ),
                Authenticated.ANDROID_ID, sessionHandler.getAndroidId(),
                Authenticated.ADS_ID, sessionHandler.getAdsId(),
                Authenticated.GA_CLIENT_ID, getClientIDString()
        );
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        pushEvent(Authenticated.KEY_CD_NAME, map);
    }

    public void sendScreen(String screenName, Map<String, String> customDimension) {
        Map<String, Object> map = DataLayer.mapOf("screenName", screenName);
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        pushEvent("openScreen", map);
    }

    public GTMAnalytics eventAddtoCart(GTMCart cart) {
        pushEvent( "addToCart", DataLayer.mapOf("ecommerce", cart.getCartMap()));
        return this;
    }

    public GTMAnalytics clearAddtoCartDataLayer(String act) {
        pushGeneral(DataLayer.mapOf("products", null,
                "currencyCode", null, "addToCart", null, "ecommerce", null, act, null));
        return this;
    }

    public void pushClickEECommerce(Bundle bundle){
        // replace list
        if (TextUtils.isEmpty(bundle.getString(FirebaseAnalytics.Param.ITEM_LIST))
                && !TextUtils.isEmpty(bundle.getString("list"))) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
            bundle.remove("list");
        }
        logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle, context);
    }

    private static final String PRODUCTVIEW = "productview";
    private static final String PRODUCTCLICK = "productclick";
    private static final String VIEWPRODUCT = "viewproduct";
    private static final String ADDTOCART = "addtocart";

    public void pushEECommerce(String keyEvent, Bundle bundle){
        // replace list
        if (TextUtils.isEmpty(bundle.getString(FirebaseAnalytics.Param.ITEM_LIST))
                && !TextUtils.isEmpty(bundle.getString("list"))) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
            bundle.remove("list");
        }

        switch (keyEvent.toLowerCase()){
            case PRODUCTVIEW:
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM_LIST;
                break;
            case PRODUCTCLICK:
                keyEvent = FirebaseAnalytics.Event.SELECT_CONTENT;
                break;
            case VIEWPRODUCT:
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM;
                break;
            case ADDTOCART:
                keyEvent = FirebaseAnalytics.Event.ADD_TO_CART;
                break;

        }
        logEvent(keyEvent, bundle, context);
    }

    public void pushGeneralGtmV5(Map<String, Object> params){
        sendGeneralEvent(params);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, params.get(KEY_CATEGORY) + "");
        bundle.putString(KEY_ACTION, params.get(KEY_ACTION) + "");
        bundle.putString(KEY_LABEL, params.get(KEY_LABEL) + "");

        logEvent(params.get(KEY_EVENT) + "", bundle, context);
    }

    public void pushGeneralGtmV5(String event, String category, String action, String label){
        sendGeneralEvent(event, category, action, label);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category);
        bundle.putString(KEY_ACTION, action);
        bundle.putString(KEY_LABEL, label);

        logEvent(event, bundle, context);
    }

    public void sendScreenV5(String screenName) {
        sendScreenV5(screenName, new HashMap<>());
    }

    public void sendScreenV5(String screenName, Map<String, String> additionalParams) {
        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        final String afUniqueId = !TextUtils.isEmpty(getAfUniqueId(context)) ? getAfUniqueId(context) : "none";

        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString("userId", sessionHandler.getLoginID());
        bundle.putString("clientId", getClientIDString());

        for(String key : additionalParams.keySet()) {
            if (additionalParams.get(key) != null) {
                bundle.putString(key, additionalParams.get(key));
            }
        }

        logEvent("openScreen", bundle, context);
    }

    public static void logEvent(String eventName, Bundle bundle,Context context){
        try {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
            log(context, eventName, bundle);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void pushGeneral(Map<String, Object> values) {
        Map<String, Object> data = new HashMap<>(values);
        Observable.just(data)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(it -> {
                    log(getContext(), null, it);
                    TagManager.getInstance(getContext()).getDataLayer().push(it);
                    pushIris("", it);
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    public void pushUserId(String userId) {
        Observable.just(userId)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(uid -> {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("user_id", uid);
                    getTagManager().getDataLayer().push(maps);
                    return true;
                })
                .subscribe(getDefaultSubscriber());
    }

    public void eventLogAnalytics(String screenName, String errorDesc) {
        Log.d(TAG, "Sending Push Event Error");
        pushEvent( "trackException", DataLayer.mapOf(
                "screenName", screenName,
                "exception.description", errorDesc,
                "exception.isFatal", "true"));
    }

    public GTMAnalytics eventDetail(ProductDetail detail) {
        pushGeneral( DataLayer.mapOf("ecommerce", DataLayer.mapOf(
                "detail", detail.getDetailMap()
        )));

        return this;
    }

    public void eventOnline(String uid) {
        pushEvent(
                "onapps", DataLayer.mapOf("LoginId", uid));
    }

    public GTMAnalytics sendEvent(Map<String, Object> events) {
        pushGeneral(events);
        return this;
    }

    public void event(String name, Map<String, Object> data) {
        pushEvent( name, data);
    }

    /**
     * ada skema di gtm yang nge-cache nah ini gimana?
     */
    public void clearEnhanceEcommerce() {
        pushGeneral(
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null,
                        "products", null,
                        "promotions", null,
                        "ecommerce", null,
                        "currentSite", null,
                        "channelId", null
                )
        );
    }

    private void clearEventTracking() {
        pushGeneral(
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null
                )
        );
    }

    private void pushIris(String eventName, Map<String, Object>values) {
        if (iris != null) {
            if (!eventName.isEmpty()) {
                values.put("event", eventName);
            }
            if(values.get("event") != null && !String.valueOf(values.get("event")).equals("")) {
                iris.saveEvent(values);
            }
        }
    }

    private static class GTMBody {
        Map<String, Object> values;
        String eventName;
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
}
