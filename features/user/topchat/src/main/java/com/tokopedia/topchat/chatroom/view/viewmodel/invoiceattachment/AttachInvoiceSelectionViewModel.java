package com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment;


import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.topchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;

import java.util.List;

/**
 * Created by Hendri on 27/03/18.
 */

public class AttachInvoiceSelectionViewModel extends BaseChatViewModel implements
        Visitable<ChatRoomTypeFactory> {
    private List<AttachInvoiceSingleViewModel> list;

    public AttachInvoiceSelectionViewModel(String messageId, String fromUid, String from,
                                           String fromRole, String attachmentId,
                                           String attachmentType, String replyTime,
                                           List<AttachInvoiceSingleViewModel> list,
                                           String message) {
        super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message);
        this.list = list;
    }

    public List<AttachInvoiceSingleViewModel> getList() {
        return list;
    }

    public void setList(List<AttachInvoiceSingleViewModel> list) {
        this.list = list;
    }

    @Override
    public int type(ChatRoomTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
