package com.tokopedia.tkpd.purchase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by herdimac on 4/8/16.
 * modified by Angga.Prasetiyo implement parcelable
 */
public class PaymentDetail implements Parcelable {
    private static final String TAG = PaymentDetail.class.getSimpleName();

    @SerializedName("deposit_amt")
    @Expose
    private String depositAmt;
    @SerializedName("voucher_amt")
    @Expose
    private String voucherAmt;
    @SerializedName("payment_amt")
    @Expose
    private String paymentAmt;
    @SerializedName("deposit_used_amt")
    @Expose
    private String depositUsedAmt;
    @SerializedName("left_amt")
    @Expose
    private String leftAmt;
    @SerializedName("total_invoice")
    @Expose
    private String totalInvoice;
    @SerializedName("sys_bank")
    @Expose
    private SysBank sysBank;
    @SerializedName("payment_method")
    @Expose
    private Integer paymentMethod;
    @SerializedName("deposit_left")
    @Expose
    private String depositLeft;
    @SerializedName("voucher_idr")
    @Expose
    private String voucherIdr;
    @SerializedName("deposit_idr")
    @Expose
    private String depositIdr;
    @SerializedName("payment_method_name")
    @Expose
    private String paymentMethodName;
    @SerializedName("pay_ref_num")
    @Expose
    private String payRefNum;
    @SerializedName("normal_product")
    @Expose
    private Integer normalProduct;

    /**
     * @return The depositAmt
     */
    public String getDepositAmt() {
        return depositAmt;
    }

    /**
     * @param depositAmt The deposit_amt
     */
    public void setDepositAmt(String depositAmt) {
        this.depositAmt = depositAmt;
    }

    /**
     * @return The voucherAmt
     */
    public String getVoucherAmt() {
        return voucherAmt;
    }

    /**
     * @param voucherAmt The voucher_amt
     */
    public void setVoucherAmt(String voucherAmt) {
        this.voucherAmt = voucherAmt;
    }

    /**
     * @return The paymentAmt
     */
    public String getPaymentAmt() {
        return paymentAmt;
    }

    /**
     * @param paymentAmt The payment_amt
     */
    public void setPaymentAmt(String paymentAmt) {
        this.paymentAmt = paymentAmt;
    }

    /**
     * @return The depositUsedAmt
     */
    public String getDepositUsedAmt() {
        return depositUsedAmt;
    }

    /**
     * @param depositUsedAmt The deposit_used_amt
     */
    public void setDepositUsedAmt(String depositUsedAmt) {
        this.depositUsedAmt = depositUsedAmt;
    }

    /**
     * @return The leftAmt
     */
    public String getLeftAmt() {
        return leftAmt;
    }

    /**
     * @param leftAmt The left_amt
     */
    public void setLeftAmt(String leftAmt) {
        this.leftAmt = leftAmt;
    }

    /**
     * @return The totalInvoice
     */
    public String getTotalInvoice() {
        return totalInvoice;
    }

    /**
     * @param totalInvoice The total_invoice
     */
    public void setTotalInvoice(String totalInvoice) {
        this.totalInvoice = totalInvoice;
    }

    /**
     * @return The sysBank
     */
    public SysBank getSysBank() {
        return sysBank;
    }

    /**
     * @param sysBank The sys_bank
     */
    public void setSysBank(SysBank sysBank) {
        this.sysBank = sysBank;
    }

    /**
     * @return The paymentMethod
     */
    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod The payment_method
     */
    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * @return The depositLeft
     */
    public String getDepositLeft() {
        return depositLeft;
    }

    /**
     * @param depositLeft The deposit_left
     */
    public void setDepositLeft(String depositLeft) {
        this.depositLeft = depositLeft;
    }

    /**
     * @return The voucherIdr
     */
    public String getVoucherIdr() {
        return voucherIdr;
    }

    /**
     * @param voucherIdr The voucher_idr
     */
    public void setVoucherIdr(String voucherIdr) {
        this.voucherIdr = voucherIdr;
    }

    /**
     * @return The depositIdr
     */
    public String getDepositIdr() {
        return depositIdr;
    }

    /**
     * @param depositIdr The deposit_idr
     */
    public void setDepositIdr(String depositIdr) {
        this.depositIdr = depositIdr;
    }

    /**
     * @return The paymentMethodName
     */
    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    /**
     * @param paymentMethodName The payment_method_name
     */
    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    /**
     * @return The payRefNum
     */
    public String getPayRefNum() {
        return payRefNum;
    }

    /**
     * @param payRefNum The pay_ref_num
     */
    public void setPayRefNum(String payRefNum) {
        this.payRefNum = payRefNum;
    }

    /**
     * @return The normalProduct
     */
    public Integer getNormalProduct() {
        return normalProduct;
    }

    /**
     * @param normalProduct The normal_product
     */
    public void setNormalProduct(Integer normalProduct) {
        this.normalProduct = normalProduct;
    }


    protected PaymentDetail(Parcel in) {
        depositAmt = in.readString();
        voucherAmt = in.readString();
        paymentAmt = in.readString();
        depositUsedAmt = in.readString();
        leftAmt = in.readString();
        totalInvoice = in.readString();
        sysBank = (SysBank) in.readValue(SysBank.class.getClassLoader());
        paymentMethod = in.readByte() == 0x00 ? null : in.readInt();
        depositLeft = in.readString();
        voucherIdr = in.readString();
        depositIdr = in.readString();
        paymentMethodName = in.readString();
        payRefNum = in.readString();
        normalProduct = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(depositAmt);
        dest.writeString(voucherAmt);
        dest.writeString(paymentAmt);
        dest.writeString(depositUsedAmt);
        dest.writeString(leftAmt);
        dest.writeString(totalInvoice);
        dest.writeValue(sysBank);
        if (paymentMethod == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(paymentMethod);
        }
        dest.writeString(depositLeft);
        dest.writeString(voucherIdr);
        dest.writeString(depositIdr);
        dest.writeString(paymentMethodName);
        dest.writeString(payRefNum);
        if (normalProduct == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(normalProduct);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PaymentDetail> CREATOR = new Parcelable.Creator<PaymentDetail>() {
        @Override
        public PaymentDetail createFromParcel(Parcel in) {
            return new PaymentDetail(in);
        }

        @Override
        public PaymentDetail[] newArray(int size) {
            return new PaymentDetail[size];
        }
    };
}
