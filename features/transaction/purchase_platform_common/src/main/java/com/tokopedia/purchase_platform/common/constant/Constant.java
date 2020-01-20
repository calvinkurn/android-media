package com.tokopedia.purchase_platform.common.constant;

/**
 * Created by Irfan Khoirul on 04/01/19.
 */

public interface Constant {

    String EXTRA_CHECKOUT_REQUEST = "EXTRA_CHECKOUT_REQUEST";

    String CHECKOUT_TYPE_DEFAULT = "ncf";
    String CHECKOUT_TYPE_OCS = "ocs";
    String CHECKOUT_TYPE_EXPRESS = "express";

    int REQUEST_CODE_ATC_EXPRESS = 10;
    int RESULT_CODE_ERROR = -10;
    int RESULT_CODE_NAVIGATE_TO_OCS = 20;
    int RESULT_CODE_NAVIGATE_TO_NCF = 30;

    String EXTRA_MESSAGES_ERROR = "EXTRA_MESSAGES_ERROR";

    String EXTRA_ATC_REQUEST = "EXTRA_ATC_REQUEST";
    String TRACKER_ATTRIBUTION = "tracker_attribution";
    String TRACKER_LIST_NAME = "tracker_list_name";

}
