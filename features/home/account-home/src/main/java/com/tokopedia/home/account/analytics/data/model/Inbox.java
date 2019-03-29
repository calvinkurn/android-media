
package com.tokopedia.home.account.analytics.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inbox {

    @SerializedName("talk")
    @Expose
    private int inboxTalk = 0;
    @SerializedName("ticket")
    @Expose
    private int inboxTicket = 0;
    @SerializedName("review")
    @Expose
    private int inboxReview = 0;
    @SerializedName("friend")
    @Expose
    private int inboxFriend = 0;
    @SerializedName("wishlist")
    @Expose
    private int inboxWishlist = 0;
    @SerializedName("message")
    @Expose
    private int inboxMessage = 0;
    @SerializedName("reputation")
    @Expose
    private int inboxReputation = 0;

    public int getInboxTalk() {
        return inboxTalk;
    }

    public void setInboxTalk(int inboxTalk) {
        this.inboxTalk = inboxTalk;
    }

    public int getInboxTicket() {
        return inboxTicket;
    }

    public void setInboxTicket(int inboxTicket) {
        this.inboxTicket = inboxTicket;
    }

    public int getInboxReview() {
        return inboxReview;
    }

    public void setInboxReview(int inboxReview) {
        this.inboxReview = inboxReview;
    }

    public int getInboxFriend() {
        return inboxFriend;
    }

    public void setInboxFriend(int inboxFriend) {
        this.inboxFriend = inboxFriend;
    }

    public int getInboxWishlist() {
        return inboxWishlist;
    }

    public void setInboxWishlist(int inboxWishlist) {
        this.inboxWishlist = inboxWishlist;
    }

    public int getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(int inboxMessage) {
        this.inboxMessage = inboxMessage;
    }

    public int getInboxReputation() {
        return inboxReputation;
    }

    public void setInboxReputation(int inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

}
