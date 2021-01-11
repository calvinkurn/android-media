package com.tokopedia.pushnotif.data.constant;

/**
 * @author ricoharisin .
 */

public interface Constant {

    String EXTRA_APPLINK_FROM_PUSH = "applink_from_notif";
    String EXTRA_NOTIFICATION_TYPE = "notification_type";
    String EXTRA_NOTIFICATION_ID = "notification_id";
    String CACHE_DELAY = "cache_delay";
    String PREV_TIME = "prev_time";

     interface NotificationGroup {
         String TALK = "com.tokopedia.tkpd.TALK";
         String TOPCHAT = "com.tokopedia.tkpd.TOPCHAT";
         String TRANSACTION = "com.tokopedia.tkpd.TRANSACTION";
         String NEW_ORDER = "com.tokopedia.tkpd.NEWORDER";
         String RESOLUTION = "com.tokopedia.tkpd.RESOLUTION";
         String REVIEW = "com.tokopedia.tkpd.REVIEW";
         String GENERAL = "com.tokopedia.tkpd.GENERAL";
    }

    interface NotificationChannel {
        String GENERAL = "ANDROID_GENERAL_CHANNEL";
    }

    interface NotificationId {
        int GENERAL = 100;
        int TALK = 200;
        int CHAT = 300;
        int TRANSACTION = 400;
        int SELLER = 500;
        int RESOLUTION = 600;
        int GROUPCHAT = 700;
        int REVIEW = 900;
        int CHALLENGES = 2018;
        int CHAT_BOT = 800;
    }

    interface Settings {
        String NOTIFICATION_VIBRATE = "notifications_new_message_vibrate";
        String NOTIFICATION_PROMO = "notification_receive_promo";
        String NOTIFICATION_PM = "notification_receive_pm";
        String NOTIFICATION_TALK = "notification_receive_talk";
        String NOTIFICATION_REVIEW = "notification_receive_review";
        String NOTIFICATION_REP = "notification_receive_reputation";
        String NOTIFICATION_SALES = "notification_sales";
        String NOTIFICATION_PURCHASE = "notification_purchase";
        String NOTIFICATION_RESCENTER = "notification_receive_rescenter";
        String NOTIFICATION_SELLER_INFO = "notification_seller_info";
    }

    interface GCMServiceState {
         int GCM_CHAT = 111;
         int GCM_MESSAGE = 101;
         int GCM_TALK = 102;
         int GCM_REVIEW = 103;
         int GCM_TICKET = 104;
         int GCM_RES_CENTER = 105;
         int GCM_REVIEW_EDIT = 113;
         int GCM_REVIEW_REPLY = 123;
         int GCM_REPUTATION_SMILEY = 202;
         int GCM_REPUTATION_EDIT_SMILEY = 212;
         int GCM_REPUTATION_SMILEY_TO_SELLER = 222;
         int GCM_REPUTATION_EDIT_SMILEY_TO_SELLER = 232;
         int GCM_REPUTATION_SMILEY_TO_BUYER = 242;
         int GCM_REPUTATION_EDIT_SMILEY_TO_BUYER = 252;
         int GCM_PURCHASE_VERIFIED = 301;
         int GCM_PURCHASE_ACCEPTED = 302;
         int GCM_PURCHASE_PARTIAL_PROCESSED = 303;
         int GCM_PURCHASE_REJECTED = 304;
         int GCM_PURCHASE_DELIVERED = 305;
         int GCM_PURCHASE_DISPUTE = 306;
         int GCM_PURCHASE_CONFIRM_SHIPPING = 307;
         int GCM_PURCHASE_FINISH = 308;
         int GCM_PURCHASE_FINISH_REMINDER = 309;
         int GCM_PURCHASE_NEW_ORDER = 310;
         int GCM_PURCHASE_AUTO_CANCEL_2D = 311;
         int GCM_PURCHASE_REJECTED_SHIPPING = 312;
         int GCM_PURCHASE_AUTO_CANCEL_4D = 313;
         int GCM_NEWORDER = 401;
         int GCM_ORDER_INVALID_RESI = 402;
         int GCM_ORDER_FINISH_SELLER = 403;
         int GCM_ORDER_CANCEL_2D_SELLER = 404;
         int GCM_ORDER_CANCEL_4D_SELLER = 405;
         int GCM_ORDER_DELIVERED_SELLER = 406;
         int GCM_DRAWER_UPDATE = 501;
         int GCM_NOTIF_UPDATE = 502;
         int GCM_CART_UPDATE = 503;
         int GCM_PEOPLE_PROFILE = 601;
         int GCM_PEOPLE_NOTIF_SETTING = 602;
         int GCM_PEOPLE_PRIVACY_SETTING = 603;
         int GCM_PEOPLE_ADDRESS_SETTING = 604;
         int GCM_SHOP_INFO = 701;
         int GCM_SHOP_PAYMENT = 702;
         int GCM_SHOP_ETALASE = 703;
         int GCM_SHOP_NOTES = 704;
         int GCM_PRODUCT_LIST = 801;
         int GCM_UPDATE_NOTIFICATION = 901;
        // Notes : Rsoluction Center code 1x5
         int GCM_RESCENTER_SELLER_REPLY = 115;
         int GCM_RESCENTER_BUYER_REPLY = 125;
         int GCM_RESCENTER_SELLER_AGREE = 135;
         int GCM_RESCENTER_BUYER_AGREE = 145;
         int GCM_RESCENTER_ADMIN_SELLER_REPLY = 155;
         int GCM_RESCENTER_ADMIN_BUYER_REPLY = 165;
         int GCM_GENERAL = 1000;
         int GCM_PROMO = 1001;
         int GCM_HOT_LIST = 1002;
         int GCM_DEEPLINK = 1003;
         int GCM_CART = 1004;
         int GCM_CATEGORY = 1005;
         int GCM_SHOP = 1006;
         int GCM_WISHLIST = 1007;
         int GCM_VERIFICATION = 1008;
         int GCM_TOPADS_BELOW_20K = 1100;
         int GCM_TOPADS_TOPUP_SUCCESS = 1101;
         int GCM_RIDEHAILING = 1200;

        //SELLER INFO
         int GCM_SELLER_INFO = 1300;
    }

    interface Host{
         String CHATBOT = "chatbot";
    }

    interface IntentFilter {
         String GET_CHAT_SELLER_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.GET_CHAT_APP_WIDGET_DATA";
         String GET_ORDER_SELLER_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.GET_ORDER_APP_WIDGET_DATA";
    }
}
