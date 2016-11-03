package com.tokopedia.tkpd.purchase.model;

/**
 * Created by herdimac on 4/8/16.
 */
public class TxPaymentItem {

    private String idPayment;
    private String comments;
    private String depositor;
    private String paymentAmount;
    private String paymentDay;
    private String paymentMethod;
    private String paymentMonth;
    private String paymentYear;
    private String token;
    private String accId;
    private String accName;
    private String accNo;
    private String bankId;
    private String bankName;
    private String bankBranch;
    private String depositPwd;
    private String sysBank;

    public void setAccId(String accId) {
        this.accId = accId;
    }

    public String getAccId() {
        return accId;
    }

    public String getIdPayment() {
        return idPayment;
    }

    public void setIdPayment(String idPayment) {
        this.idPayment = idPayment;
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

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public void setSysBank(String sysBank) {
        this.sysBank = sysBank;
    }

    public String getSysBank() {
        return sysBank;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setPaymentYear(String paymentYear) {
        this.paymentYear = paymentYear;
    }

    public String getPaymentYear() {
        return paymentYear;
    }

    public String getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDepositor() {
        return depositor;
    }

    public void setDepositor(String depositor) {
        this.depositor = depositor;
    }

    public String getDepositPwd() {
        return depositPwd;
    }

    public void setDepositPwd(String depositPwd) {
        this.depositPwd = depositPwd;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(String paymentDay) {
        this.paymentDay = paymentDay;
    }
}
