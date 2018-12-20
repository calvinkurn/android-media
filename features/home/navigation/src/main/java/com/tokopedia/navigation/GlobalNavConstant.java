package com.tokopedia.navigation;

/**
 * Created by meta on 25/07/18.
 */
public class GlobalNavConstant {

    public static final String EXTRA_APPLINK_FROM_PUSH = "applink_from_notif";

    public static final String FROM_APP_SHORTCUTS = "FROM_APP_SHORTCUTS" ;

    public class Analytics {

        static final String CLICK_HOMEPAGE = "clickHomePage";

        static final String HOME_PAGE = "homepage";

        public static final String BOTTOM = "bottom";

        static final String NAV = "nav";

        static final String CLICK = "click";

        static final String PAGE = "page";

        static final String INBOX = "inbox";
        static final String NOTIFICATION = "notification";

        static final String IMPRESSION = "Impression";

        static final String CATEGORY_APP_UPDATE = "Application Update";
        static final String EVENT_IMPRESSION_APP_UPDATE = "impressionAppUpdate";
        static final String EVENT_CLICK_APP_UPDATE = "clickAppUpdate";
        static final String EVENT_CLICK_CANCEL_APP_UPDATE = "clickCancelAppUpdate";

        static final String LABEL_OPTIONAL_APP_UPDATE = "Clicked Update - Optional";
        static final String LABEL_FORCE_APP_UPDATE = "Clicked Update - Force";
        static final String LABEL_OPTIONAL_CANCEL_APP_UPDATE = "Clicked Nanti";
        static final String LABEL_FORCE_CANCEL_APP_UPDATE = "Clicked Tutup";

        public static final String SCREEN_NAME = "screenName";
        public static final String EVENT = "event";
        public static final String EVENT_CATEGORY = "eventCategory";
        public static final String EVENT_ACTION = "eventAction";
        public static final String EVENT_LABEL = "eventLabel";
        public static final String CLICK_HOME_PAGE = "clickHomePage";
        public static final String TOP_NAV = "top nav";
        public static final String SCREEN_NAME_CHAT = "/chat";

    }

    public class Cache {
        public static final String KEY_FIRST_TIME = "FIRST_TIME";
        public static final String KEY_IS_FIRST_TIME = "is_first_time";
    }

    public static final String QUERY = "Query";

    public static final int SELLER_INFO = 13;
    public static final int PEMBELIAN = 0;
    public static final int PENJUALAN = 1;
    public static final int KOMPLAIN = 2;
    public static final int UPDATE = 3;


    public static final int MENUNGGU_PEMBAYARAN = 4;
    public static final int MENUNGGU_KONFIRMASI = 5;
    public static final int PESANAN_DIPROSES = 6;

    public static final int PESANAN_BARU = 7;
    public static final int SIAP_DIKIRIM = 8;

    public static final int SEDANG_DIKIRIM = 9;
    public static final int SAMPAI_TUJUAN = 10;

    public static final int BUYER = 11;
    public static final int SELLER = 12;

    public static final int BUYER_INFO = 14;

    public static final int NEWEST_INFO = 15;

}
