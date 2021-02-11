package com.tokopedia.core.network.constants;

import com.tokopedia.url.TokopediaUrl;

/**
 * Created by Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public class TkpdBaseURL {

    public static String BASE_DOMAIN = TokopediaUrl.Companion.getInstance().getWS();
    public static String MERLIN_DOMAIN = TokopediaUrl.Companion.getInstance().getMERLIN();
    public static String GOOGLE_APIS = "https://www.googleapis.com";
    public static String TOPADS_DOMAIN = TokopediaUrl.Companion.getInstance().getTA();
    public static String ACCOUNTS_DOMAIN = TokopediaUrl.Companion.getInstance().getACCOUNTS();
    public static String INBOX_DOMAIN = TokopediaUrl.Companion.getInstance().getINBOX();
    public static String JS_DOMAIN = TokopediaUrl.Companion.getInstance().getJS();
    public static String JS_STAGING_DOMAIN = "https://js-staging.tokopedia.com/";
    public static String JS_ALPHA_DOMAIN = "https://ajax-alpha.tokopedia.com/js/";
    public static String GOLD_MERCHANT_DOMAIN = TokopediaUrl.Companion.getInstance().getGOLDMERCHANT();
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();
    public static String BASE_CONTACT_US = WEB_DOMAIN + "contact-us";
    public static String TOKOPEDIA_CART_DOMAIN = TokopediaUrl.getInstance().getFS() + "tkpdcart/";
    public static String DIGITAL_API_DOMAIN = TokopediaUrl.Companion.getInstance().getPULSA_API();
    public static String SCROOGE_DOMAIN = TokopediaUrl.Companion.getInstance().getPAY();

    public static class User {
        public static final String URL_PEOPLE = BASE_DOMAIN + "v4/people/";
        public static final String PATH_GET_PEOPLE_INFO = "get_people_info.pl";
    }

    public static class Shop {
        public static final String PATH_MY_SHOP = "v4/myshop/";
        public static final String URL_MY_SHOP = BASE_DOMAIN + PATH_MY_SHOP;
        public static final String PATH_MY_SHOP_ETALASE = "v4/myshop-etalase/";
        public static final String PATH_ACTION_MY_SHOP_ETALASE = "v4/action/myshop-etalase/";
        public static final String PATH_MY_SHOP_SHIPMENT = "v4/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT = BASE_DOMAIN + PATH_MY_SHOP_SHIPMENT;
        public static final String PATH_MY_SHOP_SHIPMENT_ACTION = "v4/action/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT_ACTION = BASE_DOMAIN + PATH_MY_SHOP_SHIPMENT_ACTION;
        public static final String PATH_SHOP = "v4/shop/";
        public static final String URL_SHOP = BASE_DOMAIN + PATH_SHOP;
        public static final String PATH_SHIPPING_WEBVIEW = "v4/web-view/";
        public static final String URL_SHIPPING_WEBVIEW = BASE_DOMAIN + PATH_SHIPPING_WEBVIEW;

        public static final String PATH_GET_OPEN_SHOP_FORM = "get_open_shop_form.pl";

        public static final String PATH_GET_SHOP_ETALASE = "get_shop_etalase.pl";

        public static final String PATH_GET_SHOP_INFO = "get_shop_info.pl";
        public static final String PATH_GET_DETAIL_INFO_DETAIL = "get_shipping_detail_info.pl";

        public static final String PATH_UPDATE_SHIPPING_INFO = "update_shipping_info.pl";

        public static final String PATH_GET_SHIPPING_INFO = "get_shipping_info.pl";
        public static final String PATH_GET_SHOP_LOCATION = "get_shop_location.pl";
        public static final String PATH_GET_SHOP_PRODUCT = "get_shop_product.pl";

    }

    public static class Search {
        public static final String URL_HOT_LIST = BASE_DOMAIN + "v4/hotlist/";

        public static final String PATH_GET_HOTLIST = "get_hotlist.pl";
    }

    public static class Upload {
        public static final String V4_ACTION_GENERATE_HOST = "v4/action/generate-host/";

        public static final String PATH_GENERATE_HOST = "generate_host.pl";
    }

    public static class Ace {
        public static final String PATH_SEARCH = "search/";
        public static final String PATH_CATALOG = "v1/catalog";
    }

    public static class Merlin {
        public static final String PATH_CATEGORY_RECOMMENDATION = "v4/product/category/recommendation";

    }

    public static class Accounts {
        public static final String PATH_GET_TOKEN = "token";
        public static final String PATH_DISCOVER_LOGIN = "api/discover";
        public static final String GENERATE_HOST = "/api/upload-host";
    }

    public static class GoldMerchant {
        public static final String GET_PRODUCT_VIDEO = "/v1/product/video/";
        public static final String GET_SHOP_SCORE_SUMMARY = "/v1/shopstats/shopscore/sum/";
        public static final String GET_SHOP_SCORE_DETAIL = "/v1/shopstats/shopscore/dtl/";
    }

    public static class FCM {
        public static final String UPDATE_FCM = "/api/gcm/update";
    }

    public static class ContactUs {
        public static final String PATH_GET_SOLUTION = "ajax/solution/{id}";
        public static final String PATH_CREATE_STEP_1 = "ajax/create/step/1";
        public static final String PATH_CREATE_STEP_2 = "ajax/create/step/2";
        public static final String PATH_COMMENT_RATING = "ws/contact-us/rating";
    }

    public static class DigitalApi {
        public static final String VERSION = "v1.4/";
        public static final String HMAC_KEY = "web_service_v4";
    }

    public static class ResCenterV2 {
        public static final String BASE_RESOLUTION = TokopediaUrl.Companion.getInstance().getAPI() + "resolution/";
    }

    public static class Payment {
        public static final String PATH_ZEUS_UPDATE_WHITELIST = "zeus/whitelist";
    }

    public static class Chat {
        public static final String DELETE = "/tc/v1/delete";
    }

    public class Purchase {
        public static final String PATH_ORDER_HISTORY = "/v4/order/history";
    }
}