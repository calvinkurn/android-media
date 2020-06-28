package com.tokopedia.core.drawer2.data.pojo.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Angga.Prasetiyo on 18/12/2015.
 */
public class Inbox {

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

    public int getInboxTicket() {
        return inboxTicket;
    }

    public int getInboxMessage() {
        return inboxMessage;
    }

    public int getInboxTalk() {
        return inboxTalk;
    }

    public int getInboxReputation() {
        return inboxReputation;
    }
}
