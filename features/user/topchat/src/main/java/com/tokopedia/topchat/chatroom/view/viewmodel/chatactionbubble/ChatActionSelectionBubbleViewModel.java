package com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;

import java.util.List;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionSelectionBubbleViewModel extends BaseChatViewModel
        implements Visitable<ChatRoomTypeFactory> {

    List<ChatActionBubbleViewModel> chatActionList;

    public ChatActionSelectionBubbleViewModel(String messageId, String fromUid, String from,
                                              String fromRole, String attachmentId, String
                                                      attachmentType, String replyTime, String
                                                      message, List<ChatActionBubbleViewModel>
                                                      chatActionList) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.chatActionList = chatActionList;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public List<ChatActionBubbleViewModel> getChatActionList() {
        return chatActionList;
    }

    public void setChatActionList(List<ChatActionBubbleViewModel> chatActionList) {
        this.chatActionList = chatActionList;
    }
}
