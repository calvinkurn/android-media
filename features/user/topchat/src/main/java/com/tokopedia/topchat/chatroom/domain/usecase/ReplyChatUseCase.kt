package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.chat_common.data.ReplyChatViewModel
import com.tokopedia.chat_common.domain.mapper.ReplyChatMapper
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author : Steven 08/01/19
 */
class ReplyChatUseCase @Inject constructor(
        val api: ChatRoomApi,
        val replyChatMapper: ReplyChatMapper
) : UseCase<ReplyChatViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<ReplyChatViewModel>? {
        return api.reply(requestParams = requestParams.parameters).map(replyChatMapper)
    }


    companion object {

        private val PARAM_MESSAGE_ID: String = "msg_id"
        private val PARAM_PAGE: String = "page"

        fun generateParam(messageId: String, messageReply: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString("msg_id", messageId)
            requestParams.putString("message_reply", messageReply)
            return requestParams
        }

        fun generateParamAttachImage(messageId: String, filePath: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString("msg_id", messageId)
            requestParams.putString("message_reply", "Uploaded Image")
            requestParams.putString("file_path", filePath)
            requestParams.putInt("attachment_type", 2)
            return requestParams
        }
    }
}