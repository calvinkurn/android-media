package com.tokopedia.tkpd.purchase.model.response.cancelform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 24/05/2016.
 */
public class Form {
    private static final String TAG = Form.class.getSimpleName();

    @SerializedName("voucher_used")
    @Expose
    private String voucherUsed;
    @SerializedName("refund")
    @Expose
    private String refund;
    @SerializedName("vouchers")
    @Expose
    private List<String> vouchers = new ArrayList<String>();
    @SerializedName("lp_amount_idr")
    @Expose
    private String lpAmountIdr;
    @SerializedName("lp_amount")
    @Expose
    private String lpAmount;
    @SerializedName("total_refund")
    @Expose
    private String totalRefund;

    public String getVoucherUsed() {
        return voucherUsed;
    }

    public void setVoucherUsed(String voucherUsed) {
        this.voucherUsed = voucherUsed;
    }

    public String getRefund() {
        return refund;
    }

    public void setRefund(String refund) {
        this.refund = refund;
    }

    public List<String> getVouchers() {
        return vouchers;
    }

    public void setVouchers(List<String> vouchers) {
        this.vouchers = vouchers;
    }

    public String getLpAmountIdr() {
        return lpAmountIdr;
    }

    public void setLpAmountIdr(String lpAmountIdr) {
        this.lpAmountIdr = lpAmountIdr;
    }

    public String getLpAmount() {
        return lpAmount;
    }

    public void setLpAmount(String lpAmount) {
        this.lpAmount = lpAmount;
    }

    public String getTotalRefund() {
        return totalRefund;
    }

    public void setTotalRefund(String totalRefund) {
        this.totalRefund = totalRefund;
    }
}
