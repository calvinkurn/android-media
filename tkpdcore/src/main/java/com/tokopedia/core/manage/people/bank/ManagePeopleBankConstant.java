package com.tokopedia.core.manage.people.bank;

/**
 * Created by Nisie on 6/13/16.
 */
public interface ManagePeopleBankConstant {

    String INTENT_NAME = "ManagePeopleBankService";

    String EXTRA_ERROR = "EXTRA_ERROR";
    String EXTRA_SUCCESS = "EXTRA_SUCCESS";

    String EXTRA_TYPE = "EXTRA_TYPE";
    String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    String EXTRA_RESULT = "EXTRA_RESULT";

    int STATUS_SUCCESS = 1;
    int STATUS_ERROR = 2;


    int ACTION_ADD_BANK_ACCOUNT = 931;
    int ACTION_EDIT_BANK_ACCOUNT = 932;
    int ACTION_DELETE_BANK_ACCOUNT = 933;
    int ACTION_EDIT_DEFAULT_BANK_ACCOUNT = 934;

    String PARAM_IS_PHONE_VERIFIED = "PARAM_IS_PHONE_VERIFIED";

    int EXPIRE_TIME = 30;
    String SEND_OTP_SETTING_BANK_CACHE_KEY = "SEND_OTP_SETTING_BANK_CACHE_KEY";
    String EXPIRED_TIME_KEY = "expired_time";
    String TIMESTAMP_KEY = "timestamp";

    String PARAM_ADD_BANK_ACCOUNT = "PARAM_ADD_BANK_ACCOUNT";
    String PARAM_EDIT_BANK_ACCOUNT = "PARAM_ADD_BANK_ACCOUNT";
    String PARAM_DELETE_BANK_ACCOUNT = "PARAM_ADD_BANK_ACCOUNT";
    String PARAM_DEFAULT_BANK_ACCOUNT = "PARAM_ADD_BANK_ACCOUNT";




}
