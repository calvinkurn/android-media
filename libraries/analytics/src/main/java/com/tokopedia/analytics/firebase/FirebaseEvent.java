package com.tokopedia.analytics.firebase;

/**
 * Created by ashwanityagi on 14/03/18.
 */

public class FirebaseEvent {
    public interface Home {
        String HOMEPAGE_SLIDING_BANNER_IMPRESSION = "homepage_sliding_banner_impression";
        String HOMEPAGE_SLIDING_BANNER_CLICK = "homepage_sliding_banner_click";
        String HOMEPAGE_SLIDING_BANNER_VIEW_ALL_CLICK = "homepage_sliding_banner_view_all_click";
        String HOMEPAGE_USED_CASE_IMPRESSION = "homepage_used_case_impression";
        String HOMEPAGE_USED_CASE_CLICK = "homepage_used_case_click";

        String HOMEPAGE_SPRINT_SALE_IMPRESSION = "homepage_sprint_sale_impression";
        String HOMEPAGE_SPRINT_SALE_CLICK = "homepage_sprint_sale_click";
        String HOMEPAGE_SPRINT_SALE_VIEW_ALL_CLICK = "homepage_sprint_sale_view_all_click";
        String HOMEPAGE_DISCOVERY_WIDGET_IMPRESSION = "homepage_discovery_widget_impression";
        String HOMEPAGE_DISCOVERY_WIDGET_CLICK = "homepage_discovery_widget_click";
        String HOMEPAGE_DISCOVERY_WIDGET_VIEW_ALL_CLICK = "homepage_discovery_widget_view_all_click";
        String HOMEPAGE_TOP_NAV_HOME_CLICKED = "homepage_bottom_nav_home_clicked";
        String HOMEPAGE_TOP_NAV_HOTLIST_CLICKED = "homepage_top_nav_hotlist_clicked";
        String HOMEPAGE_TOP_NAV_FEED_CLICKED = "homepage_top_nav_feed_clicked";
        String HOMEPAGE_TOP_NAV_FAVORIT_CLICKED = "homepage_top_nav_favorit_clicked";
        String CART_ICON_CLICKED = "cart_icon_clicked";
        String HOMEPAGE_SEARCH_ICON_CLICKED="homepage_search_icon_clicked";
        String HOMEPAGE_RECOMMEDATION_IMPRESSION="homepage_recommedation_impression";
        String HOMEPAGE_RECOMMEDATION_CLICKED="homepage_recommedation_clicked";
        String HOMEPAGE_HAMBURGER_CLICK="homepage_hamburger_click";
        String HAMBURGER_ICON_CLICK_CATEGORY="hamburger_icon_click_category";
        String KATEGORIPAGE_CATEGORY_SELECT="kategoripage_category_select";
        String KATEGORIPAGESUB_CATEGORY_SELECT="kategoripagesub_category_select";
        String HAMBURGER_ICON_CLICK_LOGIN="hamburger_icon_click_login";
        String HAMBURGER_ICON_CLICK_SIGNUP="hamburger_icon_click_signup";
        String LOGIN_PAGE_ENTER_EMAIL="login_page_enter_email";
        String LOGIN_PAGE_ENTER_PASSWORD="login_page_enter_password";
        String LOGIN_PAGE_CLICK_LOGIN="login_page_click_login";
        String LOGIN_PAGE_CLICK_LOGIN_FACEBOOK="login_page_click_login_facebook";
        String LOGIN_PAGE_CLICK_LOGIN_GOOGLE="login_page_click_login_google";
        String LOGIN_PAGE_CLICK_LOGIN_PHONE="login_page_click_login_phone";
        String LOGIN_PAGE_CLICK_LOGIN_YAHOO="login_page_click_login_yahoo";
        String LOGIN_PAGE_CLICK_FORGOT_PASSWORD="login_page_click_forgot_password";
        String  LOGIN_PAGE_CLICK_SIGNUP  = "login_page_click_signup";
        String  FORGOT_PASSWORD_PAGE_ENTER_EMAIL  = "forgot_password_page_enter_email";
        String  FORGOT_PASSWORD_PAGE_CHANGE_PASSWORD  = "forgot_password_page_change_password";
        String  LOGIN_VIA_PHONE_ENTER_MOBILE  = "login_via_phone_enter_mobile";
        String  LOGIN_VIA_PHONE_CLICK_LOGIN  = "login_via_phone_click_login";
        String  SIGNUP_PAGE_CLICK_FACEBOOK  = "signup_page_click_facebook";
        String  SIGNUP_PAGE_CLICK_GOOGLE  = "signup_page_click_google";
        String  SIGNUP_PAGE_CLICK_YAHOO  = "signup_page_click_yahoo";
        String  SIGNUP_PAGE_CLICK_EMAIL  = "signup_page_click_email";
        String  SIGNUP_PAGE_CLICK_LOGIN  = "signup_page_click_login";
        String  SIGNUP_VIA_EMAIL_ENTER_EMAIL  = "signup_via_email_enter_email";
        String  SIGNUP_VIA_EMAIL_ENTER_FULLNAME  = "signup_via_email_enter_fullname";
        String  SIGNUP_VIA_EMAIL_ENTER_MOBILE  = "signup_via_email_enter_mobile";
        String  SIGNUP_VIA_EMAIL_ENTER_PASSWORD  = "signup_via_email_enter_password";
        String  SIGNUP_VIA_EMAIL_CLICK_SIGNUP  = "signup_via_email_click_signup";
        String  HAMBURGER_LOGOUT_CLICK  = "hamburger_logout_click";
        String  HOMEPAGE_TOKOCASH_ACTIVATE  = "homepage_tokocash_activate";
        String  HOMEPAGE_TOKOCASH  = "homepage_tokocash";
        String  HOMEPAGE_TOKOPOINTS  = "homepage_tokopoints";
        String  HAMBURGER_PROFILE  = "hamburger_profile";
        String  HAMBURGER_TOKOPOINTS  = "hamburger_tokopoints";
        String  HAMBURGER_SALDO  = "hamburger_saldo";
        String  HAMBURGER_TOKOCASH  = "hamburger_tokocash";
        String  HAMBURGER_WISHLIST  = "hamburger_wishlist";
        String  HAMBURGER_KOTAK_MASUK  = "hamburger_kotak_masuk";
        String  HAMBURGER_PEMBELIAN  = "hamburger_pembelian";
        String  HAMBURGER_PENJUALAN  = "hamburger_penjualan";
        String  HAMBURGER_PRODUK  = "hamburger_produk";
        String  HAMBURGER_KOMPLAIN_SAYA  = "hamburger_komplain_saya";
        String  HAMBURGER_GOLD_MERCHANT  = "hamburger_gold_merchant";
        String  HAMBURGER_TOPADS  = "hamburger_topads";
        String  HAMBURGER_PENGATURAN  = "hamburger_pengaturan";
        String  HAMBURGER_DAPATKAN_TOKOCASH  = "hamburger_dapatkan_tokocash";
        String  HAMBURGER_HUBUNGI_KAMI  = "hamburger_hubungi_kami";
        String  HAMBURGER_BANTUAN  = "hamburger_bantuan";
        String  HAMBURGER_TOKO_SAYA  = "hamburger_toko_saya";
        String  FEED_RECENTLY_VIEWED_IMPRESSION  = "feed_recently_viewed_impression";
        String  FEED_RECENTLY_VIEWED_CLICK  = "feed_recently_viewed_click";
        String  FEED_RECENTLY_VIEWED_VIEW_ALL  = "feed_recently viewed_view_all";
        String  FEED_TOPPICKS_IMPRESSION  = "feed_toppicks_impression";
        String  FEED_TOPPICKS_CLICK  = "feed_toppicks_click";
        String  FEED_TOPPICKS_VIEW_ALL  = "feed_toppicks_view_all";
        String  FEED_TOPADS_IMPRESSION  = "feed_topads_impression";
        String  FEED_TOPADS_CLICK  = "feed_topads_click";
        String  FEED_CARI_TOKO  = "feed_cari_toko";
        String  FEED_OFFICIAL_STORE_CAMPAIGN_IMPRESSION  = "feed_official_store_campaign_impression";
        String  FEED_OFFICIAL_STORE_CAMPAIGN_CLICK  = "feed_official_store_campaign_click";
        String  FEED_OFFICIAL_STORE_CAMPAIGN_BUNDLING_BANNER  = "feed_official_store_campaign_bundling_banner";
        String  FEED_OFFICIAL_STORE_CAMPAIGN_VIEW_ALL  = "feed_official_store_campaign_view_all";
        String  FEED_YOU_MIGHT_LIKE_THIS  = "feed_you_might_like_this";
        String  FEED_FEED_YOU_MIGHT_LIKE_THIS_FURTHERMORE  = "feed_feed_you_might_like_this_furthermore";
        String  FAVORIT_WISHLIST_IMPRESSION  = "favorit_wishlist_impression";
        String  FAVORIT_WISHLIST_CLICK  = "favorit_wishlist_click";
        String  FAVORIT_WISHLIST_VIEW_ALL  = "favorit_wishlist_view_all";
        String  FAVORIT_RECOMMENDED_SHOPS  = "favorit_recommended_shops";
        String  PROMO_NATIVE_SELECT_VERTICAL  = "promo_native_select_vertical";
        String  PROMO_NATIVE_SELECT_CATEGORY  = "promo_native_select_category";
        String  PROMO_NATIVE_BANNER_IMPRESSION  = "promo_native_banner_impression";
        String  PROMO_NATIVE_BANNER_CLICK  = "promo_native_banner_click";
        String  HOMEPAGE_CLICK_QR_CODE  = "Homepage_click_qr_code";
        String  SCAN_QR_CODE  = "scan_qr_code";
        String  SALDO_CLICK_PENARIKAN  = "saldo_click_penarikan";
        String  WISHLIST_PRODUCT_IMPRESSION  = "wishlist_product_impression";
        String  WISHLIST_PRODUCT_CLICK  = "wishlist_product_click";
        String  WISHLIST_BELI  = "wishlist_beli";
        String  WISHLIST_DELETE  = "wishlist_delete";
        String  REFERRAL_COPY_CODE  = "referral_copy_code";
        String  REFERRAL_CLICK_INVITE  = "referral_click_invite";
        String  REFERRAL_CLICK_KNOW_MORE  = "referral_click_know_more";
        String  REFERRAL_SHARE_SELECT_CHANNEL  = "referral_share_select_channel";
        String  REFERRAL_SHARE_COPY_URL  = "referral_share_copy_url";
        String  REFERRAL_SHARE_OTHER_CHANNEL  = "referral_share_other_channel";
        String  REFERAL_VERIFY_PHONE  = "referal_verify_phone";
        String  REFERRAL_NEW_COPY  = "referral_new_copy";
        String  REFERRAL_NEW_SEE_MORE  = "referral_new_see_more";
        String  REFERRAL_NEW_EXPLORE_TOKOPEDIA  = "referral_new_explore_tokopedia";


    }
}
