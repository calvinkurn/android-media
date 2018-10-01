package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppNotificationSettingModel {
    @SerializedName("flag_admin_message")
    @Expose
    private int flagAdminMessage;
    @SerializedName("flag_review")
    @Expose
    private int flagreview;
    @SerializedName("flag_newsletter")
    @Expose
    private int flagNewsletter;
    @SerializedName("flag_message")
    @Expose
    private int flagMessage;
    @SerializedName("flag_talk_product")
    @Expose
    private int flagTalkProduct;

    public int getFlagAdminMessage() {
        return flagAdminMessage;
    }

    public void setFlagAdminMessage(int flagAdminMessage) {
        this.flagAdminMessage = flagAdminMessage;
    }

    public int getFlagreview() {
        return flagreview;
    }

    public void setFlagreview(int flagreview) {
        this.flagreview = flagreview;
    }

    public int getFlagNewsletter() {
        return flagNewsletter;
    }

    public void setFlagNewsletter(int flagNewsletter) {
        this.flagNewsletter = flagNewsletter;
    }

    public int getFlagMessage() {
        return flagMessage;
    }

    public void setFlagMessage(int flagMessage) {
        this.flagMessage = flagMessage;
    }

    public int getFlagTalkProduct() {
        return flagTalkProduct;
    }

    public void setFlagTalkProduct(int flagTalkProduct) {
        this.flagTalkProduct = flagTalkProduct;
    }

    public static class Response {
        @SerializedName("notification")
        @Expose
        public AppNotificationSettingModel notification;
    }
}
