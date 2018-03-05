package com.tokopedia.core.analytics;

import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.router.home.HomeRouter;


/**
 * @author by Nanda J.A on 6/16/2015.
 *         This class for casting and collection screen name
 */
public final class AppScreen {

    public static final String IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT = "RegisterNewNextFragment";
    public static final String IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT = "RegisterPassPhoneFragment";

    public static final String STATISTIC_PAGE = "Statistic Page";
    public static final String SCREEN_COPY_PRODUCT = "Salin Product Page";
    public static final String SCREEN_INSTOPED = "Instoped Upload Page";
    public static final String SCREEN_INDEX_HOME = "Index Main";
    public static final String SCREEN_NATIVE_RECHARGE = "Native Recharge";
    public static final String SCREEN_LOGIN = "Login page";
    public static final String SCREEN_REGISTER = "Register page";
    public static final String SCREEN_REGISTER_THIRD = "Register page - Social Media";
    public static final String SCREEN_REGISTER_ACTIVATION = "Register page - Activation Email";
    public static final String SCREEN_FORGOT_PASSWORD = "Forgot password page";
    public static final String SCREEN_BROWSE_PRODUCT = "Browse Product";
    public static final String SCREEN_BROWSE_SEARCH_DIR = "Browse Search";
    public static final String SCREEN_CATALOG = "Browse Catalog";
    public static final String SCREEN_CREATE_SHOP = "Create Shop";
    public static final String SCREEN_ADD_PRODUCT = "Add Product";
    public static final String SCREEN_EDIT_PRODUCT = "Edit Product";
    public static final String SCREEN_DEPOSIT = "Deposit";
    public static final String SCREEN_DEPOSIT_WITHDRAW = "Withdraw";
    public static final String SCREEN_PEOPLE = "Profile - Profile information";
    public static final String SCREEN_PEOPLE_SEND_MESSAGE = "People Send Message";
    public static final String SCREEN_PEOPLE_FAV = "Profile - Faved store";
    public static final String SCREEN_TX_PEOPLE = "People Transaction";
    public static final String SCREEN_TX_PEOPLE_DETAIL = "People Transaction Detail";
    public static final String SCREEN_TX_P_CONFIRM = "Confirm Payment";
    public static final String SCREEN_TX_P_CONFIRM_DETAIL = "Confirm Payment Detail";
    public static final String SCREEN_TX_P_STATUS = "People Status Order Deetail";
    public static final String SCREEN_TX_P_RECEIVE = "People Receive Confirmation Detail";
    public static final String SCREEN_TX_P_TX_LIST = "People Tx List Detail";
    public static final String SCREEN_CONFIG_PEOPLE = "People Config";
    public static final String SCREEN_CONFIG_P_PROFILE = "People Edit Profile";
    public static final String SCREEN_CONFIG_P_ADDRESS = "People Edit Address";
    public static final String SCREEN_ADD_ADDRESS_FORM = "Add Address Form";
    public static final String SCREEN_PEOPLE_BANK_FORM = "People Bank Form";
    public static final String SCREEN_CONFIG_P_BANK = "People Edit Bank";
    public static final String SCREEN_CONFIG_P_NOTIF = "People Edit Notif";
    public static final String SCREEN_CONFIG_P_PRIVACY = "People Edit Privacy";
    public static final String SCREEN_CONFIG_P_PASSWORD = "People Edit Password";
    public static final String SCREEN_SHOP = "Shop Info";
    public static final String SCREEN_OFFICIAL_STORE = "Official Store";
    public static final String SCREEN_OFFICIAL_STORE_REACT = "Official Store React";
    public static final String SCREEN_SHOP_SEND_MESSAGE = "Shop Send Message";
    public static final String SCREEN_SHOP_DETAIL = "Shop Info Detail";
    public static final String SCREEN_SHOP_DETAIL_STATS = "Shop Info Detail Statistic";
    public static final String SCREEN_SHOP_STATS = "Shop Info Statistic";
    public static final String SCREEN_SHOP_NOTE = "Shop Note View";
    public static final String SCREEN_SHOP_FAVORITER = "Shop Favorited List";
    public static final String SCREEN_TX_SHOP = "Shop Transaction";
    public static final String SCREEN_TX_S_NEW_ORDER = "Shop New Order Detail";
    public static final String SCREEN_TX_S_CONFIRM_SHIPPING = "Shop Confirm Shipping Detail";
    public static final String SCREEN_TX_S_CONFIRM_SHIPPING_CONF = "Shop Confirm Shipping Confirmation";
    public static final String SCREEN_TX_S_SHIPPING_STATUS = "Shop Shipping Status Detail";
    public static final String SCREEN_TX_S_TX_LIST = "Shop Tx List Detail";
    public static final String SCREEN_CONFIG_SHOP = "Shop Config";
    public static final String SCREEN_CONFIG_S_INFO = "Shop Edit Info";
    public static final String SCREEN_CONFIG_S_SHIPPING = "Shop Edit Shipping";
    public static final String SCREEN_OPEN_CONFIG_S_SHIPPING = "Open Shop Edit Shipping";
    public static final String SCREEN_CONFIG_S_PAYMENT = "Shop Edit Payment";
    public static final String SCREEN_CONFIG_S_ETALASE = "Store - Etalase";
    public static final String SCREEN_CONFIG_S_NOTES = "Shop Edit Notes";
    public static final String SCREEN_SGOP_NOTE_FORM = "Shop Note Form";
    public static final String SCREEN_CONFIG_S_ADDRESS = "Shop Edit Address";
    public static final String SCREEN_SHOP_ADDRESS_FORM = "Shop Address Form";
    public static final String SCREEN_MANAGE_PROD = "Store - Manage product";
    public static final String SCREEN_INBOX_MESSAGE = "Inbox message";
    public static final String SCREEN_INBOX_MESSAGE_VIEW = "Inbox Message View";
    public static final String SCREEN_INBOX_MESSAGE_DETAIL_VIEW = "Inbox Message Detail View";
    public static final String SCREEN_INBOX_TALK = "Inbox talk";
    public static final String SCREEN_INBOX_REVIEW = "Inbox review";
    public static final String SCREEN_INBOX_REPUTATION_FILTER = "Inbox Reputation - Filter";
    public static final String SCREEN_INBOX_REPUTATION_DETAIL = "Inbox detail reputation";
    public static final String SCREEN_INBOX_REPUTATION_REVIEW_DETAIL = "Inbox Detail Reputation " +
            "Review";
    public static final String SCREEN_INBOX_REPUTATION_REPORT = "Inbox Reputation - Report";
    public static final String SCREEN_INBOX_RESOLUTION_CENTER_DETAIL = "Inbox resolution center detail";
    public static final String SCREEN_INBOX_TICKET_DETAIL = "Inbox Ticket Detail";
    public static final String SCREEN_BROWSE_HOT = "Browse hot list detail";
    public static final String SCREEN_BROWSE_HOT_LIST = "Browse Hot List";
    public static final String SCREEN_PRODUCT_INFO = "Product Info";
    public static final String SCREEN_PRODUCT_INFO_DETAIL = "Product Info - Detail Event";
    public static final String SCREEN_PRODUCT_TALK = "Product Talk";
    public static final String SCREEN_PRODUCT_TALK_VIEW = "Product Talk View";
    public static final String SCREEN_SHOP_TALK_VIEW = "Shop Talk View";
    public static final String SCREEN_PRODUCT_REVIEW = "Product Review";
    public static final String SCREEN_PRODUCT_REVIEW_VIEW = "Product Review View";
    public static final String SCREEN_ADD_TO_CART = "Add to cart form page";
    public static final String SCREEN_ADD_TO_CART_SUCCESS = "Add to cart form page - Success ATC";
    public static final String SCREEN_CART = "Cart";
    public static final String SCREEN_CHOOSE_ADDR = "Choose Address";
    public static final String SCREEN_ATC_EDIT_ADDR = "Cart Edit Address";
    public static final String SCREEN_PRODUCT_TALK_ADD = "Add new Talk";
    public static final String SCREEN_PEOPLE_ADDRESS = "People address";
    public static final String SCREEN_INBOX_TICKET_ADD = "Add new ticket";
    public static final String SCREEN_SETTING_MANAGE_PROFILE = "Setting - Manage Profile";
    public static final String SCREEN_SETTING_MANAGE_SHOP = "Setting - Manage Shop";
    public static final String SCREEN_SETTING_MANAGE_APP = "Setting - Manage App";
    public static final String SCREEN_SETTING_ABOUT_US = "Setting - About Us";
    public static final String SCREEN_CART_PAGE = "Cart page (step 1)";
    public static final String SCREEN_CART_PAGE_REMOVE = "Cart page (step 1) - Remove Cart";
    public static final String SCREEN_CART_SUMMARY = "Cart summary page (step 2)";
    public static final String SCREEN_CART_SUMMARY_CHECKOUT = "Cart summary page (step 2) - Checkout";
    public static final String SCREEN_CART_FINISH = "Thank you page (step 3)";
    public static final String SCREEN_HOME_FAVORITE_SHOP = "Home - Fav. Store";
    public static final String SCREEN_HOME_RECENT_PRODUCT = "Home - History product";
    public static final String SCREEN_HOME_WISHLIST = "WishList page";
    public static final String SCREEN_FRAGMENT_WISHLIST = "WishList page";
    public static final String SCREEN_HOME_PRODUCT_FEED = "Home - Product feed";
    public static final String SCREEN_HOME_PRODUCT_CATEGORY = "Top category page";
    public static final String SCREEN_HOME_HOTLIST = "Home - hot list";
    public static final String SCREEN_TOPADS = "Top Ads Screen";
    public static final String SCREEN_SORT_PRODUCT = "Sort Produk Activity";
    public static final String SCREEN_BROWSE_PRODUCT_FROM_SEARCH = "Browse Produk - From Search";
    public static final String SCREEN_BROWSE_PRODUCT_FROM_CATEGORY = "Browse Category - ";
    public static final String SCREEN_SEARCH_PAGE = "Search page";
    public static final String SCREEN_SEARCH_PAGE_PRODUCT_TAB = "Search result - Product tab";
    public static final String SCREEN_SEARCH_PAGE_CATALOG_TAB = "Search result - Catalog tab";
    public static final String SCREEN_SEARCH_PAGE_SHOP_TAB = "Search result - Store tab";
    public static final String SCREEN_NOTIFICATION = "Top notification center";
    public static final String SCREEN_SHOP_PRODUCT_LIST = "Store - Product list";
    public static final String SCREEN_SHOP_TALK_LIST = "Store - Talk list";
    public static final String SCREEN_SHOP_REVIEW_LIST = "Store - Review list";
    public static final String SCREEN_SHOP_NOTE_LIST = "Store - Note list";
    public static final String SCREEN_SHOP_MANAGE_PRODUCT = "Store - Manage product";
    public static final String SCREEN_SHOP_ETALASE_LIST = "Store - Etalase";
    public static final String SCREEN_TX_SHOP_CENTER = "Transaction - Sell page";
    public static final String SCREEN_TX_SHOP_NEW_ORDER = "New order";
    public static final String SCREEN_TX_SHOP_CONFIRM_SHIPPING = "Confirm shipping";
    public static final String SCREEN_TX_SHOP_SHIPPING_STATUS = "Shipping status";
    public static final String SCREEN_TX_SHOP_TRANSACTION_SELLING_LIST = "Transaction list - Selling";
    public static final String SCREEN_TX_PEOPLE_CENTER = "Transaction - Buy page";
    public static final String SCREEN_TX_PEOPLE_CONFIRM_PAYMENT = "Confirm payment";
    public static final String SCREEN_TX_PEOPLE_ORDER_STATUS = "Order status";
    public static final String SCREEN_TX_PEOPLE_CONFIRM_RECEIVED = "Confirm received";
    public static final String SCREEN_TX_PEOPLE_TRANSACTION_BUYING_LIST = "Transaction list - Buying";
    public static final String SCREEN_FINISH_TX = "Finish Transaction";
    public static final String SCREEN_TOPPOINTS_DETAIL = "Top points detail page";
    public static final String SCREEN_CONTACT_US = "Contact us page";
    public static final String SCREEN_INBOX_RESOLUTION_CENTER = "Inbox resolution center";
    public static final String SCREEN_RESOLUTION_CENTER_EDIT = "Edit resolution center";
    public static final String SCREEN_RESOLUTION_CENTER = "Resolution center page";
    public static final String SCREEN_RESOLUTION_CENTER_ADD = "Add resolution center page";
    public static final String SCREEN_RESOLUTION_CENTER_INPUT_SHIPPING = "Input shipping form page";
    public static final String SCREEN_SHOP_SELLING_DETAIL = "Selling Detail Page";
    public static final String SCREEN_ORDER_HISTORY = "History order";
    public static final String SCREEN_ORDER_REJECT = "Confirm Reject Order Screen";
    public static final String SCREEN_ORDER_HISTORY_DETAIL = "History order detail";
    public static final String SCREEN_MANAGE_GENERAL = "General Setting";
    public static final String SCREEN_PEOPLE_BANK = "People Bank";
    public static final String SCREEN_SHOP_EDITOR = "Edit Shop Information page";
    public static final String SCREEN_SHOP_ADDRESS_EDITOR = "Edit Shop Address page";
    public static final String SCREEN_CREDIT_CARD = "Credit card page";
    public static final String SCREEN_TRACKING_DETAIL = "Tracking detail page";
    public static final String SCREEN_CART_EDIT_ADDRESS = "Edit address page";
    public static final String SCREEN_GALLERY_BROWSER = "Gallery Browser page";
    public static final String SCREEN_GALLERY_IMAGE = "Image Gallery page";
    public static final String SCREEN_REJECT_ADMIN_SOL = "Reject admin solution page";
    public static final String SCREEN_SHOP_REPUTATION = "Reputation shop page";
    public static final String SCREEN_REVIEW_FORM = "Review form page";
    public static final String SCREEN_SHOP_FAV_DETAIL = "Detail favorite shop page";
    public static final String SCREEN_SHOP_LOCATION = "Shop location page";
    public static final String SCREEN_SHOP_ORDER_DETAIL = "Shop order detail page";
    public static final String SCREEN_DEVELOPER = "Developer page";
    public static final String SCREEN_INBOX_TICKET = "Inbox Ticket";
    public static final String SCREEN_PRODUCT_REPUTATION_VIEW = "Product Reputation View";
    public static final String SCREEN_ADDRESS_GEOLOCATION = "Add Geolocation Address page";
    public static final String SCREEN_INBOX_REPUTATION = "Inbox reputation page";
    public static final String SCREEN_WEBVIEW_BANNER = "Banner Web View";
    public static final String SCREEN_WEBVIEW = "General Web View";
    public static final String SCREEN_SHOP_INFO = "Shop info detail page";
    public static final String SCREEN_PRODUCT_IMAGE_PREVIEW = "Preview Image Product page";
    public static final String SCREEN_LOYALTY = "Loyalty page";
    public static final String SCREEN_WALLET = "Wallet page";
    public static final String SCREEN_CREDIT_CARD_BUY = "Credit card buy page";
    public static final String SCREEN_CREDIT_CARD_CART_BUY = "Credit card buy cart page";
    public static final String SCREEN_DOWNLOAD_INVOICE = "Download invoice page";
    public static final String SCREEN_HELP_DIALOG = "Help dialog page";
    public static final String SCREEN_CONFIRMATION_SUCCESS = "Success Confirmation page";
    public static final String SCREEN_PAYMENT_VERIFICATION_DETAIL = "Payment Verification Detail page";
    public static final String SCREEN_IMAGE_PICKER = "Image picker page";
    public static final String SCREEN_PROFILE_EDIT_ADDRESS = "Edit Profile Address page";
    public static final String SCREEN_SERVER_ERROR = "Server error page";
    public static final String SCREEN_SPLASH_SCREEN = "Splash Screen page";
    public static final String SCREEN_TERM_PRIVACY = "Term privacy page";
    public static final String SCREEN_SUGESTION_MAP = "Suggestion Map page";
    public static final String SCREEN_MANAGE_PEOPLE = "Manage People page";
    public static final String SCREEN_DEEP_LINK = "Deeplink page";
    public static final String SCREEN_PRODUCT_REPUTATION_VIEW_DETAIL = "Product Reputation View Detail";
    public static final String SCREEN_RECHARGE = "Recharge";
    public static final String SCREEN_RECHARGE_PAYMENT = "Recharge Payment WebView";
    public static final String SCREEN_SHARE = "Share Screen";
    public static final String SCREEN_GALLERY_BROWSE = "Browse Gallery Screen";
    public static final String SCREEN_GM_SUBSCRIBE = "Gold Merchant Subscribe";
    public static final String SCREEN_GM_SUBSCRIBE_CHECKOUT = "Gold Merchant Subscribe Checkout Page";
    public static final String SCREEN_GM_SUBSCRIBE_PAYMENT = "Gold Merchant Subscribe Payment Page";
    public static final String SCREEN_GM_SUBSCRIBE_PRODUCT = "Gold Merchant Product Subscribe Page";
    public static final String SCREEN_SELLER_HOME = "Seller Home";
    public static final String SCREEN_SELLER_REP_HISTORY = "Reputation History";
    public static final String SCREEN_SELLER_SHOP_SCORE = "Shop Score";
    public static final String SCREEN_PHONE_VERIFICATION = "Phone Verification Screen";
    public static final String SCREEN_OTP_SQ = "Security Question Screen";
    public static final String GOLD_MERCHANT_REDIRECT = "Gold Merchant Redirect Page";
    public static final String SCREEN_INBOX_MAIN = "Inbox Page";
    public static final String SCREEN_INBOX_SENT = "Inbox - Sent Page";
    public static final String SCREEN_INBOX_ARCHIVE = "Inbox - Archive Page";
    public static final String SCREEN_INBOX_TRASH = "Inbox - Trash Page";
    public static final String SCREEN_LOCA_NEWORDER = "Transaction - New Order Page";
    public static final String SCREEN_LOCA_TXCENTER = "Transaction List - Selling Page";
    public static final String SCREEN_LOCA_TXSTATUS = "Transaction - Sell page";
    public static final String SCREEN_LOCA_SHIPPING = "Transaction - Shipping Confirmation Page";
    public static final String SCREEN_LOCA_SHIPPINGSTATUS = "Transaction - Shipping status";
    public static final String SCREEN_TX_SHOP_LOCA = "event : Viewed Transaction Shop Page";
    public static final String SCREEN_HOME_CATEGORY = "event : Viewed Home - Category";
    public static final String SCREEN_HOME_FEED = "event : Viewed Home - Product feed Page";
    public static final String SCREEN_VIEWED_SHOP_PAGE = "event : Viewed Shop Page";
    public static final String SCREEN_VIEWED_ADD_TO_CART = "event : Viewed Add To Cart Page";
    public static final String SCREEN_VIEWED_FAV_STORE = "event : Viewed Home - Fav Store Page";
    public static final String SCREEN_VIEWED_WISHLIST = "event : Viewed Wishlist Page ";
    public static final String SCREEN_VIEWED_LOGIN = "event : Viewed Login Page ";
    public static final String SCREEN_VIEWED_TRANSACTION_SHOP = "event : Viewed Transaction Shop Page ";
    public static final String SCREEN_VIEWED_SEARCH_PAGE = "event : Viewed Search Page";
    public static final String EVENT_ADDED_PRODUCT = "event : Add Product";
    public static final String EVENT_ADDED_WISHLIST = "event : Add to wishlist";
    public static final String EVENT_CLICKED_HOTLIST = "event : Clicked Hot List";
    public static final String EVENT_GOOD_REVIEW = "event: Good Review";
    public static final String SCREEN_VIEWED_WISHLIST_PAGE = "event : Viewed Wishlist Page";
    public static final String SCREEN_VIDEO_PLAYER = "Video player page";
    public static final String SCREEN_RIDE_HOME = "Ride Home Page";
    public static final String SCREEN_RIDE_BOOKING = "Ride Booking Page";
    public static final String SCREEN_RIDE_APPLYPROMO = "Ride Promo Code Screen";
    public static final String SCREEN_RIDE_ONTRIP = "Ride Booked Screen";
    public static final String SCREEN_RIDE_COMPLETED = "Ride Completed Screen";
    public static final String SCREEN_RIDE_SOURCE_CHANGE = "Ride Source Change Screen";
    public static final String SCREEN_RIDE_DEST_CHANGE = "Ride Destination Change Screen";
    public static final String SCREEN_RIDE_HISTORY_DETAIL = "Ride Trip Detail Screen";
    public static final String SCREEN_RIDE_HISTORY = "Ride Your Trips Screen";
    public static final String SCREEN_RIDE_CANCEL_REASON = "Ride Cancel Reason Screen";
    public static final String SCREEN_RIDE_HISTORY_NEED_HELP ="Ride History Need Help Screen";
    public static final String SCREEN_RIDE_PENDING_FARE_CHOOSER ="Ride Pending Fare Chooser Screen";
    public static final String SCREEN_RIDE_TOPUP_TOKOCASH_CHANGE_DESTINATION = "Ride Topup TokoCash Change Destination Screen";
    public static final String SCREEN_DEEPLINK_APPLINKHANDLER = "AppLink Handler";
    public static final String SCREEN_RESOLUTION_CENTER_HISTORY_ACTION = "Resolution Center - History Action Page";
    public static final String SCREEN_RESOLUTION_CENTER_HISTORY_ADDRESS = "Resolution Center - History Address Page";
    public static final String SCREEN_RESOLUTION_CENTER_HISTORY_SHIPPING = "Resolution Center - History Shipping Page";
    public static final String SCREEN_RESOLUTION_CENTER_PRODUCT_LIST = "Resolution Center - Product List";
    public static final String SCREEN_RESOLUTION_CENTER_PRODUCT_DETAIL = "Resolution Center - Product Detail";
    public static final String SCREEN_RESOLUTION_CENTER_DISCUSSION = "Resolution Center - Discussion";
    public static final String CREATE_SHOP_REDIRECT = "Create Shop Redirect Page";
    public static final String SCREEN_FEED_DETAIL = "Feed Detail";
    public static final String SCREEN_OPPORTUNITY_DETAIL = "Replacement Detail Page";
    public static final String SCREEN_OPPORTUNITY_TAB= "Replacement Main Page";
    public static final String SCREEN_ONBOARDING = "Screen OnBoarding ";
    public static final String SCREEN_KOL_COMMENTS = "Kol Comments Page";
    public static final String SCREEN_DISCOVERY_PAGE = "Discovery Page";
    public static final String SCREEN_CHAT = "inbox-chat";
    public static final String SCREEN_CHAT_DETAIL = "chat detail";
    public static final String SCREEN_TEMPLATE_CHAT_SETTING = "template setting";
    public static final String SCREEN_TEMPLATE_CHAT_SET = "template update";

