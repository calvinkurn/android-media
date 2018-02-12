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
    String ACCOUNTS_KEY = "accounts";

    String EVENT = "event";
    String EVENT_CATEGORY = "eventCategory";
    String EVENT_ACTION = "eventAction";
    String EVENT_LABEL = "eventLabel";
    String ECOMMERCE = "ecommerce";

    interface GTMKey {
        String ACCOUNTS_TYPE = "ACCOUNTS_TYPE";
    }

    interface GTMCacheKey {
        String REGISTER_TYPE = "REGISTER_TYPE";
        String LOGIN_TYPE = "LOGIN_TYPE";
        String SESSION_STATE = "SESSION_STATE";
        String MEDIUM = "MEDIUM";
    }

    interface GTMCacheValue {
        String EMAIL = "Email";
        String FACEBOOK = "Facebook";
        String GMAIL = "Gmail";
        String WEBVIEW = "Web View";
        String REGISTER = "Register";
        String LOGIN = "Login";
    }

    interface Event {
        String HOMEPAGE = "clickHomePage";
        String CATALOG = "clickKatalog";
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
        String TOP_SELLER = "clickTopSeller";
        String SLIDE_BANNER = "sliderBanner";
        String SUCCESS_SMART_LOCK = "eventSuccessSmartLock";
        String USER_INTERACTION_HOMEPAGE = "userInteractionHomePage";
        String RECHARGE_TRACKING = "rechargeTracking";
        String EVENT_TOKO_POINT = "eventTokopoint";
        String CLICK_PROMO_MICRO_SITE = "clickPromoMicrosite";

        String SHOP_MANAGE = "clickManageShop";
        String NOTES = "clickNotes";
        String LOCATION = "clickLocation";
        String ADD_PRODUCT = "clickAddProduct";
        String SALES = "clickSales";
        String NAVIGATION_DRAWER = "clickNavigationDrawer";
        String LOGIN_ERROR = "loginError";
        String REGISTER_LOGIN = "registerLogin";
        String LOGIN_CLICK = "clickLogin";
        String TRUECALLER = "installedTrueCaller";
        String GMSUBSCRIBE = "subscribeGoldMerchant";
        String DEPOSIT = "clickSaldo";
        String OTP = "clickOTP";
        String TOP_PICKS = "clickToppicks";
        String OFFICIAL_STORE = "clickOfficialStore";
        String CLICK_GM_STAT = "clickGMStat";
        String LOAD_GM_STAT = "loadGMStat";
        String SCROLL_GM_STAT = "scrollGMStat";
        String CLICK_ADD_PRODUCT = "clickAddProduct";
        String CLICK_INSTOPED = "clickInstoped";
        String CLICK_MANAGE_PRODUCT = "clickManageProduct";
        String CLICK_DRAFT_PRODUCT = "clickDraftProduct";
        String CATEGORY_PAGE = "clickKategori";
        String INTERMEDIARY_PAGE = "clickIntermediary";
        String DRAWER_CATEGORY = "clickDrawer";
        String OPEN_PUSH_NOTIFICATION = "openPushNotification";
        String RECEIVED_PUSH_NOTIFICATION = "receivedPushNotification";
        String EVENT_OTP = "OTPSuccess";
        String CLICK_VIEW_ALL_OS = "clickOfficialStore";
        String CLICK_AD_BANNER = "clickAdBanner";
        String IMPRESSION_OTP = "impressionOtp";
        String CLICK_OTP = "clickOtp";
        String CLICK_OS_BANNER_EMPTY_FEED = "clickFeed";
        String R3 = "r3";
        String CLICK_GOLD_MERCHANT = "clickGoldMerchant";
        String CLICK_NAVIGATION_DRAWER = "clickNavigationDrawer";

        String IMPRESSION_APP_UPDATE = "impressionAppUpdate";
        String CLICK_APP_UPDATE = "clickAppUpdate";
        String CLICK_CANCEL_APP_UPDATE = "clickCancelAppUpdate";

        String SELLER_WIDGET = "sellerWidget";
        String TOP_ADS_SELLER_APP = "topAdsSellerApp";
        String CLICK_CREATE_SHOP = "clickCreateShop";

        String PRODUCT_PAGE = "clickPDP";
        String SEND_MESSAGE_PAGE = "ClickChatDetail";
        String SHOP_PAGE = "clickMessageShop";

        String CLICK_APP_SHARE_REFERRAL = "clickReferral";
        String HOME_DASHBOARD_CLICK_SELLER = "homeDashboardClicked";

        String HOME_WISHLIST = "homeWishlist";
        String HOMEPAGE_UNIFY = "userInteractionHomepage";
        String EVENT_CLICK_TOP_NAV = "eventTopNav";
        String EVENT_MANAGE_PRODUCT = "eventManageProduct";

        String EVENT_CLICK_USER_PROFILE = "clickUserProfile";
        String EVENT_INTERNAL_PROMO = "internalPromo";
        String EVENT_INTERNAL_PROMO_MULTI = "internalPromoEvent";
        String EVENT_CLICK_HOME_DIGITAL_WIDGET = "clickHomeDigitalWidget";
        String EVENT_ADD_TO_CART_DIGITAL = "addToCartDigital";

        String IMPRESSION_APP_RATING = "impressionAppRating";
        String CLICK_APP_RATING = "clickAppRating";
        String CANCEL_APP_RATING = "cancelAppRating";

        String EVENT_IMPRESSION_HOME_PAGE ="eventImpressionHomePage";
        String SELLER_INFO = "clickSellerInfo";
        String EVENT_ONBOARDING = "onBoardingEvent";
        String EVENT_RESOLUTION = "clickResolution";
        String PRODUCT_VIEW = "productView";
    }

    interface Category {
        String HOMEPAGE = "Homepage";
        String HOMEPAGE_DIGITAL_WIDGET = "homepage digital widget";
        String HOMEPAGE_TOKOCASH_WIDGET = "homepage tokocash widget";
        String HOMEPAGE_OFFICIAL_STORE_WIDGET = "homepage official store widget";
        String HOMEPAGE_BANNER = "homepage banner";
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
        String EDIT_PRODUCT = "edit product";
        String EDIT_PRODUCT_IMAGE = "edit product image";
        String HAMBURGER = "Hamburger Icon";
        String CLICK_HAMBURGER = "click hamburger icon";
        String SALES = "Sales";
        String TRUECALLER = "TrueCaller";
        String GOLD_MERCHANT = "Gold Merchant";
        String DEPOSIT = "Saldo";
        String OTP = "OTP";
        String SECURITY_QUESTION = "Security Question";
        String TOP_PICKS_HOME = "Toppicks Home";
        String TOP_PICKS = "Toppicks";
        String CATALOG = "Katalog";
        String FULLY_LOAD = "Fully Loaded";
        String GM_STAT = "GM Stat";
        String GM_STATISTIC_PRODUCT = "GM Statistic - Product";
        String GM_STATISTIC_PRODUCT_INSIGHT = "GM Statistic - Product Insight";
        String GM_STATISTIC_DATE_PICKER = "GM Statistic - Date Picker";
        String MANAGE_PRODUCT = "Manage Product";
        String DRAFT_PRODUCT = "Draft Product";
        String CATEGORY_PAGE = "Category Page";
        String INTERMEDIARY_PAGE = "IntermediaryPage";
        String CATEGORY_DRAWER = "Category Drawer";
        String CATEGORY_PRODUCT = "Kategori";
        String GM_SWITCHER = "GM Switcher";
        String PUSH_NOTIFICATION = "Push Notification";
        String SMART_LOCK = "Smart Lock";
        String SLIDER = "Slider";
        String OS_AD_BANNER = "OS Ad Banner";
        String RECHARGE = "Recharge - ";
        String PRODUCT_FEED = "Product Feed";
        String SHOP_MANAGE_GM_SWITCHER = "Manage Shop - GM Switcher";
        String TOPADS_SWITCHER = "TopAds Switcher";
        String OPENSHOP_SWITCHER = "Open Shop Switcher";
        String SWITCHER = "Switcher";
        String R3USER = "r3User";
        String GMSTAT_TRANSACTION = "GM Statistic - Transaction";
        String GM_STATISTIC_TOP_ADS = "GM Statistic - TopAds";
        String GM_STATISTIC_TRANSACTION_DETAIL = "GM Statistic - Transaction Detail";
        String GM_STATISTIC_PRODUCT_SOLD = "GM Statistic - Products Sold";
        String APP_UPDATE = "Application Update";
        String SELLER_APP_WIDGET = "Seller App Widget";
        String TOP_ADS_PRODUCT = "TopAds - Product";
        String TOP_ADS_SHOP = "TopAds - Shop";
        String DASHBOARD = "Dashboard";
        String DASHBOARD_FAB = "Dashboard FAB";
        String GOLD_MERCHANT_ATC = "Gold Merchant ATC";
        String GOLD_MERCHANT_CHECKOUT = "Gold Merchant Checkout";
        String TOP_ADS_PRODUCT_SHOP = "TopAds - Product/Shop";

        String PRODUCT_PAGE = "product page";
        String SEND_MESSAGE_PAGE = "send message page";
        String SHOP_PAGE = "shop page";
        String REFERRAL="Referral";
        String FEATURED_PRODUCT = "Featured Product";
        String SELLER_INFO_HOMEPAGE = "seller info-homepage";
        String HOMEPAGE_UNIFY = "homepage tab";
        String EVENT_TOP_NAV = "top nav";
        String CATEGORY_HOTLIST = "hotlist";
        String TOKO_POINTS_PROMO_COUPON_PAGE = "tokopoints - kode promo & kupon page";
        String TOKO_POINTS_PROMO_HOMEPAGE = "homepage-tokopoints";
        String TOKOPOINTS_USER_PAGE = "tokopoints - user profile page";
        String TOKOPOINTS_POP_UP = "tokopoints - pop up";
        String APP_RATING = "Application Rating";
        String FEED_CONTENT_COMMENT_DETAIL = "content - comment detail";
        String ONBOARDING = "onboarding";
        String RESOLUTION_CENTER = "resolution center";
        String MANAGE_PROFILE = "pengaturan profile";

        String PROMO_MICROSITE_PROMO_LIST ="promo microsite - promo list";
        String PROMO_MICROSITE_PROMO_TOOLTIP ="promo microsite - promo tooltip";
        String SORT_BY = "sort by";
        String FILTER_PRODUCT = "filter product";
    }

    interface Action {
        String CLICK = "Click";
        String CLICK_VIEW_ALL    = "click view all";
        String CLICK_CHANNEL = "Click Channel";
        String SEARCH = "Search";
        String VOICE_SEARCH = "Voice Search";
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
        String LOGIN = "Login Success";
        String LOGIN_ERROR = "Login Error";
        String REGISTER = "Register";
        String INSTALLED = "Installed";
        String SUBSCRIBE = "Subscribe";
        String ERROR = "Error";
        String SUCCESS = "Success";
        String LOAD = "Load";
        String SCROLL = "Scroll";
        String UPLOAD_SUCCESS = "Success Upload";
        String CLICK_USE_VOUCHER = " Click Gunakan Voucher - ";
        String CLICK_CANCEL_VOUCHER = " Click Batalkan Voucher";

        String CLICK_CATEGORY = "Click Category";
        String NAVIGATION_CLICK = "Navigation";
        String BANNER_CLICK = "Banner Click";
        String OFFICIAL_STORE_CLICK = "Official Store";
        String INTERMEDIARY_VIDEO_CLICK = "Video Click";
        String BOTTOM_NAVIGATION_CATEGORY = "Bottom Navigation - Category";
        String PRODUCT_CATEGORY = "Click Product";
        String CATEGORY_LEVEL = "Category";
        String CURATED = "Curated";
        String HOTLIST = "Hotlist";
        String CATEGORY_MORE = "Category Breakdown";
        String CATEGORY_SORT = "Bottom Navigation - Sort";
        String CATEGORY_FILTER = "Bottom Navigation - Filter";
        String CATEGORY_DISLPAY = "Bottom Navigation - Display";
        String CATEGORY_SHARE = "Bottom Navigation - Share";
        String OPEN = "Open";
        String OTP_SUCCESS = "OTP Success";
        String RECEIVED = "received";
        String SELECT_PRODUCT = "Select Product";
        String SELECT_OPERATOR = "Select Operator";
        String CLICK_SEARCH_BAR = "Click Search Bar";
        String CLICK_BELI_LOWER = "click beli ";

        String CLICK_LANJUT_CHECKOUT = "Click Lanjut - Checkout Page";
        String CLICK_PHONEBOOK_ICON = "Click Phonebook Icon";
        String CLICK_DAFTAR_TX = "Click Daftar Transaksi";
        String VIEW_CHECKOUT_PAGE = "View Checkout Page";
        String IMPRESSION = "Impression";
        String COPY_CODE = "Copy Code";
        String FILTER = "Filter";
        String INSTALL = "Install";
        String REMOVE = "Remove";
        String CLICK_USSD_CEK_SALDO = "Click Cek Saldo from USSD";
        String CLICK_USSD_BUY_PULSA = "Click Beli from USSD";
        String CLICK_USSD_EDIT_NUMBER = "Click Edit Number from USSD";
        String CLICK_ICON_ON_ALL_CATEGORIES = "Click Icon on All Categories";
        String VOUCHER_SUCCESS = "Voucher Success - ";
        String VOUCHER_ERROR = "Voucher Error - ";
        String SUBMIT = "Submit";

        String PRODUCT_PAGE = "click";
        String SEND_MESSAGE_PAGE = "click on kirim";
        String SHOP_PAGE = "click on kirim pesan";
        String CLICK_DASHBOARD_CARD = "Click - Dashboard Card";
        String WISHLIST_SEARCH_ITEM = "wishlist - search item";
        String CLICK_CART_BUTTON = "click cart button";
        String CLICK_TOP_NAV = "click top nav";
        String CLICK_PRODUCT_LIST = "click product list";
        String CLICK_SORT_PRODUCT = "click sort product";
        String CLICK_FILTER_PRODUCT = "click filter product";
        String CLICK_OVERFLOW_MENU = "click overflow menu";

        String CLICK_WIDGET_BAR = "click widget";
        String CLICK_BELI = "Click Beli";
        String CLICK_BELI_INSTANT_SALDO = CLICK_BELI + " with Instant Saldo";
        String CHECK_INSTANT_SALDO_WIDGET = "Check Instant Saldo from Widget";
        String UNCHECK_INSTANT_SALDO_WIDGET = "Uncheck Instant Saldo from Widget";
        String CHECK_INSTANT_SALDO = "Check Instant Saldo";
        String UNCHECK_INSTANT_SALDO = "Uncheck Instant Saldo";
        String SELECT_NUMBER_ON_USER_PROFILE = "select number on user profile";
        String CLICK_REMOVE_WISHLIST = "wishlist - remove item";
        String CLICK_SHOP_FAVORITE = "favorite - click shop";
        String CLICK_EMPTY_SEARCH_WISHLIST = "wishlist - click empty cari product";
        String CLICK_SEARCH_ITEM_WISHLIST = "wishlist - search item";
        String CLICK_BELI_INSTANT_SALDO_WIDGET = CLICK_BELI_INSTANT_SALDO + " from Widget";
        String CLICK_BELI_WIDGET = CLICK_BELI + " from Widget";
        String CLICK_SALDO = "click saldo";
        String CLICK_ACTIVATE = "click activate";
        String SEARCH_PRODUCT = "search product";
        String SEARCH_POPULAR = "search product - popular";
        String SEARCH_RECENT = "search product - recent";
        String SEARCH_HOTLIST = "search product - hotlist";
        String SEARCH_AUTOCOMPLETE = "search product - autocomplete";
        String SEARCH_PRODUCT_SUGGESTION = "search product - suggestion";
        String SEARCH_SHOP = "search shop";
        String SEARCH_AUTOCOMPLETE_SHOP = "search shop - autocomplete";
        String CLICK_MY_COUPON = "click kupon saya";
        String CHOOSE_COUPON = "click coupon";
        String CLOSE_COUPON_PAGE = "click close button";
        String CLICK_TOKO_POINTS_STATUS = "click point & tier status";
        String CLICK_TOKO_POINTS = "click tokopoints";
        String TOKOPOINTS_POP_UP_IMPRESSION = "impression on any pop up";
        String TOKOPOINTS_POP_UP_CLICK = "click any pop up button";

        String FEED_CONTENT_IMPRESSION = "feed - content impression";
        String FEED_FOLLOW_CONTENT = "feed - follow content";
        String FEED_UNFOLLOW_CONTENT = "feed - unfollow content";
        String FEED_EXPAND_CONTENT = "feed - expand content";
        String FEED_CLICK_CONTENT_CTA = "feed - click content cta";
        String FEED_CLICK_CONTENT_WRITER_NAME = "feed - click content writer name";
        String FEED_LIKE_CONTENT = "feed - like content";
        String FEED_UNLIKE_CONTENT = "feed - unlike content";
        String FEED_CLICK_CONTENT_COMMENT = "feed - click content comment";
        String FEED_SUBMIT_COMMENT = "submit comment";
        String FEED_COMMENT_CLICK_BACK = "click back";
        String FEED_LOAD_MORE_COMMENTS = "load more comments";
        String FEED_KOL_RECOMMENDATION_IMPRESSION = "feed - kol recommendation impression";
        String FEED_FOLLOW_KOL_RECOMMENDATION = "feed - follow kol recommendation";
        String FEED_UNFOLLOW_KOL_RECOMMENDATION = "feed - unfollow kol recommendation";
        String FEED_CLICK_KOL_RECOMMENDATION_PROFILE = "feed - click kol recommendation profile";
        String FEED_VIEW_ALL_KOL_RECOMMENDATION = "feed - view all kol recommendation";

        String CLICK_HAMBURGER_ICON = "click hamburger icon";
        String CLICK_ARTICLE = "click article";

        String ONBOARDING_SKIP = "click - skip button";
        String ONBOARDING_START = "click - mulai";

        String CLICK_IMAGE_SETTINGS = "click image settings";
        String CLICK_SAVE_EDIT = "click save edit";


        String CLICK_VERIFY_NUMBER = "click verify number";
        String CLICK_HOW_IT_WORKS = "click how it works";
        String CLICK_COPY_REFERRAL_CODE = "click copy referral code";
        String CLICK_SHARE_CODE = "click share code";
        String SELECT_CHANNEL = "select channel";
        String CLICK_KNOW_MORE = "click know more";
        String CLICK_EXPLORE_TOKOPEDIA = "click explore tokopedia";

        String CLICK_PRODUCT_PROBLEM = "click barang & masalah";
        String CLICK_SOLUTION = "click solution";
        String CLICK_PROVE = "click bukti & keterangan";
        String CLICK_CREATE_RESO = "click buat komplain";
        String CLICK_CREATE_RESO_ABANDON = "click abandon";

        String CLICK_CHANGE_PHONE_NUMBER = "click on ubah";
        String SUCCESS_CHANGE_PHONE_NUMBER = "success change phone number";

        String PROMO_CLICK_CATEGORY = "user click on category";
        String PROMO_CLICK_SUB_CATEGORY = "user click on subcategory";
        String PROMO_CLICK_COPY_PROMO_CODE = "user click salin kode";

        String PROMO_CLICK_OPEN_TOOLTIP ="user click on tooltip";
        String PROMO_CLICK_CLOSE_TOOLTIP ="user click tutup";

        String CLICK_SHOP = "click - shop";
        String CLICK_WISHLIST = "click - wishlist";
        String SORT_BY = "sort by";
    }

    interface ImageEditor{
        String NO_ACTION = "no action";
        String ROTATE = "rotate";
        String CROP = "crop";
        String WATERMARK = "watermark";
    }

    interface EventLabel {
        String HOME = "homepage";
        String FAVORITE = "favorite";
        String PRODUCT_FEED = "feed";
        String HOTLIST = "hotlist";
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
        String INSTAGRAM_IMG_PICKER = "Instagram Image Picker";

        String SHOP_INFO = "Shop Info";
        String SHOP_SHIPPING = "Shop Shipping";
        String SHOP_PAYMENT = "Shop Payment";
        String SHOP_ETALASE = "Shop Etalase";
        String SHOP_NOTES = "Shop Notes";
        String SHOP_LOCATION = "Shop Location";
        String SHOP_CREATED = "Shop Created";

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
        String PRODUCT = "Product - ";
        String APPSHARE = "App share";

        String REGISTER = "Register";
        String CTA = "CTA";
        String SUBSCRIBE_SUCCESS = "Subscribe Success";
        String TOPUP = "TopUp";
        String WITHDRAW = "Withdraw";
        String SEND = "Send";
        String VERIFICATION = "Verification";
        String REVIEW_DETAIL = "Review Detail";
        String REVIEW_BUYER = "Review Buyer";
        String EDIT_SOLUTION = "Edit Solution";
        String COMMENT = "Comment";
        String COMPLAINT_DETAIL = "Complaint Detail";
        String DISCUSSION_DETAIL = "Discussion Detail";
        String CANCEL = "Cancel";
        String DETAILS = "Details";
        String ORDER_DETAIL = "Order Detail";
        String OFFICIAL_STORE = "Official Store - ";
        String STATISTIC = "Statistic";
        String STATISTIC_PAGE = "Statistic Page";
        String ONE_HUNDRED_PERCENT = "100%";
        String ADD_PRODUCT = "Add Product";
        String ADD_PRODUCT_PLUS = "Add Product - Plus";
        String ADD_PRODUCT_TOP = "Add Product - Top";
        String DRAFT_PRODUCT = "Draft Product";
        String SAVE_DRAFT = "Save Draft";
        String DELETE_DRAFT = "Delete Draft";
        String EDIT_DRAFT = "Edit Draft";
        String INSTOPED = "Instoped";
        String CHANGE_PRICE_PRODUCT_LIST = "Ubah Harga - Product List";
        String CHANGE_PRICE_DROP_DOWN = "Ubah Harga - Dropdown";
        String COPY_PRODUCT = "Copy";
        String OPEN_TOP_SELLER = "Open TopSeller - ";
        String OPEN_OPENSHOP = "Open OpenShop - ";
        String OPEN_GM = "Open GM - ";
        String OPEN_APP = "Open App";
        String DOWNLOAD_APP = "Download App";
        String OTP = "OTP";

        String CHANGE_CATEGORY_PRODUCT_GEAR = "Ganti Kategori - Gear Menu";
        String CHANGE_CATEGORY_PRODUCT_TOPMENU = "Ganti Kategori - Top Menu";
        String CHANGE_ETALASE_PRODUCT_GEAR = "Ganti Etalase - Gear Menu";
        String CHANGE_ETALASE_PRODUCT_TOPMENU = "Ganti Etalase - Top Menu";
        String CHANGE_INSURANCE_PRODUCT_GEAR = "Ganti Asuransi - Gear Menu";
        String CHANGE_INSURANCE_PRODUCT_TOPMENU = "Ganti Asuransi - Top Menu";
        String DELETE_PRODUCT_GEAR = "Hapus - Gear Menu";
        String DELETE_PRODUCT_TOPMENU = "Hapus - Top Menu";
        String CATEGORY_SHOW_MORE = "Lihat Lainnya";

        String OS_MICROSITE_NON_LOGIN = "Official Store Visit Microsite - Non Login";
        String OS_MICROSITE_LOGIN = "Official Store Visit Microsite - Login";
        String TOPADS_LOW_CREDIT = "Top Ads Below 20k";
        String TOPADS_SUCCESS_TOPUP = "Top Ads Topup Success";
        String SAVE_PASSWORD = "Save Password";
        String NEVER = "Never";
        String TRUECALLER = "Truecaller";
        String TRUECALLER_ATTEMPT = "Truecaller - Attempt";
        String TRUECALLER_CONFIRM = "Truecaller - Confirm";
        String VIEW_ALL_OFFICIAL_STORE_EMPTY_FEED = "View All - Official Store";
        String OPPORTUNIT_LIST = "Peluang";

        String GOLD_MERCHANT_CURRENCY = "Gold Merchant - Currency";
        String GOLD_MERCHANT_VIDEO = "Gold Merchant - Video Produk";
        String BUY_GM_ADD_PRODUCT = "Buy GM - Add Product";
        String BUY_GM = "Buy GM";
        String SEE_DETAIL = "See Detail";
        String CHANGE_DATE = "Change Date";
        String CHANGE_DATE_COMPARE = "Change Date - Compare";
        String MANAGE_TOPADS = "Manage TopAds";
        String PRODUCT_SOLD = "Products Sold";
        String GRAPH_X = "Graph - ";
        String OPTIONAL_APP_UPDATE = "Clicked Update - Optional";
        String FORCE_APP_UPDATE = "Clicked Update - Force";
        String OPTIONAL_CANCEL_APP_UPDATE = "Clicked Nanti";
        String FORCE_CANCEL_APP_UPDATE = "Clicked Tutup";

        String WIDGET_ORDER = "Widget - Order";
        String TO_APP_ORDER = "To App - Order";
        String TOPADS = "TopAds";
        String SELLER_HOME = "Home - SellerApp";
        String EXPAND_SUB_CATEGORY = "Expand Subcategory";
        String PAYMENT_AND_TOPUP = "Pembayaran dan Top Up";
        String DIGITAL_TRANSACTION_LIST = "Daftar Transaksi Digital";
        String ADD_BALANCE = "Add Balance";
        String BALANCE_OPTION_50_RB = "Rp. 50,000";
        String BALANCE_OPTION_100_RB = "Rp. 100,000";
        String BALANCE_OPTION_500_RB = "Rp. 500,000";
        String BALANCE_OPTION_1_JT = "Rp. 1000,000";
        String PERIOD_OPTION = "Date Period - ";
        String PERIOD_OPTION_TODAY = "Hari ini";
        String PERIOD_OPTION_YESTERDAY = "Kemarin";
        String PERIOD_OPTION_LAST_7_DAY = "7 Hari Terakhir";
        String PERIOD_OPTION_LAST_1_MONTH = "30 Hari Terakhir";
        String PERIOD_OPTION_THIS_MONTH = "Bulan ini";
        String STATISTIC_BAR = "Statistic Bar -";
        String STATISTIC_OPTION_IMPRESSION = "Impression";
        String STATISTIC_OPTION_CLICK = "Click";
        String STATISTIC_OPTION_CTR = "CTR";
        String STATISTIC_OPTION_CONVERSION = "Conversion";
        String STATISTIC_OPTION_AVERAGE_CONVERSION = "Average Conversion";
        String STATISTIC_OPTION_CPC = "CPC";
        String GROUP_PRODUCT_OPTION_NEW_GROUP = "Grup Baru";
        String GROUP_PRODUCT_OPTION_EXISTING_GROUP = "Grup yang Ada";
        String GROUP_PRODUCT_OPTION_WITHOUT_GROUP = "Tanpa Grup";
        String GROUP_STATUS_FILTER_ALL = "Semua Status";
        String GROUP_STATUS_FILTER_ACTIVE = "Aktif";
        String GROUP_STATUS_FILTER_INACTIVE = "Tidak Terkirim";
        String GROUP_STATUS_FILTER_NOT_SEND = "Tidak Aktif";
        String PRODUCT_STATUS_FILTER_ALL_GROUP = "Semua Grup";
        String PRODUCT_STATUS_FILTER_IN_GROUP = "Ada di Grup";
        String PRODUCT_STATUS_FILTER_NOT_IN_GROUP= "Tidak Ada di Grup";
        String BUDGET_NOT_LIMITED = "Anggaran Tidak Dibatasi";
        String BUDGET_PER_DAY = "Perhari";
        String SHOWTIME_AUTO = "Otomatis";
        String SHOWTIME_SETUP = "Atur tanggal mulai dan berhenti";
        String KEYWORD_TYPE_PHRASE = "Phrase Match";
        String KEYWORD_TYPE_EXACT = "Exact Match";
        String DATE_CUSTOM = "Date Custom";
        String STATISTIC_DASHBOARD = "Statistic (TopAds Dashboard) - ";
        String GROUP = "Group";
        String ADD_GROUP_STEP_1 = "New Promo Step 1";
        String ADD_GROUP_STEP_2 = "New Promo Step 2 - ";
        String ADD_GROUP_STEP_3 = "New Promo Step 3 - ";
        String GROUPS_FILTER = "Groups Filter - ";
        String ADD_PRODUCT_STEP_1 = "Add Product Promo Step 1";
        String ADD_PRODUCT_STEP_2 = "Add Product Promo Step 2- ";
        String ADD_PRODUCT_STEP_3 = "Add Product Promo Step 3 - ";
        String KEYWORD_POSITIF = "Keyword - ";
        String KEYWORD_NEGATIF = "Negatif - ";
        String EDIT_GROUP_MANAGE_GROUP = "Edit Group Promo - Atur Grup ";
        String EDIT_GROUP_COST = "Edit Group Promo - Biaya ";
        String EDIT_GROUP_SCHEDULE = "Edit Group Promo - Jadwal Tampil ";
        String DELETE_GROUP = "Delete Group Promo";
        String DATE_CUSTOM_WITHOUT_DASH = "Date Custom";
        String STATISTIC_WITH_DASH = "Statistic - ";
        String ADD_SHOP_PROMO_BUDGET = "Add Shop Promo Budget - ";
        String ADD_SHOP_PROMO_SHOWTIME = "Add Shop Promo Show Time - ";
        String FAB_DASHBOARD = "FAB Menu";
        String MANAGE_PRODUCT = "Manage Product";
        String ETALASE = "Etalase";
        String CHANGE_PACKAGE_GOLD_MERCHANT = "Change Package";
        String GM_CHECKOUT = "GM Checkout";
        String ENTER = "Enter";
        String OPEN_SHOP_ONBOARDING = "Open Shop - Onboarding";
        String CONTINUE_SHOP_BIODATA = "Continue Shop Biodata";
        String CONTINUE_SHOP_BIODATA_ERROR = "Continue Shop Biodata Error";
        String SAVE_LOGISTIC = "Save Logistic";
        String SAVE_LOGISTIC_ERROR = "Save Logistic Error";
        String KEYWORD = "Keyword";
        String PRODUCT_WITHOUT_DASH = "Product";
        String PERIOD_OPTION_MAIN_PAGE = "Date Period (Main Page) - ";
        String DATE_CUSTOM_MAIN_PAGE = "Date Custom (Main Page)";
        String PERIOD_OPTION_STATISTIK = "Date Period (Statistik Page) - ";
        String DATE_CUSTOM_STATISTIK = "Date Custom (Statistik Page)";
        String PERIOD_OPTION_GROUP = "Date Period (Group Page) - ";
        String DATE_CUSTOM_GROUP = "Date Custom (Group Page)";
        String PERIOD_OPTION_PRODUCT = "Date Period (Product Page) - ";
        String DATE_CUSTOM_PRODUCT = "Date Custom (Product Page)";
        String PERIOD_OPTION_GROUP_DETAIL = "Date Period (Detail Group Page) - ";
        String DATE_CUSTOM_GROUP_DETAIL = "Date Custom (Detail Group Page)";
        String PERIOD_OPTION_PRODUCT_DETAIL = "Date Period (Detail Product Page) - ";
        String DATE_CUSTOM_PRODUCT_DETAIL = "Date Custom (Detail Product Page)";
        String EDIT_GROUP_ADD_PRODUCT = "Edit Group Promo - Tambah Produk";
        String EDIT_PRODUCT_COST = "Edit Product Promo - Biaya ";
        String EDIT_PRODUCT_SCHEDULE = "Edit Product Promo - Jadwal Tampil ";
        String EDIT_GROUP_NAME = "Edit Group Promo - Ganti Nama Grup";
        String DETAIL_PROMO_PRODUCT_PDP = "Detail Product Promo - PDP";
        String DETAIL_PROMO_PRODUCT_GROUP = "Detail Product Promo - Detail Group";
        String ADD_PRODUCT_EXISTING_GROUP_STEP_1 = "Avaiable Group Step 1 - Add Product";
        String ADD_PRODUCT_WITHOUT_GROUP_STEP_1 = "Without Group Step 1- Add Product";
        String ADD_PRODUCT_WITHOUT_GROUP_STEP_2 = "Without Group Step 2 - ";
        String ADD_PROMO = "Add Promo - ";
        String ADD_PROMO_GROUP = "Add Group Promo";
        String ADD_PROMO_PRODUCT = "Add Produk Promo";
        String ADD_PROMO_KEYWORD_POSITIF = "Add Positive Keyword";
        String ADD_PROMO_KEYWORD_NEGATIVE = "Add Negative Keyword";

        String PRODUCT_PAGE =  "message shop";
        String APP_SHARE_LABEL="Share";
        String FEATURED_PRODUCT = "Featured Product";
        String SELLER_INFO = "seller info";
        String ADD_FEATURED_PRODUCT = "Add Featured Product";
        String TICK_ERROR = "Tick Error";
        String SAVE_FEATURED_PRODUCT_PICKER = "Simpan - ";
        String SORT_FEATURED_PRODUCT_CHANGE = "Atur Urutan (Change)";
        String SORT_FEATURED_PRODUCT_NO_CHANGE = "Atur Urutan (No Change)";
        String DELETE_FEATURED_PRODUCT = "Delete";

        String DASHBOARD_MAIN_SHOP_INFO = "Shop Info";
        String DASHBOARD_MAIN_TRANSACTION = "Penjualan";
        String DASHBOARD_MAIN_INBOX = "Kotak Masuk";

        String DASHBOARD_ITEM_SETTINGS = "Settings";
        String DASHBOARD_ITEM_PERFORMA_TOKO = "Performa Toko";
        String DASHBOARD_ITEM_REPUTASI_TOKO = "Reputasi Toko";
        String DASHBOARD_ITEM_TRANSAKSI_SUKSES = "Transaksi Sukses";

        String DASHBOARD_ITEM_ORDER_BARU = "Order Baru";
        String DASHBOARD_ITEM_KONFIRMASI_PENGIRIMAN = "Konfirmasi Pengiriman";
        String DASHBOARD_ITEM_STATUS_PENGIRIMAN = "Transaksi Sukses";
        String DASHBOARD_ITEM_PELUANG = "Peluang";
        String DASHBOARD_ITEM_PESAN = "Pesan";
        String DASHBOARD_ITEM_DISKUSI_PRODUK = "Diskusi Produk";
        String DASHBOARD_ITEM_ULASAN = "Ulasan";

        String SEARCH_PRODUCT = "search product";
        String CLICK_PRODUCT_LIST = "click product list";
        String CATEGORY = "category";
        String CONDITION = "condition";
        String PICTURE_STATUS = "picture status";
        String CHECKOUT_INSTANT = "instant";
        String CHECKOUT_NO_INSTANT = "no "+CHECKOUT_INSTANT;
        String SELECT_PRODUCT_FROM_WIDGET = "select Product - ";
        String CLICK_WIDGET_BAR = "click ";
        String MY_COUPON = "kupon saya";
        String CLOSE_COUPON_PAGE = "close";
        String TOKOPOINTS_LABEL = "tokopoints";
        String TOKOPOINTS_POP_UP = "pop up";
        String TOKOPOINTS_POP_UP_BUTTON = "pop up button";
        String FEED_CONTENT_TYPE_RECOMMENDED = "recommended content";
        String FEED_CONTENT_TYPE_FOLLOWED = "followed content";
        String FEED_CAMPAIGN_TYPE_SUFFIX = " endorsement";
        String FEED_CONTENT_COMMENT_DETAIL_COMMENT = "comment";
        String FEED_CONTENT_COMMENT_DETAIL_BACK = "back";
        String FEED_CONTENT_COMMENT_DETAIL_LOAD_MORE = "load more";
        String FEED_KOL_RECOMMENDATION_VIEW_ALL = "kol discovery page";
        String ONBOARDING_SKIP_LABEL = "skip - ";
        String ONBOARDING_START_LABEL = "click mulai sekarang";

        String RESO_PROBLEM_SAVE = "problem - save";
        String RESO_PROBLEM_SAVE_CHOOSE_OTHER = "problem - simpan dan pilih barang lain";
        String RESO_PROBLEM_CONTINUE = "problem - continue";
        String RESO_SOLUTION_CONTINUE  = "solution - continue";
        String RESO_CREATE_COMPLAINT_PRE  = "pre - create complain";
        String RESO_CREATE_COMPLAINT_CONFIRM  = "confirm - create complain";
        String RESO_CREATE_COMPLAINT_UNCONFIRM  = "unconfirmed - create complain";
        String RESO_CREATE_ABANDON = "abandon";
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
        String GA_ID = "GA_ID";
        String GTM_ID = "GTM_ID";
        String GTM_RESOURCE = "GTM_RESOURCE";

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
        String EXCLUDED_HOST = "excluded-host";

        String TICKER_PDP = "is_show_ticker_pdp";
        String TICKER_PDP_TEXT = "ticker_text_pdp";
        String TICKER_CART = "is_show_ticker_cart";
        String TICKER_CART_TEXT = "ticker_text_cart";
        String TICKER_ATC = "is_show_ticker_atc";
        String TICKER_ATC_TEXT = "ticker_text_atc";

        String SEARCH_RECENT = "recent_search";
        String SEARCH_POPULAR = "popular_search";
        String SEARCH_AUTOCOMPLETE = "autocomplete";
        String SEARCH_HOTLIST = "hotlist";
        String SEARCH_AUTOCOMPLETE_IN_CAT = "in_category";

        String UTM_SOURCE = "utm_source";
        String UTM_MEDIUM = "utm_medium";
        String UTM_CAMPAIGN = "utm_campaign";
        String UTM_CONTENT = "utm_content";
        String UTM_TERM = "utm_term";
        String UTM_GCLID = "gclid";

        String UTM_SOURCE_APPEND = "&" + UTM_SOURCE + "=";
        String UTM_MEDIUM_APPEND = "&" + UTM_MEDIUM + "=";
        String UTM_CAMPAIGN_APPEND = "&" + UTM_CAMPAIGN + "=";
        String UTM_CONTENT_APPEND = "&" + UTM_CONTENT + "=";
        String UTM_TERM_APPEND = "&" + UTM_TERM + "=";
        String UTM_GCLID_APPEND = "&" + UTM_GCLID + "=";

        String RESOLUTION_CENTER_UPLOAD_VIDEO = "upload_video_resolution_center";
    }

    interface MOENGAGE {
        String CURRENCY = "currency";
        String QUANTITY = "quantity";
        String PRODUCT = "product";
        String PURCHASE_DATE = "purchaseDate";
        String PRICE = "price";
        String IS_GOLD_MERCHANT = "is_gold_merchant";
        String IS_SELLER = "is_seller";
        String SHOP_ID = "shop_id";
        String SHOP_NAME = "shop_name";
        String MOBILE_NUM = "Mobile Number";
        String USER_ID = "User_ID";
        String MEDIUM = "Medium";
        String EMAIL = "Email";
        String LOGIN_STATUS = "logged_in_status";
        String PRODUCTS_NUMBER = "number_of_products";
        String SUBCATEGORY = "subcategory";
        String SUBCATEGORY_ID = "subcategory_id";
        String CATEGORY = "category";
        String CATEGORY_ID = "category_id";
        String PRODUCT_ID = "product_id";
        String PRODUCT_NAME = "product_name";
        String PRODUCT_URL = "product_url";
        String PRODUCT_PRICE = "product_price";
        String BRAND_NAME = "brand_name";
        String BRAND_ID = "brand_id";
        String DATE_OF_BIRTH = "Date of Birth";
        String NAME = "Name";
        String DIGITAL_CAT_ID = "digital_category_id";
        String HOTLIST_NAME = "hotlist_name";
        String PRODUCT_IMAGE_URL = "product_image_url";
        String IS_OFFICIAL_STORE = "is_official_store";
        String IS_FEED_EMPTY = "is_feed_empty";
        String IS_FAVORITE_EMPTY = "is_favorite_empty";
        String KEYWORD = "keyword";
        String IS_RESULT_FOUND = "is_result_found";
        String IS_RECEIVED = "is_received";
        String PAYMENT_TYPE = "payment_type";
        String PURCHASE_SITE = "purchase_site";
        String TOTAL_PRICE = "purchase_site";
        String REVIEW_SCORE = "review_score";

        String TOTAL_SOLD_ITEM = "total_sold_item";
        String REG_DATE = "registration_date";
        String DATE_SHOP_CREATED = "date_shop_created";
        String SHOP_LOCATION     = "shop_location";

        String TOKOCASH_AMT     = "tokocash_amt";
        String SALDO_AMT     = "saldo_amt";
        String TOPADS_AMT     = "topads_amt";
        String TOPADS_USER     = "is_topads_user";
        String HAS_PURCHASED_TICKET     = "has_purchased_ticket";
        String HAS_PURCHASED_MARKETPLACE     = "has_purchased_marketplace";
        String HAS_PURCHASED_DIGITAL     = "has_purchased_digital";
        String LAST_TRANSACT_DATE     = "last_transaction_date";
        String TOTAL_ACTIVE_PRODUCT     = "total_active_product";
        String SHOP_SCORE     = "shop_score";
        String QUALITY_SCORE     = "quality_score";
        String SCREEN_NAME     = "screen_name";
        String CHANNEL     = "channel";

    }

    interface EventMoEngage {
        String LOGIN = "Login";
        String OPEN_BERANDA = "Beranda_Screen_Launched";
        String OPEN_FEED = "Feed_Screen_Launched";
        String OPEN_FAVORITE = "Favorite_Screen_Launched";
        String OPEN_HOTLIST = "Hotlist_Screen_Launched";
        String OPEN_PRODUCTPAGE = "Product_Page_Opened";
        String CLICK_HOTLIST = "Clicked_Hotlist_Item";
        String ADD_WISHLIST = "Product_Added_To_Wishlist_Marketplace";
        String CLICK_MAIN_CATEGORY_ICON = "Maincategory_Icon_Tapped";
        String DIGITAL_CAT_SCREEN_OPEN = "Digital_Category_Screen_Launched";
        String CAT_SCREEN_OPEN = "Category_Screen_Launched";
        String PRODUCT_ADDED_TO_CART = "Product_Added_To_Cart_Marketplace";
        String PRODUCT_REMOVED_FROM_CART = "Product_Removed_From_Cart_Marketplace";
        String PRODUCT_REMOVED_FROM_WISHLIST = "Product_Removed_From_Wishlist_Marketplace";
        String CLICKED_DISKUSI_PDP = "Clicked_Diskusi_Pdp";
        String CLICKED_ULASAN_PDP = "Clicked_Ulasan_Pdp";
        String SEARCH_ATTEMPT = "Search_Attempt";
        String OPEN_THANKYOU_PAGE = "Thank_You_Page_Launched";
        String SUCCESS_PURCHASE_REVIEW = "Success_Purchase_Review";
        String SUBMIT_ULASAN_REVIEW = "Submit_Ulasan_Review";

        String REG_START = "Registration_Start";
        String REG_COMPL = "Registration_Completed";
        String CLICKED_NEW_ORDER = "Seller_Clicked_Neworder";
        String SELLER_ADDED_FAVORITE = "Seller_Added_To_Favourite";
        String SELLER_REMOVE_FAVORITE = "Seller_Removed_From_Favorite";
        String EVENT_ADDR_ADD = "Address_Added";
        String EVENT_USER_ATTR = "User_Attribute_Name";
        String SELLER_SCREEN_OPEN = "Penjualan_Screen_Launched";
        String SHIPPING_CONFIRMED = "Shipping_Received_Confirmation";
        String REFERRAL_SCREEN_LAUNCHED = "Referral_Screen_Launched";
        String REFERRAL_SHARE_EVENT = "Share_Event";
    }


    interface LOCA {
        String NOTIFICATION_BUNDLE = "ll";
    }

    interface AF {
        String APPSFLYER_KEY = "APPSFLYER_KEY";
    }

    interface AddProduct {
        String EVENT_CLICK_ADD_PRODUCT = "clickAddProduct";
        String CATEGORY_ADD_PRODUCT = "Add Product";
        String CATEGORY_EDIT_PRODUCT = "Edit Product";
        String EVENT_ACTION_ADD = "click add - optional fields used";
        String EVENT_ACTION_EDIT = "click edit - optional fields used";
        String EVENT_ACTION_ADD_MORE = "click add more products - optional fields used";
        String EVENT_ACTION_ERROR = "click add error - field validation triggered";
        String EVENT_ACTION_ERROR_SERVER = "click add error - server validation triggered";

        String FIELDS_MANDATORY_PRODUCT_NAME = "Product Name";
        String FIELDS_MANDATORY_CATEGORY = "Category";
        String FIELDS_MANDATORY_PRICE = "Price";
        String FIELDS_MANDATORY_WEIGHT = "Weight";
        String FIELDS_MANDATORY_MIN_PURCHASE = "Min Purchase";
        String FIELDS_MANDATORY_STOCK_STATUS = "Stock Status";
        String FIELDS_MANDATORY_SHOWCASE = "Showcase";
        String FIELDS_MANDATORY_CONDITION = "Condition";
        String FIELDS_MANDATORY_INSURANCE = "Insurance";
        String FIELDS_OPTIONAL_PICTURE = "Picture";
        String FIELDS_OPTIONAL_WHOLESALE = "Grosir";
        String FIELDS_OPTIONAL_STOCK_MANAGEMENT = "Stock management";
        String FIELDS_OPTIONAL_FREE_RETURN = "Free Return";
        String FIELDS_OPTIONAL_DESCRIPTION = "Description";
        String FIELDS_OPTIONAL_PRODUCT_VIDEO = "Product Video";
        String FIELDS_OPTIONAL_PREORDER = "Preorder";
        String FIELDS_OPTIONAL_SHARE = "Share";
        String FIELDS_OPTIONAL_VARIANT_LEVEL1 = "Variant Level 1";
        String FIELDS_OPTIONAL_VARIANT_LEVEL2 = "Variant Level 2";
        String FIELDS_OPTIONAL_VARIANT_LEVEL1_CUSTOM = "Variant Level 1 with Custom";
        String FIELDS_OPTIONAL_VARIANT_LEVEL2_CUSTOM = "Variant Level 2 with Custom";
        String FIELDS_OPTIONAL_EMPTY = "no optional field used";
    }

    interface CustomDimension {
        String USER_ID = "userId";
        String PRODUCT_ID = "productId";
        String SHOP_ID = "shopId";
        String PROMO_ID = "promoId";
    }

    interface EventBranch {
        String EVENT_LOGIN="login";
        String EVENT_REGISTER="sign_up";
    }

    interface Branch {
        String EMAIL="email";
        String PHONE="phone";
    }
}