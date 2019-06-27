package com.tokopedia.chat_common.domain.mapper

import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.view.viewmodel.ChatRoomViewModel
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.network.exception.MessageErrorException
import retrofit2.Response
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author : Steven 30/11/18
 */

class GetChatMapper @Inject constructor() : Func1<Response<DataResponse<ChatItemPojo>>,
        ChatRoomViewModel> {
    override fun call(response: Response<DataResponse<ChatItemPojo>>): ChatRoomViewModel {
        if ((response.body() != null) && (response.body().header == null ||
                        (response.body().header != null && response.body().header.messages.isEmpty()) ||
                        (response.body().header != null && response.body().header.messages[0].isBlank()))) {
            val pojo: ChatItemPojo = response.body().data
            return mapTo(pojo)
        } else {
            throw MessageErrorException(response.body().header.messages[0])
        }
    }

    private fun mapTo(pojo: ChatItemPojo): ChatRoomViewModel {
        return ChatRoomViewModel()
    }

}