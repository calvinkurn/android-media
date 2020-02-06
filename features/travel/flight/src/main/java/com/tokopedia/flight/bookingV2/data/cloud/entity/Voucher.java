package com.tokopedia.flight.bookingV2.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 22/05/18.
 */

public class Voucher {
    @SerializedName("enable_voucher")
    @Expose
    private boolean enableVoucher;
    @SerializedName("is_coupon_active")
    @Expose
    private int isCouponActive;
    @SerializedName("autoapply")
    @Expose
    private AutoApply autoApply;
    @SerializedName("default_promo_dialog_tab")
    @Expose
    private String defaultPromoTab;

    public boolean getEnableVoucher() {
        return enableVoucher;
    }

    public void setEnableVoucher(boolean enableVoucher) {
        this.enableVoucher = enableVoucher;
    }

    public int getIsCouponActive() {
        return isCouponActive;
    }

    public void setIsCouponActive(int isCouponActive) {
        this.isCouponActive = isCouponActive;
    }

    public AutoApply getAutoApply() {
        return autoApply;
    }

    public void setAutoApply(AutoApply autoApply) {
        this.autoApply = autoApply;
    }

    public String getDefaultPromoTab() {
        return defaultPromoTab;
    }

    public void setDefaultPromoTab(String defaultPromoTab) {
        this.defaultPromoTab = defaultPromoTab;
    }
}
