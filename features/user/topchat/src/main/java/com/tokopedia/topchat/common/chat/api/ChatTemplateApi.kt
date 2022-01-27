package com.tokopedia.topchat.common.chat.api

import com.google.gson.JsonObject
import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import retrofit2.http.*

@JvmSuppressWildcards
interface ChatTemplateApi {
    @GET(TkpdBaseURL.Chat.GET_TEMPLATE)
    suspend fun getTemplateSuspend(
        @QueryMap parameters: Map<String, Boolean>
    ): TemplateDataWrapper<TemplateData>

    @Headers("Content-Type: application/json")
    @PUT(TkpdBaseURL.Chat.SET_TEMPLATE)
    suspend fun setTemplate(
        @Body parameters: JsonObject,
        @Query("is_seller") isSeller: Boolean
    ): TemplateDataWrapper<TemplateData>

    @FormUrlEncoded
    @PUT(TkpdBaseURL.Chat.UPDATE_TEMPLATE)
    suspend fun editTemplate(
        @Path("index") index: Int,
        @FieldMap jsonObject: Map<String, Any>,
        @Query("is_seller") isSeller: Boolean
    ): TemplateDataWrapper<TemplateData>

    @FormUrlEncoded
    @POST(TkpdBaseURL.Chat.CREATE_TEMPLATE)
    suspend fun createTemplate(
        @FieldMap parameters: Map<String, Any>
    ): TemplateDataWrapper<TemplateData>

    @HTTP(
        method = "DELETE",
        path = TkpdBaseURL.Chat.DELETE_TEMPLATE + "/{is_seller}",
        hasBody = true
    )
    suspend fun deleteTemplate(
        @Path("index") index: Int,
        @Path("is_seller") isSeller: Boolean,
        @Body parameters: JsonObject
    ): TemplateDataWrapper<TemplateData>
}