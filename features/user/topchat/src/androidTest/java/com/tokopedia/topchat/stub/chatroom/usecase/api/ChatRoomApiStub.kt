package com.tokopedia.topchat.stub.chatroom.usecase.api

import com.tokopedia.chat_common.domain.pojo.ReplyChatItemPojo
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

class ChatRoomApiStub @Inject constructor() : ChatRoomApi {

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

    override fun getTemplate(
            parameters: HashMap<String, Any>
    ): Observable<Response<DataResponse<TemplateData>>> {
        val dataResponse = DataResponse<TemplateData>().apply {
            data = templateResponse
        }
        return Observable.just(Response.success(dataResponse))
    }
}