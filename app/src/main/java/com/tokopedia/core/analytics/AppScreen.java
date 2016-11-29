package com.tokopedia.core.analytics;

import android.app.Activity;
import android.app.Fragment;

import com.appsflyer.AFInAppEventType;
import com.tokopedia.core.R;
import com.tokopedia.core.fragment.AboutFragment;
import com.tokopedia.core.fragment.FragmentCart;
import com.tokopedia.core.fragment.FragmentCartFinish;
import com.tokopedia.core.fragment.FragmentCartSummary;
import com.tokopedia.core.fragment.FragmentSettingPeople;
import com.tokopedia.core.fragment.FragmentSettingShop;
import com.tokopedia.core.fragment.SettingsFragment;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFormFragment;
import com.tokopedia.core.manage.people.notification.activity.ManageNotificationActivity;
import com.tokopedia.core.manage.people.password.activity.ManagePasswordActivity;
import com.tokopedia.core.manage.people.profile.activity.ManagePeopleProfileActivity;
import com.tokopedia.core.manage.shop.notes.activity.ManageShopNotesActivity;
import com.tokopedia.core.manage.shop.notes.fragment.ManageShopNotesFormFragment;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.myproduct.ProductActivity;
import com.tokopedia.core.myproduct.fragment.AddProductFragment;

//import com.tokopedia.discovery.catalog.activity.CatalogDetailActivity;


/**
 * @author by Nanda J.A on 6/16/2015.
 *         This class for casting and collection screen name
 */
public final class AppScreen {

    private AppScreen() {

    }

    public static String convertFragmentScreen(Fragment actClass) {
        if (actClass instanceof AboutFragment) {
            return SCREEN_SETTING_ABOUT_US;
        } else if (actClass instanceof FragmentSettingPeople) {
            return SCREEN_SETTING_MANAGE_PROFILE;
        } else if (actClass instanceof SettingsFragment) {
            return SCREEN_SETTING_MANAGE_APP;
        } else if (actClass instanceof FragmentSettingShop) {
            return SCREEN_SETTING_MANAGE_SHOP;
        } else if (actClass instanceof FragmentCart) {
            return SCREEN_CART_PAGE;
        } else if (actClass instanceof FragmentCartSummary) {
            return SCREEN_CART_SUMMARY;
        } else if (actClass instanceof FragmentCartFinish) {
            return SCREEN_CART_FINISH;
        } else if (actClass instanceof ManagePeopleBankFormFragment) {
            return SCREEN_PEOPLE_BANK_FORM;
        } else {
            return actClass.getClass().getSimpleName();
        }
    }


    public static String convertAFFragmentEvent(android.support.v4.app.Fragment fragment){
//        if (fragment instanceof RegisterNewNextFragment || fragment instanceof RegisterPassPhoneFragment){
//            return AFInAppEventType.COMPLETE_REGISTRATION;
//        } else {
//            return AFInAppEventType.CONTENT_VIEW;
//        }
        return null;
    }

    public static String convertFragmentScreen(android.support.v4.app.Fragment actClass) {
//        if (actClass instanceof FragmentHotListV2) {
//            return SCREEN_HOME_HOTLIST;
//        } else if (actClass instanceof FragmentIndexFavoriteV2) {
//            public static String convertFragmentScreen(android.support.v4.app.Fragment actClass) {
//                if (actClass instanceof FragmentHotListV2) {
//                    return SCREEN_HOME_HOTLIST;
//                } else if (actClass instanceof FragmentIndexFavoriteV2) {
//                    return SCREEN_HOME_FAVORITE_SHOP;
//                } else if (actClass instanceof FragmentIndexCategory) {
//                    return SCREEN_HOME_PRODUCT_CATEGORY;
//                } else if (actClass instanceof FragmentProductFeed) {
//                    return SCREEN_HOME_PRODUCT_FEED;
//                } else if (actClass instanceof RegisterNewViewFragment) {
//                    return SCREEN_REGISTER;
//                } else if (actClass instanceof RegisterPassPhoneFragment) {
//                    return SCREEN_REGISTER_THIRD;
//                } else if (actClass instanceof WishListFragment) {
//                    return SCREEN_FRAGMENT_WISHLIST;
//                } else if (actClass instanceof ForgotPasswordFragment) {
//                    return SCREEN_FORGOT_PASSWORD;
//                } else if (actClass instanceof ActivationResentFragment) {
//                    return SCREEN_REGISTER_ACTIVATION;
//                } else if (actClass instanceof AddProductFragment) {
//                    return SCREEN_ADD_PRODUCT;
//                    //} else if (actClass instanceof FragmentShopTxStatusDetailV2){
//                    //  return SCREEN_TX_PEOPLE_DETAIL;
//                } else {
//                    return actClass.getClass().getSimpleName();
//                }
//            }
//            return SCREEN_HOME_FAVORITE_SHOP;
//        } else if (actClass instanceof FragmentIndexCategory) {
//            return SCREEN_HOME_PRODUCT_CATEGORY;
//        } else if (actClass instanceof FragmentProductFeed) {
//            return SCREEN_HOME_PRODUCT_FEED;
//        } else if (actClass instanceof RegisterNewViewFragment) {
//            return SCREEN_REGISTER;
//        } else if (actClass instanceof RegisterPassPhoneFragment) {
//            return SCREEN_REGISTER_THIRD;
//        } else if (actClass instanceof WishListFragment) {
//            return SCREEN_FRAGMENT_WISHLIST;
//        } else if (actClass instanceof ForgotPasswordFragment) {
//            return SCREEN_FORGOT_PASSWORD;
//        } else if (actClass instanceof ActivationResentFragment) {
//            return SCREEN_REGISTER_ACTIVATION;
//        } else if (actClass instanceof AddProductFragment) {
//            return SCREEN_ADD_PRODUCT;
//            //} else if (actClass instanceof FragmentShopTxStatusDetailV2){
//            //  return SCREEN_TX_PEOPLE_DETAIL;
//        } else {
//            return actClass.getClass().getSimpleName();
//        }
        //TODO fix tracking
        // udah persetujuan dari hafiz suruh comment
        return actClass.getClass().getSimpleName();
    }

    private static Fragment getFragment(Activity actClass) {
        return actClass.getFragmentManager().findFragmentById(R.id.container);
    }

    public static final String SCREEN_INDEX_HOME = "Index Main";
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
    public static final String SCREEN_INBOX_REPUTATION_DETAIL = "Inbox detail reputation";
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
    public static final String SCREEN_BROWSE_PRODUCT_FROM_CATEGORY = "Browse Category";
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
    public static final String SCREEN_SHOP_INFO = "Shop info detail page";
    public static final String SCREEN_PRODUCT_IMAGE_PREVIEW = "Preview Image Product page";
    public static final String SCREEN_LOYALTY = "Loyalty page";
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
    public static final String SCREEN_GM_SUBSCRIBE = "Subscribe to the GM";
    public static final String SCREEN_SELLER_HOME = "Seller Home";
}