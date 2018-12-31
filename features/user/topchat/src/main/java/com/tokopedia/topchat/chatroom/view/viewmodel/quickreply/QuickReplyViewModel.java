package com.tokopedia.topchat.chatroom.view.viewmodel.quickreply;

/**
 * @author by yfsx on 08/05/18.
 */

public class QuickReplyViewModel{

    private String text;
    private String value;
    private String action;

    public QuickReplyViewModel(String text, String value, String action) {
        this.text = text;
        this.value = value;
        this.action = action;
    }

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
