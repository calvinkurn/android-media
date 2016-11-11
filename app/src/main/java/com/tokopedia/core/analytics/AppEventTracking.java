package com.tokopedia.core.analytics;

/**
 * Created by Alvarisi on 6/15/2016.
 * modified by Hafizh 8/23/2016
 * Collection of string that used in analytics
 */
public interface AppEventTracking {
    String GTM_CACHE = "GTM_CACHE";
    String EMAIL_KEY = "EMAIL_KEY";
    String USER_ID_KEY = "USER_ID_KEY";
    String FULLNAME_KEY = "FULLNAME_KEY";
    String NOT_AVAILABLE = "NOT AVAILABLE";
    String DEFAULT_CHANNEL = "Email";
    String DEFAULT_EVENT = "Event";


    interface GTMCacheKey {
        String REGISTER_TYPE = "REGISTER_TYPE";
        String LOGIN_TYPE = "LOGIN_TYPE";
        String SESSION_STATE = "SESSION_STATE";
    }

    interface GTMCacheValue {
        String EMAIL = "Email";
        String FACEBOOK = "Facebook";
        String GMAIL = "Gmail";
        String REGISTER = "Register";
        String LOGIN = "Login";
    }

    interface Event {
        String HOMEPAGE = "clickHomePage";
        String PRODUCT_DETAIL_PAGE = "clickPDP";
        String ATC = "clickATC";
        String CHECKOUT = "clickCheckout";
        String CATEGORY = "clickCategory";
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
        String PAYMENT = "clickPayment";
        String CONFIRM_PAYMENT = "clickConfirm";
        String BACK_CLICK = "clickBack";
        String CAMPAIGN = "deeplinkCampaign";
        String STATUS = "clickStatus";
        String NEW_ORDER = "clickNewOrder";
        String CONFIRM_SHIPPING = "clickShipping";
        String MESSAGE = "clickMessage";
        String PRODUCT_DISCUSSION = "clickProductDiscussion";
        String REVIEW = "clickReview";
        String RESOLUTION_CENTER = "clickResolution";
        String FORGOT_PASSWORD = "passwordForget";
        String RESEND_EMAIL = "resendEmail";
        String CREATE_SHOP = "clickCreateShop";
        String RECEIVED = "clickReceived";
        String LOGIN = "loginSuccess";
        String ETALASE = "clickEtalase";
        String CHANGE_VIEW = "clickChangeView";
        String TALK_SUCCESS = "talkSuccess";
        String PRICE_ALERT = "clickPriceAlert";
        String PRICE_ALERT_SUCCESS = "priceAlertSuccess";
        String REPORT = "clickReport";
        String REPORT_SUCCESS = "reportSuccess";
        String BUY = "clickBuy";
        String MESSAGE_SHOP = "clickMessageShop";
        String FAVORITE_SHOP = "clickFavoriteShop";

        String SHOP_MANAGE = "clickManageShop";
        String NOTES = "clickNotes";
        String LOCATION = "clickLocation";
        String ADD_PRODUCT = "clickAddProduct";
        String SALES = "clickSales";
        String NAVIGATION_DRAWER = "clickNavigationDrawer";
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
        String STATUS = "Status";
        String NEW_ORDER = "New Order";
        String SHIPPING = "Shipping";
        String MESSAGE = "Message";
        String REVIEW = "Review";
        String RESOLUTION = "Resolution";
        String ETALASE = "clickEtalase";
        String PRODUCT_DISCUSSION = "Product Discussion";
        String FORGOT_PASSWORD = "Forgot Password";
        String EMAIL_ACTIVATION = "Activation Email";
        String CREATE_SHOP = "Create Shop";
        String RECEIVED = "Received";
        String LOGIN = "Login";
        String SHOP_DETAIL = "Shop Detail Page";
        String SHOP_MANAGE = "Manage Shop";
        String NOTES = "Notes";
        String LOCATION = "Location";
        String ADD_PRODUCT = "Add Product";
        String HAMBURGER = "Hamburger Icon";
        String SALES = "Sales";
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
        String REGISTER_SUCCESS = "Register Success";
        String REGISTER_ERROR = "Register Error";
        String NO_RESULT = "No Result";
        String ABANDON = "Abandon";
        String DEEPLINK = "Deeplink";
        String RESET_SUCCESS = "Reset Success";
        String SEND = "Send";
        String LOGIN_SUCCESS = "Login Success";
        String TALK_SUCCESS = "Talk Success";
        String PRICE_ALERT_SUCCESS = "Price Alert Success";
        String REPORT_SUCCESS = "Report Success";
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
        String CHANGE_ADDRESS = "Change Address";
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
        String PULSA_BELI = "Button Beli Pulsa";
        String PULSA_WIDGET = "Pulsa Android Widget";
        String PAKET_DATA_WIDGET = "Paket Data Android Widget";
        String PLN_WIDGET = "Token Listrik Android Widget";
        String CHECKOUT = "Checkout";
        String PAY_NOW = "Pay Now";
        String THANK_YOU_PAGE = "Thank You Page";
        String TRACK = "Track";
        String ACCEPT_ORDER = "Accept Order";
        String REJECT_ORDER = "Reject Order";
        String CONFIRMATION = "Confirmation";
        String MESSAGE_DETAIL = "Message Detail";
        String RESET_PASSWORD = "Reset Password";
        String RESEND_EMAIL = "Resend Activation Email";
        String CREATE = "Create";
        String TALK_SUCCESS = "Talk Success";
        String PRICE_ALERT_NOT_LOGIN = "Price Alert - Not Login";
        String PRICE_ALERT = "Price Alert";
        String PRICE_ALERT_SUCCESS = "Price Alert Success";
        String MESSAGE_SHOP = "Message Shop";
        String FAVORITE_SHOP = "Favorite Shop";
        String ADD_TO_WISHLIST = "Add To Wishlist";
        String REPORT = "Report";
        String REPORT_NOT_LOGIN = "Report - Not Login";
        String REPORT_SUCCESS = "Report Success";

