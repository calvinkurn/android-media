package com.tokopedia.topchat.common.chat.api

import com.google.gson.JsonObject
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import retrofit2.Response
import retrofit2.http.*
import rx.Observable

interface ChatApiKt {
    @GET(TkpdBaseURL.Chat.GET_TEMPLATE)
    suspend fun getTemplateSuspend(@QueryMap parameters: Map<String, Boolean>): TemplateDataWrapper

    @Headers("Content-Type: application/json")
    @PUT(TkpdBaseURL.Chat.SET_TEMPLATE)
    suspend fun setTemplate(
        @Body parameters: JsonObject,
        @Query("is_seller") isSeller: Boolean
    ): TemplateDataWrapper
}