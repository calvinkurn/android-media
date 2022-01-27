package com.tokopedia.topchat.chattemplate.data.source

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.chat.api.ChatTemplateApi
import javax.inject.Inject

class CloudGetTemplateChatDataSource @Inject constructor(val chatTemplateApi: ChatTemplateApi) {

    suspend fun getTemplateSuspend(parameters: Map<String, Boolean>): TemplateData {
        return chatTemplateApi.getTemplateSuspend(parameters).data
    }

    suspend fun setTemplate(parameters: JsonObject, isSeller: Boolean): TemplateData {
        return chatTemplateApi.setTemplate(parameters, isSeller).data
    }
}