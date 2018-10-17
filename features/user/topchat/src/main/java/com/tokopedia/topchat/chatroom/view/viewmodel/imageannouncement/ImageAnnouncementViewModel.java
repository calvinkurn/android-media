package com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.presenter.ChatWebSocketListenerImpl;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewModel extends BaseChatViewModel implements Visitable<ChatRoomTypeFactory> {

    private String imageUrl;
    private String redirectUrl;
    private int blastId;

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
     * @param imageUrl       image url
     * @param redirectUrl    redirect url in http
     * @param blastId        blast id for campaign
     */
    public ImageAnnouncementViewModel(String messageId, String fromUid, String from,
                                      String fromRole, String attachmentId, String attachmentType,
                                      String replyTime, String imageUrl, String redirectUrl,
                                      String message, int blastId) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
        this.blastId = blastId;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public int getBlastId() {
        return blastId;
    }
}
