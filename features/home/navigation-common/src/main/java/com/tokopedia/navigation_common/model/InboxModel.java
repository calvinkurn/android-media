package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meta on 31/07/18.
 */
public class InboxModel {

    @SerializedName("talk")
    @Expose
    private String talk = "";

    @SerializedName("ticket")
    @Expose
    private String ticket = "";

    @SerializedName("review")
    @Expose
    private String review = "";

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
