package com.tokopedia.chat_common.domain.mapper

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.chat_common.domain.pojo.ReplyChatItemPojo
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author : Steven 08/01/19
 */
class ReplyChatMapper @Inject constructor() : Func1<Response<DataResponse<ReplyChatItemPojo>>, ReplyChatViewModel> {
    override fun call(response: Response<DataResponse<ReplyChatItemPojo>>): ReplyChatViewModel {

        if ((response.body() != null) && (response.body().header == null ||
                        (response.body().header != null && response.body().header.messages.isEmpty()) ||
                        (response.body().header != null && response.body().header.messages[0].isBlank()))) {
            val pojo: ReplyChatItemPojo = response.body().data
            return map(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    open fun map(pojo: ReplyChatItemPojo): ReplyChatViewModel {
        var temp = pojo.chatItemPojo
        var viewModel = MessageViewModel(
                temp.msgId.toString(),
                temp.senderId,
                temp.senderName,
                temp.role,
                temp.attachmentId.toString(),
                temp.attachment?.type.toString(),
                temp.replyTime,
                "",
                temp.msg,
                false,
                false,
                true
        )
        return ReplyChatViewModel(viewModel, pojo.isSuccess)
    }
}