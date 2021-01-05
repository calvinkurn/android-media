package com.tokopedia.digital.common.analytic;

/**
 * @author by furqan on 13/08/18.
 */

public interface DigitalEventTracking {

    interface Event {
        String DIGITAL_GENERAL_EVENT = "digitalGeneralEvent";
        String ADD_TO_CART = "addToCart";
        String CHECKOUT = "checkout";
        String HOMEPAGE_INTERACTION = "userInteractionHomePage";
        String EVENT_IMPRESSION_HOME_PAGE = "eventImpressionHomePage";
        String CLICK_CHECKOUT = "clickCheckout";
        String LONG_CLICK = "longClick";
        String EVENT_CLICK_USER_PROFILE = "clickUserProfile";
        String CLICK_COUPON = "clickCoupon";
        String BUSINESS_UNIT = "businessUnit";
        String CURRENT_SITE = "currentSite";

        String EVENT_KEY = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String EVENT_SCREEN_NAME = "screenName";
        String USER_ID = "userId";
    }

    interface Category {
        String DIGITAL_NATIVE = "digital - native";
        String HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget";
        String DIGITAL_HOMEPAGE = "homepage digital";
        String DIGITAL_CHECKOUT = "digital - checkout";
        String DIGITAL_CHECKOUT_PAGE = "digital - checkout page";
        String DIGITAL_MULTIPLE_CHECKOUT = "digital - multiple checkout";
        String DIGITAL = "digital - ";
        String RECHARGE = "Recharge - ";
        String PULSA = "Pulsa";
        String LONG_PRESS = "Long Press";
    }

    interface Action {
        String CLICK_USSD_CEK_SALDO = "Click Cek Saldo from USSD";
        String USSD_ATTEMPT = "ussd attempt";
        String CLICK_SEARCH_BAR = "Click Search Bar";
        String SELECT_OPERATOR = "select operator";
        String SELECT_PRODUCT = "select product";
        String CLICK_DAFTAR_TX = "Click Daftar Transaksi";
        String CLICK_PANDUAN_SECTION = "click panduan section";
        String CLICK_BELI = "click beli";
        String VIEW_CHECKOUT = "view checkout";
        String CLICK_PROCEED_PAYMENT = "click proceed to payment";
        String SELECT_DEAL_CATEGORY = "select deals category";
        String ADD_DEAL_OFFER = "add deals offer";
        String REMOVE_DEAL_OFFER = "remove deals offer";
        String CLICK_SKIP = "click skip";
        String ERROR_TO_ADD_DEAL = "error to add deals";
        String CLICK_USSD_BUY_PULSA = "Click Beli from USSD";
        String CLICK_USSD_EDIT_NUMBER = "Click Edit Number from USSD";
        String CLICK_BILL = "Click Tagihan";
        String SELECT_CATEGORY = "select category";
        String SELECT_NUMBER_ON_USER_PROFILE = "select number on user profile";
        String CHECK_INSTANT_SALDO = "Check Instant Saldo";
        String UNCHECK_INSTANT_SALDO = "Uncheck Instant Saldo";
        String CLICK_USE_COUPON = "click gunakan kode promo atau kupon";
        String CLICK_CANCEL_APPLY_COUPON = "click x on ticker";
        String TICK_AUTODEBIT = "tick auto debit";
        String TICK_CROSSSELL = "tick cross sell";
        String TICK_PROTECTION = "tick protection";
        String UNTICK_AUTODEBIT = "untick auto debit";
        String UNTICK_CROSSSELL = "untick cross sell";
        String UNTICK_PROTECTION = "untick protection";
        String CLICK_PROMO = "click promo button";
    }

    interface Label {
        String DEFAULT_EMPTY_VALUE = "";
        String NO_PROMO = "no promo";
        String PROMO = "no promo";
        String SITE = "tokopediadigital";
        String PRODUCT = "Product - ";
        String DIGITAL = "Digital";
    }


    interface Screen {
        String DIGITAL_CATEGORY = "/digital/";
        String DIGITAL_CHECKOUT = "/digital/checkout";
    }

    interface Misc{
        String ACTION_FIELD_STEP1 = "cart page loaded";
        String ACTION_FIELD_STEP2 = "click payment option button";
    }
}
