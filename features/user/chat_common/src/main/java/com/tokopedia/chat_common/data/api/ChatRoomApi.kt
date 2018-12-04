package com.tokopedia.chat_common.data.api

import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.network.ChatUrl
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import rx.Observable

/**
 * @author : Steven 30/11/18
 */

interface ChatRoomApi {
//    @GET(ChatUrl.GET_EXISTING_CHAT)
//    fun getExistingChat(@QueryMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @GET(ChatUrl.GET_MESSAGE)
//    fun getMessage(@QueryMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<>>>

    @GET(ChatUrl.GET_REPLY)
    fun getReply(@Path("msgId") messageId: String, @QueryMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<ChatItemPojo>>>

//    @GET(ChatUrl.GET_USER_CONTACT)
//    fun getUserContact(@Path("msgIds") messageIds: String, @QueryMap requestParams: HashMap<String, Any>): Observable<String>
//
//    @FormUrlEncoded
//    @Headers("Cookie:_SID_TOKOPEDIA_")
//    @POST(ChatUrl.REPLY)
//    fun reply(@FieldMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @Headers("Cookie:_SID_TOKOPEDIA_")
//    @GET(ChatUrl.LISTEN_WEBSOCKET)
//    fun listenWebSocket(@QueryMap mapParam: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//
//    @Headers("Cookie:_SID_TOKOPEDIA_")
//    @GET(ChatUrl.SEARCH)
//    fun searchChat(@QueryMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//
//    @Headers("Content-Type: application/json")
//    @POST(ChatUrl.DELETE)
//    fun deleteMessage(@Body parameters: JsonObject): Observable<Response<DataResponse<>>>
//
//    @FormUrlEncoded
//    @Headers("Cookie:_SID_TOKOPEDIA_")
//    @POST(ChatUrl.SEND_MESSAGE)
//    fun sendMessage(@FieldMap requestParams: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @GET(ChatUrl.GET_TOPCHAT_NOTIFICATION)
//    fun getNotification(@QueryMap params: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @GET(ChatUrl.GET_TEMPLATE)
//    fun getTemplate(@QueryMap parameters: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @FormUrlEncoded
//    @PUT(ChatUrl.UPDATE_TEMPLATE)
//    fun editTemplate(@Path("index") index: Int, @FieldMap jsonObject: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @FormUrlEncoded
//    @POST(ChatUrl.CREATE_TEMPLATE)
//    fun createTemplate(@FieldMap parameters: HashMap<String, Any>): Observable<Response<DataResponse<>>>
//
//    @Headers("Content-Type: application/json")
//    @PUT(ChatUrl.SET_TEMPLATE)
//    fun setTemplate(@Body parameters: JsonObject): Observable<Response<DataResponse<>>>
//
//    @HTTP(method = "DELETE", path = ChatUrl.DELETE_TEMPLATE, hasBody = true)
//    fun deleteTemplate(@Path("index") index: Int, @Body `object`: JsonObject): Observable<Response<DataResponse<>>>
}
