package com.tokopedia.topchat.chatroom.domain.pojo.chatactionballoon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.topchat.chatroom.domain.pojo.quickreply.QuickReplyPojo;

import java.util.List;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionBalloonSelectionAttachmentAttributes {
    @SerializedName("button_actions")
    @Expose
    private List<ChatActionPojo> chatActions;

    public List<ChatActionPojo> getChatActions() {
        return chatActions;
    }
}
