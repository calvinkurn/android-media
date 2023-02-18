package com.tokopedia.topchat.stub.chatroom.usecase.api

import com.tokopedia.chat_common.domain.pojo.ReplyChatItemPojo
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import kotlinx.coroutines.delay
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class ChatRoomApiStub @Inject constructor() : ChatRoomApi {

    var delay = 0L
    var templateResponse: TemplateData = TemplateData().apply {
        isIsEnable = false
        isSuccess = false
        templates = listOf()
    }

    override fun reply(
        requestParams: HashMap<String, Any>
    ): Observable<Response<DataResponse<ReplyChatItemPojo>>> {
        // TODO: TBD
        return Observable.just(Response.success(DataResponse()))
    }

    override suspend fun getTemplateSuspend(
        parameters: HashMap<String, Any>
    ): Response<DataResponse<TemplateData>> {
        val dataResponse = DataResponse<TemplateData>().apply {
            data = templateResponse
        }
        val result = Response.success(dataResponse)
        return if (delay > 0) {
            delay(delay)
            result
        } else {
            result
        }
    }
}
