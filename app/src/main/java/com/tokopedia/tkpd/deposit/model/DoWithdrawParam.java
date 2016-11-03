package com.tokopedia.tkpd.deposit.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 4/13/16.
 */
public class DoWithdrawParam {

    private static final String PARAM_BANK_ACCOUNT_ID = "bank_account_id";
    private static final String PARAM_BANK_ACCOUNT_NAME = "bank_account_name";
    private static final String PARAM_BANK_ACCOUNT_NUMBER = "bank_account_number";
    private static final String PARAM_BANK_BRANCH = "bank_branch";
    private static final String PARAM_BANK_ID = "bank_id";
    private static final String PARAM_BANK_NAME = "bank_name";
    private static final String PARAM_OTP_CODE = "otp_code";
    private static final String PARAM_USER_PASSWORD = "user_password";
    private static final String PARAM_WITHDRAW_AMOUNT = "withdraw_amount";

    String bankAccountId;
    String bankAccountName;
    String bankAccountNumber;
    String bankBranch;
    String bankId;
    String bankName;
    String otpCode;
    String withdrawAmount;
    String userPassword;
    String destination;


    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Map<String, String> getParamDoWithdraw() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_BANK_ACCOUNT_ID, bankAccountId);
        param.put(PARAM_BANK_ACCOUNT_NAME, bankAccountName);
        param.put(PARAM_BANK_ACCOUNT_NUMBER, bankAccountNumber);
        param.put(PARAM_BANK_BRANCH, bankBranch);
        param.put(PARAM_BANK_ID, bankId);
        param.put(PARAM_BANK_NAME, bankName);
        param.put(PARAM_USER_PASSWORD, userPassword);
        param.put(PARAM_WITHDRAW_AMOUNT, withdrawAmount);
        param.put(PARAM_OTP_CODE, otpCode);
        return param;
    }
}
