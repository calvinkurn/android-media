package com.tokopedia.chat_common.data;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory;

import org.jetbrains.annotations.NotNull;

/**
 * @author by nisie on 5/16/18.
 */
public class MessageViewModel extends SendableViewModel implements Visitable<BaseChatTypeFactory> {
    public MessageViewModel(@NotNull String messageId, @NotNull String fromUid, @NotNull String from, @NotNull String fromRole, @NotNull String attachmentId, @NotNull String attachmentType, @NotNull String replyTime, @NotNull String startTime, boolean isRead, boolean isDummy, boolean isSender, @NotNull String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, startTime, isRead, isDummy, isSender, message);
    }

//    /**
//     * Constructor for WebSocketResponse / API Response
//     * {@link ChatWebSocketListenerImpl}
//     * {@link GetReplyListUseCase}
//     *
//     * @param messageId      messageId
//     * @param fromUid        userId of sender
//     * @param from           name of sender
//     * @param fromRole       role of sender
//     * @param attachmentId   attachment id
//     * @param attachmentType attachment type. Please refer to
//     *                       {@link WebSocketMapper} types
//     * @param replyTime      replytime in unixtime
//     * @param startTime      date time when sending / uploading data. Used to validate temporary
//     * @param message        censored reply
//     * @param isRead         is message already read by opponent
//     * @param isSender       is own sender
//     */
//    public MessageViewModel(String messageId, String fromUid, String from, String fromRole,
//                            String replyTime, String startTime, String message,
//                            boolean isRead, boolean isDummy,
//                            boolean isSender) {
//        super(messageId, fromUid, from, fromRole, replyTime,
//                startTime, isRead, isDummy, isSender, message);
//    }
//
//    /**
//     * Constructor for send message
//     *
//     * @param messageId messageId
//     * @param fromUid   userId of sender
//     * @param from      name of sender
//     * @param startTime date time when sending / uploading data. Used to validate temporary
//     *                  message
//     * @param message   censored reply
//     */
//    public MessageViewModel(String messageId, String fromUid, String from, String startTime,
//                            String message) {
//        super(messageId, fromUid, from, "", "", "",
//                DummyChatViewModel.SENDING_TEXT, startTime,
//                false, true, true, message);
//    }

    @Override
    public int type(BaseChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
