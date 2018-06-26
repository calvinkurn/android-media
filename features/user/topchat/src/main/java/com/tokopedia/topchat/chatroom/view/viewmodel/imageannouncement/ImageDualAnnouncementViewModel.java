package com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.presenter.ChatWebSocketListenerImpl;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;

/**
 * Created by Hendri on 22/06/18.
 */
public class ImageDualAnnouncementViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private String imageUrlLeft;
    private String redirectUrlLeft;
    private String imageUrlRight;
    private String redirectUrlRight;

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
     * @param imageUrlLeft  image url Left left image
     * @param redirectUrlLeft redirect url in http for Left image click
     * @param imageUrlRight  image url Right image
     * @param redirectUrlRight redirect url in http for Right image click
     */
    public ImageDualAnnouncementViewModel(String messageId, String fromUid, String from, String
            fromRole, String attachmentId, String attachmentType, String replyTime, String
            message, String imageUrlLeft, String redirectUrlLeft, String imageUrlRight, String
            redirectUrlRight) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.imageUrlLeft = imageUrlLeft;
        this.redirectUrlLeft = redirectUrlLeft;
        this.imageUrlRight = imageUrlRight;
        this.redirectUrlRight = redirectUrlRight;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrlLeft() {
        return imageUrlLeft;
    }

    public void setImageUrlLeft(String imageUrlLeft) {
        this.imageUrlLeft = imageUrlLeft;
    }

    public String getRedirectUrlLeft() {
        return redirectUrlLeft;
    }

    public void setRedirectUrlLeft(String redirectUrlLeft) {
        this.redirectUrlLeft = redirectUrlLeft;
    }

    public String getImageUrlRight() {
        return imageUrlRight;
    }

    public void setImageUrlRight(String imageUrlRight) {
        this.imageUrlRight = imageUrlRight;
    }

    public String getRedirectUrlRight() {
        return redirectUrlRight;
    }

    public void setRedirectUrlRight(String redirectUrlRight) {
        this.redirectUrlRight = redirectUrlRight;
    }
}
