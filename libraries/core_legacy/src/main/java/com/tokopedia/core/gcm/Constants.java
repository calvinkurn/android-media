package com.tokopedia.core.gcm;

/**
 * @author by alvarisi on 12/20/16.
 *         this class contain all string used for applink/PN case
 */

@Deprecated
public interface Constants {
    String ARG_NOTIFICATION_CODE = "tkp_code";
    String ARG_NOTIFICATION_DESCRIPTION = "desc";
    String ARG_NOTIFICATION_APPLINK = "applinks";
    String ARG_NOTIFICATION_APPLINK_PROMO_LABEL = "promo";
    String KEY_ORIGIN = "origin";
    int REGISTRATION_STATUS_OK = 1;
    int REGISTRATION_STATUS_ERROR = 2;
    String REGISTRATION_MESSAGE_OK = "FCM Sucessfully";
    String REGISTRATION_MESSAGE_ERROR = "FCM Error";
    String EXTRA_APPLINK = "applink_url";
    String EXTRA_APPLINK_FROM_PUSH = "applink_from_notif";
    String EXTRA_PUSH_PERSONALIZATION = "EXTRA_PUSH_PERSONALIZATION";
    String EXTRA_APPLINK_CATEGORY = "applink_category";
    String EXTRA_APPLINK_FROM_INTERNAL = "EXTRA_APPLINK_FROM_INTERNAL";

    String ACTION_BC_RESET_APPLINK = "com.tokopedia.tkpd.APPLINK_ACTION";


    /**
     * @deprecated extends {@link com.tokopedia.abstraction.constant.TkpdAppLink} on module instead
     */
    @Deprecated
    interface Applinks {
        interface SellerApp {
            String SALES = "sellerapp://sales";
            String TOPADS_CREDIT = "sellerapp://topads/buy";
            String TOPADS_PRODUCT_CREATE = "sellerapp://topads/create";
            String GOLD_MERCHANT = "sellerapp://gold";
            String SELLER_APP_HOME = "sellerapp://home";
            String TOPADS_DASHBOARD = "sellerapp://topads";
            String TOPADS_PRODUCT_DETAIL = "sellerapp://topads/product/{ad_id}";
            String TOPADS_PRODUCT_DETAIL_CONSTS = "sellerapp://topads/product";
            String BROWSER = "sellerapp://browser";
        }
    }

    interface Schemes {
        String HTTP = "http";
        String HTTPS = HTTP + "s";
        String APPLINKS = "tokopedia";
        String APPLINKS_SELLER = "sellerapp";
    }

    //NOTE: strings must be same with {@link pref_notification.xml}
    interface Settings {
        String NOTIFICATION_PROMO = "notification_receive_promo";
    }
}