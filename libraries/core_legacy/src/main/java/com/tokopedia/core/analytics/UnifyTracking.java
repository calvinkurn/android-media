package com.tokopedia.core.analytics;

import android.content.Context;
import android.util.Log;

import com.appsflyer.AFInAppEventType;
import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.legacy.MethodChecker;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.track.TrackApp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.appsflyer.AFInAppEventParameterName.CUSTOMER_USER_ID;
import static com.appsflyer.AFInAppEventParameterName.REGSITRATION_METHOD;

/**
 * @author by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * this class contains tracking for Unify (Click, Discovery & View Product, Authentication,
 * Login/Register)
 */

public class UnifyTracking extends TrackingUtils {

    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");

    public static void eventCampaign(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CAMPAIGN,
                AppEventTracking.Category.CAMPAIGN,
                AppEventTracking.Action.DEEPLINK,
                label
        ).getEvent());
    }

    public static void eventWishlistView(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventShopSendChat(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(AppEventTracking.Event.SHOP_PAGE,
                AppEventTracking.Category.SHOP_PAGE,
                AppEventTracking.Action.SHOP_PAGE,
                "");
    }

    public static void eventWishlistBuy(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventRegisterError(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.REGISTER_ERROR,
                AppEventTracking.Category.REGISTER,
                AppEventTracking.Action.REGISTER_ERROR,
                label
        ).getEvent());
    }

    public static void eventPDPOrientationChanged(Context context, String productId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.VIEW_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.LANDSCAPE_VIEW,
                String.format(AppEventTracking.EventLabel.PRODUCT_ID_VALUE, productId)
        ).getEvent());
    }

    public static void eventShare(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE_TO + label
        ).getEvent());
    }

    public static void eventManageShopInfo(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_INFO
        ).getEvent());
    }

    public static void eventManageShopShipping(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_SHIPPING
        ).getEvent());
    }

    public static void eventManageShopEtalase(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_ETALASE
        ).getEvent());
    }

    public static void eventManageShopNotes(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_NOTES
        ).getEvent());
    }

    public static void eventManageShopLocation(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_LOCATION
        ).getEvent());
    }

    public static void eventShopTabSelected(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SALES,
                AppEventTracking.Category.SALES,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventAddProductError(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR,
                label
        ).getEvent());
    }

    public static void eventWidgetInstalled(Context context) {
        eventWidget(context, AppEventTracking.Action.INSTALL, AppEventTracking.EventLabel.WIDGET_ORDER);
    }

    public static void eventWidgetRemoved(Context context) {
        eventWidget(context, AppEventTracking.Action.REMOVE, AppEventTracking.EventLabel.WIDGET_ORDER);
    }

    public static void eventAccessAppViewWidget(Context context) {
        eventWidget(context, AppEventTracking.Action.CLICK, AppEventTracking.EventLabel.TO_APP_ORDER);
    }

    public static void eventWidget(Context context, String action, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SELLER_WIDGET,
                AppEventTracking.Category.SELLER_APP_WIDGET,
                action,
                eventLabel
        ).getEvent());
    }

    public static void eventCheckoutGoldMerchant(Context context, String eventCategory, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_GOLD_MERCHANT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventClickSubscribeGoldMerchant(Context context, String packageType) {
        eventCheckoutGoldMerchant(context, AppEventTracking.Category.GOLD_MERCHANT_ATC, packageType);
    }

    public static void eventClickChangePackageGoldMerchant(Context context) {
        eventCheckoutGoldMerchant(context, AppEventTracking.Category.GOLD_MERCHANT_CHECKOUT, AppEventTracking.EventLabel.CHANGE_PACKAGE_GOLD_MERCHANT);
    }

    public static void eventClickSubscribeCheckoutGoldMerchant(Context context) {
        eventCheckoutGoldMerchant(context, AppEventTracking.Category.GOLD_MERCHANT_CHECKOUT, AppEventTracking.EventLabel.GM_CHECKOUT);
    }

    public static void eventCreateShopSellerApp(Context context, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_CREATE_SHOP,
                AppEventTracking.Category.CREATE_SHOP,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventCreateShopFillLogisticError(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.SAVE_LOGISTIC_ERROR);
    }

    public static void eventOpportunity(Context context, String event, String category,
                                        String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                event,
                category,
                action,
                label)
                .getEvent());
    }

    public static void eventReferralAndShare(Context context, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_APP_SHARE_REFERRAL,
                AppEventTracking.Category.REFERRAL,
                action,
                label
        ).getEvent());
    }

    public static void eventAppShareWhenReferralOff(Context context, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_APP_SHARE_WHEN_REFERRAL_OFF,
                AppEventTracking.Category.APPSHARE,
                action,
                label
        ).getEvent());
    }

    public static void eventFeaturedProduct(Context context, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.FEATURED_PRODUCT,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventTickErrorFeaturedProduct(Context context) {
        eventFeaturedProduct(context, AppEventTracking.EventLabel.TICK_ERROR);
    }

    public static void eventSavePickFeaturedProduct(Context context, String counterProduct) {
        eventFeaturedProduct(context, AppEventTracking.EventLabel.SAVE_FEATURED_PRODUCT_PICKER + counterProduct);
    }

    public static void eventProductManage(Context context, String action, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_MANAGE_PRODUCT,
                AppEventTracking.Category.MANAGE_PRODUCT,
                action,
                label
        ).getEvent());
    }

    public static void eventProductManageTopNav(Context context, String label) {
        eventProductManage(context, AppEventTracking.Action.CLICK_TOP_NAV, label);
    }

    public static void eventProductManageSearch(Context context) {
        eventProductManage(context, AppEventTracking.Action.CLICK_TOP_NAV, AppEventTracking.EventLabel.SEARCH_PRODUCT);
    }

    public static void eventProductManageClickDetail(Context context) {
        eventProductManage(context, AppEventTracking.Action.CLICK_PRODUCT_LIST, AppEventTracking.EventLabel.CLICK_PRODUCT_LIST);
    }

    public static void eventProductManageSortProduct(Context context, String label) {
        eventProductManage(context, AppEventTracking.Action.CLICK_SORT_PRODUCT, label);
    }

    public static void eventProductManageFilterProduct(Context context, String label) {
        eventProductManage(context, AppEventTracking.Action.CLICK_FILTER_PRODUCT, label);
    }

    // digital user profile

    // digital native

    // digital homepage

    public static void eventProductManageOverflowMenu(Context context, String label) {
        eventProductManage(context, AppEventTracking.Action.CLICK_OVERFLOW_MENU, label);
    }

    /**
     * use trackApp instead
     */
    @Deprecated
    public static void eventTracking(Context context, EventTracking eventTracking) {
        sendGTMEvent(context, eventTracking.getEvent());
    }

    public static void eventSearchResultProductWishlistClick(Context context, boolean isWishlisted, String keyword) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_VIEW,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.CLICK_WISHLIST,
                generateWishlistClickEventLabel(isWishlisted, keyword)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    private static String generateWishlistClickEventLabel(boolean isWishlisted, String keyword) {
        String action = isWishlisted ? "add" : "remove";
        return action + " - " + keyword + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE).format(new Date());
    }

    public static void eventSearchResultSort(Context context, String screenName, String sortByValue) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SORT_BY,
                AppEventTracking.Action.SORT_BY + " - " + screenName,
                sortByValue
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventDigitalEventTracking(Context context, String action, String label) {
        Log.d("EVENTSGA", action + " - " + label);
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_DIGITAL_EVENT,
                AppEventTracking.Category.DIGITAL_EVENT,
                action,
                label
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

}
