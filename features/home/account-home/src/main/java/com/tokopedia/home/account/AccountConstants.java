package com.tokopedia.home.account;

import com.tokopedia.network.constant.TkpdBaseURL;

/**
 * @author okasurya on 7/20/18.
 */
public class AccountConstants {
    public static final String QUERY = "query";
    public static final String VARIABLES = "variables";
    public static final String KEY_SEE_ALL = "lihat_semua";
    public static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";

    public static class Url {

        public static final String BASE_SELLER_URL = "https://seller.tokopedia.com/";

        public static final String IMAGE_URL = "https://ecs7.tokopedia.net/img/android/others/";

        public static final String MORE_SELLER = BASE_SELLER_URL + "mulai-berjualan/";

        public class Pulsa {

            public static final String BASE_PULSA_URL = "https://pulsa.tokopedia.com/";

            public static final String PULSA_SUBSCRIBE = BASE_PULSA_URL + "subscribe/";

            public static final String PULSA_FAV_NUMBER = BASE_PULSA_URL + "favorite-list/";

            public static final String ZAKAT_URL = BASE_PULSA_URL + "berbagi/operator/?category_id=16";
        }

        public static final String REKSA_DANA_URL = TkpdBaseURL.WEB_DOMAIN + "reksa-dana/";
        public static final String EMAS_URL = TkpdBaseURL.WEB_DOMAIN + "emas/";

    }
}
