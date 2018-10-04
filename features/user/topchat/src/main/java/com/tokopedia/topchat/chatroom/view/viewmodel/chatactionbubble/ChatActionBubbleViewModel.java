package com.tokopedia.topchat.chatroom.view.viewmodel.chatactionbubble;

/**
 * Created by Hendri on 18/07/18.
 */
public class ChatActionBubbleViewModel {
    private String message;

    public ChatActionBubbleViewModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
