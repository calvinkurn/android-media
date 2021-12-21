package com.tokopedia.topchat.common.chat.api

import com.tokopedia.network.constant.TkpdBaseURL
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateDataWrapper
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ChatApiKt {
    @GET(TkpdBaseURL.Chat.GET_TEMPLATE)
    suspend fun getTemplateSuspend(@QueryMap parameters: Map<String, Boolean>): TemplateDataWrapper
}