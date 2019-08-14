package com.tokopedia.purchase_platform.features.checkout.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 31/01/18.
 */

public class DataVoucher {

    @SerializedName("voucher_amount_idr")
    @Expose
    private String voucherAmountIdr;
    @SerializedName("voucher_no_other_promotion")
    @Expose
    private int voucherNoOtherPromotion;
    @SerializedName("voucher_amount")
    @Expose
    private int voucherAmount;
    @SerializedName("voucher_status")
    @Expose
    private int voucherStatus;
    @SerializedName("voucher_promo_desc")
    @Expose
    private String voucherPromoDesc;
    @SerializedName("is_coupon")
    @Expose
    private int isCoupon;

    public String getVoucherAmountIdr() {
        return voucherAmountIdr;
    }

    public int getVoucherNoOtherPromotion() {
        return voucherNoOtherPromotion;
    }

    public int getVoucherAmount() {
        return voucherAmount;
    }

    public int getVoucherStatus() {
        return voucherStatus;
    }

    public String getVoucherPromoDesc() {
        return voucherPromoDesc;
    }

    public int getIsCoupon() {
        return isCoupon;
    }
}
