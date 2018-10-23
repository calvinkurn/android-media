package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author : Steven 22/10/18
 */
public class EventGroupChatViewModel implements Visitable<GroupChatTypeFactory> {

    boolean isFreeze;
    boolean isBanned;

    public EventGroupChatViewModel(boolean isFreeze, boolean isBanned) {
        super();
        this.isFreeze = isFreeze;
        this.isBanned = isBanned;
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

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return 0;
    }
}
