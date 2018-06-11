package com.tokopedia.topchat.chatroom.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom.ChatRatingViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.AttachedInvoiceSelectionViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.AttachedInvoiceSentViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.QuickReplyViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom.ImageAnnouncementViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom.ProductAttachmentViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom.TimeMachineChatViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatroom.TypingChatViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.FallbackAttachmentViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.ImageUploadViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.MessageViewHolder;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomFragment;
import com.tokopedia.topchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.topchat.chatroom.view.viewmodel.TimeMachineChatModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.TypingChatModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.AttachedInvoiceSentViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatbot.QuickReplyViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.ImageUploadViewHolder;
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.MessageViewHolder;
import com.tokopedia.topchat.chatroom.view.fragment.ChatRoomFragment;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public class ChatRoomTypeFactoryImpl extends BaseAdapterTypeFactory implements ChatRoomTypeFactory {

    ChatRoomContract.View viewListener;

    public ChatRoomTypeFactoryImpl(ChatRoomFragment context) {
        this.viewListener = context;
    }

    @Override
    public int type(TimeMachineChatModel timeMachineChatModel) {
        return TimeMachineChatViewHolder.LAYOUT;
    }

    @Override
    public int type(TypingChatModel typingChatModel) {
        return TypingChatViewHolder.LAYOUT;
    }

    @Override
    public int type(ImageUploadViewModel attachImageModel) {
        return ImageUploadViewHolder.LAYOUT;
    }

    @Override
    public int type(MessageViewModel messageViewModel) {
        return MessageViewHolder.LAYOUT;
    }

    @Override
    public int type(AttachInvoiceSentViewModel attachInvoiceSentViewModel) {
        return AttachedInvoiceSentViewHolder.LAYOUT;
    }


    @Override
    public int type(AttachInvoiceSelectionViewModel attachInvoiceSelectionViewModel) {
        return AttachedInvoiceSelectionViewHolder.LAYOUT;
    }

    @Override
    public int type(QuickReplyListViewModel quickReplyListViewModel) {
        return QuickReplyViewHolder.LAYOUT;
    }

    @Override
    public int type(FallbackAttachmentViewModel fallbackAttachmentViewModel) {
        return FallbackAttachmentViewHolder.LAYOUT;
    }

    @Override
    public int type(ChatRatingViewModel chatRatingViewModel) {
        return ChatRatingViewHolder.LAYOUT;
    }

    @Override
    public int type(ProductAttachmentViewModel productAttachmentViewModel) {
        return ProductAttachmentViewHolder.LAYOUT;
    }

    @Override
    public int type(ImageAnnouncementViewModel imageAnnouncementViewModel) {
        return ImageAnnouncementViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View view, int type) {

        AbstractViewHolder viewHolder;

        if (type == TimeMachineChatViewHolder.LAYOUT)
            viewHolder = new TimeMachineChatViewHolder(view, viewListener);
        else if (type == TypingChatViewHolder.LAYOUT)
            viewHolder = new TypingChatViewHolder(view);
        else if (type == MessageViewHolder.LAYOUT)
            viewHolder = new MessageViewHolder(view, viewListener);
        else if (type == ImageUploadViewHolder.LAYOUT)
            viewHolder = new ImageUploadViewHolder(view, viewListener);
        else if (type == AttachedInvoiceSentViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSentViewHolder(view, viewListener);
        else if (type == AttachedInvoiceSelectionViewHolder.LAYOUT)
            viewHolder = new AttachedInvoiceSelectionViewHolder(view, viewListener);
        else if (type == QuickReplyViewHolder.LAYOUT)
            viewHolder = new QuickReplyViewHolder(view, viewListener);
        else if (type == FallbackAttachmentViewHolder.LAYOUT)
            viewHolder = new FallbackAttachmentViewHolder(view, viewListener);
        else if (type == ChatRatingViewHolder.LAYOUT)
            viewHolder = new ChatRatingViewHolder(view, viewListener);
        else if (type == ProductAttachmentViewHolder.LAYOUT)
            viewHolder = new ProductAttachmentViewHolder(view, viewListener);
        else if (type == ImageAnnouncementViewHolder.LAYOUT)
            viewHolder = new ImageAnnouncementViewHolder(view, viewListener);
        else
            return super.createViewHolder(view, type);

        return viewHolder;
    }

}
