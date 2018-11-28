
package com.tokopedia.topchat.chatroom.domain.pojo.quickreply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuickReplyAttachmentAttributes {

    @SerializedName("new_quick_replies")
    @Expose
    private List<QuickReplyPojo> quickReplies;

    public List<QuickReplyPojo> getQuickReplies() {
        return quickReplies;
    }

}
