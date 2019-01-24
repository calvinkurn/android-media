package com.tokopedia.core.analytics.container;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.tkpd.library.utils.legacy.CommonUtils;
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
import com.tokopedia.track.interfaces.ContextAnalytics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * formerly {@link GTMContainer}
 */
public class GTMAnalytics extends ContextAnalytics {
    private static final String TAG = GTMAnalytics.class.getSimpleName();

    // have status that describe pending.

    public GTMAnalytics(Context context) {
        super(context);
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

    public void eventError(String screenName, String errorDesc) {

    }

    public void sendScreen(String screenName) {
        Log.i("Tag Manager", "UA-9801603-15: Send Screen Event");
        pushEvent("openScreen", DataLayer.mapOf("screenName", screenName));
    }

    public void pushEvent(String eventName, Map<String, Object> values) {
        Log.i("GAv4", "UA-9801603-15: Send Event");

        log(getContext(), eventName, values);

        getTagManager().getDataLayer().pushEvent(eventName, values);
    }

    private static void log(Context context, String eventName, Map<String, Object> values) {
        String name = eventName == null ? (String) values.get("event") : eventName;
        GtmLogger.getInstance().save(context, name, values);
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

    public GTMAnalytics sendScreenAuthenticated(String screenName) {

        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();

        Authenticated authEvent = new Authenticated();
        authEvent.setUserFullName(sessionHandler.getLoginName());
        authEvent.setUserID(sessionHandler.getGTMLoginID());
        authEvent.setShopID(sessionHandler.getShopID());
        authEvent.setShopId(sessionHandler.getShopID());
        authEvent.setUserSeller(sessionHandler.isUserHasShop() ? 1 : 0);

        CommonUtils.dumper("GAv4 appdata " + new JSONObject(authEvent.getAuthDataLayar()).toString());

        eventAuthenticate(authEvent);
        sendScreen(screenName);

        return this;
    }

    public GTMAnalytics sendScreenAuthenticatedOfficialStore(String screenName, String shopID, String shopType, String pageType, String productId) {
        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();

        Authenticated authEvent = new Authenticated();
        authEvent.setUserFullName(sessionHandler.getLoginName());
        authEvent.setUserID(sessionHandler.getGTMLoginID());
        authEvent.setShopId(shopID);
        authEvent.setShopType(shopType);
        authEvent.setPageType(pageType);
        authEvent.setProductId(productId);
        authEvent.setUserSeller(sessionHandler.isUserHasShop() ? 1 : 0);

        CommonUtils.dumper("GAv4 appdata authenticated " + new JSONObject(authEvent.getAuthDataLayar()).toString());

        eventAuthenticate(authEvent);

        return this;
    }

    public GTMAnalytics eventAuthenticate(Authenticated authenticated) {
        CommonUtils.dumper("GAv4 send authenticated");

        final SessionHandler sessionHandler = RouterUtils.getRouterFromContext(getContext()).legacySessionHandler();

        authenticated.setAdsId(sessionHandler.getAdsId());

        authenticated.setAndroidId(sessionHandler.getAndroidId());


        if (TextUtils.isEmpty(authenticated.getcIntel())) {
            pushEvent( "authenticated", DataLayer.mapOf(
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
            pushEvent( "authenticated", DataLayer.mapOf(
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

    public void pushGeneral(Map<String, Object> values) {
        Log.i("GAv4", "UA-9801603-15: Send General");

        log(getContext(), null, values);
        TagManager.getInstance(getContext()).getDataLayer().push(values);
    }

    public void pushUserId(String userId) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("user_id", userId);
        getTagManager().getDataLayer().push(maps);
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

    public void eventTransaction(Purchase purchase) {
        pushGeneral(DataLayer.mapOf("ecommerce", DataLayer.mapOf(
                "purchase", purchase.getPurchase()
        )));
    }

    public void clearTransactionDataLayer(Purchase purchase) {
        purchase.clearPurchase();
        pushGeneral(DataLayer.mapOf("id", null, "affiliation", null,
                "revenue", null, "shipping", null, "products", null,
                "currencyCode", null, "actionField", null, "ecommerce", null));
    }

    public GTMAnalytics sendEvent(Map<String, Object> events) {
        pushGeneral(events);
        return this;
    }

    public void event(String name, Map<String, Object> data) {
        pushEvent( name, data);
    }

    public void eventNetworkError(String networkError) {

    }

    public void eventClickHotlistProductFeatured(Hotlist hotlist) {
        pushGeneral(
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

    public void impressionHotlistTracking(String hotlistName, String promoName, String promoCode) {
        clearEventTracking();
        pushGeneral(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo impression",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public void clickCopyButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        clearEventTracking();
        pushGeneral(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click salin kode",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public void clickTncButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        clearEventTracking();
        pushGeneral(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click syarat ketentuan",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public void eventImpressionPromoList(List<Object> list, String promoName) {
        clearEnhanceEcommerce();

        pushGeneral(
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

    public void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData) {
        pushGeneral(trackingData);
    }

    public void eventClickPromoListItem(List<Object> list, String promoName) {
        clearEnhanceEcommerce();

        pushGeneral(
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

    public void clearEnhanceEcommerce() {
        pushGeneral(
                DataLayer.mapOf("event", null,
                        "eventCategory", null,
                        "eventAction", null,
                        "eventLabel", null,
                        "products", null,
                        "promotions", null,
                        "ecommerce", null
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

    public void eventPurchaseMarketplace(Purchase purchase) {
        pushGeneral(
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
                        AppEventTracking.ECOMMERCE, DataLayer.mapOf(
                                Purchase.PURCHASE, purchase.getPurchase()
                        )
                )
        );
    }

    public void eventPurchaseDigital(Purchase purchase) {
        pushGeneral(
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

    public void eventImpressionCategoryLifestyle(List<Object> list) {
        clearEnhanceEcommerce();
        pushGeneral( DataLayer.mapOf("event", "promoView",
                        "eventCategory", "category page",
                        "eventAction", "subcategory impression",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "promoView", DataLayer.mapOf(
                                        "promotions", DataLayer.listOf(list.toArray(new Object[list.size()]))))
                )
        );
    }

    public void eventClickCategoryLifestyle(String categoryUrl, List<Object> list) {
        clearEnhanceEcommerce();
        pushGeneral(
                DataLayer.mapOf("event", "promoClick",
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

    public void eventImpressionHotlistProductFeatured(Hotlist hotlist) {
        pushGeneral(
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

    private static class GTMBody {
        Map<String, Object> values;
        String eventName;
    }
}
