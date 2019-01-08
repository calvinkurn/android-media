package com.tokopedia.home.account;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.network.constant.TkpdBaseURL;

/**
 * @author okasurya on 9/14/18.
 */
public class AccountHomeUrl {
    public static String BASE_SELLER_URL = "https://seller.tokopedia.com/";
    public static String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String BASE_MOBILE_DOMAIN = "https://m.tokopedia.com/";

    public static String CDN_URL = "https://ecs7.tokopedia.net";
    public static String CDN_IMAGE_PATH = "/img/android/others/";
    public static String IMAGE_URL = CDN_URL + CDN_IMAGE_PATH;

    public static class ImageUrl {

        public static final String KEY_IMAGE_HOST = "image_host";

        public static final String TOKOCASH_IMG = "/img/wallet/ic_tokocash_circle.png";
        public static final String SALDO_IMG = "/img/wallet/ic_saldo_circle.png";
    }

    public static String MORE_SELLER = BASE_SELLER_URL + "mulai-berjualan/";
    public static String GOLD_MERCHANT = BASE_SELLER_URL + "gold-merchant/";

    public static class Pulsa {

        public static String BASE_PULSA_URL = "https://pulsa.tokopedia.com/";

        public static String PULSA_SUBSCRIBE = BASE_PULSA_URL + "subscribe/";

        public static String PULSA_FAV_NUMBER = BASE_PULSA_URL + "favorite-list/";

        public static String ZAKAT_URL = BASE_PULSA_URL + "berbagi/operator/?category_id=16";

        public static String MYBILLS = BASE_PULSA_URL + "mybills/";
    }

    public static String REKSA_DANA_URL = WEB_DOMAIN + "reksa-dana/";
    public static String REKSA_DANA_TX_URL = WEB_DOMAIN + "reksa-dana/mobile-dashboard/list";

    public static String TOKOCARD_URL = "tokoswipe/";

    public static String EMAS_URL = WEB_DOMAIN + "emas/";
    public static String EMAS_TX_URL = WEB_DOMAIN + "emas/daftar-transaksi/";

    public static String BASE_MOBILE_URL = TkpdBaseURL.MOBILE_DOMAIN;
    public final static String URL_TOKOPEDIA_CORNER = BASE_MOBILE_URL + "tokopedia-corner";
    public final static String APPLINK_TOKOPEDIA_CORNER = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TOKOPEDIA_CORNER);
  
    public static String GIFT_CARD_URL = BASE_MOBILE_DOMAIN + "order-list?tab=GIFTCARDS";
}