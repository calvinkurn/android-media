package com.tokopedia.groupchat.chatroom.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by StevenFredian .
 */

public class QuickReplyPojo extends BaseGroupChatPojo{

    @SerializedName("quick_reply")
    @Expose
    private List<String> listQuickReply = null;

    public List<String> getListQuickReply() {
        return listQuickReply;
    }

    public void setListQuickReply(List<String> listQuickReply) {
        this.listQuickReply = listQuickReply;
    }
}
