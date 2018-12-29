package com.tokopedia.topchat.chatroom.domain.pojo.quickreply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 09/05/18.
 */

public class QuickReplyPojo {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("action")
    @Expose
    private String action;

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public String getAction() {
        return action;
    }
}
