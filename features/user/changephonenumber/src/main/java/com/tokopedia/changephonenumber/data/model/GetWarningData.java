package com.tokopedia.changephonenumber.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningData {
    @SerializedName("is_success")
    @Expose
    private int isSuccess;
    @SerializedName("warning")
    @Expose
    private List<String> warning;
    @SerializedName("saldo")
    @Expose
    private String saldo;
    @SerializedName("tokocash")
    @Expose
    private String tokocash;
    @SerializedName("saldo_number")
    @Expose
    private long saldoNumber;
    @SerializedName("tokocash_number")
    @Expose
    private long tokocashNumber;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("have_bank_acct")
    @Expose
    private boolean hasBankAccount;
    @SerializedName("is_ovo_eligible")
    @Expose
    private boolean isOvoEligible;
    @SerializedName("is_ovo_primary")
    @Expose
    private boolean isOvoPrimary;
    @SerializedName("is_ovo_activated")
    @Expose
    private boolean isOvoActivated;
    @SerializedName("redirect_url")
    @Expose
    private String redirectUrl;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<String> getWarning() {
        return warning;
    }

    public void setWarning(List<String> warning) {
        this.warning = warning;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getTokocash() {
        return tokocash;
    }

    public void setTokocash(String tokocash) {
        this.tokocash = tokocash;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean getHasBankAccount() {
        return hasBankAccount;
    }

    public void setHasBankAccount(boolean hasBankAccount) {
        this.hasBankAccount = hasBankAccount;
    }

    public long getSaldoNumber() {
        return saldoNumber;
    }

    public void setSaldoNumber(long saldoNumber) {
        this.saldoNumber = saldoNumber;
    }

    public long getTokocashNumber() {
        return tokocashNumber;
    }

    public void setTokocashNumber(long tokocashNumber) {
        this.tokocashNumber = tokocashNumber;
    }

    public boolean isHasBankAccount() {
        return hasBankAccount;
    }

    public boolean isOvoEligible() {
        return isOvoEligible;
    }

    public void setOvoEligible(boolean ovoEligible) {
        isOvoEligible = ovoEligible;
    }

    public boolean isOvoPrimary() {
        return isOvoPrimary;
    }

    public void setOvoPrimary(boolean ovoPrimary) {
        isOvoPrimary = ovoPrimary;
    }

    public boolean isOvoActivated() {
        return isOvoActivated;
    }

    public void setOvoActivated(boolean ovoActivated) {
        isOvoActivated = ovoActivated;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
