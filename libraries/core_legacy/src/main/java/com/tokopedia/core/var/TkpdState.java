package com.tokopedia.core.var;

public class TkpdState extends com.tokopedia.abstraction.constant.TkpdState  {
    public static class InboxResCenter {
        public static int RESO_ALL = 2;
        public static int RESO_MINE = 0;
        public static int RESO_BUYER = 1;
    }

    public class GCMServiceState {
        public static final int GCM_MESSAGE = 101;
        public static final int GCM_TALK = 102;
        public static final int GCM_REVIEW = 103;
        public static final int GCM_TICKET = 104;
        public static final int GCM_RES_CENTER = 105;
        public static final int GCM_REVIEW_EDIT = 113;
        public static final int GCM_REVIEW_REPLY = 123;
        public static final int GCM_REPUTATION_SMILEY = 202;
        public static final int GCM_REPUTATION_EDIT_SMILEY = 212;
        public static final int GCM_REPUTATION_SMILEY_TO_SELLER = 222;
        public static final int GCM_REPUTATION_EDIT_SMILEY_TO_SELLER = 232;
        public static final int GCM_REPUTATION_SMILEY_TO_BUYER = 242;
        public static final int GCM_REPUTATION_EDIT_SMILEY_TO_BUYER = 252;
        public static final int GCM_PURCHASE_VERIFIED = 301;
        public static final int GCM_PURCHASE_ACCEPTED = 302;
        public static final int GCM_PURCHASE_PARTIAL_PROCESSED = 303;
        public static final int GCM_PURCHASE_REJECTED = 304;
        public static final int GCM_PURCHASE_DELIVERED = 305;
        public static final int GCM_PURCHASE_DISPUTE = 306;
        public static final int GCM_PURCHASE_CONFIRM_SHIPPING = 307;
        public static final int GCM_PURCHASE_FINISH = 308;
        public static final int GCM_PURCHASE_FINISH_REMINDER = 309;
        public static final int GCM_PURCHASE_NEW_ORDER = 310;
        public static final int GCM_PURCHASE_AUTO_CANCEL_2D = 311;
        public static final int GCM_PURCHASE_REJECTED_SHIPPING = 312;
        public static final int GCM_PURCHASE_AUTO_CANCEL_4D = 313;
        public static final int GCM_NEWORDER = 401;
        public static final int GCM_ORDER_INVALID_RESI = 402;
        public static final int GCM_ORDER_FINISH_SELLER = 403;
        public static final int GCM_ORDER_CANCEL_2D_SELLER = 404;
        public static final int GCM_ORDER_CANCEL_4D_SELLER = 405;
        public static final int GCM_ORDER_DELIVERED_SELLER = 406;
        public static final int GCM_DRAWER_UPDATE = 501;
        public static final int GCM_NOTIF_UPDATE = 502;
        public static final int GCM_CART_UPDATE = 503;
        public static final int GCM_PEOPLE_PROFILE = 601;
        public static final int GCM_PEOPLE_NOTIF_SETTING = 602;
        public static final int GCM_PEOPLE_PRIVACY_SETTING = 603;
        public static final int GCM_PEOPLE_ADDRESS_SETTING = 604;
        public static final int GCM_SHOP_INFO = 701;
        public static final int GCM_SHOP_PAYMENT = 702;
        public static final int GCM_SHOP_ETALASE = 703;
        public static final int GCM_SHOP_NOTES = 704;
        public static final int GCM_PRODUCT_LIST = 801;
        public static final int GCM_UPDATE_NOTIFICATION = 901;
        // Notes : Rsoluction Center code 1x5
        public static final int GCM_RESCENTER_SELLER_REPLY = 115;
        public static final int GCM_RESCENTER_BUYER_REPLY = 125;
        public static final int GCM_RESCENTER_SELLER_AGREE = 135;
        public static final int GCM_RESCENTER_BUYER_AGREE = 145;
        public static final int GCM_RESCENTER_ADMIN_SELLER_REPLY = 155;
        public static final int GCM_RESCENTER_ADMIN_BUYER_REPLY = 165;
        public static final int GCM_GENERAL = 1000;
        public static final int GCM_PROMO = 1001;
        public static final int GCM_HOT_LIST = 1002;
        public static final int GCM_DEEPLINK = 1003;
        public static final int GCM_CART = 1004;
        public static final int GCM_CATEGORY = 1005;
        public static final int GCM_SHOP = 1006;
        public static final int GCM_WISHLIST = 1007;
        public static final int GCM_VERIFICATION = 1008;
        public static final int GCM_TOPADS_BELOW_20K = 1100;
        public static final int GCM_TOPADS_TOPUP_SUCCESS = 1101;
        public static final int GCM_RIDEHAILING = 1200;

        //SELLER INFO
        public static final int GCM_SELLER_INFO = 1300;

        //GROUP CHAT
        public static final int GCM_GROUP_CHAT = 1400;
        public static final int GCM_GROUP_CHAT_POINTS = 1401;
        public static final int GCM_GROUP_CHAT_LOYALTY = 1402;
        public static final int GCM_GROUP_CHAT_COUPON = 1403;
    }
}
