package com.tokopedia.core.network.constants;

/**
 * Created by Angga.Prasetiyo on 07/12/2015.
 */
public interface  TkpdBaseURL {

    String LIVE_DOMAIN = "https://ws.tokopedia.com/";
    String STAGE_DOMAIN = "https://ws-staging.tokopedia.com/";
    String ALPHA_DOMAIN = "https://ws-alpha.tokopedia.com/";
    String BASE_DOMAIN = LIVE_DOMAIN;
    String ACE_STAGING_DOMAIN = "http://ace-staging.tokopedia.com/";
    String ACE_DOMAIN = "https://ace-staging.tokopedia.com/";
    String CLOVER_DOMAIN = "https://clover.tokopedia.com/";
    String TOPADS_DOMAIN = "https://ta.tokopedia.com/";
    String TOPADS_STAGING_DOMAIN = "http://ta-staging.tokopedia.com/";
    String MOJITO_DOMAIN = "https://mojito.tokopedia.com/";
    String MOJITO_STAGING_DOMAIN = "https://mojito-staging.tokopedia.com/";
    String HADES_DOMAIN = "https://hades.tokopedia.com";
    String RECHARGE_API_DOMAIN = "https://pulsa-api.tokopedia.com/";
    String RECHARGE_STAGING_DOMAIN = "https://pulsa-api-staging.tokopedia.com/";
    String ACCOUNTS_DOMAIN = "https://accounts.tokopedia.com/";
    String ACCOUNTS_STAGING_DOMAIN = "https://accounts-staging.tokopedia.com/";
    String ACCOUNTS_ALPHA_DOMAIN = "https://accounts-alpha.tokopedia.com/";
    String INBOX_DOMAIN = "https://inbox.tokopedia.com";
    String JS_DOMAIN = "https://js.tokopedia.com/";
    String JS_STAGING_DOMAIN = "https://js-staging.tokopedia.com/";
    String JS_ALPHA_DOMAIN = "https://js-alpha.tokopedia.com/";
    String KERO_DOMAIN = "https://kero.tokopedia.com/";
    String JAHE_DOMAIN = "https://jahe.tokopedia.com";
    String PULSA_WEB_DOMAIN = "https://pulsa.tokopedia.com";
    String PULSA_WEB_STAGING_DOMAIN = "https://pulsa-staging.tokopedia.com";
    String GOLD_MERCHANT_DOMAIN = "https://goldmerchant.tokopedia.com";

    interface Product {
        String URL_PRODUCT = BASE_DOMAIN + "v4/product/";
        String URL_PRODUCT_ACTION = BASE_DOMAIN + "v4/action/product/";
        String URL_PROMO_ACTION = BASE_DOMAIN + "v4/action/promo/";
        String URL_PROMO = BASE_DOMAIN + "v4/promo/";
        String URL_REVIEW_ACTION = BASE_DOMAIN + "v4/action/review/";
        String URL_TALK = BASE_DOMAIN + "v4/talk/";
        String URL_TALK_ACTION = BASE_DOMAIN + "v4/action/talk/";

        String PATH_GET_DETAIL_PRODUCT = "get_detail.pl";
        String PATH_GET_OTHER_PRODUCT = "get_other_product.pl";
        String PATH_GET_ADD_PRODUCT_FORM = "get_add_product_form.pl";
        String PATH_GET_EDIT_PRODUCT_FORM = "get_edit_product_form.pl";
        String PATH_GET_LIKE_REVIEW = "get_like_dislike_review.pl";
        String PATH_GET_PICTURE_PRODUCT = "get_product_picture.pl";
        String PATH_GET_REVIEW = "get_product_review.pl";
        String PATH_GET_TALK = "get_product_talk.pl";
        String PATH_MANAGE_PRODUCT = "manage_product.pl";

        String PATH_ADD_PRODUCT_SUBMIT = "add_product_submit.pl";
        String PATH_ADD_VALIDATION = "add_product_validation.pl";
        String PATH_DELETE_PRODUCT = "delete_product.pl";
        String PATH_DELETE_PICTURE = "delete_product_pic.pl";
        String PATH_EDIT_CATEGORY = "edit_category.pl";
        String PATH_EDIT_ETALASE = "edit_etalase.pl";
        String PATH_EDIT_INSURANCE = "edit_insurance.pl";
        String PATH_EDIT_PRICE = "edit_price.pl";
        String PATH_EDIT_PRODUCT = "edit_product.pl";
        String PATH_EDIT_PICTURE = "edit_product_picture.pl";
        String PATH_EDIT_RETURNABLE = "edit_returnable.pl";
        String PATH_MOVE_TO_WAREHOUSE = "move_to_warehouse.pl";
        String PATH_PROMOTE_PRODUCT = "promote_product.pl";
        String PATH_REPORT_PRODUCT = "report_product.pl";
        String PATH_EDIT_DESCRIPTION = "edit_description.pl";
        String PATH_EDIT_WEIGHT_PRICE = "edit_weight_price.pl";

        String PATH_AD_IMPRESSION_CLICK = "ad_impression_click.pl";

        String PATH_AD_PRODUCT_FEED = "ad_product_feed.pl";
        String PATH_AD_PRODUCT_HOTLIST = "ad_product_hotlist.pl";
        String PATH_AD_PRODUCT_SEARCH = "ad_product_search.pl";
        String PATH_AD_SHOP_FEED = "ad_shop_feed.pl";

