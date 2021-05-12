
package com.tokopedia.pms.payment.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentListInside {

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("transaction_expire")
    @Expose
    private String transactionExpire;
    @SerializedName("transaction_expire_unix")
    @Expose
    private Long transactionExpireUnix;
    @SerializedName("merchant_code")
    @Expose
    private String merchantCode;
    @SerializedName("payment_amount")
    @Expose
    private int paymentAmount;
    @SerializedName("invoice_url")
    @Expose
    private String invoiceUrl;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_img")
    @Expose
    private String productImg;
    @SerializedName("gateway_name")
    @Expose
    private String gatewayName;
    @SerializedName("gateway_img")
    @Expose
    private String gatewayImg;
    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("is_va")
    @Expose
    private boolean isVa;
    @SerializedName("is_klikbca")
    @Expose
    private boolean isKlikbca;
    @SerializedName("bank_img")
    @Expose
    private String bankImg;
    @SerializedName("user_bank_account")
    @Expose
    private UserBankAccount userBankAccount;
    @SerializedName("dest_bank_account")
    @Expose
    private DestBankAccount destBankAccount;
    @SerializedName("show_upload_button")
    @Expose
    private boolean showUploadButton;
    @SerializedName("show_edit_transfer_button")
    @Expose
    private boolean showEditTransferButton;
    @SerializedName("show_edit_klikbca_button")
    @Expose
    private boolean showEditKlikbcaButton;
    @SerializedName("show_cancel_button")
    @Expose
    private boolean showCancelButton;
    @SerializedName("show_expire_ticker")
    @Expose
    private boolean showExpireTicker;
    @SerializedName("show_help_page")
    @Expose
    private boolean showHelpPage;
    @SerializedName("ticker_message")
    @Expose
    private String tickerMessage;
    @SerializedName("app_link")
    @Expose
    private String appLink;

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionExpire() {
        return transactionExpire;
    }

    public void setTransactionExpire(String transactionExpire) {
        this.transactionExpire = transactionExpire;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getGatewayImg() {
        return gatewayImg;
    }

    public void setGatewayImg(String gatewayImg) {
        this.gatewayImg = gatewayImg;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public boolean isIsVa() {
        return isVa;
    }

    public void setIsVa(boolean isVa) {
        this.isVa = isVa;
    }

    public boolean isIsKlikbca() {
        return isKlikbca;
    }

    public void setIsKlikbca(boolean isKlikbca) {
        this.isKlikbca = isKlikbca;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }

    public UserBankAccount getUserBankAccount() {
        return userBankAccount;
    }

    public void setUserBankAccount(UserBankAccount userBankAccount) {
        this.userBankAccount = userBankAccount;
    }

    public DestBankAccount getDestBankAccount() {
        return destBankAccount;
    }

    public void setDestBankAccount(DestBankAccount destBankAccount) {
        this.destBankAccount = destBankAccount;
    }

    public boolean isShowUploadButton() {
        return showUploadButton;
    }

    public void setShowUploadButton(boolean showUploadButton) {
        this.showUploadButton = showUploadButton;
    }

    public boolean isShowEditTransferButton() {
        return showEditTransferButton;
    }

    public void setShowEditTransferButton(boolean showEditTransferButton) {
        this.showEditTransferButton = showEditTransferButton;
    }

    public boolean isShowEditKlikbcaButton() {
        return showEditKlikbcaButton;
    }

    public void setShowEditKlikbcaButton(boolean showEditKlikbcaButton) {
        this.showEditKlikbcaButton = showEditKlikbcaButton;
    }

    public boolean isShowCancelButton() {
        return showCancelButton;
    }

    public void setShowCancelButton(boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
    }

    public boolean isShowExpireTicker() {
        return showExpireTicker;
    }

    public void setShowExpireTicker(boolean showExpireTicker) {
        this.showExpireTicker = showExpireTicker;
    }

    public boolean isShowHelpPage() {
        return showHelpPage;
    }

    public void setShowHelpPage(boolean showHelpPage) {
        this.showHelpPage = showHelpPage;
    }

    public String getTickerMessage() {
        return tickerMessage;
    }

    public void setTickerMessage(String tickerMessage) {
        this.tickerMessage = tickerMessage;
    }

    public Long getTransactionExpireUnix() {
        return transactionExpireUnix;
    }

    public void setTransactionExpireUnix(Long transactionExpireUnix) {
        this.transactionExpireUnix = transactionExpireUnix;
    }
}