    public static final String SCREEN_CHANGE_PHONE_NUMBER = "Change Phone Number Page";

    public static final String SCREEN_CHANGE_PHONE_NUMBER_WARNING = "Warning";
    public static final String SCREEN_CHANGE_PHONE_NUMBER_INPUT = "Change Number";
    public static String GROUP_CHAT = "Group Chat Page";

    public static class UnifyScreenTracker {
        public static final String SCREEN_UNIFY_HOME_BERANDA = "/";
        public static final String SCREEN_UNIFY_HOME_FEED = "/feed";
        public static final String SCREEN_UNIFY_HOME_SHOP_FAVORIT = "/fav-shop";
        public static final String SCREEN_UNIFY_HOME_HOTLIST = "/hotlist";
    }

    private AppScreen() {

    }

    public static String convertAFActivityEvent(String tag) {
        if (tag.equals(HomeRouter.IDENTIFIER_HOME_ACTIVITY)) {
            return AFInAppEventType.LOGIN;
        } else {
            return AFInAppEventType.CONTENT_VIEW;
        }
    }

    public static String convertAFFragmentEvent(String tag) {
        if (tag.equals(IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT) || tag.equals(IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT)) {
            return AFInAppEventType.COMPLETE_REGISTRATION;
        } else {
            return AFInAppEventType.CONTENT_VIEW;
        }
    }
}