        String PATH_ADD_COMMENT_REVIEW = "add_comment_review.pl";
        String PATH_ADD_REVIEW = "add_product_review.pl";
        String PATH_DELETE_COMMENT_REVIEW = "delete_comment_review.pl";
        String PATH_EDIT_REVIEW = "edit_product_review.pl";
        String PATH_LIKE_DISLIKE_REVIEW = "like_dislike_review.pl";
        String PATH_REPORT_REVIEW = "report_review.pl";
        String PATH_SET_READ_REVIEW = "set_read_review.pl";
        String PATH_SKIP_REVIEW = "skip_product_review.pl";

        String PATH_ADD_REPUTATION_REVIEW_VALIDATION = "insert_reputation_review_validation.pl";
        String PATH_ADD_REPUTATION_REVIEW_SUBMIT = "insert_reputation_review_submit.pl";
        String PATH_EDIT_REPUTATION_REVIEW_VALIDATION = "edit_reputation_review_validation.pl";
        String PATH_EDIT_REPUTATION_REVIEW_SUBMIT = "edit_reputation_review_submit.pl";

        String PATH_ADD_COMMENT_TALK = "add_comment_talk.pl";
        String PATH_ADD_PRODUCT_TALK = "add_product_talk.pl";
        String PATH_DELETE_COMMENT_TALK = "delete_comment_talk.pl";
        String PATH_DELETE_PRODUCT_TALK = "delete_product_talk.pl";
        String PATH_FOLLOW_PRODUCT_TALK = "follow_product_talk.pl";
        String PATH_REPORT_COMMENT_TALK = "report_comment_talk.pl";
        String PATH_REPORT_PRODUCT_TALK = "report_product_talk.pl";

        String PATH_GET_COMMENT_BY_TALK = "get_comment_by_talk_id.pl";
        String PATH_GET_HELPFUL_REVIEW = "get_helpful_review.pl";
        String PATH_GET_REPORT_PRODUCT_TYPE = "get_product_report_type.pl";
    }

    interface User {
        String URL_FAVE_SHOP_ACTION = BASE_DOMAIN + "v4/action/favorite-shop/";
        String URL_WISH_LIST_ACTION = BASE_DOMAIN + "v4/action/wishlist/";
        String URL_WISH_LIST = BASE_DOMAIN + "v4/wishlist/";
        String URL_GENERAL_ACTION = BASE_DOMAIN + "v4/action/general-usage/";
        String URL_INBOX_MESSAGE = BASE_DOMAIN + "v4/inbox-message/";
        String URL_INBOX_PRICE_ALERT = BASE_DOMAIN + "v4/inbox-price-alert/";
        String URL_INBOX_REPUTATION = BASE_DOMAIN + "v4/inbox-reputation/";
        String URL_INBOX_RES_CENTER = BASE_DOMAIN + "v4/inbox-resolution-center/";
        String URL_INBOX_REVIEW = BASE_DOMAIN + "v4/inbox-review/";
        String URL_INBOX_TALK = BASE_DOMAIN + "v4/inbox-talk/";
        String URL_INBOX_TICKET = BASE_DOMAIN + "v4/inbox-ticket/";
        String URL_INTERRUPT_ACTION = BASE_DOMAIN + "v4/action/interrupt/";
        String URL_INTERRUPT = BASE_DOMAIN + "v4/interrupt/";
        String URL_INVOICE = BASE_DOMAIN + "v4/";
        String URL_MESSAGE_ACTION = BASE_DOMAIN + "v4/action/message/";
        String URL_MSISDN_ACTION = BASE_DOMAIN + "v4/action/msisdn/";
        String URL_MSISDN = BASE_DOMAIN + "v4/msisdn/";
        String URL_NOTIFICATION = BASE_DOMAIN + "v4/notification/";
        String URL_PEOPLE_ACTION = BASE_DOMAIN + "v4/action/people/";
        String URL_PEOPLE = BASE_DOMAIN + "v4/people/";
        String URL_SESSION_ACTION = BASE_DOMAIN + "v4/action/session/";
        String URL_SESSION = BASE_DOMAIN + "v4/session/";
        String URL_REGISTER = BASE_DOMAIN + "v4/action/register/";
        String URL_TICKET_ACTION = BASE_DOMAIN + "v4/action/ticket/";
        String URL_REGISTER_NEW = BASE_DOMAIN + "/v4/action/register/";


        String REGISTER_NEW = "register.pl";
        String PATH_FAVE_SHOP = "fav_shop.pl";

        String PATH_ACTIVATE_CODE = "activate_code.pl";
        String PATH_CANCEL_EDIT_EMAIL = "cancel_edit_email.pl";
        String PATH_CONFIRM_EEDIT_EMAIL = "confirm_edit_email.pl";
        String PATH_EDIT_EMAIL = "edit_email.pl";
        String PATH_RESEND_CODE = "resend_code.pl";
        String PATH_RESET_PASSWORD = "reset_password.pl";

        String PATH_GET_INBOX_MESSAGE = "get_inbox_message.pl";
        String PATH_GET_INBOX_DETAIL_MESSAGE = "get_inbox_detail_message.pl";

