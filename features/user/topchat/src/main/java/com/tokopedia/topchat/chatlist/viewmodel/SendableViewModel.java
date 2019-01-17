package com.tokopedia.topchat.chatlist.viewmodel;

import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatlist.presenter.ChatWebSocketListenerImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author by nisie on 5/16/18.
 */
@Deprecated
//To be converted to gql and use chat_common
public class SendableViewModel extends BaseChatViewModel {

    public static final String START_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    protected String startTime;
    private boolean isRead;
    private boolean isDummy;
    private boolean isSender;
    private boolean isShowRole = true;

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
     * @param replyTime      replytime in unixtime
     * @param startTime      date time when sending / uploading data. Used to validate temporary
     *                       sending messages
     */
    public SendableViewModel(String messageId, String fromUid, String from, String fromRole,
                             String attachmentId, String attachmentType, String replyTime,
                             String startTime, boolean isRead, boolean isDummy, boolean isSender,
                             String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.startTime = startTime;
        this.isRead = isRead;
        this.isDummy = isDummy;
        this.isSender = isSender;
    }

    public String getStartTime() {
        return startTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public boolean isSender() {
        return isSender;
    }

    public void setIsRead(boolean read) {
        isRead = read;
    }

    public void setDummy(boolean dummy) {
        isDummy = dummy;
    }

    public boolean isShowRole() {
        return isShowRole;
    }

    public void setShowRole(boolean showRole) {
        isShowRole = showRole;
    }

    public static String generateStartTime() {
        SimpleDateFormat date = new SimpleDateFormat(
                SendableViewModel.START_TIME_FORMAT, Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        return date.format(Calendar.getInstance().getTime());
    }
}
