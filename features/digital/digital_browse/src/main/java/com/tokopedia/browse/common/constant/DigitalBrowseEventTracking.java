package com.tokopedia.browse.common.constant;

/**
 * @author by furqan on 30/08/18.
 */

public interface DigitalBrowseEventTracking {

    interface Event {
        String CLICK_BACK = "clickBack";
        String CLICK_HOME_PAGE = "clickHomePage";
        String CLICK_PROMO = "promoClick";
        String IMPRESSION_PROMO = "promoView";
        String IMPRESSION_HOME_PAGE = "eventImpressionHomePage";
    }

    interface Action {
        String CLICK_BACK_BELANJA = "click back - belanja";
        String CLICK_VIEW_ALL_BELANJA = "click view all Brand Pilihan - belanja";
        String IMPRESSION_CATEGORY_BELANJA = "impression on category icon - belanja";
        String CLICK_CATEGORY_BELANJA = "click on category icon - belanja";
        String CLICK_BACK_LAYANAN = "click back - layanan";
        String CLICK_TAB_LAYANAN = "click header tab - layanan";
        String IMPRESSION_ICON_LAYANAN = "impression on %s - layanan";
        String CLICK_ICON_LAYANAN = "click on %s - layanan";
        String IMPRESSION_BRAND_BELANJA = "impression on brand - belanja";
        String CLICK_BRAND_BELANJA = "click on brand - belanja";
    }

}