        String PATH_GET_ADD_CATALOG_PRICE_ALERT_FORM = "get_add_catalog_price_alert_form.pl";
        String PATH_GET_ADD_PRODUCT_PRICE_ALERT_FORM = "get_add_product_price_alert_form.pl";
        String PATH_GET_PRICE_ALERT = "get_price_alert.pl";
        String PATH_GET_PRICE_ALERT_DETAIL = "get_price_alert_detail.pl";

        String PATH_GET_INBOX_REPUTATION = "get_inbox_reputation.pl";
        String PATH_GET_LIST_REPUTATION_REVIEW = "get_list_reputation_review.pl";
        String PATH_GET_SINGLE_REPUTATION_REVIEW = "get_single_reputation_review.pl";

        String PATH_GET_CREATE_RESOLUTION_FORM = "get_create_resolution_form.pl";
        String PATH_GET_CREATE_RESOLUTION_FORM_NEW = "get_create_resolution_form_new.pl";
        String PATH_GET_EDIT_RESOLUTION_FORM = "get_edit_resolution_form.pl";
        String PATH_GET_RES_CENTER_PRODUCT_LIST = "get_product_list.pl";
        String PATH_GET_SOLUTION = "get_form_solution.pl";
        String PATH_GET_KURIR_LIST = "get_kurir_list.pl";
        String PATH_GET_APPEAL_RESOLUTION_FORM = "get_appeal_resolution_form.pl";
        String PATH_GET_RESOLUTION_CENTER = "get_resolution_center_new.pl";
        String PATH_GET_RESOLUTION_CENTER_DETAIL = "get_resolution_center_detail_new.pl";
        String PATH_GET_RESOLUTION_CENTER_SHOW_MORE = "get_resolution_center_show_more.pl";
        String PATH_TRACK_SHIPPING_REF = "track_shipping_ref.pl";

        String PATH_GET_INBOX_REVIEW = "get_inbox_review.pl";

        String PATH_GET_INBOX_DETAIL_TALK = "get_inbox_detail_talk.pl";
        String PATH_GET_INBOX_TALK = "get_inbox_talk.pl";

        String PATH_GET_INBOX_TICKET = "get_inbox_ticket.pl";
        String PATH_GET_INBOX_TICKET_DETAIL = "get_inbox_ticket_detail.pl";
        String PATH_GET_INBOX_TICKET_VIEW_MORE = "get_inbox_ticket_view_more.pl";
        String PATH_GET_OPEN_TICKET_FORM = "get_open_ticket_form.pl";

        String PATH_ANSWER_QUESTION = "answer_question.pl";
        String PATH_REQUEST_OTP = "request_otp.pl";
        String PATH_REQUEST_OTP_PHONE = "msisdn/send_verification_otp.pl";

        String PATH_GET_QUESTION_FORM = "get_question_form.pl";

        String PATH_RENDER_INVOICE = "invoice.pl";

        String PATH_ARCHIVE_MESSAGES = "archive_messages.pl";
        String PATH_UNDO_ARCHIVE_MESSAGES = "undo_archive_messages.pl";
        String PATH_DELETE_MESSAGES = "delete_messages.pl";
        String PATH_UNDO_DELETE_MESSAGES = "undo_delete_messages.pl";
        String PATH_MOVE_TO_INBOX = "move_to_inbox.pl";
        String PATH_UNDO_MOVE_TO_INBOX = "undo_move_to_inbox.pl";
        String PATH_DELETE_MESSAGES_FOREVER= "delete_forever_messages.pl";

        String PATH_ARCHIVE_MESSAGE_DETAIL = "archive_messages_detail.pl";
        String PATH_DELETE_FOREVER_MESSAGE_DETAIL = "delete_forever_messages_detail.pl";
        String PATH_DELETE_MESSAGE_DETAIL = "delete_messages_detail.pl";
        String PATH_FLAG_SPAM = "flag_spam.pl";
        String PATH_MOVE_TO_INBOX_DETAIL = "move_to_inbox_detail.pl";
        String PATH_REPLY_MESSAGE = "reply_message.pl";
        String PATH_SEND_MESSAGE = "send_message.pl";
        String PATH_UNDO_ARCHIVE_MESSAGE_DETAIL = "undo_archive_messages_detail.pl";
        String PATH_UNDO_DELETE_MESSAGE_DETAIL = "undo_delete_messages_detail.pl";
        String PATH_UNDO_FLAG_SPAM = "undo_flag_spam.pl";
        String PATH_UNDO_MOVE_TO_INBOX_DETAIL = "undo_move_to_inbox_detail.pl";

        String PATH_DO_VERIFICATION_MSISDN = "do_verification_msisdn.pl";
        String PATH_SEND_EMAIL_CHANGE_PHONE_NUMBER = "send_email_change_phone_number.pl";
        String PATH_SEND_VERIFICATION_OTP = "send_verification_otp.pl";
        String PATH_SKIP_UPDATE = "skip_update.pl";
        String PATH_VALIDATE_EMAIL_CODE = "validate_email_code.pl";

        String PATH_GET_VERIFICATION_NUMBER_FORM = "get_verification_number_form.pl";

        String PATH_GET_NOTIFICATION = "get_notification.pl";
        String PATH_RESET_NOTIFICATION = "reset_notification.pl";

