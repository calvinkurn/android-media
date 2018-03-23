package com.tokopedia.core.var;

import com.tokopedia.core.R;


public class TkpdState extends com.tokopedia.abstraction.constant.TkpdState {

    public static class ResCenterSolution {
        public static final int SOLUTION_REFUND = 1;
        public static final int SOLUTION_RETUR = 2;
        public static final int SOLUTION_RETUR_REFUND = 3;
        public static final int SOLUTION_SELLER_WIN = 4;
        public static final int SOLUTION_SEND_REMAINING = 5;
        private static final int SOLUTION_CHECK_COURIER_AGENT = 6;

        public static int getStringRes(int state) {
            switch (state) {
                case SOLUTION_RETUR:
                    return R.string.msg_res_retur;
                case SOLUTION_REFUND:
                    return R.string.msg_res_refund;
                case SOLUTION_RETUR_REFUND:
                    return R.string.msg_res_retur_refund;
                case SOLUTION_SEND_REMAINING:
                    return R.string.msg_res_remaining;
                case SOLUTION_CHECK_COURIER_AGENT:
                    return R.string.msg_res_check_courier_agent;
                default:
                    return R.string.msg_res_refund;
            }
        }
    }

    public static class ResCenterTrouble {
        public static final int TROUBLE_DIFF_DESCRIPTION = 1;
        public static final int TROUBLE_BROKEN = 2;
        public static final int TROUBLE_DIFF_QUANTITY = 3;
        public static final int TROUBLE_DIFF_CARRIER = 4;
        public static final int TROUBLE_PRODUCT_NOT_RECEIVED = 5;

        public static int getStringRes(int state) {
            switch (state) {
                case TROUBLE_DIFF_DESCRIPTION:
                    return R.string.rescenter_trouble_diff_desc;
                case TROUBLE_BROKEN:
                    return R.string.rescenter_trouble_broken;
                case TROUBLE_DIFF_QUANTITY:
                    return R.string.rescenter_trouble_diff_quantity;
                case TROUBLE_DIFF_CARRIER:
                    return R.string.rescenter_trouble_diff_carrier;
                case TROUBLE_PRODUCT_NOT_RECEIVED:
                    return R.string.rescenter_trouble_not_received;
                default:
                    return R.string.rescenter_trouble_undefined;
            }
        }

    }

    public static class ResCenterActionBy {
        public static final int BUYER = 1;
        public static final int SELLER = 2;
        public static final int ADMIN = 3;

        public static int getStringRes(int state) {
            switch (state) {
                case BUYER:
                    return R.string.title_buyer;
                case SELLER:
                    return R.string.title_seller;
                case ADMIN:
                    return R.string.title_admin;
                default:
                    return 0;
            }
        }
    }

    public static class ResCenterAct {
        public static final int ACCEPT = 1;
        public static final int INPUT = 2;
        public static final int FINISH = 3;
        public static final int ACCEPT_ADMIN = 4;
        public static final int APPEAL = 5;

        public static int getStringRes(int state) {
            switch (state) {
                case ACCEPT:
                    return R.string.msg_accept_sol;
                case FINISH:
                    return R.string.msg_rescen_finish;
                case ACCEPT_ADMIN:
                    return R.string.msg_accept_admin;
                case APPEAL:
                    return R.string.msg_appeal_sol;
                default:
                    return 0;
            }
        }
    }

    public static class ResCenterStatus {
        public static final int RESOLUTION_CANCELED = 0;
        public static final int RESOLUTION_OPEN = 100;
        public static final int RESOLUTION_DO_ACTION = 200;
        public static final int RESOLUTION_CS_ANSWERED = 300;
        public static final int RESOLUTION_APPEAL = 400;
        public static final int RESOLUTION_FINISHED = 500;

        public static int getStringRes(int state) {
            switch (state) {
                case RESOLUTION_CANCELED:
                    return R.string.title_rescenter_canceled;
                case RESOLUTION_OPEN:
                    return R.string.title_rescenter_open;
                case RESOLUTION_DO_ACTION:
                    return R.string.title_rescenter_action;
                case RESOLUTION_CS_ANSWERED:
                    return R.string.title_rescenter_solution;
                case RESOLUTION_APPEAL:
                    return R.string.title_rescenter_appeal;
                case RESOLUTION_FINISHED:
                    return R.string.title_rescenter_canceled;
                default:
                    return 0;
            }
        }

