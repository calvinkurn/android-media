package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.ButtonClickEvent;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.analytics.nishikino.singleton.ContainerHolderSingleton;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class GTMContainer implements IGTMContainer {

    private static final String DEFAULT_CONTAINERID = "GTM-P32KTB";
    private static final int DEFAULT_CONTAINER_RES_ID = R.raw.gtm_default;
    private static final int EXPIRE_CONTAINER_TIME = 7200;
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = 7200000;
    private static final int EXPIRE_CONTAINER_TIME_DEBUG = 900;

    private static final String IS_EXCEPTION_ENABLED = "is_exception_enabled";
    private static final String IS_USING_HTTP_2 = "is_using_http_2";
    private static final String STR_GTM_EXCEPTION_ENABLED = "GTM is exception enabled";
    public static final String CLIENT_ID = "client_id";
    private static final String TAG = GTMContainer.class.getSimpleName();

    private Context context;

    public static GTMContainer newInstance(Context context) {
        return new GTMContainer(context);
    }

    public GTMContainer(Context context) {
        this.context = context;
    }

    @Override
    public TagManager getTagManager() {
        return TagManager.getInstance(context);
    }

    @Override
    public String getClientIDString() {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            String clientID = GoogleAnalytics.getInstance(context).newTracker(bundle.getString(AppEventTracking.GTM.GA_ID)).get("&cid");

            return clientID;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Boolean isAllowRefreshDefault(ContainerHolder containerHolder) {
        long lastRefresh = containerHolder.getContainer().getLastRefreshTime();
        Log.i("GTM TKPD", "Last refresh " + CommonUtils.getDate(lastRefresh));
        return System.currentTimeMillis() - lastRefresh > EXPIRE_CONTAINER_TIME_DEFAULT;
    }

    private Boolean isAllowRefresh() {
        //if (MainApplication.isDebug()) return true;
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.ALLOW_REFRESH);
        Log.i("GTM TKPD", "Allow Refresh? " + cache.isExpired());
        return cache.isExpired();
    }

    private void setExpiryRefresh() {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.ALLOW_REFRESH);
        if (MainApplication.isDebug()) {
            cache.setExpire(EXPIRE_CONTAINER_TIME_DEBUG);
        } else {
            cache.setExpire(EXPIRE_CONTAINER_TIME);
        }
        cache.applyEditor();
    }

    private void validateGTM() {
        if (ContainerHolderSingleton.getContainerHolder().getStatus().isSuccess()) {
            Log.i(TAG, STR_GTM_EXCEPTION_ENABLED + TrackingUtils.getGtmString(GTMContainer.IS_EXCEPTION_ENABLED));
        } else {
            Log.e("GTMContainer", "failure loading container");
        }
    }

    private void validateGTM(ContainerHolder containerHolder) {
        if (containerHolder.getStatus().isSuccess()) {
            Log.i(TAG, STR_GTM_EXCEPTION_ENABLED + TrackingUtils.getGtmString(GTMContainer.IS_EXCEPTION_ENABLED));
        } else {
            Log.e("GTMContainer", "failure loading container");
        }
    }

    @Override
    public void loadContainer(String containerId, int defaultContainerResId) {
        TagManager tagManager = getTagManager();
        PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(containerId, defaultContainerResId);

        pResult.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder cHolder) {
                ContainerHolderSingleton.setContainerHolder(cHolder);
                validateGTM();
            }
        }, 2, TimeUnit.SECONDS);
    }

    @Override
    public void loadContainer() {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = getTagManager();
            PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));

            pResult.setResultCallback(new ResultCallback<ContainerHolder>() {
                @Override
                public void onResult(ContainerHolder cHolder) {
                    ContainerHolderSingleton.setContainerHolder(cHolder);
                    if (isAllowRefreshDefault(cHolder)) {
                        Log.i("GTM TKPD", "Refreshed Container ");
                        cHolder.refresh();
                        //setExpiryRefresh();
                    }

                    validateGTM(cHolder);
                }
            }, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            TrackingUtils.eventError(context.getClass().toString(), e.toString());
        }
    }

    @Override
    public void loadContainer(ResultCallback<ContainerHolder> callback) {
        TagManager tagManager = getTagManager();
        PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferNonDefault(DEFAULT_CONTAINERID, DEFAULT_CONTAINER_RES_ID);

        pResult.setResultCallback(callback, 2, TimeUnit.SECONDS);
    }

    private DataLayer getDataLayer() {
        return getTagManager().getDataLayer();
    }

    @Override
    public GTMContainer sendScreen(String screenName) {
        Log.i("Tag Manager", "UA-9801603-15: Send Screen Event");
        GTMDataLayer.pushEvent(context,
                "openScreen", DataLayer.mapOf("screenName", screenName));

        return this;
    }

    @Override
    public GTMContainer sendCampaign(Campaign campaign) {
        Log.i("Tag Manager", "UA-9801603-15: Send Campaign Event");
        GTMDataLayer.pushEvent(context, "campaignTrack", campaign.getCampaign());
        return this;
    }

    @Override
    public GTMContainer clearCampaign(Campaign campaign) {
        Log.i("Tag Manager", "UA-9801603-15: Clear Campaign Event " + campaign.getNullCampaignMap());
        GTMDataLayer.pushGeneral(context, campaign.getNullCampaignMap());
        return this;
    }

    @Override
    public GTMContainer eventCheckout(Checkout checkout, String paymentId) {
        Log.i("Tag Manager", "UA-9801603-15: Send Checkout Event");
        Log.i("Tag Manager", "UA-9801603-15: MAP: " + checkout.getCheckoutMap().toString());

        GTMDataLayer.pushGeneral(context,
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

    @Override
    public GTMContainer eventCheckout(Checkout checkout) {
        Log.i("Tag Manager", "UA-9801603-15: Send Checkout Event");
        Log.i("Tag Manager", "UA-9801603-15: MAP: " + checkout.getCheckoutMap().toString());

        GTMDataLayer.pushGeneral(context,
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

    @Override
    public void clearCheckoutDataLayer() {
        GTMDataLayer.pushGeneral(context, DataLayer.mapOf("step", null, "products", null,
                "currencyCode", null, "actionField", null, "ecommerce", null));
    }

    @Override
    public GTMContainer sendScreenAuthenticated(String screenName) {
        Authenticated authEvent = new Authenticated();
        authEvent.setUserFullName(SessionHandler.getLoginName(context));
        authEvent.setUserID(SessionHandler.getGTMLoginID(context));
        authEvent.setShopID(SessionHandler.getShopID(context));
        authEvent.setShopId(SessionHandler.getShopID(context));
        authEvent.setUserSeller(SessionHandler.isUserHasShop(context) ? 1 : 0);

        CommonUtils.dumper("GAv4 appdata " + new JSONObject(authEvent.getAuthDataLayar()).toString());

        eventAuthenticate(authEvent);
        sendScreen(screenName);

        return this;
    }

    @Override
    public GTMContainer sendScreenAuthenticatedOfficialStore(String screenName, String shopID, String shopType, String pageType, String productId) {
        Authenticated authEvent = new Authenticated();
        authEvent.setUserFullName(SessionHandler.getLoginName(context));
        authEvent.setUserID(SessionHandler.getGTMLoginID(context));
        authEvent.setShopId(shopID);
        authEvent.setShopType(shopType);
        authEvent.setPageType(pageType);
        authEvent.setProductId(productId);
        authEvent.setUserSeller(SessionHandler.isUserHasShop(context) ? 1 : 0);

        CommonUtils.dumper("GAv4 appdata authenticated " + new JSONObject(authEvent.getAuthDataLayar()).toString());

        eventAuthenticate(authEvent).sendScreen(screenName);

        return this;
    }

    @Override
    public GTMContainer eventAuthenticate(Authenticated authenticated) {
        CommonUtils.dumper("GAv4 send authenticated");

        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ADVERTISINGID);
        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
            authenticated.setAdsId(adsId);
        }

        authenticated.setAndroidId(getAndroidId(context));


        if (TextUtils.isEmpty(authenticated.getcIntel())) {
            GTMDataLayer.pushEvent(context, "authenticated", DataLayer.mapOf(
                    Authenticated.KEY_CONTACT_INFO, authenticated.getAuthDataLayar(),
                    Authenticated.KEY_SHOP_ID_SELLER, authenticated.getShopId(),
                    Authenticated.KEY_SHOP_TYPE, authenticated.getShopType(),
                    Authenticated.KEY_PAGE_TYPE, authenticated.getPageType(),
                    Authenticated.KEY_PRODUCT_ID, authenticated.getProductId(),
                    Authenticated.KEY_NETWORK_SPEED, authenticated.getNetworkSpeed(),
                    Authenticated.ANDROID_ID, authenticated.getAndroidId(),
                    Authenticated.ADS_ID, authenticated.getAdsId()
            ));

        } else {
            GTMDataLayer.pushEvent(context, "authenticated", DataLayer.mapOf(
                    Authenticated.KEY_CONTACT_INFO, authenticated.getAuthDataLayar(),
                    Authenticated.KEY_SHOP_ID_SELLER, authenticated.getShopId(),
                    Authenticated.KEY_SHOP_TYPE, authenticated.getShopType(),
                    Authenticated.KEY_NETWORK_SPEED, authenticated.getNetworkSpeed(),
                    Authenticated.KEY_PAGE_TYPE, authenticated.getPageType(),
                    Authenticated.KEY_PRODUCT_ID, authenticated.getProductId(),
                    Authenticated.KEY_COMPETITOR_INTELLIGENCE, authenticated.getcIntel(),
                    Authenticated.ANDROID_ID, authenticated.getAndroidId(),
                    Authenticated.ADS_ID, authenticated.getAdsId()
            ));
        }

        return this;
    }

    @Override
    public GTMContainer eventAddtoCart(GTMCart cart) {
        Log.i("Tag Manager", "UA-9801603-15: Send impression Event");
        Log.i("Tag Manager", "UA-9801603-15: GAv4 MAP: " + cart.getCartMap().toString());
        GTMDataLayer.pushEvent(context, "addToCart", DataLayer.mapOf("ecommerce", cart.getCartMap()));
        return this;
    }

    @Override
    public GTMContainer clearAddtoCartDataLayer(String act) {
        GTMDataLayer.pushGeneral(context, DataLayer.mapOf("products", null,
                "currencyCode", null, "addToCart", null, "ecommerce", null, act, null));
        return this;
    }

    @Override
    public String eventHTTP() {
        return getString(GTMContainer.IS_USING_HTTP_2);
    }

    @Override
    public String getString(String key) {
        if (ContainerHolderSingleton.isContainerHolderAvailable())
            if (GTMContainer.getContainer() != null)
                return GTMContainer.getContainer().getString(key);
        return "";
    }


    @Override
    public void eventError(String screenName, String errorDesc) {
        if (TrackingUtils.getGtmString(GTMContainer.IS_EXCEPTION_ENABLED).equals("true")) {
            Log.d(TAG, "Sending Push Event Error");
            GTMDataLayer.pushEvent(context, "trackException", DataLayer.mapOf(
                    "screenName", screenName,
                    "exception.description", errorDesc,
                    "exception.isFatal", "true"));
        } else {
            Log.d(TAG, "Sending Push Event Error disabled");
        }
    }

    @Override
    public void eventLogAnalytics(String screenName, String errorDesc) {
        Log.d(TAG, "Sending Push Event Error");
        GTMDataLayer.pushEvent(context, "trackException", DataLayer.mapOf(
                "screenName", screenName,
                "exception.description", errorDesc,
                "exception.isFatal", "true"));
    }

    @Override
    public GTMContainer eventDetail(ProductDetail detail) {
        Log.i("Tag Manager", "UA-9801603-15: Send Deatil Event");
        Log.i("Tag Manager", "UA-9801603-15: GAv4 MAP: " + detail.getDetailMap().toString());
        GTMDataLayer.pushGeneral(context, DataLayer.mapOf("ecommerce", DataLayer.mapOf(
                "detail", detail.getDetailMap()
        )));

        return this;
    }

    @Override
    public void eventOnline(String uid) {
        Log.d(TAG, "UA-9801603-15: Sending Push Event Online");
        GTMDataLayer.pushEvent(context,
                "onapps", DataLayer.mapOf("LoginId", uid));
    }

    @Override
    public void eventNetworkError(String networkError) {
        if (TrackingUtils.getGtmString(GTMContainer.IS_EXCEPTION_ENABLED).equals("true")) {
            Log.d(TAG, "Sending Push Event Network Error");
            GTMDataLayer.pushEvent(context,
                    "trackException", DataLayer.mapOf("exception.description", networkError, "exception.isFatal", true));
        } else {
            Log.d(TAG, "Sending Push Event Network Error Disabled");
        }
    }

    @Override
    public void eventTransaction(Purchase purchase) {
        GTMDataLayer.pushGeneral(context, DataLayer.mapOf("ecommerce", DataLayer.mapOf(
                "purchase", purchase.getPurchase()
        )));
    }

    @Override
    public void clearTransactionDataLayer(Purchase purchase) {
        purchase.clearPurchase();
        GTMDataLayer.pushGeneral(context, DataLayer.mapOf("id", null, "affiliation", null,
                "revenue", null, "shipping", null, "products", null,
                "currencyCode", null, "actionField", null, "ecommerce", null));
    }

    @Override
    public GTMContainer sendEvent(Map<String, Object> events) {
        GTMDataLayer.pushGeneral(context, events);
        return this;
    }

    @Override
    public void pushUserId(String userId) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("user_id", userId);
        getDataLayer().push(maps);
    }

    public static ContainerHolder getContainerHolder() {
        return ContainerHolderSingleton.getContainerHolder();
    }

    @Override
    public void sendButtonClick(String event,
                                String category,
                                String action,
                                String label) {
        ButtonClickEvent clickEvent = new ButtonClickEvent(
                event,
                category,
                action,
                label
        );
        sendButtonEvent(clickEvent);
    }

    public GTMContainer sendButtonEvent(ButtonClickEvent buttonClickEvent) {
        Log.i("Tag Manager", "UA-9801603-15: Send Button Click Event");
        GTMDataLayer.pushGeneral(context, buttonClickEvent.getEvent());
        return this;
    }

    public static Container getContainer() {
        return ContainerHolderSingleton.getContainerHolder().getContainer();
    }

    @Override
    public boolean getBoolean(String key) {
        return ContainerHolderSingleton.isContainerHolderAvailable() && GTMContainer.getContainer().getBoolean(key);
    }

    @Override
    public long getLong(String key) {
        if (ContainerHolderSingleton.isContainerHolderAvailable())
            return GTMContainer.getContainer().getLong(key);
        return -1;
    }

    @Override
    public double getDouble(String key) {
        if (ContainerHolderSingleton.isContainerHolderAvailable())
            return GTMContainer.getContainer().getDouble(key);
        return -1;
    }

    @Override
    public void eventClickHotlistProductFeatured(Hotlist hotlist) {
        GTMDataLayer.pushGeneral(context,
                DataLayer.mapOf("event", AppEventTracking.Event.EVENT_INTERNAL_PROMO_MULTI,
                        "eventCategory", AppEventTracking.Category.CATEGORY_HOTLIST,
                        "eventAction", String.format("feature product hotlist %s - click product %s", hotlist.getHotlistAlias(), hotlist.getProductList().get(0).getProductName()),
                        "eventLabel", String.format("%s - %s", hotlist.getScreenName(), hotlist.getPosition(),
                                "ecommerce", DataLayer.mapOf(
                                        "click", DataLayer.mapOf(
                                                "actionField", DataLayer.mapOf(
                                                        "list", "hotlist"),
                                                "products", hotlist.getProduct().toArray(new Object[hotlist.getProduct().size()])
                                        )
                                )
                        )
                ));
    }

    @Override
    public void eventImpressionHotlistProductFeatured(Hotlist hotlist) {
        GTMDataLayer.pushGeneral(context,
                DataLayer.mapOf("event", AppEventTracking.Event.EVENT_INTERNAL_PROMO_MULTI,
                        "ecommerce", DataLayer.mapOf(
                                "actionField", DataLayer.mapOf("list", "hotlist"),
                                "impressions",
                                DataLayer.listOf(
                                        hotlist.getProduct().toArray(new Object[hotlist.getProduct().size()]))
                        )
                )
        );
    }

    public void event(String name, Map<String, Object> data) {
        GTMDataLayer.pushEvent(context, name, data);
    }

    @Override
    public void impressionHotlistTracking(String hotlistName, String promoName, String promoCode) {
        clearEventTracking();
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo impression",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }


    @Override
    public void clickCopyButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        clearEventTracking();
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click salin kode",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    @Override
    public void clickTncButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        clearEventTracking();
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click syarat ketentuan",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    @Override
    public void eventImpressionPromoList(List<Object> list, String promoName) {
        clearEnhanceEcommerce();

        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "promo microsite - promo list",
                        "eventAction", "impression on promo",
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    @Override
    public void eventClickPromoListItem(List<Object> list, String promoName) {
        clearEnhanceEcommerce();

        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        "event", "promoView",
                        "eventCategory", "promo microsite - promo list",
                        "eventAction", "impression on promo",
                        "eventLabel", promoName,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(
                                                list.toArray(new Object[list.size()]
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private void clearEventTracking() {
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null
                )
        );
    }

    @Override
    public void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData) {
        GTMDataLayer.pushGeneral(context, trackingData);

    }

    @Override
    public void clearEnhanceEcommerce() {
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null,
                        "ecommerce", null
                )
        );
    }

    @Override
    public void eventPurchaseMarketplace(Purchase purchase) {
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        AppEventTracking.EVENT, PurchaseTracking.TRANSACTION,
                        AppEventTracking.EVENT_CATEGORY, "purchase category",
                        AppEventTracking.EVENT_ACTION, "purchase action",
                        AppEventTracking.EVENT_LABEL, "purchase label",
                        Purchase.SHOP_ID, purchase.getShopId(),
                        Purchase.PAYMENT_ID, purchase.getPaymentId(),
                        Purchase.PAYMENT_TYPE, purchase.getPaymentType(),
                        Purchase.LOGISTIC_TYPE, purchase.getLogisticType(),
                        Purchase.USER_ID, purchase.getUserId(),
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                Purchase.PURCHASE, purchase.getPurchase()
                        )
                )
        );
    }

    @Override
    public void eventPurchaseDigital(Purchase purchase) {
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        AppEventTracking.EVENT, PurchaseTracking.TRANSACTION,
                        AppEventTracking.EVENT_CATEGORY, "purchase category digital",
                        AppEventTracking.EVENT_ACTION, "purchase action digital",
                        AppEventTracking.EVENT_LABEL, "purchase label digital",
                        Purchase.SHOP_ID, purchase.getShopId(),
                        Purchase.PAYMENT_ID, purchase.getPaymentId(),
                        Purchase.PAYMENT_TYPE, purchase.getPaymentType(),
                        Purchase.USER_ID, purchase.getUserId(),
                        Purchase.PAYMENT_STATUS, purchase.getPaymentStatus(),
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                Purchase.PURCHASE, purchase.getPurchase()
                        )
                )
        );
    }

    @Override
    public GTMContainer eventAddToCartPurchase(Product product) {
        try {
            GTMDataLayer.pushEvent(
                    context, "addToCart", DataLayer.mapOf(
                            AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                    "currencyCode", "IDR",
                                    "add", DataLayer.mapOf(
                                            "products", product.getProduct())
                            )
                    )
            );
        } catch (Exception e) {
            CommonUtils.dumper("GAv4 DATA LAYER " + e.getMessage());
            e.printStackTrace();
        }

        CommonUtils.dumper("GAv4 DATA LAYER " + DataLayer.mapOf(
                AppEventTracking.EVENT, "addToCart",
                AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                        "currencyCode", "IDR",
                        "add", DataLayer.mapOf(
                                "products", product.getProduct())
                )
        ));
        return this;
    }

    @Override
    public GTMContainer eventRemoveFromCartPurchase(Product product) {
        GTMDataLayer.pushEvent(
                context, "removeFromCart",
                DataLayer.mapOf(
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "remove", DataLayer.mapOf(
                                        "products", product.getProduct())
                        )
                )
        );
        return this;
    }

    public void eventImpressionCategoryLifestyle(List<Object> list) {
        clearEnhanceEcommerce();
        GTMDataLayer.pushGeneral(
                context, DataLayer.mapOf("event", "promoView",
                        "eventCategory", "category page",
                        "eventAction", "subcategory impression",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()]))))
                )
        );
    }

    @Override
    public void eventClickCategoryLifestyle(String categoryUrl, List<Object> list) {
        clearEnhanceEcommerce();
        GTMDataLayer.pushGeneral(
                context, DataLayer.mapOf("event", "promoClick",
                        "eventCategory", "category page",
                        "eventAction", "click subcategory",
                        "eventLabel", categoryUrl,
                        "ecommerce", DataLayer.mapOf(
                                "promoClick", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()])))),
                        "destinationURL", categoryUrl
                )
        );
    }

    @Override
    public void enhanceClickSearchResultProduct(Object object,
                                                String keyword,
                                                String actionField) {

        clearEnhanceEcommerce();

        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", keyword,
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", actionField),
                                        "products", DataLayer.listOf(object)
                                )
                        )
                )
        );
    }

    @Override
    public void enhanceClickImageSearchResultProduct(Object object, String actionField) {
        clearEnhanceEcommerce();

        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "search result",
                        "eventAction", "click - product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf("click",
                                DataLayer.mapOf("actionField",
                                        DataLayer.mapOf("list", actionField),
                                        "products", DataLayer.listOf(object)
                                )
                        )
                )
        );
    }

    @Override
    public void enhanceImpressionSearchResultProduct(List<Object> objects, String keyword) {
        clearEnhanceEcommerce();

        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "search result",
                        "eventAction", "impression - product",
                        "eventLabel", keyword,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        objects.toArray(new Object[objects.size()])
                                ))
                )
        );
    }

    @Override
    public void enhanceImpressionImageSearchResultProduct(List<Object> objects) {
        clearEnhanceEcommerce();

        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf("event", "productView",
                        "eventCategory", AppEventTracking.Category.IMAGE_SEARCH_RESULT,
                        "eventAction", "impression - product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        objects.toArray(new Object[objects.size()])
                                ))
                )
        );
    }

    private static String getAndroidId(Context context) {

        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ANDROID_ID);

        String androidId = localCacheHandler.getString(TkpdCache.Key.KEY_ANDROID_ID);
        if (androidId != null && !"".equalsIgnoreCase(androidId.trim())) {
            return androidId;
        } else {
            String android_id = AuthUtil.md5(Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            if (!TextUtils.isEmpty(android_id)) {
                localCacheHandler.putString(TkpdCache.Key.KEY_ANDROID_ID, android_id);
                localCacheHandler.applyEditor();
            }
            return android_id;
        }

    }
}