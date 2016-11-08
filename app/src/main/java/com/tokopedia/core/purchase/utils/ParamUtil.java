package com.tokopedia.core.purchase.utils;

import com.tokopedia.core.purchase.model.ConfirmPaymentData;

import java.util.HashMap;
import java.util.Map;

/**
 * ParamUtil
 * Created by Angga.Prasetiyo on 23/06/2016.
 */
public class ParamUtil {
    private static final String TAG = ParamUtil.class.getSimpleName();

    private static final String PARAM_BANK_ACC_BRANCH = "bank_account_branch";
    private static final String PARAM_BANK_ACC_ID = "bank_account_id";
    private static final String PARAM_BANK_ACC_NAME = "bank_account_name";
    private static final String PARAM_BANK_ACC_NUMBER = "bank_account_number";
    private static final String PARAM_BANK_ID = "bank_id";
    private static final String PARAM_BANK_NAME = "bank_name";
    private static final String PARAM_COMMENTS = "comments";
    private static final String PARAM_CONFIRMATION_ID = "confirmation_id";
    private static final String PARAM_DEPOSITOR = "depositor";
    private static final String PARAM_FILE_NAME = "file_name";
    private static final String PARAM_FILE_PATH = "file_path";
    private static final String PARAM_METHOD_ID = "method_id";
    private static final String PARAM_PASSWORD = "password";
    private static final String PARAM_PASSWORD_DEPOSIT = "password_deposit";
    private static final String PARAM_PAYMENT_AMOUNT = "payment_amount";
    private static final String PARAM_PAYMENT_DAY = "payment_day";
    private static final String PARAM_PAYMENT_MONTH = "payment_month";
    private static final String PARAM_PAYMENT_YEAR = "payment_year";
    private static final String PARAM_PAYMENT_ID = "payment_id";
    private static final String PARAM_SERVER_ID = "server_id";
    private static final String PARAM_SYSBANK_ID = "sysbank_id";
    private static final String PARAM_TOKEN = "token";
    private static final String PARAM_USER_ID = "user_id";

    public static Map<String, String> getParamPaymentConfirmation(ConfirmPaymentData data,
                                                                  Map<String, String> params) {
        params.put(PARAM_COMMENTS, data.getComments());
        params.put(PARAM_DEPOSITOR, data.getDepositor());
        params.put(PARAM_PAYMENT_AMOUNT, data.getPaymentAmount().replace(",", ""));
        params.put(PARAM_PAYMENT_DAY, data.getPaymentDay());
        params.put(PARAM_METHOD_ID, data.getPaymentMethod());
        params.put(PARAM_PAYMENT_MONTH, data.getPaymentMonth());
        params.put(PARAM_PAYMENT_YEAR, data.getPaymentYear());
        params.put(PARAM_BANK_ACC_ID, data.getBankAccountId());
        params.put(PARAM_BANK_ACC_NAME, data.getBankAccountName());
        params.put(PARAM_BANK_ACC_NUMBER, data.getBankAccountNumber());
        params.put(PARAM_BANK_ID, data.getBankId());
        params.put(PARAM_BANK_NAME, data.getBankName());
        params.put(PARAM_BANK_ACC_BRANCH, data.getBankAccountBranch());
        params.put(PARAM_PASSWORD_DEPOSIT, data.getPasswordDeposit());
        params.put(PARAM_SYSBANK_ID, data.getSysBankId());
        return params;
    }

    public static Map<String, String> generateParamEditPayment(ConfirmPaymentData data) {

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PAYMENT_ID, data.getPaymentId());

        return getParamPaymentConfirmation(data, params);
    }

    public static Map<String, String> generateParamConfirmPayment(ConfirmPaymentData data) {

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_CONFIRMATION_ID, data.getPaymentId());
        params.put(PARAM_TOKEN, data.getToken());

        return getParamPaymentConfirmation(data, params);
    }
}
