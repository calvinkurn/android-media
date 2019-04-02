package com.tokopedia.loyalty.domain.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

public class RequestBodyCouponRedeem {

    @SerializedName("catalog_id")
    @Expose
    private int catalogId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("is_gift")
    @Expose
    private int isGift;
    @SerializedName("gift_user_id")
    @Expose
    private int giftUserId;
    @SerializedName("gift_email")
    @Expose
    private String giftEmail;
    @SerializedName("notes")
    @Expose
    private String notes;

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public void setGiftUserId(int giftUserId) {
        this.giftUserId = giftUserId;
    }

    public void setGiftEmail(String giftEmail) {
        this.giftEmail = giftEmail;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
