package com.tokopedia.core.network.constants;

/**
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public class TkpdBaseURL {

    public static String LIVE_DOMAIN = "https://ws.tokopedia.com/";
    public static String STAGE_DOMAIN = "https://ws-staging.tokopedia.com/";
    public static String ALPHA_DOMAIN = "https://ws-alpha.tokopedia.com/";
    public static String BASE_DOMAIN = LIVE_DOMAIN;
    public static String ACE_STAGING_DOMAIN = "http://ace-staging.tokopedia.com/";
    public static String ACE_DOMAIN = "https://ace.tokopedia.com/";
    public static String CLOVER_DOMAIN = "https://points.tokopedia.com/";
    public static String TOPADS_DOMAIN = "https://ta.tokopedia.com/";
    public static String TOPADS_STAGING_DOMAIN = "http://ta-staging.tokopedia.com/";
    public static String MOJITO_DOMAIN = "https://mojito.tokopedia.com/";
    public static String MOJITO_STAGING_DOMAIN = "https://mojito-staging.tokopedia.com/";
    public static String HADES_DOMAIN = "https://hades.tokopedia.com";
    public static String HADES_STAGING_DOMAIN = "https://hades-staging.tokopedia.com";
    public static String RECHARGE_API_DOMAIN = "https://pulsa-api.tokopedia.com/";
    public static String RECHARGE_STAGING_DOMAIN = "https://pulsa-api-staging.tokopedia.com/";
    public static String ACCOUNTS_DOMAIN = "https://accounts.tokopedia.com/";
    public static String ACCOUNTS_STAGING_DOMAIN = "https://accounts-staging.tokopedia.com/";
    public static String ACCOUNTS_ALPHA_DOMAIN = "https://accounts-alpha.tokopedia.com/";
    public static String INBOX_DOMAIN = "https://inbox.tokopedia.com";
    public static String JS_DOMAIN = "https://js.tokopedia.com/";
    public static String JS_STAGING_DOMAIN = "https://js-staging.tokopedia.com/";
    public static String JS_ALPHA_DOMAIN = "https://ajax-alpha.tokopedia.com/js/";
    public static String KERO_DOMAIN = "https://kero.tokopedia.com/";
    public static String JAHE_DOMAIN = "https://jahe.tokopedia.com";
    public static String PULSA_WEB_DOMAIN = "https://pulsa.tokopedia.com";
    public static String PULSA_WEB_STAGING_DOMAIN = "https://pulsa-staging.tokopedia.com";
    public static String GOLD_MERCHANT_DOMAIN = "https://goldmerchant.tokopedia.com";
    public static String GOLD_MERCHANT_STAGING_DOMAIN = "http://goldmerchant-staging.tokopedia.com";
    public static String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String MOBILE_DOMAIN = "https://m.tokopedia.com/";
    public static String BASE_CONTACT_US = WEB_DOMAIN + "contact-us";
    public static String BASE_ACTION = BASE_DOMAIN + "v4/action/";

    public static class Product {
        public static final String URL_PRODUCT = BASE_DOMAIN + "v4/product/";
        public static final String URL_PRODUCT_ACTION = BASE_DOMAIN + "v4/action/product/";
        public static final String URL_PROMO_ACTION = BASE_DOMAIN + "v4/action/promo/";
        public static final String URL_PROMO = BASE_DOMAIN + "v4/promo/";
        public static final String URL_REVIEW_ACTION = BASE_DOMAIN + "v4/action/review/";

        public static final String PATH_GET_DETAIL_PRODUCT = "get_detail.pl";
        public static final String PATH_GET_OTHER_PRODUCT = "get_other_product.pl";
        public static final String PATH_GET_ADD_PRODUCT_FORM = "get_add_product_form.pl";
        public static final String PATH_GET_EDIT_PRODUCT_FORM = "get_edit_product_form.pl";
        public static final String PATH_GET_LIKE_REVIEW = "get_like_dislike_review.pl";
        public static final String PATH_GET_PICTURE_PRODUCT = "get_product_picture.pl";
        public static final String PATH_GET_REVIEW = "get_product_review.pl";
        public static final String PATH_GET_TALK = "get_product_talk.pl";
        public static final String PATH_MANAGE_PRODUCT = "manage_product.pl";

        public static final String PATH_ADD_PRODUCT_SUBMIT = "add_product_submit.pl";
        public static final String PATH_ADD_VALIDATION = "add_product_validation.pl";
        public static final String PATH_DELETE_PRODUCT = "delete_product.pl";
        public static final String PATH_DELETE_PICTURE = "delete_product_pic.pl";
        public static final String PATH_EDIT_CATEGORY = "edit_category.pl";
        public static final String PATH_EDIT_ETALASE = "edit_etalase.pl";
        public static final String PATH_EDIT_INSURANCE = "edit_insurance.pl";
        public static final String PATH_EDIT_PRICE = "edit_price.pl";
        public static final String PATH_EDIT_PRODUCT = "edit_product.pl";
        public static final String PATH_EDIT_PICTURE = "edit_product_picture.pl";
        public static final String PATH_EDIT_RETURNABLE = "edit_returnable.pl";
        public static final String PATH_MOVE_TO_WAREHOUSE = "move_to_warehouse.pl";
        public static final String PATH_PROMOTE_PRODUCT = "promote_product.pl";
        public static final String PATH_REPORT_PRODUCT = "report_product.pl";
        public static final String PATH_EDIT_DESCRIPTION = "edit_description.pl";
        public static final String PATH_EDIT_WEIGHT_PRICE = "edit_weight_price.pl";

        public static final String PATH_AD_IMPRESSION_CLICK = "ad_impression_click.pl";

        public static final String PATH_AD_PRODUCT_FEED = "ad_product_feed.pl";
        public static final String PATH_AD_PRODUCT_HOTLIST = "ad_product_hotlist.pl";
        public static final String PATH_AD_PRODUCT_SEARCH = "ad_product_search.pl";
        public static final String PATH_AD_SHOP_FEED = "ad_shop_feed.pl";

        public static final String PATH_ADD_COMMENT_REVIEW = "add_comment_review.pl";
        public static final String PATH_ADD_REVIEW = "add_product_review.pl";
        public static final String PATH_DELETE_COMMENT_REVIEW = "delete_comment_review.pl";
        public static final String PATH_EDIT_REVIEW = "edit_product_review.pl";
        public static final String PATH_LIKE_DISLIKE_REVIEW = "like_dislike_review.pl";
        public static final String PATH_REPORT_REVIEW = "report_review.pl";
        public static final String PATH_SET_READ_REVIEW = "set_read_review.pl";
        public static final String PATH_SKIP_REVIEW = "skip_product_review.pl";

        public static final String PATH_ADD_REPUTATION_REVIEW_VALIDATION = "insert_reputation_review_validation.pl";
        public static final String PATH_ADD_REPUTATION_REVIEW_SUBMIT = "insert_reputation_review_submit.pl";
        public static final String PATH_EDIT_REPUTATION_REVIEW_VALIDATION = "edit_reputation_review_validation.pl";
        public static final String PATH_EDIT_REPUTATION_REVIEW_SUBMIT = "edit_reputation_review_submit.pl";

        public static final String PATH_GET_HELPFUL_REVIEW = "get_helpful_review.pl";
        public static final String PATH_GET_REPORT_PRODUCT_TYPE = "get_product_report_type.pl";
    }

    public static class User {
        public static final String URL_FAVE_SHOP_ACTION = BASE_DOMAIN + "v4/action/favorite-shop/";
        public static final String URL_WISH_LIST_ACTION = BASE_DOMAIN + "v4/action/wishlist/";
        public static final String URL_WISH_LIST = BASE_DOMAIN + "v4/wishlist/";
        public static final String URL_GENERAL_ACTION = BASE_DOMAIN + "v4/action/general-usage/";
        public static final String URL_INBOX_REPUTATION = BASE_DOMAIN + "v4/inbox-reputation/";
        public static final String URL_INBOX_RES_CENTER = BASE_DOMAIN + "v4/inbox-resolution-center/";
        public static final String URL_INBOX_REVIEW = BASE_DOMAIN + "v4/inbox-review/";
        public static final String URL_INBOX_TICKET = BASE_DOMAIN + "v4/inbox-ticket/";
        public static final String URL_INTERRUPT_ACTION = BASE_DOMAIN + "v4/action/interrupt/";
        public static final String URL_INTERRUPT = BASE_DOMAIN + "v4/interrupt/";
        public static final String URL_INVOICE = BASE_DOMAIN + "v4/";
        public static final String URL_MSISDN_ACTION = BASE_DOMAIN + "v4/action/msisdn/";
        public static final String URL_MSISDN = BASE_DOMAIN + "v4/msisdn/";
        public static final String URL_NOTIFICATION = BASE_DOMAIN + "v4/notification/";
        public static final String URL_PEOPLE_ACTION = BASE_DOMAIN + "v4/action/people/";
        public static final String URL_PEOPLE = BASE_DOMAIN + "v4/people/";
        public static final String URL_SESSION = BASE_DOMAIN + "v4/session/";
        public static final String URL_REGISTER = BASE_DOMAIN + "v4/action/register/";
        public static final String URL_TICKET_ACTION = BASE_DOMAIN + "v4/action/ticket/";
        public static final String URL_REGISTER_NEW = BASE_DOMAIN + "/v4/action/register/";


        public static final String REGISTER_NEW = "register.pl";
        public static final String PATH_FAVE_SHOP = "fav_shop.pl";

        public static final String PATH_ACTIVATE_CODE = "activate_code.pl";
        public static final String PATH_CANCEL_EDIT_EMAIL = "cancel_edit_email.pl";
        public static final String PATH_CONFIRM_EEDIT_EMAIL = "confirm_edit_email.pl";
        public static final String PATH_EDIT_EMAIL = "edit_email.pl";
        public static final String PATH_RESEND_CODE = "resend_code.pl";
        public static final String PATH_RESET_PASSWORD = "reset_password.pl";

        public static final String PATH_GET_INBOX_REPUTATION = "get_inbox_reputation.pl";
        public static final String PATH_GET_LIST_REPUTATION_REVIEW = "get_list_reputation_review.pl";
        public static final String PATH_GET_SINGLE_REPUTATION_REVIEW = "get_single_reputation_review.pl";

        public static final String PATH_GET_CREATE_RESOLUTION_FORM = "get_create_resolution_form.pl";
        public static final String PATH_GET_CREATE_RESOLUTION_FORM_NEW = "get_create_resolution_form_new.pl";
        public static final String PATH_GET_EDIT_RESOLUTION_FORM = "get_edit_resolution_form.pl";
        public static final String PATH_GET_RES_CENTER_PRODUCT_LIST = "get_product_list.pl";
        public static final String PATH_GET_SOLUTION = "get_form_solution.pl";
        public static final String PATH_GET_KURIR_LIST = "get_kurir_list.pl";
        public static final String PATH_GET_APPEAL_RESOLUTION_FORM = "get_appeal_resolution_form.pl";
        public static final String PATH_GET_RESOLUTION_CENTER = "get_resolution_center_new.pl";
        public static final String PATH_GET_RESOLUTION_CENTER_DETAIL = "get_resolution_center_detail_new.pl";
        public static final String PATH_GET_RESOLUTION_CENTER_SHOW_MORE = "get_resolution_center_show_more.pl";
        public static final String PATH_TRACK_SHIPPING_REF = "track_shipping_ref.pl";

        public static final String PATH_GET_INBOX_REVIEW = "get_inbox_review.pl";

        public static final String PATH_GET_INBOX_TICKET = "get_inbox_ticket.pl";
        public static final String PATH_GET_INBOX_TICKET_DETAIL = "get_inbox_ticket_detail.pl";
        public static final String PATH_GET_INBOX_TICKET_VIEW_MORE = "get_inbox_ticket_view_more.pl";
        public static final String PATH_GET_OPEN_TICKET_FORM = "get_open_ticket_form.pl";

        public static final String PATH_ANSWER_QUESTION = "answer_question.pl";
        public static final String PATH_REQUEST_OTP = "request_otp.pl";
        public static final String PATH_REQUEST_OTP_PHONE = "msisdn/send_verification_otp.pl";

        public static final String PATH_GET_QUESTION_FORM = "get_question_form.pl";

        public static final String PATH_RENDER_INVOICE = "invoice.pl";

        public static final String PATH_ARCHIVE_MESSAGES = "archive_messages.pl";
        public static final String PATH_UNDO_ARCHIVE_MESSAGES = "undo_archive_messages.pl";
        public static final String PATH_DELETE_MESSAGES = "delete_messages.pl";
        public static final String PATH_UNDO_DELETE_MESSAGES = "undo_delete_messages.pl";
        public static final String PATH_MOVE_TO_INBOX = "move_to_inbox.pl";
        public static final String PATH_UNDO_MOVE_TO_INBOX = "undo_move_to_inbox.pl";
        public static final String PATH_DELETE_MESSAGES_FOREVER = "delete_forever_messages.pl";

        public static final String PATH_ARCHIVE_MESSAGE_DETAIL = "archive_messages_detail.pl";
        public static final String PATH_DELETE_FOREVER_MESSAGE_DETAIL = "delete_forever_messages_detail.pl";
        public static final String PATH_DELETE_MESSAGE_DETAIL = "delete_messages_detail.pl";
        public static final String PATH_FLAG_SPAM = "flag_spam.pl";
        public static final String PATH_MOVE_TO_INBOX_DETAIL = "move_to_inbox_detail.pl";
        public static final String PATH_REPLY_MESSAGE = "reply_message.pl";
        public static final String PATH_SEND_MESSAGE = "send_message.pl";
        public static final String PATH_UNDO_ARCHIVE_MESSAGE_DETAIL = "undo_archive_messages_detail.pl";
        public static final String PATH_UNDO_DELETE_MESSAGE_DETAIL = "undo_delete_messages_detail.pl";
        public static final String PATH_UNDO_FLAG_SPAM = "undo_flag_spam.pl";
        public static final String PATH_UNDO_MOVE_TO_INBOX_DETAIL = "undo_move_to_inbox_detail.pl";

        public static final String PATH_DO_VERIFICATION_MSISDN = "do_verification_msisdn.pl";
        public static final String PATH_SEND_EMAIL_CHANGE_PHONE_NUMBER = "send_email_change_phone_number.pl";
        public static final String PATH_SEND_VERIFICATION_OTP = "send_verification_otp.pl";
        public static final String PATH_SKIP_UPDATE = "skip_update.pl";
        public static final String PATH_VALIDATE_EMAIL_CODE = "validate_email_code.pl";

        public static final String PATH_GET_VERIFICATION_NUMBER_FORM = "get_verification_number_form.pl";

        public static final String PATH_GET_NOTIFICATION = "get_notification.pl";
        public static final String PATH_RESET_NOTIFICATION = "reset_notification.pl";

        public static final String PATH_ADD_ADDRESS = "add_address.pl";
        public static final String PATH_ADD_BANK_ACCOUNT = "add_bank_account.pl";
        public static final String PATH_CONFIRM_NEW_EMAIL = "confirm_new_email.pl";
        public static final String PATH_DELETE_ADDRESS = "delete_address.pl";
        public static final String PATH_DELETE_BANK_ACCOUNT = "delete_bank_account.pl";
        public static final String PATH_EDIT_ADDRESS = "edit_address.pl";
        public static final String PATH_EDIT_BANK_ACCOUNT = "edit_bank_account.pl";
        public static final String PATH_EDIT_BIODATA = "edit_biodata.pl";
        public static final String PATH_EDIT_CONTACT = "edit_contact.pl";
        public static final String PATH_EDIT_DEFAULT_ADDRESS = "edit_default_address.pl";
        public static final String PATH_EDIT_DEFAULT_BANK_ACCOUNT = "edit_default_bank_account.pl";
        public static final String PATH_EDIT_NOTIFICATION = "edit_notification.pl";
        public static final String PATH_EDIT_PASSWORD = "edit_password.pl";
        public static final String PATH_EDIT_PRIVACY = "edit_privacy.pl";
        public static final String PATH_EDIT_PROFILE = "edit_profile.pl";
        public static final String PATH_SEND_OTP_EDIT_EMAIL = "send_otp_edit_email.pl";
        public static final String PATH_UPLOAD_PROFILE_PICTURE = "upload_profile_picture.pl";

        public static final String PATH_GET_ADDRESS = "get_address.pl";
        public static final String PATH_GET_BANK_ACCOUNT = "get_bank_account.pl";
        public static final String PATH_GET_DEFAULT_BANK_ACCOUNT = "get_default_bank_account.pl";
        public static final String PATH_GET_FAVORITE_SHOP = "get_favorit_shop.pl";
        public static final String PATH_GET_PEOPLE_INFO = "get_people_info.pl";
        public static final String PATH_GET_PRIVACY = "get_privacy.pl";
        public static final String PATH_GET_PROFILE = "get_profile.pl";
        public static final String PATH_GET_RANDOM_FAV_SHOP = "get_random_fav_shop.pl";
        public static final String PATH_GET_SEARCH_BANK_ACCOUNT = "search_bank_account.pl";

        public static final String PATH_LOGIN = "login.pl";
        public static final String PATH_CREATE_PASSWORD = "create_password.pl";
        public static final String PATH_DO_LOGIN = "do_login.pl";
        public static final String PATH_DO_LOGIN_PLUS = "do_login_plus.pl";
        public static final String PATH_LOGOUT = "logout.pl";
        public static final String PATH_BYPASS_LOGIN = "v2_login.pl";
        public static final String PATH_MAKE_LOGIN = "v4/session/make_login.pl";

        public static final String PATH_GIVE_RATING = "give_rating.pl";
        public static final String PATH_OPEN_TICKET = "open_ticket.pl";
        public static final String PATH_REPLY_TICKET_SUBMIT = "reply_ticket_submit.pl";
        public static final String PATH_REPLY_TICKET_VALIDATION = "reply_ticket_validation.pl";

        public static final String PATH_ADD_WISHLIST_PRODUCT = "add_wishlist_product.pl";
        public static final String PATH_REMOVE_WISHLIST_PRODUCT = "remove_wishlist_product.pl";

        public static final String PATH_IS_ALREADY_WISHLIST_PRODUCT = "is_already_wishlist_product.pl";
    }

    public static class Shop {
        public static final String PATH_MY_SHOP = "v4/myshop/";
        public static final String URL_MY_SHOP = BASE_DOMAIN + PATH_MY_SHOP;
        public static final String URL_MY_SHOP_ACTION = BASE_DOMAIN + "v4/action/myshop/";
        public static final String URL_MY_SHOP_ADDRESS = BASE_DOMAIN + "v4/myshop-address/";
        public static final String URL_MY_SHOP_ADDRESS_ACTION = BASE_DOMAIN + "v4/action/myshop-address/";
        public static final String PATH_MY_SHOP_ETALASE = "v4/myshop-etalase/";
        public static final String URL_MY_SHOP_ETALASE = BASE_DOMAIN + PATH_MY_SHOP_ETALASE;
        public static final String PATH_ACTION_MY_SHOP_ETALASE = "v4/action/myshop-etalase/";
        public static final String URL_MY_SHOP_ETALASE_ACTION = BASE_DOMAIN + PATH_ACTION_MY_SHOP_ETALASE;
        public static final String URL_MY_SHOP_INFO_ACTION = BASE_DOMAIN + "v4/action/myshop-info/";
        public static final String URL_MY_SHOP_INFO = BASE_DOMAIN + "v4/myshop-info/";
        public static final String URL_MY_SHOP_NOTE = BASE_DOMAIN + "v4/myshop-note/";
        public static final String URL_MY_SHOP_NOTE_ACTION = BASE_DOMAIN + "v4/action/myshop-note/";
        public static final String URL_MY_SHOP_ORDER = BASE_DOMAIN + "v4/myshop-order/";
        public static final String URL_MY_SHOP_ORDER_ACTION = BASE_DOMAIN + "v4/action/myshop-order/";
        public static final String URL_MY_SHOP_PAYMENT = BASE_DOMAIN + "v4/myshop-payment/";
        public static final String URL_MY_SHOP_PAYMENT_ACTION = BASE_DOMAIN + "v4/action/myshop-payment/";
        public static final String URL_MY_SHOP_SHIPMENT = BASE_DOMAIN + "v4/myshop-shipment/";
        public static final String URL_MY_SHOP_SHIPMENT_ACTION = BASE_DOMAIN + "v4/action/myshop-shipment/";
        public static final String URL_NOTES = BASE_DOMAIN + "v4/notes/";
        public static final String URL_REPUTATION_ACTION = BASE_DOMAIN + "v4/action/reputation/";
        public static final String URL_SHOP = BASE_DOMAIN + "v4/shop/";
        public static final String URL_SHIPPING_WEBVIEW = BASE_DOMAIN + "v4/web-view/";

        public static final String PATH_CHECK_DOMAIN = "check_domain.pl";
        public static final String PATH_CHECK_SHOP_NAME = "check_shop_name.pl";
        public static final String PATH_OPEN_SHOP_SUBMIT = "open_shop_submit.pl";
        public static final String PATH_OPEN_SHOP_VALIDATION = "open_shop_validation.pl";

        public static final String PATH_ADD_LOCATION = "add_location.pl";
        public static final String PATH_DELETE_LOCATION = "delete_location.pl";
        public static final String PATH_NEW_ORDER_LOCATION = "new_order_location.pl";
        public static final String PATH_EDIT_LOCATION = "edit_location.pl";

        public static final String PATH_GET_LOCATION = "get_location.pl";
        public static final String PATH_GET_OPEN_SHOP_FORM = "get_open_shop_form.pl";

        public static final String PATH_EVENT_SHOP_ADD_ETALASE = "event_shop_add_etalase.pl";
        public static final String PATH_EVENT_SHOP_DELETE_ETALASE = "event_shop_delete_etalase.pl";
        public static final String PATH_EVENT_SHOP_REORDER_ETALASE = "event_shop_reorder_etalase.pl";
        public static final String PATH_EVENT_SHOP_EDIT_ETALASE = "event_shop_edit_etalase.pl";

        public static final String PATH_GET_SHOP_ETALASE = "get_shop_etalase.pl";

        public static final String PATH_UPDATE_SHOP_INFO = "update_shop_info.pl";
        public static final String PATH_UPDATE_SHOP_PICTURE = "update_shop_picture.pl";
        public static final String PATH_UPDATE_SHOP_CLOSE = "update_shop_close.pl";

        public static final String PATH_GET_SHOP_INFO = "get_shop_info.pl";

        public static final String PATH_ADD_SHOP_NOTE = "add_shop_note.pl";
        public static final String PATH_DELETE_SHOP_NOTE = "delete_shop_note.pl";
        public static final String PATH_EDIT_SHOP_NOTE = "edit_shop_note.pl";

        public static final String PATH_GET_SHOP_NOTE = "get_shop_note.pl";

        public static final String PATH_EDIT_SHIPPING_REF = "edit_shipping_ref.pl";
        public static final String PATH_PROCEED_ORDER = "proceed_order.pl";
        public static final String PATH_PROCEED_SHIPPING = "proceed_shipping.pl";

        public static final String PATH_GET_SHIPPING_FORM = "get_edit_shipping_form.pl";
        public static final String PATH_GET_DETAIL_INFO_DETAIL = "get_shipping_detail_info.pl";
        public static final String PATH_GET_ORDER_LIST = "get_order_list.pl";
        public static final String PATH_GET_ORDER_NEW = "get_order_new.pl";
        public static final String PATH_GET_ORDER_PROCESS = "get_order_process.pl";
        public static final String PATH_GET_ORDER_STATUS = "get_order_status.pl";
        public static final String PATH_GET_PROCEED_SHIPPING_FORM = "get_proceed_shipping_form.pl";

        public static final String PATH_UPDATE_PAYMENT_INFO = "update_payment_info.pl";

        public static final String PATH_GET_PAYMENT_INFO = "get_payment_info.pl";

        public static final String PATH_UPDATE_SHIPPING_INFO = "update_shipping_info.pl";

        public static final String PATH_GET_SHIPPING_INFO = "get_shipping_info.pl";

        public static final String PATH_GET_NOTES_DETAIL = "get_notes_detail.pl";

        public static final String PATH_DELETE_REP_REVIEW_RESPONSE = "delete_reputation_review_response.pl";
        public static final String PATH_EDIT_REP_REVIEW = "edit_reputation_review.pl";
        public static final String PATH_INSERT_REP = "insert_reputation.pl";
        public static final String PATH_INSERT_REP_REVIEW = "insert_reputation_review.pl";
        public static final String PATH_INSERT_REP_REVIEW_RESPONSE = "insert_reputation_review_response.pl";
        public static final String PATH_SKIP_REP_REVIEW = "skip_reputation_review.pl";

        public static final String PATH_GET_LIKE_DISLIKE_REVIEW = "get_like_dislike_review_shop.pl";
        public static final String PATH_GET_PEOPLE_FAV_MY_SHOP = "get_people_who_favorite_myshop.pl";
        public static final String PATH_GET_SHOP_LOCATION = "get_shop_location.pl";
        public static final String PATH_GET_SHOP_NOTES = "get_shop_notes.pl";
        public static final String PATH_GET_SHOP_PRODUCT = "get_shop_product.pl";
        public static final String PATH_GET_SHOP_REVIEW = "get_shop_review.pl";

        public static final String PATH_RETRY_PICKUP = "retry_pickup.pl";
    }

    public static final class Etc {
        public static final String URL_ADDRESS = BASE_DOMAIN + "v4/address/";
        public static final String URL_DEPARTMENT = BASE_DOMAIN + "v4/department/";
        public static final String URL_HOME = BASE_DOMAIN + "v4/home/";
        public static final String URL_TICKER = BASE_DOMAIN + "v4/ticker/";

        public static final String PATH_GET_CITY = "get_city.pl";
        public static final String PATH_GET_DISTRICT = "get_district.pl";
        public static final String PATH_GET_PROVINCE = "get_province.pl";
        public static final String PATH_GET_SHIPPING_CITY = "get_shipping_city.pl";

        public static final String PATH_GET_DEPARTMENT_CHILD = "get_department_child.pl";
        public static final String PATH_GET_DEPARTMENT_PARENT = "get_department_parent.pl";

        public static final String PATH_GET_FAVORITE_SHOP = "get_favorite_shop.pl";
        public static final String PATH_GET_PRODUCT_FEED = "get_product_feed.pl";
        public static final String PATH_GET_RECENT_VIEW_PRODUCT = "get_recent_view_product.pl";
        public static final String PATH_GET_WISHLIST = "get_wishlist.pl";

        public static final String PATH_GET_DATA_SOURCE_TICKER = "get_data_source_ticker.pl";
        public static final String PATH_GET_LIST_FAVE_SHOP_ID = "/v4/home/get_list_fave_shop_id.pl";
    }

    public static class ResCenter {
        public static final String URL_RES_CENTER_ACTION = BASE_DOMAIN + "v4/action/resolution-center/";

        public static final String PATH_ACCEPT_ADMIN_RESOLUTION = "accept_admin_resolution.pl";
        public static final String PATH_ACCEPT_RESOLUTION = "accept_resolution.pl";
        public static final String PATH_CANCEL_RESOLUTION = "cancel_resolution.pl";
        public static final String PATH_CREATE_RES_SUBMIT = "create_resolution_submit.pl";
        public static final String PATH_CREATE_RES_VALIDATION = "create_resolution_validation_new.pl";
        public static final String PATH_EDIT_RESI_RESOLUTION_VALIDATION = "edit_resi_resolution_validation.pl";
        public static final String PATH_EDIT_RESI_RESOLUTION_SUBMIT = "edit_resi_resolution_submit.pl";
        public static final String PATH_FINISH_RES_RETURN = "finish_resolution_retur.pl";
        public static final String PATH_INPUT_RESI_RESOLUTION_VALIDATION = "input_resi_resolution_validation.pl";
        public static final String PATH_INPUT_RESI_RESOLUTION_SUBMIT = "input_resi_resolution_submit.pl";
        public static final String PATH_INPUT_ADDRESS_RESOLUTION = "input_address_resolution.pl";
        public static final String PATH_EDIT_ADDRESS_RESOLUTION = "edit_address_resolution.pl";
        public static final String PATH_REJECT_ADMIN_RES_SUBMIT = "reject_admin_resolution_submit.pl";
        public static final String PATH_REJECT_ADMIN_RES_VALIDATION = "reject_admin_resolution_validation_new.pl";
        public static final String PATH_REPLY_CONVERSATION_SUBMIT = "reply_conversation_submit.pl";
        public static final String PATH_REPLY_CONVERSATION_VALIDATION = "reply_conversation_validation.pl";
        public static final String PATH_REPLY_CONVERSATION_VALIDATION_NEW = "reply_conversation_validation_new.pl";
        public static final String PATH_REPORT_REOLUTION = "report_resolution.pl";
        public static final String PATH_RESOLUTION_CENTER = "resolution-center.pl";
    }

    public static class Search {
        public static final String URL_CATALOG_SELLER = "search/v1/catalog/product";

        public static final String URL_CATALOG = BASE_DOMAIN + "v4/catalog/";
        public static final String URL_HOT_LIST = BASE_DOMAIN + "v4/hotlist/";
        public static final String URL_SEARCH = BASE_DOMAIN + "v4/search/";

        public static final String URL_SEARCH_SUGGESTION = ACE_DOMAIN;

        public static final String PATH_GET_CATALOG = "get_catalog.pl";
        public static final String PATH_GET_CATALOG_DETAIL = "get_catalog_detail.pl";
        public static final String PATH_GET_SELL_FORM = "get_sell_form.pl";

        public static final String PATH_GET_HOTLIST = "get_hotlist.pl";
        public static final String PATH_GET_HOTLIST_PRODUCT = "get_hotlist_product.pl";
        public static final String PATH_GET_HOTLIST_BANNER = "get_hotlist_banner.pl";

        public static final String PATH_SEARCH_CATALOG = "search_catalog.pl";
        public static final String PATH_SEARCH_PRODUCT = "search_product.pl";
        public static final String PATH_SEARCH_SHOP = "search_shop.pl";
    }

    public static class Transaction {
        public static final String URL_DEPOSIT_ACTION = BASE_DOMAIN + "v4/action/deposit/";
        public static final String URL_DEPOSIT = BASE_DOMAIN + "v4/deposit/";
        public static final String URL_DEPOSIT_CLOVER = CLOVER_DOMAIN + "app/";
        public static final String URL_TRACKING_ORDER = BASE_DOMAIN + "v4/tracking-order/";
        public static final String URL_TX_ACTION = BASE_DOMAIN + "v4/action/tx/";
        public static final String URL_TX = BASE_DOMAIN + "v4/";
        public static final String URL_TX_CART_ACTION = BASE_DOMAIN + "v4/action/tx-cart/";
        public static final String URL_TX_CART = BASE_DOMAIN + "v4/tx-cart/";
        public static final String URL_TX_ORDER_ACTION = BASE_DOMAIN + "v4/action/tx-order/";
        public static final String URL_TX_ORDER = BASE_DOMAIN + "v4/tx-order/";
        public static final String URL_TX_PAYMENT_BCA_CLIKPAY = BASE_DOMAIN + "v4/";
        public static final String URL_TX_PAYMENT_CC_BC = BASE_DOMAIN + "v4/";
        public static final String URL_TX_PAYMENT_EMONEY = BASE_DOMAIN + "v4/";
        public static final String URL_TX_PAYMENT_SPRINT_ASIA = BASE_DOMAIN + "v4/";
        public static final String URL_TX_PAYMENT_VOUCHER = BASE_DOMAIN + "v4/tx-voucher/";

        public static final String PATH_DO_WITHDRAW = "do_withdraw.pl";
        public static final String PATH_SEND_OTP_VERIFY_BANK_ACCOUNT = "send_otp_verify_bank_account.pl";

        public static final String PATH_GET_DEPOSIT = "get_deposit.pl";
        public static final String PATH_GET_SUMMARY = "get_summary.pl";
        public static final String PATH_GET_WITHDRAW_FORM = "get_withdraw_form.pl";
        public static final String PATH_GET_TOPPOINTS = "get_lp.pl";
        public static final String PATH_GET_TOPPOINTS_CLOVER = "v4";


        public static final String PATH_TRACK_ORDER = "track_order.pl";

        public static final String PATH_STEP_1_PROCESS_CREDIT_CARD = "step_1_process_credit_card.pl";
        public static final String PATH_GET_PARAMETER_DYNAMIC_PAYMENT = "toppay_get_parameter.pl";
        public static final String PATH_THANKS_DYNAMIC_PAYMENT = "toppay_thanks_action.pl";

        public static final String PATH_DO_PAYMENT = "tx.pl";

        public static final String PATH_ADD_TO_CART = "add_to_cart.pl";
        public static final String PATH_CANCEL_CART = "cancel_cart.pl";
        public static final String PATH_EDIT_ADDRESS = "edit_address.pl";
        public static final String PATH_EDIT_INSURANCE = "edit_insurance.pl";
        public static final String PATH_EDIT_PRODUCT = "edit_product.pl";
        public static final String PATH_EDIT_CART = "bulk-edit.pl";

        public static final String PATH_CALCULATE_CART = "calculate_cart.pl";
        public static final String PATH_CALCULATE_CREDIT_CARD = "calculate_credit_card_charge.pl";
        public static final String PATH_CART_SEARCH_ADDRESS = "cart_search_address.pl";
        public static final String PATH_GET_ADD_TO_CART_FORM = "get_add_to_cart_form.pl";
        public static final String PATH_CANCEL_CART_FORM = "get_cancel_cart_form.pl";
        public static final String PATH_GET_EDIT_ADDRESS_SHIPPING_FORM = "get_edit_address_shipping_form.pl";
        public static final String PATH_GET_EDIT_PRODUCT_FORM = "get_edit_product_form.pl";

        public static final String PATH_CANCEL_PAYMENT = "cancel_payment.pl";
        public static final String PATH_CONFIRM_PAYMENT = "confirm_payment.pl";
        public static final String PATH_DELIVERY_CONFIRM = "delivery_confirm.pl";
        public static final String PATH_DELIVERY_FINISH_ORDER = "delivery_finish_order.pl";
        public static final String PATH_DELIVERY_REJECT = "delivery_reject.pl";
        public static final String PATH_EDIT_PAYMENT = "edit_payment.pl";
        public static final String PATH_REQUEST_CANCEL_ORDER = "request_cancel_order.pl";
        public static final String PATH_REORDER = "reorder.pl";
        public static final String PATH_UPLOAD_VALID_PROOF_BY_PAYMENT = "upload_valid_proof_by_payment.pl";

        public static final String PATH_GET_CANCEL_PAYMENT_FORM = "get_cancel_payment_form.pl";
        public static final String PATH_GET_CONFIRM_PAYMENT_FORM = "get_confirm_payment_form.pl";
        public static final String PATH_GET_EDIT_PAYMENT_FORM = "get_edit_payment_form.pl";
        public static final String PATH_GET_REORDER_FORM = "get_reorder_form.pl";
        public static final String PATH_GET_TX_ORDER_DELIVER = "get_tx_order_deliver.pl";
        public static final String PATH_GET_TX_ORDER_LIST = "get_tx_order_list.pl";
        public static final String PATH_GET_TX_ORDER_PAYMENT_CONFIRMATION = "get_tx_order_payment_confirmation.pl";
        public static final String PATH_GET_TX_ORDER_PAYMENT_CONFIRMED = "get_tx_order_payment_confirmed.pl";
        public static final String PATH_GET_TX_ORDER_PAYMENT_CONFIRMED_DETAIL = "get_tx_order_payment_confirmed_detail.pl";
        public static final String PATH_GET_TX_ORDER_STATUS = "get_tx_order_status.pl";
        public static final String PATH_GET_UPLOAD_PROOF_BY_PAYMENT = "get_upload_proof_by_payment_form.pl";

        public static final String PATH_TX_PAYMENT_BCA_KLIKPAY = "tx-payment-bcaklikpay.pl";

        public static final String PATH_TX_PAYMENT_CC_BCA = "tx-payment-cc-bca.pl";

        public static final String PATH_TX_PAYMENT_SPRINTASIA = "tx-payment-sprintasia.pl";

        public static final String PATH_CHECK_VOUCHER_CODE = "check_voucher_code.pl";

        public static final String PATH_TX_PAYMENT_EMONEY = "tx-payment-emoney.pl";
    }

    public static class Shipment {
        public static final String PATH_RATES = "rates/v1";
    }

    public static class Upload {
        public static final String URL_GENERATE_HOST_ACTION = BASE_DOMAIN + "v4/action/generate-host/";

        public static final String PATH_GENERATE_HOST = "generate_host.pl";

        public static final String URL_UPLOAD_IMAGE_ACTION = BASE_DOMAIN + "v4/action/upload-image-helper/";

        public static final String PATH_CREATE_RESOLUTION_PICTURE = "create_resolution_picture.pl";
        public static final String PATH_ADD_PRODUCT_PICTURE = "add_product_picture.pl";
        public static final String PATH_OPEN_SHOP_PICTURE = "open_shop_picture.pl";
        public static final String PATH_TICKET_PICTURE = "reply_ticket_picture.pl";
        public static final String PATH_CONTACT_IMAGE = "/web-service/v4/action/upload-image/upload_contact_image.pl";
        public static final String PATH_CREATE_RESOLUTION_PICTURE_FULL = "/web-service/v4/action/upload-image-helper/create_resolution_picture.pl";
        public static final String PATH_PROFILE_IMAGE = "/web-service/v4/action/upload-image/upload_profile_image.pl";
    }

    public static class Ace {
        public static final String PATH_SEARCH = "search/";
        public static final String URL_SEARCH = ACE_DOMAIN + PATH_SEARCH;

        public static final String PATH_CATALOG_SHOP_LIST = "catalog/product";
        public static final String PATH_SEARCH_SHOP = "shop";
        public static final String PATH_OTHER_PRODUCT = "v1/product";
        public static final String PATH_FAV_SHOP_FEED = "catalog/product";
        public static final String PATH_CATALOG = "v1/catalog";
        public static final String PATH_TOP_PICKS = "/hoth/toppicks/widget";
        public static final String PATH_UNIVERSE_SEARCH = "/universe/v3";
        public static final String PATH_DELETE_SEARCH = "/universe/v1?device=android&source=searchbar";

    }

    public static class TopAds {
        public static final String URL_TOPADS = TOPADS_DOMAIN + "promo/v1.1/display/";
        public static final String URL_TOPADS_SHOP = TOPADS_DOMAIN + "promo/v1/display/";

        public static final String PATH_DISPLAY_SHOP = "promo/v1/display/shops";
        public static final String PATH_GET_PROMO_TOP_ADS = "/promo/v1.1/display/products";
        public static final String PATH_GET_SHOP_TOP_ADS = "promo/v1/display/shops";
    }

    public static class Mojito {
        public static final String PATH_USER = "v1.0.3/users/";
        public static final String PATH_PRODUCT = "users/";
        public static final String PATH_CATALOG = "v1/catalogs/";

        public static final String PATH_WISHLIST_PRODUCT = TkpdBaseURL.Mojito.PATH_USER + "{userId}/wishlist/products";
        public static final String PATH_WISHLIST = "wishlist/";
        public static final String PATH_WISH_LIST_V_1_1 = "v1.1";


        public static final String API_HOME_CATEGORY_MENU = "/api/v1/layout/category";
        public static final String PATH_USER_RECENT_VIEW = "users/";
        public static final String PATH_RECENT_VIEW = "/recentview/products/v1";
        public static final String API_V1_BRANDS = "/os/api/v1/brands/list";
        public static final String PATH_USER_WISHLIST = "/users";
        public static final String PATH_SEARCH_WISHLIST = PATH_USER_WISHLIST + "/{userId}/wishlist/search/v2";
    }


    public static class Recharge {
        public static final String VERSION = "v1.1";
        public static final String RECHARGE = "recharge";

        public static final String PATH_STATUS = VERSION + "/status";
        public static final String PATH_CATEGORY = VERSION + "/category/list";
        public static final String PATH_OPERATOR = VERSION + "/operator/list";
        public static final String PATH_PRODUCT = VERSION + "/product/list";
        public static final String PATH_RECENT_NUMBER = VERSION + "/recent-number";
        public static final String PATH_LAST_ORDER = VERSION + "/last-order";
        public static final String PATH_SALDO = "/saldo/";
    }

    public static class KunyitTalk {
        public static final String BASE_HOST_INBOX_TALK = "/talk/v2";

        public static final String GET_INBOX_TALK = BASE_HOST_INBOX_TALK + "/inbox";
        public static final String GET_PRODUCT_TALK = BASE_HOST_INBOX_TALK + "/read";
        public static final String GET_SHOP_TALK = BASE_HOST_INBOX_TALK + "/read";
        public static final String GET_COMMENT_TALK = BASE_HOST_INBOX_TALK + "/comment";
        public static final String GET_INBOX_TALK_DETAIL = BASE_HOST_INBOX_TALK + "/inbox/detail";

        public static final String ADD_PRODUCT_TALK = BASE_HOST_INBOX_TALK + "/create";
        public static final String FOLLOW_PRODUCT_TALK = BASE_HOST_INBOX_TALK + "/follow";
        public static final String DELETE_PRODUCT_TALK = BASE_HOST_INBOX_TALK + "/delete";
        public static final String REPORT_PRODUCT_TALK = BASE_HOST_INBOX_TALK + "/report";

        public static final String ADD_COMMENT_TALK = BASE_HOST_INBOX_TALK + "/comment/create";
        public static final String DELETE_COMMENT_TALK = BASE_HOST_INBOX_TALK + "/comment/delete";
        public static final String REPORT_COMMENT_TALK = BASE_HOST_INBOX_TALK + "/comment/report";

    }

    public static class KunyitMessage {
        public static final String BASE_HOST_INBOX_MESSAGE = "/message/v1";
        public static final String GET_INBOX_MESSAGE = BASE_HOST_INBOX_MESSAGE + "/list";
        public static final String GET_INBOX_DETAIL_MESSAGE = BASE_HOST_INBOX_MESSAGE + "/detail";
        public static final String SEND_MESSAGE = BASE_HOST_INBOX_MESSAGE + "/send";
        public static final String SEND_REPLY_MESSAGE = BASE_HOST_INBOX_MESSAGE + "/reply";
        public static final String DELETE_MESSAGES = BASE_HOST_INBOX_MESSAGE + "/delete";
        public static final String DELETE_MESSAGES_FOREVER = BASE_HOST_INBOX_MESSAGE + "/delete/forever";
        public static final String UNDO_DELETE_MESSAGES = BASE_HOST_INBOX_MESSAGE + "/delete/undo";
        public static final String ARCHIVE_MESSAGES = BASE_HOST_INBOX_MESSAGE + "/archive";
        public static final String UNDO_ARCHIVE_MESSAGES = BASE_HOST_INBOX_MESSAGE + "/archive/undo";
        public static final String FLAG_SPAM = BASE_HOST_INBOX_MESSAGE + "/spam";
        public static final String UNDO_FLAG_SPAM = BASE_HOST_INBOX_MESSAGE + "/spam/undo";
        public static final String MOVE_TO_INBOX = BASE_HOST_INBOX_MESSAGE + "/move_inbox";
        public static final String UNDO_MOVE_TO_INBOX = BASE_HOST_INBOX_MESSAGE + "/move_inbox/undo";
        public static final String MARK_AS_READ = BASE_HOST_INBOX_MESSAGE + "/mark/read";
        public static final String MARK_AS_UNREAD = BASE_HOST_INBOX_MESSAGE + "/mark/unread";
    }

    public static class Accounts {
        public static final String PATH_GET_TOKEN = "token";
        public static final String PATH_GET_INFO = "info";
        public static final String PATH_GET_PROFILE = "profile/json/{id}";
        public static final String PATH_DISCOVER_LOGIN = "api/discover";
        public static final String DO_REGISTER = "api/register";
        public static final String CREATE_PASSWORD = "api/create-password";
        public static final String RESET_PASSWORD = "api/reset";
        public static final String VALIDATE_EMAIL = "/api/register/validate-email";
        public static final String RESENT_ACTIVATION = "/api/resend";
        public static final String GENERATE_HOST = "/api/upload-host";
        public static final String VERIFY_PHONE_NUMBER = "/api/msisdn/verify-msisdn";


        public class OTP {
            private static final String BASE_OTP = "/otp";
            public static final String REQUEST_OTP = BASE_OTP + "/request";
            public static final String VALIDATE_OTP = BASE_OTP + "/validate";
        }
    }

    public static class Home {
        public static final String PATH_API_V1_ANNOUNCEMENT_TICKER = "/api/v1/tickers";
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
        public static final String URL_HELP = MOBILE_DOMAIN + "bantuan/";
        public static final String URL_CHANGE_NUMBER = "https://www.tokopedia.com/contact-us?solution_id=54";
    }

    public static class TopCash {
        public static final String GET_WALLET = ACCOUNTS_DOMAIN + "api/v1/wallet/";
        public static final String PATH_WALLET = "balance";
    }

    public static class Truecaller {
        public static final String VERIFY_PHONE = "/web-service/v4/truecaller/check";
    }

    public static class HadesCategory {
        public static final String CHECK_VERSION = "/v1/categories_version";
        public static final String URL_HADES = HADES_DOMAIN;
        public static final String PATH_CATEGORIES = "/v1/categories/{catId}/detail";
        public static final String FETCH_CATEGORIES = "/v1/categories?filter=type==tree";
    }
}
