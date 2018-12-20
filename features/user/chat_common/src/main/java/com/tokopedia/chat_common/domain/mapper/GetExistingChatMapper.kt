package com.tokopedia.chat_common.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_IMAGE_UPLOAD
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.data.FallbackAttachmentViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.domain.pojo.Contact
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel
import javax.inject.Inject

/**
 * @author by nisie on 14/12/18.
 */
open class GetExistingChatMapper @Inject constructor() {

    open fun map(pojo: GetExistingChatPojo): ChatroomViewModel {

        val listChat = mappingListChat(pojo)
        val headerModel = mappingHeaderModel(pojo)
        val canLoadMore = pojo.chatReplies.hasNext
        return ChatroomViewModel(listChat, headerModel, canLoadMore)

    }

    private fun mappingHeaderModel(pojo: GetExistingChatPojo): ChatRoomHeaderViewModel {
        var interlocutor = Contact()

        for (contact in pojo.chatReplies.contacts) {
            if (contact.isInterlocutor) {
                interlocutor = contact
                break
            }
        }

        return ChatRoomHeaderViewModel(
                interlocutor.name,
                interlocutor.tag,
                interlocutor.userId.toString(),
                interlocutor.role,
                ChatRoomHeaderViewModel.Companion.MODE_DEFAULT_GET_CHAT,
                "",
                interlocutor.thumbnail,
                interlocutor.status.timestamp,
                interlocutor.status.isOnline
        )
    }

    private fun mappingListChat(pojo: GetExistingChatPojo): ArrayList<Visitable<*>> {
        val listChat: ArrayList<Visitable<*>> = ArrayList()

        for (chatItemPojo in pojo.chatReplies.list) {
            val date = chatItemPojo.date
            for (chatItemPojoByDate in chatItemPojo.chats) {
                val time = chatItemPojoByDate.time
                for (chatItemPojoByDateByTime in chatItemPojoByDate.replies) {
                    if (hasAttachment(chatItemPojoByDateByTime)) {
                        listChat.add(mapAttachment(chatItemPojoByDateByTime))
                    } else {
                        listChat.add(convertToMessageViewModel(chatItemPojoByDateByTime)
                        )
                    }
                }
            }
        }
        return listChat
    }

    private fun convertToMessageViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return MessageViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id.toString(),
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                "",
                chatItemPojoByDateByTime.isRead,
                false,
                chatItemPojoByDateByTime.isOpposite,
                chatItemPojoByDateByTime.msg
        )
    }

    private fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {

//        return when(chatItemPojoByDateByTime.attachment?.type.toString()){
//            TYPE_PRODUCT_ATTACHMENT -> convertToProductAttachment(chatItemPojoByDateByTime)
//            TYPE_IMAGE_UPLOAD -> convertToImageUpload(chatItemPojoByDateByTime)
//            else -> convertToFallBackModel(chatItemPojoByDateByTime)
//        }

        return convertToFallBackModel(chatItemPojoByDateByTime)
    }

    private fun convertToFallBackModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return FallbackAttachmentViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id.toString(),
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                chatItemPojoByDateByTime.msg
        )
    }

//    private fun convertToImageUpload(chatItemPojoByDateByTime: Reply): Visitable<*> {
//        return ImageUploadViewModel(
//                chatItemPojoByDateByTime.msgId.toString(),
//                chatItemPojoByDateByTime.senderId.toString(),
//                chatItemPojoByDateByTime.senderName,
//                chatItemPojoByDateByTime.role,
//                chatItemPojoByDateByTime.attachment?.id.toString(),
//                chatItemPojoByDateByTime.attachment?.type.toString(),
//                chatItemPojoByDateByTime.replyTime,
//                !chatItemPojoByDateByTime.isOpposite,
//                chatItemPojoByDateByTime.attachment?.
//        )
//    }

//    private fun convertToProductAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
//        //TODO
//    }


    private fun hasAttachment(pojo: Reply): Boolean {
        return (pojo.attachment?.type != null && pojo.attachment?.attributes != null)
    }
}