package com.tokopedia.topchat.chattemplate.data.source

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.chat.api.ChatApiKt
import javax.inject.Inject

class CloudEditTemplateChatDataSourceKt @Inject constructor(private val chatApiKt: ChatApiKt) {

    suspend fun editTemplate(
        index: Int,
        parameters: Map<String, Any>,
        isSeller: Boolean
    ): TemplateData {
        return chatApiKt.editTemplate(index, parameters, isSeller).data
    }

    suspend fun createTemplate(parameters: Map<String, Any>): TemplateData {
        return chatApiKt.createTemplate(parameters).data
    }

    suspend fun deleteTemplate(index: Int, isSeller: Boolean): TemplateData {
        return chatApiKt.deleteTemplate(index, isSeller, JsonObject()).data

    }
}