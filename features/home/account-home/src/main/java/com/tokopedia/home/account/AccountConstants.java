package com.tokopedia.home.account;

import com.tokopedia.network.constant.TkpdBaseURL;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountConstants {

    public static final String QUERY = "query";
    public static final String VARIABLES = "variables";
    public static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    public static class Navigation {
        public static final String SEE_ALL = "lihat_semua";
        public static final String TRAIN_ORDER_LIST = "train_order_list";
        public static final String TOPADS = "topads";
    }

    public static class Url {

        public static final String BASE_SELLER_URL = "https://seller.tokopedia.com/";

        public static final String CDN_URL = "https://ecs7.tokopedia.net";
        public static final String CDN_IMAGE_PATH = "/img/android/others/";
        public static final String IMAGE_URL = CDN_URL + CDN_IMAGE_PATH;

        public static final String MORE_SELLER = BASE_SELLER_URL + "mulai-berjualan/";

        public class Pulsa {

            public static final String BASE_PULSA_URL = "https://pulsa.tokopedia.com/";

            public static final String PULSA_SUBSCRIBE = BASE_PULSA_URL + "subscribe/";

            public static final String PULSA_FAV_NUMBER = BASE_PULSA_URL + "favorite-list/";

            public static final String ZAKAT_URL = BASE_PULSA_URL + "berbagi/operator/?category_id=16";

            public static final String MYBILLS = BASE_PULSA_URL + "mybills/";
        }

        public static final String REKSA_DANA_URL = TkpdBaseURL.WEB_DOMAIN + "reksa-dana/";

        public static final String EMAS_URL = TkpdBaseURL.WEB_DOMAIN + "emas/";

    }

    public static class Analytics {

        static final String CLICK_HOME_PAGE = "clickHomePage";
        public static final String AKUN_SAYA = "akun saya";
        public static final String CLICK = "click";
        public static final String PROFILE = "profile";
        public static final String TOKOPOINTS = "tokopoints";
        public static final String MY_COUPON = "kupon saya";
        public static final String PEMBELI = "pembeli";
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
        public static final String PERSONAL_DATA = "personal data";
        public static final String ADDRESS_LIST = "address list";
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
    }
}
