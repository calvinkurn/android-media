package com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.GetReplyListUseCase;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.presenter.ChatWebSocketListenerImpl;
import com.tokopedia.topchat.chatroom.view.viewmodel.DummyChatViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableViewModel;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachInvoiceSentViewModel extends SendableViewModel implements
        Visitable<ChatRoomTypeFactory> {

    private String imageUrl;
    private String description;
    private String totalAmount;

    /**
     * Constructor for WebSocket.
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param msgId          messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param imageUrl       image url
     * @param message        message (invoice id)
     * @param description    invoice description
     * @param totalAmount    total amount
     */
    public AttachInvoiceSentViewModel(String msgId,
                                      String fromUid,
                                      String from,
                                      String fromRole,
                                      String attachmentId,
                                      String attachmentType,
                                      String replyTime,
                                      String startTime,
                                      String message,
                                      String description,
                                      String imageUrl,
                                      String totalAmount,
                                      boolean isSender) {
        super(msgId, fromUid, from, fromRole,
                attachmentId, attachmentType, replyTime, startTime, false, false, isSender, message);
        this.description = description;
        this.imageUrl = imageUrl;
        this.totalAmount = totalAmount;
    }

    /**
     * Constructor for API.
     * {@link ChatWebSocketListenerImpl}
     * {@link GetReplyListUseCase}
     *
     * @param msgId          messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     *                       {@link WebSocketMapper} types
     * @param replyTime      replytime in unixtime
     * @param imageUrl       image url
     * @param message        message (invoice id)
     * @param description    invoice description
     * @param totalAmount    total amount
     *                       !! startTime is not returned from API
     */
    public AttachInvoiceSentViewModel(String msgId,
                                      String fromUid,
                                      String from,
                                      String fromRole,
                                      String attachmentId,
                                      String attachmentType,
                                      String replyTime,
                                      String message,
                                      String description,
                                      String imageUrl,
                                      String totalAmount,
                                      boolean isSender,
                                      boolean isRead) {
        super(msgId, fromUid, from, fromRole,
                attachmentId, attachmentType, replyTime, "", isRead,
                false, isSender, message);
        this.description = description;
        this.imageUrl = imageUrl;
        this.totalAmount = totalAmount;
    }

    /**
     * Constructor for Sending Invoice
     *
     * @param fromUid     sender user id
     * @param from        sender name
     * @param message     invoice number
     * @param description invoice description
     * @param imageUrl    image url
     * @param totalAmount amount
     * @param startTime   starttime to remove dummy sent message after successful send. Check
     *                    {@link SendableViewModel}
     */
    public AttachInvoiceSentViewModel(String fromUid,
                                      String from,
                                      String message,
                                      String description,
                                      String imageUrl,
                                      String totalAmount,
                                      String startTime) {
        super("", fromUid, from, "",
                "", WebSocketMapper.TYPE_INVOICE_SEND,
                DummyChatViewModel.SENDING_TEXT, startTime, false,
                true, true, message);
        this.description = description;
        this.imageUrl = imageUrl;
        this.totalAmount = totalAmount;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

}
