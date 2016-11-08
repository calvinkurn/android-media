package com.tokopedia.core.payment.model.responsecartstep1;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Transaction
 * Created by Angga.Prasetiyo on 05/07/2016.
 */
public class Transaction implements Parcelable {
    @SerializedName("installment_bank_option")
    @Expose
    private List<InstallmentBankOption> installmentBankOptionList = new ArrayList<>();
    @SerializedName("conversion_fee_info")
    @Expose
    private String conversionFeeInfo;
    @SerializedName("voucher_amount_idr")
    @Expose
    private String voucherAmountIdr;
    @SerializedName("deposit_after")
    @Expose
    private String depositAfter;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("payment_left_idr")
    @Expose
    private String paymentLeftIdr;
    @SerializedName("lp_amount")
    @Expose
    private String lpAmount;
    @SerializedName("confirmation_id")
    @Expose
    private String confirmationId;
    @SerializedName("cashback_idr")
    @Expose
    private String cashbackIdr;
    @SerializedName("step")
    @Expose
    private String step;
    @SerializedName("deposit_left")
    @Expose
    private String depositLeft;
    @SerializedName("use_deposit")
    @Expose
    private Integer useDeposit;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("bca_param")
    @Expose
    private BCAParam bcaParam;
    @SerializedName("use_otp")
    @Expose
    private Integer useOtp;
    @SerializedName("installment_bank")
    @Expose
    private String installmentBank;
    @SerializedName("cashback")
    @Expose
    private String cashback;
    @SerializedName("now_time")
    @Expose
    private String nowTime;
    @SerializedName("emoney_code")
    @Expose
    private String emoneyCode;
    @SerializedName("unik")
    @Expose
    private String unik;
    @SerializedName("grand_total_idr")
    @Expose
    private String grandTotalIdr;
    @SerializedName("credit_card")
    @Expose
    private CreditCard creditCard;
    @SerializedName("deposit_amount_idr")
    @Expose
    private String depositAmountIdr;

    @SerializedName("max_conversion_limit")
    @Expose
    private Integer maxConversionLimit;
    @SerializedName("discount_gateway_idr")
    @Expose
    private String discountGatewayIdr;
    @SerializedName("user_deposit_idr")
    @Expose
    private String userDepositIdr;
    @SerializedName("carts")
    @Expose
    private List<Cart> carts = new ArrayList<Cart>();
    @SerializedName("msisdn_verified")
    @Expose
    private Integer msisdnVerified;
    @SerializedName("gateway")
    @Expose
    private String gateway;
    @SerializedName("lp_amount_idr")
    @Expose
    private String lpAmountIdr;
    @SerializedName("conversion_info")
    @Expose
    private String conversionInfo;
    @SerializedName("conf_code")
    @Expose
    private String confCode;
    @SerializedName("indomaret")
    @Expose
    private Indomaret indomaret;
    @SerializedName("bri_website_link")
    @Expose
    private String briWebsiteLink;
    @SerializedName("conf_due_date")
    @Expose
    private String confDueDate;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("processing")
    @Expose
    private String processing;
    @SerializedName("grand_total_before_fee_idr")
    @Expose
    private String grandTotalBeforeFeeIdr;
    @SerializedName("discount_gateway")
    @Expose
    private String discountGateway;
    @SerializedName("gateway_name")
    @Expose
    private String gatewayName;
    @SerializedName("status_unik")
    @Expose
    private String statusUnik;
    @SerializedName("user_deposit")
    @Expose
    private Integer userDeposit;
    @SerializedName("transaction_code")
    @Expose
    private String transactionCode;
    @SerializedName("lock_mandiri")
    @Expose
    private String lockMandiri;
    @SerializedName("deposit_amount")
    @Expose
    private String depositAmount;
    @SerializedName("voucher_amount")
    @Expose
    private String voucherAmount;
    @SerializedName("grand_total_before_fee")
    @Expose
    private String grandTotalBeforeFee;
    @SerializedName("conf_code_idr")
    @Expose
    private String confCodeIdr;
    @SerializedName("payment_left")
    @Expose
    private String paymentLeft;
    @SerializedName("klikbca_user")
    @Expose
    private String klikbcaUser;

    public String getConversionFeeInfo() {
        return conversionFeeInfo;
    }

    public void setConversionFeeInfo(String conversionFeeInfo) {
        this.conversionFeeInfo = conversionFeeInfo;
    }

    public List<InstallmentBankOption> getInstallmentBankOptionList() {
        return installmentBankOptionList;
    }

    public void setInstallmentBankOptionList(List<InstallmentBankOption> installmentBankOptionList) {
        this.installmentBankOptionList = installmentBankOptionList;
    }

    public String getVoucherAmountIdr() {
        return voucherAmountIdr;
    }

    public void setVoucherAmountIdr(String voucherAmountIdr) {
        this.voucherAmountIdr = voucherAmountIdr;
    }

    public String getDepositAfter() {
        return depositAfter;
    }

