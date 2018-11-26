package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by nisie on 3/29/18.
 */

public class VibrateViewModel extends BaseGroupChatPojo implements Visitable<GroupChatTypeFactory> {
    public static final String TYPE = "is_vibrate";

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
