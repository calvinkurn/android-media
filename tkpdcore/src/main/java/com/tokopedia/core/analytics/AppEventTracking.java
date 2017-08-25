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
        String INTERMEDIAR_PAGE = "IntermediaryPage";
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
    }

    interface Action {
        String CLICK = "Click";
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
        String SEARCH_POPULAR = "Popular Search";
        String SEARCH_RECENT = "Recent Search";
        String SEARCH_HOTLIST = "Search HotList";
        String SEARCH_AUTOCOMPLETE = "Search Autocomplete";
        String SEARCH_AUTOCOMPLETE_SHOP = "Search Autocomplete Shop";
        String SEARCH_AUTOCOMPLETE_CATEGORY = "Search Autocomplete Category";
        String UPLOAD_SUCCESS = "Success Upload";

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
        String SELECT_PRODUCT_FROM_WIDGET = "Select Product from Widget";
        String SELECT_OPERATOR_FROM_WIDGET = "Select Operator from Widget";
        String SELECT_PRODUCT = "Select Product";
        String SELECT_OPERATOR = "Select Operator";
        String CLICK_SEARCH_BAR = "Click Search Bar";
        String CHECK_INSTANT_SALDO_WIDGET = "Check Instant Saldo from Widget";
        String UNCHECK_INSTANT_SALDO_WIDGET = "Uncheck Instant Saldo from Widget";
        String CHECK_INSTANT_SALDO = "Check Instant Saldo";
        String UNCHECK_INSTANT_SALDO = "Uncheck Instant Saldo";
        String CLICK_BELI = "Click Beli";
        String CLICK_BELI_WIDGET = CLICK_BELI + " from Widget";
        String CLICK_BELI_INSTANT_SALDO = CLICK_BELI + " with Instant Saldo";
        String CLICK_BELI_INSTANT_SALDO_WIDGET = CLICK_BELI_INSTANT_SALDO + " from Widget";
        String CLICK_LANJUT_CHECKOUT = "Click Lanjut - Checkout Page";
        String VIEW_CHECKOUT_PAGE = "View Checkout Page";
        String IMPRESSION = "Impression";
        String COPY_CODE = "Copy Code";
        String FILTER = "Filter";
        String INSTALL = "Install";
        String REMOVE = "Remove";
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
        String SUBCATEGORY = "subcategory_name";
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
        String IS_RESULT_FOUND = "IS_RESULT_FOUND";
        String PAYMENT_TYPE = "payment_type";
        String PURCHASE_SITE = "purchase_site";
        String TOTAL_PRICE = "purchase_site";
        String REVIEW_SCORE = "review_score";
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
        String DIGITAL_CAT_SCREEN_OPEN = "Digital_Category_Screen_Open";
        String CAT_SCREEN_OPEN = "Category_Screen_Launched";
        String PRODUCT_ADDED_TO_CART = "Product_Added_To_Cart_Marketplace";
        String PRODUCT_REMOVED_FROM_CART = "Product_Removed_From_Cart_Marketplace";
        String PRODUCT_REMOVED_FROM_WISHLIST = "Product_Removed_From_Wishlist_Marketplace";
        String CLICKED_DISKUSI_PDP = "Clicked_Diskusi_Pdp";
        String CLICKED_ULASAN_PDP = "Clicked_Ulasan_Pdp";
        String SEARCH_ATTEMPT = "Search_Attempt";
        String OPEN_THANKYOU_PAGE = "Thank_You_Page_Launched";
        String SUCCESS_PURCHASE_REVIEW = "Success_Purchase_Review";

        String REG_START = "Registration_Start";
        String REG_COMPL = "Registration_Completed";
        String EVENT_ADDR_ADD = "Address_Added";
        String EVENT_USER_ATTR = "User_Attribute_Name";
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
        String EVENT_ACTION_ADD = "Add";
        String EVENT_ACTION_ADD_MORE = "Add More";
        String EVENT_ACTION_ERROR = "Error";
        String EVENT_ACTION_ERROR_SERVER = "Error Server";

        String FIELDS_MANDATORY_PRODUCT_NAME = "M1";
        String FIELDS_MANDATORY_CATEGORY = "M2";
        String FIELDS_MANDATORY_PRICE = "M3";
        String FIELDS_MANDATORY_WEIGHT = "M4";
        String FIELDS_MANDATORY_MIN_PURCHASE = "M5";
        String FIELDS_MANDATORY_STOCK_STATUS = "M6";
        String FIELDS_MANDATORY_SHOWCASE = "M7";
        String FIELDS_MANDATORY_CONDITION = "M8";
        String FIELDS_MANDATORY_INSURANCE = "M9";
        String FIELDS_OPTIONAL_PICTURE = "O1";
        String FIELDS_OPTIONAL_WHOLESALE = "O2";
        String FIELDS_OPTIONAL_STOCK_MANAGEMENT = "O3";
        String FIELDS_OPTIONAL_FREE_RETURN = "O4";
        String FIELDS_OPTIONAL_DESCRIPTION = "O5";
        String FIELDS_OPTIONAL_PRODUCT_VIDEO = "O6";
        String FIELDS_OPTIONAL_PREORDER = "O7";
        String FIELDS_OPTIONAL_SHARE = "O8";
    }

    interface CustomDimension {
        String USER_ID = "userId";
        String PRODUCT_ID = "productId";
        String SHOP_ID = "shopId";
        String PROMO_ID = "promoId";
    }
}