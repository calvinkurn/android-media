package com.tokopedia.topchat.chattemplate.data.repository

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.data.source.CloudGetTemplateChatDataSource
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import javax.inject.Inject

class TemplateRepositoryImpl @Inject constructor(
    private val cloudGetTemplateChatDataSource: CloudGetTemplateChatDataSource
): TemplateRepository {

    override suspend fun getTemplateSuspend(parameters: Map<String, Boolean>): TemplateData {
        return cloudGetTemplateChatDataSource.getTemplateSuspend(parameters)
    }

    override suspend fun setAvailabilityTemplate(parameters: JsonObject, isSeller: Boolean): TemplateData {
        return cloudGetTemplateChatDataSource.setTemplate(parameters, isSeller)
    }

}