        String PATH_ADD_ADDRESS = "add_address.pl";
        String PATH_ADD_BANK_ACCOUNT = "add_bank_account.pl";
        String PATH_CONFIRM_NEW_EMAIL = "confirm_new_email.pl";
        String PATH_DELETE_ADDRESS = "delete_address.pl";
        String PATH_DELETE_BANK_ACCOUNT = "delete_bank_account.pl";
        String PATH_EDIT_ADDRESS = "edit_address.pl";
        String PATH_EDIT_BANK_ACCOUNT = "edit_bank_account.pl";
        String PATH_EDIT_BIODATA = "edit_biodata.pl";
        String PATH_EDIT_CONTACT = "edit_contact.pl";
        String PATH_EDIT_DEFAULT_ADDRESS = "edit_default_address.pl";
        String PATH_EDIT_DEFAULT_BANK_ACCOUNT = "edit_default_bank_account.pl";
        String PATH_EDIT_NOTIFICATION = "edit_notification.pl";
        String PATH_EDIT_PASSWORD = "edit_password.pl";
        String PATH_EDIT_PRIVACY = "edit_privacy.pl";
        String PATH_EDIT_PROFILE = "edit_profile.pl";
        String PATH_SEND_OTP_EDIT_EMAIL = "send_otp_edit_email.pl";
        String PATH_UPLOAD_PROFILE_PICTURE = "upload_profile_picture.pl";

        String PATH_GET_ADDRESS = "get_address.pl";
        String PATH_GET_BANK_ACCOUNT = "get_bank_account.pl";
        String PATH_GET_DEFAULT_BANK_ACCOUNT = "get_default_bank_account.pl";
        String PATH_GET_FAVORITE_SHOP = "get_favorit_shop.pl";
        String PATH_GET_PEOPLE_INFO = "get_people_info.pl";
        String PATH_GET_PRIVACY = "get_privacy.pl";
        String PATH_GET_PROFILE = "get_profile.pl";
        String PATH_GET_RANDOM_FAV_SHOP = "get_random_fav_shop.pl";
        String PATH_GET_SEARCH_BANK_ACCOUNT = "search_bank_account.pl";

        String PATH_ADD_CATALOG_PRICE_ALERT = "add_catalog_price_alert.pl";
        String PATH_ADD_PRODUCT_PRICE_ALERT = "add_product_price_alert.pl";
        String PATH_DELETE_CATALOG_PRICE_ALERT = "delete_catalog_price_alert.pl";
        String PATH_DELETE_PRODUCT_PRICE_ALERT = "delete_product_price_alert.pl";
        String PATH_EDIT_INBOX_PRICE_ALERT = "edit_inbox_price_alert.pl";
        String PATH_REMOVE_PRODUCT_PRICE_ALERT = "remove_product_price_alert.pl";

        String PATH_DO_REGISTER = "do_register.pl";

        String PATH_LOGIN = "login.pl";
        String PATH_CREATE_PASSWORD = "create_password.pl";
        String PATH_DO_LOGIN = "do_login.pl";
        String PATH_DO_LOGIN_PLUS = "do_login_plus.pl";
        String PATH_LOGOUT = "logout.pl";
        String PATH_BYPASS_LOGIN = "v2_login.pl";
        String PATH_MAKE_LOGIN = "v4/session/make_login.pl";

        String PATH_GIVE_RATING = "give_rating.pl";
        String PATH_OPEN_TICKET = "open_ticket.pl";
        String PATH_REPLY_TICKET_SUBMIT = "reply_ticket_submit.pl";
        String PATH_REPLY_TICKET_VALIDATION = "reply_ticket_validation.pl";

        String PATH_ADD_WISHLIST_PRODUCT = "add_wishlist_product.pl";
        String PATH_REMOVE_WISHLIST_PRODUCT = "remove_wishlist_product.pl";

        String PATH_IS_ALREADY_WISHLIST_PRODUCT = "is_already_wishlist_product.pl";
    }

    interface Shop {
        String URL_MY_SHOP = BASE_DOMAIN + "v4/myshop/";
        String URL_MY_SHOP_ACTION = BASE_DOMAIN + "v4/action/myshop/";
        String URL_MY_SHOP_ADDRESS = BASE_DOMAIN + "v4/myshop-address/";
        String URL_MY_SHOP_ADDRESS_ACTION = BASE_DOMAIN + "v4/action/myshop-address/";
        String URL_MY_SHOP_ETALASE = BASE_DOMAIN + "v4/myshop-etalase/";
        String URL_MY_SHOP_ETALASE_ACTION = BASE_DOMAIN + "v4/action/myshop-etalase/";
        String URL_MY_SHOP_INFO_ACTION = BASE_DOMAIN + "v4/action/myshop-info/";
        String URL_MY_SHOP_INFO = BASE_DOMAIN + "v4/myshop-info/";
        String URL_MY_SHOP_NOTE = BASE_DOMAIN + "v4/myshop-note/";
        String URL_MY_SHOP_NOTE_ACTION = BASE_DOMAIN + "v4/action/myshop-note/";
        String URL_MY_SHOP_ORDER = BASE_DOMAIN + "v4/myshop-order/";
        String URL_MY_SHOP_ORDER_ACTION = BASE_DOMAIN + "v4/action/myshop-order/";
        String URL_MY_SHOP_PAYMENT = BASE_DOMAIN + "v4/myshop-payment/";
        String URL_MY_SHOP_PAYMENT_ACTION = BASE_DOMAIN + "v4/action/myshop-payment/";
        String URL_MY_SHOP_SHIPMENT = BASE_DOMAIN + "v4/myshop-shipment/";
        String URL_MY_SHOP_SHIPMENT_ACTION = BASE_DOMAIN + "v4/action/myshop-shipment/";
        String URL_NOTES = BASE_DOMAIN + "v4/notes/";
        String URL_REPUTATION_ACTION = BASE_DOMAIN + "v4/action/reputation/";
        String URL_SHOP = BASE_DOMAIN + "v4/shop/";
        String URL_SHIPPING_WEBVIEW = BASE_DOMAIN + "v4/web-view/";

