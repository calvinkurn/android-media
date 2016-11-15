package com.tokopedia.core.util;

/**
 * Created by Alvarisi on 6/15/2016.
 * modified by Hafizh 8/23/2016
 */
public interface AppEventTracking {
    String EMAIL_KEY = "EMAIL_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String FULLNAME_KEY = "FULLNAME_KEY";
    String NOT_AVAILABLE = "NOT AVAILABLE";
    String DEFAULT_CHANNEL = "Email";


    interface GTMKey{
        String ACCOUNTS_TYPE = "ACCOUNTS_TYPE";
    }

    interface GTMCacheKey{
        String REGISTER_TYPE = "REGISTER_TYPE";
        String LOGIN_TYPE = "LOGIN_TYPE";
        String SESSION_STATE = "SESSION_STATE";
    }

    interface GTMCacheValue{
        String EMAIL = "Email";
        String FACEBOOK = "Facebook";
        String GMAIL = "Gmail";
        String REGISTER = "Register";
        String LOGIN = "Login";
        String WEBVIEW = "Web View";
    }

    interface Event{
        String HOMEPAGE = "clickHomePage";
        String PRODUCT_DETAIL_PAGE = "clickPDP";
        String ATC = "clickATC";
        String CHECKOUT = "clickCheckout";
        String GIMMICK = "clickGimmick";
        String REGISTER_SUCCESS = "registerSuccess";
        String HOTLIST = "clickHotlist";
        String FAVORITE = "clickFavorite";
        String SEARCH = "clickSearch";
        String PULSA = "clickPulsa";
        String WISHLIST = "clickWishlist";
        String FEED = "clickFeed";
        String SEARCH_RESULT = "clickSearchResult";
        String SORT = "clickSort";
        String FILTER = "clickFilter";
        String NO_RESULT = "noSearchResult";
        String REGISTER = "clickRegister";
        String REGISTER_ERROR = "registerError";
        String BUY = "clickBuy";
        String PAYMENT = "clickPayment";
        String CONFIRM_PAYMENT = "clickConfirm";
        String BACK_CLICK = "clickBack";
        String CAMPAIGN = "deeplinkCampaign";
        String LOGIN = "loginSuccess";
        String LOGIN_ERROR = "loginError";
        String REGISTER_LOGIN = "registerLogin";
        String LOGIN_CLICK = "clickLogin";
    }

    interface Category {
        String HOMEPAGE = "Homepage";
        String PRODUCT_DETAIL = "Product Detail Page";
        String ADD_TO_CART = "Add to Cart";
        String CHECKOUT = "Checkout";
        String GIMMICK = "Gimmick";
        String HOTLIST = "Hotlist";
        String REGISTER = "Register";
        String SEARCH = "Search";
        String SEARCH_RESULT = "Search Result";
        String SORT = "Sort";
        String FILTER = "Filter";
        String WISHLIST = "Wishlist";
        String FEED = "Feed";
        String FAVORITE = "Favorite";
        String PULSA = "Pulsa";
        String ANDROID_WIDGET = "Android Widget";
        String NO_RESULT = "No Search Result'";
        String PAYMENT = "Payment";
        String CAMPAIGN = "Campaign";
        String LOGIN = "Login";
    }

    interface Action {
        String CLICK = "Click";
        String SEARCH = "Search";
        String VIEW = "View";
        String VIEW_RECENT = "View Recent";
        String VIEW_ALL = "Lihat Semua";
        String VIEW_WISHLIST = "View Wishlist";
        String VIEW_RECOMMENDATION = "View Recommendation";
        String BUY = "View";
        String REGISTER = "Register";
        String REGISTER_SUCCESS = "Register Success";
        String REGISTER_ERROR = "Register Error";
        String NO_RESULT = "No Result";
        String ABANDON = "Abandon";
        String DEEPLINK = "Deeplink";
        String LOGIN = "Login Success";
        String LOGIN_ERROR = "Login Error";
    }

    interface EventLabel {
        String HOME = "Home";
        String FAVORITE = "Favorite";
        String PRODUCT_FEED = "Product Feed";
        String HOTLIST = "Hotlist";
        String VIEW_ALL_WISHLIST = "View All Wishlist";
        String VIEW_ALL_RECENT = "View All Wishlist";
        String ADD_TO_WISHLIST_LABEL = "Add To Wishlist - ";
        String SHARE = "Share";
        String SHARE_TO = "Share - ";
        String REVIEW = "Review";
        String PRODUCT_DESCRIPTION = "Product Description";
        String PRODUCT_TALK = "Talk";
        String ADD_ADDRESS = "Add Address";
        String PAYMENT_METHOD = "Payment Method";
        String DROPSHIPPER = "Dropshipper";
        String DEPOSIT_USE = "Use Deposit";
        String CLICKED_TAB = "Clicked Tab - ";
        String CATALOG = "Katalog";
        String SHOP = "Toko";
        String BUY = "Buy";
        String GOLD_MERCHANT = "GM Filter";
        String LOCATION = "Location";
        String PRICE = "Price";
        String PRE_ORDER = "Preorder";
        String WHOLESALE = "Harga Grosir";
        String REGISTER_STEP_1 = "{Step 1}";
        String REGISTER_STEP_2 = "{Step 2}";
        String FULLNAME = "Nama Lengkap";
        String TOS = "Perjanjian Persetujuan";
        String PASSWORD = "Kata Sandi";
        String PASSWORD_CONFIRMATION = "Konfirmasi Kata Sandi";
        String HANDPHONE = "Nomor Telepon";
        String GENDER = "Jenis Kelamin";
        String BIRTHDATE = "Tanggal Lahir";
        String EMAIL = "Email";
        String REGISTER = "Register";
        String CTA = "CTA";
        String PULSA_BELI = "Button Beli Pulsa";
        String PULSA_WIDGET = "Pulsa Android Widget";
        String PAKET_DATA_WIDGET = "Paket Data Android Widget";
        String PLN_WIDGET = "Token Listrik Android Widget";
        String ADD_TO_WISHLIST = "Add To Wishlist";
        String CHECKOUT = "Checkout";
        String PAY_NOW = "Pay Now";
        String THANK_YOU_PAGE = "Thank You Page";
    }

    interface SOCIAL_MEDIA {
        String BBM = "BBM";
        String FACEBOOK = "Facebook";
        String TWITTER = "Twitter";
        String WHATSHAPP = "Whatsapp";
        String LINE = "Line";
        String PINTEREST = "Pinterest";
        String INSTAGRAM = "Instagram";
        String GOOGLE_PLUS = "Google Plus";
        String OTHER = "Other";
    }
}