        public static int getColorStatus(int state) {
            switch (state) {
                case RESOLUTION_FINISHED:
                    return R.color.tkpd_dark_gray;
                case RESOLUTION_CANCELED:
                    return R.color.tkpd_dark_gray;
                default:
                    return R.color.tkpd_dark_orange;
            }
        }

    }

    public static class InboxResCenter {
        public static int RESO_ALL = 2;
        public static int RESO_MINE = 0;
        public static int RESO_BUYER = 1;
    }

    public static class StateView {
        public static final int GRID_3 = 0;
        public static final int GRID_2 = 1;
        public static final int LIST = 2;
    }

    public class RequestCode {
        public static final int CODE_OPEN_DETAIL_REPUTATION = 100;
        public static final int CODE_OPEN_DETAIL_PRODUCT_REVIEW = 1;
        public static final int CODE_OPEN_FORM_PRODUCT_REVIEW = 2;
    }

    public class TroubleTicket {
        public static final int TICKET_CLOSED = 2;
        public static final int TICKET_OPEN = 1;
        public static final int TICKET_RATING_BAD = 2;
        public static final int TICKET_RATING_GOOD = 1;
        public static final int TICKET_RATING_NEUTRAL = 0;
        public static final int TICKET_VIEW_REPLYABLE = 1;
        public static final int TICKET_VIEW_REPLYABLE_WITH_PROMPT = 2;
        public static final int TICKET_VIEW_NOT_REPLYABLE = 3;
        public static final int TICKET_VIEW_NOT_REPLYABLE_WITH_OPTION = 4;
    }

    public class DrawerPosition {
        public static final int NO_ACCESS = 0;
        public static final int INBOX_MESSAGE = 1;
        public static final int INBOX_REVIEW = 2;
        public static final int INBOX_TALK = 3;
        public static final int INBOX_TICKET = 4;
        public static final int INDEX_HOME = 5;
        public static final int LOGIN = 6;
        public static final int MANAGE_PEOPLE = 7;
        public static final int MANAGE_PRODUCT = 8;
        public static final int ADD_PRODUCT = 1001;
        public static final int MANAGE_PAYMENT_AND_TOPUP = 55;
        public static final int MANAGE_TRANSACTION_DIGITAL = 56;
        public static final int MANAGE_PRICE_PRODUCT_DIGITAL = 57;
        public static final int DRAFT_PRODUCT = 22;
        public static final int MANAGE_SHOP = 9;
        public static final int PEOPLE = 10;
        public static final int PEOPLE_DEPOSIT = 11;
        public static final int PEOPLE_TRANSACTION = 12;
        public static final int SHOP_TRANSACTION = 13;
        public static final int CREATE_SHOP = 14;
        public static final int SHOP_INFO = 15;
        public static final int GENERAL_SETTING = 16;
        public static final int RESOLUTION_CENTER = 18;
        public static final int SELLER_INFO = 193;
		public static final int REGISTER = 19;
        public static final int DEVELOPER_OPTIONS = 20;
        public static final int MANAGE_ETALASE = 21;
        public static final int PEOPLE_PAYMENT_STATUS = 201;
        public static final int PEOPLE_ORDER_STATUS = 202;
        public static final int PEOPLE_CONFIRM_SHIPPING = 203;
        public static final int PEOPLE_TRANSACTION_LIST = 204;
        public static final int PEOPLE_TRANSACTION_CANCELED = 205;
        public static final int PEOPLE_SHOPPING_LIST = 206;
        public static final int PEOPLE_DIGITAL_TRANSACTION_LIST = 207;
        public static final int PEOPLE_FLIGHT_TRANSACTION_LIST = 208;

        public static final int SHOP_NEW_ORDER = 301;
        public static final int SHOP_CONFIRM_SHIPPING = 302;
        public static final int SHOP_SHIPPING_STATUS = 303;
        public static final int SHOP_TRANSACTION_LIST = 304;
        public static final int SHOP = 300;

