package com.tokopedia.topchat.chattemplate.data.repository

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.data.source.CloudGetTemplateChatDataSourceKt
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import javax.inject.Inject

class TemplateRepositoryImplKt @Inject constructor(
    private val cloudGetTemplateChatDataSourceKt: CloudGetTemplateChatDataSourceKt
): TemplateRepositoryKt {

    override suspend fun getTemplateSuspend(parameters: Map<String, Boolean>): TemplateData {
        return cloudGetTemplateChatDataSourceKt.getTemplateSuspend(parameters)
    }

    override suspend fun setAvailabilityTemplate(parameters: JsonObject, isSeller: Boolean): TemplateData {
        return cloudGetTemplateChatDataSourceKt.setTemplate(parameters, isSeller)
    }

}