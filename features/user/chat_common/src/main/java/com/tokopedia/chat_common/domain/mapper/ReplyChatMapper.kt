package com.tokopedia.chat_common.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
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
            null, "" -> ReplyChatViewModel(generateMessage(pojo.chatItemPojo), pojo.isSuccess)
            else -> ReplyChatViewModel(generateImageMessage(pojo.chatItemPojo), pojo.isSuccess)
        }
    }

    private fun generateMessage(reply: ChatItemPojo): MessageUiModel {
        return MessageUiModel.Builder().withResponseFromAPI(reply)
            .build()
    }

    private fun generateImageMessage(temp: ChatItemPojo): ImageUploadUiModel {
        val pojoAttribute = GsonBuilder().create().fromJson(
            temp.attachment?.attributes, ImageUploadAttributes::class.java
        )
        return ImageUploadUiModel.Builder()
            .withResponseFromAPI(temp)
            .withIsSender(!temp.isOpposite)
            .withIsRead(temp.messageIsRead)
            .withImageUrl(pojoAttribute.imageUrl)
            .withImageUrlThumbnail(pojoAttribute.thumbnail)
            .build()
    }
}