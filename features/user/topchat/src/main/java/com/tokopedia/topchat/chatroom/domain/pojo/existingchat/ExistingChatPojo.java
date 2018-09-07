
package com.tokopedia.topchat.chatroom.domain.pojo.existingchat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendri on 07/06/18.
 */
public class ExistingChatPojo {
    @SerializedName("msg_id")
    String msgId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
