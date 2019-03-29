package com.tokopedia.pms.payment.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.pms.payment.view.adapter.PaymentListAdapterTypeFactory;

import java.util.List;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public class PaymentListModel implements Visitable<PaymentListAdapterTypeFactory>, Parcelable {
    private String transactionId;
    private String transaction_date;
    private boolean showTickerMessage;
    private String tickerMessage;
    private String productImage;
    private String productName;
    private List<String> listOfAction;
    private boolean showCancelTransaction;
    private boolean showSeeDetail;
    private String invoiceUrl;
    private String paymentImage;
    private int paymentAmount;
    private boolean showContainerDetailBankTransfer;
    private String destAccountNo;
    private String destAccountName;
    private String userAccountNo;
    private String userAccountName;
    private boolean showDynamicLabelDetailPayment;
    private String labelDynamicViewDetailPayment;
    private String valueDynamicViewDetailPayment;
    private String paymentMethod;
    private String merchantCode;
    private String appLink;
    private String bankId;
    private boolean showHowToPay;

    @Override
    public int type(PaymentListAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setTransactionDate(String transactionDate) {
        this.transaction_date = transactionDate;
    }

    public void setShowTickerMessage(boolean showTickerMessage) {
        this.showTickerMessage = showTickerMessage;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public void setTickerMessage(String tickerMessage) {
        this.tickerMessage = tickerMessage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setListOfAction(List<String> listOfAction) {
        this.listOfAction = listOfAction;
    }

    public void setShowCancelTransaction(boolean showCancelTransaction) {
        this.showCancelTransaction = showCancelTransaction;
    }

    public void setShowSeeDetail(boolean showSeeDetail) {
        this.showSeeDetail = showSeeDetail;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setShowContainerDetailBankTransfer(boolean showContainerDetailBankTransfer) {
        this.showContainerDetailBankTransfer = showContainerDetailBankTransfer;
    }

    public void setDestAccountNo(String destAccountNo) {
        this.destAccountNo = destAccountNo;
    }

    public void setDestAccountName(String destAccountName) {
        this.destAccountName = destAccountName;
    }

    public void setUserAccountNo(String userAccountNo) {
        this.userAccountNo = userAccountNo;
    }

    public void setUserAccountName(String userAccountName) {
        this.userAccountName = userAccountName;
    }

    public void setShowDynamicViewDetailPayment(boolean showDynamicLabelDetailPayment) {
        this.showDynamicLabelDetailPayment = showDynamicLabelDetailPayment;
    }

    public void setLabelDynamicViewDetailPayment(String labelDynamicViewDetailPayment) {
        this.labelDynamicViewDetailPayment = labelDynamicViewDetailPayment;
    }

    public void setValueDynamicViewDetailPayment(String valueDynamicViewDetailPayment) {
        this.valueDynamicViewDetailPayment = valueDynamicViewDetailPayment;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public boolean isShowTickerMessage() {
        return showTickerMessage;
    }

    public String getTickerMessage() {
        return tickerMessage;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public List<String> getListOfAction() {
        return listOfAction;
    }

    public boolean isShowCancelTransaction() {
        return showCancelTransaction;
    }

    public boolean isShowSeeDetail() {
        return showSeeDetail;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public boolean isShowContainerDetailBankTransfer() {
        return showContainerDetailBankTransfer;
    }

    public String getDestAccountNo() {
        return destAccountNo;
    }

    public String getDestAccountName() {
        return destAccountName;
    }

    public String getUserAccountNo() {
        return userAccountNo;
    }

    public String getUserAccountName() {
        return userAccountName;
    }

    public boolean isShowDynamicLabelDetailPayment() {
        return showDynamicLabelDetailPayment;
    }

    public String getLabelDynamicViewDetailPayment() {
        return labelDynamicViewDetailPayment;
    }

    public String getValueDynamicViewDetailPayment() {
        return valueDynamicViewDetailPayment;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public PaymentListModel() {
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public boolean isShowHowToPay() {
        return showHowToPay;
    }

    public void setShowHowToPay(boolean showHowToPay) {
        this.showHowToPay = showHowToPay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transactionId);
        dest.writeString(this.transaction_date);
        dest.writeByte(this.showTickerMessage ? (byte) 1 : (byte) 0);
        dest.writeString(this.tickerMessage);
        dest.writeString(this.productImage);
        dest.writeString(this.productName);
        dest.writeStringList(this.listOfAction);
        dest.writeByte(this.showCancelTransaction ? (byte) 1 : (byte) 0);
        dest.writeByte(this.showSeeDetail ? (byte) 1 : (byte) 0);
        dest.writeString(this.invoiceUrl);
        dest.writeString(this.paymentImage);
        dest.writeInt(this.paymentAmount);
        dest.writeByte(this.showContainerDetailBankTransfer ? (byte) 1 : (byte) 0);
        dest.writeString(this.destAccountNo);
        dest.writeString(this.destAccountName);
        dest.writeString(this.userAccountNo);
        dest.writeString(this.userAccountName);
        dest.writeByte(this.showDynamicLabelDetailPayment ? (byte) 1 : (byte) 0);
        dest.writeString(this.labelDynamicViewDetailPayment);
        dest.writeString(this.valueDynamicViewDetailPayment);
        dest.writeString(this.paymentMethod);
        dest.writeString(this.merchantCode);
        dest.writeString(this.appLink);
        dest.writeString(this.bankId);
        dest.writeByte(this.showHowToPay ? (byte) 1 : (byte) 0);
    }

    protected PaymentListModel(Parcel in) {
        this.transactionId = in.readString();
        this.transaction_date = in.readString();
        this.showTickerMessage = in.readByte() != 0;
        this.tickerMessage = in.readString();
        this.productImage = in.readString();
        this.productName = in.readString();
        this.listOfAction = in.createStringArrayList();
        this.showCancelTransaction = in.readByte() != 0;
        this.showSeeDetail = in.readByte() != 0;
        this.invoiceUrl = in.readString();
        this.paymentImage = in.readString();
        this.paymentAmount = in.readInt();
        this.showContainerDetailBankTransfer = in.readByte() != 0;
        this.destAccountNo = in.readString();
        this.destAccountName = in.readString();
        this.userAccountNo = in.readString();
        this.userAccountName = in.readString();
        this.showDynamicLabelDetailPayment = in.readByte() != 0;
        this.labelDynamicViewDetailPayment = in.readString();
        this.valueDynamicViewDetailPayment = in.readString();
        this.paymentMethod = in.readString();
        this.merchantCode = in.readString();
        this.appLink = in.readString();
        this.bankId = in.readString();
        this.showHowToPay = in.readByte() != 0;
    }

    public static final Creator<PaymentListModel> CREATOR = new Creator<PaymentListModel>() {
        @Override
        public PaymentListModel createFromParcel(Parcel source) {
            return new PaymentListModel(source);
        }

        @Override
        public PaymentListModel[] newArray(int size) {
            return new PaymentListModel[size];
        }
    };
}
