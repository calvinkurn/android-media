package com.tokopedia.topchat.chattemplate.data.repository

import com.google.gson.JsonObject
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData

interface TemplateRepository {
    suspend fun getTemplateSuspend(parameters: Map<String, Boolean>): TemplateData
    suspend fun setAvailabilityTemplate(parameters: JsonObject, isSeller: Boolean): TemplateData
}