package com.tokopedia.topchat.common.chat.api

import com.google.gson.JsonObject
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.data.model.response.DataResponse
import retrofit2.http.POST
import com.tokopedia.topchat.chatlist.view.uimodel.DeleteChatListUiModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import rx.Observable

/**
 * Created by stevenfredian on 8/31/17.
 */
interface ChatApi {
    @Headers("Content-Type: application/json")
    @POST(TkpdBaseURL.Chat.DELETE)
    fun deleteMessage(@Body parameters: JsonObject?): Observable<Response<DataResponse<DeleteChatListUiModel>>>
}