package com.tokopedia.digital.common.constant;

/**
 * @author by furqan on 13/08/18.
 */

public interface DigitalEventTracking {

    interface Event {
        String DIGITAL_GENERAL_EVENT = "digitalGeneralEvent";
        String ADD_TO_CART = "addToCart";
        String CHECKOUT = "checkout";
        String HOMEPAGE_INTERACTION = "userInteractionHomePage";
        String CLICK_CHECKOUT = "clickCheckout";
    }

    interface Category {
        String DIGITAL_NATIVE = "digital - native";
        String HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget";
        String DIGITAL_HOMEPAGE = "homepage digital";
        String DIGITAL_CHECKOUT = "digital - checkout";
        String DIGITAL_MULTIPLE_CHECKOUT = "digital - multiple checkout";
    }

    interface Action {
        String CLICK_PANDUAN_SECTION = "click panduan section";
        String CLICK_BELI = "click beli";
        String CLICK_LIHAT_SEMUA_PRODUK = "click lihat semua produk";
        String VIEW_CHECKOUT = "view checkout";
        String CLICK_PROCEED_PAYMENT = "click proceed to payment";
        String SELECT_DEAL_CATEGORY = "select deals category";
        String ADD_DEAL_OFFER = "add deals offer";
        String REMOVE_DEAL_OFFER = "remove deals offer";
        String CLICK_SKIP = "click skip";
        String ERROR_TO_ADD_DEAL = "error to add deals";
        String CLICK_SEE_ALL_PRODUCTS = "click lihat semua produk";
        String CLICK_RECOMMENDATION_WIDGET = "click recommendation widget";
    }

    interface Label {
        String DEFAULT_EMPTY_VALUE = "";
        String NO_PROMO = "no promo";
        String PROMO = "no promo";
        String SITE = "tokopediadigital";
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
