package com.tokopedia.logisticCommon.data.analytics;

public interface ConstantLogisticAnalytics {

    interface Key {
        String EVENT = "event";
        String EVENT_CATEGORY = "eventCategory";
        String EVENT_ACTION = "eventAction";
        String EVENT_LABEL = "eventLabel";
        String E_COMMERCE = "ecommerce";
        String PAYMENT_ID = "payment_id";
    }

    interface EventName {
        String CLICK_SHIPPING = "clickShipping";
        String VIEW_SHIPPING = "viewShipping";
        String VIEW_COURIER = "viewCourier";
        String CLICK_COURIER = "clickCourier";
        String CLICK_COD = "clickCOD";
        String CHECKOUT = "checkout";
    }

    interface EventCategory {
        String SALES_SHIPPING = "sales shipping";
        String COURIER_SELECTION = "courier selection";
        String COURIER_SELECTION_COD = "courier selection - cod";
        String CASH_ON_DELIVERY = "cash on delivery";
    }

    interface EventAction {
        String CLICK_TOMBOL_SCAN_AWB = "click tombol scan awb";
        String VIEW_SCAN_AWB = "view scan awb";
        String CLICK_TOMBOL_SELESAI = "click tombol selesai";
        String EXIT_SCAN_AWB = "exit scan awb";
        String IMPRESSION_SCAN_AWB_PAGE = "impression scan awb page";
        String CLICK_PELAJARI_SELENGKAPNYA = "click pelajari selengkapnya cod";
        String CLICK_BACK_ARROW = "click back arrow";
        String CLICK_X_PADA_SYARAT = "click x pada syarat dan ketentuan cod";
        String SCROLL_TERMS_AND_CONDS = "scroll terms and conditions cod";
        String CLICK_CHECKLIST = "click checklist pilih durasi pengiriman";
        String CLICK_CHANGE_COURIER = "click change courier option";
        String VIEW_ERROR_INELIGIBLE = "view error message ineligible cod";
        String CLICK_X_INELIGIBLE = "click x in ineligible cod";
        String CLICK_MENGERTI_INELIGIBLE = "click button mengerti ineligible cod";
        String CLICK_BACK_ON_CONFIRMATION = "click back on payment confirmation page";
        String CLICK_BAYAR_DI_TEMPAT = "click button bayar di tempat";
        String VIEW_BAYAR_DI_TEMPAT = "view button bayar di tempat";
        String IMPRESSION_ELIGIBLE_COD = "impression eligible cod";
    }

    interface EventLabel {
        String SUCCESS = "success";
        String FAILED = "fail";
        String SUCCESS_ELIGIBLE = "success - eligible";
        String NOT_SUCCESS_INELIGIBLE = "not success - ineligible";
        String NOT_SUCCESS_INCOMPLETE = "not success - incomplete";
    }

    interface ScreenName {

    }
}
