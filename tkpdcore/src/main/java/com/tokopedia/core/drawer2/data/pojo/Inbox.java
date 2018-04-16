
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inbox {

    @SerializedName("inbox_talk")
    @Expose
    private int inboxTalk;
    @SerializedName("inbox_ticket")
    @Expose
    private int inboxTicket;
    @SerializedName("inbox_review")
    @Expose
    private int inboxReview;
    @SerializedName("inbox_friend")
    @Expose
    private int inboxFriend;
    @SerializedName("inbox_wishlist")
    @Expose
    private int inboxWishlist;
    @SerializedName("inbox_message")
    @Expose
    private int inboxMessage;
    @SerializedName("inbox_reputation")
    @Expose
    private int inboxReputation;

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
