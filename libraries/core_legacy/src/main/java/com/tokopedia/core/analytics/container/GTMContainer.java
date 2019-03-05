package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.analytics.nishikino.model.Checkout;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.analytics.nishikino.singleton.ContainerHolderSingleton;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tokopedia.core.analytics.TrackingUtils.getAfUniqueId;

@Deprecated
public class GTMContainer implements IGTMContainer {

    private static final int EXPIRE_CONTAINER_TIME = 7200;
    private static final long EXPIRE_CONTAINER_TIME_DEFAULT = 7200000;
    private static final int EXPIRE_CONTAINER_TIME_DEBUG = 900;

    private static final String TAG = GTMContainer.class.getSimpleName();

    private Context context;
    private SessionHandler sessionHandler;

    public static GTMContainer newInstance(Context context) {
        return new GTMContainer(context);
    }


    public GTMContainer(Context context) {
        this.context = context;
        this.sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
    }

    /**
     * {@link GTMAnalytics#getTagManager()}
     *
     * @return
     */
    @Override
    public TagManager getTagManager() {
        return TagManager.getInstance(context);
    }

    /**
     * {@link GTMAnalytics#getClientIDString()}
     *
     * @return
     */
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

    public void loadContainer() {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            TagManager tagManager = getTagManager();
            PendingResult<ContainerHolder> pResult = tagManager.loadContainerPreferFresh(bundle.getString(AppEventTracking.GTM.GTM_ID),
                    bundle.getInt(AppEventTracking.GTM.GTM_RESOURCE));

            pResult.setResultCallback(cHolder -> {
                ContainerHolderSingleton.setContainerHolder(cHolder);
                if (isAllowRefreshDefault(cHolder)) {
                    Log.i("GTMTKPD", "Refreshed Container ");
                    cHolder.refresh();
                }
            }, 2, TimeUnit.SECONDS);
        } catch (Exception e) {
            TrackingUtils.eventError(context, context.getClass().toString(), e.toString());
        }
    }

    private Boolean isAllowRefreshDefault(ContainerHolder containerHolder) {
        long lastRefresh = 0;
        if (containerHolder.getContainer() != null) {
            lastRefresh = containerHolder.getContainer().getLastRefreshTime();
        }
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
        if (GlobalConfig.DEBUG) {
            cache.setExpire(EXPIRE_CONTAINER_TIME_DEBUG);
        } else {
            cache.setExpire(EXPIRE_CONTAINER_TIME);
        }
        cache.applyEditor();
    }

    private DataLayer getDataLayer() {
        return getTagManager().getDataLayer();
    }

    /**
     * {@link GTMAnalytics#sendScreen(String)}
     *
     * @return
     */
    @Override
    public GTMContainer sendScreen(String screenName) {
        sendScreen(screenName, null);
        return this;
    }

    @Override
    public GTMContainer sendScreen(String screenName, Map<String, String> customDimension) {
        Log.i("Tag Manager", "UA-9801603-15: Send Screen Event");
        Map<String, Object> map = DataLayer.mapOf("screenName", screenName);
        if (customDimension != null && customDimension.size() > 0) {
            map.putAll(customDimension);
        }
        GTMDataLayer.pushEvent(context,
                "openScreen", map);

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
        eventAuthenticate();
        sendScreen(screenName);
        return this;
    }

    @Override
    public GTMContainer sendScreenAuthenticated(String screenName, Map<String, String> customDimension) {
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
        return this;
    }

    @Override
    public GTMContainer sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId) {
        Map<String, String> customDimension = new HashMap<>();
        customDimension.put(Authenticated.KEY_SHOP_ID_SELLER, shopID);
        customDimension.put(Authenticated.KEY_PAGE_TYPE, pageType);
        customDimension.put(Authenticated.KEY_SHOP_TYPE, shopType);
        customDimension.put(Authenticated.KEY_PRODUCT_ID, productId);
        eventAuthenticate(customDimension);
        sendScreen(screenName, customDimension);
        return this;
    }

    @Override
    public GTMContainer eventAuthenticate() {
        return eventAuthenticate(null);
    }

    @Override
    public GTMContainer eventAuthenticate(Map<String, String> customDimension) {
        String afUniqueId = getAfUniqueId(context);
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
        GTMDataLayer.pushEvent(context, Authenticated.KEY_CD_NAME, map);
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
    public void eventError(String screenName, String errorDesc) {
        // no op
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
        // no op
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
        getTagManager().getDataLayer().push(maps);
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
                        "products", null,
                        "promotions", null,
                        "ecommerce", null,
                        "currentSite", null
                )
        );
    }

    @Override
    public void eventPurchaseMarketplace(Purchase purchase) {
        GTMDataLayer.pushGeneral(
                context,
                DataLayer.mapOf(
                        AppEventTracking.EVENT, PurchaseTracking.TRANSACTION,
                        AppEventTracking.EVENT_CATEGORY, purchase.getEventCategory(),
                        AppEventTracking.EVENT_ACTION, purchase.getShopType(),
                        AppEventTracking.EVENT_LABEL, purchase.getEventLabel(),
                        Purchase.SHOP_ID, purchase.getShopId(),
                        Purchase.PAYMENT_ID, purchase.getPaymentId(),
                        Purchase.PAYMENT_TYPE, purchase.getPaymentType(),
                        Purchase.LOGISTIC_TYPE, purchase.getLogisticType(),
                        Purchase.USER_ID, purchase.getUserId(),
                        Purchase.CURRENT_SITE, purchase.getCurrentSite(),
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
                        AppEventTracking.EVENT_CATEGORY, "digital - thanks",
                        AppEventTracking.EVENT_ACTION, "view purchase attempt",
                        AppEventTracking.EVENT_LABEL, purchase.getEventLabel(),
                        Purchase.SHOP_ID, purchase.getShopId(),
                        Purchase.PAYMENT_ID, purchase.getPaymentId(),
                        Purchase.PAYMENT_TYPE, purchase.getPaymentType(),
                        Purchase.USER_ID, purchase.getUserId(),
                        Purchase.PAYMENT_STATUS, purchase.getPaymentStatus(),
                        Purchase.CURRENT_SITE, purchase.getCurrentSite(),
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                Purchase.PURCHASE, purchase.getPurchase()
                        )
                )
        );
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
}