        String SHOP_INFO = "Shop Info";
        String SHOP_SHIPPING = "Shop Shipping";
        String SHOP_PAYMENT = "Shop Payment";
        String SHOP_ETALASE = "Shop Etalase";
        String SHOP_NOTES = "Shop Notes";
        String SHOP_LOCATION = "Shop Location";

        String DELETE = "Delete";
        String ADD = "Add";
        String ADD_MORE = "Add More";

        String NAVIGATION_DRAWER = "Navigation Drawer";
        String DEPOSIT = "Saldo";
        String TOPPOINTS = "Toppoints";
        String WISHLIST = "Wishlist";
        String INBOX = "Inbox";
        String MESSAGE = "Message";
        String PRODUCT_DISCUSSION = "Product Discussion";
        String HELP = "Help";
        String RESOLUTION_CENTER = "Resolution Center";
        String PURCHASE = "Purchase";
        String PAYMENT_CONFIRMATION = "Payment Confirmation";
        String ORDER_STATUS = "Order Status";
        String RECEIVE_CONFIRMATION = "Receive Confirmation";
        String CANCELLED_ORDER = "Cancelled Order";
        String PURCHASE_LIST = "Purchase List";
        String SALES = "Sales";
        String NEW_ORDER = "New Order";
        String DELIVERY_CONFIRMATION = "Delivery Confirmation";
        String DELIVERY_STATUS = "Delivery Status";
        String SALES_LIST = "Sales List";
        String PRODUCT_LIST = "Product List";
        String PRODUCT_DISPLAY = "Product Display";
        String SETTING = "Setting";
        String SIGN_OUT = "Sign Out";
        String PROFILE = "Profile";
        String SHOP_EN = "Shop";

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

    interface GTM {
        String LUCKY_BUYER = "msg_lucky_buyer";
        String LUCKY_BUYER_DETAIL = "msg_lucky_buyer_detail";
        String LUCKY_BUYER_VALID = "msg_lucky_valid";

        String LUCKY_MERCHANT = "msg_lucky_merchant";
        String LUCKY_MERCHANT_DETAIL = "msg_lucky_merchant_detail";
        String OVERRIDE_BANNER = "is_override_url_banner";
        String CREATE_TICKET = "is_create_ticket";
        String REPORT = "enable_report";
        String CONTACT_US = "disable_contactus";
        String TICKER_SEARCH = "is_show_ticker_search";
        String TICKER_SEARCH_TEXT = "ticker_text_search";
        String FILTER_SORT = "sort_filter_data";
        String EXCLUDED_URL = "excluded-url";

        String TICKER_PDP = "is_show_ticker_pdp";
        String TICKER_PDP_TEXT = "ticker_text_pdp";
        String TICKER_CART = "is_show_ticker_cart";
        String TICKER_CART_TEXT = "ticker_text_cart";
        String TICKER_ATC = "is_show_ticker_atc";
        String TICKER_ATC_TEXT = "ticker_text_atc";
    }

    interface LOCA {
        String NOTIFICATION_BUNDLE = "ll";
    }
}