        String PATH_CHECK_DOMAIN = "check_domain.pl";
        String PATH_CHECK_SHOP_NAME = "check_shop_name.pl";
        String PATH_OPEN_SHOP_SUBMIT = "open_shop_submit.pl";
        String PATH_OPEN_SHOP_VALIDATION = "open_shop_validation.pl";

        String PATH_ADD_LOCATION = "add_location.pl";
        String PATH_DELETE_LOCATION = "delete_location.pl";
        String PATH_NEW_ORDER_LOCATION = "new_order_location.pl";
        String PATH_EDIT_LOCATION = "edit_location.pl";

        String PATH_GET_LOCATION = "get_location.pl";
        String PATH_GET_OPEN_SHOP_FORM = "get_open_shop_form.pl";

        String PATH_EVENT_SHOP_ADD_ETALASE = "event_shop_add_etalase.pl";
        String PATH_EVENT_SHOP_DELETE_ETALASE = "event_shop_delete_etalase.pl";
        String PATH_EVENT_SHOP_REORDER_ETALASE = "event_shop_reorder_etalase.pl";
        String PATH_EVENT_SHOP_EDIT_ETALASE = "event_shop_edit_etalase.pl";

        String PATH_GET_SHOP_ETALASE = "get_shop_etalase.pl";

        String PATH_UPDATE_SHOP_INFO = "update_shop_info.pl";
        String PATH_UPDATE_SHOP_PICTURE = "update_shop_picture.pl";
        String PATH_UPDATE_SHOP_CLOSE = "update_shop_close.pl";

        String PATH_GET_SHOP_INFO = "get_shop_info.pl";

        String PATH_ADD_SHOP_NOTE = "add_shop_note.pl";
        String PATH_DELETE_SHOP_NOTE = "delete_shop_note.pl";
        String PATH_EDIT_SHOP_NOTE = "edit_shop_note.pl";

        String PATH_GET_SHOP_NOTE = "get_shop_note.pl";

        String PATH_EDIT_SHIPPING_REF = "edit_shipping_ref.pl";
        String PATH_PROCEED_ORDER = "proceed_order.pl";
        String PATH_PROCEED_SHIPPING = "proceed_shipping.pl";

        String PATH_GET_SHIPPING_FORM = "get_edit_shipping_form.pl";
        String PATH_GET_DETAIL_INFO_DETAIL = "get_shipping_detail_info.pl";
        String PATH_GET_ORDER_LIST = "get_order_list.pl";
        String PATH_GET_ORDER_NEW = "get_order_new.pl";
        String PATH_GET_ORDER_PROCESS = "get_order_process.pl";
        String PATH_GET_ORDER_STATUS = "get_order_status.pl";
        String PATH_GET_PROCEED_SHIPPING_FORM = "get_proceed_shipping_form.pl";

        String PATH_UPDATE_PAYMENT_INFO = "update_payment_info.pl";

        String PATH_GET_PAYMENT_INFO = "get_payment_info.pl";

        String PATH_UPDATE_SHIPPING_INFO = "update_shipping_info.pl";

        String PATH_GET_SHIPPING_INFO = "get_shipping_info.pl";

        String PATH_GET_NOTES_DETAIL = "get_notes_detail.pl";

        String PATH_DELETE_REP_REVIEW_RESPONSE = "delete_reputation_review_response.pl";
        String PATH_EDIT_REP_REVIEW = "edit_reputation_review.pl";
        String PATH_INSERT_REP = "insert_reputation.pl";
        String PATH_INSERT_REP_REVIEW = "insert_reputation_review.pl";
        String PATH_INSERT_REP_REVIEW_RESPONSE = "insert_reputation_review_response.pl";
        String PATH_SKIP_REP_REVIEW = "skip_reputation_review.pl";

        String PATH_GET_LIKE_DISLIKE_REVIEW = "get_like_dislike_review_shop.pl";
        String PATH_GET_PEOPLE_FAV_MY_SHOP = "get_people_who_favorite_myshop.pl";
        String PATH_GET_SHOP_LOCATION = "get_shop_location.pl";
        String PATH_GET_SHOP_NOTES = "get_shop_notes.pl";
        String PATH_GET_SHOP_PRODUCT = "get_shop_product.pl";
        String PATH_GET_SHOP_REVIEW = "get_shop_review.pl";
        String PATH_GEt_SHOP_TALK = "get_shop_talk.pl";
    }

