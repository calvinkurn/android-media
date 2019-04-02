package com.tokopedia.topchat.chatlist.viewmodel.message;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatlist.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatlist.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatlist.presenter.ChatWebSocketListenerImpl;
import com.tokopedia.topchat.chatlist.viewmodel.DummyChatViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.SendableViewModel;

/**
 * @author by nisie on 5/16/18.
 */
@Deprecated
//To be converted to gql and use chat_common
public class MessageViewModel extends SendableViewModel implements Visitable<ChatRoomTypeFactory> {

    /**
     * Constructor for WebSocketResponse / API Response
     * {@link ChatWebSocketListenerImpl}
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
     * @param message        censored reply
     * @param isRead         is message already read by opponent
     * @param isSender       is own sender
     */
    public MessageViewModel(String messageId, String fromUid, String from, String fromRole,
                            String attachmentId, String attachmentType, String replyTime,
                            String startTime, String message, boolean isRead, boolean isDummy,
                            boolean isSender) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime,
                startTime, isRead, isDummy, isSender, message);
    }

    /**
     * Constructor for send message
     *
     * @param messageId messageId
     * @param fromUid   userId of sender
     * @param from      name of sender
     * @param startTime date time when sending / uploading data. Used to validate temporary
     *                  message
     * @param message   censored reply
     */
    public MessageViewModel(String messageId, String fromUid, String from, String startTime,
                            String message) {
        super(messageId, fromUid, from, "", "", "",
                DummyChatViewModel.SENDING_TEXT, startTime,
                false, true, true, message);
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
