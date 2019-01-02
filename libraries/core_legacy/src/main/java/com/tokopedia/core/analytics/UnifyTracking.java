package com.tokopedia.core.analytics;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.appsflyer.AFInAppEventType;
import com.moe.pushlibrary.PayloadBuilder;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tkpd.library.utils.legacy.MethodChecker;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.var.ProductItem;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.appsflyer.AFInAppEventParameterName.CUSTOMER_USER_ID;
import static com.appsflyer.AFInAppEventParameterName.REGSITRATION_METHOD;

/**
 * @author by Herdi_WORK on 25.10.16.
 *         modified by Alvarisi
 *         this class contains tracking for Unify (Click, Discovery & View Product, Authentication,
 *         Login/Register)
 */

public class UnifyTracking extends TrackingUtils {

    public static final String EXTRA_LABEL = "label";

    public static void eventHomeTab(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOMEPAGE_UNIFY,
                AppEventTracking.Category.HOMEPAGE_UNIFY,
                String.format("click %s", label),
                ""
        ).getEvent());
    }

    public static void eventHomeCategory(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventHomeTopPicksItem(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.TOP_PICKS,
                AppEventTracking.Category.TOP_PICKS_HOME,
                action,
                label
        ).getEvent());
    }

    /* VARIANT */


    public static void eventClickVariant(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.CLICK_VARIANTS,
                eventLabel
        ).getEvent());
    }

    public static void eventBuyPDPVariant(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ADD_TO_CART_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.CLICK_BUY_VARIANT_PDP,
                eventLabel
        ).getEvent());
    }

    public static void eventClickCartVariant(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.CLICK_CART_BUTTON_VARIANT,
                eventLabel
        ).getEvent());
    }

    public static void eventSelectColorVariant(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.SELECT_COLOR_VARIANT,
                eventLabel
        ).getEvent());
    }

    public static void eventSelectSizeVariant(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.SELECT_SIZE_VARIANT,
                eventLabel
        ).getEvent());
    }

    public static void eventBuyPageVariant(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ADD_TO_CART_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.CLICK_BUY_VARIANT_PAGE,
                eventLabel
        ).getEvent());
    }

    /* CATEGORY IMPROVEMENT*/

    public static void eventExpandCategoryIntermediary(Context context, String parentCat) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.NAVIGATION_CLICK,
                AppEventTracking.EventLabel.EXPAND_SUB_CATEGORY
        ).getEvent());
    }

    public static void eventBottomCategoryNavigation(Context context, String parentCat, String categoryId) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.BOTTOM_NAVIGATION_CATEGORY,
                categoryId
        ).getEvent());
    }

    public static void eventBannerClickCategory(Context context, String parentCat, String bannerName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.BANNER_CLICK,
                bannerName
        ).getEvent());
    }

    public static void eventOfficialStoreIntermediary(Context context, String parentCat, String brandName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.OFFICIAL_STORE_CLICK,
                brandName
        ).getEvent());
    }

    public static void eventVideoIntermediary(Context context, String parentCat, String videoName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.INTERMEDIARY_VIDEO_CLICK,
                videoName
        ).getEvent());
    }

    public static void eventCategoryDrawer(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.DRAWER_CATEGORY,
                AppEventTracking.Category.CATEGORY_DRAWER,
                AppEventTracking.Action.CLICK_CATEGORY,
                "0"
        ).getEvent());
    }

    public static void eventProductOnCategory(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PRODUCT,
                AppEventTracking.Action.PRODUCT_CATEGORY,
                label
        ).getEvent());
    }

    public static void eventLevelCategory(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_LEVEL,
                label
        ).getEvent());
    }

    public static void eventLevelCategoryIntermediary(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_LEVEL,
                label
        ).getEvent());
    }

    public static void eventHotlistIntermediary(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.HOTLIST,
                label
        ).getEvent());
    }

    public static void eventCuratedIntermediary(Context context, String parentCat, String curatedProductName,
                                                String productName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIARY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CURATED + " " + curatedProductName,
                productName
        ).getEvent());
    }

    public static void eventShowMoreCategory(Context context, String parentCat) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_MORE,
                AppEventTracking.EventLabel.CATEGORY_SHOW_MORE
        ).getEvent());
    }

    public static void eventSortCategory(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_SORT,
                label
        ).getEvent());
    }

    public static void eventFilterCategory(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_FILTER,
                label
        ).getEvent());
    }

    public static void eventDisplayCategory(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_DISLPAY,
                label
        ).getEvent());
    }

    public static void eventShareCategory(Context context, String parentCat, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE + "-" + parentCat,
                AppEventTracking.Action.CATEGORY_SHARE,
                label
        ).getEvent());
    }

    /* CATEGORY IMPROVEMENT*/

    public static void eventHomeTopPicksTitle(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.TOP_PICKS,
                AppEventTracking.Category.TOP_PICKS_HOME,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventNewOrderDetail(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ORDER_DETAIL
        ).getEvent());
    }

    public static void eventTrackOrder(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.STATUS,
                AppEventTracking.Category.STATUS,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRACK
        ).getEvent());
    }

    public static void eventAcceptOrder(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ACCEPT_ORDER
        ).getEvent());
    }

    public static void eventRejectOrder(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REJECT_ORDER
        ).getEvent());
    }

    public static void eventConfirmShipping(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION
        ).getEvent());
    }

    public static void eventMessageDetail(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.MESSAGE,
                AppEventTracking.Category.MESSAGE,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventMessageSend(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.MESSAGE,
                AppEventTracking.Category.MESSAGE,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.INBOX + "-" + label
        ).getEvent());
    }

    public static void eventDiscussionProductDetail(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventDiscussionProductSend(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.SEND,
                label
        ).getEvent());
    }

    public static void eventReviewDetail(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REVIEW,
                AppEventTracking.Category.REVIEW,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventReviewDetail(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REVIEW,
                AppEventTracking.Category.REVIEW,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.REVIEW_DETAIL
        ).getEvent());
    }

    public static void eventConfirmShippingDetails(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DETAILS
        ).getEvent());
    }

    public static void eventConfirmShippingCancel(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CANCEL
        ).getEvent());
    }

    public static void eventDiscussionDetail(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.DISCUSSION_DETAIL
        ).getEvent());
    }

    public static void eventDiscussionSendSuccess(Context context, String from) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.COMMENT + "-" + from
        ).getEvent());
    }

    public static void eventDiscussionSendError(Context context, String from) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.ERROR,
                AppEventTracking.EventLabel.COMMENT + "-" + from
        ).getEvent());
    }

    public static void eventResolutionDetail(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.COMPLAINT_DETAIL
        ).getEvent());
    }

    public static void eventResolutionDetail(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventEtalaseAdd(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ETALASE,
                AppEventTracking.Category.ETALASE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD
        ).getEvent());
    }

    public static void eventForgotPassword(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FORGOT_PASSWORD,
                AppEventTracking.Category.FORGOT_PASSWORD,
                AppEventTracking.Action.RESET_SUCCESS,
                AppEventTracking.EventLabel.RESET_PASSWORD
        ).getEvent());
    }

    public static void eventResendNotification(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RESEND_EMAIL,
                AppEventTracking.Category.EMAIL_ACTIVATION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.RESEND_EMAIL
        ).getEvent());
    }

    public static void eventCreateShop(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CREATE_SHOP,
                AppEventTracking.Category.CREATE_SHOP,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CREATE
        ).getEvent());
    }

    public static void eventCreateShopSuccess(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CREATE_SHOP,
                AppEventTracking.Category.CREATE_SHOP,
                AppEventTracking.Action.SUCCESS,
                AppEventTracking.EventLabel.SHOP_CREATED
        ).getEvent());
    }

    public static void eventResolutionSendSuccess(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.COMMENT
        ).getEvent());
    }

    public static void eventResolutionSendError(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.ERROR,
                AppEventTracking.EventLabel.COMMENT
        ).getEvent());
    }

    public static void eventResolutionEditSolution(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.EDIT_SOLUTION
        ).getEvent());
    }

    public static void eventReviewCompleteBuyer(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REVIEW,
                AppEventTracking.Category.REVIEW,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REVIEW_BUYER
        ).getEvent());
    }

    public static void eventReceivedShipping(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECEIVED,
                AppEventTracking.Category.RECEIVED,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION
        ).getEvent());
    }

    public static void eventAddProduct(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD
        ).getEvent());
    }

    public static void eventAddProductMore(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_MORE
        ).getEvent());
    }

    public static void eventATCAddAddress(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ATC,
                AppEventTracking.Category.ADD_TO_CART,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_ADDRESS
        ).getEvent());
    }

    public static void eventATCBuy(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ATC,
                AppEventTracking.Category.ADD_TO_CART,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventATCChangeAddress(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.ATC,
                AppEventTracking.Category.ADD_TO_CART,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CHANGE_ADDRESS
        ).getEvent());
    }

    public static void eventDiscoverySearchShop(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP
        ).getEvent());
    }

    public static void eventDiscoverySearchShopDetail(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SHOP_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP
        ).getEvent());
    }

    public static void eventTalkSuccessSend(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.TALK_SUCCESS,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.TALK_SUCCESS,
                AppEventTracking.EventLabel.TALK_SUCCESS
        ).getEvent());
    }

    public static void eventDiscoverySearchCatalog(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CATALOG
        ).getEvent());
    }

    public static void eventDiscoveryFilter(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FILTER,
                AppEventTracking.Category.FILTER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDiscoverySort(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SORT,
                AppEventTracking.Category.SORT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDiscoverySearch(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_TOP_NAV,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.SEARCH_PRODUCT,
                label
        ).getEvent());
    }

    public static void eventDiscoverySearchShop(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_TOP_NAV,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.SEARCH_SHOP,
                label
        ).getEvent());
    }

    public static void eventDiscoveryVoiceSearch(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.VOICE_SEARCH,
                label
        ).getEvent());
    }

    public static void eventDiscoveryCameraImageSearch(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.CAMERA_SEARCH,
                ""
        ).getEvent());
    }

    public static void eventDiscoveryGalleryImageSearch(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.GALLERY_SEARCH,
                ""
        ).getEvent());
    }

    public static void eventDiscoveryExternalImageSearch(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.EXTERNAL_IMAGE_SEARCH,
                ""
        ).getEvent());
    }

    public static void eventDiscoveryCameraImageSearchResult(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.CAMERA_SEARCH_RESULT,
                label
        ).getEvent());
    }

    public static void eventDiscoveryGalleryImageSearchResult(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMAGE_SEARCH_CLICK,
                AppEventTracking.Category.IMAGE_SEARCH,
                AppEventTracking.Action.GALLERY_SEARCH_RESULT,
                label
        ).getEvent());
    }

    public static void eventCartAbandon(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.BACK_CLICK,
                AppEventTracking.Category.PAYMENT,
                AppEventTracking.Action.ABANDON,
                AppEventTracking.EventLabel.THANK_YOU_PAGE
        ).getEvent());
    }

    public static void eventR3(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.R3,
                AppEventTracking.Category.R3USER,
                action,
                label
        ).getEvent());
    }

    public static void eventR3Product(Context context, String productId, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.R3,
                AppEventTracking.Category.R3USER,
                action,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, productId)
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedView(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_FEED,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedViewShop(Context context, String screenName, String shopId, String label) {
        sendGTMEvent(context, new EventTracking(
                screenName,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, shopId)
                .getEvent());
    }

    public static void eventFeedViewProduct(Context context, String screenName, String productId, String label) {
        sendGTMEvent(context, new EventTracking(
                screenName,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, productId)
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedViewProduct(Context context, String screenName, String productId, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                screenName,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                action,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, productId)
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedViewAll(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_FEED,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW_RECENT,
                AppEventTracking.EventLabel.VIEW_ALL_RECENT)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedClick(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_FEED,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.CLICK,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventOfficialStoreBrandSeeAll(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppScreen.UnifyScreenTracker.SCREEN_UNIFY_HOME_FEED,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.EventLabel.HOME.toLowerCase() + " - " + label + " " + AppEventTracking.Action.CLICK_VIEW_ALL,
                "")
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedClickProduct(Context context, String screenName, String productId, String label) {
        sendGTMEvent(context, new EventTracking(
                screenName,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.CLICK,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, productId)
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, "0")
                .getEvent());
    }

    public static void eventFeedClickShop(Context context, String screenName, String shopId, String label) {
        sendGTMEvent(context, new EventTracking(
                screenName,
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.CLICK,
                label)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .setCustomEvent(AppEventTracking.CustomDimension.PRODUCT_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.PROMO_ID, "0")
                .setCustomEvent(AppEventTracking.CustomDimension.SHOP_ID, shopId)
                .getEvent());
    }

    public static void eventCampaign(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CAMPAIGN,
                AppEventTracking.Category.CAMPAIGN,
                AppEventTracking.Action.DEEPLINK,
                label
        ).getEvent());
    }

    public static void eventCartPayment(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PAYMENT_METHOD
        ).getEvent());
    }

    public static void eventCartDeposit(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DEPOSIT_USE
        ).getEvent());
    }

    public static void eventCartDropshipper(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DROPSHIPPER
        ).getEvent());
    }

    public static void eventCartThankYou(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CONFIRM_PAYMENT,
                AppEventTracking.Category.PAYMENT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.THANK_YOU_PAGE
        ).getEvent());
    }

    public static void eventCartPayNow(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PAYMENT,
                AppEventTracking.Category.PAYMENT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PAY_NOW
        ).getEvent());
    }

    public static void eventDiscoveryNoResult(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NO_RESULT,
                AppEventTracking.Category.NO_RESULT,
                AppEventTracking.Action.NO_RESULT,
                label
        ).getEvent());
    }

    public static void eventWishlistView(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventWishlistBuy(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventFeedRecent(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW_RECENT,
                label
        ).getEvent());
    }

    public static void eventHotlist(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Category.HOTLIST,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventFavoriteView(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_WISHLIST,
                label
        ).getEvent());
    }

    public static void eventFavoriteViewRecommendation(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP_FAVORITE,
                ""
        ).getEvent());
    }

    public static void eventRegisterError(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REGISTER_ERROR,
                AppEventTracking.Category.REGISTER,
                AppEventTracking.Action.REGISTER_ERROR,
                label
        ).getEvent());
    }

    public static void eventShareProduct(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE
        ).getEvent());
    }

    public static void eventPDPOrientationChanged(Context context, String productId) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.VIEW_PDP,
                AppEventTracking.Category.PRODUCT_DETAIL.toLowerCase(),
                AppEventTracking.Action.LANDSCAPE_VIEW,
                String.format(AppEventTracking.EventLabel.PRODUCT_ID_VALUE, productId)
        ).getEvent());
    }

    public static void eventPDPCart(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.BUY,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventPDPTalk(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT_TALK
        ).getEvent());

        sendGTMEvent(context, new EventTracking(
                "clickShopPage",
                "inbox - talk",
                "click tab diskusi produk on pdp",
                ""
        ).getEvent());
    }

    public static void eventPDPReputation(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REVIEW
        ).getEvent());
    }

    public static void eventPDPWishlit(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_TO_WISHLIST
        ).getEvent());
    }

    public static void eventShare(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE_TO + label
        ).getEvent());
    }

    public static void eventPDPExpandDescription(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT_DESCRIPTION
        ).getEvent());
    }

    public static void eventPDPAddToWishlist(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_TO_WISHLIST_LABEL + MethodChecker.fromHtml(label)
        ).getEvent());
    }

    public static void eventPDPReport(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REPORT,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REPORT
        ).getEvent());
    }

    public static void eventPDPReportNotLogin(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REPORT,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REPORT_NOT_LOGIN
        ).getEvent());
    }

    public static void eventPDPSendMessage(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.MESSAGE_SHOP,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.MESSAGE_SHOP
        ).getEvent());
    }

    public static void eventPDPFavorite(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FAVORITE_SHOP,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.FAVORITE_SHOP
        ).getEvent());
    }

    public static void eventWishlistAll(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_RECOMMENDATION,
                AppEventTracking.EventLabel.VIEW_ALL_WISHLIST
        ).getEvent());
    }

    public static void eventFavoriteShop(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                AppEventTracking.Action.CLICK_SHOP_FAVORITE,
                ""
        ).getEvent());
    }

    public static void eventCartCheckout(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CHECKOUT
        ).getEvent());
    }

    public static void eventDrawerClick(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDeleteShopNotes(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NOTES,
                AppEventTracking.Category.NOTES,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DELETE
        ).getEvent());
    }

    public static void eventManageShopInfo(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_INFO
        ).getEvent());
    }

    public static void eventManageShopShipping(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_SHIPPING
        ).getEvent());
    }

    public static void eventManageShopPayment(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_PAYMENT
        ).getEvent());
    }

    public static void eventManageShopEtalase(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_ETALASE
        ).getEvent());
    }

    public static void eventManageShopNotes(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_NOTES
        ).getEvent());
    }

    public static void eventManageShopLocation(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_LOCATION
        ).getEvent());
    }

    public static void eventShopDeleteAddress(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.LOCATION,
                AppEventTracking.Category.LOCATION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DELETE
        ).getEvent());
    }

    public static void eventSuccessReport(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.REPORT_SUCCESS,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.REPORT_SUCCESS,
                AppEventTracking.EventLabel.REPORT_SUCCESS
        ).getEvent());
    }

    public static void eventShopTabSelected(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SALES,
                AppEventTracking.Category.SALES,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDepositTopUp(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.DEPOSIT,
                AppEventTracking.Category.DEPOSIT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TOPUP
        ).getEvent());
    }

    public static void eventDepositWithdraw(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.DEPOSIT,
                AppEventTracking.Category.DEPOSIT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.WITHDRAW
        ).getEvent());
    }

    public static void eventOTPSend(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.OTP,
                AppEventTracking.Category.OTP,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SEND
        ).getEvent());
    }

    public static void eventOTPVerif(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.OTP,
                AppEventTracking.Category.OTP,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.VERIFICATION
        ).getEvent());
    }

    public static void eventCTAAction(Context context) {
        eventCTAAction(context, AppEventTracking.EventLabel.CTA);
    }

    public static void eventCTAAction(Context context, String channel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.LOGIN_CLICK,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.CLICK,
                channel
        ).getEvent());
    }

    public static void eventLoginError(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.LOGIN_ERROR,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.LOGIN_ERROR,
                label
        ).getEvent());
    }

    public static void eventAppsFlyerViewListingSearch(Context context, List<ProductItem> model, String keyword) {
        JSONArray afProdIds = new JSONArray();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if (model.size() > 0) {
            for (int i = 0; i < model.size(); i++) {
                if (i < 3) {
                    prodIdArray.add(model.get(i).getId());
                    afProdIds.put(model.get(i).getId());
                } else {
                    break;
                }
            }
        }

        eventAppsFlyerViewListingSearch(context, afProdIds, keyword, prodIdArray);
        eventAppsFlyerContentView(context, afProdIds, keyword, prodIdArray);
    }

    public static void eventPDPDetail(Context context, ProductDetail productDetail) {
        getGTMEngine(context)
                .eventDetail(productDetail);
    }

    public static void eventATCSuccess(Context context, GTMCart cart) {
        getGTMEngine(context)
                .eventAddtoCart(cart)
                .sendScreen(AppScreen.SCREEN_ADD_TO_CART_SUCCESS)
                .clearAddtoCartDataLayer(GTMCart.ADD_ACTION);
    }

    public static void eventATCRemove(Context context) {
        GTMCart gtmCart = new GTMCart();
        gtmCart.setCurrencyCode("IDR");
        gtmCart.setAddAction(GTMCart.REMOVE_ACTION);

        getGTMEngine(context)
                .eventAddtoCart(gtmCart)
                .sendScreen(AppScreen.SCREEN_CART_PAGE_REMOVE)
                .clearAddtoCartDataLayer(GTMCart.REMOVE_ACTION);
    }

    public static void sendAFCompleteRegistrationEvent(Context context, int userId,String methodName) {
        Map<String, Object> eventVal = new HashMap<>();
        eventVal.put("custom_prop1", "registration");
        eventVal.put("os", "Android");
        eventVal.put(CUSTOMER_USER_ID,userId);
        eventVal.put(REGSITRATION_METHOD,methodName);
        getAFEngine(context).sendTrackEvent(AFInAppEventType.COMPLETE_REGISTRATION, eventVal);
    }

    public static void eventGoldMerchantSuccess(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.GMSUBSCRIBE,
                AppEventTracking.Category.GOLD_MERCHANT,
                AppEventTracking.Action.SUBSCRIBE,
                AppEventTracking.EventLabel.SUBSCRIBE_SUCCESS
        ).getEvent());
    }


    public static void eventTrueCaller(Context context, String userId) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.TRUECALLER,
                AppEventTracking.Category.TRUECALLER,
                AppEventTracking.Action.INSTALLED,
                userId
        ).getEvent());
    }

    public static void eventClickCatalog(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CATALOG,
                AppEventTracking.Category.CATALOG,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventClickOfficialStore(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.OFFICIAL_STORE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventLoadGMStat(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.LOAD_GM_STAT,
                AppEventTracking.Category.FULLY_LOAD,
                AppEventTracking.Action.LOAD,
                AppEventTracking.EventLabel.STATISTIC_PAGE
        ).getEvent());
    }

    public static void eventScrollGMStat(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SCROLL_GM_STAT,
                AppEventTracking.Category.GM_STAT,
                AppEventTracking.Action.SCROLL,
                AppEventTracking.EventLabel.ONE_HUNDRED_PERCENT
        ).getEvent());
    }

    public static void eventClickAddProduct(Context context, String eventCategory, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_ADD_PRODUCT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventClickAPManageProductPlus(Context context) {
        eventClickAddProduct(context,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.EventLabel.ADD_PRODUCT_PLUS
        );
    }

    public static void eventClickAPManageProductTop(Context context) {
        eventClickAddProduct(context,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.EventLabel.ADD_PRODUCT_TOP
        );
    }

    public static void eventClickAddProduct(Context context) {
        eventClickAddProduct(context,
                AppEventTracking.Category.GM_STATISTIC_PRODUCT,
                AppEventTracking.EventLabel.ADD_PRODUCT
        );
    }

    public static void eventClickGMStatProduct(Context context, String eventLabel) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_PRODUCT, eventLabel);
    }

    public static void eventClickGMStat(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.HAMBURGER,
                AppEventTracking.EventLabel.STATISTIC);
    }

    public static void eventClickGMStatSeeDetailTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GMSTAT_TRANSACTION,
                AppEventTracking.EventLabel.SEE_DETAIL);
    }

    public static void eventClickGMStatBuyGMDetailTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STAT,
                AppEventTracking.EventLabel.BUY_GM);
    }

    public static void eventClickGMStatDatePickerWOCompareTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_DATE_PICKER,
                AppEventTracking.EventLabel.CHANGE_DATE);
    }

    public static void eventClickGMStatDatePickerWCompareTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_DATE_PICKER,
                AppEventTracking.EventLabel.CHANGE_DATE_COMPARE);
    }

    public static void eventClickGMStatManageTopAds(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_TOP_ADS,
                AppEventTracking.EventLabel.MANAGE_TOPADS);
    }

    public static void eventClickGMStatProductSoldTransaction(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_TRANSACTION_DETAIL,
                AppEventTracking.EventLabel.PRODUCT_SOLD);
    }

    public static void eventClickGMStatFilterNameTransaction(Context context, String filterName) {
        eventClickFilterGMStat(context, AppEventTracking.Category.GM_STATISTIC_TRANSACTION_DETAIL,
                AppEventTracking.EventLabel.GRAPH_X + filterName);
    }

    public static void eventClickGMStatFilterTypeProductSold(Context context, String filterType) {
        eventClickFilterGMStat(context, AppEventTracking.Category.GM_STATISTIC_PRODUCT_SOLD, filterType);
    }

    private static void eventClickGMStat(Context context, String eventCategory, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_GM_STAT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    private static void eventClickFilterGMStat(Context context, String eventCategory, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_GM_STAT,
                eventCategory,
                AppEventTracking.Action.FILTER,
                eventLabel
        ).getEvent());
    }

    public static void eventClickGMStatMarketInsight(Context context) {
        eventClickGMStat(context, AppEventTracking.Category.GM_STATISTIC_PRODUCT_INSIGHT,
                AppEventTracking.EventLabel.ADD_PRODUCT);
    }

    public static void eventClickInstoped(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_INSTOPED,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.INSTOPED
        ).getEvent());
    }

    public static void eventChangeCurrencyProductList(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_PRICE_PRODUCT_LIST);
    }

    public static void eventChangeCurrencyDropDown(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_PRICE_DROP_DOWN);
    }

    public static void eventCopyProduct(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.COPY_PRODUCT);
    }

    public static void eventMoRegistrationStart(Context context, String medium) {
        getMoEngine(context).sendRegistrationStartEvent(medium);
    }

    public static void eventMoRegister(Context context, String name, String mobileNo) {
        CommonUtils.dumper("GAv4 moengage action " + name + " " + mobileNo);
        getMoEngine(context).sendRegisterEvent(name, mobileNo);
    }

    public static void eventOTPSuccess(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.OTP_SUCCESS,
                AppEventTracking.EventLabel.OTP
        ).getEvent());
    }

    public static void eventChangeCategoryProductGear(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_CATEGORY_PRODUCT_GEAR);
    }

    public static void eventChangeCategoryProductTopMenu(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_CATEGORY_PRODUCT_TOPMENU);
    }

    public static void eventChangeEtalaseProductGear(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_ETALASE_PRODUCT_GEAR);
    }

    public static void eventChangeEtalaseProductTopMenu(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_ETALASE_PRODUCT_TOPMENU);
    }

    public static void eventChangeInsuranceProductGear(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_INSURANCE_PRODUCT_GEAR);
    }

    public static void eventChangeInsuranceProductTopMenu(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.CHANGE_INSURANCE_PRODUCT_TOPMENU);
    }

    public static void eventDeleteProductGear(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.DELETE_PRODUCT_GEAR);
    }

    public static void eventDeleteProductTopMenu(Context context) {
        eventManageProductClicked(context, AppEventTracking.EventLabel.DELETE_PRODUCT_TOPMENU);
    }

    public static void eventClickImageInAddProduct(Context context, String itemSettings) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_MANAGE_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.CLICK_IMAGE_SETTINGS,
                itemSettings
        ).getEvent());
    }

    public static void eventClickImageInEditProduct(Context context, String itemSettings) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_MANAGE_PRODUCT,
                AppEventTracking.Category.EDIT_PRODUCT,
                AppEventTracking.Action.CLICK_IMAGE_SETTINGS,
                itemSettings
        ).getEvent());
    }

    public static void eventClickSaveEditImageProduct(Context context, String editSequence) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_MANAGE_PRODUCT,
                AppEventTracking.Category.EDIT_PRODUCT_IMAGE,
                AppEventTracking.Action.CLICK_SAVE_EDIT,
                editSequence
        ).getEvent());
    }

    public static void eventClickGMSwitcher(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.TOP_SELLER,
                AppEventTracking.Category.GM_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }


    public static void eventPersonalizedClicked(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label
        ).getEvent());
    }

    public static void eventImageUploadSuccessInstagram(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.UPLOAD_SUCCESS,
                AppEventTracking.EventLabel.INSTAGRAM_IMG_PICKER
        ).getEvent());
    }

    public static void eventSlideBannerClicked(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SLIDE_BANNER,
                AppEventTracking.Category.SLIDER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventPushNotifLowTopadsReceived(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECEIVED_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.RECEIVED,
                AppEventTracking.EventLabel.TOPADS_LOW_CREDIT
        ).getEvent());
    }

    public static void eventPushNotifSuccessTopadsReceived(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECEIVED_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.RECEIVED,
                AppEventTracking.EventLabel.TOPADS_SUCCESS_TOPUP
        ).getEvent());
    }

    public static void eventOpenTopadsPushNotification(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label
        ).getEvent());
    }

    public static void eventSmartLock(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SUCCESS_SMART_LOCK,
                AppEventTracking.Category.SMART_LOCK,
                AppEventTracking.Action.SUCCESS,
                label
        ).getEvent());
    }

    public static void eventViewAllOSNonLogin(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_VIEW_ALL_OS,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.OS_MICROSITE_NON_LOGIN
        ).getEvent());
    }

    public static void eventViewAllOSLogin(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_VIEW_ALL_OS,
                AppEventTracking.Category.HOMEPAGE_OFFICIAL_STORE_WIDGET,
                AppEventTracking.Action.CLICK_VIEW_ALL,
                ""
        ).getEvent());
    }

    public static void eventClickOsBanner(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_AD_BANNER,
                AppEventTracking.Category.OS_AD_BANNER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventImpressionOsBanner(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_AD_BANNER,
                AppEventTracking.Category.OS_AD_BANNER,
                AppEventTracking.Action.IMPRESSION,
                label
        ).getEvent());
    }

    public static void eventBannerEmptyFeedOS(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_OS_BANNER_EMPTY_FEED,
                AppEventTracking.Category.PRODUCT_FEED,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.VIEW_ALL_OFFICIAL_STORE_EMPTY_FEED
        ).getEvent());
    }

    public static void eventAddProductErrorServer(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR_SERVER,
                label
        ).getEvent());
    }

    public static void eventAddProductError(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR,
                label
        ).getEvent());
    }

    public static void eventAddProductAdd(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ADD,
                label
        ).getEvent());
    }

    public static void eventAddProductEdit(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_EDIT_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_EDIT,
                label
        ).getEvent());
    }

    public static void eventAddProductAddMore(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ADD_MORE,
                label
        ).getEvent());
    }

    public static void eventManageProductClicked(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_MANAGE_PRODUCT,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDraftProductClicked(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_DRAFT_PRODUCT,
                AppEventTracking.Category.DRAFT_PRODUCT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }


    public static void eventClickCategoriesIcon(Context context, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT + el
        ).getEvent());
    }

    // cart digital

    public static void eventClickViewAllPromo(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE_BANNER,
                AppEventTracking.Action.CLICK_VIEW_ALL,
                ""
        ).getEvent());
    }

    public static void eventClickVoucher(Context context, String ec, String ea, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECHARGE_TRACKING,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.CLICK_USE_VOUCHER + ea,
                AppEventTracking.EventLabel.PRODUCT + el
        ).getEvent());
    }

    public static void eventClickCancelVoucher(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECHARGE_TRACKING,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.CLICK_CANCEL_VOUCHER,
                AppEventTracking.EventLabel.PRODUCT + el
        ).getEvent());
    }

    public static void eventClickBeliInstantSaldoWidget(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.CLICK_BELI_INSTANT_SALDO_WIDGET,
                el
        ).getEvent());
    }

    public static void eventClickBeliWidget(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.CLICK_BELI_WIDGET,
                el
        ).getEvent());
    }

    public static void eventViewCheckoutPage(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.VIEW_CHECKOUT_PAGE,
                el
        ).getEvent());
    }

    public static void eventClickLanjutCheckoutPage(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECHARGE_TRACKING,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.CLICK_LANJUT_CHECKOUT,
                el
        ).getEvent());
    }

    public static void eventClickTruecaller(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRUECALLER_ATTEMPT
        ).getEvent());
    }

    public static void eventClickTruecallerConfirm(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRUECALLER_CONFIRM
        ).getEvent());
    }

    public static void eventTruecallerImpression(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMPRESSION_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.IMPRESSION,
                AppEventTracking.EventLabel.TRUECALLER
        ).getEvent());
    }

    public static void eventGMSwitcherManageShop(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE_GM_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventTopAdsSwitcher(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.TOPADS_SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.OPEN_TOP_SELLER + label
        ).getEvent());
    }

    public static void eventOpenShopSwitcher(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.OPENSHOP_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDownloadFromSwitcher(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DOWNLOAD_APP
        ).getEvent());
    }

    public static void eventVoucherError(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECHARGE_TRACKING,
                AppEventTracking.Category.RECHARGE,
                AppEventTracking.Action.VOUCHER_ERROR + action,
                label
        ).getEvent());
    }

    public static void eventVoucherSuccess(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.RECHARGE_TRACKING,
                AppEventTracking.Category.RECHARGE,
                AppEventTracking.Action.VOUCHER_SUCCESS + action,
                label
        ).getEvent());
    }

    public static void eventClickPhoneIcon(Context context, String category, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + category,
                AppEventTracking.Action.CLICK_PHONEBOOK_ICON,
                AppEventTracking.EventLabel.PRODUCT + label
        ).getEvent());
    }


    public static void eventClickDaftarTransaksiEvent(Context context, String category, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + category,
                AppEventTracking.Action.CLICK_DAFTAR_TX,
                AppEventTracking.EventLabel.PRODUCT + label
        ).getEvent());
    }

    public static void eventSwitchRpToDollarAddProduct(Context context) {
        eventClickAddProduct(context, AppEventTracking.Category.ADD_PRODUCT, AppEventTracking.EventLabel.GOLD_MERCHANT_CURRENCY);
    }

    public static void eventClickVideoAddProduct(Context context) {
        eventClickAddProduct(context, AppEventTracking.Category.ADD_PRODUCT, AppEventTracking.EventLabel.GOLD_MERCHANT_VIDEO);
    }

    public static void eventClickYesGoldMerchantAddProduct(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_GOLD_MERCHANT,
                AppEventTracking.Category.GOLD_MERCHANT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY_GM_ADD_PRODUCT
        ).getEvent());
    }

    public static void eventClickGoldMerchantViaDrawer(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY_GM
        ).getEvent());
    }

    public static void eventImpressionAppUpdate(Context context, boolean isForceUpdate) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.IMPRESSION_APP_UPDATE,
                AppEventTracking.Category.APP_UPDATE,
                AppEventTracking.Action.IMPRESSION,
                isForceUpdate ?
                        AppEventTracking.EventLabel.FORCE_APP_UPDATE :
                        AppEventTracking.EventLabel.OPTIONAL_APP_UPDATE
        ).getEvent());
    }

    public static void eventClickAppUpdate(Context context, boolean isForceUpdate) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_APP_UPDATE,
                AppEventTracking.Category.APP_UPDATE,
                AppEventTracking.Action.CLICK,
                isForceUpdate ?
                        AppEventTracking.EventLabel.FORCE_APP_UPDATE :
                        AppEventTracking.EventLabel.OPTIONAL_APP_UPDATE
        ).getEvent());
    }

    public static void eventClickCancelAppUpdate(Context context, boolean isForceUpdate) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_CANCEL_APP_UPDATE,
                AppEventTracking.Category.APP_UPDATE,
                AppEventTracking.Action.CLICK,
                isForceUpdate ?
                        AppEventTracking.EventLabel.FORCE_CANCEL_APP_UPDATE :
                        AppEventTracking.EventLabel.OPTIONAL_CANCEL_APP_UPDATE
        ).getEvent());
    }

    public static void eventWidgetInstalled(Context context) {
        eventWidget(context,AppEventTracking.Action.INSTALL, AppEventTracking.EventLabel.WIDGET_ORDER);
    }

    public static void eventWidgetRemoved(Context context) {
        eventWidget(context,AppEventTracking.Action.REMOVE, AppEventTracking.EventLabel.WIDGET_ORDER);
    }

    public static void eventAccessAppViewWidget(Context context) {
        eventWidget(context,AppEventTracking.Action.CLICK, AppEventTracking.EventLabel.TO_APP_ORDER);
    }

    public static void eventWidget(Context context, String action, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SELLER_WIDGET,
                AppEventTracking.Category.SELLER_APP_WIDGET,
                action,
                eventLabel
        ).getEvent());
    }

    public static void eventDrawerTopads(Context context) {
        eventDrawerClick(context,AppEventTracking.EventLabel.TOPADS);
    }

    public static void eventDrawerSellerHome(Context context) {
        eventDrawerClick(context,AppEventTracking.EventLabel.SELLER_HOME);
    }

    public static void eventUssd(Context context, String action, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + AppEventTracking.Category.PULSA,
                action,
                eventLabel
        ).getEvent());
    }

    public static void eventUssdAttempt(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_IMPRESSION_HOME_PAGE,
                AppEventTracking.Category.DIGITAL + AppEventTracking.Category.PULSA,
                AppEventTracking.Action.USSD_ATTEMPT,
                eventLabel
        ).getEvent());
    }

    public static void eventClickPaymentAndTopupOnDrawer(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PAYMENT_AND_TOPUP
        ).getEvent());
    }

    public static void eventClickDigitalTransactionListOnDrawer(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DIGITAL_TRANSACTION_LIST
        ).getEvent());
    }

    public static void eventTopAds(Context context, String category, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
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

    public static void eventTopAdsProductStatisticDashboard(Context context, String statisticOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.STATISTIC_DASHBOARD + statisticOption);
    }

    public static void eventTopAdsProductStatisticBar(Context context, String statisticOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.STATISTIC_BAR + statisticOption);
    }

    public static void eventTopAdsProductGroupsFilter(Context context, String groupFilterOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.GROUPS_FILTER + groupFilterOption);
    }

    public static void eventTopAdsProductNewKeyword(Context context, String keywordTypeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.KEYWORD_POSITIF + keywordTypeOption);
    }

    public static void eventTopAdsProductNewKeywordNegatif(Context context, String keywordTypeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.KEYWORD_NEGATIF + keywordTypeOption);
    }

    public static void eventTopAdsProductDeleteGrup(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DELETE_GROUP);
    }

    public static void eventTopAdsProductMainPageDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_MAIN_PAGE + periodOption);
    }

    public static void eventTopAdsProductMainPageDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_MAIN_PAGE);
    }

    public static void eventTopAdsProductStatistikDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_STATISTIK + periodOption);
    }

    public static void eventTopAdsProductStatistikDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_STATISTIK);
    }

    public static void eventTopAdsProductPageGroupDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_GROUP + periodOption);
    }

    public static void eventTopAdsProductPageGroupDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_GROUP);
    }

    public static void eventTopAdsProductPageProductDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_PRODUCT + periodOption);
    }

    public static void eventTopAdsProductPageProductDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_PRODUCT);
    }

    public static void eventTopAdsProductDetailGroupPageDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_GROUP_DETAIL + periodOption);
    }

    public static void eventTopAdsProductDetailGroupPageDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_GROUP_DETAIL);
    }

    public static void eventTopAdsProductDetailProductPageDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION_PRODUCT_DETAIL + periodOption);
    }

    public static void eventTopAdsProductDetailProductPageDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM_PRODUCT_DETAIL);
    }

    public static void eventTopadsEditGroupPromoAddProduct(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_ADD_PRODUCT);
    }

    public static void eventTopAdsProductNewPromo(Context context, String promoOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO + promoOption);
    }

    public static void eventTopAdsProductNewPromoGroup(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO_GROUP);
    }

    public static void eventTopAdsProductNewPromoProduct(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO_PRODUCT);
    }

    public static void eventTopAdsProductNewPromoKeywordPositif(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO_KEYWORD_POSITIF);
    }

    public static void eventTopAdsProductNewPromoKeywordNegatif(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PROMO_KEYWORD_NEGATIVE);
    }

    public static void eventTopAdsProductAddPromoStep1(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_GROUP_STEP_1);
    }

    public static void eventTopAdsProductAddPromoStep2(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_GROUP_STEP_2 + budgetOption);
    }

    public static void eventTopAdsProductAddPromoStep3(Context context, String showTimeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_GROUP_STEP_3 + showTimeOption);
    }

    public static void eventTopAdsProductAddPromoExistingGroupStep1(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PRODUCT_EXISTING_GROUP_STEP_1);
    }

    public static void eventTopAdsProductAddPromoWithoutGroupStep1(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PRODUCT_WITHOUT_GROUP_STEP_1);
    }

    public static void eventTopAdsProductAddPromoWithoutGroupStep2(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.ADD_PRODUCT_WITHOUT_GROUP_STEP_2 + budgetOption);
    }

    public static void eventTopAdsProductFilter(Context context, String filterOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PRODUCT + filterOption);
    }

    public static void eventTopAdsProductClickDetailGroupPDP(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DETAIL_PROMO_PRODUCT_PDP);
    }

    public static void eventTopAdsProductClickDetailProductPDP(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DETAIL_PROMO_PRODUCT_GROUP);
    }

    public static void eventTopAdsProductEditGroupName(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_NAME);
    }

    public static void eventTopAdsProductEditGrupManage(Context context, String groupOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_MANAGE_GROUP + groupOption);
    }

    public static void eventTopAdsProductEditGrupCost(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_COST + budgetOption);
    }

    public static void eventTopAdsProductEditProductCost(Context context, String budgetOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_PRODUCT_COST + budgetOption);
    }

    public static void eventTopAdsProductEditGrupSchedule(Context context, String showTimeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_GROUP_SCHEDULE + showTimeOption);
    }

    public static void eventTopAdsProductEditProductSchedule(Context context, String showTimeOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.EDIT_PRODUCT_SCHEDULE + showTimeOption);
    }

    public static void eventTopAdsShopDatePeriod(Context context, String periodOption) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PERIOD_OPTION + periodOption);
    }

    public static void eventTopAdsShopChooseDateCustom(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.DATE_CUSTOM);
    }

    public static void eventTopAdsShopStatistic(Context context, String statisticOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.STATISTIC_WITH_DASH + statisticOption);
    }

    public static void eventTopAdsShopStatisticBar(Context context, String statisticOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.STATISTIC_BAR + statisticOption);
    }

    public static void eventTopAdsShopAddPromoBudget(Context context, String budgetOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.ADD_SHOP_PROMO_BUDGET + budgetOption);
    }

    public static void eventTopAdsShopAddPromoShowTime(Context context, String showTimeOption) {
        eventTopAdsShop(context, AppEventTracking.EventLabel.ADD_SHOP_PROMO_SHOWTIME + showTimeOption);
    }

    public static void eventTopAdsProductClickGroupDashboard(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.GROUP);
    }

    public static void eventTopAdsProductClickProductDashboard(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.PRODUCT_WITHOUT_DASH);
    }

    public static void eventTopAdsProductClickKeywordDashboard(Context context) {
        eventTopAdsProduct(context, AppEventTracking.EventLabel.KEYWORD);
    }

    public static void eventCheckoutGoldMerchant(Context context, String eventCategory, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
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
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_CREATE_SHOP,
                AppEventTracking.Category.CREATE_SHOP,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventLoginCreateShopSellerApp(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.ENTER);
    }

    public static void eventClickCreateShopOnBoardingSellerApp(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.OPEN_SHOP_ONBOARDING);
    }

    public static void eventCreateShopFillBiodata(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.CONTINUE_SHOP_BIODATA);
    }

    public static void eventCreateShopFillBiodataError(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.CONTINUE_SHOP_BIODATA_ERROR);
    }

    public static void eventCreateShopFillLogistic(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.SAVE_LOGISTIC);
    }

    public static void eventCreateShopFillLogisticError(Context context) {
        eventCreateShopSellerApp(context, AppEventTracking.EventLabel.SAVE_LOGISTIC_ERROR);
    }

    public static void eventOpportunity(Context context, String event, String category,
                                        String action, String label) {
        sendGTMEvent(context, new EventTracking(
                event,
                category,
                action,
                label)
                .getEvent());
    }

    public static void eventOpportunityCustom(Context context, String event, String category,
                                              String action, String label,
                                              HashMap<String, String> customDimension) {
        sendGTMEvent(context, new EventTracking(
                event,
                category,
                action,
                label)
                .setCustomDimension(customDimension)
                .getEvent()
        );
    }

    public static void eventTopChatSearch(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void eventAttachment(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void eventInsertAttachment(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void eventSendAttachment(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void sendChat(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void eventOpenTopChat(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void eventClickTemplate(Context context, String category, String action, String event) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, "").getEvent());
    }

    public static void eventPDPSendChat(Context context) {
        sendGTMEvent(context, new EventTracking(AppEventTracking.Event.PRODUCT_PAGE
                , AppEventTracking.Category.PRODUCT_PAGE
                , AppEventTracking.Action.PRODUCT_PAGE
                , AppEventTracking.EventLabel.PRODUCT_PAGE
        ).getEvent());
    }


    public static void eventSendMessagePage(Context context) {
        sendGTMEvent(context, new EventTracking(AppEventTracking.Event.SEND_MESSAGE_PAGE
                , AppEventTracking.Category.SEND_MESSAGE_PAGE
                , AppEventTracking.Action.SEND_MESSAGE_PAGE
                , ""
        ).getEvent());
    }

    public static void eventShopSendChat(Context context) {
        sendGTMEvent(context, new EventTracking(AppEventTracking.Event.SHOP_PAGE
                , AppEventTracking.Category.SHOP_PAGE
                , AppEventTracking.Action.SHOP_PAGE
                , ""
        ).getEvent());
    }


    public static void eventClickThumbnailMarketing(Context context, String category, String action, String event, String id) {
        sendGTMEvent(context, new EventTracking(
                event, category, action, id).getEvent());
    }


    public static void eventSellerHomeDashboardClick(Context context, String main, String item) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOME_DASHBOARD_CLICK_SELLER,
                AppEventTracking.Category.DASHBOARD,
                AppEventTracking.Action.CLICK + " " + main,
                item)
                .setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId())
                .getEvent());
    }

    public static void eventReferralAndShare(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_APP_SHARE_REFERRAL,
                AppEventTracking.Category.REFERRAL,
                action,
                label
        ).getEvent());
    }

    public static void eventAppShareWhenReferralOff(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_APP_SHARE_WHEN_REFERRAL_OFF,
                AppEventTracking.Category.APPSHARE,
                action,
                label
        ).getEvent());
    }

    public static void eventFeaturedProduct(Context context, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.FEATURED_PRODUCT,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventSellerInfo(Context context, String eventAction, String eventLabel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SELLER_INFO,
                AppEventTracking.Category.SELLER_INFO_HOMEPAGE,
                eventAction,
                eventLabel
        ).getEvent());
    }

    public static void eventClickMenuSellerInfo(Context context) {
        eventSellerInfo(context,AppEventTracking.Action.CLICK_HAMBURGER_ICON, AppEventTracking.EventLabel.SELLER_INFO);
    }

    public static void eventClickItemSellerInfo(Context context, String articleName) {
        eventSellerInfo(context,AppEventTracking.Action.CLICK_ARTICLE, articleName);
    }

    public static void eventClickMenuFeaturedProduct(Context context) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.FEATURED_PRODUCT);
    }

    public static void eventClickAddFeaturedProduct(Context context) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.ADD_FEATURED_PRODUCT);
    }

    public static void eventTickErrorFeaturedProduct(Context context) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.TICK_ERROR);
    }

    public static void eventSavePickFeaturedProduct(Context context, String counterProduct) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.SAVE_FEATURED_PRODUCT_PICKER + counterProduct);
    }

    public static void eventSortFeaturedProductChange(Context context) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.SORT_FEATURED_PRODUCT_CHANGE);
    }

    public static void eventSortFeaturedProductNotChange(Context context) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.SORT_FEATURED_PRODUCT_NO_CHANGE);
    }

    public static void eventDeleteFeaturedProduct(Context context) {
        eventFeaturedProduct(context,AppEventTracking.EventLabel.DELETE_FEATURED_PRODUCT);
    }

    public static void eventClickCart(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_TOP_NAV,
                AppEventTracking.Category.EVENT_TOP_NAV,
                AppEventTracking.Action.CLICK_CART_BUTTON,
                ""
        ).getEvent());
    }

    public static void eventProductManage(Context context, String action, String label) {
        sendGTMEvent(context, new EventTracking(
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

    public static void eventProductManageOverflowMenu(Context context, String label) {
        eventProductManage(context, AppEventTracking.Action.CLICK_OVERFLOW_MENU, label);
    }

    // digital widget

    public static void eventSelectOperatorOnWidget(Context context, String categoryItem, String operator) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                AppEventTracking.Action.SELECT_OPERATOR,
                categoryItem + " - " + operator
        ).getEvent());
    }

    public static void eventSelectProductOnWidget(Context context, String categoryItem, String product) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                AppEventTracking.Action.SELECT_PRODUCT,
                categoryItem + " - " + product
        ).getEvent());
    }

    public static void eventClickBuyOnWidget(Context context, String categoryItem, String instant) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                AppEventTracking.Action.CLICK_BELI_LOWER + " - " + categoryItem,
                instant
        ).getEvent());
    }

    // digital user profile

    public static void eventSelectNumberOnUserProfileNative(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_USER_PROFILE,
                AppEventTracking.Category.RECHARGE + label,
                AppEventTracking.Action.SELECT_NUMBER_ON_USER_PROFILE,
                label
        ).getEvent());
    }

    public static void eventSelectNumberOnUserProfileWidget(Context context, String label) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_USER_PROFILE,
                AppEventTracking.Category.HOMEPAGE_DIGITAL_WIDGET,
                AppEventTracking.Action.SELECT_NUMBER_ON_USER_PROFILE,
                label
        ).getEvent());
    }

    // digital native

    public static void eventSelectOperatorOnNativePage(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.DIGITAL + ec,
                AppEventTracking.Action.SELECT_OPERATOR,
                AppEventTracking.EventLabel.PRODUCT + el
        ).getEvent());
    }

    public static void eventSelectProductOnNativePage(Context context, String categoryName, String productName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.DIGITAL + categoryName,
                AppEventTracking.Action.SELECT_PRODUCT,
                AppEventTracking.EventLabel.PRODUCT + productName
        ).getEvent());
    }

    public static void eventCheckInstantSaldo(Context context, String ec, String el, boolean toggle) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + ec,
                toggle ? AppEventTracking.Action.CHECK_INSTANT_SALDO : AppEventTracking.Action.UNCHECK_INSTANT_SALDO,
                AppEventTracking.EventLabel.PRODUCT + el
        ).getEvent());
    }

    public static void eventClickBuyOnNative(Context context, String categoryItem, String instant) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.DIGITAL + categoryItem,
                AppEventTracking.Action.CLICK_BELI_LOWER + " - " + categoryItem,
                instant
        ).getEvent());
    }

    public static void eventClickSearchBar(Context context, String ec, String el) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE + ec,
                AppEventTracking.Action.CLICK_SEARCH_BAR,
                AppEventTracking.EventLabel.PRODUCT + el
        ).getEvent());
    }

    // digital homepage

    public static void eventClickProductOnDigitalHomepage(Context context, String category) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE_DIGITAL,
                AppEventTracking.Action.SELECT_CATEGORY,
                category
        ).getEvent());
    }

    public static void eventMyCouponClicked(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKO_POINTS_PROMO_COUPON_PAGE,
                AppEventTracking.Action.CLICK_MY_COUPON,
                AppEventTracking.EventLabel.MY_COUPON
        ).getEvent());
    }

    public static void eventCouponChosen(Context context, String couponName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKO_POINTS_PROMO_COUPON_PAGE,
                AppEventTracking.Action.CHOOSE_COUPON,
                couponName
        ).getEvent());
    }

    public static void eventCouponPageClosed(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKO_POINTS_PROMO_COUPON_PAGE,
                AppEventTracking.Action.CLOSE_COUPON_PAGE,
                AppEventTracking.EventLabel.CLOSE_COUPON_PAGE
        ).getEvent());
    }

    public static void eventUserClickedPoints(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOMEPAGE,
                AppEventTracking.Category.TOKO_POINTS_PROMO_HOMEPAGE,
                AppEventTracking.Action.CLICK_TOKO_POINTS_STATUS,
                AppEventTracking.EventLabel.TOKOPOINTS_LABEL
        ).getEvent());
    }

    public static void eventViewTokopointPopup(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKOPOINTS_POP_UP,
                AppEventTracking.Action.TOKOPOINTS_POP_UP_IMPRESSION,
                AppEventTracking.EventLabel.TOKOPOINTS_POP_UP
        ).getEvent());
    }

    public static void eventClickTokoPointPopup(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_TOKO_POINT,
                AppEventTracking.Category.TOKOPOINTS_POP_UP,
                AppEventTracking.Action.TOKOPOINTS_POP_UP_CLICK,
                AppEventTracking.EventLabel.TOKOPOINTS_POP_UP_BUTTON
        ).getEvent());
    }

    public static void eventKolRecommendationViewAllClick(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.FEED_VIEW_ALL_KOL_RECOMMENDATION,
                AppEventTracking.EventLabel.FEED_KOL_RECOMMENDATION_VIEW_ALL
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventKolRecommendationGoToProfileClick(Context context, String kolCategory, String kolName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.FEED_CLICK_KOL_RECOMMENDATION_PROFILE,
                generateKolRecommendationEventLabel(kolCategory, kolName)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventKolRecommendationUnfollowClick(Context context, String kolCategory, String kolName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.FEED_UNFOLLOW_KOL_RECOMMENDATION,
                generateKolRecommendationEventLabel(kolCategory, kolName)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventKolRecommendationFollowClick(Context context, String kolCategory, String kolName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.FEED_FOLLOW_KOL_RECOMMENDATION,
                generateKolRecommendationEventLabel(kolCategory, kolName)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    private static String generateKolRecommendationEventLabel(String kolCategory, String kolName) {
        return kolCategory + " - " + kolName;
    }

    public static void eventOnboardingSkip(Context context, int pageNumber) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_ONBOARDING,
                AppEventTracking.Category.ONBOARDING,
                AppEventTracking.Action.ONBOARDING_SKIP,
                AppEventTracking.EventLabel.ONBOARDING_SKIP_LABEL + pageNumber
        ).getEvent());
    }

    public static void eventOnboardingStartNow(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_ONBOARDING,
                AppEventTracking.Category.ONBOARDING,
                AppEventTracking.Action.ONBOARDING_START,
                AppEventTracking.EventLabel.ONBOARDING_START_LABEL
        ).getEvent());
    }


    //Resolution Tracking
    public static void eventCreateResoStep1SaveAndChooseOther(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PRODUCT_PROBLEM,
                AppEventTracking.EventLabel.RESO_PROBLEM_SAVE_CHOOSE_OTHER
        ).getEvent());
    }

    public static void eventCreateResoStep1Save(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PRODUCT_PROBLEM,
                AppEventTracking.EventLabel.RESO_PROBLEM_SAVE
        ).getEvent());
    }

    public static void eventCreateResoStep1Continue(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PRODUCT_PROBLEM,
                AppEventTracking.EventLabel.RESO_PROBLEM_CONTINUE
        ).getEvent());
    }

    public static void eventCreateResoStep2Continue(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_SOLUTION,
                AppEventTracking.EventLabel.RESO_SOLUTION_CONTINUE
        ).getEvent());
    }

    public static void eventCreateResoStep3Continue(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_PROVE,
                AppEventTracking.EventLabel.RESO_SOLUTION_CONTINUE
        ).getEvent());
    }

    public static void eventCreateResoPre(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO,
                AppEventTracking.EventLabel.RESO_CREATE_COMPLAINT_PRE
        ).getEvent());
    }

    public static void eventCreateResoConfirm(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO,
                AppEventTracking.EventLabel.RESO_CREATE_COMPLAINT_CONFIRM
        ).getEvent());
    }

    public static void eventCreateResoUnconfirm(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO,
                AppEventTracking.EventLabel.RESO_CREATE_COMPLAINT_UNCONFIRM
        ).getEvent());
    }

    public static void eventCreateResoAbandon(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_RESOLUTION,
                AppEventTracking.Category.RESOLUTION_CENTER,
                AppEventTracking.Action.CLICK_CREATE_RESO_ABANDON,
                AppEventTracking.EventLabel.RESO_CREATE_ABANDON
        ).getEvent());
    }

    public static void eventClickChangePhoneNumber(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_CLICK_USER_PROFILE,
                AppEventTracking.Category.PROFILE_PAGE,
                AppEventTracking.Action.CLICK_CHANGE_PHONE_NUMBER,
                ""
        ).getEvent());
    }

    public static void eventSuccessChangePhoneNumber(Context context) {
        sendGTMEvent(context, new EventTracking(
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


    public static void eventPromoListClickCategory(Context context, String categoryName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PROMO_MICRO_SITE,
                AppEventTracking.Category.PROMO_MICROSITE_PROMO_LIST,
                AppEventTracking.Action.PROMO_CLICK_CATEGORY,
                categoryName
        ).getEvent());
    }

    public static void eventPromoListClickSubCategory(Context context, String subCategoryName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PROMO_MICRO_SITE,
                AppEventTracking.Category.PROMO_MICROSITE_PROMO_LIST,
                AppEventTracking.Action.PROMO_CLICK_SUB_CATEGORY,
                subCategoryName
        ).getEvent());
    }

    public static void eventPromoListClickCopyToClipboardPromoCode(Context context, String promoName) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PROMO_MICRO_SITE,
                AppEventTracking.Category.PROMO_MICROSITE_PROMO_LIST,
                AppEventTracking.Action.PROMO_CLICK_COPY_PROMO_CODE,
                promoName
        ).getEvent());
    }

    public static void eventPromoTooltipClickOpenTooltip(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PROMO_MICRO_SITE,
                AppEventTracking.Category.PROMO_MICROSITE_PROMO_TOOLTIP,
                AppEventTracking.Action.PROMO_CLICK_OPEN_TOOLTIP,
                ""
        ).getEvent());
    }

    public static void eventPromoTooltipClickCloseTooltip(Context context) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.CLICK_PROMO_MICRO_SITE,
                AppEventTracking.Category.PROMO_MICROSITE_PROMO_TOOLTIP,
                AppEventTracking.Action.PROMO_CLICK_CLOSE_TOOLTIP,
                ""
        ).getEvent());
    }

    public static void eventSearchResultProductWishlistClick(Context context, boolean isWishlisted, String keyword) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.PRODUCT_VIEW,
                AppEventTracking.Category.SEARCH_RESULT.toLowerCase(),
                AppEventTracking.Action.CLICK_WISHLIST,
                generateWishlistClickEventLabel(isWishlisted, keyword)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static final Locale DEFAULT_LOCALE = new Locale("in", "ID");
    private static String generateWishlistClickEventLabel(boolean isWishlisted, String keyword) {
        String action = isWishlisted ? "add" : "remove";
        return action + " - " + keyword + " - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", DEFAULT_LOCALE).format(new Date());
    }

    public static void eventSearchResultSort(Context context, String screenName, String sortByValue) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SORT_BY,
                AppEventTracking.Action.SORT_BY + " - " + screenName,
                sortByValue
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventSearchResultQuickFilter(Context context, String filterName, String filterValue, boolean isSelected) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.FILTER_PRODUCT,
                AppEventTracking.Action.QUICK_FILTER,
                filterName + " - " + filterValue + " - " + Boolean.toString(isSelected)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }

    public static void eventBillShortcut(Context context ) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.LONG_CLICK,
                AppEventTracking.Category.LONG_PRESS,
                AppEventTracking.Action.CLICK_BILL,
                AppEventTracking.EventLabel.DIGITAL
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventDigitalEventTracking(Context context, String action, String label) {
        Log.d("EVENTSGA", action + " - " + label);
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.EVENT_DIGITAL_EVENT,
                AppEventTracking.Category.DIGITAL_EVENT,
                action,
                label
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }
}
