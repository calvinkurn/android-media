package com.tokopedia.topchat.chatroom.view.viewmodel;

import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomAdapter;
import com.tokopedia.topchat.chatroom.view.presenter.ChatWebSocketListenerImpl;

/**
 * @author by yfsx on 08/05/18.
 */

public class BaseChatViewModel {
    private String messageId;
    private String fromUid;
    private String from;
    private String fromRole;
    private String attachmentId;
    private String attachmentType;
    private String replyTime;
    private boolean showDate = false;
    private boolean showTime = true;
    private String message;

    /**
     * Constructor for WebSocketResponse / API Response
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param messageId      messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime replytime in unixtime
     */
    public BaseChatViewModel(String messageId,
                             String fromUid,
                             String from,
                             String fromRole,
                             String attachmentId,
                             String attachmentType,
                             String replyTime,
                             String message) {
        this.messageId = messageId;
        this.fromUid = fromUid;
        this.from = from;
        this.fromRole = fromRole;
        this.attachmentId = attachmentId;
        this.attachmentType = attachmentType;
        this.replyTime = replyTime;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFromUid() {
        return fromUid;
    }

    public String getFrom() {
        return from;
    }

    public String getFromRole() {
        return fromRole;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public String getMessage() {
        return message;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    /**
     * Set in {@link ChatRoomAdapter}
     *
     * @param showDate set true to show date in header of chat
     */
    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isShowTime() {
        return showTime;
    }

    /**
     * Set in {@link ChatRoomAdapter}
     *
     * @param showTime set true to show time in chat
     */
    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
