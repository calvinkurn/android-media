package com.tokopedia.topchat.revamp.data.api

import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
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

    @GET(ChatUrl.GET_REPLY)
    fun getReply(@Path("msgId") messageId: String, @QueryMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<ChatItemPojo>>>

    @FormUrlEncoded
    @Headers("Cookie:_SID_TOKOPEDIA_")
    @POST(ChatUrl.REPLY)
    fun reply(@FieldMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<ReplyChatItemPojo>>>

    @GET(ChatUrl.GET_TEMPLATE)
    fun getTemplate(@QueryMap parameters: HashMap<String, Any>): Observable<Response<DataResponse<TemplateData>>>

}
