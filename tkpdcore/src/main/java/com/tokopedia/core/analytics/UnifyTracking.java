package com.tokopedia.core.analytics;

import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.analytics.nishikino.model.GTMCart;
import com.tokopedia.core.analytics.nishikino.model.ProductDetail;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.var.ProductItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * this class contains tracking for Unify (Click, Discovery & View Product, Authentication,
 * Login/Register)
 */

public class UnifyTracking extends TrackingUtils {

    public static final String EXTRA_LABEL = "label";

    public static void eventHomeTab(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventHomeCategory(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOMEPAGE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventHomeTopPicksItem(String action, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.TOP_PICKS,
                AppEventTracking.Category.TOP_PICKS_HOME,
                action,
                label
        ).getEvent());
    }

    /* CATEGORY IMPROVEMENT*/

    public static void eventProductOnCategory(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PRODUCT,
                AppEventTracking.Action.PRODUCT_CATEGORY,
                label
        ).getEvent());
    }

    public static void eventLevelCategory(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_LEVEL,
                label
        ).getEvent());
    }

    public static void eventLevelCategoryIntermediary(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIAR_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_LEVEL,
                label
        ).getEvent());
    }

    public static void eventHotlistIntermediary(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIAR_PAGE+"-"+parentCat,
                AppEventTracking.Action.HOTLIST,
                label
        ).getEvent());
    }

    public static void eventCuratedIntermediary(String parentCat, String curatedProductName,
                                                String productName){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.INTERMEDIARY_PAGE,
                AppEventTracking.Category.INTERMEDIAR_PAGE+"-"+parentCat,
                AppEventTracking.Action.CURATED + " " +curatedProductName,
                productName
        ).getEvent());
    }

    public static void eventShowMoreCategory(String parentCat){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_MORE,
                AppEventTracking.EventLabel.CATEGORY_SHOW_MORE
        ).getEvent());
    }

    public static void eventSortCategory(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_SORT,
                label
        ).getEvent());
    }

    public static void eventFilterCategory(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_FILTER,
                label
        ).getEvent());
    }

    public static void eventDisplayCategory(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_DISLPAY,
                label
        ).getEvent());
    }

    public static void eventShareCategory(String parentCat, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATEGORY_PAGE,
                AppEventTracking.Category.CATEGORY_PAGE+"-"+parentCat,
                AppEventTracking.Action.CATEGORY_SHARE,
                label
        ).getEvent());
    }

     /* CATEGORY IMPROVEMENT*/

    public static void eventHomeTopPicksTitle( String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.TOP_PICKS,
                AppEventTracking.Category.TOP_PICKS_HOME,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventNewOrderDetail(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ORDER_DETAIL
        ).getEvent());
    }

    public static void eventTrackOrder(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.STATUS,
                AppEventTracking.Category.STATUS,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRACK
        ).getEvent());
    }

    public static void eventAcceptOrder(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ACCEPT_ORDER
        ).getEvent());
    }

    public static void eventRejectOrder(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NEW_ORDER,
                AppEventTracking.Category.NEW_ORDER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REJECT_ORDER
        ).getEvent());
    }

    public static void eventConfirmShipping(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION
        ).getEvent());
    }

    public static void eventMessageDetail(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.MESSAGE,
                AppEventTracking.Category.MESSAGE,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventMessageSend(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.MESSAGE,
                AppEventTracking.Category.MESSAGE,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.INBOX + "-" + label
        ).getEvent());
    }

    public static void eventDiscussionProductDetail(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventDiscussionProductSend(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.SEND,
                label
        ).getEvent());
    }

    public static void eventReviewDetail(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REVIEW,
                AppEventTracking.Category.REVIEW,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventReviewDetail(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REVIEW,
                AppEventTracking.Category.REVIEW,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.REVIEW_DETAIL
        ).getEvent());
    }

    public static void eventConfirmShippingDetails(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DETAILS
        ).getEvent());
    }

    public static void eventConfirmShippingCancel(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_SHIPPING,
                AppEventTracking.Category.SHIPPING,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CANCEL
        ).getEvent());
    }

    public static void eventDiscussionDetail(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.DISCUSSION_DETAIL
        ).getEvent());
    }

    public static void eventDiscussionSendSuccess(String from){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.COMMENT + "-" + from
        ).getEvent());
    }

    public static void eventDiscussionSendError(String from){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DISCUSSION,
                AppEventTracking.Category.PRODUCT_DISCUSSION,
                AppEventTracking.Action.ERROR,
                AppEventTracking.EventLabel.COMMENT + "-" + from
        ).getEvent());
    }

    public static void eventResolutionDetail(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.VIEW,
                AppEventTracking.EventLabel.COMPLAINT_DETAIL
        ).getEvent());
    }

    public static void eventResolutionDetail(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventEtalaseAdd(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.ETALASE,
                AppEventTracking.Category.ETALASE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD
        ).getEvent());
    }

    public static void eventForgotPassword(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FORGOT_PASSWORD,
                AppEventTracking.Category.FORGOT_PASSWORD,
                AppEventTracking.Action.RESET_SUCCESS,
                AppEventTracking.EventLabel.RESET_PASSWORD
        ).getEvent());
    }

    public static void eventResendNotification(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RESEND_EMAIL,
                AppEventTracking.Category.EMAIL_ACTIVATION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.RESEND_EMAIL
        ).getEvent());
    }

    public static void eventCreateShop(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CREATE_SHOP,
                AppEventTracking.Category.CREATE_SHOP,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CREATE
        ).getEvent());
    }

    public static void eventCreateShopSuccess(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CREATE_SHOP,
                AppEventTracking.Category.CREATE_SHOP,
                AppEventTracking.Action.SUCCESS,
                AppEventTracking.EventLabel.SHOP_CREATED
        ).getEvent());
    }

    public static void eventResolutionSendSuccess(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.SEND,
                AppEventTracking.EventLabel.COMMENT
        ).getEvent());
    }

    public static void eventResolutionSendError(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.ERROR,
                AppEventTracking.EventLabel.COMMENT
        ).getEvent());
    }

    public static void eventResolutionEditSolution(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RESOLUTION_CENTER,
                AppEventTracking.Category.RESOLUTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.EDIT_SOLUTION
        ).getEvent());
    }

    public static void eventReviewCompleteBuyer(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REVIEW,
                AppEventTracking.Category.REVIEW,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REVIEW_BUYER
        ).getEvent());
    }

    public static void eventReceivedShipping(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RECEIVED,
                AppEventTracking.Category.RECEIVED,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CONFIRMATION
        ).getEvent());
    }

    public static void eventAddProduct(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD
        ).getEvent());
    }

    public static void eventAddProductMore(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_MORE
        ).getEvent());
    }

    public static void eventATCAddAddress(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.ATC,
                AppEventTracking.Category.ADD_TO_CART,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_ADDRESS
        ).getEvent());
    }

    public static void eventATCBuy(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.ATC,
                AppEventTracking.Category.ADD_TO_CART,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }
    public static void eventATCChangeAddress(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.ATC,
                AppEventTracking.Category.ADD_TO_CART,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CHANGE_ADDRESS
        ).getEvent());
    }

    public static void eventDiscoverySearchShop(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP
        ).getEvent());
    }
    public static void eventDiscoverySearchShopDetail(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SHOP_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP
        ).getEvent());
    }
    public static void eventTalkSuccessSend(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.TALK_SUCCESS,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.TALK_SUCCESS,
                AppEventTracking.EventLabel.TALK_SUCCESS
        ).getEvent());
    }

    public static void eventDiscoverySearchCatalog(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH_RESULT,
                AppEventTracking.Category.SEARCH_RESULT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CATALOG
        ).getEvent());
    }

    public static void eventDiscoveryFilter(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FILTER,
                AppEventTracking.Category.FILTER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDiscoverySort(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SORT,
                AppEventTracking.Category.SORT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDiscoverySearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH,
                label
        ).getEvent());
    }

    public static void eventDiscoveryVoiceSearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.VOICE_SEARCH,
                label
        ).getEvent());
    }

    public static void eventCartAbandon(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.BACK_CLICK,
                AppEventTracking.Category.PAYMENT,
                AppEventTracking.Action.ABANDON,
                AppEventTracking.EventLabel.THANK_YOU_PAGE
        ).getEvent());
    }

    public static void eventFeedView(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventFeedView(String action, String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                action,
                label
        ).getEvent());
    }

    public static void eventFeedViewAll(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW_RECENT,
                AppEventTracking.EventLabel.VIEW_ALL_RECENT
        ).getEvent());
    }

    public static void eventCampaign(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CAMPAIGN,
                AppEventTracking.Category.CAMPAIGN,
                AppEventTracking.Action.DEEPLINK,
                label
        ).getEvent());
    }

    public static void eventCartPayment(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PAYMENT_METHOD
        ).getEvent());
    }

    public static void eventCartDeposit(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DEPOSIT_USE
        ).getEvent());
    }

    public static void eventCartDropshipper(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DROPSHIPPER
        ).getEvent());
    }

    public static void eventCartThankYou(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CONFIRM_PAYMENT,
                AppEventTracking.Category.PAYMENT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.THANK_YOU_PAGE
        ).getEvent());
    }

    public static void eventCartPayNow(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PAYMENT,
                AppEventTracking.Category.PAYMENT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PAY_NOW
        ).getEvent());
    }

    public static void eventDiscoveryNoResult(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NO_RESULT,
                AppEventTracking.Category.NO_RESULT,
                AppEventTracking.Action.NO_RESULT,
                label
        ).getEvent());
    }

    public static void eventHomeGimmick(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.GIMMICK,
                AppEventTracking.Category.GIMMICK,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventHomeRechargeTab(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PULSA,
                AppEventTracking.Category.PULSA,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CLICKED_TAB + label
        ).getEvent());
    }

    public static void eventWishlistView(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.VIEW,
                label
        ).getEvent());
    }

    public static void eventWishlistBuy(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventFeedRecent(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.VIEW_RECENT,
                label
        ).getEvent());
    }

    public static void eventHotlist(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Category.HOTLIST,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventFavoriteView(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_WISHLIST,
                label
        ).getEvent());
    }

    public static void eventFavoriteViewRecommendation(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_RECOMMENDATION,
                label
        ).getEvent());
    }

    public static void eventRechargeBuy(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PULSA,
                AppEventTracking.Category.ANDROID_WIDGET,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventRegisterSuccess(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REGISTER_SUCCESS,
                AppEventTracking.Category.REGISTER,
                AppEventTracking.Action.REGISTER_SUCCESS,
                label
        ).getEvent());
    }

    public static void eventLoginSuccess(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.LOGIN,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.LOGIN_SUCCESS,
                label
        ).getEvent());
    }

    public static void eventRegister(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REGISTER,
                AppEventTracking.Category.REGISTER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventRegisterChannel(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REGISTER,
                AppEventTracking.Category.REGISTER,
                AppEventTracking.Action.CLICK_CHANNEL,
                label
        ).getEvent());
    }

    public static void eventRegisterError(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REGISTER_ERROR,
                AppEventTracking.Category.REGISTER,
                AppEventTracking.Action.REGISTER_ERROR,
                label
        ).getEvent());
    }

    public static void eventShareProduct(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE
        ).getEvent());
    }

    public static void eventPDPCart(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.BUY,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY
        ).getEvent());
    }

    public static void eventPDPTalk(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT_TALK
        ).getEvent());
    }

    public static void eventPDPReputation(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REVIEW
        ).getEvent());
    }

    public static void eventPDPWishlit(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_TO_WISHLIST
        ).getEvent());
    }

    public static void eventShare(String source, String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE_TO + label + "-" + source
        ).getEvent());
    }

    public static void eventPDPExpandDescription(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PRODUCT_DESCRIPTION
        ).getEvent());
    }

    public static void eventPDPAddToWishlist(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.WISHLIST,
                AppEventTracking.Category.WISHLIST,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.ADD_TO_WISHLIST_LABEL + MethodChecker.fromHtml(label)
        ).getEvent());
    }

    public static void eventPDPReport(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REPORT,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REPORT
        ).getEvent());
    }
    public static void eventPDPReportNotLogin(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REPORT,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.REPORT_NOT_LOGIN
        ).getEvent());
    }
    public static void eventPDPSendMessage(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.MESSAGE_SHOP,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.MESSAGE_SHOP
        ).getEvent());
    }
    public static void eventPDPFavorite(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE_SHOP,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.FAVORITE_SHOP
        ).getEvent());
    }

    public static void eventWishlistAll(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_RECOMMENDATION,
                AppEventTracking.EventLabel.VIEW_ALL_WISHLIST
        ).getEvent());
    }

    public static void eventFavoriteShop(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FAVORITE,
                AppEventTracking.Category.FAVORITE,
                AppEventTracking.Action.VIEW_RECOMMENDATION,
                label
        ).getEvent());
    }

    public static void eventCartCheckout(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CHECKOUT,
                AppEventTracking.Category.CHECKOUT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.CHECKOUT
        ).getEvent());
    }

    public static void eventDrawerClick(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDeleteShopNotes(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NOTES,
                AppEventTracking.Category.NOTES,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DELETE
        ).getEvent());
    }

    public static void eventManageShopInfo(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_INFO
        ).getEvent());
    }
    public static void eventManageShopShipping(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_SHIPPING
        ).getEvent());
    }
    public static void eventManageShopPayment(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_PAYMENT
        ).getEvent());
    }

    public static void eventManageShopEtalase(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_ETALASE
        ).getEvent());
    }
    public static void eventManageShopNotes(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_NOTES
        ).getEvent());
    }
    public static void eventManageShopLocation(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHOP_LOCATION
        ).getEvent());
    }
    public static void eventShopDeleteAddress(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.LOCATION,
                AppEventTracking.Category.LOCATION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DELETE
        ).getEvent());
    }
    public static void eventSuccessReport(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REPORT_SUCCESS,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.REPORT_SUCCESS,
                AppEventTracking.EventLabel.REPORT_SUCCESS
        ).getEvent());
    }

    public static void eventShopTabSelected(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SALES,
                AppEventTracking.Category.SALES,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDepositTopUp(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.DEPOSIT,
                AppEventTracking.Category.DEPOSIT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TOPUP
        ).getEvent());
    }

    public static void eventDepositWithdraw(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.DEPOSIT,
                AppEventTracking.Category.DEPOSIT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.WITHDRAW
        ).getEvent());
    }

    public static void eventOTPSend(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.OTP,
                AppEventTracking.Category.OTP,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SEND
        ).getEvent());
    }

    public static void eventOTPVerif(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.OTP,
                AppEventTracking.Category.OTP,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.VERIFICATION
        ).getEvent());
    }

    public static void eventRegisterThroughLogin(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.REGISTER_LOGIN,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.REGISTER,
                AppEventTracking.EventLabel.REGISTER
        ).getEvent());
    }

    public static void eventCTAAction(){
        eventCTAAction(AppEventTracking.EventLabel.CTA);
    }

    public static void eventCTAAction(String channel){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.LOGIN_CLICK,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.CLICK,
                channel
        ).getEvent());
    }

    public static void eventLoginError(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.LOGIN_ERROR,
                AppEventTracking.Category.LOGIN,
                AppEventTracking.Action.LOGIN_ERROR,
                label
        ).getEvent());
    }

    public static  void eventAppsFlyerViewListingSearch(java.util.List<ProductItem> model, String keyword){
        JSONArray afProdIds = new JSONArray();
        ArrayList<String> prodIdArray = new ArrayList<>();

        if(model.size() > 0)
        {
            for (int i = 0; i < model.size(); i++){
                if (i < 3) {
                    prodIdArray.add(model.get(i).getId());
                    afProdIds.put(model.get(i).getId());
                } else {
                    break;
                }
            }
        }

        eventAppsFlyerViewListingSearch(afProdIds, keyword, prodIdArray);
        eventAppsFlyerContentView(afProdIds, keyword, prodIdArray);
    }


    public static void eventLocaRegister(String userID){
        getLocaEngine().tagEventWithAttribute(
                "event : "+ "Successful Register",
                userID,
                new String[]{"customer_name", userID},
                new String[]{"Login Type", "E-mail"}
        );
    }

    public static void eventPDPDetail(ProductDetail productDetail){
        getGTMEngine()
                .eventDetail(productDetail)
                .sendScreen(AppScreen.SCREEN_PRODUCT_INFO_DETAIL);
    }

    public static void eventATCSuccess(GTMCart cart){
        getGTMEngine()
                .eventAddtoCart(cart)
                .sendScreen(AppScreen.SCREEN_ADD_TO_CART_SUCCESS)
                .clearAddtoCartDataLayer(GTMCart.ADD_ACTION);
    }

    public static void eventATCRemove(){
        GTMCart gtmCart = new GTMCart();
        gtmCart.setCurrencyCode("IDR");
        gtmCart.setAddAction(GTMCart.REMOVE_ACTION);

        getGTMEngine()
                .eventAddtoCart(gtmCart)
                .sendScreen(AppScreen.SCREEN_CART_PAGE_REMOVE)
                .clearAddtoCartDataLayer(GTMCart.REMOVE_ACTION);
    }

    public static void eventLoginLoca(CustomerWrapper customerWrapper){
        getLocaEngine().sendLoginSuccessfull(customerWrapper);
    }
    public static void eventRegisterLoca(CustomerWrapper customerWrapper){
        getLocaEngine().sendEventRegister(customerWrapper);
    }

    public static void eventLogoutLoca(){
        Map<String, String> attrs = new HashMap<>();
        getLocaEngine().sendEventLoggedOut(attrs);
    }

    public static void eventLocaGoodReview(Integer accuracy, Integer quality) {
        if (accuracy > 3 && quality > 3){
            eventLoca(AppScreen.EVENT_GOOD_REVIEW);
        }
    }

    public static void eventViewATC(){
        eventLoca(AppScreen.SCREEN_VIEWED_ADD_TO_CART);
    }

    public static void eventViewFavStore(){
        eventLoca(AppScreen.SCREEN_VIEWED_FAV_STORE);
    }

    public static void eventViewLoginPage(){
        eventLoca(AppScreen.SCREEN_VIEWED_LOGIN);
    }

    public static void eventViewShopTransactionPage(){
        eventLoca(AppScreen.SCREEN_VIEWED_TRANSACTION_SHOP);
    }

    public static void eventViewWishlist(){
        eventLoca(AppScreen.SCREEN_VIEWED_WISHLIST_PAGE);
    }

    public static void deleteProfileAttrLoca(){
        getLocaEngine().deleteProfileAttribute("profile : last date has product in cart");
    }

    public static void sendAFCompleteRegistrationEvent(){
        Map<String, Object> eventVal = new HashMap<>();
        eventVal.put("advertising_id", "aifa");
        eventVal.put("custom_prop1", "registration");
        eventVal.put("os", "Android");
        getAFEngine().sendTrackEvent(AFInAppEventType.COMPLETE_REGISTRATION, eventVal);
    }

    public static void eventGoldMerchantSuccess() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.GMSUBSCRIBE,
                AppEventTracking.Category.GOLD_MERCHANT,
                AppEventTracking.Action.SUBSCRIBE,
                AppEventTracking.EventLabel.SUBSCRIBE_SUCCESS
        ).getEvent());
    }


    public static void eventTrueCaller(String userId) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.TRUECALLER,
                AppEventTracking.Category.TRUECALLER,
                AppEventTracking.Action.INSTALLED,
                userId
        ).getEvent());
    }

    public static void sendLocaProductDetailEvent(ProductDetailData successResult, Map<String,String> attributes){
        getLocaEngine().sendEventProductView(
                successResult.getInfo().getProductName(),
                Integer.toString(successResult.getInfo().getProductId()),
                successResult.getInfo().getProductCatalogName(),
                attributes
        );
    }

    public static void eventClickCatalog(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CATALOG,
                AppEventTracking.Category.CATALOG,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventClickOfficialStore(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.OFFICIAL_STORE,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventLoadGMStat() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.LOAD_GM_STAT,
                AppEventTracking.Category.FULLY_LOAD,
                AppEventTracking.Action.LOAD,
                AppEventTracking.EventLabel.STATISTIC_PAGE
        ).getEvent());
    }

    public static void eventScrollGMStat() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SCROLL_GM_STAT,
                AppEventTracking.Category.GM_STAT,
                AppEventTracking.Action.SCROLL,
                AppEventTracking.EventLabel.ONE_HUNDRED_PERCENT
        ).getEvent());
    }

    public static void eventClickAddProduct(String eventCategory, String eventLabel){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_ADD_PRODUCT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventClickAPManageProductPlus(){
        eventClickAddProduct(
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.EventLabel.ADD_PRODUCT_PLUS
        );
    }

    public static void eventClickAPManageProductTop(){
        eventClickAddProduct(
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.EventLabel.ADD_PRODUCT_TOP
        );
    }

    public static void eventClickAddProduct() {
        eventClickAddProduct(
                AppEventTracking.Category.GM_STATISTIC_PRODUCT,
                AppEventTracking.EventLabel.ADD_PRODUCT
                );
    }

    public static void eventClickGMStatProduct(String eventLabel) {
        eventClickGMStat(AppEventTracking.Category.GM_STATISTIC_PRODUCT, eventLabel);
    }

    public static void eventClickGMStat(){
        eventClickGMStat(AppEventTracking.Category.HAMBURGER,
                AppEventTracking.EventLabel.STATISTIC);
    }

    private static void eventClickGMStat(String eventCategory, String eventLabel){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_GM_STAT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel
        ).getEvent());
    }

    public static void eventClickGMStatMarketInsight(){
        eventClickGMStat(AppEventTracking.Category.GM_STATISTIC_PRODUCT_INSIGHT,
                AppEventTracking.EventLabel.ADD_PRODUCT);
    }

    public static void eventClickInstoped() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_INSTOPED,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.INSTOPED
        ).getEvent());
    }

    public static void eventChangeCurrencyProductList() {
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_PRICE_PRODUCT_LIST);
    }

    public static void eventChangeCurrencyDropDown() {
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_PRICE_DROP_DOWN);
    }

    public static void eventCopyProduct() {
        eventManageProductClicked(AppEventTracking.EventLabel.COPY_PRODUCT);
    }

    public static void eventMoRegistrationStart(String medium) {
        getMoEngine().sendRegistrationStartEvent(medium);
    }

    public static void eventMoRegister(String name, String mobileNo) {
        getMoEngine().sendRegisterEvent(name,mobileNo);
    }

    public static void eventOTPSuccess(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.EVENT_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.OTP_SUCCESS,
                AppEventTracking.EventLabel.OTP
        ).getEvent());
    }

    public static void eventChangeCategoryProductGear(){
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_CATEGORY_PRODUCT_GEAR);
    }

    public static void eventChangeCategoryProductTopMenu(){
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_CATEGORY_PRODUCT_TOPMENU);
    }

    public static void eventChangeEtalaseProductGear(){
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_ETALASE_PRODUCT_GEAR);
    }

    public static void eventChangeEtalaseProductTopMenu(){
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_ETALASE_PRODUCT_TOPMENU);
    }

    public static void eventChangeInsuranceProductGear(){
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_INSURANCE_PRODUCT_GEAR);
    }

    public static void eventChangeInsuranceProductTopMenu(){
        eventManageProductClicked(AppEventTracking.EventLabel.CHANGE_INSURANCE_PRODUCT_TOPMENU);
    }

    public static void eventDeleteProductGear(){
        eventManageProductClicked(AppEventTracking.EventLabel.DELETE_PRODUCT_GEAR);
    }

    public static void eventDeleteProductTopMenu(){
        eventManageProductClicked(AppEventTracking.EventLabel.DELETE_PRODUCT_TOPMENU);
    }

    public static void eventClickPopularSearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH_POPULAR,
                label
        ).getEvent());
    }

    public static void eventClickRecentSearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH_RECENT,
                label
        ).getEvent());
    }

    public static void eventClickHotListSearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH_HOTLIST,
                label
        ).getEvent());
    }

    public static void eventClickAutoCompleteSearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH_AUTOCOMPLETE,
                label
        ).getEvent());
    }

    public static void eventClickAutoCompleteShopSearch(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH_AUTOCOMPLETE_SHOP,
                label
        ).getEvent());
    }

    public static void eventClickAutoCompleteCategory(String catId, String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SEARCH,
                AppEventTracking.Category.SEARCH,
                AppEventTracking.Action.SEARCH_AUTOCOMPLETE_CATEGORY,
                catId + " | " + label
        ).getEvent());
    }

    public static void eventClickGMSwitcher(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.TOP_SELLER,
                AppEventTracking.Category.GM_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }


    public static void eventPersonalizedClicked(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label
        ).getEvent());
    }

    public static void eventImageUploadSuccessInstagram(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_ADD_PRODUCT,
                AppEventTracking.Category.ADD_PRODUCT,
                AppEventTracking.Action.UPLOAD_SUCCESS,
                AppEventTracking.EventLabel.INSTAGRAM_IMG_PICKER
        ).getEvent());
    }

    public static void eventSlideBannerClicked(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SLIDE_BANNER,
                AppEventTracking.Category.SLIDER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventPushNotifLowTopadsReceived() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RECEIVED_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.RECEIVED,
                AppEventTracking.EventLabel.TOPADS_LOW_CREDIT
        ).getEvent());
    }

    public static void eventPushNotifSuccessTopadsReceived() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RECEIVED_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.RECEIVED,
                AppEventTracking.EventLabel.TOPADS_SUCCESS_TOPUP
        ).getEvent());
    }

    public static void eventOpenTopadsPushNotification(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.OPEN_PUSH_NOTIFICATION,
                AppEventTracking.Category.PUSH_NOTIFICATION,
                AppEventTracking.Action.OPEN,
                label
        ).getEvent());
    }

    public static void eventSmartLock(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SUCCESS_SMART_LOCK,
                AppEventTracking.Category.SMART_LOCK,
                AppEventTracking.Action.SUCCESS,
                label
        ).getEvent());
    }

    public static void eventViewAllOSNonLogin() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_VIEW_ALL_OS,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.OS_MICROSITE_NON_LOGIN
        ).getEvent());
    }

    public static void eventViewAllOSLogin() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_VIEW_ALL_OS,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.OS_MICROSITE_LOGIN
        ).getEvent());
    }

    public static void eventClickOsBanner(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_AD_BANNER,
                AppEventTracking.Category.OS_AD_BANNER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventImpressionOsBanner(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_AD_BANNER,
                AppEventTracking.Category.OS_AD_BANNER,
                AppEventTracking.Action.IMPRESSION,
                label
        ).getEvent());
    }

    public static void eventBannerEmptyFeedOS() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_OS_BANNER_EMPTY_FEED,
                AppEventTracking.Category.PRODUCT_FEED,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.VIEW_ALL_OFFICIAL_STORE_EMPTY_FEED
        ).getEvent());
    }

    public static void eventAddProductErrorServer(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR_SERVER,
                label
        ).getEvent());
    }

    public static void eventAddProductError(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ERROR,
                label
        ).getEvent());
    }

    public static void eventAddProductAdd(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ADD,
                label
        ).getEvent());
    }

    public static void eventAddProductAddMore(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.AddProduct.EVENT_CLICK_ADD_PRODUCT,
                AppEventTracking.AddProduct.CATEGORY_ADD_PRODUCT,
                AppEventTracking.AddProduct.EVENT_ACTION_ADD_MORE,
                label
        ).getEvent());
    }

    public static void eventManageProductClicked(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_MANAGE_PRODUCT,
                AppEventTracking.Category.MANAGE_PRODUCT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDraftProductClicked(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_DRAFT_PRODUCT,
                AppEventTracking.Category.DRAFT_PRODUCT,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventClickBeli(String ec, String el){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE+ec,
                AppEventTracking.Action.CLICK_BELI,
                el
        ).getEvent());
    }


    public static void eventClickBeliInstantSaldo(String ec, String el){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE+ec,
                AppEventTracking.Action.CLICK_BELI_INSTANT_SALDO,
                el
        ).getEvent());
    }


    public static void eventClickBeliInstantSaldoWidget(String ec, String el){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE+ec,
                AppEventTracking.Action.CLICK_BELI_INSTANT_SALDO_WIDGET,
                el
        ).getEvent());
    }


    public static void eventClickBeliWidget(String ec, String el){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE+ec,
                AppEventTracking.Action.CLICK_BELI_WIDGET,
                el
        ).getEvent());
    }


    public static void eventViewCheckoutPage(String ec, String el){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.RECHARGE+ec,
                AppEventTracking.Action.VIEW_CHECKOUT_PAGE,
                el
        ).getEvent());
    }

    public static void eventClickLanjutCheckoutPage(String ec, String el){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.RECHARGE_TRACKING,
                AppEventTracking.Category.RECHARGE+ec,
                AppEventTracking.Action.CLICK_LANJUT_CHECKOUT,
                el
        ).getEvent());
    }

    public static void eventClickTruecaller() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRUECALLER_ATTEMPT
        ).getEvent());
    }

    public static void eventClickTruecallerConfirm() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.TRUECALLER_CONFIRM
        ).getEvent());
    }

    public static void eventTruecallerImpression() {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.IMPRESSION_OTP,
                AppEventTracking.Category.SECURITY_QUESTION,
                AppEventTracking.Action.IMPRESSION,
                AppEventTracking.EventLabel.TRUECALLER
        ).getEvent());
    }

    public static void eventGMSwitcherManageShop(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.SHOP_MANAGE_GM_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventTopAdsSwitcher(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.TOPADS_SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.OPEN_TOP_SELLER+label
        ).getEvent());
    }

    public static void eventOpenShopSwitcher(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.OPENSHOP_SWITCHER,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventDownloadFromSwitcher(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.SWITCHER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DOWNLOAD_APP
        ).getEvent());
    }

    public static void eventFeedClick(String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                AppEventTracking.Action.CLICK,
                label
        ).getEvent());
    }

    public static void eventR3(String action, String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.R3,
                AppEventTracking.Category.R3USER,
                action,
                label
        ).getEvent());
    }

    public static void eventFeedClick(String action, String label) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.FEED,
                AppEventTracking.Category.FEED,
                action,
                label
        ).getEvent());
    }

    public static void eventSwitchRpToDollarAddProduct(){
        eventClickAddProduct(AppEventTracking.Category.ADD_PRODUCT, AppEventTracking.EventLabel.GOLD_MERCHANT_CURRENCY);
    }

    public static void eventClickVideoAddProduct(){
        eventClickAddProduct(AppEventTracking.Category.ADD_PRODUCT, AppEventTracking.EventLabel.GOLD_MERCHANT_VIDEO);
    }

    public static void eventClickYesGoldMerchantAddProduct(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_GOLD_MERCHANT,
                AppEventTracking.Category.GOLD_MERCHANT,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY_GM_ADD_PRODUCT
        ).getEvent());
    }

    public static void eventClickGoldMerchantViaDrawer(){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.CLICK_NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY_GM
        ).getEvent());
    }
}
