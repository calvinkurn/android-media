package com.tokopedia.chat_common.domain

import com.tokopedia.chat_common.data.api.ChatRoomApi
import com.tokopedia.chat_common.domain.mapper.GetChatMapper
import com.tokopedia.chat_common.view.viewmodel.ChatRoomViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject

/**
 * @author : Steven 30/11/18
 */

class GetChatUseCase @Inject constructor(val api: ChatRoomApi, val mapper: GetChatMapper)
    : UseCase<ChatRoomViewModel>() {
    
    override fun createObservable(requestParams: RequestParams): Observable<ChatRoomViewModel> {
            return api.getReply(requestParams.parameters
                    .get("msg_id").toString(), requestParams.parameters)
                    .map(mapper)
                    
    }

    companion object {

        val PARAM_GET_ALL = "GET_ALL"

        fun generateParam(messageId: String, page: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString("msg_id", messageId)
            requestParams.putString("page", page.toString())
            requestParams.putString("platform", "android")
            requestParams.putString("per_page", "10")
            return requestParams
        }

        fun generateParamSearch(messageId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString("msg_id", messageId)
            requestParams.putString("page", "0")
            requestParams.putString("platform", "android")
            return requestParams
        }
    }
}