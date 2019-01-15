//package com.tokopedia.topchat.chatroom.data.mapper;
//
//import android.text.TextUtils;
//
//import com.tokopedia.abstraction.base.view.adapter.Visitable;
//import com.tokopedia.topchat.chatroom.domain.pojo.reply.Contact;
//import com.tokopedia.topchat.chatroom.domain.pojo.reply.ListReply;
//import com.tokopedia.topchat.chatroom.domain.pojo.reply.ReplyData;
//import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;
//import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
//import com.tokopedia.topchat.revamp.view.viewmodel.ImageDualAnnouncementViewModel;
//import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
//import com.tokopedia.topchat.chatroom.view.viewmodel.message.MessageViewModel;
//import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
//import com.tokopedia.user.session.UserSessionInterface;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import javax.inject.Inject;
//
///**
// * Created by stevenfredian on 8/31/17.
// */
//
//public class GetReplyMapper extends BaseChatApiCallMapper<ReplyData, ChatRoomViewModel> {
//
//    private static final String SHOP_ADMIN_ROLE = "Shop Admin";
//    private static final String SHOP_OWNER_ROLE = "Shop Owner";
//    private static final String CLIENT_SHOP_ROLE = "Shop";
//    private static final String USER_ROLE = "User";
//    private static final String TOKOPEDIA_ADMIN_ROLE = "Tokopedia Administrator";
//    private static final String TOKOPEDIA = "Tokopedia";
//    private final UserSessionInterface userSession;
//
//    @Inject
//    public GetReplyMapper(UserSessionInterface userSession) {
//        this.userSession = userSession;
//    }
//
//    @Override
//    ChatRoomViewModel mappingToDomain(ReplyData data) {
//
//        ChatRoomViewModel chatRoomViewModel = new ChatRoomViewModel();
//
//        ArrayList<Visitable> list = new ArrayList<>();
//
//        for (ListReply item : data.getList()) {
//
//            if (item.getAttachment() != null
//                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_ANNOUNCEMENT)) {
//                mapToImageAnnouncement(list, item);
//            } else if (item.getAttachment() != null
//                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_DUAL_ANNOUNCEMENT)) {
//                mapToImageDualAnnouncement(list, item);
//            } else if (item.getAttachment() != null
//                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_IMAGE_UPLOAD)) {
//                mapToImageUpload(list, item);
//            } else if (item.getAttachment() != null
//                    && item.getAttachment().getType().equals(WebSocketMapper.TYPE_PRODUCT_ATTACHMENT)) {
//                mapToProductAttachment(list, item);
//            } else {
//                mapToMessageViewModel(list, item);
//            }
//        }
//
//        Collections.reverse(list);
//        chatRoomViewModel.setChatList(list);
//        chatRoomViewModel.setHasNext(data.isHasNext());
//        chatRoomViewModel.setTextAreaReply(data.getTextAreaReply());
//        if (data.getContacts().size() > 0) {
//            chatRoomViewModel.setShopId(data.getContacts().get(0).getShopId());
//        }
//        setOpponentViewModel(chatRoomViewModel, data.getContacts());
//        return chatRoomViewModel;
//    }
//
//    private void mapToMessageViewModel(ArrayList<Visitable> list, ListReply item) {
//        MessageViewModel messageViewModel = new MessageViewModel(
//                String.valueOf(item.getMsgId()),
//                String.valueOf(item.getSenderId()),
//                item.getSenderName(),
//                item.getRole(),
//                "",
//                "",
//                item.getReplyTime(),
//                "",
//                item.getMsg(),
//                item.isMessageIsRead(),
//                false,
//                !item.isOpposite()
//        );
//        list.add(messageViewModel);
//    }
//
//    private void mapToImageUpload(ArrayList<Visitable> list, ListReply item) {
//        ImageUploadViewModel imageUpload = new ImageUploadViewModel(
//                String.valueOf(item.getMsgId()),
//                item.getSenderId(),
//                item.getSenderName(),
//                item.getRole(),
//                item.getAttachment().getId(),
//                item.getAttachment().getType(),
//                item.getReplyTime(),
//                !item.isOpposite(),
//                item.getAttachment().getAttributes().getImageUrl(),
//                item.getAttachment().getAttributes().getThumbnail(),
//                item.isMessageIsRead(),
//                item.getMsg()
//        );
//
//        list.add(imageUpload);
//    }
//
//    private void mapToImageAnnouncement(ArrayList<Visitable> list, ListReply item) {
//        ImageAnnouncementViewModel imageAnnouncement = new ImageAnnouncementViewModel(
//                String.valueOf(item.getMsgId()),
//                item.getSenderId(),
//                item.getSenderName(),
//                item.getRole(),
//                item.getAttachment().getId(),
//                item.getAttachment().getType(),
//                item.getReplyTime(),
//                item.getAttachment().getAttributes().getImageUrl(),
//                item.getAttachment().getAttributes().getUrl(),
//                item.getMsg(),
//                item.getBlastId()
//        );
//
//        list.add(imageAnnouncement);
//    }
//
//    private void mapToImageDualAnnouncement(ArrayList<Visitable> list, ListReply item) {
//        ImageDualAnnouncementViewModel imageDualAnnouncement = new ImageDualAnnouncementViewModel(
//                String.valueOf(item.getMsgId()),
//                item.getSenderId(),
//                item.getSenderName(),
//                item.getRole(),
//                item.getAttachment().getId(),
//                item.getAttachment().getType(),
//                item.getReplyTime(),
//                item.getMsg(),
//                item.getAttachment().getAttributes().getImageUrl(),
//                item.getAttachment().getAttributes().getUrl(),
//                item.getAttachment().getAttributes().getImageUrl2(),
//                item.getAttachment().getAttributes().getUrl2(),
//                item.getBlastId()
//        );
//
//        list.add(imageDualAnnouncement);
//    }
//
//    private void mapToProductAttachment(ArrayList<Visitable> list, ListReply item) {
//
//        ProductAttachmentViewModel productAttachment = new ProductAttachmentViewModel(
//                String.valueOf(item.getMsgId()),
//                item.getSenderId(),
//                item.getSenderName(),
//                item.getRole(),
//                item.getAttachment().getId(),
//                item.getAttachment().getType(),
//                item.getReplyTime(),
//                item.isMessageIsRead(),
//                item.getAttachment().getAttributes().getProductId(),
//                item.getAttachment().getAttributes().getProductProfile().getName(),
//                item.getAttachment().getAttributes().getProductProfile().getPrice(),
//                item.getAttachment().getAttributes().getProductProfile().getUrl(),
//                item.getAttachment().getAttributes().getProductProfile().getImageUrl(),
//                !item.isOpposite(),
//                item.getMsg()
//        );
//
//        list.add(productAttachment);
//    }
//
//    private void setOpponentViewModel(ChatRoomViewModel chatRoomViewModel, List<Contact>
//            contacts) {
//
//        for (Contact contact : contacts) {
//
//            if (contact.getUserId() != 0
//                    && !String.valueOf(contact.getUserId()).equals(userSession.getUserId())
//                    && contact.isInterlocutor()) {
//                if (!TextUtils.isEmpty(contact.getAttributes().getName())) {
//                    chatRoomViewModel.setNameHeader(contact.getAttributes().getName());
//                }
//                if (!TextUtils.isEmpty(contact.getAttributes().getThumbnail())) {
//                    chatRoomViewModel.setImageHeader(contact.getAttributes().getThumbnail());
//                }
//                if (!TextUtils.isEmpty(contact.getRole())) {
//                    if (contact.getRole().equalsIgnoreCase(USER_ROLE)) {
//                        chatRoomViewModel.setInterlocutorRole(contact.getRole());
//                        chatRoomViewModel.setInterlocutorId(String.valueOf(contact.getUserId()));
//                    } else if (contact.getRole().equalsIgnoreCase(SHOP_ADMIN_ROLE) || contact.getRole().equalsIgnoreCase(SHOP_OWNER_ROLE)) {
//                        chatRoomViewModel.setInterlocutorRole(CLIENT_SHOP_ROLE);
//                        chatRoomViewModel.setInterlocutorId(String.valueOf(contact.getShopId()));
//                    }
//                }
//
//
//            }
//        }
//    }
//}
