package com.tokopedia.home.account;

/**
 * @author okasurya on 9/14/18.
 */
public class AccountHomeUrl {
    public static String BASE_SELLER_URL = "https://seller.tokopedia.com/";
    public static String WEB_DOMAIN = "https://www.tokopedia.com/";

    public static final String CDN_URL = "https://ecs7.tokopedia.net";
    public static final String CDN_IMAGE_PATH = "/img/android/others/";
    public static final String IMAGE_URL = CDN_URL + CDN_IMAGE_PATH;

    public static final String MORE_SELLER = BASE_SELLER_URL + "mulai-berjualan/";
    public static final String GOLD_MERCHANT = BASE_SELLER_URL + "gold-merchant/";

    public class Pulsa {

        public static final String BASE_PULSA_URL = "https://pulsa.tokopedia.com/";

        public static final String PULSA_SUBSCRIBE = BASE_PULSA_URL + "subscribe/";

        public static final String PULSA_FAV_NUMBER = BASE_PULSA_URL + "favorite-list/";

        public static final String ZAKAT_URL = BASE_PULSA_URL + "berbagi/operator/?category_id=16";

        public static final String MYBILLS = BASE_PULSA_URL + "mybills/";
    }

    public static final String REKSA_DANA_URL = WEB_DOMAIN + "reksa-dana/";
    public static final String REKSA_DANA_TX_URL = WEB_DOMAIN + "reksa-dana/mobile-dashboard/list";

    public static final String TOKOCARD_URL = WEB_DOMAIN + "tokocard/";

    public static final String EMAS_URL = WEB_DOMAIN + "emas/";
    public static final String EMAS_TX_URL = WEB_DOMAIN + "emas/daftar-transaksi/";
}
