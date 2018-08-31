package com.tokopedia.common_digital.cart.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author  by alvarisi on 3/29/18.
 */

public class AutoApplyVoucher {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("is_coupon")
    @Expose
    private int isCoupon;
    @SerializedName("discount_amount")
    @Expose
    private long discountAmount;
    @SerializedName("title_description")
    @Expose
    private String title;
    @SerializedName("message_success")
    @Expose
    private String messageSuccess;
    @SerializedName("promo_id")
    @Expose
    private long promoId;

    public AutoApplyVoucher() {
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public long getDiscountAmount() {
        return discountAmount;
    }

    public String getTitle() {
        return title;
    }

    public String getMessageSuccess() {
        return messageSuccess;
    }

    public long getPromoId() {
        return promoId;
    }
}
