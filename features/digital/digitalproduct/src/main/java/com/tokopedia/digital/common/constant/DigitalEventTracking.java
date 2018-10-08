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
    }

    interface Category {
        String DIGITAL_NATIVE = "digital - native";
        String HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget";
        String DIGITAL_HOMEPAGE = "homepage digital";
        String DIGITAL_CHECKOUT = "digital - checkout";
    }

    interface Action {
        String CLICK_PANDUAN_SECTION = "click panduan section";
        String CLICK_BELI = "click beli";
        String CLICK_LIHAT_SEMUA_PRODUK = "click lihat semua produk";
        String VIEW_CHECKOUT = "view checkout";
        String CLICK_PROCEED_PAYMENT = "click proceed to payment";
    }

    interface Label {
        String DEFAULT_EMPTY_VALUE = "";
    }

}
