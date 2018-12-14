package com.tokopedia.core.drawer2.data.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public class Inbox {
    private static final String TAG = Inbox.class.getSimpleName();

    @SerializedName("inbox_reputation")
    @Expose
    private int inboxReputation;
    @SerializedName("inbox_friend")
    @Expose
    private int inboxFriend;
    @SerializedName("inbox_ticket")
    @Expose
    private int inboxTicket;
    @SerializedName("inbox_review")
    @Expose
    private int inboxReview;
    @SerializedName("inbox_message")
    @Expose
    private int inboxMessage;
    @SerializedName("inbox_talk")
    @Expose
    private int inboxTalk;

    public int getInboxFriend() {
        return inboxFriend;
    }

    public void setInboxFriend(int inboxFriend) {
        this.inboxFriend = inboxFriend;
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

    public int getInboxMessage() {
        return inboxMessage;
    }

    public void setInboxMessage(int inboxMessage) {
        this.inboxMessage = inboxMessage;
    }

    public int getInboxTalk() {
        return inboxTalk;
    }

    public void setInboxTalk(int inboxTalk) {
        this.inboxTalk = inboxTalk;
    }

    public int getInboxReputation() {
        return inboxReputation;
    }

    public void setInboxReputation(int inboxReputation) {
        this.inboxReputation = inboxReputation;
    }
}
