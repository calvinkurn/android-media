package com.tokopedia.home.account;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.config.url.TokopediaUrl;

/**
 * @author okasurya on 9/14/18.
 */
public class AccountHomeUrl {

    public static String BASE_SELLER_URL = TokopediaUrl.Companion.getInstance().getSELLER();
    public static String WEB_DOMAIN = TokopediaUrl.Companion.getInstance().getWEB();

    public static String CDN_URL = "https://ecs7.tokopedia.net";
    public static String CDN_IMAGE_PATH = "/img/android/others/";
    public static String IMAGE_URL = CDN_URL + CDN_IMAGE_PATH;

    public static class ImageUrl {

        public static final String KEY_IMAGE_HOST = "image_host";

        public static final String OVO_IMG = "/img/android/ovo/drawable-xxxhdpi/ovo.png";
        public static final String TOKOCASH_IMG = "/img/wallet/ic_tokocash_circle.png";
        public static final String SALDO_IMG = "/img/android/saldo_tokopedia/drawable-xxxhdpi/saldo_tokopedia.png";

        public static final String EMPTY_SELLER_IMG = "/img/android/seller_dashboard/seller_dashboard.png";
    }

    public static String EDU_MORE_SELLER = BASE_SELLER_URL + "edu/mulai-berjualan/";

    public static class Pulsa {

        public static String BASE_PULSA_URL = TokopediaUrl.Companion.getInstance().getPULSA();

        public static String MYBILLS = BASE_PULSA_URL + "mybills/";
    }

    public static String REKSA_DANA_TX_URL = WEB_DOMAIN + "reksa-dana/mobile-dashboard/list";

    public static String TOKOCARD_URL = "tokoswipe/";

    public static String EMAS_TX_URL = WEB_DOMAIN + "emas/daftar-transaksi/";

    public static String BASE_MOBILE_URL = TokopediaUrl.Companion.getInstance().getMOBILEWEB();
    public final static String URL_TOKOPEDIA_CORNER = BASE_MOBILE_URL + "tokopedia-corner";
    public final static String APPLINK_TOKOPEDIA_CORNER = String.format("%s?url=%s", ApplinkConst.WEBVIEW, URL_TOKOPEDIA_CORNER);
}