        public static final int INBOX = 31;
        public static final int WISHLIST = 32;
        public static final int SECURITY_QUESTION = 33;
        public static final int REGISTER_NEXT = 34;
        public static final int ACTIVATION_RESENT = 35;
        public static final int FORGOT_PASSWORD = 36;
        public static final int HOTLIST = 37;
        public static final int HOME = 38;
        public static final int SETTINGS = 39;
        public static final int REGISTER_THIRD = 40;
        public static final int CONTACT_US = 41;
        public static final int REPORT = 42;
        public static final int LOGOUT = 43;
        public static final int SELLER_INDEX_HOME = TkpdState.DrawerPosition.INDEX_HOME;
        public static final int REGISTER_INITIAL = 45;
        public static final int SELLER_GM_SUBSCRIBE = 46;
        public static final int SELLER_GM_SUBSCRIBE_EXTEND = 47;
        public static final int SELLER_PRODUCT_EXTEND = 53;
        public static final int SELLER_PRODUCT_DIGITAL_EXTEND = 54;
        public static final int SELLER_TOP_ADS = 48;
        public static final int SELLER_GM_STAT = 49;
        public static final int SELLER_MITRA_TOPPERS = 101;
        public static final int GOLD_MERCHANT = 50;
        public static final int HELP = 51;
        public static final int SHOP_OPPORTUNITY_LIST = 52;
        public static final int CATEGORY_NAVIGATION = 54;
        public static final int RESOLUTION_CENTER_BUYER = 55;
        public static final int RESOLUTION_CENTER_SELLER = 56;

        public static final int FEATURED_PRODUCT = 99;

        public static final int APPSHARE = 58;

    }

    public class Application {
        public static final int ACTIVITY = 1;
        public static final int FRAGMENT_ACTIVITY = 2;
    }

    public class NetworkState {
        public static final int NO_CONNECTION = 1;
        public static final int TIMEOUT = 2;
        public static final int SERVER_ERROR = 3;
    }

    /**
     * @author EkaCipta
     *         Tidak bisa passing intent code melalui fragment ke activity, jadi menggunakan result untuk passing code
     */
    public class TxActivityCode {
        public static final int PaymentConfirmationSuccess = 101;
        public static final int BuyerItemReceived = 102;
        public static final int BuyerCreateResolution = 103;
    }

    /**
     * @author EkaCipta
     *         Nama package untuk program yang dipanggil
     */
    public class PackageName {
        public static final String BlackBerry = "com.bbm";
        public static final String Twitter = "com.twitter.android";
        public static final String Instagram = "com.instagram.android";
        public static final String Facebook = "com.facebook.katana";
        public static final String Wechat = "con.tencent.mm";
        public static final String Whatsapp = "com.whatsapp";
        public static final String Pinterest = "com.pinterest";
        public static final String Gplus = "com.google.android.apps.plus";
        public static final String Line = "jp.naver.line.android";
        public static final String TYPE_IMAGE = "image/jpeg";
        public static final String TYPE_IMAGE_2 = "image/*";
        public static final String TYPE_TEXT = "text/plain";
        /**
         * Format for twitter : "http://www.twitter.com/intent/tweet?url=YOURURL&text=YOURTEXT"
         */
        public static final String TWITTER_DEFAULT = "http://www.twitter.com/intent/tweet?";
    }

    public class OrderStatusState {
        public static final int ORDER_WAITING_STATUS_FROM_SHIPPING_AGENCY = 501;
        public static final int ORDER_SHIPPING = 500;
        public static final int ORDER_SHIPPING_TRACKER_INVALID = 520;
        public static final int ORDER_SHIPPING_REF_NUM_EDITED = 530;
        public static final int ORDER_DELIVERED = 600;
        public static final int ORDER_DELIVERY_FAILURE = 630;
        public static final int ORDER_CONFLICTED = 601;
        public static final int ORDER_OPPORTUNITY = 11;
    }

    public class BrowseProdState {
        public static final int CATALOG = 1;
        public static final int PRODUCT = 2;
    }

    public class ResCenterCase {
        public static final int ACCEPT = 1;
        public static final int INPUT = 2;
        public static final int INPUT_FINISH = 3;
        public static final int ACCEPT_ADMIN_APPEAL = 4;
        public static final int APPEAL = 5;
        public static final int FINISH = 6;

    }

    public class TrackerState {
        public static final int EVENT_REGISTER = 101;
        public static final int EVENT_ATC = 102;
        public static final int EVENT_CHECKOUT = 103;
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
    }

    public class RecyclerView {
        public static final int VIEW_LOADING = 1;
        public static final int VIEW_HISTORY = 2;
        public static final int VIEW_PRODUCT = 3;
        public static final int VIEW_PRODUCT_GRID_1 = 12;
        public static final int VIEW_PRODUCT_GRID_2 = 13;
        public static final int VIEW_TOP_ADS = 4;
        public static final int VIEW_WISHLIST = 5;
        public static final int VIEW_REC_SHOP = 6;
        public static final int VIEW_FAV_SHOP = 7;
        public static final int VIEW_PRODUCT_RIGHT = 8;
        public static final int VIEW_EMPTY_SEARCH = 9;
        public static final int VIEW_EMPTY_STATE = 10;
        public static final int VIEW_CATEGORY = 11;
        public static final int VIEW_BANNER_HOT_LIST = 998;
        public static final int VIEW_TOP_ADS_LIST = 997;
        public static final int VIEW_CATEGORY_HEADER = 996;
        public static final int VIEW_CATEGORY_REVAMP_HEADER = 995;
        public static final int VIEW_BANNER_OFFICIAL_STORE = 994;

