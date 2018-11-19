package com.tokopedia.topchat.chatroom.domain.pojo.chatRoomSettings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatBlockStatus {

    @SerializedName(value = "is_blocked", alternate = {"isBlocked"})
    @Expose
    private boolean isBlocked;

    @SerializedName(value = "is_promo_blocked", alternate = {"isPromoBlocked"})
    @Expose
    private boolean isPromoBlocked;

    @SerializedName(value = "blocked_until", alternate = {"blockedUntil"})
    @Expose
    private String validDate;

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isPromoBlocked() {
        return isPromoBlocked;
    }

    public void setPromoBlocked(boolean promoBlocked) {
        isPromoBlocked = promoBlocked;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
