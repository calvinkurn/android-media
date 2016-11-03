package com.tokopedia.tkpd.inboxreputation;

/**
 * Created by Nisie on 6/9/16.
 */
public interface InboxReputationConstant {

    String PARAM_NAV = "nav";
    String PARAM_ACTION = "action";

    int CODE_OPEN_DETAIL_REPUTATION = 100;
    int CODE_OPEN_DETAIL_PRODUCT_REVIEW = 1;
    int CODE_OPEN_FORM_PRODUCT_REVIEW = 2;

    String ALL = "all";
    String UNREAD = "unread";
    String ACTION_UPDATE_REPUTATION = "update_reputation";
    String IS_SUCCESS = "is_success";
    String PARAM_INVOICE_NUM = "invoice";
    String PARAM_POSITION = "position";

    String ACT_GET_REPUTATION = "get_inbox_reputation";

    String BUNDLE_INBOX_REPUTATION = "data";
    String BUNDLE_POSITION = "position";
    String DEFAULT_MSG_ERROR = "Mohon maaf, mohon coba kembali";

    String ACTION_UPDATE_PRODUCT = "update_product";
    int BUYER = 1;
    int SELLER = 2;

    String NAV_INBOX_REPUTATION = "inbox-reputation";

    String INTENT_NAME = "ReputationService";

    String EXTRA_TYPE = "EXTRA_TYPE";
    String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    String EXTRA_RESULT = "EXTRA_RESULT";

    int STATUS_SUCCESS = 1;
    int STATUS_ERROR = 2;


    int ACTION_POST_REPUTATION = 333;
    int ACTION_POST_RESPONSE = 334;
    int ACTION_SKIP_REVIEW = 335;
    int ACTION_DELETE_RESPONSE = 336;
    int ACTION_POST_REVIEW = 337;
    int ACTION_EDIT_REVIEW = 338;
    int ACTION_POST_REPORT = 339;

    String PARAM_POST_REPUTATION = "PARAM_POST_REPUTATION";
    String PARAM_POST_RESPONSE = "PARAM_POST_RESPONSE";
    String PARAM_SKIP_REVIEW = "PARAM_SKIP_REVIEW";
    String PARAM_DELETE_RESPONSE = "PARAM_DELETE_RESPONSE";
    String PARAM_POST_REVIEW = "PARAM_POST_REVIEW";
    String PARAM_EDIT_REVIEW = "PARAM_EDIT_REVIEW";
    String PARAM_POST_REPORT = "PARAM_POST_REPORT";

    String EXTRA_ERROR = "EXTRA_ERROR";
    String EXTRA_PRODUCT_POSITION = "EXTRA_PRODUCT_POSITION";
    String EXTRA_SMILEY = "EXTRA_SMILEY";
    String EXTRA_REPUTATION_ID = "EXTRA_REPUTATION_ID";
}