        // hotlist view type
        public static final int VIEW_STANDARD = 9;

        // instoped view type
        public static final int VIEW_INSTOPED = 11;


        public static final int VIEW_FIRST_TIME_USER = 123;
        public static final int VIEW_EMPTY = 0;
        public static final int VIEW_RETRY = 101;
        public static final int VIEW_ERROR_NETWORK = 102;
        public static final int VIEW_UNKNOWN = 45679726;

    }

    public class RecyclerViewItem {
        public static final int TYPE_ITEM = 1;
        public static final int TYPE_LIST = 2;
        public static final int TYPE_EMPTY = 3;

        public static final int TYPE_FOOTER_DETAIL_RES_CENTER = 0x113;
        public static final int TYPE_CONVERSATION_DETAIL_RES_CENTER = 0x112;
        public static final int TYPE_HEADER_DETAIL_RES_CENTER = 0x111;
    }


    public class CacheName {
        public static final String CACHE_MAIN = "CACHE_MAIN";
        public static final String CACHE_USER = "USER_INFO";
        public static final String CACHE_ONBOARDING = "CACHE_ONBOARDING";
    }

    public class CacheKeys {
        public static final String RECENT_PRODUCT = "RECENT_PRODUCT";
        public static final String PRODUCT_FEED = "PRODUCT_FEED";
        public static final String WISHLIST = "WISHLIST";
        public static final String FAVORITE_SHOP = "FAVORITE_SHOP";
        public static final String RECOMMEND_SHOP = "RECOMMEND_SHOP";
        public static final String HOTLIST = "HOTLIST";

        public static final String INBOX_MESSAGE = "INBOX_MESSAGE";
        public static final String INBOX_ARCHIVE = "INBOX_ARCHIVE";
        public static final String INBOX_SENT = "INBOX_SENT";
        public static final String INBOX_TRASH = "INBOX_TRASH";

    }

    public class DrawerItem {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_LIST = 1;
        public static final int TYPE_ITEM = 2;
        public static final int TYPE_SEPARATOR = 3;
    }

    public class SHIPPING_ID {
        public static final String WAHANA = "6";
        public static final String GOJEK = "10";
    }

    public class Gender {
        public static final String MALE = "1";
        public static final String FEMALE = "2";
    }

    public class Geolocation {
        public static final String defaultLatitude = "-6.1753924";
        public static final String defaultLongitude = "106.8249641";
    }

    public class ProductService {
        /* BROADCAST INTENT FILTER */
        public static final String BROADCAST_ADD_PRODUCT = "BROADCAST_ADD_PRODUCT";

        /* TYPE */
        public static final String SERVICE_TYPE = "SERVICE_TYPE";
        public static final int ADD_PRODUCT = 10;
        public static final int ADD_PRODUCT_WITHOUT_IMAGE = 11;
        public static final int EDIT_PRODUCT = 12;
        public static final int DELETE_PRODUCT = 13;
        public static final int INVALID_TYPE = -1;


        /* STATUS */
        public static final String STATUS_FLAG = "STATUS_FLAG";
        public static final int STATUS_RUNNING = 1;
        public static final int STATUS_DONE = 2;
        public static final int STATUS_ERROR = 3;

        /* DATA */
        public static final String PRODUCT_DB_ID = "PRODUCT_DB_ID";
        public static final long NO_PRODUCT_DB = -1;
        public static final String PRODUCT_POS = "PRODUCT_POS";
        public static final int NO_PRODUCT_POS = -1;
        public static final String PRODUCT_ID = "PRODUCT_ID";
        public static final int NO_PRODUCT_ID = -1;
        public static final String MESSAGE_ERROR_FLAG = "MESSAGE_ERROR_FLAG";
        public static final String INVALID_MESSAGE_ERROR = "default";


        public static final String PRODUCT_NAME = "PRODUCT_NAME";
        public static final String IMAGE_URI = "IMAGE_URI";
        public static final String PRODUCT_DESCRIPTION = "PRODUCT_DESCRIPTION";
        public static final String PRODUCT_URI = "PRODUCT_URI";
    }
}
