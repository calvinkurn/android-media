package com.tokopedia.chat_common.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.domain.pojo.ReplyChatItemPojo
import com.tokopedia.chat_common.domain.pojo.imageupload.ImageUploadAttributes
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author : Steven 08/01/19
 */
class ReplyChatMapper @Inject constructor() : Func1<Response<DataResponse<ReplyChatItemPojo>>, ReplyChatViewModel> {
    override fun call(response: Response<DataResponse<ReplyChatItemPojo>>): ReplyChatViewModel {
        val body = response.body()
        if (body != null) {
            if ((body.header == null ||
                            (body.header != null && body.header.messages.isEmpty()) ||
                            (body.header != null && body.header.messages[0].isBlank()))) {
                val pojo: ReplyChatItemPojo = body.data
                return map(pojo)
            } else {
                throw MessageErrorException(body.header.messages[0])
            }
        } else {
            throw MessageErrorException("")
        }
    }

    fun map(pojo: ReplyChatItemPojo): ReplyChatViewModel {

        return when (pojo.chatItemPojo.attachment?.attributes) {
            null -> ReplyChatViewModel(generateMessage(pojo.chatItemPojo), pojo.isSuccess)
            else -> ReplyChatViewModel(generateImageMessage(pojo.chatItemPojo), pojo.isSuccess)
        }
    }

    private fun generateMessage(temp: ChatItemPojo): MessageViewModel {
        var viewModel = MessageViewModel(
                temp.msgId.toString(),
                temp.senderId,
                temp.senderName,
                temp.role,
                temp.attachmentId.toString(),
                temp.attachment?.type.toString(),
                System.currentTimeMillis().toString(),
                "",
                temp.msg,
                false,
                false,
                true
        )
        return viewModel
    }

    private fun generateImageMessage(temp: ChatItemPojo): ImageUploadViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ImageUploadAttributes>( temp.attachment?.attributes,
                ImageUploadAttributes::class.java)
        var viewModel = ImageUploadViewModel(
                temp.msgId.toString(),
                temp.senderId,
                temp.senderName,
                temp.role,
                temp.attachmentId.toString(),
                temp.attachment?.type.toString(),
                System.currentTimeMillis().toString(),
                !temp.isOpposite,
                pojoAttribute.imageUrl,
                pojoAttribute.thumbnail,
                temp.messageIsRead,
                temp.msg
        )
        return viewModel
    }
}