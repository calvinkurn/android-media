package com.tokopedia.topchat.chattemplate.data.source

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.common.chat.api.ChatApiKt
import javax.inject.Inject

class CloudGetTemplateChatDataSourceKt @Inject constructor(private val chatApiKt: ChatApiKt) {

    suspend fun getTemplateSuspend(parameters: Map<String, Boolean>): TemplateData {
        return chatApiKt.getTemplateSuspend(parameters).templateData
    }

    suspend fun setTemplate(parameters: JsonObject, isSeller: Boolean): TemplateData {
        return chatApiKt.setTemplate(parameters, isSeller).templateData
    }
}