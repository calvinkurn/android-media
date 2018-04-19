package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 2/15/18.
 */

public class PendingChatViewModel extends BaseChatViewModel implements Visitable<GroupChatTypeFactory> {

    String senderId;
    String senderName;
    String senderIconUrl;
    boolean isInfluencer;
    private boolean isRetry;

    public PendingChatViewModel(String message, long createdAt, long updatedAt,
                                String messageId, String senderId, String senderName,
                                String senderIconUrl, boolean isInfluencer) {
        super(message, createdAt, updatedAt, messageId);
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderIconUrl = senderIconUrl;
        this.isInfluencer = isInfluencer;
        this.isRetry = false;
    }

    public PendingChatViewModel(String message, String senderId, String senderName, String senderIconUrl, boolean isInfluencer) {
        super(message, 0, 0, "");
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderIconUrl = senderIconUrl;
        this.isInfluencer = isInfluencer;
        this.isRetry = false;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderIconUrl() {
        return senderIconUrl;
    }

    public boolean isInfluencer() {
        return isInfluencer;
    }

    public void setRetry(boolean isRetry) {
        this.isRetry = isRetry;
    }

    public boolean isRetry() {
        return isRetry;
    }
}
