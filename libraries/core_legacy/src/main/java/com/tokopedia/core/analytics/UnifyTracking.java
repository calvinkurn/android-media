package com.tokopedia.core.analytics;

import android.content.Context;
import android.util.Log;

import com.appsflyer.AFInAppEventType;
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

    public static void eventClickItemSellerInfo(Context context, String articleName) {
        eventSellerInfo(context, AppEventTracking.Action.CLICK_ARTICLE, articleName);
    }

    public static void eventClickCartVariant(Context context, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.CLICK_CART_BUTTON_VARIANT,
                eventLabel
        ).getEvent());
    }

    public static void eventSelectColorVariant(Context context, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.SELECT_COLOR_VARIANT,
                eventLabel
        ).getEvent());
    }

    /* CATEGORY IMPROVEMENT*/

    public static void eventSelectSizeVariant(Context context, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.SELECT_SIZE_VARIANT,
                eventLabel
        ).getEvent());
    }

    public static void eventExpandCategoryIntermediary(Context context, String parentCat) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.NAVIGATION_CLICK,
                AppEventTracking.EventLabel.EXPAND_SUB_CATEGORY
        ).getEvent());
    }

    public static void eventBottomCategoryNavigation(Context context, String parentCat, String categoryId) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.BOTTOM_NAVIGATION_CATEGORY,
                categoryId
        ).getEvent());
    }

    public static void eventBannerClickCategory(Context context, String parentCat, String bannerName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.BANNER_CLICK,
                bannerName
        ).getEvent());
    }

    public static void eventOfficialStoreIntermediary(Context context, String parentCat, String brandName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.OFFICIAL_STORE_CLICK,
                brandName
        ).getEvent());
    }

    public static void eventVideoIntermediary(Context context, String parentCat, String videoName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.INTERMEDIARY_VIDEO_CLICK,
                videoName
        ).getEvent());
    }

    public static void eventLevelCategory(Context context, String parentCat, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_LEVEL,
                label
        ).getEvent());
    }

    public static void eventLevelCategoryIntermediary(Context context, String parentCat, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_LEVEL,
                label
        ).getEvent());
    }

    public static void eventHotlistIntermediary(Context context, String parentCat, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.HOTLIST,
                label
        ).getEvent());
    }

    public static void eventCuratedIntermediary(Context context, String parentCat, String curatedProductName,
                                                String productName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CURATED + " " + curatedProductName,
                productName
        ).getEvent());
    }

    public static void eventShowMoreCategory(Context context, String parentCat) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_MORE,
                AppEventTracking.EventLabel.CATEGORY_SHOW_MORE
        ).getEvent());
    }

    /* CATEGORY IMPROVEMENT*/

    public static void eventShareCategory(Context context, String parentCat, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_SHARE,
                label
        ).getEvent());
    }

    public static void eventNewOrderDetail(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ORDER_DETAIL
        ).getEvent());
    }

    public static void eventTrackOrder(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.STATUS,
                AppEventTracking.Category.STATUS,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRACK
        ).getEvent());
    }

    public static void eventAcceptOrder(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ACCEPT_ORDER
        ).getEvent());
    }

    public static void eventRejectOrder(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REJECT_ORDER
        ).getEvent());
    }

    public static void eventConfirmShipping(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION
        ).getEvent());
    }

    public static void eventConfirmShippingDetails(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DETAILS
        ).getEvent());
    }

    public static void eventConfirmShippingCancel(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CANCEL
        ).getEvent());
    }

    public static void eventResolutionDetail(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.COMPLAINT_DETAIL
        ).getEvent());
    }

    public static void eventResolutionDetail(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventForgotPassword(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FORGOT_PASSWORD,
                AppEventTracking.Category.FORGOT_PASSWORD,
                AppEventTracking.Action.RESET_SUCCESS,
                AppEventTracking.EventLabel.RESET_PASSWORD
        ).getEvent());
    }

    public static void eventResolutionSendSuccess(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.COMMENT
        ).getEvent());
    }

    public static void eventResolutionSendError(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.ERROR,
                AppEventTracking.EventLabel.COMMENT
        ).getEvent());
    }

    public static void eventResolutionEditSolution(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.EDIT_SOLUTION
        ).getEvent());
    }

    public static void eventReceivedShipping(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.RECEIVED,
                AppEventTracking.Category.RECEIVED,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION
        ).getEvent());
    }

    public static void eventAddProduct(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD
        ).getEvent());
    }

    public static void eventDiscoverySearch(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_TOP_NAV,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.SEARCH_PRODUCT,
                label
        ).getEvent());
    }

    public static void eventDiscoverySearchShop(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_TOP_NAV,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.SEARCH_SHOP,
                label
        ).getEvent());
    }

    public static void eventDiscoveryVoiceSearch(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.VOICE_SEARCH,
                label
        ).getEvent());
    }

    public static void eventDiscoveryCameraImageSearch(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.CAMERA_SEARCH,
                ""
        ).getEvent());
    }

    public static void eventDiscoveryGalleryImageSearch(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.GALLERY_SEARCH,
                ""
        ).getEvent());
    }

    public static void eventDiscoveryExternalImageSearch(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.EXTERNAL_IMAGE_SEARCH,
                ""
        ).getEvent());
    }

    public static void eventDiscoveryCameraImageSearchResult(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.CAMERA_SEARCH_RESULT,
                label
        ).getEvent());
    }

    public static void eventDiscoveryGalleryImageSearchResult(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.GALLERY_SEARCH_RESULT,
                label
        ).getEvent());
    }

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

    public static void eventWishlistBuy(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventFeedRecent(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW_RECENT,
                label
        ).getEvent());
    }

    public static void eventFavoriteView(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_WISHLIST,
                label
        ).getEvent());
    }

    public static void eventFavoriteViewRecommendation(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP_FAVORITE,
                ""
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

    public static void eventShareProduct(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE
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

    public static void eventPDPCart(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.BUY,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventPDPTalk(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT_TALK
        ).getEvent());

        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                "clickShopPage",
                "inbox - talk",
                "click tab diskusi produk on pdp",
                ""
        ).getEvent());
    }

    public static void eventPDPReputation(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REVIEW
        ).getEvent());
    }

    public static void eventPDPWishlit(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_TO_WISHLIST
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

    public static void eventPDPExpandDescription(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT_DESCRIPTION
        ).getEvent());
    }

    public static void eventPDPAddToWishlist(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_TO_WISHLIST_LABEL + MethodChecker.fromHtml(label)
        ).getEvent());
    }

    public static void eventPDPReport(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.REPORT,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REPORT
        ).getEvent());
    }

    public static void eventPDPReportNotLogin(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.REPORT,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REPORT_NOT_LOGIN
        ).getEvent());
    }

    public static void eventPDPSendMessage(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.MESSAGE_SHOP,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.MESSAGE_SHOP
        ).getEvent());
    }

    public static void eventPDPFavorite(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE_SHOP,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.FAVORITE_SHOP
        ).getEvent());
    }

    public static void eventWishlistAll(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_RECOMMENDATION,
                AppEventTracking.EventLabel.VIEW_ALL_WISHLIST
        ).getEvent());
    }

    public static void eventFavoriteShop(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP_FAVORITE,
                ""
        ).getEvent());
    }

    public static void eventDrawerClick(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                label
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

    public static void eventSuccessReport(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.REPORT_SUCCESS,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.REPORT_SUCCESS,
                AppEventTracking.EventLabel.REPORT_SUCCESS
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

    public static void eventCTAAction(Context context, String channel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.LOGIN_CLICK,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.CLICK,
                channel
        ).getEvent());
    }

    public static void eventPDPDetail(Context context, ProductDetail productDetail) {
        getGTMEngine(context)
                .eventDetail(productDetail);
    }

    public static void sendAFCompleteRegistrationEvent(Context context, int userId, String methodName) {
        Map<String, Object> eventVal = new HashMap<>();
        eventVal.put("custom_prop1", "registration");
        eventVal.put("os", "Android");
        eventVal.put(CUSTOMER_USER_ID, userId);
        eventVal.put(REGSITRATION_METHOD, methodName);
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AFInAppEventType.COMPLETE_REGISTRATION, eventVal);
    }

    public static void eventClickAddProduct(Context context, String eventCategory, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_ADD_PRODUCT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventClickGMSwitcher(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.TOP_SELLER,
                AppEventTracking.Category.GM_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventPersonalizedClicked(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label
        ).getEvent());
    }

    public static void eventOpenTopadsPushNotification(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label
        ).getEvent());
    }

    public static void eventSmartLock(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SUCCESS_SMART_LOCK,
                AppEventTracking.Category.SMART_LOCK,
                AppEventTracking.Action.SUCCESS,
                label
        ).getEvent());
    }

    public static void eventAddProductErrorServer(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR_SERVER,
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

    public static void eventAddProductAdd(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ADD,
                label
        ).getEvent());
    }

    public static void eventAddProductEdit(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_EDIT_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_EDIT,
                label
        ).getEvent());
    }

    public static void eventManageProductClicked(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_MANAGE_PRODUCT,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDraftProductClicked(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_DRAFT_PRODUCT,
                AppEventTracking.Category.DRAFT_PRODUCT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventTopAdsSwitcher(Context context, String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.TOPADS_SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.OPEN_TOP_SELLER + label
        ).getEvent());
    }

    public static void eventDownloadFromSwitcher(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DOWNLOAD_APP
        ).getEvent());
    }

    public static void eventClickYesGoldMerchantAddProduct(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.CLICK_GOLD_MERCHANT,
                AppEventTracking.Category.GOLD_MERCHANT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY_GM_ADD_PRODUCT
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

    public static void eventTopAds(Context context, String category, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.TOP_ADS_SELLER_APP,
                category,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventTopAdsProduct(Context context, String eventLabel) {
        eventTopAds(context, AppEventTracking.Category.TOP_ADS_PRODUCT, eventLabel);
    }

    public static void eventTopAdsShop(Context context, String eventLabel) {
        eventTopAds(context, AppEventTracking.Category.TOP_ADS_SHOP, eventLabel);
    }

    public static void eventTopAdsProductShop(Context context, String eventLabel) {
        eventTopAds(context, AppEventTracking.Category.TOP_ADS_PRODUCT_SHOP, eventLabel);
    }

    public static void eventTopAdsProductAddBalance(Context context) {
        eventTopAdsProductShop(context, AppEventTracking.EventLabel.ADD_BALANCE);
    }

    public static void eventTopAdsShopDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION + periodOption);
    }

    public static void eventTopAdsShopChooseDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM);
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

    public static void eventPDPSendChat(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(AppEventTracking.Event.PRODUCT_PAGE
                , AppEventTracking.Category.PRODUCT_PAGE
                , AppEventTracking.Action.PRODUCT_PAGE
                , AppEventTracking.EventLabel.PRODUCT_PAGE
        ).getEvent());
    }

    public static void eventShopSendChat(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(AppEventTracking.Event.SHOP_PAGE
                , AppEventTracking.Category.SHOP_PAGE
                , AppEventTracking.Action.SHOP_PAGE
                , ""
        ).getEvent());
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

    public static void eventSellerInfo(Context context, String eventAction, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SELLER_INFO,
                AppEventTracking.Category.SELLER_INFO_HOMEPAGE,
                eventAction,
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

    public static void eventMyCouponClicked(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKO_POINTS_PROMO_COUPON_PAGE,
                AppEventTracking.Action.CLICK_MY_COUPON,
                AppEventTracking.EventLabel.MY_COUPON
        ).getEvent());
    }

    public static void eventCouponChosen(Context context, String couponName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKO_POINTS_PROMO_COUPON_PAGE,
                AppEventTracking.Action.CHOOSE_COUPON,
                couponName
        ).getEvent());
    }

    public static void eventCouponPageClosed(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKO_POINTS_PROMO_COUPON_PAGE,
                AppEventTracking.Action.CLOSE_COUPON_PAGE,
                AppEventTracking.EventLabel.CLOSE_COUPON_PAGE
        ).getEvent());
    }

    public static void eventUserClickedPoints(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.HOMEPAGE,
                AppEventTracking.Category.TOKO_POINTS_PROMO_HOMEPAGE,
                AppEventTracking.Action.CLICK_TOKO_POINTS_STATUS,
                AppEventTracking.EventLabel.TOKOPOINTS_LABEL
        ).getEvent());
    }

    //Resolution Tracking
    public static void eventCreateResoStep1SaveAndChooseOther(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PRODUCT_PROBLEM,
                AppEventTracking.EventLabel.RESO_PROBLEM_SAVE_CHOOSE_OTHER
        ).getEvent());
    }

    public static void eventCreateResoStep1Save(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PRODUCT_PROBLEM,
                AppEventTracking.EventLabel.RESO_PROBLEM_SAVE
        ).getEvent());
    }

    public static void eventCreateResoStep1Continue(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PRODUCT_PROBLEM,
                AppEventTracking.EventLabel.RESO_PROBLEM_CONTINUE
        ).getEvent());
    }

    public static void eventCreateResoStep2Continue(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_SOLUTION,
                AppEventTracking.EventLabel.RESO_SOLUTION_CONTINUE
        ).getEvent());
    }

    public static void eventCreateResoStep3Continue(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PROVE,
                AppEventTracking.EventLabel.RESO_SOLUTION_CONTINUE
        ).getEvent());
    }

    public static void eventCreateResoPre(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO,
                AppEventTracking.EventLabel.RESO_CREATE_COMPLAINT_PRE
        ).getEvent());
    }

    public static void eventCreateResoConfirm(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO,
                AppEventTracking.EventLabel.RESO_CREATE_COMPLAINT_CONFIRM
        ).getEvent());
    }

    public static void eventCreateResoUnconfirm(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO,
                AppEventTracking.EventLabel.RESO_CREATE_COMPLAINT_UNCONFIRM
        ).getEvent());
    }

    public static void eventCreateResoAbandon(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO_ABANDON,
                AppEventTracking.EventLabel.RESO_CREATE_ABANDON
        ).getEvent());
    }

    public static void eventClickChangePhoneNumber(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_USER_PROFILE,
                AppEventTracking.Category.PROFILE_PAGE,
                AppEventTracking.Action.CLICK_CHANGE_PHONE_NUMBER,
                ""
        ).getEvent());
    }

    public static void eventSuccessChangePhoneNumber(Context context) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_USER_PROFILE,
                AppEventTracking.Category.MANAGE_PROFILE,
                AppEventTracking.Action.SUCCESS_CHANGE_PHONE_NUMBER,
                ""
        ).getEvent());
    }

    public static void eventTracking(Context context, EventTracking eventTracking) {
        sendGTMEvent(context, eventTracking.getEvent());
    }

    public static EventTracking eventResoDetailClickBackEditAddressPage(Context context, String resolutionId) {
        return new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_BACK_EDIT_ADDRESS_PAGE_DETAIL,
                ""
        ).setCustomEvent(AppEventTracking.ResoDimension.RESOLUTION_ID, resolutionId);
    }

    public static EventTracking eventResoDetailClickBackInputAddressPage(Context context, String resolutionId) {
        return new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_BACK_INPUT_ADDRESS_PAGE_DETAIL,
                ""
        ).setCustomEvent(AppEventTracking.ResoDimension.RESOLUTION_ID, resolutionId);
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

    public static void eventSearchResultQuickFilter(Context context, String filterName, String filterValue, boolean isSelected) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_PRODUCT,
                AppEventTracking.Action.QUICK_FILTER,
                filterName + " - " + filterValue + " - " + Boolean.toString(isSelected)
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

    public static void eventTopAdsProductStatisticBar(Context context, String statisticOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.STATISTIC_BAR + statisticOption);
    }

    public static void eventTopAdsShopStatisticBar(Context context, String statisticOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.STATISTIC_BAR + statisticOption);
    }
}
