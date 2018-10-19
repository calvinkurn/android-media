package com.tokopedia.topchat.chatroom.data.mapper;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.topchat.chatroom.domain.pojo.quickreply.QuickReplyListPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.quickreply.QuickReplyPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.Attachment;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.AttachmentInvoice;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.AttachmentInvoiceAttributes;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.Contact;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.ListReply;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.ReplyData;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageDualAnnouncementViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSelectionViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSingleViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.rating.ChatRatingViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper.TYPE_CHAT_RATING;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class GetReplyMapper extends BaseChatApiCallMapper<ReplyData, ChatRoomViewModel> {

    private static final String SHOP_ADMIN_ROLE = "Shop Admin";
    private static final String SHOP_OWNER_ROLE = "Shop Owner";
    private static final String CLIENT_SHOP_ROLE = "Shop";
    private static final String USER_ROLE = "User";
    private static final String TOKOPEDIA_ADMIN_ROLE = "Tokopedia Administrator";
    private static final String TOKOPEDIA = "Tokopedia";
    private final SessionHandler sessionHandler;

    public GetReplyMapper(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    ChatRoomViewModel mappingToDomain(ReplyData data) {

        ChatRoomViewModel chatRoomViewModel = new ChatRoomViewModel();

        ArrayList<Visitable> list = new ArrayList<>();

        for (ListReply item : data.getList()) {

            if (item.isShowRating() || item.getRatingStatus() != 0) {
                mapToChatRating(list, item);
            } else if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_ANNOUNCEMENT)) {
                mapToImageAnnouncement(list, item);
            } else if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_DUAL_ANNOUNCEMENT)) {
                mapToImageDualAnnouncement(list, item);
            } else if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_UPLOAD)) {
                mapToImageUpload(list, item);
            } else if (item.getAttachment() != null
                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_PRODUCT_ATTACHMENT)) {
                mapToProductAttachment(list, item);
            } else if (item.getAttachment() != null && item.getAttachment().getType().equals
                    (WebSocketMapper.TYPE_QUICK_REPLY)) {
                mapToQuickReply(list, item);
            } else if (item.getAttachment() != null && item.getAttachment().getType().equals
                    (WebSocketMapper.TYPE_INVOICE_SEND)) {
                mapToInvoiceSend(list, item);
            } else if (item.getAttachment() != null && item.getAttachment().getType().equals
                    (WebSocketMapper.TYPE_INVOICES_SELECTION)) {
                mapToInvoiceSelectionAttachment(list, item);
            } else {
                mapToMessageViewModel(list, item);
            }
        }

        Collections.reverse(list);
        chatRoomViewModel.setChatList(list);
        chatRoomViewModel.setHasNext(data.isHasNext());
        chatRoomViewModel.setTextAreaReply(data.getTextAreaReply());
        if (data.getContacts().size() > 0) {
            chatRoomViewModel.setShopId(data.getContacts().get(0).getShopId());
        }
        setOpponentViewModel(chatRoomViewModel, data.getContacts());
        return chatRoomViewModel;
    }

    private void mapToChatRating(ArrayList<Visitable> list, ListReply pojo) {
        ChatRatingViewModel chatRatingViewModel = new ChatRatingViewModel(
                String.valueOf(pojo.getMsgId()),
                String.valueOf(pojo.getSenderId()),
                pojo.getSenderName(),
                pojo.getRole(),
                pojo.getMsg(),
                "",
                TYPE_CHAT_RATING,
                pojo.getReplyTime(),
                pojo.getRatingStatus(),
                Long.valueOf(pojo.getReplyTimeNano())
        );
        list.add(chatRatingViewModel);
    }

    private void mapToQuickReply(ArrayList<Visitable> list, ListReply item) {
        QuickReplyListViewModel quickReplyListViewModel = new QuickReplyListViewModel(
                String.valueOf(item.getMsgId()),
                String.valueOf(item.getSenderId()),
                item.getSenderName(),
                item.getRole(),
                item.getMsg(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                convertQuickItemChatList(item.getAttachment().getQuickReplies())
        );
        list.add(quickReplyListViewModel);
    }

    private void mapToMessageViewModel(ArrayList<Visitable> list, ListReply item) {
        MessageViewModel messageViewModel = new MessageViewModel(
                String.valueOf(item.getMsgId()),
                String.valueOf(item.getSenderId()),
                item.getSenderName(),
                item.getRole(),
                "",
                "",
                item.getReplyTime(),
                "",
                item.getMsg(),
                item.isMessageIsRead(),
                false,
                !item.isOpposite()
        );
        list.add(messageViewModel);
    }

    private void mapToImageUpload(ArrayList<Visitable> list, ListReply item) {
        ImageUploadViewModel imageUpload = new ImageUploadViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                !item.isOpposite(),
                item.getAttachment().getAttributes().getImageUrl(),
                item.getAttachment().getAttributes().getThumbnail(),
                item.isMessageIsRead(),
                item.getMsg()
        );

        list.add(imageUpload);
    }

    private void mapToImageAnnouncement(ArrayList<Visitable> list, ListReply item) {
        ImageAnnouncementViewModel imageAnnouncement = new ImageAnnouncementViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                item.getAttachment().getAttributes().getImageUrl(),
                item.getAttachment().getAttributes().getUrl(),
                item.getMsg(),
                item.getBlastId()
        );

        list.add(imageAnnouncement);
    }

    private void mapToImageDualAnnouncement(ArrayList<Visitable> list, ListReply item) {
        ImageDualAnnouncementViewModel imageDualAnnouncement = new ImageDualAnnouncementViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                item.getMsg(),
                item.getAttachment().getAttributes().getImageUrl(),
                item.getAttachment().getAttributes().getUrl(),
                item.getAttachment().getAttributes().getImageUrl2(),
                item.getAttachment().getAttributes().getUrl2(),
                item.getBlastId()
        );

        list.add(imageDualAnnouncement);
    }

    private void mapToInvoiceSend(ArrayList<Visitable> list, ListReply item) {
        AttachmentInvoiceAttributes invoiceAttributes =
                item.getAttachment().getAttributes().getInvoiceLink().getAttributes();
        AttachInvoiceSentViewModel model = new AttachInvoiceSentViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                invoiceAttributes.getTitle(),
                invoiceAttributes.getDescription(),
                invoiceAttributes.getImageUrl(),
                invoiceAttributes.getAmount(),
                !item.isOpposite(),
                item.isMessageIsRead()
        );

        list.add(model);
    }

    private void mapToProductAttachment(ArrayList<Visitable> list, ListReply item) {

        ProductAttachmentViewModel productAttachment = new ProductAttachmentViewModel(
                String.valueOf(item.getMsgId()),
                item.getSenderId(),
                item.getSenderName(),
                item.getRole(),
                item.getAttachment().getId(),
                item.getAttachment().getType(),
                item.getReplyTime(),
                item.isMessageIsRead(),
                item.getAttachment().getAttributes().getProductId(),
                item.getAttachment().getAttributes().getProductProfile().getName(),
                item.getAttachment().getAttributes().getProductProfile().getPrice(),
                item.getAttachment().getAttributes().getProductProfile().getUrl(),
                item.getAttachment().getAttributes().getProductProfile().getImageUrl(),
                !item.isOpposite(),
                item.getMsg()
        );

        list.add(productAttachment);
    }

    private void mapToInvoiceSelectionAttachment(ArrayList<Visitable> list, ListReply item) {
        Attachment attachment = item.getAttachment();
        if (attachment.getType().equals(WebSocketMapper.TYPE_INVOICES_SELECTION)) {
            ArrayList<AttachInvoiceSingleViewModel> listSingleInvoice = new ArrayList<>();
            for (AttachmentInvoice invoice : attachment.getAttributes().getInvoices()) {
                listSingleInvoice.add(new AttachInvoiceSingleViewModel(
                        invoice.getTypeString(),
                        invoice.getType(),
                        invoice.getAttributes().getCode(),
                        invoice.getAttributes().getCreatedTime(),
                        invoice.getAttributes().getDescription(),
                        invoice.getAttributes().getUrl(),
                        invoice.getAttributes().getId(),
                        invoice.getAttributes().getImageUrl(),
                        invoice.getAttributes().getStatus(),
                        invoice.getAttributes().getStatusId(),
                        invoice.getAttributes().getTitle(),
                        invoice.getAttributes().getAmount()
                ));
            }
            AttachInvoiceSelectionViewModel invoiceSelectionViewModel = new
                    AttachInvoiceSelectionViewModel(String.valueOf(item.getMsgId()),
                    item.getSenderId(),
                    item.getSenderName(),
                    item.getRole(),
                    String.valueOf(item.getAttachmentId()),
                    attachment.getType(),
                    item.getReplyTime(),
                    listSingleInvoice,
                    item.getMsg());
            list.add(invoiceSelectionViewModel);
        }
    }


    private void setOpponentViewModel(ChatRoomViewModel chatRoomViewModel, List<Contact>
            contacts) {

        for (Contact contact : contacts) {

            if (contact.getUserId() != 0
                    && !String.valueOf(contact.getUserId()).equals(sessionHandler.getLoginID())
                    && contact.isInterlocutor()) {
                if (!TextUtils.isEmpty(contact.getAttributes().getName())) {
                    chatRoomViewModel.setNameHeader(contact.getAttributes().getName());
                }
                if (!TextUtils.isEmpty(contact.getAttributes().getThumbnail())) {
                    chatRoomViewModel.setImageHeader(contact.getAttributes().getThumbnail());
                }
                if (!TextUtils.isEmpty(contact.getRole())) {
                    if (contact.getRole().equalsIgnoreCase(USER_ROLE)) {
                        chatRoomViewModel.setInterlocutorRole(contact.getRole());
                        chatRoomViewModel.setInterlocutorId(String.valueOf(contact.getUserId()));
                    } else if (contact.getRole().equalsIgnoreCase(SHOP_ADMIN_ROLE) || contact.getRole().equalsIgnoreCase(SHOP_OWNER_ROLE)) {
                        chatRoomViewModel.setInterlocutorRole(CLIENT_SHOP_ROLE);
                        chatRoomViewModel.setInterlocutorId(String.valueOf(contact.getShopId()));
                    }
                }


            }
        }
    }

    private List<QuickReplyViewModel> convertQuickItemChatList(QuickReplyListPojo pojoList) {

        List<QuickReplyViewModel> list = new ArrayList<>();
        if (pojoList != null) {
            for (QuickReplyPojo pojo : pojoList.getQuickReplies()) {
                QuickReplyViewModel model = new QuickReplyViewModel(pojo.getMessage());
                list.add(model);
            }
        }
        return list;
    }
}
