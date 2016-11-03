
package com.tokopedia.tkpd.cart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartModel {

    @SerializedName("checkout_notif_button")
    @Expose
    private Integer checkoutNotifButton;
    @SerializedName("cashback_idr")
    @Expose
    private String cashbackIdr;
    @SerializedName("lp_amount_idr")
    @Expose
    private String lpAmountIdr;
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("gateway_list")
    @Expose
    private List<GatewayList> gatewayList = new ArrayList<GatewayList>();
    @SerializedName("deposit")
    @Expose
    private Integer deposit;
    @SerializedName("deposit_idr")
    @Expose
    private String depositIdr;
    @SerializedName("list")
    @Expose
    private List<TransactionList> transactionLists = new ArrayList<TransactionList>();
    @SerializedName("checkout_notif_error")
    @Expose
    private String checkoutNotifError;
    @SerializedName("ecash_flag")
    @Expose
    private String ecashFlag;
    @SerializedName("not_empty")
    @Expose
    private Integer notEmpty;
    @SerializedName("voucher_code")
    @Expose
    private String voucherCode;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("dropship_list")
    @Expose
    private Object dropshipList;
    @SerializedName("credit_card")
    @Expose
    private CreditCard creditCard;
    @SerializedName("grand_total_idr")
    @Expose
    private String grandTotalIdr;
    @SerializedName("cashback")
    @Expose
    private int cashback;
    @SerializedName("grand_total")
    @Expose
    private Integer grandTotal;
    @SerializedName("lp_amount")
    @Expose
    private int lpAmount;
    @SerializedName("grand_total_without_lp")
    @Expose
    private String grandTotalWithoutLP;
    @SerializedName("grand_total_without_lp_idr")
    @Expose
    private String grandTotalWithoutLPIDR;

    public String getGrandTotalWithoutLP() {
        return grandTotalWithoutLP;
    }

    public void setGrandTotalWithoutLP(String grandTotalWithoutLP) {
        this.grandTotalWithoutLP = grandTotalWithoutLP;
    }

    public Integer getCheckoutNotifButton() {
        return checkoutNotifButton;
    }

    public void setCheckoutNotifButton(Integer checkoutNotifButton) {
        this.checkoutNotifButton = checkoutNotifButton;
    }

    public String getCashbackIdr() {
        return cashbackIdr;
    }

    public void setCashbackIdr(String cashbackIdr) {
        this.cashbackIdr = cashbackIdr;
    }

    public String getLpAmountIdr() {
        return lpAmountIdr;
    }

    public void setLpAmountIdr(String lpAmountIdr) {
        this.lpAmountIdr = lpAmountIdr;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public List<GatewayList> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<GatewayList> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public String getDepositIdr() {
        return depositIdr;
    }

    public void setDepositIdr(String depositIdr) {
        this.depositIdr = depositIdr;
    }

    public List<TransactionList> getTransactionLists() {
        return transactionLists;
    }

    public void setTransactionLists(List<TransactionList> list) {
        this.transactionLists = list;
    }

    public String getCheckoutNotifError() {
        return checkoutNotifError;
    }

    public void setCheckoutNotifError(String checkoutNotifError) {
        this.checkoutNotifError = checkoutNotifError;
    }

    public String getEcashFlag() {
        return ecashFlag;
    }

    public void setEcashFlag(String ecashFlag) {
        this.ecashFlag = ecashFlag;
    }

    public Integer getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(Integer notEmpty) {
        this.notEmpty = notEmpty;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getDropshipList() {
        return dropshipList;
    }

    public void setDropshipList(Object dropshipList) {
        this.dropshipList = dropshipList;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getGrandTotalIdr() {
        return grandTotalIdr;
    }

    public void setGrandTotalIdr(String grandTotalIdr) {
        this.grandTotalIdr = grandTotalIdr;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }

    public Integer getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Integer grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getLpAmount() {
        return lpAmount;
    }

    public void setLpAmount(int lpAmount) {
        this.lpAmount = lpAmount;
    }

    public String getGrandTotalWithoutLPIDR() {
        return grandTotalWithoutLPIDR;
    }

    public void setGrandTotalWithoutLPIDR(String grandTotalWithoutLPIDR) {
        this.grandTotalWithoutLPIDR = grandTotalWithoutLPIDR;
    }
}
