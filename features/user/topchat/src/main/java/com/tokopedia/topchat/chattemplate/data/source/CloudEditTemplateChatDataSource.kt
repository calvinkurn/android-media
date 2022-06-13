package com.tokopedia.topchat.chattemplate.data.source

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.chat.api.ChatTemplateApi
import javax.inject.Inject

class CloudEditTemplateChatDataSource @Inject constructor(private val chatTemplateApi: ChatTemplateApi) {

    suspend fun editTemplate(
        index: Int,
        parameters: Map<String, Any>,
        isSeller: Boolean
    ): TemplateData {
        return chatTemplateApi.editTemplate(index, parameters, isSeller).data
    }

    suspend fun createTemplate(parameters: Map<String, Any>): TemplateData {
        return chatTemplateApi.createTemplate(parameters).data
    }

    suspend fun deleteTemplate(index: Int, isSeller: Boolean): TemplateData {
        return chatTemplateApi.deleteTemplate(index, isSeller, JsonObject()).data

    }
}