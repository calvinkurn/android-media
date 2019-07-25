package com.tokopedia.home.account;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountConstants {

    public static final String QUERY = "query";
    public static final String TOPADS_QUERY = "topads_query";
    public static final String VARIABLES = "variables";
    public static final String RC_GIFTCARD_ENABLE = "enable_gift_card_transaction_history_entry";
    public static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";
    public static final String SALDO_QUERY = "saldo_query";

    public static class Navigation {
        public static final String SEE_ALL = "lihat_semua";
        public static final String TRAIN_ORDER_LIST = "train_order_list";
        public static final String TOPADS = "topads";
        public static final String FEATURED_PRODUCT = "featured_product";
    }

    public static class Analytics {
        public static final String CLICK_HOME_PAGE = "clickHomePage";
        public static final String CLICK_ACCOUNT = "clickAccount";
        public static final String AKUN_SAYA = "akun saya";
        public static final String CLICK = "click";
        public static final String PROFILE = "profile";
        public static final String TOKOPOINTS = "tokopoints";
        public static final String MY_COUPON = "kupon saya";
        public static final String LOAN = "modal usaha";
        public static final String PEMBELI = "pembeli";
        public static final String CLICK_CHALLENGE = "challenge";
        public static final String BY_ME = "by.me";
        public static final String PENJUAL = "penjual";
        public static final String USER = "user";
        public static final String SETTING = "setting";
        public static final String SHOP = "shop";
        public static final String ACCOUNT = "account";
        public static final String PAYMENT_METHOD = "payment method";
        public static final String NOTIFICATION = "notification";
        public static final String SHAKE_SHAKE = "shake - shake";
        public static final String TERM_CONDITION = "terms & condition";
        public static final String PRIVACY_POLICY = "privacy policy";
        public static final String APPLICATION_REVIEW = "application review";
        public static final String HELP_CENTER = "help center";
        public static final String DEVELOPER_OPTIONS = "developer options";
        public static final String LOGOUT = "logout";
        public static final String USER_DATA = "user_data";
        public static final String PERSONAL_DATA = "personal data";
        public static final String ADDRESS_LIST = "address list";
        public static final String KYC = "kyc";
        public static final String PASSWORD = "password";
        public static final String INFORMATION = "information";
        public static final String ETALASE = "etalase";
        public static final String PRODUCT = "product";
        public static final String LOCATION = "location";
        public static final String SHIPPING = "shipping";
        public static final String PAYMENT = "payment";
        public static final String NOTES = "notes";
        public static final String TOKOCASH = "tokocash";
        public static final String BALANCE = "balance";
        public static final String ACCOUNT_BANK = "account bank";
        public static final String CREDIT_CARD = "credit card";
        public static final String APPLICATION = "application";
        public static final String EMAIL = "email";
        public static final String PURCHASE = "purchase";
        public static final String SALES = "sales";
        public static final String MESSAGE = "message";
        public static final String DISCUSSION = "discussion";
        public static final String REVIEW = "review";
        public static final String PROMO = "promo";
        public static final String SELLER_INFO = "seller info";
        public static final String GROUP = "group";
        public static final String CHAT = "chat";
        public static final String TOKOPEDIA = "tokopedia";
        public static final String NEWS_LETTER = "newsletter";
        public static final String SCREEN_NAME = "screenName";
        public static final String EVENT = "event";
        public static final String EVENT_CATEGORY = "eventCategory";
        public static final String EVENT_ACTION = "eventAction";
        public static final String EVENT_LABEL = "eventLabel";
        public static final String TOP_NAV = "top nav";
        public static final String SCREEN_NAME_ACCOUNT = "/account";
        public static final String MY_ACCOUNT = "my account page";
        public static final String CLICK_MY_ACCOUNT_ACTIVATION_OVO = "click aktivasi ovo pada akun";
        public static final String CLICK_KYC_SETTING = "click on dokumen data diri";
        public static final String CLICK_KYC_REJECTED = "click on verifikasi ulang";
        public static final String CLICK_KYC_NOT_VERIFIED = "click on verifikasi sekarang";
        public static final String CLICK_KYC_PENDING = "click on lihat status";
        public static final String EVENT_SALDO_OVO = "clickSaldo";
        public static final String EVENT_CLICK_SAMPAI = "clickRegister";
        public static final String EVENT_CATEGORY_SAMPAI = "register tokopedia corner";
        public static final String EVENT_ACTION_SAMPAI = "click button daftar";
        public static final String OVO_PAY_LATER_CATEGORY = "fin - android main screen";
        public static final String OVO_PAY_LATER_CLICK = "ovo - pay later click";
        public static final String OVO_PAY_LATER_LABEL = "success - %s";
        public static final String OVO_PAY_ICON_CLICK = "ovo - tokopedia pay icon click";
        public static final String BY_ME_CURATION = "kurasi produk by.me";
        public static final String CLICK_FINTECH_MICROSITE = "clickFintechMicrosite";

        public static final String EVENT_CLICK_ACCOUNT = "clickAccount";
        public static final String CATEGORY_ACCOUNT_SELL = "my account - sell";
        public static final String ACTION_CLICK_OPEN_SHOP = "click open shop";
        public static final String ACTION_CLICK_LEARN_MORE = "click learn more";

        public static final String ECOMMERCE = "ecommerce";
        public static final String EVENT_PRODUCT_VIEW = "productView";

        public static final String EVENT_CATEGORY_ACCOUNT_PAGE_BUYER = "account page - buyer";
        public static final String EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION = "impression - product recommendation";
        public static final String CURRENCY_CODE = "currencyCode";
        public static final String IDR = "IDR";
        public static final String IMPRESSIONS = "impressions";
        public static final String DATA_NAME = "name";
        public static final String DATA_ID = "id";
        public static final String DATA_PRICE = "price";
        public static final String DATA_BRAND = "brand";
        public static final String DATA_CATEGORY = "category";
        public static final String DATA_VARIAN = "variant";
        public static final String LIST = "list";
        public static final String DATA_POSITION = "position";
        public static final String NONE_OTHER = "none/other";
        public static final String EVENT_PRODUCT_CLICK = "productClick";
        public static final String EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation";
        public static final String EMPTY = "";
        public static final String ACTION_FIELD = "actionField";
        public static final String PRODUCTS = "products";
        public static final String DATA_ATTRIBUTION = "attribution";
        public static final String VALUE_PRODUCT_RECOMMENDATION_LIST = "/user - rekomendasi untuk anda - %s";
        public static final String VALUE_PRODUCT_TOPADS = " - product topads";
    }

    public interface VccStatus {
        String ELIGIBLE = "eligible";
        String NOT_ELIGIBLE = "not_eligible";
        String HOLD = "hold";
        String ACTIVE = "active";
        String BLOCKED = "blocked";
        String KYC_PENDING = "kyc pending";
        String NOT_FOUND = "not_found";
        String REJECTED = "rejected";
        String DEACTIVATED = "deactivated";
    }

    public interface ErrorCodes{
        String ERROR_CODE_NULL_MENU = "ACC001";
    }
}
