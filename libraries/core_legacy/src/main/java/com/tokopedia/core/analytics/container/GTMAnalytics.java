package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.analytics.debugger.TetraDebugger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
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
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = 150; // 150 minutes (2.5 hours)
    private static final String KEY_GTM_EXPIRED_TIME = "android_gtm_expired_time";

    private static final String KEY_EVENT = "event";
    private static final String KEY_CATEGORY = "eventCategory";
    private static final String KEY_ACTION = "eventAction";
    private static final String KEY_LABEL = "eventLabel";
    private static final String USER_ID = "userId";
    private static final String SHOP_ID = "shopId";
    private static final String SHOP_TYPE = "shopType";
    private final Iris iris;
    private TetraDebugger tetraDebugger;
    private final RemoteConfig remoteConfig;

    // have status that describe pending.

    public GTMAnalytics(Context context) {
        super(context);
        if (GlobalConfig.isAllowDebuggingTools()) {
            tetraDebugger = TetraDebugger.Companion.instance(context);
        }
        iris = IrisAnalytics.Companion.getInstance(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    @Override
    public void sendGeneralEvent(Map<String, Object> value) {
        pushGeneralGtmV5(value);
    }

    @Override
    public void sendGeneralEvent(String event, String category, String action, String label) {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EVENT, event);
        map.put(KEY_CATEGORY, category);
        map.put(KEY_ACTION, action);
        map.put(KEY_LABEL, label);
        pushGeneralGtmV5(map);
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
                try {
                    cHolder.setContainerAvailableListener((containerHolder, s) -> {
                        if (!containerHolder.getStatus().isSuccess()) {
                            Log.d("GTM TKPD", "Container Available Failed");
                            return;
                        }

                        Log.d("GTM TKPD", "Container Available");

                        if (remoteConfig.getBoolean(RemoteConfigKey.ENABLE_GTM_REFRESH, true)) {
                            if (isAllowRefreshDefault(containerHolder)) {
                                Log.d("GTM TKPD", "Refreshed Container ");
                                refreshContainerInBackground(containerHolder);
                            }
                        }
                    });
                } catch (Exception e) {
                    eventError(getContext().getClass().toString(), e.toString());
                }
            }, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            eventError(getContext().getClass().toString(), e.toString());
        }
    }


    private void refreshContainerInBackground(ContainerHolder containerHolder) {
        if (remoteConfig.getBoolean(RemoteConfigKey.GTM_REFRESH_IN_BACKGROUND, false)) {
            //Refresh the container on background thread
            Observable.just(containerHolder)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .map(it -> {
                        containerHolder.refresh();
                        Log.d("GTM TKPD", "Refreshed Container in Background");
                        return true;
                    })
                    .subscribe(getDefaultSubscriber());
        } else {
            containerHolder.refresh();
            Log.d("GTM TKPD", "Refreshed Container in Main Thread");
        }
    }


    private Boolean isAllowRefreshDefault(ContainerHolder containerHolder) {
        long lastRefresh = 0;
        if (containerHolder.getContainer() != null) {
            lastRefresh = containerHolder.getContainer().getLastRefreshTime();
        }
        if (lastRefresh <= 0) {
            return true;
        }
        long gtmExpiredTime = remoteConfig.getLong(KEY_GTM_EXPIRED_TIME, EXPIRE_CONTAINER_TIME_DEFAULT);
        long gtmExpiredTimeInMillis = TimeUnit.MINUTES.toMillis(gtmExpiredTime);
        return System.currentTimeMillis() - lastRefresh > gtmExpiredTimeInMillis;
    }

    public void eventError(String screenName, String errorDesc) {

    }

    public void sendScreen(String screenName, Map<String, String> customDimension) {
        // v4 sendScreen
        Map<String, Object> map = DataLayer.mapOf("screenName", screenName);
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        pushEvent("openScreen", map);


        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        final String afUniqueId = !TextUtils.isEmpty(getAfUniqueId(context)) ? getAfUniqueId(context) : "none";


        // V5 sendScreen
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString("userId", sessionHandler.getLoginID());
        bundle.putString("clientId", getClientIDString());

        if(customDimension != null) {
            for (String key : customDimension.keySet()) {
                if (customDimension.get(key) != null) {
                    bundle.putString(key, customDimension.get(key));
                }
            }
        }

        logEvent("openScreen", bundle, context);
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
        if (customDimension != null) {
            map.putAll(customDimension);
        }
        pushGeneral(map);
    }

    private void log(Context context, String eventName, Bundle bundle) {
        log(context, eventName, bundleToMap(bundle));
    }

    private void log(Context context, String eventName, Map<String, Object> values) {
        String name = eventName == null ? (String) values.get("event") : eventName;
        GtmLogger.getInstance(context).save(name, values);
        if (tetraDebugger != null) {
            tetraDebugger.send(values);
        }
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

    public void sendScreenAuthenticated(String screenName) {
        if (TextUtils.isEmpty(screenName)) return;
        eventAuthenticate(null);
        sendScreen(screenName, null);
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

    public GTMAnalytics eventAddtoCart(GTMCart cart) {
        pushEvent("addToCart", DataLayer.mapOf("ecommerce", cart.getCartMap()));
        return this;
    }

    public GTMAnalytics clearAddtoCartDataLayer(String act) {
        pushGeneral(DataLayer.mapOf("products", null,
                "currencyCode", null, "addToCart", null, "ecommerce", null, act, null));
        return this;
    }

    public void pushClickEECommerce(Bundle bundle) {
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
    private static final String BEGINCHECKOUT = "begin_checkout";
    private static final String CHECKOUT_PROGRESS = "checkout_progress";

    public void pushEECommerce(String keyEvent, Bundle bundle) {
        // replace list
        if (TextUtils.isEmpty(bundle.getString(FirebaseAnalytics.Param.ITEM_LIST))
                && !TextUtils.isEmpty(bundle.getString("list"))) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
            bundle.remove("list");
        }

        switch (keyEvent.toLowerCase()) {
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
            case BEGINCHECKOUT:
                keyEvent = FirebaseAnalytics.Event.BEGIN_CHECKOUT;
                break;
            case CHECKOUT_PROGRESS:
                keyEvent = FirebaseAnalytics.Event.CHECKOUT_PROGRESS;
                break;
        }
        logEvent(keyEvent, bundle, context);
    }

    public void sendCampaign(Map<String, Object> param) {
        Bundle bundle = new Bundle();

        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();
        String afUniqueId = getAfUniqueId(context);

        bundle.putString("appsflyerId", afUniqueId);
        bundle.putString("userId", sessionHandler.getLoginID());
        bundle.putString("clientId", getClientIDString());

        bundle.putString("screenName", (String)param.get("screenName"));

        bundle.putString("gclid", (String)param.get(AppEventTracking.GTM.UTM_GCLID));
        bundle.putString("utmSource", (String)param.get(AppEventTracking.GTM.UTM_SOURCE));
        bundle.putString("utmMedium", (String)param.get(AppEventTracking.GTM.UTM_MEDIUM));
        bundle.putString("utmCampaign", (String)param.get(AppEventTracking.GTM.UTM_CAMPAIGN));
        bundle.putString("utmContent", (String)param.get(AppEventTracking.GTM.UTM_CAMPAIGN));
        bundle.putString("utmTerm", (String)param.get(AppEventTracking.GTM.UTM_TERM));

        logEvent("campaignTrack", bundle, context);
    }

    public void pushGeneralGtmV5(Map<String, Object> params) {
        pushGeneral(params);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, params.get(KEY_CATEGORY) + "");
        bundle.putString(KEY_ACTION, params.get(KEY_ACTION) + "");
        bundle.putString(KEY_LABEL, params.get(KEY_LABEL) + "");

        logEvent(params.get(KEY_EVENT) + "", bundle, context);
    }

    public void pushGeneralGtmV5(String event, String category, String action, String label) {
        sendGeneralEvent(event, category, action, label);

        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category);
        bundle.putString(KEY_ACTION, action);
        bundle.putString(KEY_LABEL, label);

        logEvent(event, bundle, context);
    }

    public void logEvent(String eventName, Bundle bundle, Context context) {
        try {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
            log(context, eventName, bundle);
        } catch (Exception ex) {
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
                    if (tetraDebugger != null) {
                        tetraDebugger.setUserId(userId);
                    }
                    return true;
                })
                .subscribe(getDefaultSubscriber());
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
