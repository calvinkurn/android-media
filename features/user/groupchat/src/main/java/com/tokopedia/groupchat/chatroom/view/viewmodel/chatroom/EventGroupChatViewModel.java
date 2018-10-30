package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author : Steven 22/10/18
 */
public class EventGroupChatViewModel implements Visitable<GroupChatTypeFactory> {

    boolean isFreeze;
    boolean isBanned;
    String channelId;
    String userId;

    public EventGroupChatViewModel(boolean isFreeze, boolean isBanned, String channelId, String userId) {
        super();
        this.isFreeze = isFreeze;
        this.isBanned = isBanned;
        this.channelId = channelId;
        this.userId = userId;
    }

    public boolean isFreeze() {
        return isFreeze;
    }

    public void setFreeze(boolean freeze) {
        isFreeze = freeze;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return 0;
    }
}
