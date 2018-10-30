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

    private String imageUrlTop;
    private String redirectUrlTop;
    private String imageUrlBottom;
    private String redirectUrlBottom;
    private int blastId;

    /**
     * Constructor for WebSocketResponse / API Response
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param messageId         messageId
     * @param fromUid           userId of sender
     * @param from              name of sender
     * @param fromRole          role of sender
     * @param attachmentId      attachment id
     * @param attachmentType    attachment type. Please refer to
     *                          {@link WebSocketMapper} types
     * @param replyTime         replytime in unixtime
     * @param imageUrlTop       image url Top image
     * @param redirectUrlTop    redirect url in http for Top image click
     * @param imageUrlBottom    image url Bottom image
     * @param redirectUrlBottom redirect url in http for Bottom image click
     * @param blastId           blast id for campaign.
     */
    public ImageDualAnnouncementViewModel(String messageId, String fromUid, String from, String
            fromRole, String attachmentId, String attachmentType, String replyTime, String
                                                  message, String imageUrlTop, String redirectUrlTop, String imageUrlBottom, String
                                                  redirectUrlBottom, int blastId) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.imageUrlTop = imageUrlTop;
        this.redirectUrlTop = redirectUrlTop;
        this.imageUrlBottom = imageUrlBottom;
        this.redirectUrlBottom = redirectUrlBottom;
        this.blastId = blastId;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrlTop() {
        return imageUrlTop;
    }

    public void setImageUrlTop(String imageUrlTop) {
        this.imageUrlTop = imageUrlTop;
    }

    public String getRedirectUrlTop() {
        return redirectUrlTop;
    }

    public void setRedirectUrlTop(String redirectUrlTop) {
        this.redirectUrlTop = redirectUrlTop;
    }

    public String getImageUrlBottom() {
        return imageUrlBottom;
    }

    public void setImageUrlBottom(String imageUrlBottom) {
        this.imageUrlBottom = imageUrlBottom;
    }

    public String getRedirectUrlBottom() {
        return redirectUrlBottom;
    }

    public void setRedirectUrlBottom(String redirectUrlBottom) {
        this.redirectUrlBottom = redirectUrlBottom;
    }

    public int getBlastId() {
        return blastId;
    }
}
