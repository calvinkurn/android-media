package com.tokopedia.transactionanalytics;

/**
 * @author anggaprasetiyo on 18/05/18.
 */
public interface ConstantTransactionAnalytics {


    interface EventName {
        String CLICK_ATC = "clickATC";
        String VIEW_ATC = "viewATC";
    }

    interface EventCategory {
        String CART = "Cart";
        String ADD_TO_CART = "add to cart";
    }

    interface EventAction {
        String CLICK_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode promo atau kupon";
        String CLICK_HAPUS_ON_TOP_RIGHT_CORNER = "click hapus on top right corner";
        String CLICK_SHOP_NAME = "click shop name";
        String CLICK_PRODUCT_NAME = "click product name";
        String CLICK_BUTTON_PLUS = "click button +";
        String CLICK_BUTTON_MIN = "click button -";
        String CLICK_TRASH_BIN = "click trash bin";
        String CLICK_ARROW_BACK = "click arrow back";
        String CLICK_X_ON_BANNER_PROMO_CODE = "click x on banner promo code";
        String CLICK_BELANJA_SEKARANG_ON_EMPTY_CART = "click belanja sekarang on empty cart";
        String CLICK_PILIH_SEMUA_FORM_HAPUS = "click pilih semua from hapus";
        String CLICK_CHECKLIST_BOX_FORM_HAPUS = "click checklist box from hapus";
        String CLICK_HAPUS_FORM_HAPUS = "click hapus from hapus";
        String CLICK_GUNAKAN_KODE_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click gunakan kode from gunakan kode promo atau kupon";
        String CLICK_KUPON_SAYA_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kupon saya from gunakan kode promo atau kupon";
        String CLICK_KODE_PROMO_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kode promo from gunakan kode promo atau kupon";
        String CLICK_KUPON_FROM_GUNAKAN_KODE_PROMO_ATAU_KUPON = "click kupon from gunakan kode promo atau kupon";

        String CLICK_BAYAR_ON_ATC_SUCCESS = "click bayar on atc success";
        String CLICK_LANJUTKAN_BELANJA_ON_ATC_SUCCESS = "click lanjutkan belanja on atc success";

        String VIEW_IMPRESSION_CART_EMPTY = "impression cart empty";
        String IMPRESSION_ATC_SUCCESS = "impression atc success";
    }

    interface EventLabel {
        String CLICK_BELI = "click beli";
    }
}