    interface Etc {
        String URL_ADDRESS = BASE_DOMAIN + "v4/address/";
        String URL_CONTACT_US = BASE_DOMAIN + "v4/contact-us/";
        String URL_CONTACT_US_ACTION = BASE_DOMAIN + "v4/action/contact-us/";
        String URL_DEPARTMENT = BASE_DOMAIN + "v4/department/";
        String URL_HOME = BASE_DOMAIN + "v4/home/";
        String URL_TICKER = BASE_DOMAIN + "v4/ticker/";

        String PATH_GET_CITY = "get_city.pl";
        String PATH_GET_DISTRICT = "get_district.pl";
        String PATH_GET_PROVINCE = "get_province.pl";
        String PATH_GET_SHIPPING_CITY = "get_shipping_city.pl";

        String PATH_GET_FORM_MODEL_CONTACT_US = "get_form_model_contact_us.pl";
        String PATH_GET_TREE_TICKET_CATEGORY = "get_tree_ticket_category.pl";
        String PATH_CREATE_TICKET = "create_ticket.pl";
        String PATH_CREATE_TICKET_VALIDATION = "create_ticket_validation.pl";


        String PATH_GET_DEPARTMENT_CHILD = "get_department_child.pl";
        String PATH_GET_DEPARTMENT_PARENT = "get_department_parent.pl";

        String PATH_GET_FAVORITE_SHOP = "get_favorite_shop.pl";
        String PATH_GET_PRODUCT_FEED = "get_product_feed.pl";
        String PATH_GET_RECENT_VIEW_PRODUCT = "get_recent_view_product.pl";
        String PATH_GET_WISHLIST = "get_wishlist.pl";

        String PATH_GET_DATA_SOURCE_TICKER = "get_data_source_ticker.pl";
    }

    interface ResCenter {
        String URL_RES_CENTER_ACTION = BASE_DOMAIN + "v4/action/resolution-center/";

        String PATH_ACCEPT_ADMIN_RESOLUTION = "accept_admin_resolution.pl";
        String PATH_ACCEPT_RESOLUTION = "accept_resolution.pl";
        String PATH_CANCEL_RESOLUTION = "cancel_resolution.pl";
        String PATH_CREATE_RES_SUBMIT = "create_resolution_submit.pl";
        String PATH_CREATE_RES_VALIDATION = "create_resolution_validation_new.pl";
        String PATH_EDIT_RESI_RESOLUTION = "edit_resi_resolution.pl";
        String PATH_FINISH_RES_RETURN = "finish_resolution_retur.pl";
        String PATH_INPUT_RESI_RESOLUTION = "input_resi_resolution.pl";
        String PATH_INPUT_ADDRESS_RESOLUTION = "input_address_resolution.pl";
        String PATH_EDIT_ADDRESS_RESOLUTION = "edit_address_resolution.pl";
        String PATH_REJECT_ADMIN_RES_SUBMIT = "reject_admin_resolution_submit.pl";
        String PATH_REJECT_ADMIN_RES_VALIDATION = "reject_admin_resolution_validation_new.pl";
        String PATH_REPLY_CONVERSATION_SUBMIT = "reply_conversation_submit.pl";
        String PATH_REPLY_CONVERSATION_VALIDATION = "reply_conversation_validation.pl";
        String PATH_REPLY_CONVERSATION_VALIDATION_NEW = "reply_conversation_validation_new.pl";
        String PATH_REPORT_REOLUTION = "report_resolution.pl";
        String PATH_RESOLUTION_CENTER = "resolution-center.pl";
    }

    interface Search {
        String URL_CATALOG_SELLER = "search/v1/catalog/product";

        String URL_CATALOG = BASE_DOMAIN + "v4/catalog/";
        String URL_HOT_LIST = BASE_DOMAIN + "v4/hotlist/";
        String URL_SEARCH = BASE_DOMAIN + "v4/search/";

        String URL_SEARCH_SUGGESTION = ACE_DOMAIN;

        String PATH_GET_CATALOG = "get_catalog.pl";
        String PATH_GET_CATALOG_DETAIL = "get_catalog_detail.pl";
        String PATH_GET_SELL_FORM = "get_sell_form.pl";

        String PATH_GET_HOTLIST = "get_hotlist.pl";
        String PATH_GET_HOTLIST_PRODUCT = "get_hotlist_product.pl";
        String PATH_GET_HOTLIST_BANNER = "get_hotlist_banner.pl";

        String PATH_SEARCH_CATALOG = "search_catalog.pl";
        String PATH_SEARCH_PRODUCT = "search_product.pl";
        String PATH_SEARCH_SHOP = "search_shop.pl";
    }

    interface Transaction {
        String URL_DEPOSIT_ACTION = BASE_DOMAIN + "v4/action/deposit/";
        String URL_DEPOSIT = BASE_DOMAIN + "v4/deposit/";
        String URL_DEPOSIT_CLOVER = CLOVER_DOMAIN + "deposit/v4/";
        String URL_TRACKING_ORDER = BASE_DOMAIN + "v4/tracking-order/";
        String URL_TX_ACTION = BASE_DOMAIN + "v4/action/tx/";
        String URL_TX = BASE_DOMAIN + "v4/";
        String URL_TX_CART_ACTION = BASE_DOMAIN + "v4/action/tx-cart/";
        String URL_TX_CART = BASE_DOMAIN + "v4/tx-cart/";
        String URL_TX_ORDER_ACTION = BASE_DOMAIN + "v4/action/tx-order/";
        String URL_TX_ORDER = BASE_DOMAIN + "v4/tx-order/";
        String URL_TX_PAYMENT_BCA_CLIKPAY = BASE_DOMAIN + "v4/";
        String URL_TX_PAYMENT_CC_BC = BASE_DOMAIN + "v4/";
        String URL_TX_PAYMENT_EMONEY = BASE_DOMAIN + "v4/";
        String URL_TX_PAYMENT_SPRINT_ASIA = BASE_DOMAIN + "v4/";
        String URL_TX_PAYMENT_VOUCHER = BASE_DOMAIN + "v4/tx-voucher/";

