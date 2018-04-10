
package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inbox {

    @SerializedName("inbox_talk")
    @Expose
    private Integer inboxTalk;
    @SerializedName("inbox_ticket")
    @Expose
    private Integer inboxTicket;
    @SerializedName("inbox_review")
    @Expose
    private Integer inboxReview;
    @SerializedName("inbox_friend")
    @Expose
    private Integer inboxFriend;
    @SerializedName("inbox_wishlist")
    @Expose
    private Integer inboxWishlist;
    @SerializedName("inbox_message")
    @Expose
    private Integer inboxMessage;
    @SerializedName("inbox_reputation")
    @Expose
    private Integer inboxReputation;

    public Integer getInboxTalk() {
        return inboxTalk;
    }

    public void setInboxTalk(Integer inboxTalk) {
        this.inboxTalk = inboxTalk;
    }

    public Integer getInboxTicket() {
        return inboxTicket;
    }

    public void setInboxTicket(Integer inboxTicket) {
        this.inboxTicket = inboxTicket;
    }

    public Integer getInboxReview() {
        return inboxReview;
    }

    public void setInboxReview(Integer inboxReview) {
        this.inboxReview = inboxReview;
    }

    public Integer getInboxFriend() {
        return inboxFriend;
    }

    public void setInboxFriend(Integer inboxFriend) {
        this.inboxFriend = inboxFriend;
    }

    public Integer getInboxWishlist() {
        return inboxWishlist;
    }

    public void setInboxWishlist(Integer inboxWishlist) {
        this.inboxWishlist = inboxWishlist;
    }

    public Integer getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(Integer inboxMessage) {
        this.inboxMessage = inboxMessage;
    }

    public Integer getInboxReputation() {
        return inboxReputation;
    }

    public void setInboxReputation(Integer inboxReputation) {
        this.inboxReputation = inboxReputation;
    }

}
