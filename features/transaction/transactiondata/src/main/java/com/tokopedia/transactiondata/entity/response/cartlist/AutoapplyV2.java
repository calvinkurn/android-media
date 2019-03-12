
package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutoapplyV2 {

    @SerializedName("message")
    @Expose
    private Message message = new Message();
    @SerializedName("code")
    @Expose
    private String code = "";
    @SerializedName("promo_code_id")
    @Expose
    private String promoCodeId = "";
    @SerializedName("title_description")
    @Expose
    private String titleDescription = "";
    @SerializedName("is_coupon")
    @Expose
    private int isCoupon;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(String promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

    public String getTitleDescription() {
        return titleDescription;
    }

    public void setTitleDescription(String titleDescription) {
        this.titleDescription = titleDescription;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

}
