package com.tokopedia.tkpd.analytics;

import android.text.Html;

import com.appsflyer.AFInAppEventType;
import com.tokopedia.tkpd.analytics.model.CustomerWrapper;
import com.tokopedia.tkpd.analytics.nishikino.model.EventTracking;
import com.tokopedia.tkpd.analytics.nishikino.model.GTMCart;
import com.tokopedia.tkpd.analytics.nishikino.model.ProductDetail;
import com.tokopedia.tkpd.var.ProductItem;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author  by Herdi_WORK on 25.10.16.
 * modified by Alvarisi
 * this class contains tracking for Unify (Click, Discovery & View Product, Authentication,
 * Login/Register)
 */

public class UnifyTracking extends TrackingUtils {

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
                label
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

    public static void eventShare(String label){
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.PRODUCT_DETAIL_PAGE,
                AppEventTracking.Category.PRODUCT_DETAIL,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.SHARE_TO + label
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
                AppEventTracking.EventLabel.ADD_TO_WISHLIST_LABEL + Html.fromHtml(label)
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

    public static  void eventAppsFlyerViewListingSearch(java.util.List<ProductItem> model, String keyword){
        JSONArray afProdIds = new JSONArray();
        for (int i = 0; i < model.size(); i++){
            if (i < 3) {
                afProdIds.put(model.get(i).getId());
            } else {
                break;
            }
        }

        eventAppsFlyerViewListingSearch(afProdIds, keyword);
        eventAppsFlyerContentView(afProdIds, keyword);
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
}