    public void setDepositAfter(String depositAfter) {
        this.depositAfter = depositAfter;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getPaymentLeftIdr() {
        return paymentLeftIdr;
    }

    public void setPaymentLeftIdr(String paymentLeftIdr) {
        this.paymentLeftIdr = paymentLeftIdr;
    }

    public String getLpAmount() {
        return lpAmount;
    }

    public void setLpAmount(String lpAmount) {
        this.lpAmount = lpAmount;
    }

    public String getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public String getCashbackIdr() {
        return cashbackIdr;
    }

    public void setCashbackIdr(String cashbackIdr) {
        this.cashbackIdr = cashbackIdr;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getDepositLeft() {
        return depositLeft;
    }

    public void setDepositLeft(String depositLeft) {
        this.depositLeft = depositLeft;
    }

    public Integer getUseDeposit() {
        return useDeposit;
    }

    public void setUseDeposit(Integer useDeposit) {
        this.useDeposit = useDeposit;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public BCAParam getBcaParam() {
        return bcaParam;
    }

    public void setBcaParam(BCAParam bcaParam) {
        this.bcaParam = bcaParam;
    }

    public Integer getUseOtp() {
        return useOtp;
    }

    public void setUseOtp(Integer useOtp) {
        this.useOtp = useOtp;
    }

    public String getInstallmentBank() {
        return installmentBank;
    }

    public void setInstallmentBank(String installmentBank) {
        this.installmentBank = installmentBank;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    public String getEmoneyCode() {
        return emoneyCode;
    }

    public void setEmoneyCode(String emoneyCode) {
        this.emoneyCode = emoneyCode;
    }

    public String getUnik() {
        return unik;
    }

    public void setUnik(String unik) {
        this.unik = unik;
    }

    public String getGrandTotalIdr() {
        return grandTotalIdr;
    }

    public void setGrandTotalIdr(String grandTotalIdr) {
        this.grandTotalIdr = grandTotalIdr;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getDepositAmountIdr() {
        return depositAmountIdr;
    }

    public void setDepositAmountIdr(String depositAmountIdr) {
        this.depositAmountIdr = depositAmountIdr;
    }

    public Integer getMaxConversionLimit() {
        return maxConversionLimit;
    }

    public void setMaxConversionLimit(Integer maxConversionLimit) {
        this.maxConversionLimit = maxConversionLimit;
    }

    public String getDiscountGatewayIdr() {
        return discountGatewayIdr;
    }

    public void setDiscountGatewayIdr(String discountGatewayIdr) {
        this.discountGatewayIdr = discountGatewayIdr;
    }

    public String getUserDepositIdr() {
        return userDepositIdr;
    }

    public void setUserDepositIdr(String userDepositIdr) {
        this.userDepositIdr = userDepositIdr;
    }

    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }

    public Integer getMsisdnVerified() {
        return msisdnVerified;
    }

    public void setMsisdnVerified(Integer msisdnVerified) {
        this.msisdnVerified = msisdnVerified;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getLpAmountIdr() {
        return lpAmountIdr;
    }

    public void setLpAmountIdr(String lpAmountIdr) {
        this.lpAmountIdr = lpAmountIdr;
    }

    public String getConversionInfo() {
        return conversionInfo;
    }

    public void setConversionInfo(String conversionInfo) {
        this.conversionInfo = conversionInfo;
    }

    public String getConfCode() {
        return confCode;
    }

    public void setConfCode(String confCode) {
        this.confCode = confCode;
    }

    public Indomaret getIndomaret() {
        return indomaret;
    }

    public void setIndomaret(Indomaret indomaret) {
        this.indomaret = indomaret;
    }

    public String getBriWebsiteLink() {
        return briWebsiteLink;
    }

    public void setBriWebsiteLink(String briWebsiteLink) {
        this.briWebsiteLink = briWebsiteLink;
    }

    public String getConfDueDate() {
        return confDueDate;
    }

    public void setConfDueDate(String confDueDate) {
        this.confDueDate = confDueDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public String getGrandTotalBeforeFeeIdr() {
        return grandTotalBeforeFeeIdr;
    }

    public void setGrandTotalBeforeFeeIdr(String grandTotalBeforeFeeIdr) {
        this.grandTotalBeforeFeeIdr = grandTotalBeforeFeeIdr;
    }

    public String getDiscountGateway() {
        return discountGateway;
    }

    public void setDiscountGateway(String discountGateway) {
        this.discountGateway = discountGateway;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getStatusUnik() {
        return statusUnik;
    }

    public void setStatusUnik(String statusUnik) {
        this.statusUnik = statusUnik;
    }

    public Integer getUserDeposit() {
        return userDeposit;
    }

    public void setUserDeposit(Integer userDeposit) {
        this.userDeposit = userDeposit;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getLockMandiri() {
        return lockMandiri;
    }

    public void setLockMandiri(String lockMandiri) {
        this.lockMandiri = lockMandiri;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(String voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public String getGrandTotalBeforeFee() {
        return grandTotalBeforeFee;
    }

    public void setGrandTotalBeforeFee(String grandTotalBeforeFee) {
        this.grandTotalBeforeFee = grandTotalBeforeFee;
    }

    public String getConfCodeIdr() {
        return confCodeIdr;
    }

    public void setConfCodeIdr(String confCodeIdr) {
        this.confCodeIdr = confCodeIdr;
    }

    public String getPaymentLeft() {
        return paymentLeft;
    }

    public void setPaymentLeft(String paymentLeft) {
        this.paymentLeft = paymentLeft;
    }

    public String getKlikbcaUser() {
        return klikbcaUser;
    }

    public void setKlikbcaUser(String klikbcaUser) {
        this.klikbcaUser = klikbcaUser;
    }


    protected Transaction(Parcel in) {
        if (in.readByte() == 0x01) {
            installmentBankOptionList = new ArrayList<InstallmentBankOption>();
            in.readList(installmentBankOptionList, InstallmentBankOption.class.getClassLoader());
        } else {
            installmentBankOptionList = null;
        }
        conversionFeeInfo = in.readString();
        voucherAmountIdr = in.readString();
        depositAfter = in.readString();
        grandTotal = in.readString();
        paymentLeftIdr = in.readString();
        lpAmount = in.readString();
        confirmationId = in.readString();
        cashbackIdr = in.readString();
        step = in.readString();
        depositLeft = in.readString();
        useDeposit = in.readByte() == 0x00 ? null : in.readInt();
        paymentId = in.readString();
        bcaParam = (BCAParam) in.readValue(BCAParam.class.getClassLoader());
        useOtp = in.readByte() == 0x00 ? null : in.readInt();
        installmentBank = in.readString();
        cashback = in.readString();
        nowTime = in.readString();
        emoneyCode = in.readString();
        unik = in.readString();
        grandTotalIdr = in.readString();
        creditCard = (CreditCard) in.readValue(CreditCard.class.getClassLoader());
        depositAmountIdr = in.readString();
        maxConversionLimit = in.readByte() == 0x00 ? null : in.readInt();
        discountGatewayIdr = in.readString();
        userDepositIdr = in.readString();
        if (in.readByte() == 0x01) {
            carts = new ArrayList<Cart>();
            in.readList(carts, Cart.class.getClassLoader());
        } else {
            carts = null;
        }
        msisdnVerified = in.readByte() == 0x00 ? null : in.readInt();
        gateway = in.readString();
        lpAmountIdr = in.readString();
        conversionInfo = in.readString();
        confCode = in.readString();
        indomaret = (Indomaret) in.readValue(Indomaret.class.getClassLoader());
        briWebsiteLink = in.readString();
        confDueDate = in.readString();
        token = in.readString();
        processing = in.readString();
        grandTotalBeforeFeeIdr = in.readString();
        discountGateway = in.readString();
        gatewayName = in.readString();
        statusUnik = in.readString();
        userDeposit = in.readByte() == 0x00 ? null : in.readInt();
        transactionCode = in.readString();
        lockMandiri = in.readString();
        depositAmount = in.readString();
        voucherAmount = in.readString();
        grandTotalBeforeFee = in.readString();
        confCodeIdr = in.readString();
        paymentLeft = in.readString();
        klikbcaUser = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (installmentBankOptionList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(installmentBankOptionList);
        }
        dest.writeString(conversionFeeInfo);
        dest.writeString(voucherAmountIdr);
        dest.writeString(depositAfter);
        dest.writeString(grandTotal);
        dest.writeString(paymentLeftIdr);
        dest.writeString(lpAmount);
        dest.writeString(confirmationId);
        dest.writeString(cashbackIdr);
        dest.writeString(step);
        dest.writeString(depositLeft);
        if (useDeposit == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(useDeposit);
        }
        dest.writeString(paymentId);
        dest.writeValue(bcaParam);
        if (useOtp == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(useOtp);
        }
        dest.writeString(installmentBank);
        dest.writeString(cashback);
        dest.writeString(nowTime);
        dest.writeString(emoneyCode);
        dest.writeString(unik);
        dest.writeString(grandTotalIdr);
        dest.writeValue(creditCard);
        dest.writeString(depositAmountIdr);
        if (maxConversionLimit == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(maxConversionLimit);
        }
        dest.writeString(discountGatewayIdr);
        dest.writeString(userDepositIdr);
        if (carts == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(carts);
        }
        if (msisdnVerified == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(msisdnVerified);
        }
        dest.writeString(gateway);
        dest.writeString(lpAmountIdr);
        dest.writeString(conversionInfo);
        dest.writeString(confCode);
        dest.writeValue(indomaret);
        dest.writeString(briWebsiteLink);
        dest.writeString(confDueDate);
        dest.writeString(token);
        dest.writeString(processing);
        dest.writeString(grandTotalBeforeFeeIdr);
        dest.writeString(discountGateway);
        dest.writeString(gatewayName);
        dest.writeString(statusUnik);
        if (userDeposit == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(userDeposit);
        }
        dest.writeString(transactionCode);
        dest.writeString(lockMandiri);
        dest.writeString(depositAmount);
        dest.writeString(voucherAmount);
        dest.writeString(grandTotalBeforeFee);
        dest.writeString(confCodeIdr);
        dest.writeString(paymentLeft);
        dest.writeString(klikbcaUser);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
