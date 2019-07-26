package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.tokopedia.core.analytics.TrackingUtils.getAfUniqueId;
import com.google.firebase.analytics.FirebaseAnalytics;

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

        Bundle bundle = new Bundle();
        bundle.putString(KEY_EVENT, event);
        bundle.putString(KEY_CATEGORY, event);
        bundle.putString(KEY_ACTION, event);
        bundle.putString(KEY_LABEL, event);

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
        Log.i("Tag Manager", "UA-9801603-15: Send Screen Event");
        pushEvent("openScreen", DataLayer.mapOf("screenName", screenName));
    }

    public void pushEvent(String eventName, Map<String, Object> values) {
        Observable.just(values)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(uid -> {
                    Log.i("GAv4", "UA-9801603-15: Send Event");
                    log(getContext(), eventName, values);
                    getTagManager().getDataLayer().pushEvent(eventName, values);
                    pushIris(eventName, values);
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

    private static void log(Context context, String eventName, Map<String, Object> values) {
        String name = eventName == null ? (String) values.get("event") : eventName;
        GtmLogger.getInstance(context).save(name, values);
    }

    public GTMAnalytics sendCampaign(Campaign campaign) {
        Log.i("Tag Manager", "UA-9801603-15: Send Campaign Event");
        pushEvent("campaignTrack", campaign.getCampaign());
        return this;
    }

    public GTMAnalytics clearCampaign(Campaign campaign) {
        Log.i("Tag Manager", "UA-9801603-15: Clear Campaign Event " + campaign.getNullCampaignMap());
        pushGeneral(campaign.getNullCampaignMap());
        return this;
    }

    public GTMAnalytics eventCheckout(Checkout checkout, String paymentId) {
        Log.i("Tag Manager", "UA-9801603-15: Send Checkout Event");
        Log.i("Tag Manager", "UA-9801603-15: MAP: " + checkout.getCheckoutMap().toString());

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
        Log.i("Tag Manager", "UA-9801603-15: Send Checkout Event");
        Log.i("Tag Manager", "UA-9801603-15: MAP: " + checkout.getCheckoutMap().toString());

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
        Log.i("Tag Manager", "UA-9801603-15: Send Screen Event");
        Map<String, Object> map = DataLayer.mapOf("screenName", screenName);
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        pushEvent("openScreen", map);
    }

    public GTMAnalytics eventAddtoCart(GTMCart cart) {
        Log.i("Tag Manager", "UA-9801603-15: Send impression Event");
        Log.i("Tag Manager", "UA-9801603-15: GAv4 MAP: " + cart.getCartMap().toString());
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
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
        bundle.remove("list");
        logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle, context);
    }

    private static final String PRODUCTVIEW = "productview";
    private static final String PRODUCTCLICK = "productclick";

    public void pushEECommerce(String keyEvent, Bundle bundle){
        // replace list
        bundle.putString(FirebaseAnalytics.Param.ITEM_LIST, bundle.getString("list"));
        bundle.remove("list");

        switch (keyEvent.toLowerCase()){
            case PRODUCTVIEW:
                keyEvent = FirebaseAnalytics.Event.VIEW_ITEM;
                break;
            case PRODUCTCLICK:
                keyEvent = FirebaseAnalytics.Event.SELECT_CONTENT;
                break;

        }
        logEvent(keyEvent, bundle, context);
    }

    public void pushGeneralGtmV5(Map<String, Object> params){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, params.get(KEY_CATEGORY)+"");
        bundle.putString(KEY_ACTION, params.get(KEY_ACTION)+"");
        bundle.putString(KEY_LABEL, params.get(KEY_LABEL)+"");

        logEvent(params.get(KEY_EVENT)+"", bundle, context);
    }

    public void pushGeneralGtmV5(String event, String category, String action, String label){
        Bundle bundle = new Bundle();
        bundle.putString(KEY_CATEGORY, category);
        bundle.putString(KEY_ACTION, action);
        bundle.putString(KEY_LABEL, label);

        logEvent(event, bundle, context);
    }

    public static void logEvent(String eventName, Bundle bundle,Context context){
        try {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void pushGeneral(Map<String, Object> values) {
        Observable.just(values)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(map -> {
                    Log.i("GAv4", "UA-9801603-15: Send General");
                    log(getContext(), null, values);
                    TagManager.getInstance(getContext()).getDataLayer().push(values);
                    pushIris("", values);
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
        Log.i("Tag Manager", "UA-9801603-15: Send Deatil Event");
        Log.i("Tag Manager", "UA-9801603-15: GAv4 MAP: " + detail.getDetailMap().toString());
        pushGeneral( DataLayer.mapOf("ecommerce", DataLayer.mapOf(
                "detail", detail.getDetailMap()
        )));

        return this;
    }

    public void eventOnline(String uid) {
        Log.d(TAG, "UA-9801603-15: Sending Push Event Online");
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
                        "currentSite", null
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