        String PATH_DO_WITHDRAW = "do_withdraw.pl";
        String PATH_SEND_OTP_VERIFY_BANK_ACCOUNT = "send_otp_verify_bank_account.pl";

        String PATH_GET_DEPOSIT = "get_deposit.pl";
        String PATH_GET_SUMMARY = "get_summary.pl";
        String PATH_GET_WITHDRAW_FORM = "get_withdraw_form.pl";
        String PATH_GET_TOPPOINTS = "get_lp.pl";
        String PATH_GET_TOPPOINTS_CLOVER = "lp";


        String PATH_TRACK_ORDER = "track_order.pl";

        String PATH_STEP_1_PROCESS_CREDIT_CARD = "step_1_process_credit_card.pl";
        String PATH_GET_PARAMETER_DYNAMIC_PAYMENT = "toppay_get_parameter.pl";
        String PATH_THANKS_DYNAMIC_PAYMENT = "toppay_thanks_action.pl";

        String PATH_DO_PAYMENT = "tx.pl";

        String PATH_ADD_TO_CART = "add_to_cart.pl";
        String PATH_CANCEL_CART = "cancel_cart.pl";
        String PATH_EDIT_ADDRESS = "edit_address.pl";
        String PATH_EDIT_INSURANCE = "edit_insurance.pl";
        String PATH_EDIT_PRODUCT = "edit_product.pl";
        String PATH_EDIT_CART = "bulk-edit.pl";

        String PATH_CALCULATE_CART = "calculate_cart.pl";
        String PATH_CALCULATE_CREDIT_CARD = "calculate_credit_card_charge.pl";
        String PATH_CART_SEARCH_ADDRESS = "cart_search_address.pl";
        String PATH_GET_ADD_TO_CART_FORM = "get_add_to_cart_form.pl";
        String PATH_CANCEL_CART_FORM = "get_cancel_cart_form.pl";
        String PATH_GET_EDIT_ADDRESS_SHIPPING_FORM = "get_edit_address_shipping_form.pl";
        String PATH_GET_EDIT_PRODUCT_FORM = "get_edit_product_form.pl";

        String PATH_CANCEL_PAYMENT = "cancel_payment.pl";
        String PATH_CONFIRM_PAYMENT = "confirm_payment.pl";
        String PATH_DELIVERY_CONFIRM = "delivery_confirm.pl";
        String PATH_DELIVERY_FINISH_ORDER = "delivery_finish_order.pl";
        String PATH_DELIVERY_REJECT = "delivery_reject.pl";
        String PATH_EDIT_PAYMENT = "edit_payment.pl";
        String PATH_REQUEST_CANCEL_ORDER = "request_cancel_order.pl";
        String PATH_REORDER = "reorder.pl";
        String PATH_UPLOAD_VALID_PROOF_BY_PAYMENT = "upload_valid_proof_by_payment.pl";

        String PATH_GET_CANCEL_PAYMENT_FORM = "get_cancel_payment_form.pl";
        String PATH_GET_CONFIRM_PAYMENT_FORM = "get_confirm_payment_form.pl";
        String PATH_GET_EDIT_PAYMENT_FORM = "get_edit_payment_form.pl";
        String PATH_GET_REORDER_FORM = "get_reorder_form.pl";
        String PATH_GET_TX_ORDER_DELIVER = "get_tx_order_deliver.pl";
        String PATH_GET_TX_ORDER_LIST = "get_tx_order_list.pl";
        String PATH_GET_TX_ORDER_PAYMENT_CONFIRMATION = "get_tx_order_payment_confirmation.pl";
        String PATH_GET_TX_ORDER_PAYMENT_CONFIRMED = "get_tx_order_payment_confirmed.pl";
        String PATH_GET_TX_ORDER_PAYMENT_CONFIRMED_DETAIL = "get_tx_order_payment_confirmed_detail.pl";
        String PATH_GET_TX_ORDER_STATUS = "get_tx_order_status.pl";
        String PATH_GET_UPLOAD_PROOF_BY_PAYMENT = "get_upload_proof_by_payment_form.pl";

        String PATH_TX_PAYMENT_BCA_KLIKPAY = "tx-payment-bcaklikpay.pl";

        String PATH_TX_PAYMENT_CC_BCA = "tx-payment-cc-bca.pl";

        String PATH_TX_PAYMENT_SPRINTASIA = "tx-payment-sprintasia.pl";

        String PATH_CHECK_VOUCHER_CODE = "check_voucher_code.pl";

        String PATH_TX_PAYMENT_EMONEY = "tx-payment-emoney.pl";
    }

    interface Shipment {
        String PATH_RATES = "rates/v1";
    }

