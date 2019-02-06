package com.tokopedia.topchat.chatroom.data.api

import com.tokopedia.chat_common.domain.pojo.ReplyChatItemPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

/**
 * @author : Steven 30/11/18
 */

interface ChatRoomApi {

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(ChatUrl.REPLY)
    fun reply(@FieldMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<ReplyChatItemPojo>>>

    @GET(ChatUrl.GET_TEMPLATE)
    fun getTemplate(@QueryMap parameters: HashMap<String, Any>): Observable<Response<DataResponse<TemplateData>>>

}
