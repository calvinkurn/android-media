package com.tokopedia.browse.common.constant

/**
 * @author by furqan on 30/08/18.
 */

interface DigitalBrowseEventTracking {

    interface Event {
        companion object {
            val CLICK_BACK = "clickBack"
            val CLICK_HOME_PAGE = "clickHomePage"
            val CLICK_PROMO = "promoClick"
            val IMPRESSION_PROMO = "promoView"
            val IMPRESSION_HOME_PAGE = "eventImpressionHomePage"
        }
    }

    interface Action {
        companion object {
            val CLICK_BACK_BELANJA = "click back - belanja"
            val CLICK_VIEW_ALL_BELANJA = "click view all Brand Pilihan - belanja"
            val IMPRESSION_CATEGORY_BELANJA = "impression on category icon - belanja"
            val CLICK_CATEGORY_BELANJA = "click on category icon - belanja"
            val CLICK_BACK_LAYANAN = "click back - layanan"
            val CLICK_TAB_LAYANAN = "click header tab - layanan"
            val IMPRESSION_ICON_LAYANAN = "impression on %s - layanan"
            val CLICK_ICON_LAYANAN = "click on %s - layanan"
            val IMPRESSION_BRAND_BELANJA = "impression on brand - belanja"
            val CLICK_BRAND_BELANJA = "click on brand - belanja"
        }
    }

}