    interface Upload {
        String URL_GENERATE_HOST_ACTION = BASE_DOMAIN + "v4/action/generate-host/";

        String PATH_GENERATE_HOST = "generate_host.pl";

        String URL_UPLOAD_IMAGE_ACTION = BASE_DOMAIN + "v4/action/upload-image-helper/";

        String PATH_CREATE_RESOLUTION_PICTURE = "create_resolution_picture.pl";
        String PATH_ADD_PRODUCT_PICTURE = "add_product_picture.pl";
        String PATH_OPEN_SHOP_PICTURE = "open_shop_picture.pl";
        String PATH_TICKET_PICTURE = "reply_ticket_picture.pl";
        String PATH_CONTACT_IMAGE = "/web-service/v4/action/upload-image/upload_contact_image.pl";
        String PATH_CREATE_RESOLUTION_PICTURE_FULL = "/web-service/v4/action/upload-image-helper/create_resolution_picture.pl";
        String PATH_PROFILE_IMAGE = "/web-service/v4/action/upload-image/upload_profile_image.pl";
    }

    interface Ace {
        String URL_SEARCH = ACE_DOMAIN + "search/";

        String PATH_CATALOG_SHOP_LIST = "catalog/product";
        String PATH_SEARCH_SHOP = "shop";
        String PATH_OTHER_PRODUCT = "v1/product";
        String PATH_FAV_SHOP_FEED = "catalog/product";
        String PATH_CATALOG = "v1/catalog";
        String PATH_TOP_PICKS = "/hoth/toppicks/random";

    }

    interface TopAds {
        String URL_TOPADS = TOPADS_DOMAIN + "promo/v1.1/display/";
        String URL_TOPADS_SHOP = TOPADS_DOMAIN + "promo/v1/display/";

        String PATH_DISPLAY_SHOP = "shops";
    }

    interface Mojito {
        String PATH_USER = "v1.0.2/users/";
        String PATH_PRODUCT = "v1/products/";
        String PATH_CATALOG = "v1/catalogs/";

        String PATH_WISHLIST_PRODUCT = "wishlist/products";
        String PATH_WISHLIST = "wishlist";

        String API_HOME_CATEGORY_MENU = "/api/v1/layout/category";
    }



    interface Recharge {
        String VERSION = "v1.1";
        String RECHARGE = "recharge";

        String PATH_STATUS = VERSION + "/status";
        String PATH_CATEGORY = VERSION + "/category/list";
        String PATH_OPERATOR = VERSION + "/operator/list";
        String PATH_PRODUCT = VERSION + "/product/list";
        String PATH_RECENT_NUMBER =  VERSION + "/recent-number";
        String PATH_LAST_ORDER = VERSION + "/last-order";
        String PATH_SALDO = "/saldo/";
    }

    interface Kunyit {
//        String GET_INBOX_TALK = "/v2/inbox/talks";
//        String GET_PRODUCT_TALK = "/v2/talks/read";
//        String GET_COMMENT_TALK = "/v2/comments/read";
//        String GET_INBOX_TALK_DETAIL = "/v2/inbox/talks/detail";
//        String ADD_PRODUCT_TALK = "/v2/talks/create";
//        String ADD_COMMENT_TALK = "/v2/comments/create";
//        String DELETE_COMMENT_TALK = "/v2/comments/delete";
//        String REPORT_COMMENT_TALK = "/v1/report/talkcomments";
//        String FOLLOW_PRODUCT_TALK = "/v2/talks/follow";
//        String DELETE_PRODUCT_TALK = "/v2/talks/delete";
//        String REPORT_PRODUCT_TALK = "/v1/report/talks";

        String GET_INBOX_TALK = "/v2/talk/inbox";
        String GET_PRODUCT_TALK = "/v2/talk";
        String GET_COMMENT_TALK = "/v2/talk/comment";
        String GET_INBOX_TALK_DETAIL = "/v2/talk/inbox/detail";

        String ADD_PRODUCT_TALK    = "/v2/talk/create";
        String FOLLOW_PRODUCT_TALK = "/v2/talk/follow";
        String DELETE_PRODUCT_TALK = "/v2/talk/delete";
        String REPORT_PRODUCT_TALK = "/v2/talk/report";

        String ADD_COMMENT_TALK    = "/v2/talk/comment/create";
        String DELETE_COMMENT_TALK = "/v2/talk/comment/delete";
        String REPORT_COMMENT_TALK = "/v2/talk/comment/report";

    }
    interface Accounts{
        String PATH_GET_TOKEN = "token";
        String PATH_GET_INFO = "info";
        String PATH_GET_PROFILE = "profile/json/{id}";
        String PATH_DISCOVER_LOGIN = "api/discover";
        String DO_REGISTER = "api/register";
        String CREATE_PASSWORD = "api/create-password";
        String RESET_PASSWORD = "api/reset";
        String VALIDATE_EMAIL = "/api/register/validate-email";
        String RESENT_ACTIVATION = "/api/resend";
    }

    interface Home{
        String PATH_API_V1_ANNOUNCEMENT_TICKER = "/api/v1/tickers";
    }

    interface GoldMerchant{
        String GET_PRODUCT_VIDEO = "/v1/product/video/";
    }

    interface FCM{
        String UPDATE_FCM = "/api/gcm/update";
    }
}
