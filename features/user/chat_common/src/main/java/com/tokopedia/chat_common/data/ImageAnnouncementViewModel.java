package com.tokopedia.chat_common.data;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory;

/**
 * @author by nisie on 5/15/18.
 */
public class ImageAnnouncementViewModel extends BaseChatViewModel implements Visitable<BaseChatTypeFactory> {

    private String imageUrl;
    private String redirectUrl;
    private int blastId;

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
     * @param imageUrl       image url
     * @param redirectUrl    redirect url in http
     * @param blastId        blast id for campaign
     * @see AttachmentType for attachment types.
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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public int getBlastId() {
        return blastId;
    }

    @Override
    public int type(BaseChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
