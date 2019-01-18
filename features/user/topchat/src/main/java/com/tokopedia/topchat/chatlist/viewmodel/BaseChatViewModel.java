package com.tokopedia.topchat.chatlist.viewmodel;

/**
 * @author by yfsx on 08/05/18.
 */
@Deprecated
//To be converted to gql and use chat_common
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
     *
     * @param messageId      messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type.
     * @param replyTime      replytime in unixtime
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
     * @param showDate set true to show date in header of chat
     */
    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isShowTime() {
        return showTime;
    }

    /**
     * @param showTime set true to show time in chat
     */
    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }
}
