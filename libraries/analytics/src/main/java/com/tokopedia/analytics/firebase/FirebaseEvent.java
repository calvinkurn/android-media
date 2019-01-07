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
        String HOMEPAGE_TOP_NAV_HOME_CLICKED = "homepage_top_nav_home_clicked";
        String HOMEPAGE_TOP_NAV_HOTLIST_CLICKED = "homepage_top_nav_hotlist_clicked";
        String HOMEPAGE_TOP_NAV_FEED_CLICKED = "homepage_top_nav_feed_clicked";
        String HOMEPAGE_TOP_NAV_FAVORIT_CLICKED = "homepage_top_nav_favorit_clicked";
        String CART_ICON_CLICKED = "cart_icon_clicked";
        String HOMEPAGE_SEARCH_ICON_CLICKED = "homepage_search_icon_clicked";
        String HOMEPAGE_RECOMMEDATION_CLICKED = "homepage_recommedation_clicked";
        String HOMEPAGE_HAMBURGER_CLICK = "homepage_hamburger_click";
        String HAMBURGER_ICON_CLICK_LOGIN = "hamburger_icon_click_login";
        String HAMBURGER_ICON_CLICK_SIGNUP = "hamburger_icon_click_signup";
        String LOGIN_PAGE_ENTER_EMAIL = "login_page_enter_email";
        String LOGIN_PAGE_ENTER_PASSWORD = "login_page_enter_password";
        String LOGIN_PAGE_CLICK_LOGIN = "login_page_click_login";
        String LOGIN_PAGE_CLICK_LOGIN_FACEBOOK = "login_page_click_login_facebook";
        String LOGIN_PAGE_CLICK_LOGIN_GOOGLE = "login_page_click_login_google";
        String LOGIN_PAGE_CLICK_LOGIN_PHONE = "login_page_click_login_phone";
        String LOGIN_PAGE_CLICK_LOGIN_YAHOO = "login_page_click_login_yahoo";
        String LOGIN_PAGE_CLICK_FORGOT_PASSWORD = "login_page_click_forgot_password";
        String LOGIN_PAGE_CLICK_SIGNUP = "login_page_click_signup";
        String SIGNUP_PAGE_CLICK_FACEBOOK = "signup_page_click_facebook";
        String SIGNUP_PAGE_CLICK_GOOGLE = "signup_page_click_google";
        String SIGNUP_PAGE_CLICK_EMAIL = "signup_page_click_email";
        String HOMEPAGE_TOKOCASH_ACTIVATE = "homepage_tokocash_activate";
        String HOMEPAGE_TOKOPOINTS = "homepage_tokopoints";
        String HAMBURGER_PROFILE = "hamburger_profile";
        String HAMBURGER_SALDO = "hamburger_saldo";
        String HAMBURGER_TOKOCASH = "hamburger_tokocash";
        String HAMBURGER_OPTION_CLICK = "hamburger_option_click";
        String HAMBURGER_TOKOCASH_ACTIVATE = "hamburger_tokocash_activate";
        String HAMBURGER_TOKOPOINTS = "hamburger_tokopoints";
        String HAMBURGER_TOKOCARD = "hamburger_tokocard";

    }

    public interface SellerHome {
        String HOMEPAGE_AKUN_PENJUAL_CLICK = "homepage_akun_penjual_widget_click";
    }
}
